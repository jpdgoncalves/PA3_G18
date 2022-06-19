package Server;

import Gui.Server.ServerConfigFrame;
import Gui.Server.ServerMainFrame;
import Messages.Request;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Server class
 */
public class Server {

    //Priority Blocking Queue for the requests
    public static PriorityBlockingQueue<Request> request_list;

    private static String ip;
    private static int port;
    private static int id;
    private static String mIp;
    private static int mPort;

    private static ServerSocket serverSocket;
    private static final ReentrantLock l = new ReentrantLock();
    private static final Condition waitSocket = l.newCondition();

    private final static ServerMainFrame mainGui = new ServerMainFrame();
    private final static ServerConfigFrame configGui = new ServerConfigFrame();

    public static void startServer(String ip, int port, int id, String mIp, int mPort) {
        Server.ip = ip;
        Server.port = port;
        Server.id = id;
        Server.mIp = mIp;
        Server.mPort = mPort;

        try {
            l.lock();
            //send to monitor with my port and IP
            Socket socketup = new Socket(mIp, mPort);
            ObjectOutputStream oos = new ObjectOutputStream(socketup.getOutputStream());
            oos.writeObject(new Request(
                    0,0,id,7,
                    0,"",0,  ip , port
            ));
            serverSocket = new ServerSocket(port);
            waitSocket.signal();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } finally {
            l.unlock();
        }

        configGui.setVisible(false);

        mainGui.setIp(ip);
        mainGui.setPort(port);
        mainGui.setId(id);
        mainGui.setVisible(true);
    }

    public static void stopServer() {
        try {
            l.lock();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } finally {
            l.unlock();
        }

        mainGui.setVisible(false);
        configGui.setVisible(true);
    }

    /**
     * Adds a request to the request list if the list is not full and returns an error if it is full.
     *
     * @param request request to add to the request_list
     */
    public static void addRequest (Request request){

        if(request_list.size() < 2){
            request_list.add(request);
        } else {
            //TODO - send err message
        }
    }

    public static void main(String[] args) throws IOException { //chain of responsibility to not have like a 1000 diff types threads

        //create the request list
        request_list = new PriorityBlockingQueue<>(2, Comparator.comparingInt(Request::getDeadline));


        /** -------NOTES-------
         *
         * get the connections and the ports
         * 1 server socket to receive requests from the load balancer
         * 1 socket for the monitor (to receive the heartbeat and send info to the monitor)
         */

        configGui.setVisible(true);
        configGui.setStartCallback(Server::startServer);
        mainGui.setStopCallback(Server::stopServer);

        while (true) {
            l.lock();
            while (serverSocket == null || serverSocket.isClosed()) {
                System.out.println("Waiting for main gui");
                waitSocket.awaitUninterruptibly();
            }
            l.unlock();

            try {
                Socket socket = serverSocket.accept();
                if (socket != null) (new TConnectionHandler(socket, mainGui, ip, port, mIp, mPort)).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
