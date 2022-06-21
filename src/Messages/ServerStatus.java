package Messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServerStatus implements Serializable {
    private String ip;
    private int port;
    private int id;
    private int status; //0 = down and 1 = up
    private int heartbeat;
    private HashMap<Integer, Request> incompleteRequests = new HashMap<>(); //<requestId, Request>

    /**
     * Constructor of the class
     *
     * @param ip IP of the server
     * @param port Port of the server
     * @param status Status of the server (0 = down and 1 = up)
     * @param heartbeat Number of heartbeats missed
     */
    public ServerStatus(String ip, int port, int id, int status, int heartbeat){
        this.ip = ip;
        this.port = port;
        this.id = id;
        this.status = status;
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

    public int getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(int heartbeat) {
        this.heartbeat = heartbeat;
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

    public void addIncompleteRequest(Request request){
        incompleteRequests.put(request.getRequestId(), request);
    }

    public void removeIncompleteRequest(int requestId) {
        incompleteRequests.remove(requestId);
    }

    public List<Request> getIncompleteRequests() {
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return "IP = " + this.getIp() + ", Port = "+ this.getPort() + " and Status = " + this.getStatus();
    }
}
