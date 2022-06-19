package Gui.Server;

public interface IStartCallback {
    void call(String ip, int port, int id, String mIp, int mPort);
}
