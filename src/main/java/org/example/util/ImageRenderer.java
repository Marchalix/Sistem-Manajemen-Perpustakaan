package org.example.util;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.File;

public class ImageRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = new JLabel();
        label.setHorizontalAlignment(JLabel.CENTER);

        // Jika value adalah path gambar
        if (value != null) {
            String path = value.toString();
            File file = new File(path);

            if (file.exists()) {
                // Resize gambar agar muat di baris tabel
                ImageIcon icon = new ImageIcon(path);
                Image img = icon.getImage().getScaledInstance(60, 80, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(img));
            } else {
                label.setText("No Cover"); // Jika file tidak ditemukan
            }
        } else {
            label.setText("No Data");
        }

        // Warna Background saat diklik
        if (isSelected) {
            label.setBackground(table.getSelectionBackground());
            label.setOpaque(true);
        } else {
            label.setBackground(Color.WHITE);
            label.setOpaque(true);
        }

        return label;
    }
}