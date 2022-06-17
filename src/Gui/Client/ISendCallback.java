package Gui.Client;

public interface ISendCallback {
    void call(String ip, String port, String ni, String deadline);
}
