package Messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerStateMessage implements Serializable {
    //serverId, ServerStatus
    HashMap<Integer, ServerStatus> requestsPerServers = new HashMap<>();

    public ServerStateMessage(){

    }

    public void addServer(int serverId, ServerStatus status){
        requestsPerServers.put(serverId, status);
    }

    public HashMap<Integer, ServerStatus> getRequestsPerServers(){
        return requestsPerServers;
    }

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
