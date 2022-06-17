package Gui.Client;

import Gui.Common.Components.BorderButtonPanel;
import Gui.Common.Components.LabeledTextFieldPanel;
import Gui.Common.Components.TitledPanel;

import java.awt.*;

public class NewRequestPanel extends TitledPanel {
    private final LabeledTextFieldPanel ipField = new LabeledTextFieldPanel("IP: ", "localhost");
    private final LabeledTextFieldPanel portField = new LabeledTextFieldPanel("Port: ", "99999");
    private final LabeledTextFieldPanel niField = new LabeledTextFieldPanel(
            "Number of Iterations: ", "2"
    );
    private final LabeledTextFieldPanel deadlineField = new LabeledTextFieldPanel(
            "Deadline: ", "100"
    );
    private ISendCallback callback = (ip, port, ni, deadline) -> System.out.println(ip + " " + port + " " + ni + " " + deadline);

    public NewRequestPanel() {
        super("New Request: ");
        setLayout(new GridLayout(3,2));
        setMaximumSize(new Dimension(Short.MAX_VALUE, 200));

        BorderButtonPanel sendButton = new BorderButtonPanel("Send");
        sendButton.addActionListener((e) -> {
            sendButton.setEnabled(false);
            this.callback.call(ipField.getValue(), portField.getValue(), niField.getValue(), deadlineField.getValue());
            sendButton.setEnabled(true);
        });

        add(ipField);
        add(portField);
        add(niField);
        add(deadlineField);
        add(sendButton);
    }

    public void setSendCallback(ISendCallback callback) {
        this.callback = callback;
    }
}
