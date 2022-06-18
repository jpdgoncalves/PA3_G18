package Server;

import Gui.Server.ServerMainFrame;
import Messages.Request;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Server class
 */
public class Server {

    //Priority Blocking Queue for the requests
    public static PriorityBlockingQueue<Request> request_list;
    private final static int ServerSocketPort = 5058;
    public static int getServerSocketPort() {
        return ServerSocketPort;
    }

    /**
     * Adds a request to the request list if the list is not full and returns an error if it is full.
     *
     * @param request request to add to the request_list
     */
    public static void addRequest (Request request){

        if(request_list.size() < 2){
            request_list.add(request);
        } else {
            //TODO - send err message
        }
    }

    public static void main(String[] args) throws IOException { //chain of responsibility to not have like a 1000 diff types threads

        //create the request list
        request_list = new PriorityBlockingQueue<>(2, Comparator.comparingInt(Request::getDeadline));


        /** -------NOTES-------
         *
         * get the connections and the ports
         * 1 server socket to receive requests from the load balancer
         * 1 socket for the monitor (to receive the heartbeat and send info to the monitor)
         */

        try {

            ServerMainFrame gui = new ServerMainFrame();
            gui.setVisible(true);


            ServerSocket serverSocket = new ServerSocket(ServerSocketPort);

            //send to monitor with my port and IP
            Socket socketup = new Socket("127.0.0.1", 5056);
            ObjectOutputStream oos = new ObjectOutputStream(socketup.getOutputStream());
            oos.writeObject(new Request(0,0,0,7,0,0,0,  "127.0.0.1" , ServerSocketPort));


            while(true){
                Socket socket = serverSocket.accept();
                TConnectionHandler thread = new TConnectionHandler(socket, gui);
                System.out.println("connection made !");
                thread.start();
            }
        }catch (Exception e){

        }

    }
}
