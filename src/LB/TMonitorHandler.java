package LB;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TMonitorHandler extends Thread{
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;


    public TMonitorHandler(Socket monitorSocket, DataOutputStream outMonitor, DataInputStream inMonitor){
        this.socket = monitorSocket;
        this.out = outMonitor;
        this.in = inMonitor;
    }

    /**
     * Receives the answer from the server, via TCP/IP socket.
     */
    private void startClient() {

        while (true) {
            System.out.println("Server Connected");

            try {
                // read what received from Monitor
//                String received = in.readUTF();
//                System.out.println(received);


                String tosend = "coucou from client";
                out.writeUTF(tosend);
                System.out.println(tosend + " //has been sent");
                out.writeUTF("Exit");

                // Exiting from a while loo should be done when a client gives an exit message.
                if(tosend.equals("Exit"))
                {
                    System.out.println("Connection closing... : " + socket);
                    socket.close();
                    System.out.println("Closed");
                    break;
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

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
