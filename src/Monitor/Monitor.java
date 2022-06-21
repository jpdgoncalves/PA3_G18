package Monitor;

import Gui.Monitor.MonitorConfigFrame;
import Gui.Monitor.MonitorMainFrame;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
    private static final MonitorData data = new MonitorData();

    private static String ip;
    private static int port;

    private static final MonitorConfigFrame configGui = new MonitorConfigFrame();
    private static final MonitorMainFrame mainGui = new MonitorMainFrame();

    private static ServerSocket serverSocket;
    private static THeartbeatChecker hbck = new THeartbeatChecker(5, data, mainGui);
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
                TConnectionHandler thread = new TConnectionHandler(socket, data, mainGui);
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }



}
