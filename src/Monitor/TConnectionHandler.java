package Monitor;

import Gui.Monitor.MonitorMainFrame;
import Messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Connection Handler thread of Monitor
 */
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

            gui.addLbRequest(req);
            gui.addServerRequest(req);

        }

        //Reply to the request
        if(req.getCode() == 2 || req.getCode() == 3) {
            System.out.println("Connection with Server made - receiving reply");
            System.out.println(req);

            gui.removeServerRequest(req.getRequestId());
            gui.removeLbRequest(req.getRequestId());

            //send request to LBs
            for (int i = 0; i < monitorData.getLbList().size(); i++){
                sendRequest(monitorData.getLbList().get(i).getIp(), monitorData.getLbList().get(i).getPort(), req);
            }

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
                System.out.println(" Port is ::::" + port);
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

            //send request to LBs
            for (int i = 0; i < monitorData.getLbList().size(); i++){
                sendRequest(monitorData.getLbList().get(i).getIp(), monitorData.getLbList().get(i).getPort(), req);
            }
        }

        if(req.getCode() == 5) {
            System.out.println("Heartbeat received!!");
            monitorData.resetHeartbeat(req.getTarget_IP(), req.getTargetPort());
        }


    }

    /**
     * Send request
     * @param ip ip to entity to which send
     * @param port port of entity to which send
     * @param request request to send
     * @return if request has been sent
      */
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
