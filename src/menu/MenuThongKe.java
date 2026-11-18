package menu;

import app.DuLieu;
import core.NhapLieu;
// KHI GHÉP module 1 và 3: bỏ comment 5 import dưới
// import core.DinhDang;
// import java.time.LocalDate;
// import model.CTHopDong;
// import model.HoaDon;
// import model.Phong;

public class MenuThongKe {

    public static void chay() {
        while (true) {
            int chon = NhapLieu.chonMenu("THỐNG KÊ", "Quay lại",
                    "Doanh thu theo tháng/năm",
                    "Tình trạng phòng theo loại",
                    "Doanh thu theo phòng",
                    "Tồn kho",
                    "Hàng sắp hết",
                    "Chi phí nhập hàng theo tháng",
                    "Nhập hàng theo nhà cung cấp",
                    "Phiếu nhập theo nhân viên",
                    "Nhân viên theo chức vụ");
            switch (chon) {
                case 1: doanhThuTheoThangNam(); break;
                case 2: tinhTrangPhongTheoLoai(); break;
                case 3: doanhThuTheoPhong(); break;
                case 4: DuLieu.dsHangHoa.xuatTonKho(); break;
                case 5: DuLieu.dsHangHoa.xuatSapHet(); break;
                case 6: DuLieu.dsPhieuNhap.thongKeTheoThang(); break;
                case 7: DuLieu.dsPhieuNhap.thongKeTheoNhaCC(); break;
                case 8: DuLieu.dsPhieuNhap.thongKeTheoNhanVien(); break;
                case 9: DuLieu.dsNhanVien.thongKeTheoChucVu(); break;
                default:
                    return;
            }
        }
    }

    // =====================================================================
    // 3 thống kê dưới cần lớp của module khác (Phong, CTHopDong, HoaDon).
    // Code đã viết sẵn theo đúng tên hàm trong MO_TA_DO_AN.md.
    // KHI GHÉP: trong mỗi hàm xóa dòng println tạm và bỏ comment khối /* ... */
    // =====================================================================

    // tổng tongCong của hóa đơn DA_TT có ngày lập trong tháng/năm (mục 6.4)
    private static void doanhThuTheoThangNam() {
        System.out.println("Cần module Dịch vụ & Thanh toán (HoaDon), sẽ bật khi ghép code.");
        /*
        int nam = NhapLieu.nhapSoNguyen("Năm", 2000, 2100);
        int thang = NhapLieu.nhapSoNguyen("Tháng (0 = cả năm)", 0, 12);
        double tong = 0;
        int soHoaDon = 0;
        for (HoaDon hd : DuLieu.dsHoaDon.getDs()) {
            LocalDate d = hd.getNgayLapHD();
            boolean dungKy = d.getYear() == nam && (thang == 0 || d.getMonthValue() == thang);
            if (dungKy && hd.getTrangThai().equals(HoaDon.DA_TT)) {
                tong += hd.getTongCong();
                soHoaDon++;
            }
        }
        String ky = (thang == 0) ? "năm " + nam : "tháng " + thang + "/" + nam;
        System.out.println("Doanh thu " + ky + ": " + DinhDang.tien(tong)
                + " (" + soHoaDon + " hóa đơn đã thanh toán)");
        */
    }

    // đếm số phòng TRONG / DA_THUE của từng loại (mục 6.4)
    private static void tinhTrangPhongTheoLoai() {
        System.out.println("Cần module Phòng (Phong), sẽ bật khi ghép code.");
        /*
        String[] cacLoai = {"THUONG", "DELUXE", "SUITE"};
        System.out.printf("%-8s %8s %8s %8s%n", "Loại", "Trống", "Đã thuê", "Tổng");
        for (String loai : cacLoai) {
            int trong = 0;
            int daThue = 0;
            for (Phong p : DuLieu.dsPhong.getDs()) {
                if (p.getLoaiPhong().equals(loai)) {
                    if (p.getTinhTrang().equals(Phong.TRONG)) {
                        trong++;
                    } else {
                        daThue++;
                    }
                }
            }
            System.out.printf("%-8s %8d %8d %8d%n", loai, trong, daThue, trong + daThue);
        }
        */
    }

    // mỗi phòng: cộng thanhTien các CTHopDong của phòng đó,
    // chỉ tính hợp đồng đã có hóa đơn DA_TT (mục 7)
    private static void doanhThuTheoPhong() {
        System.out.println("Cần module Phòng, Thuê phòng và Hóa đơn, sẽ bật khi ghép code.");
        /*
        System.out.printf("%-6s %-6s %18s%n", "Mã P", "Số P", "Doanh thu");
        double tongTatCa = 0;
        for (Phong p : DuLieu.dsPhong.getDs()) {
            double tong = 0;
            for (CTHopDong ct : DuLieu.dsCTHopDong.getDs()) {
                if (!ct.getMaP().equalsIgnoreCase(p.getMaP())) {
                    continue;
                }
                HoaDon hd = DuLieu.dsHoaDon.timTheoHopDong(ct.getMaHDong());
                if (hd != null && hd.getTrangThai().equals(HoaDon.DA_TT)) {
                    tong += ct.getThanhTien();
                }
            }
            System.out.printf("%-6s %-6s %18s%n", p.getMaP(), p.getTenP(), DinhDang.tien(tong));
            tongTatCa += tong;
        }
        System.out.println("Tổng doanh thu tiền phòng: " + DinhDang.tien(tongTatCa));
        */
    }
}
