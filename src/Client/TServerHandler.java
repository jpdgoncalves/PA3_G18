package Client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TServerHandler extends Thread {

    private ServerSocket ss;
    private Socket socket;
    private ObjectInputStream in;
    private DataOutputStream out;

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
        ObjectInputStream is;

        Object obj;

        while (true) {
            System.out.println("Server Connected");
            try {
                System.out.println("here");
                in = new ObjectInputStream(socket.getInputStream());

                obj = (Object) in.readObject();

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



    /**
     * Receives the answer to a request
     */
    /*private void receiveAnswer(){

        while(Client.getStateRequests()){

            // Invoking input stream via getInputStream()
            // method by creating DataInputStream class
            // object


            InputStream inputStream = null;
            String answer;
            try {
                inputStream = socket.getInputStream();

                BufferedInputStream in = new BufferedInputStream(inputStream);

                answer = (String) in.read;

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            System.out.println(answer);





        }


    }*/


    /**
     * The routine that will be done by each THandleCommsServer
     * TODO - there should probably only be one but shhhhhh
     */
    @Override
    public void run() {
        System.out.println("Client to server communication will soon begin!");
        //start server
        try {
            startServer();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        //while (Client.getStateRequests()) {
            //receiveAnswer();
        //}
        //terminate a server
        terminateServer();


    }
}
