package Gui.Monitor;

import Gui.Common.IStopCallback;
import Messages.Request;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MonitorMainFrame extends JFrame {
    private final JPanel mainPanel = new JPanel();

    private final RequestsTab requestsTab = new RequestsTab();
    private final UpDownTab upDownTab = new UpDownTab();
    private final JTabbedPane tabbedPane = new JTabbedPane();

    private final FooterPanel footerPanel = new FooterPanel();

    public MonitorMainFrame() {
        this(1000, 800);
    }

    public MonitorMainFrame(int width, int height) {
        super();
        setTitle("Monitor Configuration");
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

    public boolean hasLb(int id) {
        return upDownTab.hasLb(id);
    }

    public boolean hasServer(int id) {
        return upDownTab.hasLb(id);
    }

    public void addLb(int id, String ip, int port) {
        upDownTab.addLb(id, ip, port);
    }

    public void addServer(int id, String ip, int port) {
        upDownTab.addServer(id, ip, port);
        requestsTab.addServer(id);
    }

    public void removeServer(int id) {
        requestsTab.removeServer(id);
    }

    public void setIsLbPrimary(int id, boolean isPrimary) {
        upDownTab.setIsLbPrimary(id, isPrimary);
    }

    public void setIsLbAlive(int id, boolean isAlive) {
        upDownTab.setIsLbAlive(id, isAlive);
    }

    public void setIsServerAlive(int id, boolean isAlive) {
        upDownTab.setIsServerAlive(id, isAlive);
    }

    public void addLbRequest(Request request) {
        requestsTab.addLbRequest(request);
    }

    public void addServerRequest(Request request) {
        requestsTab.addServerRequest(request);
    }

    public void removeLbRequest(int id) {
        requestsTab.removeLbRequest(id);
    }

    public void removeServerRequest(int id) {
        requestsTab.removeServerRequest(id);
    }

    public void setIp(String ip) {
        footerPanel.setIp(ip);
    }

    public void setPort(int port) {
        footerPanel.setPort(port);
    }

    public void setStopCallback(IStopCallback callback) {
        footerPanel.setStopCallback(callback);
    }
}
