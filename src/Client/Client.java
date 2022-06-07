package Client;

import Messages.Request;

import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Client class
 */
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

        //parse the arguments given via cmd
        //nr_requests_to_send = args[0];
        //TODO - test val
        c.nr_requests_to_do = 1;

        for (int req = 0; req < c.nr_requests_to_do; req ++){

            //create a new request
            Request request = new Request(1,1,1,01,2,0,2,"1000", 1);

            //create a thread that send the request to the LB
            /*TSend2LB stlb = new TSend2LB(request);
            stlb.run();*/

            //create a thread that deals with the future communications with the server
            THandleCommsServer hcs = new THandleCommsServer();
            hcs.run();
        }

    }


}
