package org.example.data;

import org.example.util.FileUtil;

import java.util.*;

public class BookManager {
    private static final String FILE = "Sistem-Manajemen-Perpustakaan/database/Book.csv";

    public static List<BookData> getAll() {
        List<BookData> list = new ArrayList<>();
        for (String line : FileUtil.read(FILE)) {
            list.add(BookData.fromCSV(line));
        }
        return list;
    }

    public static void add(BookData newBook) {
        List<BookData> allBooks = getAll();
        boolean found = false;
        List<String> lines = new ArrayList<>();

        for (BookData b : allBooks) {
            if (b.kode.equals(newBook.kode)) {
                lines.add(newBook.toCSV());
                found = true;
            } else {
                lines.add(b.toCSV());
            }
        }

        if (!found) {
            lines.add(newBook.toCSV());
        }

        FileUtil.write(FILE, lines);
    }

    public static void delete(String kode) {
        List<String> out = new ArrayList<>();
        for (BookData b : getAll()) {
            if (!b.kode.equals(kode)) out.add(b.toCSV());
        }
        FileUtil.write(FILE, out);
    }
}
