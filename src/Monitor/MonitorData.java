package Monitor;

import Messages.LBStatus;
import Messages.Request;
import Messages.ServerStatus;

import java.util.*;

public class MonitorData {
    private static final List<Request> EMPTY_REQUEST_LIST = Collections.unmodifiableList(new ArrayList<>());

    private final HashMap<Integer, ServerStatus> listServers = new HashMap<>();
    private final HashMap<Integer, LBStatus> listLB = new HashMap<>();
    private final HashMap<String, Object> heartbeats = new HashMap<>();

    public synchronized void addLb(LBStatus status) {
        listLB.put(status.getId(), status);
        heartbeats.put(serializeToKey(status.getIp(), status.getPort()), status);
    }

    public synchronized void removeLb(int lbId) {
        LBStatus status = listLB.remove(lbId);
        if (status == null) return;
        heartbeats.remove(serializeToKey(status.getIp(), status.getPort()));
    }

    public synchronized List<LBStatus> getLbList() {
        return new ArrayList<>(listLB.values());
    }

    public synchronized void addServer(ServerStatus status) {
        listServers.put(status.getId(), status);
        heartbeats.put(serializeToKey(status.getIp(), status.getPort()), status);
    }

    public synchronized void removeServer(int serverId) {
        ServerStatus status = listServers.remove(serverId);
        if (status == null) return;
        heartbeats.remove(serializeToKey(status.getIp(), status.getPort()));
    }

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

    public synchronized int incrementLbHeartbeat(LBStatus status) {
        String hbKey = serializeToKey(status.getIp(), status.getPort());
        if (!heartbeats.containsKey(hbKey)) return 0;

        status.setHeartbeat(status.getHeartbeat() + 1);
        return status.getHeartbeat();
    }

    public synchronized int incrementServerHeartbeat(ServerStatus status) {
        String hbKey = serializeToKey(status.getIp(), status.getPort());
        if (!heartbeats.containsKey(hbKey)) return 0;

        status.setHeartbeat(status.getHeartbeat() + 1);
        return status.getHeartbeat();
    }

    public synchronized List<ServerStatus> getServerList() {
        return new ArrayList<>(listServers.values());
    }

    public synchronized void addServerRequest(Request request) {
        if (!listServers.containsKey(request.getServerId())) return;

        listServers.get(request.getServerId()).addIncompleteRequest(request);
    }

    public synchronized void removeServerRequest(int serverId, int requestId) {
        if (!listServers.containsKey(serverId)) return;

        listServers.get(serverId).removeIncompleteRequest(requestId);
    }

    public synchronized List<Request> getServerRequests(int serverId) {
        if (!listServers.containsKey(serverId)) return MonitorData.EMPTY_REQUEST_LIST;

        return new ArrayList<>(listServers.get(serverId).getIncompleteRequests());
    }

    public synchronized void addLbRequest(Request request) {
        if (!listLB.containsKey(request.getServerId())) return;

        listLB.get(request.getServerId()).addManagedRequest(request);
    }

    public synchronized void removeLbRequest(int lbId, int requestId) {
        if (!listLB.containsKey(lbId)) return;

        listLB.get(lbId).removeManagedRequest(requestId);
    }

    public synchronized List<Request> getLbRequests(int lbId) {
        if (!listLB.containsKey(lbId)) return MonitorData.EMPTY_REQUEST_LIST;

        return new ArrayList<>(listLB.get(lbId).getManagedRequests());
    }

    private String serializeToKey(Object ...values) {
        StringBuilder builder = new StringBuilder();
        for (Object value: values) {
            builder.append(value.toString());
            builder.append(":");
        }
        return builder.toString();
    }
}
