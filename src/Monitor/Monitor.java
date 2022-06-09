package Monitor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Monitor {

    public static void main(String[] args) throws IOException
    {
        ServerSocket lbSocketServer = new ServerSocket(5056);
        ServerSocket serverSocketServer = new ServerSocket(5057);
        // getting client request
        while (true)
        // running infinite loop
        {
            Socket lbSocketClient = null;
            Socket serverSocketClient = null;

            try
            {
                // socket object to receive incoming client requests from load balancer
                lbSocketClient = lbSocketServer.accept();

                // socket object to receive incoming client requests from server
                serverSocketClient = serverSocketServer.accept();

                /* DIS and DOS for LB ! */

                System.out.println("A new connection identified : " + lbSocketClient);
                // obtaining input and out streams
                ObjectInputStream ournewDataInputstream = new ObjectInputStream(lbSocketClient.getInputStream());
                ObjectOutputStream ournewDataOutputstream = new ObjectOutputStream(lbSocketClient.getOutputStream());



                /* DIS and DOS for server ! */

                System.out.println("A new connection identified : " + serverSocketClient);
                // obtaining input and out streams
                ObjectInputStream disServer = new ObjectInputStream(serverSocketClient.getInputStream());
                ObjectOutputStream dosServer = new ObjectOutputStream(serverSocketClient.getOutputStream());



                /* Instantiation thread for LB */
                System.out.println("Connection with LB ! ");
                Thread lbThread = new TLoadBalancerHandler(lbSocketClient, ournewDataInputstream, ournewDataOutputstream);
                // starting
                lbThread.start();


                /* Instantiation thread for Server */
                System.out.println("Connection with Server !");
                Thread serverThread = new TServerHandler(serverSocketClient, disServer, dosServer);
                // starting
                serverThread.start();

                lbThread.join();
                serverThread.join();

            }
            catch (Exception e){
                lbSocketClient.close();
                serverSocketClient.close();
                e.printStackTrace();
            }
        }
    }
}
