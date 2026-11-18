package model;

import core.DoiTuong;
import core.NhapLieu;

// phần chung của KhachHang và NhanVien
// (lớp thuộc module 1, viết đúng theo mô tả mục 4.1 để module 4 biên dịch được trước khi ghép)
public abstract class Nguoi implements DoiTuong {

    private String hoTen;
    private String sdt;

    public Nguoi() {
        this.hoTen = "";
        this.sdt = "";
    }

    public Nguoi(String hoTen, String sdt) {
        this.hoTen = hoTen;
        this.sdt = sdt;
    }

    public Nguoi(Nguoi khac) {
        this.hoTen = khac.hoTen;
        this.sdt = khac.sdt;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    @Override
    public void nhap() {
        hoTen = NhapLieu.nhapChuoi("Họ tên");
        sdt = NhapLieu.nhapSdt("Số điện thoại");
    }

    // in 2 cột họ tên, sđt trên cùng 1 dòng, lớp con in tiếp phần riêng rồi xuống dòng
    @Override
    public void xuat() {
        System.out.printf("%-24s %-11s ", hoTen, sdt);
    }
}
