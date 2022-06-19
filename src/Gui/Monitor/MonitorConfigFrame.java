package Gui.Monitor;

import Gui.Common.Components.BorderButtonPanel;
import Gui.Common.Components.LabeledTextFieldPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MonitorConfigFrame extends JFrame {
    private final JPanel mainPanel = new JPanel();

    private final LabeledTextFieldPanel ipField = new LabeledTextFieldPanel("Client IP: ", "localhost");
    private final LabeledTextFieldPanel portField = new LabeledTextFieldPanel("Client Port: ", "5056");
    private final BorderButtonPanel startButton = new BorderButtonPanel("Start");

    private IStartCallback callback = (ip, port) -> System.out.println(ip + " " + port);

    public MonitorConfigFrame() {
        this(500, 500);
    }

    public MonitorConfigFrame(int width, int height) {
        super();
        setTitle("Monitor Configuration");
        setSize(width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        ipField.setAlignmentX(LEFT_ALIGNMENT);
        portField.setAlignmentX(LEFT_ALIGNMENT);
        startButton.setAlignmentX(LEFT_ALIGNMENT);

        startButton.setMaximumSize(new Dimension(200, 70));
        startButton.addActionListener((e) -> {
            startButton.setEnabled(false);
            int port = Integer.parseInt(portField.getValue());
            this.callback.call(ipField.getValue(), port);
            startButton.setEnabled(true);
        });

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.add(ipField);
        mainPanel.add(portField);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(startButton);
        add(mainPanel);
    }

    public void setStartCallback(IStartCallback callback) {
        this.callback = callback;
    }
}
