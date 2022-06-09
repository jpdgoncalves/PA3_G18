package Monitor;

import Messages.Request;

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
        Request receivedRequest = null;
        int i=0;
        while (true)
        {
            System.out.println("loop number : " + i);
            try {
                receivedRequest = (Request) ournewDataInputstream.readObject();
                System.out.println("received :: request.deadline = "+ receivedRequest.getDeadline());

                ournewDataOutputstream.flush();

                if(receivedRequest.getDeadline() == -1)
                {
                    System.out.println("Client " + this.mynewSocket + " sends exit...");
                    System.out.println("Connection closing...");
                    this.mynewSocket.close();
                    this.ournewDataOutputstream.close();
                    this.ournewDataInputstream.close();
                    System.out.println("Closed");
                    break;
                }

            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error");
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
