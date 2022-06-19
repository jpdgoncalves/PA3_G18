package Monitor;

import Messages.LBStatus;
import Messages.Request;
import Messages.ServerStatus;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class THeartbeatChecker extends Thread{

    private static final int SLEEPTIME = 2000;
    private final int maxHeartbeatsLost;

    /**
     * THeartbeatChecker constructor
     *
     * @param maxHeartbeatsLost maximum number of heartbeats that can be lost before the entity is removed.
     */
    public THeartbeatChecker(int maxHeartbeatsLost) throws IOException {
        this.maxHeartbeatsLost = maxHeartbeatsLost;
    }

    /**
     * Sends heartbeats to all servers and LBs and removes the ones that are non-responsive.
     * @throws IOException
     */
    private void sendHeartbeats() throws IOException {

        //check servers
        Object[] keysSrv = Monitor.getListServers().keySet().toArray();
        //System.out.println("keysSRV.length -> " + keysSrv.length);
        if (keysSrv.length != 0) {

            for (int i = 0; i < keysSrv.length; i++) {

                ServerStatus value = Monitor.getListServers().get(keysSrv[i]);

                String send_ip = value.getIp();
                int send_port = value.getPort();
                System.out.println("Sending to IP - " + send_ip + "  port - " + send_port);

                //new heartbeat
                value.setHeartbeat(value.getHeartbeat() + 1);
                //set it on its place
                Monitor.setServer(keysSrv[i].toString(), value);

                Socket socket = new Socket(send_ip, send_port);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(new Request(0,0,0,4,0,0,0, "", 0));

                //System.out.println("?<-Heartbeat srv");


            }
        }


        //check LBs
        Object[] keysLB = Monitor.getListLB().keySet().toArray();
        //System.out.println("keysLB.length -> " + keysLB.length);
        if (keysLB.length != 0) {
            for (int i = 0; i < keysLB.length; i++) {

                LBStatus value = Monitor.getListLB().get(keysLB[i]);

                String send_ip = value.getIp();
                int send_port = value.getPort();

                //new heartbeat val TODO not sure if +1 should be done here
                value.setHeartbeat(value.getHeartbeat() + 1);

                if (Monitor.getListLB().get(keysLB[i]).getHeartbeat() < maxHeartbeatsLost & Monitor.getListLB().get(keysLB[i]).getStatus() == 1) {
                    System.out.println("Sending to IP - " + send_ip + "  port - " + send_port + " w hb at -> " + Monitor.getListLB().get(keysLB[i]).getHeartbeat() + " status -> " + Monitor.getListLB().get(keysLB[i]).getStatus());

                    //set it on its place
                    Monitor.setLB(keysLB[i].toString(), value);

                    Socket socket = null;

                    socket = new Socket(send_ip, send_port);


                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(new Request(0, 0, 0, 4, 0, 0, 0, "", 0));

                    //System.out.println("?<-Heartbeat LB");

                    //System.out.println("new hb val - " + Monitor.getListLB().get(keysLB[i]).getHeartbeat());
                } else { //LB died, remove
                    Monitor.removeLB(keysLB[i].toString());
                    System.out.println("ERR not sending to IP - " + send_ip + "  port - " + send_port + " w hb at -> " + Monitor.getListLB().get(keysLB[i]).getHeartbeat() + " status -> " + Monitor.getListLB().get(keysLB[i]).getStatus());

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
                //throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
