package menu;

import app.DuLieu;
import core.NhapLieu;

public class MenuThuePhong {

    public static void chay() {
        while (true) {
            int chon = NhapLieu.chonMenu("THUÊ PHÒNG", "Quay lại",
                    "Lập hợp đồng thuê phòng",
                    "Sửa ngày trả phòng (HĐ chưa thanh toán)",
                    "Tìm hợp đồng theo mã",
                    "Xem tất cả hợp đồng",
                    "Xem các hợp đồng chưa thanh toán",
                    "Tìm hợp đồng theo tên khách hàng",
                    "Xem danh sách phòng thuê của 1 hợp đồng",
                    "Xem danh sách khách ở theo phòng");
            switch (chon) {
                case 1:
                    DuLieu.dsHDThuePhong.lapHopDong();
                    // lập hợp đồng có thể thêm khách mới, thêm HĐ, chi tiết HĐ, chi tiết khách ở
                    // (riêng dsPhong đã tự ghiFile() trong capNhatTinhTrang)
                    DuLieu.dsHDThuePhong.ghiFile();
                    DuLieu.dsCTHopDong.ghiFile();
                    DuLieu.dsCTKhachO.ghiFile();
                    DuLieu.dsKhachHang.ghiFile();
                    break;
                case 2:
                    DuLieu.dsHDThuePhong.sua();
                    // sửa ngày trả phòng làm đổi thành tiền bên chi tiết hợp đồng nên ghi cả 2 file
                    DuLieu.dsHDThuePhong.ghiFile();
                    DuLieu.dsCTHopDong.ghiFile();
                    break;
                case 3:
                    DuLieu.dsHDThuePhong.timTheoMa();
                    break;
                case 4:
                    DuLieu.dsHDThuePhong.xuat();
                    break;
                case 5:
                    DuLieu.dsHDThuePhong.xuat(DuLieu.dsHDThuePhong.dsChuaThanhToan());
                    break;
                case 6:
                    DuLieu.dsHDThuePhong.timTheoTenKH();
                    break;
                case 7:
                    DuLieu.dsCTHopDong.layTheoHopDong();
                    break;
                case 8:
                    DuLieu.dsCTKhachO.layTheoPhong();
                    break;
                default:
                    return;
            }
        }
    }
}