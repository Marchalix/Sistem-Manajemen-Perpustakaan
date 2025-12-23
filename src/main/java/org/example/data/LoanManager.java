package org.example.data;

import org.example.util.FileUtil;

import java.time.*;
import java.util.*;

public class LoanManager {
    private static final String FILE = "Sistem-Manajemen-Perpustakaan/database/Loans.csv";

    public static List<LoanData> getAll() {
        List<LoanData> list = new ArrayList<>();
        List<String> lines = FileUtil.read(FILE);

        for (String l : lines) {
            String[] d = l.split(",");
            if (d.length < 7) continue;

            LoanData loan = new LoanData();
            loan.idPinjam = d[0];
            loan.namaAnggota = d[1];
            loan.kodeBuku = d[2];
            loan.tanggalPinjam = LocalDate.parse(d[3]);
            loan.tanggalKembali = LocalDate.parse(d[4]);
            loan.perpanjang = Integer.parseInt(d[5]);
            loan.status = d[6];

            list.add(loan);
        }
        return list;
    }

    public static void add(LoanData l) {
        List<String> d = FileUtil.read(FILE);

        String csvLine = l.idPinjam + "," +
                l.namaAnggota + "," +
                l.kodeBuku + "," +
                l.tanggalPinjam + "," +
                l.tanggalKembali + "," +
                l.perpanjang + "," +
                l.status;

        d.add(csvLine);

        FileUtil.write(FILE, d);
    }

    public static boolean canBorrow(String nama) {
        int aktif = 0;
        for (LoanData l : getAll()) {
            if (l.namaAnggota.equals(nama) && l.status.equalsIgnoreCase("aktif")) {
                aktif++;
            }
        }
        return aktif < 3;
    }

    public static void returnBook(String idPinjam) {
        List<LoanData> allLoans = getAll();
        List<String> lines = new ArrayList<>();

        for (LoanData l : allLoans) {
            if (l.idPinjam.equals(idPinjam)) {
                l.status = "dikembalikan";
                l.tanggalKembali = LocalDate.now();
            }

            String csv = l.idPinjam + "," + l.namaAnggota + "," + l.kodeBuku + "," +
                    l.tanggalPinjam + "," + l.tanggalKembali + "," +
                    l.perpanjang + "," + l.status;
            lines.add(csv);
        }
        FileUtil.write(FILE, lines);
    }

}