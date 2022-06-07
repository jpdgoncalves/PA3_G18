package Server;

import Request.Request;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Server class
 */
public class Server {

    //Linked list for the requests
    static LinkedList<Request> request_list;

    //boolean that indicates if the server is still running
    static Boolean stillRunning = true;

    /**
     * Adds a request to the request list if the list is not full and returns an error if it is full.
     *
     * @param request request to add to the request_list
     */
    public void addRequest (Request request){

        if(request_list.size() < 2){
            request_list.add(request);
        } else {
            //TODO - send err message
        }
    }

    /**
     * Gets the request with the lowest deadline.
     *
     * @return request with the lowest deadline
     */
    public static Request getRequest(){

        int closestDeadline = Integer.MAX_VALUE;
        int posWClosestDeadline = -1;

        if (request_list.size() > 0) {

            for (int i = 0; i < request_list.size(); i++) {

                if (closestDeadline > request_list.get(i).getDeadline()) {
                    //update
                    closestDeadline = request_list.get(i).getDeadline();
                    posWClosestDeadline = i;
                }
            }

            Request returnVal = request_list.get(posWClosestDeadline);
            request_list.remove(posWClosestDeadline);

            return returnVal;
        } else {
            return new Request(-1, -1, -1, -1, -1, -1, -1, -1);
        }
    }

    /**
     * Sets the state of the simulation
     * @param stillRunning new state of the simulation
     */
    public void setStillRunning(Boolean stillRunning) {
        this.stillRunning = stillRunning;
    }

    public static void main(String[] args) throws IOException { //chain of responsibility to not have like a 1000 diff types threads

        //create the request list
        request_list = new LinkedList<Request>();


        //get the connections and the ports
        //1 server socket to receive requests from the load balancer
        //1 socket for the monitor (to receive the heartbeat and send info to the monitor)

//        for (int i = 0; i < 3; i++){
//            //Create and run a String2Client thread which will take care of requests and send answers
//            TServer2Client stc = new TServer2Client();
//            stc.run();
//        }

        Socket serverSocket = new Socket("localhost", 5057);
        try {
            DataInputStream disServer = new DataInputStream(serverSocket.getInputStream());
            DataOutputStream dosServer = new DataOutputStream(serverSocket.getOutputStream());
            TMonitorHandlerFromServer tserver = new TMonitorHandlerFromServer(serverSocket, dosServer, disServer);
            tserver.start();
            tserver.join();

//            while (stillRunning){
//
//                //TODO - send requests to the stcs
//
//
//            }
//            //TODO - Kill comm channels
        }catch (Exception e){

        }

    }
}
