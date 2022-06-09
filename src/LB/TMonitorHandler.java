package LB;

import Messages.Request;

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

                Request request = new Request(1, 2, 3, 4, 5, 6, 7, "127.0.0.1", 8080);

                out.writeObject(request);

                request = new Request(1, 2, 3, 4, 5, 6, -1, "127.0.0.1", 8080);
                out.writeObject(request);
                System.out.println("sent end deadline ");

                out.flush();

                // Exiting from a while loo should be done when a client gives a deadline of -1.
                if(request.getDeadline() == -1)
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
