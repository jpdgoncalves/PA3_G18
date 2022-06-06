package Server;

import Request.Request;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Creates the answer to the requests made and sends said answer to the client that made the request
 */
public class TServer2Client extends Thread{

    private final ReentrantLock lock = new ReentrantLock();

    private Request request;
    private int nr_iterations;
    private String ip_client;
    private int port_client;
    private int timePerIteration;
    private Socket s;

    /**
     * Constructor
     */
    public TServer2Client(){

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

    private void receiveRequest(){

        //while server says nothing, wait

        //server should unlock this
        lock.lock();  // block until condition holds
        try {
            //TODO - receive request
            //TODO - set params that should probs returned?


        } finally {
            lock.unlock();
        }




        //TODO - CODE DUMP

        /* this.request = request;

        this.nr_iterations = request.getNr_iterations();

        //TODO get these params somehow, probs have a special request with that data
        this.ip_client = "127.0.0.1";
        this.port_client = 8080;

        this.timePerIteration = request.getTimePerIteration();

        //create socket
        try {
            s = new Socket(ip_client, port_client);
        } catch (IOException e) {
            e.printStackTrace();
        }

        */

    }

    /**
     * The routine that will be done by each Server2Client thread
     */
    @Override
    public void run() {
        System.out.println("Server to client begins !");

        while (true){
            //TODO - wait to receive request
            receiveRequest();

            //process requests
            String answer = getPi(nr_iterations, timePerIteration);

            //sends info to client
            sendInfo(answer);

            //TODO - send indication to the server that I can receive and process another request
        }
        //System.out.println("Kill thread");

        //TODO - end connection, err killing client as well
        //terminateServer();
    }
}
