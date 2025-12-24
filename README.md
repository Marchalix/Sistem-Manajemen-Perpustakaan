# 📚 Sistem Manajemen Perpustakaan

Aplikasi **Sistem Manajemen Perpustakaan** ini merupakan aplikasi berbasis **Java** yang dirancang untuk membantu **anggota perpustakaan (user)** dalam melakukan proses peminjaman buku secara **mandiri, terstruktur, dan sesuai aturan**.

Aplikasi ini menekankan pada kemudahan penggunaan, pengelolaan data yang aman, serta penerapan aturan peminjaman secara otomatis.

---

## ✨ Deskripsi Aplikasi

Aplikasi ini memungkinkan user untuk:
- Login menggunakan akun anggota
- Mengelola peminjaman buku pribadi
- Melihat status dan riwayat peminjaman
- Melakukan peminjaman, perpanjangan, dan pengembalian buku

Seluruh data disimpan menggunakan **file CSV**, sehingga aplikasi bersifat ringan dan mudah dijalankan tanpa database eksternal.

---

## 🚀 Fitur Aplikasi

Berikut fitur utama yang tersedia dalam sistem:

- 🔐 **Login Multi-User** menggunakan data anggota
- 📝 **Pendaftaran Anggota Baru**
- 📊 **Dashboard User** sebagai pusat informasi dan laporan peminjaman
- 🔎 **Pencarian Buku** yang tersedia di perpustakaan
- 📖 **Peminjaman Buku**
- 🔄 **Perpanjangan Masa Peminjaman**
- 📥 **Pengembalian Buku**
- 💾 **Penyimpanan Data menggunakan File CSV**

> 🔒 Setiap user **hanya dapat mengakses dan mengelola data peminjamannya sendiri**.

---

## 📌 Aturan Peminjaman

Sistem menerapkan aturan peminjaman sebagai berikut:

- ❌ User **tidak dapat meminjam buku** jika masih memiliki peminjaman aktif atau terlambat
- 📚 Maksimal **5 buku** dalam satu periode peminjaman
- ⏳ **Due date maksimal 7 hari** sejak tanggal peminjaman
- 🔁 **Perpanjangan maksimal 2 kali**
- ➕ Setiap perpanjangan menambah waktu **maksimal 7 hari**

Status peminjaman akan **ditentukan secara otomatis** berdasarkan perbandingan tanggal hari ini dengan *due date*.

---

## ▶️ Cara Menjalankan Program

Ikuti langkah berikut untuk menjalankan aplikasi:

1. Pastikan **JDK (Java Development Kit)** sudah terinstal
2. Buka project menggunakan IDE (NetBeans / IntelliJ / VS Code)
3. Jalankan file **`Main.java`**
4. Login menggunakan akun yang tersedia pada file **`anggota.csv`**

---

## 👥 Tim Project

| No | Nama                  | NIM             |
|----|-----------------------|-----------------|
| 1  | Marlove Salim         | 202410370110263 |
| 2  | Ayshea Marvella Pasha | 202410370110379 |

### 🔧 Pembagian Tugas
- **Anggota 1** → Antarmuka pengguna (**GUI**) dan navigasi
- **Anggota 2** → **Backend**, logika program, dan file handling

🧪 Proses **integrasi dan pengujian** dilakukan secara bersama-sama untuk memastikan sistem berjalan dengan baik.
