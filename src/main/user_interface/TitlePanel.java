package main.user_interface;

import javax.swing.*;
import java.awt.*;

// TODO make the technician reuse this too
public class TitlePanel {
    JPanel titlePanel;

    public TitlePanel(String title) {
        this.titlePanel = new JPanel(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel(title);
        Font boldFont = new Font(titleLabel.getFont().getName(), Font.BOLD, titleLabel.getFont().getSize());
        titleLabel.setFont(boldFont);
        titlePanel.add(titleLabel, BorderLayout.WEST);
    }

    public JPanel getTitlePanel() {
        return titlePanel;
    }
}
