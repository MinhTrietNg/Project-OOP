package danhsach;

import app.DuLieu;
import core.DinhDang;
import core.NhapLieu;
import core.TimKiem;
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
import model.HangHoa;

public class DSHangHoa {

    private static final String FILE = "data/hanghoa.txt";
    private HangHoa[] ds = new HangHoa[0];
    private int n = 0;

    public HangHoa[] getDs() {
        return ds;
    }

    public int getN() {
        return n;
    }

    // ================= CÁC HÀM BẮT BUỘC (gói 50%) =================

    // hàng mới luôn có slTon = 0 (constructor không tham số đã gán), chỉ tăng khi lập phiếu nhập
    public void them() {
        HangHoa hh = new HangHoa();
        hh.nhap();
        hh.setMaH(sinhMa());
        them(hh);
        System.out.println("Đã thêm hàng hóa " + hh.getMaH() + " (tồn kho = 0, hãy lập phiếu nhập)");
    }

    public void them(HangHoa hh) {
        ds = Arrays.copyOf(ds, n + 1);
        ds[n++] = new HangHoa(hh);
    }

    public void timTheoMa() {
        String ma = NhapLieu.nhapChuoi("Nhập mã hàng cần tìm");
        HangHoa hh = timTheoMa(ma);
        if (hh == null) {
            System.out.println("Không tìm thấy hàng có mã: " + ma);
        } else {
            HangHoa.inTieuDe();
            hh.xuat();
        }
    }

    public HangHoa timTheoMa(String ma) {
        for (int i = 0; i < n; i++) {
            if (ds[i].getMaH().equalsIgnoreCase(ma)) {
                return ds[i];
            }
        }
        return null;
    }

    // sửa tên và giá; số lượng tồn không sửa tay được
    public void sua() {
        String ma = NhapLieu.nhapChuoi("Nhập mã hàng cần sửa");
        sua(ma);
    }

    public void sua(String ma) {
        HangHoa hh = timTheoMa(ma);
        if (hh == null) {
            System.out.println("Không tìm thấy hàng có mã: " + ma);
            return;
        }
        hh.nhap();
        System.out.println("Đã cập nhật hàng " + hh.getMaH());
    }

    public void xoa() {
        String ma = NhapLieu.nhapChuoi("Nhập mã hàng cần xóa");
        xoa(ma);
    }

    // hàng đã từng nhập kho hoặc đang gắn với dịch vụ thì không xóa được (mục 6.5)
    public void xoa(String ma) {
        for (CTPhieuNhap ct : DuLieu.dsCTPhieuNhap.getDs()) {
            if (ct.getMaH().equalsIgnoreCase(ma)) {
                System.out.println("Hàng đã có trong phiếu nhập, không xóa được.");
                return;
            }
        }
        // KHI GHÉP module Dịch vụ: bỏ comment vòng lặp dưới (và import model.DichVu)
        // for (DichVu dv : DuLieu.dsDichVu.getDs()) {
        //     if (dv.getMaH().equalsIgnoreCase(ma)) {
        //         System.out.println("Hàng đang dùng cho dịch vụ " + dv.getMaDV() + ", không xóa được.");
        //         return;
        //     }
        // }

        for (int i = 0; i < n; i++) {
            if (ds[i].getMaH().equalsIgnoreCase(ma)) {
                for (int j = i; j < n - 1; j++) {
                    ds[j] = ds[j + 1];
                }
                ds = Arrays.copyOf(ds, n - 1);
                n--;
                System.out.println("Đã xóa hàng có mã: " + ma);
                return;
            }
        }
        System.out.println("Không tìm thấy hàng có mã: " + ma);
    }

    public void xuat() {
        xuat(ds);
    }

    public void xuat(HangHoa[] mang) {
        if (mang.length == 0) {
            System.out.println("Danh sách trống.");
            return;
        }
        HangHoa.inTieuDe();
        for (HangHoa hh : mang) {
            hh.xuat();
        }
    }

    // "HH005" -> substring(2) = "005" -> 5
    public String sinhMa() {
        int max = 0;
        for (int i = 0; i < n; i++) {
            int so = Integer.parseInt(ds[i].getMaH().substring(2));
            if (so > max) {
                max = so;
            }
        }
        return String.format("HH%03d", max + 1);
    }

    public HangHoa chon() {
        while (true) {
            String ma = NhapLieu.nhapChuoi("Nhập mã hàng (0 để dừng)");
            if (ma.equals("0")) {
                return null;
            }
            HangHoa hh = timTheoMa(ma);
            if (hh != null) {
                return hh;
            }
            System.out.println("  Không có hàng mã " + ma);
        }
    }

    // ---------- 2 hàm giao tiếp với module khác (mục 6.6) ----------

    // module Dịch vụ gọi khi khách dùng dịch vụ có tiêu hao hàng
    // không đủ hàng thì KHÔNG trừ và trả false để bên kia báo lỗi
    public boolean truKho(String maH, int sl) {
        HangHoa hh = timTheoMa(maH);
        if (hh == null || hh.getSlTon() < sl) {
            return false;
        }
        hh.setSlTon(hh.getSlTon() - sl);
        return true;
    }

    // DSPhieuNhap gọi khi lập phiếu nhập
    public void congKho(String maH, int sl) {
        HangHoa hh = timTheoMa(maH);
        if (hh != null) {
            hh.setSlTon(hh.getSlTon() + sl);
        }
    }

    // ================= HÀM NÂNG CAO (gói 70%) =================

    public void timTheoTen() {
        String tuKhoa = NhapLieu.nhapChuoi("Nhập tên hàng (gõ không dấu cũng được)");
        xuat(timTheoTen(tuKhoa));
    }

    public HangHoa[] timTheoTen(String tuKhoa) {
        HangHoa[] kq = new HangHoa[0];
        int dem = 0;
        for (int i = 0; i < n; i++) {
            if (TimKiem.chua(ds[i].getTenH(), tuKhoa)) {
                kq = Arrays.copyOf(kq, dem + 1);
                kq[dem++] = ds[i];
            }
        }
        return kq;
    }

    // hàng có tồn kho <= ngưỡng, cần nhập thêm
    public HangHoa[] dsSapHet(int nguong) {
        HangHoa[] kq = new HangHoa[0];
        int dem = 0;
        for (int i = 0; i < n; i++) {
            if (ds[i].getSlTon() <= nguong) {
                kq = Arrays.copyOf(kq, dem + 1);
                kq[dem++] = ds[i];
            }
        }
        return kq;
    }

    public void xuatSapHet() {
        int nguong = NhapLieu.nhapSoNguyen("Ngưỡng cảnh báo (tồn <= ngưỡng)", 0, Integer.MAX_VALUE);
        HangHoa[] kq = dsSapHet(nguong);
        if (kq.length == 0) {
            System.out.println("Không có hàng nào sắp hết.");
            return;
        }
        System.out.println("Có " + kq.length + " mặt hàng cần nhập thêm:");
        xuat(kq);
    }

    // tổng tiền hàng đang nằm trong kho
    public double tongGiaTriTon() {
        double tong = 0;
        for (int i = 0; i < n; i++) {
            tong += ds[i].giaTriTon();
        }
        return tong;
    }

    // sắp xếp nổi bọt (bubble sort) trên bản sao: tồn ít nhất lên đầu để dễ thấy hàng cần nhập
    // mỗi lượt đẩy phần tử tồn nhiều nhất còn lại xuống cuối
    public HangHoa[] sapXepTheoTonTang() {
        HangHoa[] kq = Arrays.copyOf(ds, n);
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (kq[j].getSlTon() > kq[j + 1].getSlTon()) {
                    HangHoa tam = kq[j];
                    kq[j] = kq[j + 1];
                    kq[j + 1] = tam;
                }
            }
        }
        return kq;
    }

    // báo cáo tồn kho (mục 6.4): danh sách theo tồn tăng dần + tổng giá trị
    public void xuatTonKho() {
        xuat(sapXepTheoTonTang());
        int tongSl = 0;
        for (int i = 0; i < n; i++) {
            tongSl += ds[i].getSlTon();
        }
        System.out.println("Số mặt hàng     : " + n);
        System.out.println("Tổng số lượng   : " + tongSl);
        System.out.println("Tổng giá trị tồn: " + DinhDang.tien(tongGiaTriTon()));
    }

    // ================= ĐỌC / GHI FILE =================

    public void docFile() {
        ds = new HangHoa[0];
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
                    // mã sai dạng sẽ làm sinhMa() lỗi, nên loại ngay từ lúc đọc
                    if (!t[0].matches("HH\\d+")) {
                        throw new IllegalArgumentException();
                    }
                    them(new HangHoa(t[0], t[1], Double.parseDouble(t[2]), Integer.parseInt(t[3])));
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
