package Monitor;

import Messages.LBStatus;
import Messages.Request;
import Messages.ServerStatus;

import java.util.*;

/**
 * Store data for the monitor
 */
public class MonitorData {
    private static final List<Request> EMPTY_REQUEST_LIST = Collections.unmodifiableList(new ArrayList<>());

    private final HashMap<Integer, ServerStatus> listServers = new HashMap<>();
    private final HashMap<Integer, LBStatus> listLB = new HashMap<>();
    private final HashMap<String, Object> heartbeats = new HashMap<>();
    private LBStatus primaryLb = null;

    /**
     * Add Load Balancer
     * @param status Load Balancer status
     */
    public synchronized void addLb(LBStatus status) {
        if (primaryLb == null) primaryLb = status;
        listLB.put(status.getId(), status);
        heartbeats.put(serializeToKey(status.getIp(), status.getPort()), status);
    }

    /**
     * Remove Load Balancer
     * @param lbId Load Balancer Id
     * @return the old port of the Load Balancer
     */
    public synchronized int removeLb(int lbId) {
        LBStatus status = listLB.remove(lbId);
        if (status == null) return 0;
        heartbeats.remove(serializeToKey(status.getIp(), status.getPort()));

        if (status != primaryLb) return 0;
        LBStatus newPrimary = getLbList().get(0);
        int oldPort = newPrimary.getPort();
        heartbeats.remove(serializeToKey(newPrimary.getIp(), newPrimary.getPort()));
        newPrimary.setPort(status.getPort());
        primaryLb = newPrimary;
        heartbeats.put(serializeToKey(newPrimary.getIp(), newPrimary.getPort()), newPrimary);

        return oldPort;
    }

    /**
     * Get primary Load Balancer
     * @return the LBStatus of primary LB
     */
    public synchronized LBStatus getPrimaryLb() {
        return primaryLb;
    }

    /**
     * Get the list of Load Balancer
     * @return the list of LB
     */
    public synchronized List<LBStatus> getLbList() {
        return new ArrayList<>(listLB.values());
    }

    /**
     * Add a server
     * @param status ServerStatus of server to add
     */
    public synchronized void addServer(ServerStatus status) {
        listServers.put(status.getId(), status);
        heartbeats.put(serializeToKey(status.getIp(), status.getPort()), status);
    }

    /**
     * Remove server
     * @param serverId Server id of server to remove
     */
    public synchronized void removeServer(int serverId) {
        ServerStatus status = listServers.remove(serverId);
        if (status == null) return;
        heartbeats.remove(serializeToKey(status.getIp(), status.getPort()));
    }

    /**
     * reset Heartbeat
     * @param ip the ip
     * @param port the port
     */
    public synchronized void resetHeartbeat(String ip, int port) {
        String hbKey = serializeToKey(ip, port);
        if (!heartbeats.containsKey(hbKey)) return;

        Object status = heartbeats.get(hbKey);

        if (status instanceof ServerStatus) {
            ((ServerStatus) status).setHeartbeat(0);
        } else if (status instanceof LBStatus){
            ((LBStatus) status).setHeartbeat(0);
        }
    }

    /**
     * Increment the Load Balancer heartbeat
     * @param status LBStatus of LB to increment
     * @return the new heartbeat
     */
    public synchronized int incrementLbHeartbeat(LBStatus status) {
        String hbKey = serializeToKey(status.getIp(), status.getPort());
        if (!heartbeats.containsKey(hbKey)) return 0;

        status.setHeartbeat(status.getHeartbeat() + 1);
        return status.getHeartbeat();
    }

    /**
     * Increment the Server heartbeat
     * @param status ServerStatus of server to increment
     * @return the new heartbeat
     */
    public synchronized int incrementServerHeartbeat(ServerStatus status) {
        String hbKey = serializeToKey(status.getIp(), status.getPort());
        if (!heartbeats.containsKey(hbKey)) return 0;

        status.setHeartbeat(status.getHeartbeat() + 1);
        return status.getHeartbeat();
    }

    /**
     * Get server list
     * @return server list
     */
    public synchronized List<ServerStatus> getServerList() {
        return new ArrayList<>(listServers.values());
    }

    /**
     * Add server request
     * @param request request to add
     */
    public synchronized void addServerRequest(Request request) {
        if (!listServers.containsKey(request.getServerId())) return;

        listServers.get(request.getServerId()).addIncompleteRequest(request);
    }

    /**
     * Remove server requst
     * @param serverId id of server
     * @param requestId request id to remove
     */
    public synchronized void removeServerRequest(int serverId, int requestId) {
        if (!listServers.containsKey(serverId)) return;

        listServers.get(serverId).removeIncompleteRequest(requestId);
    }

    /**
     * Get server Requests
     * @param serverId server id from which retrieve requests
     * @return list of request
     */
    public synchronized List<Request> getServerRequests(int serverId) {
        if (!listServers.containsKey(serverId)) return MonitorData.EMPTY_REQUEST_LIST;

        return new ArrayList<>(listServers.get(serverId).getIncompleteRequests());
    }

    /**
     * Add LB request
     * @param request request to add
     */
    public synchronized void addLbRequest(Request request) {
        if (!listLB.containsKey(request.getServerId())) return;

        listLB.get(request.getServerId()).addManagedRequest(request);
    }

    /**
     * Remove LB request
     * @param lbId id of LB
     * @param requestId request id
     */
    public synchronized void removeLbRequest(int lbId, int requestId) {
        if (!listLB.containsKey(lbId)) return;

        listLB.get(lbId).removeManagedRequest(requestId);
    }

    /**
     * Get LB requests
     * @param lbId id of LB to retrieve requests
     * @return list of requests from lb
     */
    public synchronized List<Request> getLbRequests(int lbId) {
        if (!listLB.containsKey(lbId)) return MonitorData.EMPTY_REQUEST_LIST;

        return new ArrayList<>(listLB.get(lbId).getManagedRequests());
    }

    /**
     * Serialize to key an array of object
     * @param values the multiple objects
     * @return a String with the object serialized
     */
    private String serializeToKey(Object ...values) {
        StringBuilder builder = new StringBuilder();
        for (Object value: values) {
            builder.append(value.toString());
            builder.append(":");
        }
        return builder.toString();
    }
}
