package Server;

import Gui.Server.ServerMainFrame;
import Messages.Request;
import Messages.ServerStateMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TConnectionHandler extends Thread{
    Socket socket;
    //ObjectOutputStream oos;
    ObjectInputStream ois;

    ServerMainFrame gui;

    private final String serverIp;
    private final int serverPort;
    private final String monitorIP;
    private final int monitorPort;

    public TConnectionHandler(Socket socket, ServerMainFrame gui, String ip, int port, String mIp, int mPort){
        this.socket = socket;
        this.gui = gui;

        serverIp = ip;
        serverPort = port;
        monitorIP = mIp;
        monitorPort = mPort;

        setDaemon(true);
    }

    private void startConnection() throws IOException, ClassNotFoundException {
        this.ois = new ObjectInputStream(socket.getInputStream());
        //this.oos = new ObjectOutputStream(socket.getOutputStream());

        Request req = (Request) ois.readObject();
        System.out.println("I got a request - " + req.getCode());

        //Closing connection
        if(req.getDeadline() == -1)
        {
            System.out.println("Client " + this.socket + " sends exit...");
            System.out.println("Connection closing...");
            this.socket.close();
            //this.oos.close();
            this.ois.close();
            System.out.println("Closed");
            gui.addProcessedRequest(req);
            gui.removeReceivedRequest(req.getRequestId());
            return;
        }

        //Client request connection
        if(req.getCode() == 1){
            System.out.println("Connection with LB made - receiving client request :");
            System.out.println(req);

            gui.addReceivedRequest(req);
            Request reply = new Request(
                    req.getClientId(), req.getRequestId(), 2020,
                    02, req.getNr_iterations(), getPi(req.getNr_iterations(), 5000), req.getDeadline(),
                    req.getTarget_IP(), req.getTargetPort()
            );
            Socket clientSocket = new Socket(req.getTarget_IP(), req.getTargetPort());
            ObjectOutputStream oosClient = new ObjectOutputStream(clientSocket.getOutputStream());
            //TODO: change reply with the real serverID and change pi for a double
            oosClient.writeObject(reply);
            System.out.println("Reply sent to Client");

            gui.addProcessedRequest(req);
            gui.removeReceivedRequest(req.getRequestId());

            oosClient.close();
            clientSocket.close();
        }

         else if (req.getCode() == 4) { //receives heartbeat
            System.out.println("Connection with Monitor made !!");

            //send to monitor with my port and IP
            Socket socketToMonitor = new Socket(monitorIP, monitorPort);
            ObjectOutputStream oos = new ObjectOutputStream(socketToMonitor.getOutputStream());
            oos.writeObject(new Request(
                    0,0,0,5,
                    0,"",0,  serverIp, serverPort
            ));

            //close connection with monitor
            socketToMonitor.close();
            oos.flush();
            oos.close();

        }

        //LB up connection
        /*if(req.getCode() == 6){
            System.out.println("Connection with LB made - LB up!!");
            Server.addRequest(req);
            System.out.println(req);
            //oos.flush();
        }


        //Server up connection
        if (req.getCode() == 7) {
            System.out.println("Connection with Server made - server up!!");
            Server.addRequest(req);
            System.out.println(req);
            //oos.flush();
        }

        //Heartbeat reply
        if(req.getCode() == 5){
            System.out.println("Heartbeat received!!");
            Server.addRequest(req);
            System.out.println(req);
            //oos.flush();
        }*/
    }

    /**
     * Creates the value of pi requested and sleeps according to the time per iteration.s
     *
     * @param nr_iterations number of iterations
     * @param timePerIteration time per each iteration
     * @return returns the value of pi according to the number of iterations
     */
    private String getPi(int nr_iterations, int timePerIteration){

        try {
            Thread.sleep(timePerIteration * nr_iterations);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return "3.1415926589793".substring(0, 2 + nr_iterations);
    }

    private void closeConnections() throws IOException {
//        serverSocket.close();
        //oos.close();
        ois.close();
    }

    @Override
    public void run() {
        try {
            startConnection();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
