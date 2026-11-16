package core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

// danh sách chung, lớp con chỉ cần cho biết tên file và cách đọc 1 dòng
public abstract class DanhSach<T extends DoiTuong> {

    protected ArrayList<T> ds = new ArrayList<>();

    protected abstract String tenFile();

    protected abstract T tuDong(String[] f);

    public void them(T x) {
        ds.add(x);
    }

    public ArrayList<T> getDs() {
        return ds;
    }

    public void xuatTatCa() {
        if (ds.isEmpty()) {
            System.out.println("(Danh sách trống)");
            return;
        }
        for (T x : ds) {
            x.xuat();
        }
    }

    public void docFile() {
        ds.clear();
        Path p = Paths.get(tenFile());
        if (!Files.exists(p)) {
            return;
        }
        try (BufferedReader br = Files.newBufferedReader(p, StandardCharsets.UTF_8)) {
            String line;
            int soDong = 0;
            while ((line = br.readLine()) != null) {
                soDong++;
                // file lưu bằng Notepad có thể dính ký tự BOM ở đầu
                if (soDong == 1 && line.startsWith("﻿")) {
                    line = line.substring(1);
                }
                if (line.trim().isEmpty()) {
                    continue;
                }
                try {
                    ds.add(tuDong(DinhDang.tach(line)));
                } catch (RuntimeException e) {
                    System.out.println("Bỏ qua dòng " + soDong + " của " + tenFile() + ": " + e);
                }
            }
        } catch (IOException e) {
            System.out.println("Không đọc được " + tenFile() + ": " + e.getMessage());
        }
    }

    public void ghiFile() {
        Path p = Paths.get(tenFile());
        try {
            if (p.getParent() != null) {
                Files.createDirectories(p.getParent());
            }
            try (BufferedWriter bw = Files.newBufferedWriter(p, StandardCharsets.UTF_8)) {
                for (T x : ds) {
                    bw.write(x.toLine());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Không ghi được " + tenFile() + ": " + e.getMessage());
        }
    }
}
