package Monitor;

import Messages.Request;

import java.io.*;
import java.net.Socket;

public class TServerHandler extends Thread{

    final ObjectInputStream ois;
    final ObjectOutputStream oos;
    final Socket serverSocket;


    // Constructor
    public TServerHandler(Socket mynewSocket, ObjectInputStream ournewDataInputstream, ObjectOutputStream ournewDataOutputstream)
    {
        this.serverSocket = mynewSocket;
        this.ois = ournewDataInputstream;
        this.oos = ournewDataOutputstream;
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
                receivedRequest = (Request) ois.readObject();
                System.out.println("received :: request.deadline = "+ receivedRequest.getDeadline());

                oos.flush();

                if(receivedRequest.getDeadline() == -1)
                {
                    System.out.println("Client " + this.serverSocket + " sends exit...");
                    System.out.println("Connection closing...");
                    this.serverSocket.close();
                    this.oos.close();
                    this.ois.close();
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
            this.serverSocket.close();
            this.ois.close();
            this.oos.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
