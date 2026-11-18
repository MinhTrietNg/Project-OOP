package model;

import core.DinhDang;
import core.DoiTuong;

// Một khách ở một phòng trong hợp đồng. Bộ (maHDong, maP, maKH) không trùng.
public class CTKhachO implements DoiTuong {
    private String maHDong;
    private String maP;
    private String maKH;

    // Constructor không tham số
    public CTKhachO() {
    }

    // Constructor đầy đủ tham số
    public CTKhachO(String maHDong, String maP, String maKH) {
        this.maHDong = maHDong;
        this.maP = maP;
        this.maKH = maKH;
    }

    // Constructor sao chép
    public CTKhachO(CTKhachO x) {
        this.maHDong = x.maHDong;
        this.maP = x.maP;
        this.maKH = x.maKH;
    }

    // Cả ba thuộc tính đều là mã liên kết (maHDong, maP, maKH) nên không có gì để nhập:
    // các mã này gán bằng setter hoặc constructor.
    @Override
    public void nhap() {
    }

    @Override
    public void xuat() {
        System.out.println("HĐ " + maHDong + " | Phòng " + maP + " | Khách " + maKH);
    }

    // maHDong|maP|maKH
    @Override
    public String toLine() {
        return DinhDang.dong(maHDong, maP, maKH);
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

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }
}