package Monitor;

import LB.LoadBalancer;
import Messages.Request;
import Server.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Monitor {
    LinkedList <LoadBalancer> loadBalancers = new LinkedList<>();

    LinkedList <Server> servers = new LinkedList<>();
    private static LinkedList <Request> listRequests = new LinkedList<>();


    public static void addRequest(Request request) {
        listRequests.add(request);
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket serverSocket = new ServerSocket(5056);
        while(true){
            Socket socket = serverSocket.accept();
            System.out.println("connection made !");
            TConnectionHandler thread = new TConnectionHandler(socket);
            thread.start();
        }
    }
}
