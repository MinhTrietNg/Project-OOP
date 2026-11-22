package model;

import core.DinhDang;
import core.DoiTuong;
import core.NhapLieu;
import java.util.Scanner;

// hàng trong kho, dùng cho dịch vụ (vd nước suối cho dịch vụ "Nước suối")
public class HangHoa implements DoiTuong {

    // Scanner riêng của lớp này; không close() vì sẽ đóng luôn System.in
    private static final Scanner sc = new Scanner(System.in, "UTF-8");

    private String maH;
    private String tenH;
    private double donGia;   // giá nhập của lần nhập gần nhất
    private int slTon;       // số lượng còn trong kho

    public HangHoa() {
        this.maH = "";
        this.tenH = "";
        this.donGia = 0;
        this.slTon = 0;
    }

    public HangHoa(String maH, String tenH, double donGia, int slTon) {
        this.maH = maH;
        this.tenH = tenH;
        this.donGia = donGia;
        this.slTon = slTon;
    }

    public HangHoa(HangHoa khac) {
        this.maH = khac.maH;
        this.tenH = khac.tenH;
        this.donGia = khac.donGia;
        this.slTon = khac.slTon;
    }

    public String getMaH() {
        return maH;
    }

    public void setMaH(String maH) {
        this.maH = maH;
    }

    public String getTenH() {
        return tenH;
    }

    public void setTenH(String tenH) {
        this.tenH = tenH;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public int getSlTon() {
        return slTon;
    }

    public void setSlTon(int slTon) {
        this.slTon = slTon;
    }

    // giá trị hàng đang nằm trong kho = số lượng tồn x giá nhập gần nhất
    public double giaTriTon() {
        return slTon * donGia;
    }

    // chỉ nhập tên và giá; slTon không nhập: hàng mới = 0, sau đó chỉ đổi khi nhập kho / dùng dịch vụ
    @Override
    public void nhap() {
        tenH = NhapLieu.nhapChuoi(sc, "Tên hàng");
        donGia = NhapLieu.nhapSoThuc(sc, "Đơn giá nhập", 0);
    }

    // vd: HH001 | Nước suối | giá nhập 4.500 đ | tồn 100
    @Override
    public void xuat() {
        System.out.println(maH + " | " + tenH + " | giá nhập " + DinhDang.tien(donGia) + " | tồn " + slTon);
    }

    // hanghoa.txt: maH|tenH|donGia|slTon
    @Override
    public String toLine() {
        return DinhDang.dong(maH, tenH, donGia, slTon);
    }
}
