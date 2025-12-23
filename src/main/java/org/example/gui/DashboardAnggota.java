package org.example.gui;

import javax.swing.*;

public class DashboardAnggota extends JFrame {
    public DashboardAnggota() {
        setTitle("Dashboard Anggota");
        setSize(800, 500);
        setLayout(null);

        JButton btnPinjam = new JButton("Pinjam Buku");
        JButton btnLogout = new JButton("Logout");

        btnPinjam.setBounds(50,50,150,40);
        btnLogout.setBounds(50,100,150,40);

        add(btnPinjam);
        add(btnLogout);

        setVisible(true);
    }
}
