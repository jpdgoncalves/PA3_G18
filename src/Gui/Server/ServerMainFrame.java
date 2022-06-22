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

    /**
     * Constructor of ServerMainFrame
     */
    public ServerMainFrame() {
        this(1000, 800);
    }

    /**
     * Constructor of ServerMainFrame
     *
     * @param width width of the frame
     * @param height height of the frame
     */
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

    /**
     * Sets the ip
     * @param ip ip to be set
     */
    public void setIp(String ip) {
        footerPanel.setIp(ip);
    }

    /**
     * Sets the port
     * @param port port to be set
     */
    public void setPort(int port) {
        footerPanel.setPort("" + port);
    }

    /**
     * Sets the id
     * @param id id to be set
     */
    public void setId(int id) {
        footerPanel.setId("" + id);
    }

    /**
     * Sets the stop callback
     * @param callback callback
     */
    public void setStopCallback(IStopCallback callback) {
        footerPanel.setStopCallback(callback);
    }

    /**
     * Adds a received request
     * @param request request to add
     */
    public void addReceivedRequest(Request request) {
        receivedPanel.addRequest(request);
    }

    /**
     * Removes a received request given its id
     * @param requestId id of the request to remove
     */
    public void removeReceivedRequest(int requestId) {
        receivedPanel.removeRequest(requestId);
    }

    /**
     * Adds a processed request
     * @param request request to add
     */
    public void addProcessedRequest(Request request) {
        processedPanel.addRequest(request);
    }

    /**
     * Removes a processed request
     * @param requestId id of the request to remove
     */
    public void removeProcessedRequest(int requestId) {
        processedPanel.removeRequest(requestId);
    }
}
