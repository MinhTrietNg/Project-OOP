package menu;

import app.DuLieu;
import core.NhapLieu;

public class MenuPhong {

    public static void chay() {
        while (true) {
            int chon = NhapLieu.chonMenu("QUẢN LÝ PHÒNG", "Quay lại",
                    "Thêm phòng",
                    "Sửa phòng",
                    "Xóa phòng",
                    "Tìm phòng theo mã",
                    "Xem tất cả phòng",
                    "Xem phòng trống",
                    "Lọc phòng theo loại",
                    "Sắp xếp theo giá thuê");
            switch (chon) {
                case 1:
                    DuLieu.dsPhong.them();
                    DuLieu.dsPhong.ghiFile();
                    break;
                case 2:
                    DuLieu.dsPhong.sua();
                    DuLieu.dsPhong.ghiFile();
                    break;
                case 3:
                    DuLieu.dsPhong.xoa();
                    DuLieu.dsPhong.ghiFile();
                    break;
                case 4: DuLieu.dsPhong.timTheoMa(); break;
                case 5: DuLieu.dsPhong.xuat(); break;
                case 6: DuLieu.dsPhong.xuatPhongTrong(); break;
                case 7: DuLieu.dsPhong.locTheoLoai(); break;
                case 8: DuLieu.dsPhong.sapXepTheoGia(); break;
                default:
                    return;
            }
        }
    }
}
