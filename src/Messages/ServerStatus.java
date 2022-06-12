package Messages;

import java.util.HashMap;

public class ServerStatus {
    private String ip;
    private int port;
    private int status; //0 = down and 1 = up
    private HashMap<Integer, Request> incompleteRequests; //<requestId, Request>
    private HashMap<Integer, Request> completeRequests; //<requestId, Request>

    public ServerStatus(String ip, int port, int status){
        this.ip = ip;
        this.port = port;
        this.status = status;
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

    public HashMap<Integer, Request> getIncompleteRequests() {
        return incompleteRequests;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public void addCompleteRequest(int requestId, Request request){
        completeRequests.put(requestId, request);
    }

    public void addIncompleteRequest(int requestId, Request request){
        incompleteRequests.put(requestId, request);
    }
}
