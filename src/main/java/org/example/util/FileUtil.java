package org.example.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static List<String> read(String filePath) {
        List<String> lines = new ArrayList<>();
        File file = new File(filePath);

        // DEBUG: Cek lokasi file
        if (!file.exists()) {
            System.out.println("[DEBUG] File tidak ditemukan di: " + file.getAbsolutePath());
            return lines;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static void write(String filePath, List<String> lines) {
        File file = new File(filePath);

        // Buat folder database jika belum ada
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
            // DEBUG: Konfirmasi file berhasil disimpan
            System.out.println("[DEBUG] Berhasil menulis ke: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}