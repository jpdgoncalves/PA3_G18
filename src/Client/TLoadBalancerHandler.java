package Client;

import Messages.Request;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;

/**
 * Sends a request from the client to the load balancer
 */
public class TLoadBalancerHandler extends Thread{
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private LinkedList<Request> listRequest;


    public TLoadBalancerHandler(Socket monitorSocket, ObjectOutputStream outMonitor, ObjectInputStream inMonitor, LinkedList<Request> listRequest){
        this.socket = monitorSocket;
        this.out = outMonitor;
        this.in = inMonitor;
        this.listRequest = listRequest;
    }

    /**
     * Receives the answer from the server, via TCP/IP socket.
     */
    private void startClient() {
        System.out.println("Monitor Connected");

        try {

            //sends all requests received from the client
            for (int i = 0; i < listRequest.size(); i++) {

                out.writeObject(listRequest.get(i));
            }

            //sends the death signal
            Request request = new Request(
                    1, 2, 3, 4, 5,
                    "", -1, "127.0.0.1", 8080
            );
            out.writeObject(request);

            System.out.println("sent end deadline ");

            out.flush();

            // Exiting from a while loo should be done when a client gives a deadline of -1.
            if(request.getDeadline() == -1)
            {
                System.out.println("Connection closing... : " + socket);
                socket.close();
                System.out.println("Closed");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Closes all communications.
     */
    private void terminateClient(){
        try {
            in.close();
            out.close();
            socket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            startClient();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        terminateClient();
    }
}
