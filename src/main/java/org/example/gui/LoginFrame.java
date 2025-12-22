package org.example.gui;

import javax.swing.*;
import java.awt.event.*;


public class LoginFrame extends JFrame {
    JTextField tfId = new JTextField();
    JPasswordField pfPass = new JPasswordField();

    public LoginFrame() {
        setTitle("Login Perpustakaan");
        setSize(350, 250);
        setLayout(null);

        JLabel l1 = new JLabel("ID");
        JLabel l2 = new JLabel("Password");
        JButton btn = new JButton("Login");

        l1.setBounds(40,40,100,25);
        tfId.setBounds(140,40,150,25);
        l2.setBounds(40,80,100,25);
        pfPass.setBounds(140,80,150,25);
        btn.setBounds(120,130,100,30);

        add(l1); add(tfId);
        add(l2); add(pfPass);
        add(btn);

        btn.addActionListener(e -> {
            if (tfId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID tidak boleh kosong");
            }
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}
