package Client;

import Messages.Request;
import Messages.ServerStateMessage;
import Server.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TServerHandler extends Thread {

    Socket socket;
    //ObjectOutputStream oos;
    ObjectInputStream ois;

    public TServerHandler(Socket socket){
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

        //Server reply message
        if(req.getCode() == 2){
            System.out.println("Connection with Server made - receiving server reply :");
            System.out.println(req);
            Client.addRequest(req);
            socket.close();
            //oos.close();
            ois.close();

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
