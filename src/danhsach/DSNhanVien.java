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
import model.NhanVien;
import model.PhieuNhap;

public class DSNhanVien {

    private static final String FILE = "data/nhanvien.txt";
    private NhanVien[] ds = new NhanVien[0];
    private int n = 0;

    public NhanVien[] getDs() {
        return ds;
    }

    public int getN() {
        return n;
    }

    // ================= CÁC HÀM BẮT BUỘC (gói 50%) =================

    // Thêm (không tham số): nhập từ bàn phím, tự sinh mã
    public void them() {
        NhanVien nv = new NhanVien();
        nv.nhap();
        if (trungSdt(nv.getSdt(), "")) {
            System.out.println("Số điện thoại đã có nhân viên khác dùng, không thêm.");
            return;
        }
        nv.setMaNV(sinhMa());
        them(nv);
        System.out.println("Đã thêm nhân viên " + nv.getMaNV());
    }

    // Thêm (có tham số): nới mảng ra 1 rồi đặt bản sao vào cuối
    public void them(NhanVien nv) {
        ds = Arrays.copyOf(ds, n + 1);
        ds[n++] = new NhanVien(nv);
    }

    public void timTheoMa() {
        String ma = NhapLieu.nhapChuoi("Nhập mã nhân viên cần tìm");
        NhanVien nv = timTheoMa(ma);
        if (nv == null) {
            System.out.println("Không tìm thấy nhân viên có mã: " + ma);
        } else {
            NhanVien.inTieuDe();
            nv.xuat();
        }
    }

    // chỉ trả về, không in; không thấy thì null
    public NhanVien timTheoMa(String ma) {
        for (int i = 0; i < n; i++) {
            if (ds[i].getMaNV().equalsIgnoreCase(ma)) {
                return ds[i];
            }
        }
        return null;
    }

    public void sua() {
        String ma = NhapLieu.nhapChuoi("Nhập mã nhân viên cần sửa");
        sua(ma);
    }

    // nhập vào 1 bản tạm trước, hợp lệ rồi mới chép sang, để nhập lỗi không làm hỏng dữ liệu cũ
    public void sua(String ma) {
        NhanVien nv = timTheoMa(ma);
        if (nv == null) {
            System.out.println("Không tìm thấy nhân viên có mã: " + ma);
            return;
        }
        NhanVien tam = new NhanVien(nv);
        tam.nhap();
        if (trungSdt(tam.getSdt(), ma)) {
            System.out.println("Số điện thoại đã có nhân viên khác dùng, không sửa.");
            return;
        }
        nv.setHoTen(tam.getHoTen());
        nv.setSdt(tam.getSdt());
        nv.setChucVu(tam.getChucVu());
        nv.setLuongCB(tam.getLuongCB());
        System.out.println("Đã cập nhật nhân viên " + nv.getMaNV());
    }

    public void xoa() {
        String ma = NhapLieu.nhapChuoi("Nhập mã nhân viên cần xóa");
        xoa(ma);
    }

    // chỉ xóa được khi nhân viên chưa lập chứng từ nào (mục 6.5)
    public void xoa(String ma) {
        for (PhieuNhap pn : DuLieu.dsPhieuNhap.getDs()) {
            if (pn.getMaNV().equalsIgnoreCase(ma)) {
                System.out.println("Nhân viên đã lập phiếu nhập, không xóa được.");
                return;
            }
        }
        // KHI GHÉP module Thuê phòng và Hóa đơn: bỏ comment 2 vòng lặp dưới (và import 2 lớp)
        // for (HDThuePhong hd : DuLieu.dsHDThuePhong.getDs()) {
        //     if (hd.getMaNV().equalsIgnoreCase(ma)) {
        //         System.out.println("Nhân viên đã lập hợp đồng, không xóa được.");
        //         return;
        //     }
        // }
        // for (HoaDon hd : DuLieu.dsHoaDon.getDs()) {
        //     if (hd.getMaNV().equalsIgnoreCase(ma)) {
        //         System.out.println("Nhân viên đã lập hóa đơn, không xóa được.");
        //         return;
        //     }
        // }

        for (int i = 0; i < n; i++) {
            if (ds[i].getMaNV().equalsIgnoreCase(ma)) {
                for (int j = i; j < n - 1; j++) {
                    ds[j] = ds[j + 1];
                }
                ds = Arrays.copyOf(ds, n - 1);
                n--;
                System.out.println("Đã xóa nhân viên có mã: " + ma);
                return;
            }
        }
        System.out.println("Không tìm thấy nhân viên có mã: " + ma);
    }

    public void xuat() {
        xuat(ds);
    }

    // in 1 mảng nhân viên bất kỳ thành bảng (dùng lại cho tìm theo tên, sắp xếp)
    public void xuat(NhanVien[] mang) {
        if (mang.length == 0) {
            System.out.println("Danh sách trống.");
            return;
        }
        NhanVien.inTieuDe();
        for (NhanVien nv : mang) {
            nv.xuat();
        }
    }

    // mã mới = số lớn nhất đang có + 1, "NV007" -> substring(2) = "007" -> 7
    public String sinhMa() {
        int max = 0;
        for (int i = 0; i < n; i++) {
            int so = Integer.parseInt(ds[i].getMaNV().substring(2));
            if (so > max) {
                max = so;
            }
        }
        return String.format("NV%03d", max + 1);
    }

    // cho module khác chọn nhân viên lập chứng từ: hỏi mã tới khi đúng, gõ 0 thì trả null (hủy)
    // (nhớ gọi xuat() trước để người dùng thấy danh sách)
    public NhanVien chon() {
        while (true) {
            String ma = NhapLieu.nhapChuoi("Nhập mã nhân viên (0 để hủy)");
            if (ma.equals("0")) {
                return null;
            }
            NhanVien nv = timTheoMa(ma);
            if (nv != null) {
                return nv;
            }
            System.out.println("  Không có nhân viên mã " + ma);
        }
    }

    // ================= HÀM NÂNG CAO (gói 70%) =================

    // sđt đã có người khác dùng chưa; maBoQua là mã của chính người đang sửa
    public boolean trungSdt(String sdt, String maBoQua) {
        for (int i = 0; i < n; i++) {
            if (ds[i].getSdt().equals(sdt) && !ds[i].getMaNV().equalsIgnoreCase(maBoQua)) {
                return true;
            }
        }
        return false;
    }

    public void timTheoTen() {
        String tuKhoa = NhapLieu.nhapChuoi("Nhập tên (gõ không dấu cũng được)");
        xuat(timTheoTen(tuKhoa));
    }

    // tìm gần đúng, gom kết quả vào mảng mới
    public NhanVien[] timTheoTen(String tuKhoa) {
        NhanVien[] kq = new NhanVien[0];
        int dem = 0;
        for (int i = 0; i < n; i++) {
            if (TimKiem.chua(ds[i].getHoTen(), tuKhoa)) {
                kq = Arrays.copyOf(kq, dem + 1);
                kq[dem++] = ds[i];
            }
        }
        return kq;
    }

    public void sapXepTheoLuong() {
        System.out.println("Nhân viên theo lương cơ bản giảm dần:");
        xuat(sapXepTheoLuongGiam());
    }

    // sắp xếp chọn (selection sort) trên BẢN SAO, không đổi thứ tự gốc trong file
    // mỗi vòng i: tìm người lương cao nhất trong đoạn [i..cuối] rồi đổi chỗ về vị trí i
    public NhanVien[] sapXepTheoLuongGiam() {
        NhanVien[] kq = Arrays.copyOf(ds, n);
        for (int i = 0; i < n - 1; i++) {
            int viTriMax = i;
            for (int j = i + 1; j < n; j++) {
                if (kq[j].getLuongCB() > kq[viTriMax].getLuongCB()) {
                    viTriMax = j;
                }
            }
            NhanVien tam = kq[i];
            kq[i] = kq[viTriMax];
            kq[viTriMax] = tam;
        }
        return kq;
    }

    // đếm số người và tổng lương theo từng chức vụ
    // dùng 3 mảng song song: chucVu[k] có soNguoi[k] người, tổng lương tongLuong[k]
    public void thongKeTheoChucVu() {
        if (n == 0) {
            System.out.println("Chưa có nhân viên.");
            return;
        }
        String[] chucVu = new String[0];
        int[] soNguoi = new int[0];
        double[] tongLuong = new double[0];

        for (int i = 0; i < n; i++) {
            // tìm chức vụ này đã có trong mảng chưa
            int k = -1;
            for (int j = 0; j < chucVu.length; j++) {
                if (chucVu[j].equalsIgnoreCase(ds[i].getChucVu())) {
                    k = j;
                    break;
                }
            }
            // chưa có thì nới cả 3 mảng ra 1 ô
            if (k == -1) {
                k = chucVu.length;
                chucVu = Arrays.copyOf(chucVu, k + 1);
                soNguoi = Arrays.copyOf(soNguoi, k + 1);
                tongLuong = Arrays.copyOf(tongLuong, k + 1);
                chucVu[k] = ds[i].getChucVu();
            }
            soNguoi[k]++;
            tongLuong[k] += ds[i].getLuongCB();
        }

        System.out.printf("%-14s %8s %18s%n", "Chức vụ", "Số người", "Tổng lương CB");
        double tongTatCa = 0;
        for (int k = 0; k < chucVu.length; k++) {
            System.out.printf("%-14s %8d %18s%n", chucVu[k], soNguoi[k], DinhDang.tien(tongLuong[k]));
            tongTatCa += tongLuong[k];
        }
        System.out.printf("%-14s %8d %18s%n", "TỔNG", n, DinhDang.tien(tongTatCa));
    }

    // ================= ĐỌC / GHI FILE =================

    public void docFile() {
        ds = new NhanVien[0];
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
                    them(new NhanVien(t[0], t[1], t[2], t[3], Double.parseDouble(t[4])));
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
