package Gui.Monitor;

import Gui.Common.Components.TablePanel;

import javax.swing.*;

/**
 * Tabs displaying the status of the servers
 * and load balancers.
 */
class UpDownTab extends JPanel {
    private final TablePanel lbsTable = new TablePanel(
            "Load Balancers",
            new String[] {"Id", "IP", "Port", "Primary", "Status"}
    );
    private final TablePanel serversTable = new TablePanel(
            "Servers",
            new String[] {"Id", "IP", "Port", "Status"}
    );

    /**
     * Creates a new up down tab.
     */
    public UpDownTab() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(lbsTable);
        add(serversTable);
    }

    /**
     * Adds a new load balancer entry.
     * @param id Id of the lb.
     * @param ip Ip of the lb.
     * @param port port of the lb.
     */
    public void addLb(int id, String ip, int port) {
        lbsTable.addRow(id, new Object[] {ip, port, false, "DEAD"});
    }

    /**
     * Adds a new server entry.
     * @param id Id of the server.
     * @param ip Ip of the server.
     * @param port Port of the server.
     */
    public void addServer(int id, String ip, int port) {
        serversTable.addRow(id, new Object[] {ip, port, "DEAD"});
    }

    /**
     * Sets the primary state of an lb
     * @param id Id of the lb.
     * @param isPrimary Whether it is primary or not.
     */
    public void setIsLbPrimary(int id, boolean isPrimary) {
        lbsTable.setValueAt(id, "Primary", isPrimary);
    }

    /**
     * Sets the alive state of an lb.
     * @param id Id of the lb.
     * @param isAlive Whether it is alive or not.
     */
    public void setIsLbAlive(int id, boolean isAlive) {
        lbsTable.setValueAt(id, "Status", isAlive ? "ALIVE" : "DEAD");
    }

    /**
     * Sets the port of an lb.
     * @param id Id of the lb.
     * @param port Port of the lb.
     */
    public void setLbPort(int id, int port) {
        lbsTable.setValueAt(id, "Port", port);
    }

    /**
     * Sets the alive state of a server.
     * @param id Id of the server.
     * @param isAlive Whether it is alive or not.
     */
    public void setIsServerAlive(int id, boolean isAlive) {
        serversTable.setValueAt(id, "Status", isAlive ? "ALIVE" : "DEAD");
    }
}
