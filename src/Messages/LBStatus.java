package Messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Load Balancer Status message
 */
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

    /**
     * Get heartbeat
     * @return heartbeat
     */
    public int getHeartbeat() {
        return heartbeat;
    }

    /**
     * Set heatbeat
     * @param heartbeat hearbeat
     */
    public void setHeartbeat(int heartbeat) {
        this.heartbeat = heartbeat;
    }

    /**
     * Set Load Balancer IP
     * @param ip new Load Balancer IP
     */
    public void setIp(String ip) {
            this.ip = ip;
        }

    /**
     * Set Load Balancer port
      * @param port new Load Balancer port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Set Load Balancer status
     * @param status new Load Balancer status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Get Load Balancer IP
     * @return Load Balancer IP
     */
    public String getIp() {
        return ip;
    }

    /**
     * Get Load Balancer port
     * @return Load Balancer port
     */
    public int getPort() {
        return port;
    }

    /**
     * Get Load Balancer status
     * @return Load Balancer status
     */
    public int getStatus() {
        return status;
    }

    /**
     * Get Load Balancer id
     * @return Load Balancer id
     */
    public int getId() {
        return id;
    }

    /**
     * Set Load Balancer id
     * @param id new Load Balancer id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get Load Balancer managed requests
     * @return Load Balancer managed requests
     */
    public List<Request> getManagedRequests() {
        return new ArrayList<>(managedRequests.values());
    }

    /**
     * Add managed request
     * @param request managed request
     */
    public void addManagedRequest(Request request) {
        managedRequests.put(request.getRequestId(), request);
    }

    /**
     * Remove managed request
     * @param requestId request id of the request to be remove
     */
    public void removeManagedRequest(int requestId) {
        managedRequests.remove(requestId);
    }

    @Override
    public String toString() {
        return "IP = " + this.getIp() + ", Port = "+ this.getPort() + " and Status = " + this.getStatus();
    }
}
