package model;

import core.DinhDang;
import core.DoiTuong;
import core.NhapLieu;
import java.util.Scanner;

// nhà cung cấp hàng hóa cho kho
public class NhaCC implements DoiTuong {

    // Scanner riêng của lớp này; không close() vì sẽ đóng luôn System.in
    private static final Scanner sc = new Scanner(System.in, "UTF-8");

    private String maNCC;
    private String tenNCC;
    private String diaChi;
    private String sdt;

    public NhaCC() {
        this.maNCC = "";
        this.tenNCC = "";
        this.diaChi = "";
        this.sdt = "";
    }

    public NhaCC(String maNCC, String tenNCC, String diaChi, String sdt) {
        this.maNCC = maNCC;
        this.tenNCC = tenNCC;
        this.diaChi = diaChi;
        this.sdt = sdt;
    }

    public NhaCC(NhaCC khac) {
        this.maNCC = khac.maNCC;
        this.tenNCC = khac.tenNCC;
        this.diaChi = khac.diaChi;
        this.sdt = khac.sdt;
    }

    public String getMaNCC() {
        return maNCC;
    }

    public void setMaNCC(String maNCC) {
        this.maNCC = maNCC;
    }

    public String getTenNCC() {
        return tenNCC;
    }

    public void setTenNCC(String tenNCC) {
        this.tenNCC = tenNCC;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    @Override
    public void nhap() {
        tenNCC = NhapLieu.nhapChuoi(sc, "Tên nhà cung cấp");
        diaChi = NhapLieu.nhapChuoi(sc, "Địa chỉ");
        sdt = NhapLieu.nhapSdt(sc, "Số điện thoại");
    }

    // vd: NCC001 | Công ty ABC | 12 Lê Lợi | 0281234567
    @Override
    public void xuat() {
        System.out.println(maNCC + " | " + tenNCC + " | " + diaChi + " | " + sdt);
    }

    // nhacc.txt: maNCC|tenNCC|diaChi|sdt
    @Override
    public String toLine() {
        return DinhDang.dong(maNCC, tenNCC, diaChi, sdt);
    }
}
