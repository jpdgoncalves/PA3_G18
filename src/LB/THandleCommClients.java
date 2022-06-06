package LB;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Receives the request made by the client
 */
public class THandleCommClients extends Thread{

    private ServerSocket ss;
    private Socket socket;
    private ObjectInputStream in;
    private DataOutputStream out;

    /**
     * Receives the requests from the client, via TCP/IP socket.
     */
    private void startServer() throws ClassNotFoundException {

        int port = 9090;
        ServerSocket ss = null;

        //open server and accept connections
        try {
            ss = new ServerSocket(port);

            socket = ss.accept();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ObjectInputStream is;

        Object obj;

        while (true) {
            System.out.println("Server Connected");
            try {
                System.out.println("here");
                in = new ObjectInputStream(socket.getInputStream());

                //receive a request
                obj = (Object) in.readObject();
                System.out.println("me");
                System.out.println("Obj - " + obj);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }

    /**
     * Closes all communications.
     */
    private void terminateServer(){
        try {
            in.close();
            out.close();
            socket.close();
            ss.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The routine that will be done by each THandleCommClients
     * TODO - there should probably only be one but shhhhhh
     */
    @Override
    public void run() {
        System.out.println("LB to client communication will soon begin!");

        //start server and accept requests
        try {
            startServer();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        //terminate a server
        terminateServer();

    }
}


