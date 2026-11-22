package app;

import core.NhapLieu;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import menu.MenuDichVu;
import menu.MenuHoaDon;
import menu.MenuKhachHang;
import menu.MenuKho;
import menu.MenuNhanVien;
import menu.MenuPhong;
import menu.MenuThongKe;
import menu.MenuThuePhong;

public class Main {

    // Scanner riêng của lớp này; không close() vì sẽ đóng luôn System.in
    private static final Scanner sc = new Scanner(System.in, "UTF-8");

    public static void main(String[] args) throws UnsupportedEncodingException {
        // ép in ra UTF-8 cho khỏi lỗi font tiếng Việt (chạy cmd thì gõ chcp 65001 trước)
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, "UTF-8"));

        DuLieu.docTatCa();

        while (true) {
            int chon = NhapLieu.chonMenu(sc, "QUẢN LÝ KHÁCH SẠN", "Thoát",
                    "Quản lý phòng",
                    "Quản lý khách hàng",
                    "Thuê phòng",
                    "Dịch vụ",
                    "Trả phòng & hóa đơn",
                    "Nhân viên",
                    "Kho",
                    "Thống kê");
            switch (chon) {
                case 1: MenuPhong.chay(); break;
                case 2: MenuKhachHang.chay(); break;
                case 3: MenuThuePhong.chay(); break;
                case 4: MenuDichVu.chay(); break;
                case 5: MenuHoaDon.chay(); break;
                case 6: MenuNhanVien.chay(); break;
                case 7: MenuKho.chay(); break;
                case 8: MenuThongKe.chay(); break;
                default:
                    System.out.println("Tạm biệt!");
                    return;
            }
        }
    }
}
