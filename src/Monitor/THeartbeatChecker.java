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
     * @throws IOException
     */
    private void sendHeartbeats() throws IOException {

        //check servers
        List<ServerStatus> serverStatuses = monitorData.getServerList();
        //System.out.println("keysSRV.length -> " + keysSrv.length);
        for (ServerStatus status: serverStatuses) {
            int hbCount = monitorData.incrementServerHeartbeat(status);

            if (hbCount < maxHeartbeatsLost) {
                try {
                    Socket socket = new Socket(status.getIp(), status.getPort());


                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(new Request(
                            0, 0, 0, 4,
                            0, "", 0, "", 0
                    ));

                    oos.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                monitorData.removeServer(status.getId());
                gui.removeServer(status.getId());
                gui.setIsServerAlive(status.getId(), false);
            }
        }

        List<LBStatus> lbStatuses = monitorData.getLbList();
        for (LBStatus status: lbStatuses) {
            int hbCount = monitorData.incrementLbHeartbeat(status);

            if (hbCount < maxHeartbeatsLost) {
                try {
                    System.out.println("Sending a heartbeat to lb " + status);
                    Socket socket = new Socket(status.getIp(), status.getPort());


                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(new Request(
                            0, 0, 0, 4,
                            0, "", 0, "", 0
                    ));
                    System.out.println("Sent the hearbeat lb" + status);

                    oos.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Removing lb " + status);
                monitorData.removeLb(status.getId());
                gui.setIsLbAlive(status.getId(), false);
            }
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
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException ignored) {
                System.out.println("Stopping the hearbeat");
            }
        }
    }
}
