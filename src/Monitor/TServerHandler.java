package Monitor;

import java.io.*;
import java.net.Socket;

public class TServerHandler extends Thread{

    final ObjectInputStream ournewDataInputstream;
    final ObjectOutputStream ournewDataOutputstream;
    final Socket mynewSocket;


    // Constructor
    public TServerHandler(Socket mynewSocket, ObjectInputStream ournewDataInputstream, ObjectOutputStream ournewDataOutputstream)
    {
        this.mynewSocket = mynewSocket;
        this.ournewDataInputstream = ournewDataInputstream;
        this.ournewDataOutputstream = ournewDataOutputstream;
    }

    @Override
    public void run()
    {
        String receivedString;
//        while (true)
//        {
            try {
                System.out.println("Connection with server done !");
                receivedString = ournewDataInputstream.readUTF();
                System.out.println("Server sent :: "+ receivedString);
                ournewDataOutputstream.flush();

                if(receivedString.equals("Exit"))
                {
                    System.out.println("Server " + this.mynewSocket + " sends exit...");
                    System.out.println("Connection closing...");
                    this.mynewSocket.close();
                    System.out.println("Closed");
                    return;
                }

//                stringToReturn = "coucou from monitor";
//                ournewDataOutputstream.writeUTF(stringToReturn);

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
