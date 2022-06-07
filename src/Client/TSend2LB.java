package Client;

import Messages.Request;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Sends a request from the client to the load balancer
 */
public class TSend2LB extends Thread{

    private Request request;
    private int nr_iterations;
    private String ip_LB;
    private int port_LB;
    private int timePerIteration;
    private Socket s;

    /**
     * Constructor
     *
     * @param request Request that will be processed
     */
    public TSend2LB(Request request){
        this.request = request;
        this.nr_iterations = request.getNr_iterations();

        //TODO get these params somehow
        this.ip_LB = "127.0.0.1";
        this.port_LB = 9090;

        this.timePerIteration = 1000;

        //create socket
        try {
            s = new Socket(ip_LB, port_LB);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Sends the request to the load balancer, via TCP/IP socket
     */
    private void sendInfo(Request request) {

        try {
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            out.writeObject(request);

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    /**
     * TODO - not working, killing LB(?) as well (idk not tested here but I suppose it is still true)
     */
    private void terminateServer() {
        try {
            s.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The routine that will be done by each Server2Client thread
     */
    @Override
    public void run() {
        System.out.println("Server to client begins !");

        //sends info to client
        sendInfo(request);

        //System.out.println("Kill thread");

        //TODO - end connection, err killing client as well(I suppose it is still true)
        //terminateServer();

    }

}

