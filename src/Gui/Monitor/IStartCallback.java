package Gui.Monitor;

/**
 * Defines a single method interface
 * that is used as a callback for when the start
 * button of the monitor is pressed.
 */
public interface IStartCallback {
    /**
     * Method called when the start button is pressed.
     * @param ip The ip in the configuration interface.
     * @param port The port number in the configuration interface.
     */
    void call(String ip, int port);
}
