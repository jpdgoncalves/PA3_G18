package Gui.Common.Components;

import Gui.Common.IStopCallback;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class FooterPanel extends JPanel {
    private final LabeledTextFieldPanel ipField = new LabeledTextFieldPanel("IP:", "");
    private final LabeledTextFieldPanel portField = new LabeledTextFieldPanel("Port:", "");
    private final LabeledTextFieldPanel idField = new LabeledTextFieldPanel("Id:", "");
    private final BorderButtonPanel stopButton = new BorderButtonPanel("Stop");

    private IStopCallback callback = () -> System.out.println("Stop clicked");

    public FooterPanel() {
        super();
        setLayout(new GridLayout(1, 4));
        setBorder(new EmptyBorder(10, 10,10, 10));

        ipField.setEditable(false);
        portField.setEditable(false);
        idField.setEditable(false);
        stopButton.addActionListener((e) -> {
            stopButton.setEnabled(false);
            this.callback.call();
            stopButton.setEnabled(true);
        });

        add(ipField);
        add(portField);
        add(idField);
        add(stopButton);
    }

    public void setIp(String ip) {
        ipField.setValue(ip);
    }

    public void setPort(String port) {
        portField.setValue(port);
    }

    public void setId(String id) {
        idField.setValue(id);
    }

    public void setStopCallback(IStopCallback callback) {
        this.callback = callback;
    }
}
