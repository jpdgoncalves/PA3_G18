package Messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Server Status
 */
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

    /**
     * Set Server IP
     * @param ip new Server IP
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * Set Server port
     * @param port new Server port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Set server status
     * @param status new server status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Get Server Heartbeat
     * @return server heartbeat
     */
    public int getHeartbeat() {
        return heartbeat;
    }

    /**
     * Set Server Heartbeat
     * @param heartbeat new Server heartbeat
     */
    public void setHeartbeat(int heartbeat) {
        this.heartbeat = heartbeat;
    }

    public String getIp() {
        return ip;
    }

    /**
     * Get Server Port
     * @return Server port
     */
    public int getPort() {
        return port;
    }

    /**
     * Get Server Status
     * @return Server Status
     */
    public int getStatus() {
        return status;
    }

    /**
     * Get Server id
     * @return Server id
     */
    public int getId() {
        return id;
    }

    /**
     * Set server id
     * @param id Server id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Add incomplete request
     * @param request Request of incomplete request to add
     */
    public void addIncompleteRequest(Request request){
        incompleteRequests.put(request.getRequestId(), request);
    }

    public void removeIncompleteRequest(int requestId) {
        incompleteRequests.remove(requestId);
    }

    /**
     * Get incomplete requests
     * @return incomplete request
     */
    public List<Request> getIncompleteRequests() {
        return new ArrayList<>(incompleteRequests.values());
    }

    @Override
    public String toString() {
        return "IP = " + this.getIp() + ", Port = "+ this.getPort() + " and Status = " + this.getStatus();
    }
}
