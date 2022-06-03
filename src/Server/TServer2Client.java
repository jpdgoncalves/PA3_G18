package Server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TServer2Client extends Thread{

    String request;
    int nr_iterations;
    String ip_client;
    int port_client;
    int timePerIteration;

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
    private void sendInfo(String ip_client, int port_client, String answer) {

        Socket socket = null;
        DataOutputStream out = null;

        try {
            //creates the connection
            socket = new Socket(ip_client, port_client);
            System.out.println("Connected");

            // sends output to the socket
            out  = new DataOutputStream(socket.getOutputStream());

            out.close();
            socket.close();

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
        sendInfo(ip_client, port_client, answer);

        System.out.println("Kill thread");

    }


}
