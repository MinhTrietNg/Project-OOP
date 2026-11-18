package danhsach;

import core.DinhDang;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import model.CTPhieuNhap;

// danh sách chi tiết: không có mã riêng, không sửa, không xóa (mục 6.5)
public class DSCTPhieuNhap {

    private static final String FILE = "data/ctphieunhap.txt";
    private CTPhieuNhap[] ds = new CTPhieuNhap[0];
    private int n = 0;

    public CTPhieuNhap[] getDs() {
        return ds;
    }

    public int getN() {
        return n;
    }

    // chỉ có bản có tham số: chi tiết được tạo bên trong DSPhieuNhap.them()
    public void them(CTPhieuNhap ct) {
        ds = Arrays.copyOf(ds, n + 1);
        ds[n++] = new CTPhieuNhap(ct);
    }

    // lấy các mặt hàng của 1 phiếu (mục 6.6)
    public CTPhieuNhap[] layTheoPhieu(String maPN) {
        CTPhieuNhap[] kq = new CTPhieuNhap[0];
        int dem = 0;
        for (int i = 0; i < n; i++) {
            if (ds[i].getMaPN().equalsIgnoreCase(maPN)) {
                kq = Arrays.copyOf(kq, dem + 1);
                kq[dem++] = ds[i];
            }
        }
        return kq;
    }

    public void xuat() {
        if (n == 0) {
            System.out.println("Danh sách trống.");
            return;
        }
        CTPhieuNhap.inTieuDe();
        for (int i = 0; i < n; i++) {
            ds[i].xuat();
        }
    }

    public void docFile() {
        ds = new CTPhieuNhap[0];
        n = 0;
        File f = new File(FILE);
        if (!f.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                try {
                    String[] t = DinhDang.tach(line);
                    them(new CTPhieuNhap(t[0], t[1], Integer.parseInt(t[2]),
                            Double.parseDouble(t[3]), Double.parseDouble(t[4])));
                } catch (RuntimeException e) {
                    System.out.println("Bỏ qua dòng lỗi trong " + FILE + ": " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Lỗi đọc file " + FILE);
        }
    }

    public void ghiFile() {
        new File(FILE).getParentFile().mkdirs();
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(FILE), StandardCharsets.UTF_8))) {
            for (int i = 0; i < n; i++) {
                bw.write(ds[i].toLine());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Lỗi ghi file " + FILE);
        }
    }
}
