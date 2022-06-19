package Gui.Server;

import Gui.Common.Components.FooterPanel;
import Gui.Common.Components.RequestTablePanel;
import Gui.Common.IStopCallback;
import Messages.Request;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ServerMainFrame extends JFrame {
    private final JPanel mainPanel = new JPanel();
    private final RequestTablePanel receivedPanel = new RequestTablePanel(
            "Requests Received:",
            new String[] {
                    "Request Id", "Client Id", "Server Id",
                    "Nº Iterations", "Code", "Pi", "Deadline",
                    "Target Ip", "Target Port"
            }
    );
    private final RequestTablePanel processedPanel = new RequestTablePanel(
            "Requests Processed:",
            new String[] {
                    "Request Id", "Client Id", "Server Id",
                    "Nº Iterations", "Code", "Pi", "Deadline",
                    "Target Ip", "Target Port"
            }
    );
    private final FooterPanel footerPanel = new FooterPanel();

    public ServerMainFrame() {
        this(1000, 800);
    }

    public ServerMainFrame(int width, int height) {
        super();
        setTitle("Server");
        setSize(width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        mainPanel.add(receivedPanel);
        mainPanel.add(processedPanel);
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

    public void setStopCallback(IStopCallback callback) {
        footerPanel.setStopCallback(callback);
    }

    public void addReceivedRequest(Request request) {
        receivedPanel.addRequest(request);
    }

    public void removeReceivedRequest(int requestId) {
        receivedPanel.removeRequest(requestId);
    }

    public void addProcessedRequest(Request request) {
        processedPanel.addRequest(request);
    }

    public void removeProcessedRequest(int requestId) {
        processedPanel.removeRequest(requestId);
    }
}
