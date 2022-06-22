package Gui.Common.Components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Simple button that has a invisible 10 pixel
 * border around it.
 */
public class BorderButtonPanel extends JPanel {
    public final JButton button;

    /**
     * Creates a new instance of a bordered button.
     * @param text The text to display in the button.
     */
    public BorderButtonPanel(String text) {
        setLayout(new GridLayout(1,1));
        setBorder(new EmptyBorder(10,10,10,10));
        button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        add(button);
    }

    /**
     * Sets the button to either enabled or disabled.
     * @param enabled The state of the button.
     */
    public void setEnabled(boolean enabled) {
        button.setEnabled(enabled);
    }

    /**
     * Adds an action listener to the native button component.
     * @param listener The listener to add to the button.
     */
    public void addActionListener(ActionListener listener) {
        button.addActionListener(listener);
    }
}
