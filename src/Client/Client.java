package Client;

import Messages.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

/**
 * Client class
 */
public class Client {

    /**
     * Tasks to be done by a client
     */
    public static void main(String[] args) {

        int nr_requests_to_do = 2;

        //creates requests
        Request request = new Request(1, 1, 1, 01, 2, 0, 2, "127.0.0.1", 5000);
        LinkedList<Request> listRequest = new LinkedList<>();
        listRequest.add(request);
        listRequest.add(request);

        //TMonitorHandlerFromServer block of code
        System.out.println("Connection with monitor");
        Socket serverSocket = null;
        try {
            serverSocket = new Socket("localhost", 9090);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            ObjectOutputStream dosServer = new ObjectOutputStream(serverSocket.getOutputStream());
            ObjectInputStream disServer = new ObjectInputStream(serverSocket.getInputStream());
            System.out.println("Starting thread !");

            //starts the threads
            TLoadBalancerHandler tserver = new TLoadBalancerHandler(serverSocket, dosServer, disServer, listRequest);
            System.out.println("Thread linked to Monitor");
            tserver.start();
            tserver.join();

        }catch (Exception e){

        }

    }

}
