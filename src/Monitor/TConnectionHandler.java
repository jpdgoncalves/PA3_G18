package Monitor;

import Messages.Request;
import Messages.ServerStateMessage;
import Messages.ServerStatus;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TConnectionHandler extends Thread{
    Socket socket;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    ServerStateMessage serverStateMessage;

    public TConnectionHandler(Socket socket){
        this.socket = socket;
    }

    private void startConnection() throws IOException, ClassNotFoundException {
        this.ois = new ObjectInputStream(socket.getInputStream());
        this.oos = new ObjectOutputStream(socket.getOutputStream());

        Request req = (Request) ois.readObject();
        System.out.println("I read a request - " + req.getCode());

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

        //Client request connection
        if(req.getCode() == 1){
            System.out.println("Connection with LB made - receiving client request :");
            System.out.println(req);
            Monitor.addRequest(req);

            ServerStateMessage ssm = new ServerStateMessage();
            ServerStatus serverStatus = new ServerStatus("localhost", 5058, 1);
            ssm.addServer(1, serverStatus);
            System.out.println("ServerStateMessage sends to LB : " + ssm);
            oos.writeObject(ssm);

            oos.flush();
        }

        //LB up connection
        if(req.getCode() == 6){
            System.out.println("Connection with LB made - LB up!!");
            Monitor.addRequest(req);
            System.out.println(req);
            oos.flush();
        }


        //Server up connection
        if (req.getCode() == 7) {
            System.out.println("Connection with Server made - server up!!");
                Monitor.addRequest(req);
                System.out.println(req);
                oos.flush();
        }

        //Heartbeat reply
        if(req.getCode() == 5){
            System.out.println("Heartbeat received!!");
            Monitor.addRequest(req);
            System.out.println(req);
            oos.flush();
        }
    }

    private void closeConnections() throws IOException {
//        serverSocket.close();
        oos.close();
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
