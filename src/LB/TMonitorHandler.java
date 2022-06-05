package LB;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TMonitorHandler extends Thread{
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ServerSocket ss;



    public TMonitorHandler(Socket monitorSocket, ServerSocket ss, ObjectOutputStream outMonitor, ObjectInputStream inMonitor){
        this.socket = monitorSocket;
        this.ss = ss;
        this.out = outMonitor;
        this.in = inMonitor;
    }

    /**
     * Receives the answer from the server, via TCP/IP socket.
     */
    private void startServer() throws ClassNotFoundException {

        int port = 8080;
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(port);

            socket = ss.accept();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Object obj;

        while (true) {
            System.out.println("Server Connected");
            try {
                System.out.println("here");
                in = new ObjectInputStream(socket.getInputStream());

                obj = in.readObject();

                System.out.println("me");
                System.out.println("Obj - " + obj);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }


    }

    /**
     * Closes all communications.
     */
    private void terminateServer(){
        try {
            in.close();
            out.close();
            socket.close();
            ss.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    public void send(String message) {
//        try {
//            out.writeObject(message);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public String read() {
//        String readMessage = null;
//        try {
//            readMessage = (String) in.readObject();
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        return readMessage;
//    }

    @Override
    public void run() {
        try {
            startServer();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        terminateServer();
    }
}
