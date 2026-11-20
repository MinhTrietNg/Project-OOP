package model;

import core.DinhDang;
import core.NhapLieu;

public class PhongDeluxe extends Phong {

    private String view;
    private boolean banCong;
    private String loaiNoiThat;

    public PhongDeluxe() {
        super();
        this.view = "";
        this.banCong = false;
        this.loaiNoiThat = "";
    }

    public PhongDeluxe(String maP, String tenP, double donGia, int soNguoiToiDa, int slGiuong, String tinhTrang,
            String view, boolean banCong, String loaiNoiThat) {
        super(maP, tenP, donGia, soNguoiToiDa, slGiuong, tinhTrang);
        this.view = view;
        this.banCong = banCong;
        this.loaiNoiThat = loaiNoiThat;
    }

    public PhongDeluxe(PhongDeluxe khac) {
        super(khac);
        this.view = khac.view;
        this.banCong = khac.banCong;
        this.loaiNoiThat = khac.loaiNoiThat;
    }

    @Override
    public String getLoaiPhong() {
        return DELUXE;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public boolean isBanCong() {
        return banCong;
    }

    public void setBanCong(boolean banCong) {
        this.banCong = banCong;
    }

    public String getLoaiNoiThat() {
        return loaiNoiThat;
    }

    public void setLoaiNoiThat(String loaiNoiThat) {
        this.loaiNoiThat = loaiNoiThat;
    }

    @Override
    public void nhap() {
        super.nhap();
        view = NhapLieu.nhapChuoi("View (hướng nhìn)");
        banCong = NhapLieu.nhapCoKhong("Có ban công");
        loaiNoiThat = NhapLieu.nhapChuoi("Loại nội thất");
    }

    @Override
    public void xuat() {
        super.xuat();
        String chuBanCong = "Không ban công";
        if (banCong) {
            chuBanCong = "Có ban công";
        }
        System.out.println(" | View " + view + " | " + chuBanCong + " | Nội thất " + loaiNoiThat);
    }

    @Override
    public String toLine() {
        return super.toLine() + DinhDang.PHAN_CACH + DinhDang.dong(view, banCong, loaiNoiThat);
    }
}
