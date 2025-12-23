package org.example.data;

import org.example.util.FileUtil;
import java.util.*;

public class BookManager {
    private static final String FILE = "database/books.csv";

    public static List<BookData> getAll() {
        List<BookData> list = new ArrayList<>();
        for (String line : FileUtil.read(FILE)) {
            list.add(BookData.fromCSV(line));
        }
        return list;
    }

    public static void add(BookData b) {
        List<String> data = FileUtil.read(FILE);
        data.add(b.toCSV());
        FileUtil.write(FILE, data);
    }

    public static void delete(String kode) {
        List<String> out = new ArrayList<>();
        for (BookData b : getAll()) {
            if (!b.kode.equals(kode)) out.add(b.toCSV());
        }
        FileUtil.write(FILE, out);
    }
}

