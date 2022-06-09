package Monitor;

import Messages.Request;

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
//        String receivedString;
        Request receivedRequest = null;
//        while (true)
//        {
            try {
                receivedRequest = (Request) ournewDataInputstream.readObject();
                System.out.println("received :: request.deadline = "+ receivedRequest.getDeadline());
//                ournewDataOutputstream.writeUTF("Received from LB : " + receivedString);
//                stringToReturn = "coucou from monitor";
//                ournewDataOutputstream.writeUTF(stringToReturn);

                ournewDataOutputstream.flush();

//                if(receivedString.equals("Exit"))
//                {
//                    System.out.println("Client " + this.mynewSocket + " sends exit...");
//                    System.out.println("Connection closing...");
//                    this.mynewSocket.close();
//                    this.ournewDataOutputstream.close();
//                    this.ournewDataInputstream.close();
//                    System.out.println("Closed");
//                    break;
//                }
                return;

            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error");
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
