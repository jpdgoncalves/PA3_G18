package Gui.Server;

import Gui.Common.Components.BorderButtonPanel;
import Gui.Common.Components.LabeledTextFieldPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ServerConfigFrame extends JFrame {
    private final JPanel mainPanel = new JPanel();

    private final LabeledTextFieldPanel ipField = new LabeledTextFieldPanel("Server IP:", "localhost");
    private final LabeledTextFieldPanel portField = new LabeledTextFieldPanel("Server Port:", "88888");
    private final LabeledTextFieldPanel idField = new LabeledTextFieldPanel("Server ID:", "99");
    private final LabeledTextFieldPanel mIpField = new LabeledTextFieldPanel("Monitor IP:", "localhost");
    private final LabeledTextFieldPanel mPortField = new LabeledTextFieldPanel("Monitor Port:", "77777");

    private final BorderButtonPanel startButton = new BorderButtonPanel("Start");

    private IStartCallback callback = (ip, port, id, mIp, mPort) -> {
        System.out.println(ip + " " + port + " " + id + " " + mIp + " " + mPort);
    };

    public ServerConfigFrame() {
        this(500, 500);
    }

    public ServerConfigFrame(int width, int height) {
        super();
        setTitle("Server Configuration");
        setSize(width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Ensuring that all elements are left aligned
        ipField.setAlignmentX(LEFT_ALIGNMENT);
        portField.setAlignmentX(LEFT_ALIGNMENT);
        idField.setAlignmentX(LEFT_ALIGNMENT);
        mIpField.setAlignmentX(LEFT_ALIGNMENT);
        mPortField.setAlignmentX(LEFT_ALIGNMENT);
        startButton.setAlignmentX(LEFT_ALIGNMENT);
        // Box layout respects the maximum size and nothing else
        startButton.setMaximumSize(new Dimension(200, 70));
        startButton.addActionListener((e) -> {
            startButton.setEnabled(false);
            int port = Integer.parseInt(portField.getValue());
            int id = Integer.parseInt(idField.getValue());
            int mPort = Integer.parseInt(mPortField.getValue());
            this.callback.call(
                    ipField.getValue(), port, id,
                    mIpField.getValue(), mPort
            );
            startButton.setEnabled(true);
        });

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.add(ipField);
        mainPanel.add(portField);
        mainPanel.add(idField);
        mainPanel.add(mIpField);
        mainPanel.add(mPortField);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(startButton);
        add(mainPanel);
    }

    public void setStartCallback(IStartCallback callback) {
        this.callback = callback;
    }
}
