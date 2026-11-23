package menu;

import app.DuLieu;
import core.NhapLieu;

public class MenuKhachHang {

    public static void chay() {
        while (true) {
            int chon = NhapLieu.chonMenu("QUẢN LÝ KHÁCH HÀNG", "Quay lại",
                    "Thêm khách hàng",
                    "Sửa khách hàng",
                    "Xóa khách hàng",
                    "Tìm khách hàng theo mã",
                    "Tìm khách hàng theo số điện thoại",
                    "Tìm khách hàng theo tên",
                    "Xem tất cả khách hàng");
            switch (chon) {
                case 1:
                    DuLieu.dsKhachHang.them();
                    DuLieu.dsKhachHang.ghiFile();
                    break;
                case 2:
                    DuLieu.dsKhachHang.sua();
                    DuLieu.dsKhachHang.ghiFile();
                    break;
                case 3:
                    DuLieu.dsKhachHang.xoa();
                    DuLieu.dsKhachHang.ghiFile();
                    break;
                case 4: DuLieu.dsKhachHang.timTheoMa(); break;
                case 5: DuLieu.dsKhachHang.timTheoSdt(); break;
                case 6: DuLieu.dsKhachHang.timTheoTen(); break;
                case 7: DuLieu.dsKhachHang.xuat(); break;
                default:
                    return;
            }
        }
    }
}
