package model;

import core.DinhDang;
import core.DoiTuong;
import java.time.LocalDate;

// 1 lần nhập hàng từ nhà cung cấp, các mặt hàng nằm ở CTPhieuNhap
public class PhieuNhap implements DoiTuong {

    private String maPN;
    private String maNCC;     // -> NhaCC
    private String maNV;      // nhân viên lập -> NhanVien
    private LocalDate ngayNhap;
    private double tongTien;  // tổng thanhTien các dòng chi tiết

    public PhieuNhap() {
        this.maPN = "";
        this.maNCC = "";
        this.maNV = "";
        this.ngayNhap = LocalDate.now();
        this.tongTien = 0;
    }

    public PhieuNhap(String maPN, String maNCC, String maNV, LocalDate ngayNhap, double tongTien) {
        this.maPN = maPN;
        this.maNCC = maNCC;
        this.maNV = maNV;
        this.ngayNhap = ngayNhap;
        this.tongTien = tongTien;
    }

    public PhieuNhap(PhieuNhap khac) {
        this.maPN = khac.maPN;
        this.maNCC = khac.maNCC;
        this.maNV = khac.maNV;
        this.ngayNhap = khac.ngayNhap;
        this.tongTien = khac.tongTien;
    }

    public String getMaPN() {
        return maPN;
    }

    public void setMaPN(String maPN) {
        this.maPN = maPN;
    }

    public String getMaNCC() {
        return maNCC;
    }

    public void setMaNCC(String maNCC) {
        this.maNCC = maNCC;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public LocalDate getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(LocalDate ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    // tổng tiền phiếu = cộng thanhTien của các dòng chi tiết
    public void tinhTongTien(CTPhieuNhap[] cacDong) {
        tongTien = 0;
        for (CTPhieuNhap ct : cacDong) {
            tongTien += ct.getThanhTien();
        }
    }

    // phiếu nhập không có trường nào gõ tay:
    // mã tự sinh, NCC và NV chọn ở DSPhieuNhap, ngày nhập là hôm nay, tổng tiền tự tính
    @Override
    public void nhap() {
        ngayNhap = LocalDate.now();
    }

    public static void inTieuDe() {
        System.out.printf("%-6s %-11s %-7s %-6s %17s%n", "Mã PN", "Ngày nhập", "Mã NCC", "Mã NV", "Tổng tiền");
    }

    @Override
    public void xuat() {
        System.out.printf("%-6s %-11s %-7s %-6s %17s%n",
                maPN, DinhDang.ngay(ngayNhap), maNCC, maNV, DinhDang.tien(tongTien));
    }

    // phieunhap.txt: maPN|maNCC|maNV|ngayNhap|tongTien
    @Override
    public String toLine() {
        return DinhDang.dong(maPN, maNCC, maNV, ngayNhap, tongTien);
    }
}
