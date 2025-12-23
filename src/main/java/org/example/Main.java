package org.example;

import org.example.gui.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Buat data admin default jika belum ada

        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}

