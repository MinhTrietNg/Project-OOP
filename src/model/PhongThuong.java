package model;

// phòng tiêu chuẩn, không có thuộc tính riêng
public class PhongThuong extends Phong {

    public PhongThuong() {
        super();
    }

    public PhongThuong(String maP, String tenP, double donGia, int soNguoiToiDa, int slGiuong, String tinhTrang) {
        super(maP, tenP, donGia, soNguoiToiDa, slGiuong, tinhTrang);
    }

    public PhongThuong(PhongThuong khac) {
        super(khac);
    }

    @Override
    public String getLoaiPhong() {
        return THUONG;
    }

    @Override
    public void xuat() {
        super.xuat();
        System.out.println();
    }
}
