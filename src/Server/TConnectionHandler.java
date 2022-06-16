package Server;

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

    public TConnectionHandler(Socket socket){
        this.socket = socket;
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
            return;
        }

        //Client request connection
        if(req.getCode() == 1){
            System.out.println("Connection with LB made - receiving client request :");
            System.out.println(req);
            Server.addRequest(req);
            Socket clientSocket = new Socket(req.getTarget_IP(), req.getTargetPort());
            ObjectOutputStream oosClient = new ObjectOutputStream(clientSocket.getOutputStream());
            //TODO: change reply with the real serverID and chnage pi for a double
            Request reply = new Request(req.getClientId(), req.getRequestId(), 2020, 02, req.getNr_iterations(), 3, req.getDeadline(), req.getTarget_IP(), req.getTargetPort());
            oosClient.writeObject(reply);

            oosClient.close();
            clientSocket.close();

            //oos.flush();
        }

        //LB up connection
        if(req.getCode() == 6){
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
        }
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
