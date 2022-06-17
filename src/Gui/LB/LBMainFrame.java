package Gui.LB;

import Gui.Common.Components.LabeledTextFieldPanel;
import Gui.Common.IStopCallback;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LBMainFrame extends JFrame {
    private final JPanel mainPanel = new JPanel();

    private final LabeledTextFieldPanel ipField = new LabeledTextFieldPanel("LB IP:", "localhost");
    private final LabeledTextFieldPanel portField = new LabeledTextFieldPanel("LB Port:", "88888");
    private final LabeledTextFieldPanel mIpField = new LabeledTextFieldPanel("Monitor IP:", "localhost");
    private final LabeledTextFieldPanel mPortField = new LabeledTextFieldPanel("Monitor Port:", "77777");

    private final FooterPanel footerPanel = new FooterPanel();

    private IStartCallback startCallback = (ip, port, mIp, mPort) -> {
        System.out.println(ip + " " + port + " " + mIp + " " + mPort);
    };

    private IStopCallback stopCallback = () -> System.out.println("LB Stop");

    public LBMainFrame() {
        this(500, 500);
    }

    public LBMainFrame(int width, int height) {
        super();
        setTitle("Load Balancer Configuration");
        setSize(width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        ipField.setAlignmentX(LEFT_ALIGNMENT);
        portField.setAlignmentX(LEFT_ALIGNMENT);
        mIpField.setAlignmentX(LEFT_ALIGNMENT);
        mPortField.setAlignmentX(LEFT_ALIGNMENT);
        footerPanel.setAlignmentX(LEFT_ALIGNMENT);

        footerPanel.addStartActionListener((e) -> {
            this.startCallback.call(
                    ipField.getValue(), portField.getValue(),
                    mIpField.getValue(), mPortField.getValue()
            );
        });
        footerPanel.addStopActionListener((e) -> {
            this.stopCallback.call();
        });
        footerPanel.setStopEnabled(false);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.add(ipField);
        mainPanel.add(portField);
        mainPanel.add(mIpField);
        mainPanel.add(mPortField);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(footerPanel);
        add(mainPanel);
    }

    public void setStartCallback(IStartCallback callback) {
        startCallback = callback;
    }

    public void setStopCallback(IStopCallback callback) {
        stopCallback = callback;
    }

    public void setStartEnabled(boolean enabled) {
        footerPanel.setStartEnabled(enabled);
    }

    public void setStopEnabled(boolean enabled) {
        footerPanel.setStopEnabled(enabled);
    }
}
