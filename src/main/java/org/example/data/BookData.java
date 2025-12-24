package org.example.data;

public class BookData {
    public String kode;
    public String judul;
    public String pengarang;
    public int tahun;
    public int stockTersedia;
    public String status; // tersedia / dipinjam
    public String cover;

    public String toCSV() {
        return kode + "," + judul + "," + pengarang + "," + tahun + "," +
                stockTersedia + "," + status + "," + cover;
    }

    public static BookData fromCSV(String line) {
        String[] d = line.split(",");
        BookData b = new BookData();
        b.kode = d[0];
        b.judul = d[1];
        b.pengarang = d[2];
        b.tahun = Integer.parseInt(d[3]);
        b.stockTersedia = Integer.parseInt(d[4]);
        b.status = d[5];
        b.cover = d[6];
        return b;
    }
}

