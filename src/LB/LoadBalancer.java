package LB;

import Gui.LB.LBMainFrame;
import Messages.Request;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Load Balancer class
 */
public class LoadBalancer {
    private static String ip;
    private static int port;
    private static int id;
    private static String mIp;
    private static int mPort;
    private static final LBMainFrame gui = new LBMainFrame();
    private static ServerSocket lbServerSocket;
    private static final ReentrantLock l = new ReentrantLock();
    private static final Condition waitSocket = l.newCondition();
    private static LinkedList <Request> listRequests = new LinkedList<>();

    /**
     * Start Load Balancer
     * @param ip Load Balancer IP
     * @param port Load Balancer port
     * @param id Load Balancer id
     * @param mIp Monitor IP
     * @param mPort Monitor port
     */
    private static void startLb(String ip, int port, int id, String mIp, int mPort) {
        LoadBalancer.ip = ip;
        LoadBalancer.port = port;
        LoadBalancer.id = id;
        LoadBalancer.mIp = mIp;
        LoadBalancer.mPort = mPort;

        try {
            l.lock();
            //send to monitor with my port and IP
            Socket socketup = new Socket(mIp, mPort);
            ObjectOutputStream oos = new ObjectOutputStream(socketup.getOutputStream());
            oos.writeObject(new Request(
                    0,0,id,6,
                    0,"",0, ip  , port));

            lbServerSocket = new ServerSocket(port);
            waitSocket.signal();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } finally {
            l.unlock();
        }

        gui.setCanEdit(false);
        gui.setStartEnabled(false);
        gui.setStopEnabled(true);
    }

    /**
     * Stop Load Balancer
     */
    private static void stopLb() {
        try {
            l.lock();
            lbServerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } finally {
            l.unlock();
        }

        gui.setCanEdit(true);
        gui.setStopEnabled(false);
        gui.setStartEnabled(true);
    }

    public static void swapServerSocket(int port){
        l.lock();
        try{
            lbServerSocket.close();
            lbServerSocket = new ServerSocket(port);
            LoadBalancer.port = port;
            gui.setLbPort(port);
            waitSocket.signal();
        }catch (Exception e){
            e.printStackTrace();
        }

        l.unlock();
    }

    /**
     * Adding request to list of requests
     * @param request
     */
    public static void addRequest(Request request) {
        listRequests.add(request);
    }

    /**
     * Load Balancer routine
     * @param args
     */
    public static void main(String[] args){
        gui.setStartCallback(LoadBalancer::startLb);
        gui.setStopCallback(LoadBalancer::stopLb);
        gui.setStopEnabled(false);
        gui.setStartEnabled(true);
        gui.setVisible(true);

        while(true){
            l.lock();
            while (lbServerSocket == null || lbServerSocket.isClosed()) {
                System.out.println("Waiting for main gui");
                waitSocket.awaitUninterruptibly();
            }
            l.unlock();

            try {
                Socket socket = lbServerSocket.accept();
                TConnectionHandler thread = new TConnectionHandler(socket, ip, port, mIp, mPort);
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

