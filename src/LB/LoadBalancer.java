package LB;

import Messages.Request;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class LoadBalancer {

    private static LinkedList <Request> listRequests = new LinkedList<>();

    public LoadBalancer() throws IOException {
    }

    public static void addRequest(Request request) {
        listRequests.add(request);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket lbServerSocket = new ServerSocket(22222);
        while(true){
            Socket socket = lbServerSocket.accept();
            TConnectionHandler thread = new TConnectionHandler(socket);
            thread.start();
        }
    }
}

