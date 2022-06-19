package Client;

import Gui.Client.ClientMainFrame;
import Messages.Request;
import Messages.ServerStateMessage;
import Server.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TServerHandler extends Thread {

    private Socket socket;
    //ObjectOutputStream oos;
    private ObjectInputStream ois;
    private ClientMainFrame gui;

    public TServerHandler(Socket socket, ClientMainFrame gui){
        this.socket = socket;
        this.gui = gui;
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
            return;
        }

        //Server reply message
        if (req.getCode() == 2 || req.getCode() == 3){
            System.out.println("Connection with Server made - receiving server reply :");
            System.out.println(req);
            socket.close();
            //oos.close();
            ois.close();
            gui.removePendingRequest(req.getRequestId());
            gui.addExecutedRequest(req);
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
