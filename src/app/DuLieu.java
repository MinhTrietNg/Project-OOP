package app;

import core.DanhSach;
import danhsach.DSCTHopDong;
import danhsach.DSCTKhachO;
import danhsach.DSCTPhieuNhap;
import danhsach.DSCTSuDungDV;
import danhsach.DSDichVu;
import danhsach.DSHDThuePhong;
import danhsach.DSHangHoa;
import danhsach.DSHoaDon;
import danhsach.DSKhachHang;
import danhsach.DSNhaCC;
import danhsach.DSNhanVien;
import danhsach.DSPhieuNhap;
import danhsach.DSPhong;

// giữ 1 bản duy nhất của mỗi danh sách, module nào thêm danh sách thì khai báo ở đây và thêm vào mảng TAT_CA
public final class DuLieu {
    // ----- module 1: Phòng & Khách hàng -----
    public static final DSPhong dsPhong = new DSPhong();
    public static final DSKhachHang dsKhachHang = new DSKhachHang();

    // ----- module 2: Thuê phòng -----
    public static final DSHDThuePhong dsHDThuePhong = new DSHDThuePhong();
    public static final DSCTHopDong dsCTHopDong = new DSCTHopDong();
    public static final DSCTKhachO dsCTKhachO = new DSCTKhachO();

    // ----- module 4: Nhân sự, Kho & Thống kê -----
    public static final DSNhanVien dsNhanVien = new DSNhanVien();
    public static final DSNhaCC dsNhaCC = new DSNhaCC();
    public static final DSHangHoa dsHangHoa = new DSHangHoa();
    public static final DSPhieuNhap dsPhieuNhap = new DSPhieuNhap();
    public static final DSCTPhieuNhap dsCTPhieuNhap = new DSCTPhieuNhap();

    // ----- module 3: Dịch vụ & Thanh toán -----
    public static final DSDichVu dsDichVu = new DSDichVu();
    public static final DSCTSuDungDV dsCTSuDungDV = new DSCTSuDungDV();
    public static final DSHoaDon dsHoaDon = new DSHoaDon();

    // cả 13 danh sách đều implements DanhSach nên gom được vào 1 mảng kiểu interface
    // (phải khai báo SAU các danh sách ở trên, vì static được khởi tạo theo thứ tự viết)
    private static final DanhSach<?>[] TAT_CA = {
        dsPhong, dsKhachHang,                                         // module 1
        dsHDThuePhong, dsCTHopDong, dsCTKhachO,                       // module 2
        dsNhanVien, dsNhaCC, dsHangHoa, dsPhieuNhap, dsCTPhieuNhap,   // module 4
        dsDichVu, dsCTSuDungDV, dsHoaDon                              // module 3
    };

    private DuLieu() {
    }

    // đa hình qua interface: cùng lệnh d.docFile() nhưng mỗi danh sách đọc file của riêng nó
    public static void docTatCa() {
        for (DanhSach<?> d : TAT_CA) {
            d.docFile();
        }
    }
}
