package Server;

import Gui.Server.ServerMainFrame;
import Messages.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * TConnectionHandler - receives requests from the LBs
 */
public class TConnectionHandler extends Thread{
    Socket socket;
    ObjectInputStream ois;

    ServerMainFrame gui;

    private final String serverIp;
    private final int serverPort;
    private final String monitorIP;
    private final int monitorPort;

    /**
     * TConnectionHandler constructor
     * @param socket socket to receive communications
     * @param gui Server GUI
     * @param ip Server IP
     * @param port Server port
     * @param mIp Monitor IP
     * @param mPort Monitor port
     */
    public TConnectionHandler(Socket socket, ServerMainFrame gui, String ip, int port, String mIp, int mPort){
        this.socket = socket;
        this.gui = gui;

        serverIp = ip;
        serverPort = port;
        monitorIP = mIp;
        monitorPort = mPort;

        setDaemon(true);
    }

    /**
     * Handles incoming requests
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void startConnection() throws IOException, ClassNotFoundException {
        this.ois = new ObjectInputStream(socket.getInputStream());

        Request req = (Request) ois.readObject();
        System.out.println("I got a request - " + req.getCode());

        //Closing connection
        if(req.getDeadline() == -1)
        {
            System.out.println("Client " + this.socket + " sends exit...");
            System.out.println("Connection closing...");
            this.socket.close();
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

            Server.addRequest(req);

            /*
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
            */
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
    }


    /**
     * Closes the connections
     * @throws IOException
     */
    private void closeConnections() throws IOException {
//        serverSocket.close();
        //oos.close();
        ois.close();
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
