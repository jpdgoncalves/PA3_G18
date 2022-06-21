package LB;

import Messages.Request;
import Messages.ServerStatus;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Load Balancer thread handler
 */
public class TConnectionHandler extends Thread{
    private static volatile int lbRank;
    private static volatile int lbPrimaryPort;
    private final Socket socket;
    private ObjectInputStream ois;
    private final String lbIp;
    private final int lbPort;
    private final String monitorIP;
    private final int monitorPort;

    private static final ConcurrentHashMap<Integer, ServerStatus> incompleteRequestPerServer = new ConcurrentHashMap<>();

    /**
     * TConnectionHandler constructor
     * @param socket client Socket
     */
    public TConnectionHandler(Socket socket, String ip, int port, String mIp, int mPort){
        this.socket = socket;
        lbIp = ip;
        lbPort = port;
        monitorIP = mIp;
        monitorPort = mPort;
    }

    /**
     * Receives a request and treats it accordingly
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void startConnection() throws IOException, ClassNotFoundException {
        this.ois = new ObjectInputStream(socket.getInputStream());

        Request req = (Request) ois.readObject();
        System.out.println("I got code - " + req.getCode());

        //Closing connection
        if(req.getDeadline() == -1)
        {
            System.out.println("Client " + this.socket + " sends exit...");
            System.out.println("Connection closing...");
            this.socket.close();
            this.ois.close();
            System.out.println("Closed");
            return;
        }

        //request from client
        if(req.getCode() == 1){
            System.out.println("Connection with Client made !!");

            //this is the server with less occupation
            ServerStatus serverId = getServerWithLessOccupationForRequests();
            System.out.println("Message to monitor sent\n Now openning connection with server");
            System.out.println("SERVER ID ->" + serverId);
            if (serverId != null) {
                //open connection with less occupied server
                if (lbRank == 1) {
                    req.setServerId(serverId.getId());
                    System.out.println("server to forward to is : " + serverId);
                    sendRequest(serverId.getIp(), serverId.getPort(), req);
                    System.out.println("I finished forwarding my request to Server !");

                    serverId.addIncompleteRequest(req);

                    //open connection with Monitor
                    System.out.println(req + " is sending to monitor");
                    sendRequest(monitorIP, monitorPort, req);
                    System.out.println("request sent to monitor");

                    System.out.println("server connection finished");
                }
            }else{
                System.out.println("No servers available.");
            }

            LoadBalancer.addRequest(req);
        }
        //if Monitor sends this code, it means the server has replied to client
        else if(req.getCode() == 2 || req.getCode() == 3){
            //remove request that has been completed
            incompleteRequestPerServer.get(req.getServerId()).removeIncompleteRequest(req.getRequestId());
        }
        //receives Monitor heartbeat
        else if (req.getCode() == 4) {
            System.out.println("Connection with Monitor made !!");

            //open connection with Monitor
            sendRequest(monitorIP, monitorPort, new Request(
                    0,0,0,5,
                    0,"",0,
                    lbIp, lbPort
            ));
        }
        //when a server is up
        else if (req.getCode() == 7) {
            incompleteRequestPerServer.put(req.getServerId(), new ServerStatus(req.getTarget_IP(), req.getTargetPort(), req.getServerId(), 0, 0));
            System.out.println(incompleteRequestPerServer);
        }
        //when a server goes down
        else if (req.getCode() == 8) {
            ServerStatus status = incompleteRequestPerServer.remove(req.getServerId());
            List<Request> requestIncompleteFromServerDown = status.getIncompleteRequests();
            for (Request request : requestIncompleteFromServerDown){
                ServerStatus serverStatusToSend = getServerWithLessOccupationForRequests();
                request.setServerId(serverStatusToSend.getId());
                serverStatusToSend.addIncompleteRequest(request);

                if (lbRank == 1) {
                    sendRequest(serverStatusToSend.getIp(), serverStatusToSend.getPort(), request);
                    sendRequest(monitorIP, monitorPort, request);
                }
            }
        }

        else if (req.getCode() == 9) {
            if(lbRank == 2){
                LoadBalancer.swapServerSocket(lbPrimaryPort);
                lbRank = 1;
            }
        }
        //Load balancer is primary
        else if (req.getCode() == 10) {
            lbRank = 1;
        }
        //Load balancer is secondary
        else if (req.getCode() == 11) {
            lbRank = 2;
            lbPrimaryPort = req.getTargetPort();
        }
    }

    private void sendRequest(String ip, int port, Request request) {
        Socket socket = null;
        ObjectOutputStream oos  = null;

        try {
            System.out.println("Sending request " + request);

            socket = new Socket(ip, port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(request);

            System.out.println("Sent request " + request);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (oos != null) oos.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ServerStatus getServerWithLessOccupationForRequests(){
        int workload = 1000;
        int selectedServer = -1;
        for (int serverId : incompleteRequestPerServer.keySet()) {
            List<Request> requests = incompleteRequestPerServer.get(serverId).getIncompleteRequests();
            int newWorkload = requests.stream().mapToInt(Request::getNr_iterations).reduce(0, Integer::sum);
            System.out.println(newWorkload);
            if (newWorkload < workload){
                selectedServer = serverId;
                workload = newWorkload;
            }
        }
        return incompleteRequestPerServer.get(selectedServer);
    }

    private ServerStatus getServerWithLessOccupation(ArrayList<ServerStatus> listServers){
        ServerStatus serverIdWithLessOccupation = null;
        int numberRequestServer = 25; //higher than the maximum possible for a server
        for (ServerStatus serverStatus : listServers){
            if (serverStatus.getIncompleteRequests().size() < numberRequestServer) {
                serverIdWithLessOccupation = serverStatus;
                numberRequestServer = serverStatus.getIncompleteRequests().size();
                break;
            }
        }

        return serverIdWithLessOccupation;
    }

    /**
     * Life cycle of the thread
     */
    @Override
    public void run() {
        try {
            startConnection();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
