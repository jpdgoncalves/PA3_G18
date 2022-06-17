package Gui.Common.Components;

import Messages.Request;

public class RequestTablePanel extends TablePanel {

    public RequestTablePanel(String title, String[] fields) {
        super(title, fields);
    }

    public void addRequest(Request request) {
        int requestId = request.getRequestId();
        Object[] data = requestToObjectArray(request);
        addRow(requestId, data);
    }

    public void removeRequest(int requestId) {
        removeRow(requestId);
    }

    private Object[] requestToObjectArray(Request request) {
        return new Object[] {
                request.getClientId(), request.getServerId(),
                request.getCode(), request.getNr_iterations(),
                request.getPi(), request.getDeadline(),
                request.getTarget_IP(), request.getTargetPort()
        };
    }
}
