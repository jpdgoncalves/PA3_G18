package Server;

import Messages.Request;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static Server.Server.*;

/**
 * Creates the answer to the requests made and sends said answer to the client that made the request
 */
public class TClientHandler extends Thread{
    private static final int TIMEPERITERATION = 1;

    //create socket
    private Socket s;

    private ObjectOutputStream out;

    /**
     * Constructor
     */
    public TClientHandler(){

    }

    /**
     * Creates the value of pi requested and sleeps according to the time per iteration.s
     *
     * @param nr_iterations number of iterations
     * @return returns the value of pi according to the number of iterations
     */
    private String getPi(int nr_iterations){

        try {
            Thread.sleep(TIMEPERITERATION * nr_iterations);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return "3.1415926589793".substring(0, 2 + nr_iterations);
    }

    /**
     * Sends the answer obtained to the client that made the request, via TCP/IP socket.
     * TODO - only sends one request to the client for some reason
     * @param answer Value of pi to send
     */
    private void sendInfo(String answer, String ip_client, int port_client) throws IOException {

        //create socket
        s = new Socket(ip_client, port_client);

        out = new ObjectOutputStream(s.getOutputStream());

        out.writeObject(answer);

        //out.close();

        //ObjectOutputStream out2 = new ObjectOutputStream(s.getOutputStream());
        //out2.writeObject(answer.concat("kant"));


        //TODO - client dies
        //s.close();

    }


    /**
     * Gets a request from the request_list of the server and processes it
     */
    private void receiveRequest(){

        //get the request from the server
        Request request ;
        try {
            //TODO - probs make a getter
            request = request_list.take();
        } catch (InterruptedException e) {
            System.out.println("Could not take");
            throw new RuntimeException(e);
        }

        System.out.println(request.getDeadline());

        //process requests
        String answer = getPi(request.getNr_iterations());

        //sends answer to client
        try {
            sendInfo(answer, request.getTarget_IP(), request.getTargetPort());
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
        //TODO - consider having a way to kill this with a bool
        while (true){

            //receive requests
            receiveRequest();

            // TODO - idk how would I reference self to add it to the list
            // addServer2Client();

        }

    }
}
