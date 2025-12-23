package org.example.data;

import java.time.*;
import java.time.temporal.ChronoUnit;

public class LoanData {
    public String idPinjam;
    public String namaAnggota;
    public String kodeBuku;
    public LocalDate tanggalPinjam;
    public LocalDate tanggalKembali;
    public int perpanjang;
    public String status; // aktif / telat

    public int hitungDenda() {
        if (LocalDate.now().isAfter(tanggalKembali)) {
            long hari = ChronoUnit.DAYS.between(tanggalKembali, LocalDate.now());
            return (int) hari * 500;
        }
        return 0;
    }
}

