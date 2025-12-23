package org.example.gui;

import org.example.data.BookData;
import org.example.data.BookManager;
import org.example.data.MemberData;
import org.example.data.MemberManager;
import org.example.util.ImageRenderer;
import org.example.util.StatusRenderer;
import org.example.util.StyleUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class DashboardAdmin extends JFrame {
    // Komponen Global
    private JPanel mainContentPanel;
    private CardLayout cardLayout;

    // Komponen Panel Buku
    private JTable tableBuku;
    private DefaultTableModel modelBuku;
    private TableRowSorter<DefaultTableModel> rowSorterBuku;
    private JTextField txtSearchBuku;

    // Komponen Panel Anggota
    private JTable tableAnggota;
    private DefaultTableModel modelAnggota;
    private TableRowSorter<DefaultTableModel> rowSorterAnggota;
    private JTextField txtSearchAnggota;

    public DashboardAdmin() {
        setTitle("Admin Panel - Library Management");
        setSize(1100, 700); // Lebarkan sedikit
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- 1. HEADER (ATAS) ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 60));
        // Sekat Bawah Header
        header.setBorder(new MatteBorder(0, 0, 2, 0, new Color(230, 230, 230)));

        JLabel lblApp = new JLabel(" LIBRARY MANAGEMENT SYSTEM");
        lblApp.setFont(StyleUtil.FONT_HEADER);
        lblApp.setForeground(StyleUtil.PRIMARY_COLOR);
        header.add(lblApp, BorderLayout.WEST);

        JLabel lblUser = new JLabel("Mode: Administrator  ");
        lblUser.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        header.add(lblUser, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // --- 2. SIDEBAR (KIRI) ---
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(StyleUtil.PRIMARY_COLOR);
        sidebar.setPreferredSize(new Dimension(230, 0));
        // Sekat Kanan Sidebar (Garis Putih Tipis biar misah)
        sidebar.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 0, 2, Color.WHITE),
                new EmptyBorder(20, 10, 20, 10)
        ));

        JButton btnHome = createSidebarBtn("Dashboard Home");
        JButton btnBuku = createSidebarBtn("Manajemen Buku");
        JButton btnAnggota = createSidebarBtn("Data Anggota");
        // Menu Laporan SUDAH DIHAPUS sesuai request

        sidebar.add(btnHome);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnBuku);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnAnggota);

        sidebar.add(Box.createVerticalGlue()); // Dorong logout ke bawah
        JButton btnLogout = createSidebarBtn("Logout");
        btnLogout.setBackground(new Color(192, 57, 43));
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);

        // --- 3. MAIN CONTENT (TENGAH) ---
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(StyleUtil.BG_COLOR);

        mainContentPanel.add(createHomePanel(), "HOME");
        mainContentPanel.add(createBukuPanel(), "BUKU");
        mainContentPanel.add(createAnggotaPanel(), "ANGGOTA"); // Panel Anggota Baru

        add(mainContentPanel, BorderLayout.CENTER);

        // --- EVENTS NAVIGASI ---
        btnHome.addActionListener(e -> cardLayout.show(mainContentPanel, "HOME"));

        btnBuku.addActionListener(e -> {
            loadDataBuku();
            cardLayout.show(mainContentPanel, "BUKU");
        });

        btnAnggota.addActionListener(e -> {
            loadDataAnggota(); // Load data anggota real-time
            cardLayout.show(mainContentPanel, "ANGGOTA");
        });

        btnLogout.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        // Default tampilkan Home
        cardLayout.show(mainContentPanel, "HOME");
    }

    // =================================================================================
    // PANEL 1: HOME
    // =================================================================================
    private JPanel createHomePanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(StyleUtil.BG_COLOR);

        JLabel lbl = new JLabel("Selamat Datang, Admin!");
        lbl.setFont(StyleUtil.FONT_HEADER);
        lbl.setForeground(StyleUtil.TEXT_COLOR);

        JLabel lblSub = new JLabel("Pilih menu di sebelah kiri untuk mengelola perpustakaan.");
        lblSub.setFont(StyleUtil.FONT_BODY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx=0; gbc.gridy=0;
        p.add(lbl, gbc);

        gbc.gridy=1;
        gbc.insets = new Insets(10,0,0,0);
        p.add(lblSub, gbc);

        return p;
    }

    // =================================================================================
    // PANEL 2: MANAJEMEN BUKU
    // =================================================================================
    private JPanel createBukuPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(StyleUtil.BG_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Top Bar
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(StyleUtil.BG_COLOR);
        JLabel lblTitle = new JLabel("Koleksi Buku");
        lblTitle.setFont(StyleUtil.FONT_SUBHEADER);
        top.add(lblTitle, BorderLayout.WEST);

        txtSearchBuku = new JTextField(20);
        txtSearchBuku.putClientProperty("JTextField.placeholderText", "Cari judul / penulis...");
        top.add(txtSearchBuku, BorderLayout.EAST);
        panel.add(top, BorderLayout.NORTH);

        // Table
        String[] columns = {"Cover", "Kode", "Judul", "Penulis", "Tahun", "Stok", "Status"};
        modelBuku = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tableBuku = new JTable(modelBuku);
        styleTable(tableBuku); // Panggil styling

        rowSorterBuku = new TableRowSorter<>(modelBuku);
        tableBuku.setRowSorter(rowSorterBuku);

        // Search Logic
        txtSearchBuku.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                String text = txtSearchBuku.getText();
                if(text.trim().length() == 0) rowSorterBuku.setRowFilter(null);
                else rowSorterBuku.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        });

        panel.add(new JScrollPane(tableBuku), BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(StyleUtil.BG_COLOR);

        JButton btnAdd = new JButton("Tambah");
        StyleUtil.styleButton(btnAdd);

        JButton btnEdit = new JButton("Edit Buku");
        StyleUtil.styleButton(btnEdit);
        btnEdit.setBackground(new Color(243, 156, 18));

        JButton btnDel = new JButton("Hapus");
        StyleUtil.styleButton(btnDel);
        btnDel.setBackground(new Color(192, 57, 43));

        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDel);
        panel.add(btnPanel, BorderLayout.SOUTH);

        // Actions
        btnAdd.addActionListener(e -> {
            new example.gui.BookPanel(this).setVisible(true);
            loadDataBuku();
        });

        btnEdit.addActionListener(e -> {
            int row = tableBuku.getSelectedRow();
            if (row >= 0) {
                int modelRow = tableBuku.convertRowIndexToModel(row);
                String kode = (String) modelBuku.getValueAt(modelRow, 1);
                BookData bookToEdit = findBookByKode(kode);
                if (bookToEdit != null) {
                    new example.gui.BookPanel(this, bookToEdit).setVisible(true);
                    loadDataBuku();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih buku dulu!");
            }
        });

        btnDel.addActionListener(e -> {
            int row = tableBuku.getSelectedRow();
            if (row >= 0) {
                int modelRow = tableBuku.convertRowIndexToModel(row);
                String kode = (String) modelBuku.getValueAt(modelRow, 1);
                if (JOptionPane.showConfirmDialog(this, "Hapus buku " + kode + "?") == JOptionPane.YES_OPTION) {
                    BookManager.delete(kode);
                    loadDataBuku();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih buku dulu!");
            }
        });

        return panel;
    }

    // =================================================================================
    // PANEL 3: DATA ANGGOTA (FITUR BARU)
    // =================================================================================
    private JPanel createAnggotaPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(StyleUtil.BG_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header Panel Anggota
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(StyleUtil.BG_COLOR);
        JLabel lblTitle = new JLabel("Daftar Anggota Terdaftar");
        lblTitle.setFont(StyleUtil.FONT_SUBHEADER);
        top.add(lblTitle, BorderLayout.WEST);

        txtSearchAnggota = new JTextField(20);
        txtSearchAnggota.putClientProperty("JTextField.placeholderText", "Cari nama anggota...");
        top.add(txtSearchAnggota, BorderLayout.EAST);
        panel.add(top, BorderLayout.NORTH);

        // Table Setup
        // Kita sembunyikan password demi keamanan tampilan
        String[] columns = {"ID Member", "Nama Lengkap", "Gender", "Role", "Password"};
        modelAnggota = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tableAnggota = new JTable(modelAnggota);
        styleTable(tableAnggota); // Pakai style yang sama biar rapi

        // Atur lebar kolom
        tableAnggota.getColumnModel().getColumn(0).setPreferredWidth(80); // ID
        tableAnggota.getColumnModel().getColumn(1).setPreferredWidth(200); // Nama

        rowSorterAnggota = new TableRowSorter<>(modelAnggota);
        tableAnggota.setRowSorter(rowSorterAnggota);

        // Search Logic Anggota
        txtSearchAnggota.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                String text = txtSearchAnggota.getText();
                if(text.trim().length() == 0) rowSorterAnggota.setRowFilter(null);
                else rowSorterAnggota.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        });

        panel.add(new JScrollPane(tableAnggota), BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(StyleUtil.BG_COLOR);

        JButton btnDel = new JButton("Hapus Anggota");
        StyleUtil.styleButton(btnDel);
        btnDel.setBackground(new Color(192, 57, 43)); // Merah

        btnPanel.add(btnDel);
        panel.add(btnPanel, BorderLayout.SOUTH);

        // Action Delete
        btnDel.addActionListener(e -> {
            int row = tableAnggota.getSelectedRow();
            if (row >= 0) {
                int modelRow = tableAnggota.convertRowIndexToModel(row);
                String id = (String) modelAnggota.getValueAt(modelRow, 0); // Ambil ID
                String nama = (String) modelAnggota.getValueAt(modelRow, 1); // Ambil Nama
                String role = (String) modelAnggota.getValueAt(modelRow, 3); // Role

                if (role.equalsIgnoreCase("admin")) {
                    JOptionPane.showMessageDialog(this, "Tidak boleh menghapus sesama Admin!", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int opt = JOptionPane.showConfirmDialog(this, "Yakin hapus anggota: " + nama + "?");
                if (opt == JOptionPane.YES_OPTION) {
                    MemberManager.delete(id);
                    loadDataAnggota();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih anggota yang mau dihapus!");
            }
        });

        return panel;
    }

    // =================================================================================
    // HELPER & STYLING
    // =================================================================================
    private JButton createSidebarBtn(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(StyleUtil.PRIMARY_COLOR);
        btn.setBorder(new EmptyBorder(12, 15, 12, 15));
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void styleTable(JTable t) {
        t.setRowHeight(60); // Tinggi baris sedang
        // SEKAT: Aktifkan garis vertikal biar 'enak dibaca'
        t.setShowVerticalLines(true);
        t.setGridColor(new Color(230, 230, 230)); // Warna garis abu-abu muda

        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        t.getTableHeader().setBackground(new Color(236, 240, 241));

        // Set renderer hanya jika tabel buku (cek jumlah kolom)
        if (t.getColumnCount() > 5) {
            t.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer()); // Cover
            t.getColumnModel().getColumn(6).setCellRenderer(new StatusRenderer()); // Status
        }
    }

    private void loadDataBuku() {
        modelBuku.setRowCount(0);
        List<BookData> books = BookManager.getAll();
        for (BookData b : books) {
            modelBuku.addRow(new Object[]{
                    b.coverPath, b.kode, b.judul, b.pengarang, b.tahun, b.stockTersedia, b.status
            });
        }
    }

    private void loadDataAnggota() {
        modelAnggota.setRowCount(0);
        List<MemberData> members = MemberManager.getAll();
        for (MemberData m : members) {
            // Tampilkan password apa adanya, atau ganti "****" kalau mau rahasia
            modelAnggota.addRow(new Object[]{
                    m.id, m.namaAnggota, m.jenisKelamin, m.role, m.password
            });
        }
    }

    private BookData findBookByKode(String kode) {
        for (BookData b : BookManager.getAll()) {
            if (b.kode.equals(kode)) return b;
        }
        return null;
    }
}