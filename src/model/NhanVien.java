package model;

import core.DinhDang;
import core.NhapLieu;

// nhân viên: người lập hợp đồng, hóa đơn, phiếu nhập
public class NhanVien extends Nguoi {

    // chỉ cho chọn trong danh sách này để thống kê theo chức vụ không bị lệch kiểu "le tan" / "Lễ tân"
    public static final String[] CAC_CHUC_VU = {"Lễ tân", "Quản lý", "Thủ kho", "Phục vụ", "Bảo vệ"};

    private String maNV;
    private String chucVu;
    private double luongCB;

    public NhanVien() {
        super();
        this.maNV = "";
        this.chucVu = "";
        this.luongCB = 0;
    }

    public NhanVien(String maNV, String hoTen, String sdt, String chucVu, double luongCB) {
        super(hoTen, sdt);
        this.maNV = maNV;
        this.chucVu = chucVu;
        this.luongCB = luongCB;
    }

    public NhanVien(NhanVien khac) {
        super(khac);
        this.maNV = khac.maNV;
        this.chucVu = khac.chucVu;
        this.luongCB = khac.luongCB;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public double getLuongCB() {
        return luongCB;
    }

    public void setLuongCB(double luongCB) {
        this.luongCB = luongCB;
    }

    // không nhập mã: danh sách tự sinh
    @Override
    public void nhap() {
        super.nhap();
        for (int i = 0; i < CAC_CHUC_VU.length; i++) {
            System.out.println("  " + (i + 1) + ". " + CAC_CHUC_VU[i]);
        }
        int chon = NhapLieu.nhapSoNguyen("Chọn chức vụ", 1, CAC_CHUC_VU.length);
        chucVu = CAC_CHUC_VU[chon - 1];
        luongCB = NhapLieu.nhapSoThuc("Lương cơ bản", 0);
    }

    // vd: NV001 | Nguyễn Văn An | 0901234567 | Lễ tân | 8.000.000 đ
    @Override
    public void xuat() {
        System.out.print(maNV + " | ");
        super.xuat();
        System.out.println(chucVu + " | " + DinhDang.tien(luongCB));
    }

    // nhanvien.txt: maNV|hoTen|sdt|chucVu|luongCB
    @Override
    public String toLine() {
        return DinhDang.dong(maNV, getHoTen(), getSdt(), chucVu, luongCB);
    }
}
