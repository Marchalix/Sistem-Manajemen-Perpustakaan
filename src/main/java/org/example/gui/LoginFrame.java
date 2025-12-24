package org.example.gui;

import org.example.data.MemberData;
import org.example.data.MemberManager;
import org.example.util.StyleUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField userField;
    private JPasswordField passField;

    public LoginFrame() {
        setTitle("Login Perpustakaan");
        setSize(400, 450); // Sedikit lebih tinggi untuk tombol baru
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(StyleUtil.BG_COLOR);

        // HEADER
        JLabel lblTitle = new JLabel("LOGIN SYSTEM", SwingConstants.CENTER);
        lblTitle.setFont(StyleUtil.FONT_HEADER);
        lblTitle.setForeground(StyleUtil.PRIMARY_COLOR);
        lblTitle.setBorder(new EmptyBorder(30, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // FORM PANEL
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(StyleUtil.BG_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Label Username
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(StyleUtil.FONT_BODY);
        formPanel.add(lblUser, gbc);

        // Input Username
        gbc.gridx = 0; gbc.gridy = 1;
        userField = new JTextField();
        userField.setPreferredSize(new Dimension(200, 35));
        userField.setFont(StyleUtil.FONT_BODY);
        userField.setBorder(BorderFactory.createCompoundBorder(
                userField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        formPanel.add(userField, gbc);

        // Label Password
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.insets = new Insets(15, 10, 5, 10);
        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(StyleUtil.FONT_BODY);
        formPanel.add(lblPass, gbc);

        // Input Password
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.insets = new Insets(5, 10, 5, 10);
        passField = new JPasswordField();
        passField.setPreferredSize(new Dimension(200, 35));
        passField.setFont(StyleUtil.FONT_BODY);
        passField.setBorder(BorderFactory.createCompoundBorder(
                passField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        formPanel.add(passField, gbc);

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(StyleUtil.BG_COLOR);
        wrapperPanel.add(formPanel, BorderLayout.CENTER);
        wrapperPanel.setBorder(new EmptyBorder(0, 40, 0, 40));
        add(wrapperPanel, BorderLayout.CENTER);

        // BUTTON PANEL
        JPanel btnPanel = new JPanel(new GridLayout(3, 1, 0, 5)); // Grid 2 baris
        btnPanel.setBackground(StyleUtil.BG_COLOR);
        btnPanel.setBorder(new EmptyBorder(20, 60, 10, 60)); // Margin kiri kanan agar tombol tidak terlalu lebar

        JButton btnLogin = new JButton("MASUK");
        btnLogin.setPreferredSize(new Dimension(100, 40));
        StyleUtil.styleButton(btnLogin);

        // Tombol Daftar (Desain beda dikit biar kontras)
        JButton btnSignup = new JButton("Belum punya akun? Daftar");
        btnSignup.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnSignup.setForeground(StyleUtil.PRIMARY_COLOR);
        btnSignup.setBackground(StyleUtil.BG_COLOR);
        btnSignup.setBorderPainted(false); // Hilangkan border kotak
        btnSignup.setFocusPainted(false);
        btnSignup.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Tombol Daftar (Desain beda dikit biar kontras)
        JButton btnForgot = new JButton("Lupa akun? Forgot Password");
        btnForgot.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnForgot.setForeground(StyleUtil.PRIMARY_COLOR);
        btnForgot.setBackground(StyleUtil.BG_COLOR);
        btnForgot.setBorderPainted(false); // Hilangkan border kotak
        btnForgot.setFocusPainted(false);
        btnForgot.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnPanel.add(btnLogin);
        btnPanel.add(btnSignup);
        btnPanel.add(btnForgot);
        add(btnPanel, BorderLayout.SOUTH);

        // LOGIC
        // 1. Logic Login
        btnLogin.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());

            MemberData member = MemberManager.login(user, pass);
            if (member != null) {
                this.dispose();
                if (member.role.equalsIgnoreCase("admin")) {
                    new DashboardAdmin().setVisible(true);
                } else {
                    new DashboardAnggota(member).setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Login Gagal! Cek Username/Password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnForgot.addActionListener(e -> {
            String namaAnggota = JOptionPane.showInputDialog(this, "Masukkan Username:");
            if (namaAnggota == null || namaAnggota.isEmpty()) return;

            String newPass = JOptionPane.showInputDialog (this, "Masukkan Password Baru:");
            if (newPass == null || newPass.isEmpty()) return;

            boolean success = MemberManager.updatePassword(namaAnggota, newPass);

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Password berhasil diperbarui",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "ID tidak ditemukan",
                        "Gagal",
                        JOptionPane.ERROR_MESSAGE);
            }
        });


        // 2. Logic Buka Halaman Daftar
        btnSignup.addActionListener(e -> {
            new SignupFrame(this).setVisible(true);
        });
    }
}