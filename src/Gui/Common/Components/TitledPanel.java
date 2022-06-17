package Gui.Common.Components;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class TitledPanel extends JPanel {

    public TitledPanel(String title) {
        super();
        TitledBorder border = new TitledBorder(title);
        border.setTitleFont(new Font("Arial", Font.PLAIN, 14));
        setBorder(border);
    }
}
