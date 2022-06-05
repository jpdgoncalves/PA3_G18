package Server;

import Request.Request;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Server
 */
public class Server {

    /**
     * Blocking queue for the requests
     */
    static BlockingQueue<Request> request_list;

    static Boolean stillRunning = true;

    public boolean addRequest (Request request){

        return request_list.add(request);
    }

    public static Request getRequest(){
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
        Request request = new Request(1,1,1,1,3,0,0, 1000);

        TServer2Client stc = new TServer2Client(request);
        stc.run();


        while (stillRunning){
            //attend the requests



            //create 1 socket to send the response to the client
            //send the answer
        }













    }
}
