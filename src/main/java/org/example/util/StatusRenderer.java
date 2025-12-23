package org.example.util;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class StatusRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // Panggil method bawaan dulu biar seleksi baris (warna biru saat diklik) tetap jalan
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Cek apakah value null agar tidak error
        if (value != null) {
            String status = value.toString();

            // Set Font agak tebal biar terbaca jelas
            c.setFont(new Font("Segoe UI", Font.BOLD, 12));

            // --- LOGIKA WARNA ---

            // 1. Status Buku (Di Admin)
            if ("Tersedia".equalsIgnoreCase(status)) {
                c.setForeground(new Color(39, 174, 96)); // Hijau (Aman)
            }
            else if ("Kosong".equalsIgnoreCase(status) || "Dipinjam".equalsIgnoreCase(status)) {
                c.setForeground(new Color(192, 57, 43)); // Merah (Warning)
            }

            // 2. Status Peminjaman (Di Anggota) - INI YANG BARU
            else if ("aktif".equalsIgnoreCase(status)) {
                c.setForeground(new Color(230, 126, 34)); // Oranye (Sedang berlangsung/Warning)
            }
            else if ("dikembalikan".equalsIgnoreCase(status)) {
                c.setForeground(new Color(41, 128, 185)); // Biru (Selesai/History)
            }

            // Default (Hitam)
            else {
                c.setForeground(Color.BLACK);
            }
        }

        return c;
    }
}