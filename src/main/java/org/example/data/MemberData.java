package org.example.data;

public class MemberData {
    public String id;
    public String namaAnggota;
    public String jenisKelamin;
    public String password;
    public String role;

    public String toCSV() {
        return id + "," + namaAnggota + "," + jenisKelamin + "," + password + "," + role;
    }

    public static MemberData fromCSV(String line) {
        String[] d = line.split(",");
        MemberData m = new MemberData();
        m.id = d[0];
        m.namaAnggota = d[1];
        m.jenisKelamin = d[2];
        m.password = d[3];
        m.role = d[4];
        return m;
    }
}

