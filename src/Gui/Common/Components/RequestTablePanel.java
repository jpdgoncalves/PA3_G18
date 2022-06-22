package Gui.Common.Components;

import Messages.Request;

/**
 * Table component specialized is displaying requests.
 */
public class RequestTablePanel extends TablePanel {

    /**
     * Creates a new request table panel
     * @param title Title of the table to display.
     * @param fields Fields that the table will have.
     */
    public RequestTablePanel(String title, String[] fields) {
        super(title, fields);
    }

    /**
     * Adds a new request to the table.
     * @param request The new request to display in the table.
     */
    public void addRequest(Request request) {
        int requestId = request.getRequestId();
        Object[] data = requestToObjectArray(request);
        addRow(requestId, data);
    }

    /**
     * Removes a request from the table.
     * @param requestId The id of the request to remove.
     */
    public void removeRequest(int requestId) {
        removeRow(requestId);
    }

    private Object[] requestToObjectArray(Request request) {
        return new Object[] {
                request.getClientId(), request.getServerId(),
                request.getNr_iterations(), request.getCode(),
                request.getPi(), request.getDeadline(),
                request.getTarget_IP(), request.getTargetPort()
        };
    }
}
