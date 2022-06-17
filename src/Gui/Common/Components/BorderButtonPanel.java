package Gui.Common.Components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class BorderButtonPanel extends JPanel {
    public final JButton button;

    public BorderButtonPanel(String text) {
        setLayout(new GridLayout(1,1));
        setBorder(new EmptyBorder(10,10,10,10));
        button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        add(button);
    }

    public void setEnabled(boolean enabled) {
        button.setEnabled(enabled);
    }

    public void addActionListener(ActionListener listener) {
        button.addActionListener(listener);
    }
}
