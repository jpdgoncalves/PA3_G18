package Server;

import Gui.Server.ServerMainFrame;
import Messages.Request;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * TRequestHandler - receives requests, calculates pi and sends the answer to the client
 */
public class TRequestHandler extends Thread{
    private static ServerMainFrame gui;
    private final int timePerIteration = 5000;
    public static PriorityBlockingQueue<Request> request_list;

    /**
     * TRequestHandler constructor
     *
     * @param gui Server gui
     * @param request_list list of requests that are waiting to be handled
     */
    public TRequestHandler(ServerMainFrame gui, PriorityBlockingQueue<Request> request_list){

        this.gui = gui;
        this.request_list = request_list;
        //setDaemon(true);
    }

    /**
     * Creates the value of pi requested and sleeps according to the time per iteration.s
     *
     * @param nr_iterations number of iterations
     * @return returns the value of pi according to the number of iterations
     */
    private String getPi(int nr_iterations){

        try {
            Thread.sleep(timePerIteration * nr_iterations);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return "3.1415926589793".substring(0, 2 + nr_iterations);
    }

    /**
     * Gets a request, calculates pi, sends answer to the client and updates the GUI.
     * @param req Request that will be handled
     */
    public void handleRequest (Request req){

        String answer = getPi(req.getNr_iterations());
        req.setPi(answer);
        req.setCode(2);

        Socket clientSocket = null;
        ObjectOutputStream oosClient = null;

        try {
            clientSocket = new Socket(req.getTarget_IP(), req.getTargetPort());
            oosClient = new ObjectOutputStream(clientSocket.getOutputStream());
            oosClient.writeObject(req);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Reply sent to Client");

        gui.addProcessedRequest(req);
        gui.removeReceivedRequest(req.getRequestId());

        try {
            oosClient.close();
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Life cycle of the thread
     */
    @Override
    public void run() {

        while (true) {

            Request req = null;
            try {
                req = request_list.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            //handle the request
            handleRequest(req);

        }
    }
}
