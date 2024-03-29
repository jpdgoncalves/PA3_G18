package Gui.LB;

import Gui.Common.Components.LabeledTextFieldPanel;
import Gui.Common.IStopCallback;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Load balancer main Frame
 */
public class LBMainFrame extends JFrame {
    private final JPanel mainPanel = new JPanel();

    private final LabeledTextFieldPanel ipField = new LabeledTextFieldPanel("LB IP:", "localhost");
    private final LabeledTextFieldPanel portField = new LabeledTextFieldPanel("LB Port:", "22222");
    private final LabeledTextFieldPanel idField = new LabeledTextFieldPanel("LB Id:", "666");
    private final LabeledTextFieldPanel mIpField = new LabeledTextFieldPanel("Monitor IP:", "localhost");
    private final LabeledTextFieldPanel mPortField = new LabeledTextFieldPanel("Monitor Port:", "5056");

    private final FooterPanel footerPanel = new FooterPanel();

    private IStartCallback startCallback = (ip, port, id, mIp, mPort) -> {
        System.out.println(ip + " " + port + " " + id + " " + mIp + " " + mPort);
    };

    private IStopCallback stopCallback = () -> System.out.println("LB Stop");

    /**
     * Constructor
     */
    public LBMainFrame() {
        this(500, 500);
    }

    /**
     * Constructor
     * @param width with of frmae
     * @param height height of frame
     */
    public LBMainFrame(int width, int height) {
        super();
        setTitle("Load Balancer");
        setSize(width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        ipField.setAlignmentX(LEFT_ALIGNMENT);
        portField.setAlignmentX(LEFT_ALIGNMENT);
        idField.setAlignmentX(LEFT_ALIGNMENT);
        mIpField.setAlignmentX(LEFT_ALIGNMENT);
        mPortField.setAlignmentX(LEFT_ALIGNMENT);
        footerPanel.setAlignmentX(LEFT_ALIGNMENT);

        footerPanel.addStartActionListener((e) -> {
            int port = Integer.parseInt(portField.getValue());
            int id = Integer.parseInt(idField.getValue());
            int mPort = Integer.parseInt(mPortField.getValue());
            this.startCallback.call(
                    ipField.getValue(), port, id,
                    mIpField.getValue(), mPort
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
        mainPanel.add(idField);
        mainPanel.add(mIpField);
        mainPanel.add(mPortField);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(footerPanel);
        add(mainPanel);
    }

    /**
     * Set edition of frame
     * @param value if we can edit or not
     */
    public void setCanEdit(boolean value) {
        ipField.setEditable(value);
        portField.setEditable(value);
        idField.setEditable(value);
        mIpField.setEditable(value);
        mPortField.setEditable(value);
    }

    /**
     * Set the start call back
     * @param callback the call back when start
     */
    public void setStartCallback(IStartCallback callback) {
        startCallback = callback;
    }

    /**
     * Set the stop call back
     * @param callback the call back when stop
     */
    public void setStopCallback(IStopCallback callback) {
        stopCallback = callback;
    }

    /**
     * Set the start button enabled
     * @param enabled tells if the start is enabled
     */
    public void setStartEnabled(boolean enabled) {
        footerPanel.setStartEnabled(enabled);
    }

    /**
     * Set the stop button enabled
     * @param enabled tells if the stop is enabled
     */
    public void setStopEnabled(boolean enabled) {
        footerPanel.setStopEnabled(enabled);
    }

    /**
     * Set Load Balancer port
     * @param port port of Load Balancer
     */
    public void setLbPort(int port) {
        SwingUtilities.invokeLater(() -> {
            portField.setValue("" + port);
        });
    }
}
