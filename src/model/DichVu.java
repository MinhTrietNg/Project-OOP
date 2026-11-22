package model;

import core.DinhDang;
import core.DoiTuong;
import core.NhapLieu;
import java.util.Scanner;

// dịch vụ khách sạn cung cấp (đồ uống, giặt ủi, ăn sáng...)
// có thể tiêu hao 1 loại hàng trong kho (vd "Nước suối" dùng hàng HH001), không dùng hàng thì maH = ""
public class DichVu implements DoiTuong {

    // Scanner riêng của lớp này; không close() vì sẽ đóng luôn System.in
    private static final Scanner sc = new Scanner(System.in, "UTF-8");

    private String maDV;
    private String tenDV;
    private double donGia;   // giá bán 1 lần dùng
    private String maH;      // -> HangHoa, "" nếu không dùng hàng

    public DichVu() {
        this.maDV = "";
        this.tenDV = "";
        this.donGia = 0;
        this.maH = "";
    }

    public DichVu(String maDV, String tenDV, double donGia, String maH) {
        this.maDV = maDV;
        this.tenDV = tenDV;
        this.donGia = donGia;
        this.maH = maH;
    }

    public DichVu(DichVu khac) {
        this.maDV = khac.maDV;
        this.tenDV = khac.tenDV;
        this.donGia = khac.donGia;
        this.maH = khac.maH;
    }

    public String getMaDV() {
        return maDV;
    }

    public void setMaDV(String maDV) {
        this.maDV = maDV;
    }

    public String getTenDV() {
        return tenDV;
    }

    public void setTenDV(String tenDV) {
        this.tenDV = tenDV;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public String getMaH() {
        return maH;
    }

    public void setMaH(String maH) {
        this.maH = maH;
    }

    // dịch vụ có trừ kho khi khách dùng hay không
    public boolean coDungHang() {
        return !maH.isEmpty();
    }

    // chỉ nhập tên và giá; maH là mã liên kết nên DSDichVu cho chọn rồi gán bằng setter
    @Override
    public void nhap() {
        tenDV = NhapLieu.nhapChuoi(sc, "Tên dịch vụ");
        donGia = NhapLieu.nhapSoThuc(sc, "Đơn giá", 1);
    }

    // vd: DV001 | Nước suối | 15.000 đ | dùng hàng HH001
    @Override
    public void xuat() {
        String hang = "không dùng hàng";
        if (coDungHang()) {
            hang = "dùng hàng " + maH;
        }
        System.out.println(maDV + " | " + tenDV + " | " + DinhDang.tien(donGia) + " | " + hang);
    }

    // dichvu.txt: maDV|tenDV|donGia|maH   (không dùng hàng thì dòng kết thúc bằng "|")
    @Override
    public String toLine() {
        return DinhDang.dong(maDV, tenDV, donGia, maH);
    }
}
