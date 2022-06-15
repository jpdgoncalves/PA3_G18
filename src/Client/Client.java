package Client;

import Messages.Request;
import Server.TConnectionHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

/**
 * Client class
 */
public class Client {

    private static LinkedList <Request> listRequests = new LinkedList<>();


    public static void addRequest(Request request) {
        listRequests.add(request);
    }

    /**
     * Tasks to be done by a client
     */
    public static void main(String[] args) throws IOException {

        int nr_requests_to_do = 2;

        //creates requests
        Request request = new Request(1, 1, 1, 01, 2, 0, 2, "127.0.0.1", 5055);
        LinkedList<Request> listRequest = new LinkedList<>();
        listRequest.add(request);
        listRequest.add(request);


        try {
            ServerSocket serverSocket = new ServerSocket(5055);
//            while(true){
                //connection with LB
                Socket lbSocket = new Socket("localhost", 22222);
                ObjectOutputStream oos = new ObjectOutputStream(lbSocket.getOutputStream());
                oos.writeObject(request);
                System.out.println("Request sent to LB !");
                //create connection with server
                Socket socket = serverSocket.accept();
                TServerHandler thread = new TServerHandler(socket);
                System.out.println("connection made with server!");
                thread.start();
//            }
        }catch (Exception e){

        }

    }

}
