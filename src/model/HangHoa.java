package model;

import core.DinhDang;
import core.DoiTuong;
import core.NhapLieu;

// hàng trong kho, dùng cho dịch vụ (vd nước suối cho dịch vụ "Nước suối")
public class HangHoa implements DoiTuong {

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
        tenH = NhapLieu.nhapChuoi("Tên hàng");
        donGia = NhapLieu.nhapSoThuc("Đơn giá nhập", 0);
    }

    public static void inTieuDe() {
        System.out.printf("%-6s %-24s %15s %8s %17s%n", "Mã", "Tên hàng", "Đơn giá", "Tồn", "Giá trị tồn");
    }

    @Override
    public void xuat() {
        System.out.printf("%-6s %-24s %15s %8d %17s%n",
                maH, tenH, DinhDang.tien(donGia), slTon, DinhDang.tien(giaTriTon()));
    }

    // hanghoa.txt: maH|tenH|donGia|slTon
    @Override
    public String toLine() {
        return DinhDang.dong(maH, tenH, donGia, slTon);
    }
}
