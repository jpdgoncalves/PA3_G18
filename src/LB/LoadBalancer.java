package LB;

import java.io.*;
import java.net.Socket;

public class LoadBalancer {

    public static void main(String[] args) throws IOException
    {
        try
        {
            // establishing the connection
            Socket ournewsocket = new Socket("localhost",5056);
            DataInputStream ournewDataInputstream = new DataInputStream(ournewsocket.getInputStream());
            DataOutputStream ournewDataOutputstream = new DataOutputStream(ournewsocket.getOutputStream());
            // In the following loop, the client and client handle exchange data.
            while (true)
            {
                System.out.println(ournewDataInputstream.readUTF());
                String tosend = "coucou from client";
                ournewDataOutputstream.writeUTF(tosend);
                // Exiting from a while loo should be done when a client gives an exit message.
                if(tosend.equals("Exit"))
                {
                    System.out.println("Connection closing... : " + ournewsocket);
                    ournewsocket.close();
                    System.out.println("Closed");
                    break;
                }

                // printing date or time as requested by client
                String newresuiltReceivedString = ournewDataInputstream.readUTF();
                System.out.println(newresuiltReceivedString);
            }

            ournewDataInputstream.close();
            ournewDataOutputstream.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
