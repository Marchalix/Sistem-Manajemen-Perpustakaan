package org.example.gui;

import org.example.data.BookData;
import org.example.data.BookManager;
import org.example.util.StyleUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class BookPanel extends JDialog {
    private JTextField txtKode, txtJudul, txtPengarang;
    private JSpinner spinTahun, spinStock;
    private JComboBox<String> cmbStatus;
    private JLabel lblPath;
    private boolean isEditMode = false; // Penanda mode

    // Constructor 1: Untuk Tambah Baru (Kosong)
    public BookPanel(Frame parent) {
        this(parent, null);
    }

    // Constructor 2: Untuk Edit (Terisi)
    public BookPanel(Frame parent, BookData bookToEdit) {
        super(parent, bookToEdit == null ? "Tambah Buku" : "Edit Buku", true);
        setSize(450, 550);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(8, 2, 10, 10)); // Grid ditambah 1 baris
        form.setBorder(new EmptyBorder(20, 20, 20, 20));
        form.setBackground(Color.WHITE);

        // --- INPUT FIELDS ---
        form.add(new JLabel("Kode Buku (Unik):"));
        txtKode = new JTextField();
        form.add(txtKode);

        form.add(new JLabel("Judul Buku:"));
        txtJudul = new JTextField();
        form.add(txtJudul);

        form.add(new JLabel("Pengarang:"));
        txtPengarang = new JTextField();
        form.add(txtPengarang);

        form.add(new JLabel("Tahun Terbit:"));
        spinTahun = new JSpinner(new SpinnerNumberModel(2025, 1900, 2100, 1));
        form.add(spinTahun);

        form.add(new JLabel("Stok:"));
        spinStock = new JSpinner(new SpinnerNumberModel(5, 0, 1000, 1));
        form.add(spinStock);

        form.add(new JLabel("Status:"));
        cmbStatus = new JComboBox<>(new String[]{"Tersedia", "Kosong", "Dipinjam"});
        form.add(cmbStatus);

        form.add(new JLabel("Cover Image:"));
        JPanel pnlImg = new JPanel(new BorderLayout());
        pnlImg.setBackground(Color.WHITE);
        lblPath = new JLabel("No File");
        JButton btnBrowse = new JButton("Pilih Cover");
        pnlImg.add(lblPath, BorderLayout.CENTER);
        pnlImg.add(btnBrowse, BorderLayout.EAST);
        form.add(pnlImg);

        add(form, BorderLayout.CENTER);

        // --- TOMBOL SIMPAN ---
        JButton btnSave = new JButton("SIMPAN DATA");
        StyleUtil.styleButton(btnSave);
        add(btnSave, BorderLayout.SOUTH);

        // --- LOGIC PRE-FILL (JIKA EDIT) ---
        if (bookToEdit != null) {
            isEditMode = true;
            txtKode.setText(bookToEdit.kode);
            txtKode.setEditable(false); // ID tidak boleh diedit saat update!
            txtKode.setBackground(new Color(230, 230, 230)); // Visual disabled

            txtJudul.setText(bookToEdit.judul);
            txtPengarang.setText(bookToEdit.pengarang);
            spinTahun.setValue(bookToEdit.tahun);
            spinStock.setValue(bookToEdit.stockTersedia);
            cmbStatus.setSelectedItem(bookToEdit.status);
            lblPath.setText(new File(bookToEdit.cover).getName());
            lblPath.putClientProperty("path", bookToEdit.cover);
        }

        // --- EVENTS ---
        btnBrowse.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();

            FileNameExtensionFilter imgFilter =
                    new FileNameExtensionFilter(
                            "Image Files (JPG, PNG, JPEG)",
                            "jpg", "jpeg", "png"
                    );
            fc.setFileFilter(imgFilter);
            fc.setAcceptAllFileFilterUsed(false);

            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File src = fc.getSelectedFile();

                String projectDir = System.getProperty("user.dir");

                File coverDir = new File(projectDir + File.separator + "assets"+ File.separator + "Cover");
                coverDir.mkdirs();

                File dest = new File(coverDir, src.getName());

                try {
                    Files.copy(
                            src.toPath(),
                            dest.toPath(),
                            StandardCopyOption.REPLACE_EXISTING
                    );

                    lblPath.setText(src.getName());
                    lblPath.putClientProperty("path", src.getName());

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Gagal upload cover");
                }
            }
        });

        btnSave.addActionListener(e -> {
            if (txtKode.getText().isEmpty() || txtJudul.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Kode dan Judul wajib diisi!");
                return;
            }

            BookData b = new BookData();
            b.kode = txtKode.getText();
            b.judul = txtJudul.getText();
            b.pengarang = txtPengarang.getText();
            b.tahun = (int) spinTahun.getValue();
            b.stockTersedia = (int) spinStock.getValue();
            b.status = (String) cmbStatus.getSelectedItem();

            // Sebelumnya bisa ambil path absolut dari JFileChooser
            Object pathObj = lblPath.getClientProperty("path");
            if (pathObj != null) {
                // Ambil nama file saja dan simpan ke coverPath
                File f = new File(pathObj.toString());
                b.cover = f.getName(); // nama file saja
            } else if (isEditMode) {
                b.cover = bookToEdit.cover;
            } else {
                b.cover = "default.jpg"; // default
            }

            // Panggil Manager (Method add() di codingan kamu sudah handle Update jika ID sama)
            BookManager.add(b);

            JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
            dispose();
        });
    }
}