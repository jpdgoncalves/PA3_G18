package LB;

import java.io.*;
import java.net.Socket;

public class LoadBalancer {

    public static void main(String[] args) throws IOException
    {
        Socket socket = null;
        try
        {
            // establishing the connection
            socket = new Socket("localhost",5056);
            System.out.println("connection established");
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            Thread monitor = new TMonitorHandler(socket, dos, dis);
            monitor.start();


        }catch(Exception e){
            socket.close();
            e.printStackTrace();
        }
    }
}
