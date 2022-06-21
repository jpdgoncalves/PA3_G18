package Messages;

import java.io.Serializable;

/**
 * Request format for communications
 */
public class Request implements Serializable {
    private int clientId;
    private int requestId;
    private int serverId;
    private int code;
    private int nr_iterations;
    private String pi;
    private int deadline;
    private String target_IP;
    private int targetPort;

    /**
     * Constructor
     * @param clientId Client id
     * @param requestId Request id
     * @param serverId Server id
     * @param code Code
     * @param nr_iterations Number of iterations
     * @param pi Pi
     * @param deadline Deadline
     * @param target_IP Target IP
     * @param targetPort Target port
     */
    public Request(int clientId, int requestId, int serverId, int code, int nr_iterations, String pi, int deadline, String target_IP, int targetPort) {
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

    /**
     * Get client id
     * @return client id
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * Get request id
     * @return request id
     */
    public int getRequestId() {
        return requestId;
    }

    /**
     * Get server id
     * @return server id
     */
    public int getServerId() {
        return serverId;
    }

    /**
     * Set server id
     * @param serverId new server id
     */
    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getCode() {
        return code;
    }

    /**
     * Set Code of request
     * @param code new code of request
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Get number of iterations
     * @return number of iterations
     */
    public int getNr_iterations() {
        return nr_iterations;
    }

    /**
     * Set number of iterations
     * @param nr_iterations new Number of iterations
     */
    public void setNr_iterations(int nr_iterations) {
        this.nr_iterations = nr_iterations;
    }

    /**
     * Get Pi
     * @return Pi
     */
    public String getPi() {
        return pi;
    }

    /**
     * Set pi
     * @param pi value of Pi
     */
    public void setPi(String pi) {
        this.pi = pi;
    }

    /**
     * Get Deadline
     * @return Deadline
     */
    public int getDeadline() {
        return deadline;
    }

    /**
     * Get target IP
     * @return target IP
     */
    public String getTarget_IP() {
        return target_IP;
    }

    /**
     * Set target IP
     * @param target_ip new target IP
     */
    public void setTarget_IP(String target_ip) { this.target_IP = target_ip; }

    /**
     * Get target port
     * @return target port
     */
    public int getTargetPort() {
        return targetPort;
    }

    /**
     * Set target port
     * @param targetPort new target port
     */
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
