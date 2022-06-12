package LB;

import Messages.Request;
import Messages.ServerStateMessage;
import Messages.ServerStatus;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class TConnectionHandler extends Thread{

    Socket socket;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    private final String monitorIP = "localhost";
    private final int monitorPort = 5056;


    public TConnectionHandler(Socket socket){
        this.socket = socket;
    }

    public void startConnection() throws IOException, ClassNotFoundException {
        this.ois = new ObjectInputStream(socket.getInputStream());
        this.oos = new ObjectOutputStream(socket.getOutputStream());

        Request req = (Request) ois.readObject();

        //Closing connection
        if(req.getDeadline() == -1)
        {
            System.out.println("Client " + this.socket + " sends exit...");
            System.out.println("Connection closing...");
            this.socket.close();
            this.oos.close();
            this.ois.close();
            System.out.println("Closed");
            return;
        }

        //TODO: check if connection is from client with this code
        if(req.getCode() == 1){
            System.out.println("Connection with Client made !!");

            //open connection with monitor to ask about which server is less occupied
            Socket socketToMonitor = new Socket(monitorIP, monitorPort);
            ObjectOutputStream oosMonitor = new ObjectOutputStream(socketToMonitor.getOutputStream());
            System.out.println(req + " is sending to monitor");
            oosMonitor.writeObject(req);
            System.out.println("request sent to monitor");

            //receives response from monitor
            ObjectInputStream oisMonitor = new ObjectInputStream(socketToMonitor.getInputStream());
            System.out.println("I received an answer from the monitor");
            ServerStateMessage serverState = (ServerStateMessage) oisMonitor.readObject();
            //close connection with monitor
            socketToMonitor.close();
            oosMonitor.close();
            oisMonitor.close();
            System.out.println("Message to montiro sent\n Now openning connection with server");


            //this is the server with less occupation
            ServerStatus serverId = serverState.getServerWithLessOccupation();
            //open connection with less occupied server
            Socket serverSocket = new Socket(serverId.getIp(), serverId.getPort());
            ObjectOutputStream oosServer = new ObjectOutputStream(socketToMonitor.getOutputStream());
            oosServer.writeObject(req);

            //close connection with server
            serverSocket.close();
            oosServer.close();
            System.out.println("server connection finished");


            LoadBalancer.addRequest(req);
            oos.flush();
        }
        //TODO: check if connection is from monitor with this code
        else if (req.getCode() == 4) { //receivess heartbeat
            System.out.println("Connection with Monitor made !!");
            LoadBalancer.addRequest(req);
            oos.flush();
        }


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
