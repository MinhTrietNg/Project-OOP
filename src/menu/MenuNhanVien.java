package menu;

import app.DuLieu;
import core.NhapLieu;
import java.util.Scanner;

public class MenuNhanVien {

    // Scanner riêng của lớp này; không close() vì sẽ đóng luôn System.in
    private static final Scanner sc = new Scanner(System.in, "UTF-8");

    public static void chay() {
        while (true) {
            int chon = NhapLieu.chonMenu(sc, "QUẢN LÝ NHÂN VIÊN", "Quay lại",
                    "Thêm nhân viên",
                    "Sửa nhân viên",
                    "Xóa nhân viên",
                    "Tìm theo mã",
                    "Xem tất cả",
                    "Tìm theo tên",
                    "Xem theo lương giảm dần");
            switch (chon) {
                case 1:
                    DuLieu.dsNhanVien.them();
                    DuLieu.dsNhanVien.ghiFile();
                    break;
                case 2:
                    DuLieu.dsNhanVien.sua();
                    DuLieu.dsNhanVien.ghiFile();
                    break;
                case 3:
                    DuLieu.dsNhanVien.xoa();
                    DuLieu.dsNhanVien.ghiFile();
                    break;
                case 4:
                    DuLieu.dsNhanVien.timTheoMa();
                    break;
                case 5:
                    DuLieu.dsNhanVien.xuat();
                    break;
                case 6:
                    DuLieu.dsNhanVien.timTheoTen();
                    break;
                case 7:
                    DuLieu.dsNhanVien.sapXepTheoLuong();
                    break;
                default:
                    return;
            }
        }
    }
}
