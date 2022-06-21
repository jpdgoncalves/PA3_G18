package Messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LBStatus implements Serializable{

    private String ip;
    private int port;
    private int id;
    private int status; //0 = down and 1 = up
    private int heartbeat;
    private final HashMap<Integer, Request> managedRequests = new HashMap<>();

    /**
     * Constructor of the class
     *
     * @param ip IP of the server
     * @param port Port of the server
     * @param status Status of the server (0 = down and 1 = up)
     * @param heartbeat Number of heartbeats missed
     */
    public LBStatus(String ip, int port, int id, int status, int heartbeat){
        this.ip = ip;
        this.port = port;
        this.id = id;
        this.status = status;
        this.heartbeat = heartbeat;
    }

    public int getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(int heartbeat) {
        this.heartbeat = heartbeat;
    }

    public void setIp(String ip) {
            this.ip = ip;
        }

    public void setPort(int port) {
        this.port = port;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public int getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Request> getManagedRequests() {
        return new ArrayList<>(managedRequests.values());
    }

    public void addManagedRequest(Request request) {
        managedRequests.put(request.getRequestId(), request);
    }

    public void removeManagedRequest(int requestId) {
        managedRequests.remove(requestId);
    }

    @Override
    public String toString() {
        return "IP = " + this.getIp() + ", Port = "+ this.getPort() + " and Status = " + this.getStatus();
    }
}
