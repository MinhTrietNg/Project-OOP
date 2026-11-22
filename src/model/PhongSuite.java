package model;

import core.DinhDang;
import core.NhapLieu;
import java.util.Scanner;

public class PhongSuite extends Phong {

    // Scanner riêng của lớp này; không close() vì sẽ đóng luôn System.in
    private static final Scanner sc = new Scanner(System.in, "UTF-8");

    private boolean phongKhachRieng;
    private int slPhong;

    public PhongSuite() {
        super();
        this.phongKhachRieng = false;
        this.slPhong = 2;
    }

    public PhongSuite(String maP, String tenP, double donGia, int soNguoiToiDa, int slGiuong, String tinhTrang,
            boolean phongKhachRieng, int slPhong) {
        super(maP, tenP, donGia, soNguoiToiDa, slGiuong, tinhTrang);
        this.phongKhachRieng = phongKhachRieng;
        this.slPhong = slPhong;
    }

    public PhongSuite(PhongSuite khac) {
        super(khac);
        this.phongKhachRieng = khac.phongKhachRieng;
        this.slPhong = khac.slPhong;
    }

    @Override
    public String getLoaiPhong() {
        return SUITE;
    }

    public boolean isPhongKhachRieng() {
        return phongKhachRieng;
    }

    public void setPhongKhachRieng(boolean phongKhachRieng) {
        this.phongKhachRieng = phongKhachRieng;
    }

    public int getSlPhong() {
        return slPhong;
    }

    public void setSlPhong(int slPhong) {
        this.slPhong = slPhong;
    }

    @Override
    public void nhap() {
        super.nhap();
        phongKhachRieng = NhapLieu.nhapCoKhong(sc, "Có phòng khách riêng");
        slPhong = NhapLieu.nhapSoNguyen(sc, "Số phòng con", 2, 10);
    }

    @Override
    public void xuat() {
        super.xuat();
        String chuPhongKhach = "Không phòng khách riêng";
        if (phongKhachRieng) {
            chuPhongKhach = "Có phòng khách riêng";
        }
        System.out.println(" | " + slPhong + " phòng con | " + chuPhongKhach);
    }

    @Override
    public String toLine() {
        return super.toLine() + DinhDang.PHAN_CACH + DinhDang.dong(phongKhachRieng, slPhong);
    }
}
