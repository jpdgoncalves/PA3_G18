package Gui.LB;

import Gui.Common.Components.BorderButtonPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * FootPanel of Load Balancer
 */
public class FooterPanel extends JPanel {
    private final BorderButtonPanel startButton = new BorderButtonPanel("Start");
    private final BorderButtonPanel stopButton = new BorderButtonPanel("Stop");

    /**
     * Constructor
     */
    public FooterPanel() {
        super();
        setLayout(new GridLayout(1, 2));
        setBorder(new EmptyBorder(10, 10,10, 10));
        setMaximumSize(new Dimension(Short.MAX_VALUE, 70));

        add(startButton);
        add(stopButton);
    }

    /**
     * Add start action listener
     * @param listener listener to add on start button
     */
    protected void addStartActionListener(ActionListener listener) {
        startButton.addActionListener(listener);
    }

    /**
     * Add stop action listener
     * @param listener listener to add on stop button
     */
    protected void addStopActionListener(ActionListener listener) {
        stopButton.addActionListener(listener);
    }

    /**
     * Set start enable or not
     * @param enabled if the start is enabled
     */
    protected void setStartEnabled(boolean enabled) {
        startButton.setEnabled(enabled);
    }

    /**
     * Set stop enable or not
     * @param enabled if the stop is enabled
     */
    protected void setStopEnabled(boolean enabled) {
        stopButton.setEnabled(enabled);
    }
}
