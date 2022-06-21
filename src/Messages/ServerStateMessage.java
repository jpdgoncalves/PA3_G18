package Messages;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Server state message
 */
public class ServerStateMessage implements Serializable {
    //serverId, ServerStatus
    HashMap<Integer, ServerStatus> requestsPerServers = new HashMap<>();

    public ServerStateMessage(){

    }

    /**
     * Add server to list
     * @param serverId server id
     * @param status server status
     */
    public void addServer(int serverId, ServerStatus status){
        requestsPerServers.put(serverId, status);
    }

    /**
     * Get requests per server
     * @return requests per server
     */
    public HashMap<Integer, ServerStatus> getRequestsPerServers(){
        return requestsPerServers;
    }

    /**
     * Get server with less occupation
     * @return the Server Status of server with less occupation
     */
    public ServerStatus getServerWithLessOccupation(){
        ServerStatus serverIdWithLessOccupation = null;
        int numberRequestServer = 25; //higher than the maximum possible for a server
        for (Map.Entry<Integer, ServerStatus> entry : requestsPerServers.entrySet())
            if (entry.getValue().getIncompleteRequests().size() < numberRequestServer) {
                serverIdWithLessOccupation = entry.getValue();
                numberRequestServer = entry.getValue().getIncompleteRequests().size();
                break;
            }

        return serverIdWithLessOccupation;
    }

    @Override
    public String toString() {
        String tmp = "";
        for (Map.Entry<Integer, ServerStatus> entry : requestsPerServers.entrySet()){
            tmp += "\nserverId = " + entry.getKey() + " has server status : <" + entry.getValue() + ">";
        }

        return tmp;
    }
}
