package model;

import core.DinhDang;
import core.DoiTuong;
import core.NhapLieu;
import java.time.LocalDate;

// 1 lần khách dùng dịch vụ trong thời gian thuê phòng
public class CTSuDungDV implements DoiTuong {

    private String maHDong;     // -> HDThuePhong
    private String maDV;        // -> DichVu
    private LocalDate ngaySD;
    private int sl;
    private double donGia;      // giá dịch vụ lúc dùng, chép từ DichVu.donGia
    private double thanhTien;   // sl x donGia

    public CTSuDungDV() {
        this.maHDong = "";
        this.maDV = "";
        this.ngaySD = LocalDate.now();
        this.sl = 0;
        this.donGia = 0;
        this.thanhTien = 0;
    }

    public CTSuDungDV(String maHDong, String maDV, LocalDate ngaySD, int sl, double donGia, double thanhTien) {
        this.maHDong = maHDong;
        this.maDV = maDV;
        this.ngaySD = ngaySD;
        this.sl = sl;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
    }

    public CTSuDungDV(CTSuDungDV khac) {
        this.maHDong = khac.maHDong;
        this.maDV = khac.maDV;
        this.ngaySD = khac.ngaySD;
        this.sl = khac.sl;
        this.donGia = khac.donGia;
        this.thanhTien = khac.thanhTien;
    }

    public String getMaHDong() {
        return maHDong;
    }

    public void setMaHDong(String maHDong) {
        this.maHDong = maHDong;
    }

    public String getMaDV() {
        return maDV;
    }

    public void setMaDV(String maDV) {
        this.maDV = maDV;
    }

    public LocalDate getNgaySD() {
        return ngaySD;
    }

    public void setNgaySD(LocalDate ngaySD) {
        this.ngaySD = ngaySD;
    }

    public int getSl() {
        return sl;
    }

    public void setSl(int sl) {
        this.sl = sl;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    public void tinhThanhTien() {
        thanhTien = sl * donGia;
    }

    // ngày sử dụng có nằm trong thời gian thuê không (tính cả 2 ngày biên)
    public static boolean trongKhoang(LocalDate ngay, LocalDate tu, LocalDate den) {
        return !ngay.isBefore(tu) && !ngay.isAfter(den);
    }

    // nhập ngày dùng và số lượng; maHDong, maDV, donGia do DSCTSuDungDV gán, thanhTien tự tính
    @Override
    public void nhap() {
        ngaySD = NhapLieu.nhapNgay("Ngày sử dụng");
        nhapSoLuong();
    }

    // như nhap() nhưng bắt ngày dùng nằm trong thời gian thuê (ngày nhận -> ngày trả của hợp đồng)
    public void nhap(LocalDate tu, LocalDate den) {
        while (true) {
            ngaySD = NhapLieu.nhapNgay("Ngày sử dụng");
            if (trongKhoang(ngaySD, tu, den)) {
                break;
            }
            System.out.println("  Ngày sử dụng phải từ " + DinhDang.ngay(tu) + " đến " + DinhDang.ngay(den) + ".");
        }
        nhapSoLuong();
    }

    // chặn trên để sl x donGia và trừ kho không bị số quá lớn
    private void nhapSoLuong() {
        sl = NhapLieu.nhapSoNguyen("Số lượng", 1, 1000);
    }

    // vd: HDTP001 | DV001 | 17/11/2026 | 2 x 15.000 đ = 30.000 đ
    @Override
    public void xuat() {
        System.out.println(maHDong + " | " + maDV + " | " + DinhDang.ngay(ngaySD) + " | " + sl + " x "
                + DinhDang.tien(donGia) + " = " + DinhDang.tien(thanhTien));
    }

    // ctsudungdv.txt: maHDong|maDV|ngaySD|sl|donGia|thanhTien
    @Override
    public String toLine() {
        return DinhDang.dong(maHDong, maDV, ngaySD, sl, donGia, thanhTien);
    }
}
