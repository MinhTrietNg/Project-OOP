package model;

import core.DinhDang;
import core.DoiTuong;
import java.time.LocalDate;

// hóa đơn thanh toán, lập lúc trả phòng; mỗi hợp đồng có tối đa 1 hóa đơn
public class HoaDon implements DoiTuong {

    public static final String CHUA_TT = "CHUA_TT";
    public static final String DA_TT = "DA_TT";

    private String maHDon;
    private String maHDong;        // -> HDThuePhong
    private String maNV;           // nhân viên lập -> NhanVien
    private LocalDate ngayLapHD;
    private double tongTienThue;   // tổng thanhTien các CTHopDong của hợp đồng
    private double tongTienDV;     // tổng thanhTien các CTSuDungDV của hợp đồng
    private double tongCong;       // tongTienThue + tongTienDV
    private String trangThai;      // CHUA_TT / DA_TT

    public HoaDon() {
        this.maHDon = "";
        this.maHDong = "";
        this.maNV = "";
        this.ngayLapHD = LocalDate.now();
        this.tongTienThue = 0;
        this.tongTienDV = 0;
        this.tongCong = 0;
        this.trangThai = CHUA_TT;
    }

    public HoaDon(String maHDon, String maHDong, String maNV, LocalDate ngayLapHD,
                  double tongTienThue, double tongTienDV, double tongCong, String trangThai) {
        this.maHDon = maHDon;
        this.maHDong = maHDong;
        this.maNV = maNV;
        this.ngayLapHD = ngayLapHD;
        this.tongTienThue = tongTienThue;
        this.tongTienDV = tongTienDV;
        this.tongCong = tongCong;
        this.trangThai = trangThai;
    }

    public HoaDon(HoaDon khac) {
        this.maHDon = khac.maHDon;
        this.maHDong = khac.maHDong;
        this.maNV = khac.maNV;
        this.ngayLapHD = khac.ngayLapHD;
        this.tongTienThue = khac.tongTienThue;
        this.tongTienDV = khac.tongTienDV;
        this.tongCong = khac.tongCong;
        this.trangThai = khac.trangThai;
    }

    public String getMaHDon() {
        return maHDon;
    }

    public void setMaHDon(String maHDon) {
        this.maHDon = maHDon;
    }

    public String getMaHDong() {
        return maHDong;
    }

    public void setMaHDong(String maHDong) {
        this.maHDong = maHDong;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public LocalDate getNgayLapHD() {
        return ngayLapHD;
    }

    public void setNgayLapHD(LocalDate ngayLapHD) {
        this.ngayLapHD = ngayLapHD;
    }

    public double getTongTienThue() {
        return tongTienThue;
    }

    public void setTongTienThue(double tongTienThue) {
        this.tongTienThue = tongTienThue;
    }

    public double getTongTienDV() {
        return tongTienDV;
    }

    public void setTongTienDV(double tongTienDV) {
        this.tongTienDV = tongTienDV;
    }

    public double getTongCong() {
        return tongCong;
    }

    public void setTongCong(double tongCong) {
        this.tongCong = tongCong;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public boolean daThanhToan() {
        return trangThai.equals(DA_TT);
    }

    // cộng tiền các phòng và các lần dùng dịch vụ của hợp đồng
    public void tinhTien(CTHopDong[] cacPhong, CTSuDungDV[] cacDV) {
        tongTienThue = 0;
        for (CTHopDong ct : cacPhong) {
            tongTienThue += ct.getThanhTien();
        }
        tongTienDV = 0;
        for (CTSuDungDV ct : cacDV) {
            tongTienDV += ct.getThanhTien();
        }
        tongCong = tongTienThue + tongTienDV;
    }

    // số còn phải trả = tổng cộng - tiền cọc của hợp đồng (chỉ tính khi in, không lưu - mục 7)
    public double soConPhaiTra(double tienCoc) {
        return tongCong - tienCoc;
    }

    // hóa đơn không có trường nào gõ tay:
    // mã tự sinh, hợp đồng và nhân viên chọn ở DSHoaDon, ngày lập là hôm nay, tiền tự tính
    @Override
    public void nhap() {
        ngayLapHD = LocalDate.now();
    }

    // vd: HDON001 | HDTP001 | NV001 | 18/11/2026 | phòng 600.000 đ + DV 30.000 đ = 630.000 đ | Chưa thanh toán
    @Override
    public void xuat() {
        String tt = daThanhToan() ? "Đã thanh toán" : "Chưa thanh toán";
        System.out.println(maHDon + " | " + maHDong + " | " + maNV + " | " + DinhDang.ngay(ngayLapHD)
                + " | phòng " + DinhDang.tien(tongTienThue) + " + DV " + DinhDang.tien(tongTienDV)
                + " = " + DinhDang.tien(tongCong) + " | " + tt);
    }

    // hoadon.txt: maHDon|maHDong|maNV|ngayLapHD|tongTienThue|tongTienDV|tongCong|trangThai
    @Override
    public String toLine() {
        return DinhDang.dong(maHDon, maHDong, maNV, ngayLapHD, tongTienThue, tongTienDV, tongCong, trangThai);
    }
}
