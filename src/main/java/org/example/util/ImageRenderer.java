package org.example.util;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.File;

public class ImageRenderer extends DefaultTableCellRenderer {
    private static final String ASSETS_DIR = "assets/cover/";

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        JLabel label = new JLabel();
        label.setHorizontalAlignment(JLabel.CENTER);

        if (value != null) {
            String fileName = value.toString();
            File file = new File(ASSETS_DIR + fileName);

            if (!file.exists()) {
                file = new File(ASSETS_DIR + "default.jpg"); // fallback default
            }

            if (file.exists()) {
                ImageIcon icon = new ImageIcon(file.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(60, 80, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(img));
            } else {
                label.setText("No Cover");
            }
        } else {
            label.setText("No Data");
        }

        label.setOpaque(true);
        label.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);

        return label;
    }
}
