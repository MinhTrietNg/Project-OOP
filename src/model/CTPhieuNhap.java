package model;

import core.DinhDang;
import core.DoiTuong;
import core.NhapLieu;
import java.util.Scanner;

// 1 mặt hàng trong phiếu nhập, cặp (maPN, maH) không trùng
public class CTPhieuNhap implements DoiTuong {

    // Scanner riêng của lớp này; không close() vì sẽ đóng luôn System.in
    private static final Scanner sc = new Scanner(System.in, "UTF-8");

    private String maPN;        // -> PhieuNhap
    private String maH;         // -> HangHoa
    private int sl;
    private double donGiaNhap;
    private double thanhTien;   // sl x donGiaNhap

    public CTPhieuNhap() {
        this.maPN = "";
        this.maH = "";
        this.sl = 0;
        this.donGiaNhap = 0;
        this.thanhTien = 0;
    }

    public CTPhieuNhap(String maPN, String maH, int sl, double donGiaNhap, double thanhTien) {
        this.maPN = maPN;
        this.maH = maH;
        this.sl = sl;
        this.donGiaNhap = donGiaNhap;
        this.thanhTien = thanhTien;
    }

    public CTPhieuNhap(CTPhieuNhap khac) {
        this.maPN = khac.maPN;
        this.maH = khac.maH;
        this.sl = khac.sl;
        this.donGiaNhap = khac.donGiaNhap;
        this.thanhTien = khac.thanhTien;
    }

    public String getMaPN() {
        return maPN;
    }

    public void setMaPN(String maPN) {
        this.maPN = maPN;
    }

    public String getMaH() {
        return maH;
    }

    public void setMaH(String maH) {
        this.maH = maH;
    }

    public int getSl() {
        return sl;
    }

    public void setSl(int sl) {
        this.sl = sl;
    }

    public double getDonGiaNhap() {
        return donGiaNhap;
    }

    public void setDonGiaNhap(double donGiaNhap) {
        this.donGiaNhap = donGiaNhap;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    public void tinhThanhTien() {
        thanhTien = sl * donGiaNhap;
    }

    // chỉ nhập số lượng và giá; maPN, maH do DSPhieuNhap gán, thanhTien tự tính
    @Override
    public void nhap() {
        // chặn trên để slTon (kiểu int) cộng dồn không bị tràn số thành số âm
        sl = NhapLieu.nhapSoNguyen(sc, "Số lượng nhập", 1, 100000);
        donGiaNhap = NhapLieu.nhapSoThuc(sc, "Đơn giá nhập", 1);
        tinhThanhTien();
    }

    // vd: PN001 | HH001 | 100 x 4.500 đ = 450.000 đ
    @Override
    public void xuat() {
        System.out.println(maPN + " | " + maH + " | " + sl + " x " + DinhDang.tien(donGiaNhap)
                + " = " + DinhDang.tien(thanhTien));
    }

    // ctphieunhap.txt: maPN|maH|sl|donGiaNhap|thanhTien
    @Override
    public String toLine() {
        return DinhDang.dong(maPN, maH, sl, donGiaNhap, thanhTien);
    }
}
