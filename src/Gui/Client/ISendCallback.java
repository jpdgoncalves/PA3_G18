package Gui.Client;

import java.io.IOException;

public interface ISendCallback {
    void call(String ip, String port, String ni, String deadline);
}
