package model;

import core.DinhDang;
import core.DoiTuong;
import core.NhapLieu;

// Một phòng được thuê trong hợp đồng. Cặp (maHDong, maP) không trùng.
public class CTHopDong implements DoiTuong {
    private String maHDong;
    private String maP;
    private double donGia;      // giá phòng tại lúc thuê, chép từ Phong.donGia
    private int slNguoi;        // số người ở phòng này, từ 1 tới Phong.soNguoiToiDa
    private double thanhTien;   // donGia x soNgayThue() của hợp đồng

    // Constructor không tham số
    public CTHopDong() {
    }

    // Constructor đầy đủ tham số
    public CTHopDong(String maHDong, String maP, double donGia, int slNguoi, double thanhTien) {
        this.maHDong = maHDong;
        this.maP = maP;
        this.donGia = donGia;
        this.slNguoi = slNguoi;
        this.thanhTien = thanhTien;
    }

    // Constructor sao chép
    public CTHopDong(CTHopDong x) {
        this.maHDong = x.maHDong;
        this.maP = x.maP;
        this.donGia = x.donGia;
        this.slNguoi = x.slNguoi;
        this.thanhTien = x.thanhTien;
    }

    // Tính và lưu thành tiền theo số ngày thuê của hợp đồng
    public double tinhThanhTien(long soNgay) {
        this.thanhTien = donGia * soNgay;
        return thanhTien;
    }

    // Nhập số người ở. maHDong, maP, donGia gán bằng setter; thanhTien tính bằng tinhThanhTien().
    @Override
    public void nhap() {
        nhap(Integer.MAX_VALUE);
    }

    // Như nhap() nhưng giới hạn số người tối đa (Phong.soNguoiToiDa)
    public void nhap(int soNguoiToiDa) {
        slNguoi = NhapLieu.nhapSoNguyen("Số người ở (1-" + soNguoiToiDa + ")", 1, soNguoiToiDa);
    }

    @Override
    public void xuat() {
        System.out.println(String.format("Phòng %s | Đơn giá: %s | Số người: %d | Thành tiền: %s",
                maP, DinhDang.tien(donGia), slNguoi, DinhDang.tien(thanhTien)));
    }

    // maHDong|maP|donGia|slNguoi|thanhTien
    @Override
    public String toLine() {
        return DinhDang.dong(maHDong, maP, donGia, slNguoi, thanhTien);
    }

    public String getMaHDong() {
        return maHDong;
    }

    public void setMaHDong(String maHDong) {
        this.maHDong = maHDong;
    }

    public String getMaP() {
        return maP;
    }

    public void setMaP(String maP) {
        this.maP = maP;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public int getSlNguoi() {
        return slNguoi;
    }

    public void setSlNguoi(int slNguoi) {
        this.slNguoi = slNguoi;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }
}