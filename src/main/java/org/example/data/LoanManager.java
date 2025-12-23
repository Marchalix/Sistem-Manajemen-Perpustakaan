package org.example.data;

import org.example.util.FileUtil;
import java.time.*;
import java.util.*;

public class LoanManager {
    private static final String FILE = "database/loans.csv";

    public static List<LoanData> getAll() {
        List<LoanData> list = new ArrayList<>();
        for (String l : FileUtil.read(FILE)) {
            String[] d = l.split(",");
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

    public static boolean canBorrow(String nama) {
        int aktif = 0;
        for (LoanData l : getAll()) {
            if (l.namaAnggota.equals(nama) && l.status.equals("aktif")) aktif++;
        }
        return aktif < 3;
    }
}

