package Gui.Monitor;

import Gui.Common.Components.TablePanel;

import javax.swing.*;

class UpDownTab extends JPanel {
    private final TablePanel lbsTable = new TablePanel(
            "Load Balancers",
            new String[] {"Id", "IP", "Port", "Primary", "Status"}
    );
    private final TablePanel serversTable = new TablePanel(
            "Servers",
            new String[] {"Id", "IP", "Port", "Status"}
    );

    public UpDownTab() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(lbsTable);
        add(serversTable);
    }

    public boolean hasLb(int id) {
        return lbsTable.hasRow(id);
    }

    public boolean hasServer(int id) {
        return serversTable.hasRow(id);
    }

    public void addLb(int id, String ip, int port) {
        lbsTable.addRow(id, new Object[] {ip, port, false, "DEAD"});
    }

    public void addServer(int id, String ip, int port) {
        serversTable.addRow(id, new Object[] {ip, port, "DEAD"});
    }

    public void setIsLbPrimary(int id, boolean isPrimary) {
        lbsTable.setValueAt(id, "Primary", isPrimary);
    }

    public void setIsLbAlive(int id, boolean isAlive) {
        lbsTable.setValueAt(id, "Status", isAlive ? "ALIVE" : "DEAD");
    }

    public void setIsServerAlive(int id, boolean isAlive) {
        serversTable.setValueAt(id, "Status", isAlive ? "ALIVE" : "DEAD");
    }
}
