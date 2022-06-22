package Gui.Monitor;

import Gui.Common.IStopCallback;
import Messages.Request;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Main interface of the monitor.
 * It contains two tabs one for the requests managed
 * by the load balancer and request being processed by each
 * server, and the other for the status of the load balancers
 * and servers.
 */
public class MonitorMainFrame extends JFrame {
    private final JPanel mainPanel = new JPanel();

    private final RequestsTab requestsTab = new RequestsTab();
    private final UpDownTab upDownTab = new UpDownTab();
    private final JTabbedPane tabbedPane = new JTabbedPane();

    private final FooterPanel footerPanel = new FooterPanel();

    /**
     * Creates a new main monitor interface
     * with the default size.
     */
    public MonitorMainFrame() {
        this(1000, 800);
    }

    /**
     * Creates a new main monitor interface
     * with the specified width and height.
     * @param width Width in pixels.
     * @param height Height in pixels.
     */
    public MonitorMainFrame(int width, int height) {
        super();
        setTitle("Monitor");
        setSize(width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));
        tabbedPane.add("Requests", requestsTab);
        tabbedPane.add("Up/Down", upDownTab);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.add(tabbedPane);
        mainPanel.add(footerPanel);
        add(mainPanel);
    }

    /**
     * Adds a lb to the interface.
     * @param id Id of the lb.
     * @param ip Ip address of the lb.
     * @param port Port number of the lb.
     */
    public void addLb(int id, String ip, int port) {
        upDownTab.addLb(id, ip, port);
    }

    /**
     * Adds a server ot the interface.
     * @param id Id of the server.
     * @param ip Ip of the server.
     * @param port Port of the server.
     */
    public void addServer(int id, String ip, int port) {
        upDownTab.addServer(id, ip, port);
        requestsTab.addServer(id);
    }

    /**
     * Remove a server from the interface.
     * @param id The server to remove.
     */
    public void removeServer(int id) {
        requestsTab.removeServer(id);
    }

    /**
     * Sets the primary state of a LB.
     * @param id Id of the lb.
     * @param isPrimary Whether the LB is primary or not.
     */
    public void setIsLbPrimary(int id, boolean isPrimary) {
        upDownTab.setIsLbPrimary(id, isPrimary);
    }

    /**
     * Sets the alive state of a LB.
     * @param id Id of the lb.
     * @param isAlive Whether the LB is alive or not.
     */
    public void setIsLbAlive(int id, boolean isAlive) {
        upDownTab.setIsLbAlive(id, isAlive);
    }

    /**
     * Sets the port of a lb.
     * @param id The id of the lb to set.
     * @param port The port number of the lb.
     */
    public void setLbPort(int id, int port) {
        upDownTab.setLbPort(id, port);
    }

    /**
     * Sets the alive state of a server.
     * @param id The id of the server.
     * @param isAlive Whether the server is alive or not.
     */
    public void setIsServerAlive(int id, boolean isAlive) {
        upDownTab.setIsServerAlive(id, isAlive);
    }

    /**
     * Adds a load balancer managed request.
     * @param request The request to add.
     */
    public void addLbRequest(Request request) {
        requestsTab.addLbRequest(request);
    }

    /**
     * Adds a server request that is being processed.
     * @param request The request to add.
     */
    public void addServerRequest(Request request) {
        requestsTab.addServerRequest(request);
    }

    /**
     * Removes a request being managed by the lb.
     * @param id The id of the request.
     */
    public void removeLbRequest(int id) {
        requestsTab.removeLbRequest(id);
    }

    /**
     * Removes a request being processed by a server.
     * @param id The id of the request.
     */
    public void removeServerRequest(int id) {
        requestsTab.removeServerRequest(id);
    }

    /**
     * Sets the ip of the monitor to be displayed.
     * @param ip The ip of the monitor
     */
    public void setIp(String ip) {
        footerPanel.setIp(ip);
    }

    /**
     * Sets the port of the monitor to be displayed.
     * @param port The port of the monitor.
     */
    public void setPort(int port) {
        footerPanel.setPort(port);
    }

    /**
     * Sets the callback for when the stop button is pressed.
     * @param callback The callback for the stop button press.
     */
    public void setStopCallback(IStopCallback callback) {
        footerPanel.setStopCallback(callback);
    }
}
