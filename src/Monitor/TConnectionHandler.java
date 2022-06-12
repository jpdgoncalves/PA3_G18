package Monitor;

import LB.LoadBalancer;
import Messages.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TConnectionHandler extends Thread{
    Socket socket;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    public TConnectionHandler(Socket socket){
        this.socket = socket;
    }

    private void startConnection() throws IOException, ClassNotFoundException {
        this.ois = new ObjectInputStream(socket.getInputStream());
        this.oos = new ObjectOutputStream(socket.getOutputStream());

        Request req = (Request) ois.readObject();
        System.out.println("I read a request - " + req.getCode());

        //Closing connection
        if(req.getDeadline() == -1)
        {
            System.out.println("Client " + this.socket + " sends exit...");
            System.out.println("Connection closing...");
            this.socket.close();
            this.oos.close();
            this.ois.close();
            System.out.println("Closed");
            return;
        }

        //Load balancer connection
        if(req.getCode() == 1){ //TODO - check the code
            System.out.println("Connection with LB made !!");
//            while(true){
                LoadBalancer.addRequest(req);
                System.out.println(req);
                oos.flush();
//            }
        }
        //Server connection
        else if (req.getCode() == 7) {
            System.out.println("Connection with Server made !!");
//            while(true){
                LoadBalancer.addRequest(req);
                System.out.println(req);
                oos.flush();
//            }
        }
    }

    private void closeConnections() throws IOException {
//        serverSocket.close();
        oos.close();
        ois.close();
    }

    @Override
    public void run() {
        try {
            startConnection();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
//        try {
//            closeConnections();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
}
