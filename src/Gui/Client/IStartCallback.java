package Gui.Client;

/**
 * Single method interface that defines
 * the callback for when the client start button
 * is pressed
 */
public interface IStartCallback {
    /**
     * Method called when start is pressed.
     * @param ip IP address in the interface.
     * @param port Port number in the interface.
     * @param id Client id in the interface.
     */
    public void call(String ip, int port, int id);
}
