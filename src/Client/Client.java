package Client;

import Gui.Client.ClientConfigFrame;
import Gui.Client.ClientMainFrame;
import Messages.Request;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Client class
 */
public class Client {

    //client ip
    private static String ip;

    //client port
    private static int port;

    //client id
    private static int id;

    //number of requests sent
    private static int requestCounter = 0;

    private static ServerSocket serverSocket;
    private static final ReentrantLock l = new ReentrantLock();
    private static final Condition waitSocket = l.newCondition();

    //configuration GUI
    private static final ClientConfigFrame configGui = new ClientConfigFrame();
    //Main GUI
    private static final ClientMainFrame mainGui = new ClientMainFrame();

    /**
     * Starting client
     * @param ip client IP
     * @param port client port
     * @param id client id
     */
    public static void startClient(String ip, int port, int id) {
        Client.ip = ip;
        Client.port = port;
        Client.id = id;

        try {
            l.lock();
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

    /**
     * Stop client
     */
    public static void stopClient() {
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
     * Client sends a request to Load Balancer
     * @param ip Load Balancer IP
     * @param port Load Balancer port
     * @param nbOfIteration request number of iteration
     * @param deadline request deadline
     */
    public static void sendRequestToLB(String ip, int port, int nbOfIteration, int deadline){
        Request request = new Request(id, id * 1000 + requestCounter++, 0, 1, nbOfIteration, "", deadline, Client.ip, Client.port);

        try {
            Socket lbSocket = new Socket(ip, port);
            ObjectOutputStream oos = new ObjectOutputStream(lbSocket.getOutputStream());
            oos.writeObject(request);
            System.out.println("Request sent to LB !");
            System.out.println(request);

            mainGui.addPendingRequest(request);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Tasks to be done by a client
     */
    public static void main(String[] args) {

        configGui.setVisible(true);
        configGui.setStartCallback(Client::startClient);
        mainGui.setSendCallback(Client::sendRequestToLB);
        mainGui.setStopCallback(Client::stopClient);

        while (true) {
            l.lock();
            while (serverSocket == null || serverSocket.isClosed()) {
                System.out.println("Waiting for main gui");
                waitSocket.awaitUninterruptibly();
            }
            l.unlock();

            try {
                Socket socket = serverSocket.accept();
                if (socket != null) (new TServerHandler(socket, mainGui)).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
