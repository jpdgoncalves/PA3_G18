package Gui.Common.Components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LabeledTextFieldPanel extends JPanel {
    private final JLabel label = new JLabel();
    private final JTextField textField = new JTextField();

    public LabeledTextFieldPanel(String labelText, String defaultText) {
        super();
        setMaximumSize(new Dimension(Short.MAX_VALUE, 50));
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setBorder(new EmptyBorder(10,10,10,10));

        label.setText(labelText);
        label.setAlignmentX(LEFT_ALIGNMENT);
        label.setFont(new Font("Arial", Font.PLAIN, 14));

        textField.setText(defaultText);
        textField.setAlignmentX(LEFT_ALIGNMENT);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));

        add(label);
        add(Box.createRigidArea(new Dimension(5,0)));
        add(textField);
    }

    public String getValue() {
        return textField.getText();
    }

    public void setValue(String value) {
        textField.setText(value);
    }

    public void setEditable(boolean editable) {
        textField.setEditable(false);
    }
}
