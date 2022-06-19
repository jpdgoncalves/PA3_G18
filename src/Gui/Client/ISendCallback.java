package Gui.Client;

public interface ISendCallback {
    void call(String ip, int port, int ni, int deadline);
}
