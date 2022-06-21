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
    /**
     * Priority Blocaking Queue for requests
     */
    public static PriorityBlockingQueue<Request> request_list;
    private final String monitorIp;
    private final int monitorPort;

    /**
     * TRequestHandler constructor
     *
     * @param gui Server gui
     * @param request_list list of requests that are waiting to be handled
     */
    public TRequestHandler(String mIp, int mPort, ServerMainFrame gui, PriorityBlockingQueue<Request> request_list){
        monitorIp = mIp;
        monitorPort = mPort;
        this.gui = gui;
        this.request_list = request_list;
    }

    /**
     * Creates the value of pi requested and sleeps according to the time per iteration.s
     *
     * @param nr_iterations number of iterations
     * @return returns the value of pi according to the number of iterations
     */
    private String getPi(int nr_iterations) {
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

        if (!sendRequest(req.getTarget_IP(), req.getTargetPort(), req)) return;

        System.out.println("Reply sent to Client");

        gui.addProcessedRequest(req);
        gui.removeReceivedRequest(req.getRequestId());

        sendRequest(monitorIp, monitorPort, req);
    }

    private boolean sendRequest(String ip, int port, Request request) {
        Socket socket = null;
        ObjectOutputStream oos  = null;
        boolean success = false;

        try {
            System.out.println("Sending request " + request);

            socket = new Socket(ip, port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(request);

            System.out.println("Sent request " + request);
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (oos != null) oos.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return success;
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
