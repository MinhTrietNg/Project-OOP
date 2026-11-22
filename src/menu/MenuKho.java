package menu;

import app.DuLieu;
import core.NhapLieu;
import java.util.Scanner;

// menu Kho gồm 3 menu con: nhà cung cấp, hàng hóa, phiếu nhập
public class MenuKho {

    // Scanner riêng của lớp này; không close() vì sẽ đóng luôn System.in
    private static final Scanner sc = new Scanner(System.in, "UTF-8");

    public static void chay() {
        while (true) {
            int chon = NhapLieu.chonMenu(sc, "KHO", "Quay lại",
                    "Nhà cung cấp",
                    "Hàng hóa",
                    "Phiếu nhập hàng");
            switch (chon) {
                case 1: menuNhaCC(); break;
                case 2: menuHangHoa(); break;
                case 3: menuPhieuNhap(); break;
                default:
                    return;
            }
        }
    }

    private static void menuNhaCC() {
        while (true) {
            int chon = NhapLieu.chonMenu(sc, "NHÀ CUNG CẤP", "Quay lại",
                    "Thêm nhà cung cấp",
                    "Sửa nhà cung cấp",
                    "Xóa nhà cung cấp",
                    "Tìm theo mã",
                    "Xem tất cả",
                    "Tìm theo tên");
            switch (chon) {
                case 1:
                    DuLieu.dsNhaCC.them();
                    DuLieu.dsNhaCC.ghiFile();
                    break;
                case 2:
                    DuLieu.dsNhaCC.sua();
                    DuLieu.dsNhaCC.ghiFile();
                    break;
                case 3:
                    DuLieu.dsNhaCC.xoa();
                    DuLieu.dsNhaCC.ghiFile();
                    break;
                case 4:
                    DuLieu.dsNhaCC.timTheoMa();
                    break;
                case 5:
                    DuLieu.dsNhaCC.xuat();
                    break;
                case 6:
                    DuLieu.dsNhaCC.timTheoTen();
                    break;
                default:
                    return;
            }
        }
    }

    private static void menuHangHoa() {
        while (true) {
            int chon = NhapLieu.chonMenu(sc, "HÀNG HÓA", "Quay lại",
                    "Thêm hàng hóa",
                    "Sửa tên hàng hóa",
                    "Xóa hàng hóa",
                    "Tìm theo mã",
                    "Xem tất cả",
                    "Tìm theo tên",
                    "Hàng sắp hết");
            switch (chon) {
                case 1:
                    DuLieu.dsHangHoa.them();
                    DuLieu.dsHangHoa.ghiFile();
                    break;
                case 2:
                    DuLieu.dsHangHoa.sua();
                    DuLieu.dsHangHoa.ghiFile();
                    break;
                case 3:
                    DuLieu.dsHangHoa.xoa();
                    DuLieu.dsHangHoa.ghiFile();
                    break;
                case 4:
                    DuLieu.dsHangHoa.timTheoMa();
                    break;
                case 5:
                    DuLieu.dsHangHoa.xuat();
                    break;
                case 6:
                    DuLieu.dsHangHoa.timTheoTen();
                    break;
                case 7:
                    DuLieu.dsHangHoa.xuatSapHet();
                    break;
                default:
                    return;
            }
        }
    }

    private static void menuPhieuNhap() {
        while (true) {
            int chon = NhapLieu.chonMenu(sc, "PHIẾU NHẬP HÀNG", "Quay lại",
                    "Lập phiếu nhập",
                    "Xem tất cả phiếu",
                    "Xem chi tiết 1 phiếu",
                    "Lọc phiếu theo khoảng ngày");
            switch (chon) {
                case 1:
                    DuLieu.dsPhieuNhap.them();
                    // lập phiếu làm thay đổi 3 danh sách nên ghi cả 3 file
                    DuLieu.dsPhieuNhap.ghiFile();
                    DuLieu.dsCTPhieuNhap.ghiFile();
                    DuLieu.dsHangHoa.ghiFile();
                    break;
                case 2:
                    DuLieu.dsPhieuNhap.xuat();
                    break;
                case 3:
                    DuLieu.dsPhieuNhap.timTheoMa();
                    break;
                case 4:
                    DuLieu.dsPhieuNhap.locTheoKhoangNgay();
                    break;
                default:
                    return;
            }
        }
    }
}
