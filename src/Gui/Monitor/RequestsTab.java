package Gui.Monitor;

import Gui.Common.Components.RequestTablePanel;
import Messages.Request;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Tab that displays the requests.
 */
class RequestsTab extends JPanel {
    private final JPanel contentPanel = new JPanel();
    private final JScrollPane scroller = new JScrollPane(contentPanel);
    private final RequestTablePanel lbPanel = new RequestTablePanel(
            "Load Balancer",
            new String[] {
                    "Request Id", "Client Id", "Server Id",
                    "Nº Iterations", "Code", "Pi", "Deadline",
                    "Target Ip", "Target Port"
            }
    );
    private final HashMap<Integer, RequestTablePanel> serverTableMapper = new HashMap<>();
    private final HashMap<Integer, Integer> requestIdToServer = new HashMap<>();

    /**
     * Creates a new requests tab.
     */
    public RequestsTab() {
        super();
        setLayout(new GridLayout(1, 1));
        lbPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 300));

        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.add(lbPanel);

        add(scroller);
    }

    /**
     * Tests whether the tab is already displaying
     * a certain server.
     * @param id The id of the server.
     * @return Whether the server is being displayed.
     */
    public boolean hasServer(int id) {
        return serverTableMapper.containsKey(id);
    }

    /**
     * Adds a server ot the interface.
     * @param id Id of the server.
     */
    public void addServer(int id) {
        SwingUtilities.invokeLater(() -> {
            if (hasServer(id)) return;

            RequestTablePanel newRequestPanel = new RequestTablePanel(
                    "Server " + id,
                    new String[] {
                            "Request Id", "Client Id", "Server Id",
                            "Nº Iterations", "Code", "Pi", "Deadline",
                            "Target Ip", "Target Port"
                    }
            );
            newRequestPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 300));

            serverTableMapper.put(id, newRequestPanel);
            contentPanel.add(newRequestPanel);
        });
    }

    /**
     * Remove a server from the interface.
     * @param id The server to remove.
     */
    public void removeServer(int id) {
        SwingUtilities.invokeLater(() -> {
            if (!hasServer(id)) return;

            RequestTablePanel toRemove = serverTableMapper.get(id);

            serverTableMapper.remove(id);
            contentPanel.remove(toRemove);
            updateRequestIdToServer(id);
        });
    }

    private void updateRequestIdToServer(int serverId) {
        HashSet<Integer> requestIds = new HashSet<>();

        for (Map.Entry<Integer, Integer> entry: requestIdToServer.entrySet()) {
            if (entry.getValue() == serverId) requestIds.add(entry.getKey());
        }

        for (Integer requestId: requestIds) {
            requestIdToServer.remove(requestId);
        }
    }

    /**
     * Adds a server request that is being processed.
     * @param request The request to add.
     */
    public void addServerRequest(Request request) {
        SwingUtilities.invokeLater(() -> {
            if (!hasServer(request.getServerId())) return;

            RequestTablePanel requestTable = serverTableMapper.get(request.getServerId());
            requestTable.addRequest(request);
            requestIdToServer.put(request.getRequestId(), request.getServerId());
        });
    }

    /**
     * Removes a request being processed by a server.
     * @param requestId The id of the request.
     */
    public void removeServerRequest(int requestId) {
        SwingUtilities.invokeLater(() -> {
            if (!requestIdToServer.containsKey(requestId)) return;

            int serverId = requestIdToServer.get(requestId);

            if (!hasServer(serverId)) return;

            RequestTablePanel requestTable = serverTableMapper.get(serverId);
            requestTable.removeRequest(requestId);
            requestIdToServer.remove(requestId);
        });
    }

    /**
     * Adds a load balancer managed request.
     * @param request The request to add.
     */
    public void addLbRequest(Request request) {
        lbPanel.addRequest(request);
    }

    /**
     * Removes a request being managed by the lb.
     * @param requestId The id of the request.
     */
    public void removeLbRequest(int requestId) {
        lbPanel.removeRequest(requestId);
    }
}
