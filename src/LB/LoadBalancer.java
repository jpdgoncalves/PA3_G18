package LB;

import Messages.Request;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class LoadBalancer {

    private static LinkedList <Request> listRequests = new LinkedList<>();



    public static void addRequest(Request request) {
        listRequests.add(request);
    }

    public static void main(String[] args) throws IOException {

        /*Socket socket = null;
        try
        {
            // establishing the connection
            socket = new Socket("localhost",5056);
            ObjectOutputStream dos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream dis = new ObjectInputStream(socket.getInputStream());
            System.out.println("connection established");

            Thread monitor = new TMonitorHandler(socket, dos, dis);
            monitor.start();


        }catch(Exception e){
            socket.close();
            e.printStackTrace();
        }
    }*/


        ServerSocket serverSocketServer = new ServerSocket(9090);
        // getting client request
        while (true)
        // running infinite loop
        {
            Socket serverSocketClient = null;

            try {
                serverSocketClient = serverSocketServer.accept();

                /* DIS and DOS for server ! */

                System.out.println("A new connection identified : " + serverSocketClient);
                // obtaining input and out streams
                ObjectInputStream disServer = new ObjectInputStream(serverSocketClient.getInputStream());
                ObjectOutputStream dosServer = new ObjectOutputStream(serverSocketClient.getOutputStream());

                /* Instantiation thread for Server */
                System.out.println("Connection with Server !");
                TClientHandler thcwc = new TClientHandler(serverSocketClient, disServer, dosServer);
                // starting
                thcwc.start();
                thcwc.join();

            } catch (Exception e) {
                serverSocketClient.close();
                e.printStackTrace();
            }
        }
    }

}

