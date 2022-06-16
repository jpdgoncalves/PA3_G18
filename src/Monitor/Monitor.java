package Monitor;

import LB.LoadBalancer;
import Messages.LBStatus;
import Messages.Request;
import Messages.ServerStatus;
import Server.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;

public class Monitor {
    private static LinkedList <Request> listRequests = new LinkedList<>();
    private static HashMap<String, ServerStatus> listServers = new HashMap<>();

    private static HashMap<String, LBStatus> listLB = new HashMap<>();

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
        listLB.remove(key);
    }

    public static void removeServer(String key) {
        listServers.remove(key);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket serverSocket = new ServerSocket(5056);

        //TODO - not working because it is sharing the socket I guess
        //THeartbeatChecker hbck = new THeartbeatChecker(5, serverSocket);
        //hbck.run();

        while(true){
            System.out.println("test");
            Socket socket = serverSocket.accept();
            System.out.println("connection made !");
            TConnectionHandler thread = new TConnectionHandler(socket);
            thread.start();
        }

    }



}
