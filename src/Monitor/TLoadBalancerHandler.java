package Monitor;

import java.io.*;
import java.net.Socket;

public class TLoadBalancerHandler extends Thread{

    final DataInputStream ournewDataInputstream;
    final DataOutputStream ournewDataOutputstream;
    final Socket mynewSocket;


    // Constructor
    public TLoadBalancerHandler(Socket mynewSocket, DataInputStream ournewDataInputstream, DataOutputStream ournewDataOutputstream)
    {
        this.mynewSocket = mynewSocket;
        this.ournewDataInputstream = ournewDataInputstream;
        this.ournewDataOutputstream = ournewDataOutputstream;
    }

    @Override
    public void run()
    {
        String receivedString;
        String stringToReturn;
        while (true)
        {
            try {
                receivedString = ournewDataInputstream.readUTF();
                System.out.println("received :: "+ receivedString);
                ournewDataOutputstream.writeUTF("Received from LB : " + receivedString);

                if(receivedString.equals("Exit"))
                {
                    System.out.println("Client " + this.mynewSocket + " sends exit...");
                    System.out.println("Connection closing...");
                    this.mynewSocket.close();
                    System.out.println("Closed");
                    break;
                }

                stringToReturn = "coucou from monitor";
                ournewDataOutputstream.writeUTF(stringToReturn);

            } catch (IOException e) {
                System.out.println("Problem inside handler LB !!");
                e.printStackTrace();
            }
        }

        try
        {

            // closing resources
            this.ournewDataInputstream.close();
            this.ournewDataOutputstream.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
