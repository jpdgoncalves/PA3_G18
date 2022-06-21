package Monitor;

import Gui.Monitor.MonitorMainFrame;
import Messages.LBStatus;
import Messages.Request;
import Messages.ServerStatus;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class THeartbeatChecker extends Thread{

    private static final int SLEEPTIME = 2000;
    private final int maxHeartbeatsLost;
    private final MonitorData monitorData;
    private final MonitorMainFrame gui;

    /**
     * THeartbeatChecker constructor
     *
     * @param maxHeartbeatsLost maximum number of heartbeats that can be lost before the entity is removed.
     */
    public THeartbeatChecker(int maxHeartbeatsLost, MonitorData data, MonitorMainFrame gui) {
        this.maxHeartbeatsLost = maxHeartbeatsLost;
        this.monitorData = data;
        this.gui = gui;
    }

    /**
     * Sends heartbeats to all servers and LBs and removes the ones that are non-responsive.
     */
    private void sendHeartbeats() {

        //check servers
        List<ServerStatus> serverStatuses = monitorData.getServerList();
        //System.out.println("keysSRV.length -> " + keysSrv.length);
        for (ServerStatus status: serverStatuses) {
            int hbCount = monitorData.incrementServerHeartbeat(status);

            if (hbCount < maxHeartbeatsLost) {
                sendRequest(
                        status.getIp(), status.getPort(),
                        new Request(
                                0, 0, 0, 4,
                                0, "", 0, "", 0
                        )
                );
            } else {
                System.out.println("Removing server " + status);
                monitorData.removeServer(status.getId());

                for (LBStatus lbStatus : monitorData.getLbList()) {
                    sendRequest(
                            lbStatus.getIp(), lbStatus.getPort(),
                            new Request(
                                    0, 0, status.getId(), 8,
                                    0, "", 0, "", 0
                            )
                    );
                }

                gui.removeServer(status.getId());
                gui.setIsServerAlive(status.getId(), false);
            }
        }

        List<LBStatus> lbStatuses = monitorData.getLbList();
        for (LBStatus status: lbStatuses) {
            int hbCount = monitorData.incrementLbHeartbeat(status);

            if (hbCount < maxHeartbeatsLost) {
                sendRequest(
                        status.getIp(), status.getPort(),
                        new Request(
                                0, 0, 0, 4,
                                0, "", 0, "", 0
                        )
                );
            } else {
                System.out.println("Removing lb " + status);
                gui.setIsLbAlive(status.getId(), false);
                if (monitorData.getPrimaryLb() != status) return;

                monitorData.removeLb(status.getId());
                LBStatus sLbStatus = monitorData.getPrimaryLb();
                sendRequest(
                        sLbStatus.getIp(), sLbStatus.getPort(),
                        new Request(
                                0, 0, 0, 9,
                                0, "", 0, "", 0
                        )
                );

                gui.setIsLbPrimary(status.getId(), false);
                gui.setIsLbPrimary(sLbStatus.getId(), true);
            }
        }
    }

    private void sendRequest(String ip, int port, Request request) {
        Socket socket = null;
        ObjectOutputStream oos  = null;

        try {
            System.out.println("Sending request " + request);

            socket = new Socket(ip, port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(request);

            System.out.println("Sent request " + request);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (oos != null) oos.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Life cycle of the thread
     */
    @Override
    public void run() {
        System.out.println("Heartbeat start!");

        while (!this.isInterrupted()) {
            try {
                sendHeartbeats();
                Thread.sleep(SLEEPTIME);
            } catch (InterruptedException ignored) {
                System.out.println("Stopping the hearbeat");
            }
        }
    }
}
