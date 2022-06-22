package Gui.Server;

/**
 * Interface for the callback
 */
public interface IStartCallback {
    /**
     * Method called when start button is pressed
     * @param ip server ip
     * @param port server port
     * @param id server id
     * @param mIp monitor ip
     * @param mPort monitor port
     */
    void call(String ip, int port, int id, String mIp, int mPort);
}
