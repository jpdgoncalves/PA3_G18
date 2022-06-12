package Server;

import Messages.Request;

import java.io.*;
import java.net.Socket;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Server class
 */
public class Server {

    //Priority Blocking Queue for the requests
    public static PriorityBlockingQueue<Request> request_list;

    private static int empty_postions = 2;

    //Linked list of servers available
    private static LinkedList<TClientHandler> llts2c = new LinkedList<TClientHandler>();

    //boolean that indicates if the server is still running
    private static Boolean stillRunning = true;

    /**
     * Adds a request to the request list if the list is not full and returns an error if it is full.
     *
     * @param request request to add to the request_list
     */
    public static void addRequest (Request request){

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
  /*  public static Request getRequest(){

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
            System.out.println("Failure, sending fail request anyway");
            return new Request(-1, -1, -1, -1, 1, -1, 1, "-1", -1);
        }
    }*/

    public static void addServer2Client(TClientHandler s2c){
        llts2c.add(s2c);
    }

    private static TClientHandler getAServer2Client(){
        return llts2c.pop();
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
        request_list = new PriorityBlockingQueue<>(2, Comparator.comparingInt(Request::getDeadline));

        //create requests for test
        Request request = new Request(1, 2, 3, 4, 5, 6, 7, "127.0.0.1", 8080);
        Request request2 = new Request(1, 2, 3, 4, 2, 6, 3, "127.0.0.1", 8080);

        request_list.add(request);
        request_list.add(request2);

        /** -------NOTES-------
         *
         * get the connections and the ports
         * 1 server socket to receive requests from the load balancer
         * 1 socket for the monitor (to receive the heartbeat and send info to the monitor)
         */


        //start the execution of the TServer2Client
//        for (int i = 0; i < 3; i++) {
//
//            //Create and run a String2Client thread which will take care of requests and send answers
//            TServer2Client stc = new TServer2Client();
//            stc.run();
//
//            //add to list of available threads
//            addServer2Client(stc);
//        }

        //TMonitorHandlerFromServer block of code
        System.out.println("Connection with monitor");
        Socket serverSocket = new Socket("localhost", 5056);
        try {
            ObjectOutputStream dosServer = new ObjectOutputStream(serverSocket.getOutputStream());
            ObjectInputStream disServer = new ObjectInputStream(serverSocket.getInputStream());
            System.out.println("Starting thread !");
            TMonitorHandler tserver = new TMonitorHandler(serverSocket, dosServer, disServer);
            System.out.println("Thread linked to Monitor");
            tserver.start();
//            tserver.join();
        }catch (Exception e){

        }

        /*while (stillRunning){
            //warn them to come get a request
            if (empty_postions > 0) {
                //get a TServer2Client that is available
                TServer2Client threadToSend = getAServer2Client();
            }

        }*/
        //TODO - Kill comm channels

    }
}
