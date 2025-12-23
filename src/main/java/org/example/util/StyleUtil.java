package org.example.util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class StyleUtil {
    // Palet Warna (Modern Dark Blue Theme)
    public static final Color PRIMARY_COLOR = new Color(41, 54, 63);   // Dark Blue (Sidebar)
    public static final Color ACCENT_COLOR = new Color(230, 126, 34);  // Orange (Tombol/Highlight)
    public static final Color BG_COLOR = new Color(236, 240, 241);     // Light Gray (Background)
    public static final Color TEXT_COLOR = new Color(44, 62, 80);      // Dark Grey (Teks)
    public static final Color WHITE = Color.WHITE;

    // Font Standar
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_SUBHEADER = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);

    // Method untuk mempercantik Tombol
    public static void styleButton(JButton btn) {
        btn.setBackground(ACCENT_COLOR);
        btn.setForeground(WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20)); // Padding dalam tombol
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Method untuk mempercantik Tabel
    public static void styleTable(JTable table) {
        table.setRowHeight(30); // Tinggi baris agar tidak sempit
        table.setShowVerticalLines(false);
        table.setFont(FONT_BODY);
        table.setSelectionBackground(new Color(52, 152, 219)); // Warna saat diklik
        table.setSelectionForeground(WHITE);

        // Header Tabel
        table.getTableHeader().setFont(FONT_SUBHEADER);
        table.getTableHeader().setBackground(PRIMARY_COLOR);
        table.getTableHeader().setForeground(WHITE);
        table.getTableHeader().setOpaque(false);

        // Rata Tengah untuk sel tertentu
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
    }
}