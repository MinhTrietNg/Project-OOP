package model;

import core.DinhDang;
import core.NhapLieu;

// khách thuê phòng hoặc ở cùng trong phòng
public class KhachHang extends Nguoi {

    private String maKH;
    private String diaChi;

    public KhachHang() {
        super();
        this.maKH = "";
        this.diaChi = "";
    }

    public KhachHang(String maKH, String hoTen, String sdt, String diaChi) {
        super(hoTen, sdt);
        this.maKH = maKH;
        this.diaChi = diaChi;
    }

    public KhachHang(KhachHang khac) {
        super(khac);
        this.maKH = khac.maKH;
        this.diaChi = khac.diaChi;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    // không nhập mã: danh sách tự sinh
    @Override
    public void nhap() {
        super.nhap();
        diaChi = NhapLieu.nhapChuoi("Địa chỉ");
    }

    // vd: KH001 | Nguyễn Văn An | 0901234567 | Quận 1, TP.HCM
    @Override
    public void xuat() {
        System.out.print(maKH + " | ");
        super.xuat();
        System.out.println(diaChi);
    }

    // khachhang.txt: maKH|hoTen|sdt|diaChi
    @Override
    public String toLine() {
        return DinhDang.dong(maKH, getHoTen(), getSdt(), diaChi);
    }
}
