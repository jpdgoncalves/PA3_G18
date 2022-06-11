package LB;
import Messages.Request;

import java.net.*;
import java.io.*;

public class THandleCommClients extends Thread
{
    final ObjectInputStream in;
    final ObjectOutputStream out;
    final Socket socket;


    // Constructor
    public THandleCommClients(Socket socket, ObjectInputStream in, ObjectOutputStream out)
    {
        this.socket = socket;
        this.in = in;
        this.out = out;
    }

    @Override
    public void run()
    {
        Request receivedRequest;

        while (true)
        {
            try {
                receivedRequest = (Request) in.readObject();
                System.out.println("received :: request.deadline = "+ receivedRequest.getDeadline());
                LoadBalancer.addRequest(receivedRequest);

                out.flush();

                if(receivedRequest.getDeadline() == -1)
                {
                    System.out.println("Client " + this.socket + " sends exit...");
                    System.out.println("Connection closing...");
                    this.socket.close();
                    this.out.close();
                    this.in.close();
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
            this.in.close();
            this.out.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
