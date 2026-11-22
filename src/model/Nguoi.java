package model;

import core.DoiTuong;
import core.NhapLieu;
import java.util.Scanner;

// phần chung của KhachHang và NhanVien
public abstract class Nguoi implements DoiTuong {

    // Scanner riêng của lớp này; không close() vì sẽ đóng luôn System.in
    private static final Scanner sc = new Scanner(System.in, "UTF-8");

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
        hoTen = NhapLieu.nhapChuoi(sc, "Họ tên");
        sdt = NhapLieu.nhapSdt(sc, "Số điện thoại");
    }

    // in họ tên, sđt (chưa xuống dòng), lớp con in tiếp phần riêng rồi mới xuống dòng
    @Override
    public void xuat() {
        System.out.print(hoTen + " | " + sdt + " | ");
    }
}
