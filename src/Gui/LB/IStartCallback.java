package Gui.LB;

/**
 * Interface for the call backs to implement in GUIs
 */
public interface IStartCallback {
    void call(String ip, int port, int id, String mIp, int mPort);
}
