package model;

import core.DinhDang;
import core.DoiTuong;
import core.NhapLieu;

// phần chung của 3 loại phòng, không tạo trực tiếp mà chỉ tạo qua lớp con
public abstract class Phong implements DoiTuong {

    public static final String TRONG = "TRONG";
    public static final String DA_THUE = "DA_THUE";

    public static final String THUONG = "THUONG";
    public static final String DELUXE = "DELUXE";
    public static final String SUITE = "SUITE";

    private String maP;
    private String tenP;
    private double donGia;
    private int soNguoiToiDa;
    private int slGiuong;
    private String tinhTrang;

    public Phong() {
        this.maP = "";
        this.tenP = "";
        this.donGia = 0;
        this.soNguoiToiDa = 1;
        this.slGiuong = 1;
        this.tinhTrang = TRONG;
    }

    public Phong(String maP, String tenP, double donGia, int soNguoiToiDa, int slGiuong, String tinhTrang) {
        this.maP = maP;
        this.tenP = tenP;
        this.donGia = donGia;
        this.soNguoiToiDa = soNguoiToiDa;
        this.slGiuong = slGiuong;
        this.tinhTrang = tinhTrang;
    }

    public Phong(Phong khac) {
        this.maP = khac.maP;
        this.tenP = khac.tenP;
        this.donGia = khac.donGia;
        this.soNguoiToiDa = khac.soNguoiToiDa;
        this.slGiuong = khac.slGiuong;
        this.tinhTrang = khac.tinhTrang;
    }

    // mỗi lớp con tự trả về loại của mình: THUONG / DELUXE / SUITE
    public abstract String getLoaiPhong();

    public String getMaP() {
        return maP;
    }

    public void setMaP(String maP) {
        this.maP = maP;
    }

    public String getTenP() {
        return tenP;
    }

    public void setTenP(String tenP) {
        this.tenP = tenP;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public int getSoNguoiToiDa() {
        return soNguoiToiDa;
    }

    public void setSoNguoiToiDa(int soNguoiToiDa) {
        this.soNguoiToiDa = soNguoiToiDa;
    }

    public int getSlGiuong() {
        return slGiuong;
    }

    public void setSlGiuong(int slGiuong) {
        this.slGiuong = slGiuong;
    }

    public String getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(String tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    // không nhập mã (danh sách tự sinh) và tình trạng (phòng mới luôn TRONG)
    @Override
    public void nhap() {
        tenP = NhapLieu.nhapChuoi("Số phòng");
        donGia = NhapLieu.nhapSoThuc("Giá thuê 1 ngày", 1);
        soNguoiToiDa = NhapLieu.nhapSoNguyen("Số người tối đa", 1, 20);
        slGiuong = NhapLieu.nhapSoNguyen("Số giường", 1, 10);
    }

    // in phần chung (chưa xuống dòng), lớp con in tiếp phần riêng rồi mới xuống dòng
    @Override
    public void xuat() {
        System.out.print(maP + " | " + getLoaiPhong() + " | Phòng " + tenP + " | "
                + DinhDang.tien(donGia) + "/ngày | " + soNguoiToiDa + " người | "
                + slGiuong + " giường | " + tinhTrang);
    }

    // phong.txt: loai|maP|tenP|donGia|soNguoiToiDa|slGiuong|tinhTrang (+ phần riêng của lớp con)
    @Override
    public String toLine() {
        return DinhDang.dong(getLoaiPhong(), maP, tenP, donGia, soNguoiToiDa, slGiuong, tinhTrang);
    }
}
