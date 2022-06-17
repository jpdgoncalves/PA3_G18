package Gui.Server;

public interface IStartCallback {
    void call(String ip, String port, String id, String mIp, String mPort);
}
