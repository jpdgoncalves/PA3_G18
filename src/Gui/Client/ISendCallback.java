package Gui.Client;

/**
 * Single method interface that defines
 * the callback for when the client send button
 * is pressed
 */
public interface ISendCallback {
    /**
     * Method called when the send button is pressed.
     * @param ip IP address as specified in the interface.
     * @param port Port number as specified in the interface.
     * @param ni Number of iterations as specified in the interface.
     * @param deadline Deadline as specified in the interface.
     */
    void call(String ip, int port, int ni, int deadline);
}
