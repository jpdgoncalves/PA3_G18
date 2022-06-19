package LB;

import Messages.Request;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class LoadBalancer {

    private final static int ServerSocketPort = 22222;

    public static int getServerSocketPort() {
        return ServerSocketPort;
    }

    private static LinkedList <Request> listRequests = new LinkedList<>();

    public LoadBalancer() throws IOException {
    }

    public static void addRequest(Request request) {
        listRequests.add(request);
    }

    public static void main(String[] args) throws IOException, InterruptedException {


        ServerSocket lbServerSocket = new ServerSocket(ServerSocketPort);

        //send to monitor with my port and IP
        Socket socketup = new Socket("127.0.0.1", 5056);
        ObjectOutputStream oos = new ObjectOutputStream(socketup.getOutputStream());
        oos.writeObject(new Request(
                0,0,1,6,
                0,"",0,  "127.0.0.1" , ServerSocketPort));

        while(true){
            Socket socket = lbServerSocket.accept();
            TConnectionHandler thread = new TConnectionHandler(socket);
            thread.start();
        }
    }
}

