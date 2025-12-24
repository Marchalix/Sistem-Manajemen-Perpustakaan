package org.example;

import org.example.data.MemberManager;
import org.example.gui.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Buat data admin default jika belum ada
        SwingUtilities.invokeLater(() -> {
            MemberManager.initAdmin();
            new LoginFrame().setVisible(true);
        });
    }
}

