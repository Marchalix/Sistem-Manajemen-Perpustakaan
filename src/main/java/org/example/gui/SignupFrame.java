package org.example.gui;

import org.example.data.MemberData;
import org.example.data.MemberManager;
import org.example.util.StyleUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SignupFrame extends JDialog {
    private JTextField txtUsername, txtNama, txtPassword;
    private JComboBox<String> cmbGender;

    public SignupFrame(Frame parent) {
        super(parent, "Daftar Anggota Baru", true);
        setSize(400, 500); // Sedikit lebih tinggi
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(StyleUtil.BG_COLOR);

        // HEADER
        JLabel lblTitle = new JLabel("DAFTAR AKUN", SwingConstants.CENTER);
        lblTitle.setFont(StyleUtil.FONT_HEADER);
        lblTitle.setForeground(StyleUtil.PRIMARY_COLOR);
        lblTitle.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // FORM
        JPanel formPanel = new JPanel(new GridLayout(8, 1, 5, 5)); // Tambah baris
        formPanel.setBackground(StyleUtil.BG_COLOR);
        formPanel.setBorder(new EmptyBorder(0, 40, 0, 40));

        // 1. Username (Untuk Login) - INI YANG BARU
        formPanel.add(new JLabel("Username (untuk Login):"));
        txtUsername = new JTextField();
        styleField(txtUsername);
        formPanel.add(txtUsername);

        // 2. Nama Lengkap (Untuk Display)
        formPanel.add(new JLabel("Nama Lengkap:"));
        txtNama = new JTextField();
        styleField(txtNama);
        formPanel.add(txtNama);

        // 3. Password
        formPanel.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        styleField(txtPassword);
        formPanel.add(txtPassword);

        // 4. Gender
        formPanel.add(new JLabel("Jenis Kelamin:"));
        cmbGender = new JComboBox<>(new String[]{"L", "P"});
        cmbGender.setBackground(Color.WHITE);
        formPanel.add(cmbGender);

        add(formPanel, BorderLayout.CENTER);

        // BUTTONS
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.setBackground(StyleUtil.BG_COLOR);
        btnPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        JButton btnRegister = new JButton("Daftar Sekarang");
        StyleUtil.styleButton(btnRegister);
        btnRegister.setPreferredSize(new Dimension(150, 40));

        btnPanel.add(btnRegister);
        add(btnPanel, BorderLayout.SOUTH);

        JButton btnBack = new JButton("Back");
        btnPanel.add(btnBack);

        StyleUtil.styleButton(btnBack);
        btnBack.setPreferredSize(new Dimension(100, 40));

        // ACTION REGISTER
        btnRegister.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String nama = txtNama.getText().trim();
            String pass = txtPassword.getText();



            // Validasi Input
            if (username.isEmpty() || nama.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua data wajib diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validasi: Username tidak boleh ada spasi
            if (username.contains(" ")) {
                JOptionPane.showMessageDialog(this, "Username tidak boleh mengandung spasi!", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validasi: Username sudah dipakai belum?
            if (MemberManager.isUsernameExist(username)) {
                JOptionPane.showMessageDialog(this, "Username sudah terpakai! Pilih yang lain.", "Gagal", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Simpan Data
            MemberData m = new MemberData();
            // ID kita isi dengan Username (Manual)
            m.id = username;
            m.namaAnggota = nama;
            m.password = pass;
            m.jenisKelamin = (String) cmbGender.getSelectedItem();
            m.role = "anggota";

            MemberManager.add(m);

            JOptionPane.showMessageDialog(this, "Registrasi Berhasil! Silakan Login dengan username: " + username);
            dispose();
        });

        btnBack.addActionListener(e -> {
            new LoginFrame(); // buka halaman awal
            dispose();        // tutup halaman signup
        });
    }

    private void styleField(JTextField tf) {
        tf.setFont(StyleUtil.FONT_BODY);
        tf.setBorder(BorderFactory.createCompoundBorder(
                tf.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }
}