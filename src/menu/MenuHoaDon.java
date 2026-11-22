package menu;

import app.DuLieu;
import core.NhapLieu;
import java.util.Scanner;

public class MenuHoaDon {

    // Scanner riêng của lớp này; không close() vì sẽ đóng luôn System.in
    private static final Scanner sc = new Scanner(System.in, "UTF-8");

    public static void chay() {
        while (true) {
            int chon = NhapLieu.chonMenu(sc, "TRẢ PHÒNG & HÓA ĐƠN", "Quay lại",
                    "Trả phòng & lập hóa đơn",
                    "Thanh toán hóa đơn",
                    "Xem chi tiết 1 hóa đơn",
                    "Xem tất cả hóa đơn",
                    "Hóa đơn chưa thanh toán",
                    "Lọc hóa đơn theo khoảng ngày");
            switch (chon) {
                case 1:
                    DuLieu.dsHoaDon.them();
                    DuLieu.dsHoaDon.ghiFile();
                    // trả phòng đổi tình trạng phòng nên ghi cả file phòng
                    DuLieu.dsPhong.ghiFile();
                    break;
                case 2:
                    DuLieu.dsHoaDon.thanhToan();
                    DuLieu.dsHoaDon.ghiFile();
                    break;
                case 3:
                    DuLieu.dsHoaDon.timTheoMa();
                    break;
                case 4:
                    DuLieu.dsHoaDon.xuat();
                    break;
                case 5:
                    DuLieu.dsHoaDon.xuatChuaThanhToan();
                    break;
                case 6:
                    DuLieu.dsHoaDon.locTheoKhoangNgay();
                    break;
                default:
                    return;
            }
        }
    }
}
