package Monitor;

import Messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TConnectionHandler extends Thread{
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private ServerStateMessage serverStateMessage;

    /**
     * Constructor of TConnectionHandler
     * @param socket TCP IP socket
     */
    public TConnectionHandler(Socket socket){
        this.socket = socket;
    }

    /**
     * Receives a request and treats it accordingly
     * @throws IOException
     * @throws ClassNotFoundException
     */
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

        //Client request connection
        if(req.getCode() == 1){
            System.out.println("Connection with LB made - receiving client request :");
            System.out.println(req);
            Monitor.addRequest(req);

            ServerStateMessage ssm = new ServerStateMessage();
            //TODO - check these lines and the status
            ServerStatus serverStatus = new ServerStatus("localhost", 5058, 1, 0);
            ssm.addServer(1, serverStatus);
            System.out.println("ServerStateMessage sends to LB : " + ssm);
            oos.writeObject(ssm);

            oos.flush();
        }

        //LB up connection
        if(req.getCode() == 6){
            System.out.println("Connection with LB made - LB up!!");
            //add LB to a list of LBs
            LBStatus lbst = new LBStatus(socket.getInetAddress().getHostAddress(), socket.getPort(), 1, 0);
            Monitor.addLB(socket.getInetAddress().getHostAddress() + socket.getPort(), lbst);
            System.out.println(lbst);
            oos.flush();
        }


        //Server up connection
        if (req.getCode() == 7) {
            System.out.println("Connection with Server made - server up!!");
            //add server to a list of servers
            ServerStatus st = new ServerStatus(socket.getInetAddress().getHostAddress(), socket.getPort(), 1, 0);
            Monitor.addServer(socket.getInetAddress().getHostAddress() + socket.getPort(), st);
            System.out.println(st);
            oos.flush();
        }

        //Heartbeat reply - TODO test
        if(req.getCode() == 5){
            System.out.println("Heartbeat received!!");

            String key = socket.getInetAddress().getHostAddress() + socket.getPort();

            //try to find a server or LB
            ServerStatus stServer = Monitor.getListServers().get(key);
            LBStatus stLB = Monitor.getListLB().get(key);

            //is a server
            if (stServer != null){
                //reset heartbeat
                stServer.setHeartbeat(0);
                //set it on its place
                Monitor.setServer(key, stServer);
            }
            //else is a LB
            else if (stLB != null){
                //reset heartbeat
                stLB.setHeartbeat(0);
                //set it on its place
                Monitor.setLB(key, stLB);
            } else {
                System.out.println("Err - not an LB or a Server!");
            }

            System.out.println(req);
            oos.flush();
        }
    }

    /**
     * Close connections
     * @throws IOException
     */
    private void closeConnections() throws IOException {
//        serverSocket.close();
        oos.close();
        ois.close();
    }

    /**
     * Thread lifecycle
     */
    @Override
    public void run() {
        try {
            startConnection();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
