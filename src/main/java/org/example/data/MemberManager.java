package org.example.data;

import org.example.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

public class MemberManager {
    // Pastikan path ini sesuai dengan file detektif kemarin (path absolut kalau perlu)
    private static final String FILE = "Sistem-Manajemen-Perpustakaan/database/Member.csv";

    public static List<MemberData> getAll() {
        List<MemberData> list = new ArrayList<>();
        for (String l : FileUtil.read(FILE)) {
            MemberData m = MemberData.fromCSV(l);
            if (m != null) list.add(m);
        }
        return list;
    }

    // --- REVISI LOGIN ---
    // Sekarang login mencocokkan ID (Username) dengan Password
    public static MemberData login(String username, String pw) {
        for (MemberData m : getAll()) {
            // Cek ID (sebagai username)
            if (m.id.equalsIgnoreCase(username) && m.password.equals(pw)) {
                return m;
            }
        }
        return null;
    }

    // FITUR BARU: CEK USERNAME KEMBAR
    public static boolean isUsernameExist(String username) {
        for (MemberData m : getAll()) {
            if (m.id.equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    public static boolean updatePassword(String id, String newPassword) {
        List<MemberData> list = getAll();
        boolean found = false;

        List<String> out = new ArrayList<>();
        for (MemberData m : list) {
            if (m.id.equals(id)) {
                m.password = newPassword;   // UPDATE
                found = true;
            }
            out.add(m.toCSV());
        }

        if (found) {
            FileUtil.write(FILE, out);
        }

        return found;
    }

    public static void initAdmin() {
        if (getAll().isEmpty()) {
            MemberData admin = new MemberData();
            // Admin default
            admin.id = "admin";           // Username
            admin.namaAnggota = "Administrator"; // Nama Lengkap
            admin.jenisKelamin = "L";
            admin.password = "admin";
            admin.role = "admin";
            add(admin);
        }
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