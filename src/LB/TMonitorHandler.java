package LB;

import java.io.*;
import java.net.Socket;

public class TMonitorHandler extends Thread{
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;


    public TMonitorHandler(Socket monitorSocket, ObjectOutputStream outMonitor, ObjectInputStream inMonitor){
        this.socket = monitorSocket;
        this.out = outMonitor;
        this.in = inMonitor;
    }

    /**
     * Receives the answer from the server, via TCP/IP socket.
     */
    private void startClient() {
            System.out.println("Monitor Connected");

            try {
                String tosend = "coucou from client";
                out.writeUTF(tosend);
                tosend = "Exit";
                out.writeUTF(tosend);
                out.flush();

                // Exiting from a while loo should be done when a client gives an exit message.
                if(tosend.equals("Exit"))
                {
                    System.out.println("Connection closing... : " + socket);
                    socket.close();
                    System.out.println("Closed");
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }



    }

    /**
     * Closes all communications.
     */
    private void terminateClient(){
        try {
            in.close();
            out.close();
            socket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            startClient();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        terminateClient();
    }
}
