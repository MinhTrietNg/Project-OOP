package menu;

import app.DuLieu;
import core.NhapLieu;
import java.util.Scanner;

public class MenuDichVu {

    // Scanner riêng của lớp này; không close() vì sẽ đóng luôn System.in
    private static final Scanner sc = new Scanner(System.in, "UTF-8");

    public static void chay() {
        while (true) {
            int chon = NhapLieu.chonMenu(sc, "DỊCH VỤ", "Quay lại",
                    "Thêm dịch vụ",
                    "Sửa dịch vụ",
                    "Xóa dịch vụ",
                    "Tìm theo mã",
                    "Xem tất cả",
                    "Tìm theo tên",
                    "Ghi dịch vụ khách dùng",
                    "Xem dịch vụ đã dùng của 1 hợp đồng",
                    "Thống kê theo dịch vụ");
            switch (chon) {
                case 1:
                    DuLieu.dsDichVu.them();
                    DuLieu.dsDichVu.ghiFile();
                    break;
                case 2:
                    DuLieu.dsDichVu.sua();
                    DuLieu.dsDichVu.ghiFile();
                    break;
                case 3:
                    DuLieu.dsDichVu.xoa();
                    DuLieu.dsDichVu.ghiFile();
                    break;
                case 4:
                    DuLieu.dsDichVu.timTheoMa();
                    break;
                case 5:
                    DuLieu.dsDichVu.xuat();
                    break;
                case 6:
                    DuLieu.dsDichVu.timTheoTen();
                    break;
                case 7:
                    DuLieu.dsCTSuDungDV.ghiDichVu();
                    // ghi dịch vụ có thể trừ kho nên ghi cả 2 file
                    DuLieu.dsCTSuDungDV.ghiFile();
                    DuLieu.dsHangHoa.ghiFile();
                    break;
                case 8:
                    DuLieu.dsCTSuDungDV.xuatTheoHopDong();
                    break;
                case 9:
                    DuLieu.dsCTSuDungDV.thongKeTheoDichVu();
                    break;
                default:
                    return;
            }
        }
    }
}
