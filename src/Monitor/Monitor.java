package Monitor;

import Gui.Monitor.MonitorConfigFrame;
import Gui.Monitor.MonitorMainFrame;
import Messages.LBStatus;
import Messages.Request;
import Messages.ServerStatus;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
    private static LinkedList <Request> listRequests = new LinkedList<>();
    private static HashMap<String, ServerStatus> listServers = new HashMap<>();
    private static HashMap<String, LBStatus> listLB = new HashMap<>();

    private static String ip;
    private static int port;

    private static final MonitorConfigFrame configGui = new MonitorConfigFrame();
    private static final MonitorMainFrame mainGui = new MonitorMainFrame();

    private static ServerSocket serverSocket;
    private static THeartbeatChecker hbck = new THeartbeatChecker(5);
    private static final ReentrantLock l = new ReentrantLock();
    private static final Condition waitSocket = l.newCondition();

    private static void startMonitor(String ip, int port) {
        Monitor.ip = ip;
        Monitor.port = port;

        try {
            l.lock();
            serverSocket = new ServerSocket(port);
            hbck.start();
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
        mainGui.setVisible(true);
    }

    private static void stopMonitor() {
        try {
            l.lock();
            serverSocket.close();
            hbck.interrupt();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } finally {
            l.unlock();
        }

        mainGui.setVisible(false);
        configGui.setVisible(true);
    }

    //TODO - idk if it will be maintained as a linkedlist
    public static void addRequest(Request request) {
        listRequests.add(request);
    }

    /**
     * Adds a server to the hashmap
     * @param IPandPort IP and port concatenated as a String
     * @param svst Server info
     */
    public static void addServer(String IPandPort, ServerStatus svst) {
        listServers.put(IPandPort, svst);
        System.out.println("listServers.size() - " + listServers.size());
    }

    /**
     * Adds a LB to a hashmap
     * @param IPandPort IP and port concatenated as a String
     * @param lbst LB info
     */
    public static void addLB(String IPandPort, LBStatus lbst) {
        listLB.put(IPandPort, lbst);
    }

    public static LinkedList<Request> getListRequests() {
        return listRequests;
    }

    public static HashMap<String, ServerStatus> getListServers() {
        return listServers;
    }

    public static HashMap<String, LBStatus> getListLB() {
        return listLB;
    }

    public static void setServer(String key, ServerStatus server) {
        Monitor.listServers.put(key, server);
    }

    public static void setLB(String key, LBStatus lb) {
        Monitor.listLB.put(key, lb);
    }

    public static void removeLB(String key) {
        int id = listLB.get(key).getId();
        listLB.remove(key);
        mainGui.setIsLbAlive(id, false);
    }

    public static void removeServer(String key) {
        int id = listServers.get(key).getId();
        listServers.remove(key);
        mainGui.setIsServerAlive(id, false);
    }

    public static void main(String[] args) {

        configGui.setVisible(true);
        configGui.setStartCallback(Monitor::startMonitor);
        mainGui.setStopCallback(Monitor::stopMonitor);

        while(true){

            l.lock();
            while (serverSocket == null || serverSocket.isClosed()) {
                System.out.println("Waiting for main gui");
                waitSocket.awaitUninterruptibly();
            }
            l.unlock();

            try {
                Socket socket = serverSocket.accept();
                TConnectionHandler thread = new TConnectionHandler(socket, mainGui);
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }



}
