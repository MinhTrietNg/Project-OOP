package model;

import core.DinhDang;
import core.DoiTuong;
import core.NhapLieu;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// Hợp đồng thuê phòng của một khách đứng tên.
public class HDThuePhong implements DoiTuong {
    private String maHDong;
    private String maKH;
    private String maNV;
    private LocalDate ngayLapHD;
    private double tienCoc;
    private LocalDate ngayNhanPhong;
    private LocalDate ngayTraPhong;

    // Constructor không tham số
    public HDThuePhong() {
    }

    // Constructor đầy đủ tham số
    public HDThuePhong(String maHDong, String maKH, String maNV, LocalDate ngayLapHD,
                       double tienCoc, LocalDate ngayNhanPhong, LocalDate ngayTraPhong) {
        this.maHDong = maHDong;
        this.maKH = maKH;
        this.maNV = maNV;
        this.ngayLapHD = ngayLapHD;
        this.tienCoc = tienCoc;
        this.ngayNhanPhong = ngayNhanPhong;
        this.ngayTraPhong = ngayTraPhong;
    }

    // Constructor sao chép
    public HDThuePhong(HDThuePhong x) {
        this.maHDong = x.maHDong;
        this.maKH = x.maKH;
        this.maNV = x.maNV;
        this.ngayLapHD = x.ngayLapHD;
        this.tienCoc = x.tienCoc;
        this.ngayNhanPhong = x.ngayNhanPhong;
        this.ngayTraPhong = x.ngayTraPhong;
    }

    // Số ngày giữa ngày nhận và ngày trả
    public long soNgayThue() {
        return ChronoUnit.DAYS.between(ngayNhanPhong, ngayTraPhong);
    }

    // Nhập ngày nhận, ngày trả, tiền cọc.
    // maHDong, maKH, maNV, ngayLapHD gán trước bằng setter (không nhập ở đây).
    @Override
    public void nhap() {
        LocalDate ngayLap = (ngayLapHD != null) ? ngayLapHD : LocalDate.now();
        while (true) {
            ngayNhanPhong = NhapLieu.nhapNgay("Ngày nhận phòng");
            if (!ngayNhanPhong.isBefore(ngayLap)) {
                break;
            }
            System.out.println("  Ngày nhận phòng không được trước ngày lập hợp đồng.");
        }
        nhapNgayTra();
        tienCoc = NhapLieu.nhapSoThuc("Tiền cọc", 0);
    }

    // Nhập ngày trả phòng (phải sau ngày nhận). Dùng khi lập hợp đồng và khi sửa ngày trả.
    public void nhapNgayTra() {
        while (true) {
            LocalDate d = NhapLieu.nhapNgay("Ngày trả phòng");
            if (d.isAfter(ngayNhanPhong)) {
                ngayTraPhong = d;
                return;
            }
            System.out.println("  Ngày trả phải sau ngày nhận phòng.");
        }
    }

    @Override
    public void xuat() {
        System.out.println(String.format("%s | KH: %s | NV: %s | Lập: %s | Nhận: %s | Trả: %s (%d ngày) | Cọc: %s",
                maHDong, maKH, maNV, DinhDang.ngay(ngayLapHD), DinhDang.ngay(ngayNhanPhong),
                DinhDang.ngay(ngayTraPhong), soNgayThue(), DinhDang.tien(tienCoc)));
    }

    // maHDong|maKH|maNV|ngayLapHD|tienCoc|ngayNhanPhong|ngayTraPhong
    @Override
    public String toLine() {
        return DinhDang.dong(maHDong, maKH, maNV, ngayLapHD, tienCoc, ngayNhanPhong, ngayTraPhong);
    }

    public String getMaHDong() {
        return maHDong;
    }

    public void setMaHDong(String maHDong) {
        this.maHDong = maHDong;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
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

    public double getTienCoc() {
        return tienCoc;
    }

    public void setTienCoc(double tienCoc) {
        this.tienCoc = tienCoc;
    }

    public LocalDate getNgayNhanPhong() {
        return ngayNhanPhong;
    }

    public void setNgayNhanPhong(LocalDate ngayNhanPhong) {
        this.ngayNhanPhong = ngayNhanPhong;
    }

    public LocalDate getNgayTraPhong() {
        return ngayTraPhong;
    }

    public void setNgayTraPhong(LocalDate ngayTraPhong) {
        this.ngayTraPhong = ngayTraPhong;
    }
}