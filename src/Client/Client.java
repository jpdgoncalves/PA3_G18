package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.DoubleToIntFunction;

public class Client {

    private ServerSocket ss;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private int nr_requests_to_do;

    private int nr_requests_sent = 0;

    private Boolean notKilled = true;



    /**
     * Receives the answer from the server, via TCP/IP socket.
     */
    private void startServer() {

        // Try block to check for exceptions
        try {

            // Creating an object of ServerSocket class
            // in the main() method  for socket connection
            ss = new ServerSocket(8080);

            // Establishing a connection
            socket = ss.accept();


        }

        // Catch block to handle the exceptions
        catch (Exception e) {

            // Display the exception on the console
            System.out.println(e);
        }


    }

    private void killServer(){
        try {
            in.close();
            out.close();
            socket.close();
            ss.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void sendrequest(String request){

        nr_requests_sent ++;



    }

    private void receiveAnswer(){

        while(notKilled){

            // Invoking input stream via getInputStream()
            // method by creating DataInputStream class
            // object
            DataInputStream dis = null;
            String str = null;
            try {
                dis = new DataInputStream(socket.getInputStream());


                //str = (String)dis.readUTF();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Display the string on the console

                System.out.println("message= " + dis);



        }


    }

    public static void main(String[] args) {

        //start server socket
        Client c = new Client();
        c.startServer();

        //TODO - test value
        c.nr_requests_to_do = 1;

        while (c.nr_requests_to_do > c.nr_requests_sent){
            // send a request
            //TODO - define request and create comunication with load balancer
            //String request = "2";
            //c.sendrequest(request);

            //receive answer to the request, sent from the server
            c.receiveAnswer();

        }

        c.notKilled = false;
        //kill server
        c.killServer();

    }


}
