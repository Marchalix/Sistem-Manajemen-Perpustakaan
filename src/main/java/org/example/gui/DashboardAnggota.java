package org.example.gui;

import org.example.data.*;
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
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class DashboardAnggota extends JFrame {
    private MemberData loggedInMember;
    private CardLayout cardLayout;
    private JPanel mainContent;

    // Komponen Buku
    private JTable tableBuku;
    private DefaultTableModel modelBuku;
    private JTextField txtSearchBuku;
    private TableRowSorter<DefaultTableModel> rowSorterBuku;

    // Komponen History
    private JTable tablePinjam;
    private DefaultTableModel modelPinjam;

    public DashboardAnggota(MemberData member) {
        this.loggedInMember = member;

        setTitle("Area Anggota - " + member.namaAnggota);
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- 1. HEADER ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 60));
        header.setBorder(new MatteBorder(0, 0, 2, 0, new Color(230, 230, 230)));

        JLabel lblBrand = new JLabel(" LIBRARY APP");
        lblBrand.setFont(StyleUtil.FONT_HEADER);
        lblBrand.setForeground(StyleUtil.PRIMARY_COLOR);
        header.add(lblBrand, BorderLayout.WEST);

        JLabel lblUser = new JLabel("Halo, " + member.namaAnggota + "  ");
        lblUser.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        header.add(lblUser, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // --- 2. SIDEBAR (Style Admin) ---
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(StyleUtil.PRIMARY_COLOR);
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 0, 2, Color.WHITE),
                new EmptyBorder(20, 10, 20, 10)
        ));

        JButton btnBuku = createSidebarBtn("Katalog Buku");
        JButton btnHistory = createSidebarBtn("Peminjaman Saya");

        sidebar.add(btnBuku);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnHistory);

        sidebar.add(Box.createVerticalGlue());
        JButton btnLogout = createSidebarBtn("Logout");
        btnLogout.setBackground(new Color(192, 57, 43));
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);

        // --- 3. MAIN CONTENT ---
        cardLayout = new CardLayout();
        mainContent = new JPanel(cardLayout);
        mainContent.setBackground(StyleUtil.BG_COLOR);

        mainContent.add(createPanelBuku(), "BUKU");
        mainContent.add(createPanelHistory(), "HISTORY");

        add(mainContent, BorderLayout.CENTER);

        // --- EVENTS ---
        btnBuku.addActionListener(e -> {
            loadDataBuku();
            cardLayout.show(mainContent, "BUKU");
        });

        btnHistory.addActionListener(e -> {
            loadDataPinjam();
            cardLayout.show(mainContent, "HISTORY");
        });

        btnLogout.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        // Load awal
        loadDataBuku();
    }

    // ================= PANEL BUKU =================
    private JPanel createPanelBuku() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(StyleUtil.BG_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Top Bar
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(StyleUtil.BG_COLOR);
        JLabel lblTitle = new JLabel("Cari & Pinjam Buku");
        lblTitle.setFont(StyleUtil.FONT_SUBHEADER);
        top.add(lblTitle, BorderLayout.WEST);

        txtSearchBuku = new JTextField(20);
        txtSearchBuku.putClientProperty("JTextField.placeholderText", "Cari judul buku...");
        top.add(txtSearchBuku, BorderLayout.EAST);
        panel.add(top, BorderLayout.NORTH);

        // Table dengan Cover
        String[] cols = {"Cover", "Kode", "Judul", "Penulis", "Tahun", "Stok"};
        modelBuku = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tableBuku = new JTable(modelBuku);
        styleTable(tableBuku); // Panggil Helper Style yang sudah ada ImageRenderer

        rowSorterBuku = new TableRowSorter<>(modelBuku);
        tableBuku.setRowSorter(rowSorterBuku);

        txtSearchBuku.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                String text = txtSearchBuku.getText();
                if(text.trim().length() == 0) rowSorterBuku.setRowFilter(null);
                else rowSorterBuku.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        });

        panel.add(new JScrollPane(tableBuku), BorderLayout.CENTER);

        // Button Pinjam
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(StyleUtil.BG_COLOR);

        JButton btnPinjam = new JButton("Pinjam Buku Terpilih");
        StyleUtil.styleButton(btnPinjam);
        actionPanel.add(btnPinjam);
        panel.add(actionPanel, BorderLayout.SOUTH);

        btnPinjam.addActionListener(e -> aksiPinjam());

        return panel;
    }

    // ================= PANEL HISTORY (PEMINJAMAN) =================
    private JPanel createPanelHistory() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(StyleUtil.BG_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Riwayat & Pengembalian");
        lblTitle.setFont(StyleUtil.FONT_SUBHEADER);
        panel.add(lblTitle, BorderLayout.NORTH);

        String[] cols = {"ID Pinjam", "Kode Buku", "Tgl Pinjam", "Tgl Kembali", "Status"};
        modelPinjam = new DefaultTableModel(cols, 0){
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tablePinjam = new JTable(modelPinjam);
        styleTable(tablePinjam); // Style standar

        panel.add(new JScrollPane(tablePinjam), BorderLayout.CENTER);

        // Button Kembalikan
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(StyleUtil.BG_COLOR);

        JButton btnKembali = new JButton("Kembalikan Buku");
        StyleUtil.styleButton(btnKembali);
        btnKembali.setBackground(new Color(39, 174, 96)); // Warna Hijau

        actionPanel.add(btnKembali);
        panel.add(actionPanel, BorderLayout.SOUTH);

        btnKembali.addActionListener(e -> aksiKembalikan());

        return panel;
    }

    // ================= LOGIC & HELPERS =================

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
        t.setRowHeight(80); // Tinggi baris agar Cover muat
        t.setShowVerticalLines(true);
        t.setGridColor(new Color(230, 230, 230));
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        t.getTableHeader().setBackground(new Color(236, 240, 241));

        // Jika tabel buku (ada 6 kolom), set renderer cover
        if (t.getColumnCount() == 6) {
            t.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer());
            t.getColumnModel().getColumn(0).setPreferredWidth(70);
        }
        // Jika tabel history (ada kolom status di index 4)
        if (t.getColumnCount() == 5) {
            t.getColumnModel().getColumn(4).setCellRenderer(new StatusRenderer());
        }
    }

    private void loadDataBuku() {
        modelBuku.setRowCount(0);
        List<BookData> books = BookManager.getAll();
        for (BookData b : books) {
            if (b.stockTersedia > 0) {
                // Pastikan urutan kolom sesuai header: Cover, Kode, Judul...
                modelBuku.addRow(new Object[]{b.coverPath, b.kode, b.judul, b.pengarang, b.tahun, b.stockTersedia});
            }
        }
    }

    private void loadDataPinjam() {
        modelPinjam.setRowCount(0);
        List<LoanData> loans = LoanManager.getAll();
        for (LoanData l : loans) {
            if (l.namaAnggota.equals(loggedInMember.namaAnggota)) {
                modelPinjam.addRow(new Object[]{l.idPinjam, l.kodeBuku, l.tanggalPinjam, l.tanggalKembali, l.status});
            }
        }
    }

    private void aksiPinjam() {
        int row = tableBuku.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih buku dulu!");
            return;
        }

        // Convert index karena ada sorting
        int modelRow = tableBuku.convertRowIndexToModel(row);
        String kodeBuku = (String) modelBuku.getValueAt(modelRow, 1);

        if (!LoanManager.canBorrow(loggedInMember.namaAnggota)) {
            JOptionPane.showMessageDialog(this, "Batas peminjaman (3 buku) tercapai!", "Gagal", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (JOptionPane.showConfirmDialog(this, "Pinjam buku ini?") == JOptionPane.YES_OPTION) {
            // Update Stok Buku
            BookData targetBook = null;
            for(BookData b : BookManager.getAll()) {
                if(b.kode.equals(kodeBuku)) { targetBook = b; break; }
            }

            if(targetBook != null && targetBook.stockTersedia > 0) {
                targetBook.stockTersedia -= 1;
                if(targetBook.stockTersedia == 0) targetBook.status = "Dipinjam";
                BookManager.add(targetBook); // Logic add() yang baru akan UPDATE, bukan duplicate

                // Catat Peminjaman
                LoanData loan = new LoanData();
                loan.idPinjam = "L-" + UUID.randomUUID().toString().substring(0, 5);
                loan.namaAnggota = loggedInMember.namaAnggota;
                loan.kodeBuku = kodeBuku;
                loan.tanggalPinjam = LocalDate.now();
                loan.tanggalKembali = LocalDate.now().plusDays(7);
                loan.perpanjang = 0;
                loan.status = "aktif";
                LoanManager.add(loan);

                JOptionPane.showMessageDialog(this, "Berhasil dipinjam!");
                loadDataBuku(); // Refresh tabel
            }
        }
    }

    private void aksiKembalikan() {
        int row = tablePinjam.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih buku yang mau dikembalikan!");
            return;
        }

        // Ambil data dari tabel
        String idPinjam = (String) modelPinjam.getValueAt(row, 0);
        String kodeBuku = (String) modelPinjam.getValueAt(row, 1);
        String status = (String) modelPinjam.getValueAt(row, 4);

        // Ambil Tanggal Jatuh Tempo (Kolom ke-3)
        LocalDate tglJatuhTempo = (LocalDate) modelPinjam.getValueAt(row, 3);
        LocalDate hariIni = LocalDate.now();

        // Cek Status dulu
        if (!status.equalsIgnoreCase("aktif")) {
            JOptionPane.showMessageDialog(this, "Buku ini sudah dikembalikan!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // --- HITUNG DENDA ---
        long telat = 0;
        int denda = 0;
        String pesan = "Kembalikan buku ini sekarang?";

        // Jika hari ini lebih besar dari tanggal jatuh tempo
        if (hariIni.isAfter(tglJatuhTempo)) {
            // Hitung selisih hari
            telat = hariIni.toEpochDay() - tglJatuhTempo.toEpochDay();
            denda = (int) telat * 500; // Rp 500 per hari

            pesan = "⚠️ ANDA TERLAMBAT PENGEMBALIAN! \n" +
                    "Terlambat: " + telat + " hari\n" +
                    "Total Denda: Rp " + denda + "\n\n" +
                    "Bayar denda dan kembalikan buku?";
        }

        // Tampilkan Dialog Konfirmasi (Isinya beda kalau ada denda)
        int type = (denda > 0) ? JOptionPane.WARNING_MESSAGE : JOptionPane.QUESTION_MESSAGE;
        int opt = JOptionPane.showConfirmDialog(this, pesan, "Konfirmasi Pengembalian", JOptionPane.YES_NO_OPTION, type);

        if (opt == JOptionPane.YES_OPTION) {
            // 1. Update Status Peminjaman
            LoanManager.returnBook(idPinjam);

            // 2. Kembalikan Stok Buku
            BookData targetBook = null;
            for(BookData b : BookManager.getAll()) {
                if(b.kode.equals(kodeBuku)) { targetBook = b; break; }
            }

            if (targetBook != null) {
                targetBook.stockTersedia += 1;
                targetBook.status = "Tersedia";
                BookManager.add(targetBook);
            }

            // Feedback sukses
            if (denda > 0) {
                JOptionPane.showMessageDialog(this, "Denda Rp " + denda + " dibayar. Buku berhasil dikembalikan.");
            } else {
                JOptionPane.showMessageDialog(this, "Terima kasih, buku berhasil dikembalikan tepat waktu.");
            }

            loadDataPinjam(); // Refresh tabel
        }
    }
}