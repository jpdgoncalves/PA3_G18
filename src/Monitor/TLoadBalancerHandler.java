package Monitor;

import java.io.*;
import java.net.Socket;

public class TLoadBalancerHandler extends Thread{

    final ObjectInputStream ournewDataInputstream;
    final ObjectOutputStream ournewDataOutputstream;
    final Socket mynewSocket;


    // Constructor
    public TLoadBalancerHandler(Socket mynewSocket, ObjectInputStream ournewDataInputstream, ObjectOutputStream ournewDataOutputstream)
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
//        while (true)
//        {
            try {
                receivedString = ournewDataInputstream.readUTF();
                System.out.println("received :: "+ receivedString);
                ournewDataOutputstream.writeUTF("Received from LB : " + receivedString);
//                stringToReturn = "coucou from monitor";
//                ournewDataOutputstream.writeUTF(stringToReturn);

                ournewDataOutputstream.flush();

                if(receivedString.equals("Exit"))
                {
                    System.out.println("Client " + this.mynewSocket + " sends exit...");
                    System.out.println("Connection closing...");
                    this.mynewSocket.close();
                    this.ournewDataOutputstream.close();
                    this.ournewDataInputstream.close();
                    System.out.println("Closed");
                    return;
                }


            } catch (IOException e) {
                System.out.println("Problem inside handler LB !!");
                e.printStackTrace();
            }
//        }

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
