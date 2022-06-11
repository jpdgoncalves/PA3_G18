package Server;

import Messages.Request;

import java.io.*;
import java.net.Socket;

public class TMonitorHandlerFromServer extends Thread{
    private Socket monitorSocket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;


    public TMonitorHandlerFromServer(Socket monitorSocket, ObjectOutputStream outMonitor, ObjectInputStream inMonitor){
        this.monitorSocket = monitorSocket;
        this.oos = outMonitor;
        this.ois = inMonitor;
    }

    /**
     * Receives the answer from the server, via TCP/IP socket.
     */
    private void startClient() {
        System.out.println("Monitor Connected");

        try {

            Request request = new Request(1, 2, 3, 4, 5, 6, 7, "127.0.0.1", 8080);

            oos.writeObject(request);

            request = new Request(1, 2, 3, 4, 5, 6, -1, "127.0.0.1", 8080);
            oos.writeObject(request);
            System.out.println("sent end deadline ");

            oos.flush();

            // Exiting from a while loo should be done when a client gives a deadline of -1.
            if(request.getDeadline() == -1)
            {
                System.out.println("Connection closing... : " + monitorSocket);
                monitorSocket.close();
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
            ois.close();
            oos.close();
            monitorSocket.close();

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
