package org.example.data;

import org.example.util.FileUtil;
import java.util.*;

public class MemberManager {
    private static final String FILE = "database/members.csv";

    public static List<MemberData> getAll() {
        List<MemberData> list = new ArrayList<>();
        for (String l : FileUtil.read(FILE)) {
            list.add(MemberData.fromCSV(l));
        }
        return list;
    }

    public static MemberData login(String id, String pw) {
        for (MemberData m : getAll()) {
            if (m.id.equals(id) && m.password.equals(pw)) return m;
        }
        return null;
    }

    public static void add(MemberData m) {
        List<String> d = FileUtil.read(FILE);
        d.add(m.toCSV());
        FileUtil.write(FILE, d);
    }

    public static void delete(String id) {
        List<String> out = new ArrayList<>();
        for (MemberData m : getAll()) {
            if (!m.id.equals(id)) out.add(m.toCSV());
        }
        FileUtil.write(FILE, out);
    }
}

