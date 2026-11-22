package model;

import core.DinhDang;
import core.DoiTuong;
import core.NhapLieu;
import java.util.Scanner;

// Một phòng được thuê trong hợp đồng. Cặp (maHDong, maP) không trùng.
public class CTHopDong implements DoiTuong {
    // Scanner riêng của lớp này; không close() vì sẽ đóng luôn System.in
    private static final Scanner sc = new Scanner(System.in, "UTF-8");

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
    public void tinhThanhTien(long soNgay) {
        thanhTien = donGia * soNgay;
    }

    // Nhập số người ở (mặc định tối đa 10). maHDong, maP, donGia gán bằng setter; thanhTien tính bằng tinhThanhTien().
    @Override
    public void nhap() {
        nhap(10);
    }

    // Như nhap() nhưng giới hạn theo sức chứa của phòng (Phong.soNguoiToiDa), dùng khi lập hợp đồng
    public void nhap(int soNguoiToiDa) {
        slNguoi = NhapLieu.nhapSoNguyen(sc, "Số người ở (1-" + soNguoiToiDa + ")", 1, soNguoiToiDa);
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