package LB;

import Messages.Request;
import Messages.ServerStatus;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

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

            //open connection with Monitor
            Socket socketToMonitor = new Socket(monitorIP, monitorPort);
            ObjectOutputStream oosMonitor = new ObjectOutputStream(socketToMonitor.getOutputStream());
            ObjectInputStream oisMonitor = new ObjectInputStream(socketToMonitor.getInputStream());

            System.out.println(req + " is sending to monitor");
            oosMonitor.writeObject(req);
            System.out.println("request sent to monitor");

            //receives response from monitor
            ArrayList serverState = (ArrayList) oisMonitor.readObject();

            //TODO: make the monitor inform about if there already is a LB running (in this case, the second one will be the secondary)
            System.out.println("I received an answer from the monitor");
            //close connection with monitor
            socketToMonitor.close();
            oosMonitor.close();
            oisMonitor.close();

            //this is the server with less occupation
            ServerStatus serverId = getServerWithLessOccupation(serverState);
            System.out.println("Message to monitor sent\n Now openning connection with server");
            System.out.println("SERVER ID ->" + serverId);
            if (serverId != null) {
                //open connection with less occupied server
                System.out.println("server to forward to is : " + serverId);
                sendRequest(serverId.getIp(), serverId.getPort(), req);
                System.out.println("I finished forwarding my request to Server !");
                System.out.println("server connection finished");
            }else{
                System.out.println("No servers available.");
            }

            LoadBalancer.addRequest(req);
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
        } else if (req.getCode() == 9) {
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
