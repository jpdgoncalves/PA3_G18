package Gui.Client;

import Gui.Common.Components.FooterPanel;
import Gui.Common.Components.RequestTablePanel;
import Gui.Common.IStopCallback;
import Messages.Request;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Client Main interface shown
 * when the client is up.
 */
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

    /**
     * Creates a new instance of the client main interface
     * with the default width and height.
     */
    public ClientMainFrame() {
        this(1000, 800);
    }

    /**
     * Creates a new client main interface
     * with the specified width and height.
     * @param width Width of the interface in pixels.
     * @param height Height of the interface in pixels.
     */
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

    /**
     * Sets the ip address on the footer panel.
     * @param ip The ip address to display.
     */
    public void setIp(String ip) {
        footerPanel.setIp(ip);
    }

    /**
     * Sets the port number on the footer panel.
     * @param port The port number to display.
     */
    public void setPort(int port) {
        footerPanel.setPort("" + port);
    }

    /**
     * Sets the client id on the footer panel.
     * @param id The client id to display.
     */
    public void setId(int id) {
        footerPanel.setId("" + id);
    }

    /**
     * Sets the function to be called when the
     * Send button is pressed.
     * @param callback Function to be called when send is pressed.
     */
    public void setSendCallback(ISendCallback callback) {
        requestPanel.setSendCallback(callback);
    }

    /**
     * Sets the function to be called when the
     * Stop button is pressed.
     * @param callback
     */
    public void setStopCallback(IStopCallback callback) {
        footerPanel.setStopCallback(callback);
    }

    /**
     * Adds a request to the pending table.
     * @param request The request to add.
     */
    public void addPendingRequest(Request request) {
        pendingPanel.addRequest(request);
    }

    /**
     * Remove a request from the pending table.
     * @param requestId The id of the request to remove.
     */
    public void removePendingRequest(int requestId) {
        pendingPanel.removeRequest(requestId);
    }

    /**
     * Add a request to the executed table.
     * @param request The request to add.
     */
    public void addExecutedRequest(Request request) {
        executedPanel.addRequest(request);
    }
}
