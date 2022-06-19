package Gui.Client;

import Gui.Common.Components.FooterPanel;
import Gui.Common.Components.RequestTablePanel;
import Gui.Common.IStopCallback;
import Messages.Request;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ClientMainFrame extends JFrame {
    private final JPanel mainPanel = new JPanel();
    private final NewRequestPanel requestPanel = new NewRequestPanel();
    private final RequestTablePanel pendingPanel = new RequestTablePanel(
            "Pending Requests:",
            new String[] {
                    "Request Id", "Client Id", "Server Id",
                    "Nº Iterations", "Code", "Pi", "Deadline",
                    "Target Ip", "Target Port"
            }
    );
    private final RequestTablePanel executedPanel = new RequestTablePanel(
            "Executed Request:",
            new String[] {
                    "Request Id", "Client Id", "Server Id",
                    "Nº Iterations", "Code", "Pi", "Deadline",
                    "Target Ip", "Target Port"
            }
    );
    private final FooterPanel footerPanel = new FooterPanel();

    public ClientMainFrame() {
        this(1000, 800);
    }

    public ClientMainFrame(int width, int height) {
        super();
        setTitle("Client");
        setSize(width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        mainPanel.add(requestPanel);
        mainPanel.add(pendingPanel);
        mainPanel.add(executedPanel);
        mainPanel.add(footerPanel);
        add(mainPanel);
    }

    public void setIp(String ip) {
        footerPanel.setIp(ip);
    }

    public void setPort(int port) {
        footerPanel.setPort("" + port);
    }

    public void setId(int id) {
        footerPanel.setId("" + id);
    }

    public void setSendCallback(ISendCallback callback) {
        requestPanel.setSendCallback(callback);
    }

    public void setStopCallback(IStopCallback callback) {
        footerPanel.setStopCallback(callback);
    }

    public void addPendingRequest(Request request) {
        pendingPanel.addRequest(request);
    }

    public void removePendingRequest(int requestId) {
        pendingPanel.removeRequest(requestId);
    }

    public void addExecutedRequest(Request request) {
        executedPanel.addRequest(request);
    }

    public void removeExecutedRequest(int requestId) {
        executedPanel.removeRequest(requestId);
    }
}
