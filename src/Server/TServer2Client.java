package Server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TServer2Client extends Thread{

    private String request;
    private int nr_iterations;
    private String ip_client;
    private int port_client;
    private int timePerIteration;
    private Socket s;

    /**
     * Constructor
     *
     * @param request Request that will be processed
     */
    public TServer2Client(String request){
        this.request = request;

        //TODO get these params from the request
        this.nr_iterations = 2;
        this.ip_client = "127.0.0.1";
        this.port_client = 8080;
        this.timePerIteration = 1000;

        //create socket
        try {
            s = new Socket(ip_client, port_client);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Creates the value of pi requested and sleeps according to the time per iteration.s
     *
     * @param nr_iterations number of iterations
     * @param timePerIteration time per each iteration
     * @return returns the value of pi according to the number of iterations
     */
    private String getPi(int nr_iterations, int timePerIteration){

        try {
            Thread.sleep(timePerIteration * nr_iterations);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return "3.1415926589793".substring(0, 2 + nr_iterations);
    }

    /**
     * Sends the answer obtained to the client that made the request, via TCP/IP socket.
     *
     * @param ip_client IP of the client
     * @param port_client Port of the client
     * @param answer Value of pi to send
     */
    private void sendInfo(String answer) {

        try {
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            out.writeObject(answer);

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    /**
     * TODO - not working, killing client as well
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
        //process requests
        String answer = getPi(nr_iterations, timePerIteration);
        //sends info to client
        sendInfo(answer);

        System.out.println("Kill thread");

        //TODO - end connection, killing client as well
        //terminateServer();



    }




}
