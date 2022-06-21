package Client;

import Gui.Client.ClientMainFrame;
import Messages.Request;

import java.io.*;
import java.net.Socket;

/**
 * Server handler thread
 */
public class TServerHandler extends Thread {

    private Socket socket;
    private ObjectInputStream ois;
    private ClientMainFrame gui;

    /**
     * Constructor
     * @param socket Load Balancer socket
     * @param gui Client GUI
     */
    public TServerHandler(Socket socket, ClientMainFrame gui){
        this.socket = socket;
        this.gui = gui;
        setDaemon(true);
    }

    /**
     * Client start connection with entities
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
            return;
        }

        //Server reply message
        if (req.getCode() == 2 || req.getCode() == 3){
            System.out.println("Connection with Server made - receiving server reply :");
            System.out.println(req);
            socket.close();
            ois.close();
            gui.removePendingRequest(req.getRequestId());
            gui.addExecutedRequest(req);
        }
    }

    /**
     * Thread routine
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
