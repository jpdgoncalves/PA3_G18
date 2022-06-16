package Monitor;

import Messages.LBStatus;
import Messages.ServerStatus;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class THeartbeatChecker extends Thread{

    private static final int SLEEPTIME = 5000;
    private int maxHeartbeatsLost;
    private Socket socket;

    /**
     * THeartbeatChecker constructor
     * @param maxHeartbeatsLost maximum number of heartbeats that can be lost before the entity is removed.
     */
    public THeartbeatChecker(int maxHeartbeatsLost, ServerSocket serverSocket) {
        this.maxHeartbeatsLost = maxHeartbeatsLost;

        try {
            this.socket = serverSocket.accept();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends heartbeats to all servers and LBs and removes the ones that are non-responsive.
     * @throws IOException
     */
    private void sendHeartbeats() throws IOException {

        //check servers
        Object[] keysSrv = Monitor.getListLB().keySet().toArray();

        if (keysSrv.length == 0) {
            System.out.println("here" + keysSrv.length);
            for (int i = 0; i < keysSrv.length; i++) {
                ServerStatus value = Monitor.getListServers().get(keysSrv);

                if (value.getHeartbeat() >= maxHeartbeatsLost) { //discard
                    Monitor.removeLB(keysSrv[i].toString());

                } else { //send heartbeat
                    /*Socket socket = new Socket(value.getIp(), value.getPort());
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(new Request(0,0,0,4,0,0,0, "", 0));

                    System.out.println("?<-Heartbeat");
                    oos.flush();
                    oos.close();*/
                }
            }
        }


        //check LBs
        Object[] keysLB = Monitor.getListLB().keySet().toArray();

        if (keysLB.length == 0) {
            for (int i = 0; i < keysLB.length; i++) {

                LBStatus value = Monitor.getListLB().get(keysLB[i]);

                if (value.getHeartbeat() >= maxHeartbeatsLost) { //discard
                    Monitor.removeLB(keysLB[i].toString());

                } else { //send heartbeat
                /*Socket socket = new Socket(value.getIp(), value.getPort());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(new Request(0,0,0,4,0,0,0, "", 0));

                System.out.println("?<-Heartbeat");
                oos.flush();
                oos.close();*/
                }

            }
        }
    }

    /**
     * Life cycle of the thread
     */
    @Override
    public void run() {
        System.out.println("Heartbeat start!");

        while (true) {
            try {

                sendHeartbeats();
                Thread.sleep(SLEEPTIME);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
