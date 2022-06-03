package Server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Server
 */
public class Server {

    /**
     * Blocking queue for the requests
     */
    static BlockingQueue<String> request_list;

    static Boolean stillRunning = true;




    public boolean addRequest (String request){

        return request_list.add(request);
    }

    public static String getRequest(){
        try {
            return request_list.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void setStillRunning(Boolean stillRunning) {
        this.stillRunning = stillRunning;
    }

    public static void main(String[] args) { //chain of responsibility to not have like a 1000 diff types threads

        //create the request list
        request_list = new LinkedBlockingDeque<>(2);

        //get the connections and the ports

        //1 server socket to receive requests from the load balancer

        //1 socket for the monitor (to receive the heartbeat and send info to the monitor)

        //Create and run a String2Client thread
        //String request = getRequest();
        String request = "69";
        TServer2Client stc = new TServer2Client(request);
        stc.run();


        while (stillRunning){
            //attend the requests



            //create 1 socket to send the response to the client
            //send the answer
        }













    }
}
