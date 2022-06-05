package LB;

import java.io.*;
import java.net.Socket;

public class LoadBalancer {

    public static void main(String[] args) throws IOException
    {
        Socket ournewsocket = null;
        try
        {
            // establishing the connection
            ournewsocket = new Socket("localhost",5056);
            System.out.println("connection established");
            DataInputStream ournewDataInputstream = new DataInputStream(ournewsocket.getInputStream());
            DataOutputStream ournewDataOutputstream = new DataOutputStream(ournewsocket.getOutputStream());
            // In the following loop, the client and client handle exchange data.
            while (true)
            {
//                System.out.println(ournewDataInputstream.readUTF());
                Thread monitor = new TMonitorHandler(ournewsocket, ournewDataOutputstream, ournewDataInputstream);
                monitor.start();

            }

        }catch(Exception e){
            ournewsocket.close();
            e.printStackTrace();
        }
    }
}
