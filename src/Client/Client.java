package Client;

import Server.TServer2Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.DoubleToIntFunction;

public class Client {

    private ServerSocket ss;
    private Socket socket;
    private ObjectInputStream in;
    private DataOutputStream out;

    private int nr_requests_to_do;

    private int nr_requests_sent = 0;

    public static Boolean notKilled = true;

    private void sendrequest(String request){
        nr_requests_sent ++;
    }

    public static boolean getStateRequests(){
        return notKilled;
    }

    /**
     * Tasks to be done by a client
     */
    public static void main(String[] args) {

        //creates a client
        Client c = new Client();

        //create a thread that deals with the future comunications with the server
        THandleCommsServer hcs = new THandleCommsServer();
        hcs.run();




        //TODO - test value
        c.nr_requests_to_do = 1;

        //while there are requests to do
        //while (c.nr_requests_to_do > c.nr_requests_sent){
            // send a request
            //TODO - define request and create comunication with load balancer
            //String request = "2";
            //c.sendrequest(request);

            //receive answer to the request, sent from the server
            //c.receiveAnswer();

        //}

        //no more requests
        //c.notKilled = false;

        //terminate a server
        //c.killServer();

    }


}
