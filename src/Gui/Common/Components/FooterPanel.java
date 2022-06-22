package Gui.Common.Components;

import Gui.Common.IStopCallback;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Component added at the bottom of several
 * interfaces, displaying Ip address, Port number and a unique Id.
 * It also includes a stop button intended to be used to stop
 * the program and set it back to its configuration interface.
 */
public class FooterPanel extends JPanel {
    private final LabeledTextFieldPanel ipField = new LabeledTextFieldPanel("IP:", "");
    private final LabeledTextFieldPanel portField = new LabeledTextFieldPanel("Port:", "");
    private final LabeledTextFieldPanel idField = new LabeledTextFieldPanel("Id:", "");
    private final BorderButtonPanel stopButton = new BorderButtonPanel("Stop");

    private IStopCallback callback = () -> System.out.println("Stop clicked");

    /**
     * Creates a new instance of the footer panel.
     */
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

    /**
     * Sets the ip address field.
     * @param ip The ip to display.
     */
    public void setIp(String ip) {
        ipField.setValue(ip);
    }

    /**
     * Sets the port number field.
     * @param port The port number to display.
     */
    public void setPort(String port) {
        portField.setValue(port);
    }

    /**
     * Sets the unique id field.
     * @param id The id to display.
     */
    public void setId(String id) {
        idField.setValue(id);
    }

    /**
     * Sets the stop callback for the stop button.
     * @param callback The stop callback.
     */
    public void setStopCallback(IStopCallback callback) {
        this.callback = callback;
    }
}
