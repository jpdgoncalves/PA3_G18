package Gui.Monitor;

import Gui.Common.Components.BorderButtonPanel;
import Gui.Common.Components.LabeledTextFieldPanel;
import Gui.Common.IStopCallback;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Component that appears at the bottom
 * of the Monitor Main interface. It displays the
 * ip address, port number as well as a stop button,
 * intended to reopen the configuration interface.
 */
public class FooterPanel extends JPanel {
    private final LabeledTextFieldPanel ipField = new LabeledTextFieldPanel("IP:", "");
    private final LabeledTextFieldPanel portField = new LabeledTextFieldPanel("Port:", "");
    private final BorderButtonPanel stopButton = new BorderButtonPanel("Stop");

    private IStopCallback callback = () -> System.out.println("Stop clicked");

    /**
     * Instantiates a new footer component.
     */
    public FooterPanel() {
        super();
        setLayout(new GridLayout(1, 4));
        setBorder(new EmptyBorder(10, 10,10, 10));

        ipField.setEditable(false);
        portField.setEditable(false);
        stopButton.addActionListener((e) -> {
            stopButton.setEnabled(false);
            this.callback.call();
            stopButton.setEnabled(true);
        });

        add(ipField);
        add(portField);
        add(stopButton);
    }

    /**
     * Sets the ip to be displayed.
     * @param ip The ip to be displayed.
     */
    public void setIp(String ip) {
        ipField.setValue(ip);
    }

    /**
     * Sets the port number to be displayed.
     * @param port The port number to be displayed.
     */
    public void setPort(int port) {
        portField.setValue("" + port);
    }

    /**
     * Sets the callback for when the stop button is pressed.
     * @param callback callback for stop button press.
     */
    public void setStopCallback(IStopCallback callback) {
        this.callback = callback;
    }
}
