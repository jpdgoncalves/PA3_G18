package Monitor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TServerHandler extends Thread{

    final DataInputStream ournewDataInputstream;
    final DataOutputStream ournewDataOutputstream;
    final Socket mynewSocket;


    // Constructor
    public TServerHandler(Socket mynewSocket, DataInputStream ournewDataInputstream, DataOutputStream ournewDataOutputstream)
    {
        this.mynewSocket = mynewSocket;
        this.ournewDataInputstream = ournewDataInputstream;
        this.ournewDataOutputstream = ournewDataOutputstream;
    }

    @Override
    public void run()
    {
        String receivedString;
        while (true)
        {
            try {
                System.out.println("Connection with server done !");
                receivedString = ournewDataInputstream.readUTF();
                System.out.println("Server sent :: "+ receivedString);

                if(receivedString.equals("Exit"))
                {
                    System.out.println("Server " + this.mynewSocket + " sends exit...");
                    System.out.println("Connection closing...");
                    this.mynewSocket.close();
                    System.out.println("Closed");
                    break;
                }

//                stringToReturn = "coucou from monitor";
//                ournewDataOutputstream.writeUTF(stringToReturn);

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
