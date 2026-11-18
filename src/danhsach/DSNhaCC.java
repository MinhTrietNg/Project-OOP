package danhsach;

import app.DuLieu;
import core.NhapLieu;
import core.TepTin;
import core.TimKiem;
import java.util.Arrays;
import model.NhaCC;
import model.PhieuNhap;

public class DSNhaCC {

    private static final String FILE = "data/nhacc.txt";
    private NhaCC[] ds = new NhaCC[0];
    private int n = 0;

    public NhaCC[] getDs() {
        return ds;
    }

    public int getN() {
        return n;
    }

    // ================= CÁC HÀM BẮT BUỘC (gói 50%) =================

    public void them() {
        NhaCC ncc = new NhaCC();
        ncc.nhap();
        ncc.setMaNCC(sinhMa());
        them(ncc);
        System.out.println("Đã thêm nhà cung cấp " + ncc.getMaNCC());
    }

    public void them(NhaCC ncc) {
        ds = Arrays.copyOf(ds, n + 1);
        ds[n] = new NhaCC(ncc);
        n++;
    }

    public void timTheoMa() {
        String ma = NhapLieu.nhapChuoi("Nhập mã nhà cung cấp cần tìm");
        NhaCC ncc = timTheoMa(ma);
        if (ncc == null) {
            System.out.println("Không tìm thấy nhà cung cấp có mã: " + ma);
        } else {
            ncc.xuat();
        }
    }

    public NhaCC timTheoMa(String ma) {
        for (int i = 0; i < n; i++) {
            if (ds[i].getMaNCC().equalsIgnoreCase(ma)) {
                return ds[i];
            }
        }
        return null;
    }

    public void sua() {
        String ma = NhapLieu.nhapChuoi("Nhập mã nhà cung cấp cần sửa");
        sua(ma);
    }

    public void sua(String ma) {
        NhaCC ncc = timTheoMa(ma);
        if (ncc == null) {
            System.out.println("Không tìm thấy nhà cung cấp có mã: " + ma);
            return;
        }
        ncc.nhap();
        System.out.println("Đã cập nhật nhà cung cấp " + ma);
    }

    public void xoa() {
        String ma = NhapLieu.nhapChuoi("Nhập mã nhà cung cấp cần xóa");
        xoa(ma);
    }

    // nhà cung cấp đã có phiếu nhập thì không xóa được (mục 6.5)
    public void xoa(String ma) {
        for (PhieuNhap pn : DuLieu.dsPhieuNhap.getDs()) {
            if (pn.getMaNCC().equalsIgnoreCase(ma)) {
                System.out.println("Nhà cung cấp đã có phiếu nhập, không xóa được.");
                return;
            }
        }
        for (int i = 0; i < n; i++) {
            if (ds[i].getMaNCC().equalsIgnoreCase(ma)) {
                for (int j = i; j < n - 1; j++) {
                    ds[j] = ds[j + 1];
                }
                ds = Arrays.copyOf(ds, n - 1);
                n--;
                System.out.println("Đã xóa nhà cung cấp có mã: " + ma);
                return;
            }
        }
        System.out.println("Không tìm thấy nhà cung cấp có mã: " + ma);
    }

    public void xuat() {
        xuat(ds);
    }

    public void xuat(NhaCC[] mang) {
        if (mang.length == 0) {
            System.out.println("Danh sách trống.");
        }
        for (NhaCC ncc : mang) {
            ncc.xuat();
        }
    }

    // "NCC012".substring(3) = "012" -> 12
    public String sinhMa() {
        int max = 0;
        for (int i = 0; i < n; i++) {
            int so = Integer.parseInt(ds[i].getMaNCC().substring(3));
            if (so > max) {
                max = so;
            }
        }
        return String.format("NCC%03d", max + 1);
    }

    public NhaCC chon() {
        while (true) {
            String ma = NhapLieu.nhapChuoi("Nhập mã nhà cung cấp (0 để thoát)");
            if (ma.equals("0")) {
                return null;
            }
            NhaCC ncc = timTheoMa(ma);
            if (ncc != null) {
                return ncc;
            }
            System.out.println("  Không có nhà cung cấp mã " + ma);
        }
    }

    // ================= HÀM NÂNG CAO (gói 70%) =================

    public void timTheoTen() {
        String tuKhoa = NhapLieu.nhapChuoi("Nhập tên nhà cung cấp (gõ không dấu cũng được)");
        xuat(timTheoTen(tuKhoa));
    }

    public NhaCC[] timTheoTen(String tuKhoa) {
        NhaCC[] kq = new NhaCC[0];
        for (int i = 0; i < n; i++) {
            if (TimKiem.chua(ds[i].getTenNCC(), tuKhoa)) {
                kq = Arrays.copyOf(kq, kq.length + 1);
                kq[kq.length - 1] = ds[i];
            }
        }
        return kq;
    }

    // ================= ĐỌC / GHI FILE =================

    // nhacc.txt: maNCC|tenNCC|diaChi|sdt
    public void docFile() {
        ds = new NhaCC[0];
        n = 0;
        for (String[] t : TepTin.doc(FILE)) {
            try {
                if (!t[0].matches("NCC\\d+")) {
                    throw new IllegalArgumentException();
                }
                them(new NhaCC(t[0], t[1], t[2], t[3]));
            } catch (RuntimeException e) {
                System.out.println("Bỏ qua dòng lỗi trong " + FILE + ": " + String.join("|", t));
            }
        }
    }

    public void ghiFile() {
        String[] cacDong = new String[n];
        for (int i = 0; i < n; i++) {
            cacDong[i] = ds[i].toLine();
        }
        TepTin.ghi(FILE, cacDong);
    }
}
