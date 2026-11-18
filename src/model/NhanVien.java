package model;

import core.DinhDang;
import core.NhapLieu;

// nhân viên: người lập hợp đồng, hóa đơn, phiếu nhập
public class NhanVien extends Nguoi {

    // cho chọn chức vụ có sẵn để thống kê theo chức vụ không bị lệch kiểu "le tan" / "Lễ tân"
    public static final String[] CAC_CHUC_VU = {"Lễ tân", "Quản lý", "Thủ kho", "Phục vụ"};

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
        int chon = NhapLieu.chonMenu("CHỨC VỤ", "Khác (tự gõ)", CAC_CHUC_VU);
        if (chon == 0) {
            chucVu = NhapLieu.nhapChuoi("Chức vụ");
        } else {
            chucVu = CAC_CHUC_VU[chon - 1];
        }
        luongCB = NhapLieu.nhapSoThuc("Lương cơ bản", 0);
    }

    // in tiêu đề bảng 1 lần trước khi in các dòng xuat()
    public static void inTieuDe() {
        System.out.printf("%-6s %-24s %-11s %-12s %15s%n",
                "Mã NV", "Họ tên", "SĐT", "Chức vụ", "Lương CB");
    }

    @Override
    public void xuat() {
        System.out.printf("%-6s ", maNV);
        super.xuat();
        System.out.printf("%-12s %15s%n", chucVu, DinhDang.tien(luongCB));
    }

    // nhanvien.txt: maNV|hoTen|sdt|chucVu|luongCB
    @Override
    public String toLine() {
        return DinhDang.dong(maNV, getHoTen(), getSdt(), chucVu, luongCB);
    }
}
