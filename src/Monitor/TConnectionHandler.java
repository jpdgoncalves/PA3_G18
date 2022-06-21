package Monitor;

import Gui.Monitor.MonitorMainFrame;
import Messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TConnectionHandler extends Thread{
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private final MonitorMainFrame gui;
    private final MonitorData monitorData;

    /**
     * Constructor of TConnectionHandler
     * @param socket TCP IP socket
     */
    public TConnectionHandler(Socket socket, MonitorData data, MonitorMainFrame gui){
        this.socket = socket;
        this.gui = gui;
        this.monitorData = data;
    }

    /**
     * Receives a request and treats it accordingly
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void startConnection() throws IOException, ClassNotFoundException {
        this.ois = new ObjectInputStream(socket.getInputStream());
        //this.oos = new ObjectOutputStream(socket.getOutputStream());

        Request req = (Request) ois.readObject();
        //System.out.println("I read a request - " + req.getCode());

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
        if(req.getCode() == 1) {

            System.out.println("Connection with LB made - receiving client request :");
            System.out.println(req);
            monitorData.addLbRequest(req);

            this.oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(monitorData.getServerList());
            oos.close();
        }

        //LB up connection
        if(req.getCode() == 6){
            System.out.println("Connection with LB made - LB up!!");
            //add LB to a list of LBs
            monitorData.addLb(new LBStatus(req.getTarget_IP(), req.getTargetPort(), req.getServerId(), 1, 0));
            gui.addLb(req.getServerId(), req.getTarget_IP(), req.getTargetPort());
            //TODO: We don't know here if the LB is the primary.
            gui.setIsLbPrimary(req.getServerId(), true);
            gui.setIsLbAlive(req.getServerId(), true);
        }


        //Server up connection
        if (req.getCode() == 7) {
            System.out.println("Connection with srv made - SRV up!!");
            //add LB to a list of LBs
            monitorData.addServer(new ServerStatus(req.getTarget_IP(), req.getTargetPort(), req.getServerId(),1, 0));
            gui.addServer(req.getServerId(), req.getTarget_IP(), req.getTargetPort());
            gui.setIsServerAlive(req.getServerId(), true);
        }

        //Heartbeat reply - TODO test
        if(req.getCode() == 5){
            //System.out.println("Heartbeat received!!");
            monitorData.resetHeartbeat(req.getTarget_IP(), req.getTargetPort());
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
