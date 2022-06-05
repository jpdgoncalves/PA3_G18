package Monitor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Monitor {

    public static void main(String[] args) throws IOException
    {
        ServerSocket myserverSocket = new ServerSocket(5056);
        // getting client request
        while (true)
        // running infinite loop
        {
            Socket socket = null;

            try
            {
                // socket object to receive incoming client requests
                socket = myserverSocket.accept();

                System.out.println("A new connection identified : " + socket);
                // obtaining input and out streams
                DataInputStream ournewDataInputstream = new DataInputStream(socket.getInputStream());
                DataOutputStream ournewDataOutputstream = new DataOutputStream(socket.getOutputStream());

                System.out.println("Thread assigned");

                Thread myThread = new TLoadBalancerHandler(socket, ournewDataInputstream, ournewDataOutputstream);
                // starting
                myThread.start();

            }
            catch (Exception e){
                socket.close();
                e.printStackTrace();
            }
        }
    }
}
