package Client;

import Gui.Client.ClientMainFrame;
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
    private static int requestCounter;

    private static ServerSocket serverSocket;
    private static final ClientMainFrame gui = new ClientMainFrame();


    public static void addRequest(Request request) {
        listRequests.add(request);
    }

    public static void sendRequestToLB(String ip, String port, String nbOfIteration, String deadline){
        Request request = new Request(1, requestCounter++, 1, 01, Integer.parseInt(nbOfIteration), 0, Integer.parseInt(deadline), "localhost", 5055);

        try {
            Socket lbSocket = new Socket(ip, Integer.parseInt(port));
            ObjectOutputStream oos = new ObjectOutputStream(lbSocket.getOutputStream());
            oos.writeObject(request);
            System.out.println("Request sent to LB !");

            //create connection with server
            Socket socket = serverSocket.accept();
            TServerHandler thread = new TServerHandler(socket);
            System.out.println("connection made with server!");
            thread.start();
        }catch(Exception e){
            System.out.println(e);
        }
    }

    /**
     * Tasks to be done by a client
     */
    public static void main(String[] args) throws IOException {

        //creates requests
        Request request = new Request(1, 5, 1, 01, 2, 0, 2, "127.0.0.1", 5055);
        LinkedList<Request> listRequest = new LinkedList<>();
        listRequest.add(request);
        listRequest.add(request);

        try {
            gui.setVisible(true);
            gui.setSendCallback(Client::sendRequestToLB);

            serverSocket = new ServerSocket(5055);

        }catch (Exception e){

        }
    }
}
