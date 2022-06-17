package Gui.LB;

import Gui.Common.Components.BorderButtonPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class FooterPanel extends JPanel {
    private final BorderButtonPanel startButton = new BorderButtonPanel("Start");
    private final BorderButtonPanel stopButton = new BorderButtonPanel("Stop");

    public FooterPanel() {
        super();
        setLayout(new GridLayout(1, 2));
        setBorder(new EmptyBorder(10, 10,10, 10));
        setMaximumSize(new Dimension(Short.MAX_VALUE, 70));

        add(startButton);
        add(stopButton);
    }

    protected void addStartActionListener(ActionListener listener) {
        startButton.addActionListener(listener);
    }

    protected void addStopActionListener(ActionListener listener) {
        stopButton.addActionListener(listener);
    }

    protected void setStartEnabled(boolean enabled) {
        startButton.setEnabled(enabled);
    }

    protected void setStopEnabled(boolean enabled) {
        stopButton.setEnabled(enabled);
    }
}
