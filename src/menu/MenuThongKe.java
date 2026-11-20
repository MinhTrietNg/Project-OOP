package menu;

import app.DuLieu;
import core.DinhDang;
import core.NhapLieu;
import java.time.LocalDate;
import model.CTHopDong;
import model.HoaDon;
import model.Phong;

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

    // ===== 3 thống kê dưới dùng lớp của module khác (Phong, CTHopDong, HoaDon) =====

    // tổng tongCong của hóa đơn DA_TT có ngày lập trong tháng/năm (mục 6.4)
    private static void doanhThuTheoThangNam() {
        int nam = NhapLieu.nhapSoNguyen("Năm", 2000, 2100);
        int thang = NhapLieu.nhapSoNguyen("Tháng (0 = cả năm)", 0, 12);
        double tong = 0;
        for (HoaDon hd : DuLieu.dsHoaDon.getDs()) {
            LocalDate d = hd.getNgayLapHD();
            if (hd.daThanhToan() && d.getYear() == nam) {
                // tháng = 0 thì lấy cả năm
                if (thang == 0 || d.getMonthValue() == thang) {
                    tong += hd.getTongCong();
                }
            }
        }
        System.out.println("Doanh thu: " + DinhDang.tien(tong));
    }

    // đếm số phòng TRONG / DA_THUE của từng loại (mục 6.4)
    private static void tinhTrangPhongTheoLoai() {
        String[] cacLoai = {Phong.THUONG, Phong.DELUXE, Phong.SUITE};
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
            System.out.println(loai + ": " + trong + " trống, " + daThue + " đã thuê");
        }
    }

    // mỗi phòng: cộng thanhTien các CTHopDong của phòng đó,
    // chỉ tính hợp đồng đã có hóa đơn DA_TT (mục 7)
    private static void doanhThuTheoPhong() {
        for (Phong p : DuLieu.dsPhong.getDs()) {
            double tong = 0;
            for (CTHopDong ct : DuLieu.dsCTHopDong.layTheoPhong(p.getMaP())) {
                HoaDon hd = DuLieu.dsHoaDon.timTheoHopDong(ct.getMaHDong());
                if (hd != null && hd.daThanhToan()) {
                    tong += ct.getThanhTien();
                }
            }
            System.out.println("Phòng " + p.getTenP() + ": " + DinhDang.tien(tong));
        }
    }
}
