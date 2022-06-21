package Monitor;

import Gui.Monitor.MonitorMainFrame;
import Messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TConnectionHandler extends Thread{
    private final Socket socket;
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;

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
     * @throws IOException If an IOException occurs
     * @throws ClassNotFoundException if the object received isn't a {@link Request} object
     */
    private void startConnection() throws IOException, ClassNotFoundException {
        ois = new ObjectInputStream(socket.getInputStream());
        Request req = (Request) ois.readObject();

        //Client request connection
        if(req.getCode() == 1) {

            System.out.println("Connection with LB made - receiving client request :");
            System.out.println(req);
            monitorData.addLbRequest(req);

            this.oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(monitorData.getServerList());
        }

        //LB up connection
        if(req.getCode() == 6){
            System.out.println("Connection with LB made - LB up!!");
            //add LB to a list of LBs
            LBStatus newLb = new LBStatus(req.getTarget_IP(), req.getTargetPort(), req.getServerId(), 1, 0);
            monitorData.addLb(newLb);
            gui.addLb(req.getServerId(), req.getTarget_IP(), req.getTargetPort());
            gui.setIsLbAlive(req.getServerId(), true);

            if (monitorData.getPrimaryLb() == newLb) {
                req.setCode(10);
                if (!sendRequest(req.getTarget_IP(), req.getTargetPort(), req)) return;
                gui.setIsLbPrimary(req.getServerId(), true);
            } else {
                req.setCode(11);
                int port = req.getTargetPort();
                req.setTargetPort(monitorData.getPrimaryLb().getPort());
                sendRequest(req.getTarget_IP(), port, req);
            }
        }


        //Server up connection
        if (req.getCode() == 7) {
            System.out.println("Connection with srv made - SRV up!!");
            //add LB to a list of LBs
            monitorData.addServer(new ServerStatus(req.getTarget_IP(), req.getTargetPort(), req.getServerId(),1, 0));
            gui.addServer(req.getServerId(), req.getTarget_IP(), req.getTargetPort());
            gui.setIsServerAlive(req.getServerId(), true);
        }

        if(req.getCode() == 5) {
            System.out.println("Heartbeat received!!");
            monitorData.resetHeartbeat(req.getTarget_IP(), req.getTargetPort());
        }


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
     * Close connections
     * @throws IOException If it is unable to close all used resources.
     */
    private void closeConnections() throws IOException {
        this.socket.close();
        if (this.ois != null) this.ois.close();
        if (this.oos != null) this.oos.close();
    }

    /**
     * Thread lifecycle
     */
    @Override
    public void run() {

        try {
            startConnection();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            closeConnections();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
