package Messages;

import java.io.Serializable;

public class Request implements Serializable {

    private int clientId;
    private int requestId;
    private int serverId;
    private int code;
    private int nr_iterations;
    private int pi;
    private int deadline;
    private String target_IP;
    private int targetPort;

    public Request(int clientId, int requestId, int serverId, int code, int nr_iterations, int pi, int deadline, String target_IP, int targetPort) {
        this.clientId = clientId;
        this.requestId = requestId;
        this.serverId = serverId;
        this.code = code;
        this.nr_iterations = nr_iterations;
        this.pi = pi;
        this.deadline = deadline;
        this.target_IP = target_IP;
        this.targetPort = targetPort;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getNr_iterations() {
        return nr_iterations;
    }

    public void setNr_iterations(int nr_iterations) {
        this.nr_iterations = nr_iterations;
    }

    public int getPi() {
        return pi;
    }

    public void setPi(int pi) {
        this.pi = pi;
    }

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public String getTarget_IP() {
        return target_IP;
    }

    public void setTarget_IP(String target_ip) { this.target_IP = target_ip; }

    public int getTargetPort() {
        return targetPort;
    }

    public void setTargetPort(int targetPort) {
        this.targetPort = targetPort;
    }

    @Override
    public String toString() {
        return "Client ID = " + getClientId() + ", requestId = " + getRequestId() + ", serverId = " + getServerId() +
                ", code =" + getCode() + ", nr_iterations = " + getNr_iterations() + ", pi = "+ getPi() +
                ", deadline = " + getDeadline() + ", targetIP = " + getTarget_IP() + ", target port = " + getTargetPort();
    }
}
