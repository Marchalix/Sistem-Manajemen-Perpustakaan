package org.example.util;

import java.io.*;
import java.util.*;

public class FileUtil {
    public static List<String> read(String path) {
        List<String> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) data.add(line);
        } catch (Exception e) {}
        return data;
    }

    public static void write(String path, List<String> data) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            for (String d : data) pw.println(d);
        } catch (Exception e) {}
    }
}

