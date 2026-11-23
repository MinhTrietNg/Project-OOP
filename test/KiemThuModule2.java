import app.DuLieu;
import core.DinhDang;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import model.CTHopDong;
import model.CTKhachO;
import model.HDThuePhong;
import model.HoaDon;
import model.KhachHang;
import model.NhanVien;
import model.Phong;
import model.PhongDeluxe;
import model.PhongThuong;

// kiểm thử tự động module 2 (không cần gõ phím): lập hợp đồng dùng bàn phím giả lập qua System.in
// chạy: xem mục "Chạy kiểm thử" trong docs/HUONG_DAN_MUC2.md
public class KiemThuModule2 {

    private static int dat = 0;
    private static int truot = 0;

    private static void kiemTra(String ten, boolean dung) {
        if (dung) {
            dat++;
            System.out.println("[ĐẠT]   " + ten);
        } else {
            truot++;
            System.out.println("[TRƯỢT] " + ten);
        }
    }

    public static void main(String[] args) {
        // bàn phím giả lập cho dsHDThuePhong.them(): phải đặt trước lần đầu dùng NhapLieu
        // (Scanner toàn cục của NhapLieu được tạo 1 lần, đọc từ System.in lúc đó)
        String nhan = DinhDang.ngay(LocalDate.now());
        String tra = DinhDang.ngay(LocalDate.now().plusDays(2));
        String banPhim = String.join("\n",
                "KH001",                 // khách đứng tên
                "NV001",                 // nhân viên lập
                nhan, tra, "400000",     // ngày nhận, ngày trả (2 ngày), tiền cọc
                "P001", "2",             // phòng P001, 2 người
                "KH001", "c", "KH002",   // 2 khách ở P001
                "P002", "1",             // phòng P002, 1 người
                "0909999999",            // khách ở P002 chưa có -> thêm mới ngay tại chỗ
                "Lê Văn Mới", "0909999999", "Huế",
                "0") + "\n";             // kết thúc chọn phòng
        System.setIn(new ByteArrayInputStream(banPhim.getBytes(StandardCharsets.UTF_8)));

        // ----- dữ liệu mẫu, chỉ nằm trong bộ nhớ -----
        DuLieu.dsNhanVien.them(new NhanVien("NV001", "Nguyễn Văn An", "0901234567", "Lễ tân", 8000000));
        KhachHang kh1 = new KhachHang();
        kh1.setMaKH("KH001");
        kh1.setHoTen("Nguyễn Văn Minh");
        kh1.setSdt("0901112222");
        kh1.setDiaChi("TP.HCM");

        KhachHang kh2 = new KhachHang();
        kh2.setMaKH("KH002");
        kh2.setHoTen("Trần Ngọc Ánh");
        kh2.setSdt("0903334444");
        kh2.setDiaChi("Hà Nội");

        DuLieu.dsKhachHang.them(kh1);
        DuLieu.dsKhachHang.them(kh2);

        PhongThuong p1 = new PhongThuong();
        p1.setMaP("P001");
        p1.setTenP("101");
        p1.setDonGia(300000);
        p1.setSoNguoiToiDa(2);
        p1.setSlGiuong(1);
        p1.setTinhTrang(Phong.TRONG);

        PhongDeluxe p2 = new PhongDeluxe();
        p2.setMaP("P002");
        p2.setTenP("201");
        p2.setDonGia(600000);
        p2.setSoNguoiToiDa(2);
        p2.setSlGiuong(1);
        p2.setTinhTrang(Phong.TRONG);

        PhongThuong p3 = new PhongThuong();
        p3.setMaP("P003");
        p3.setTenP("102");
        p3.setDonGia(300000);
        p3.setSoNguoiToiDa(2);
        p3.setSlGiuong(1);
        p3.setTinhTrang(Phong.TRONG);

        DuLieu.dsPhong.them(p1);
        DuLieu.dsPhong.them(p2);
        DuLieu.dsPhong.them(p3);

        HDThuePhong hd1 = new HDThuePhong("HDTP001", "KH001", "NV001",
                LocalDate.of(2026, 11, 10), 500000,
                LocalDate.of(2026, 11, 10), LocalDate.of(2026, 11, 13), 0);
        HDThuePhong hd3 = new HDThuePhong("HDTP003", "KH002", "NV001",
                LocalDate.of(2026, 11, 15), 200000,
                LocalDate.of(2026, 11, 15), LocalDate.of(2026, 11, 17), 0);
        DuLieu.dsHDThuePhong.them(hd1);
        DuLieu.dsHDThuePhong.them(hd3);

        // ----- sinh mã -----
        kiemTra("sinhMa = số lớn nhất + 1 (HDTP001, HDTP003 -> HDTP004)",
                DuLieu.dsHDThuePhong.sinhMa().equals("HDTP004"));
        kiemTra("sinhMa khi danh sách rỗng ra HDTP001",
                new danhsach.DSHDThuePhong().sinhMa().equals("HDTP001"));

        // ----- tính số ngày thuê & thành tiền chi tiết -----
        kiemTra("soNgayThue từ 10/11 đến 13/11 = 3 ngày", hd1.soNgayThue() == 3);
        CTHopDong ctMau = new CTHopDong("HDTP001", "P001", 300000, 2, 0);
        ctMau.tinhThanhTien(hd1.soNgayThue());
        kiemTra("thanhTien = 300000 x 3 = 900000", ctMau.getThanhTien() == 900000);

        // ----- ca biên: thuê 1 đêm (hôm nay nhận, ngày mai trả) -----
        HDThuePhong hd1Dem = new HDThuePhong("HDTP099", "KH001", "NV001",
                LocalDate.of(2026, 11, 20), 0,
                LocalDate.of(2026, 11, 20), LocalDate.of(2026, 11, 21), 0);
        kiemTra("soNgayThue thuê qua 1 đêm = 1 ngày", hd1Dem.soNgayThue() == 1);

        // ----- lập hợp đồng từ bàn phím (giao dịch liên bảng) -----
        DuLieu.dsHDThuePhong.them();
        System.out.println();

        HDThuePhong hd4 = DuLieu.dsHDThuePhong.timTheoMa("HDTP004");
        kiemTra("them() lưu thành công hợp đồng HDTP004", hd4 != null);
        kiemTra("them() tính tongTien = 300000 x 2 + 600000 x 2 = 1.800.000", hd4.getTongTien() == 1800000);
        kiemTra("them() lưu đủ 2 chi tiết phòng cho HDTP004",
                DuLieu.dsCTHopDong.layTheoHopDong("HDTP004").length == 2);
        kiemTra("them() lưu đủ 2 khách ở phòng P001 của HDTP004",
                DuLieu.dsCTKhachO.layTheoPhong("HDTP004", "P001").length == 2);
        kiemTra("them() lưu đủ 3 lượt khách ở trong toàn bộ HDTP004",
                DuLieu.dsCTKhachO.layTheoHopDong("HDTP004").length == 3);
        kiemTra("them() đổi trạng thái P001, P002 sang DA_THUE",
                DuLieu.dsPhong.timTheoMa("P001").getTinhTrang().equals(Phong.DA_THUE)
                && DuLieu.dsPhong.timTheoMa("P002").getTinhTrang().equals(Phong.DA_THUE));
        kiemTra("... phòng P003 không thuê vẫn giữ TRONG",
                DuLieu.dsPhong.timTheoMa("P003").getTinhTrang().equals(Phong.TRONG));
        kiemTra("khách mới nhập ngay khi lập hợp đồng được thêm với mã KH003",
                DuLieu.dsKhachHang.timTheoMa("KH003") != null
                && DuLieu.dsKhachHang.timTheoMa("KH003").getHoTen().equals("Lê Văn Mới"));
        kiemTra("... và được ghi là khách ở phòng P002",
                DuLieu.dsCTKhachO.timTheoKhoa("HDTP004", "P002", "KH003") != null);

        // ----- chống trùng khóa ở them(T) -----
        kiemTra("DSCTHopDong.them trả false khi trùng cặp khóa (HDTP004, P001)",
                !DuLieu.dsCTHopDong.them(new CTHopDong("HDTP004", "P001", 300000, 1, 600000))
                && DuLieu.dsCTHopDong.layTheoHopDong("HDTP004").length == 2);
        kiemTra("DSCTKhachO.them trả false khi trùng bộ 3 khóa (HDTP004, P001, KH001)",
                !DuLieu.dsCTKhachO.them(new CTKhachO("HDTP004", "P001", "KH001"))
                && DuLieu.dsCTKhachO.layTheoHopDong("HDTP004").length == 3);
        kiemTra("DSHDThuePhong.them trả false khi trùng mã HDTP001",
                !DuLieu.dsHDThuePhong.them(new HDThuePhong("HDTP001", "KH002", "NV001",
                        LocalDate.of(2026, 1, 1), 0, LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 2), 0)));

        // ----- tra cứu theo khóa, theo phòng & theo hợp đồng -----
        kiemTra("timTheoKhoa (HDTP004, P002) tìm đúng chi tiết phòng",
                DuLieu.dsCTHopDong.timTheoKhoa("HDTP004", "P002") != null);
        kiemTra("timTheoKhoa (HDTP004, P999) không có trả null",
                DuLieu.dsCTHopDong.timTheoKhoa("HDTP004", "P999") == null);
        kiemTra("layTheoPhong P001 có 1 lượt thuê",
                DuLieu.dsCTHopDong.layTheoPhong("P001").length == 1);

        // ----- tìm theo mã KH, tìm tên KH không dấu -----
        kiemTra("timTheoMaKH(\"KH001\") ra 2 hợp đồng (HDTP001, HDTP004)",
                DuLieu.dsHDThuePhong.timTheoMaKH("KH001").length == 2);
        kiemTra("timTheoTenKH(\"ngoc anh\") không dấu ra đúng HDTP003",
                DuLieu.dsHDThuePhong.timTheoTenKH("ngoc anh").length == 1
                && DuLieu.dsHDThuePhong.timTheoTenKH("ngoc anh")[0].getMaHDong().equals("HDTP003"));
        kiemTra("timTheoTenKH(\"xyz\") không có ra mảng rỗng",
                DuLieu.dsHDThuePhong.timTheoTenKH("xyz").length == 0);

        // ----- lọc hợp đồng chưa thanh toán -----
        kiemTra("dsChuaThanhToan ban đầu có 3 hợp đồng",
                DuLieu.dsHDThuePhong.dsChuaThanhToan().length == 3);
        HoaDon hdon1 = new HoaDon();
        hdon1.setMaHDon("HDON001");
        hdon1.setMaHDong("HDTP001");
        DuLieu.dsHoaDon.them(hdon1);
        kiemTra("sau khi lập hóa đơn cho HDTP001, dsChuaThanhToan còn 2 hợp đồng",
                DuLieu.dsHDThuePhong.dsChuaThanhToan().length == 2);

        // ----- định dạng dòng file (toLine) -----
        kiemTra("toLine HDThuePhong đúng thứ tự trường và ngày dd/MM/yyyy",
                hd1.toLine().equals("HDTP001|KH001|NV001|10/11/2026|500000|10/11/2026|13/11/2026|0"));
        kiemTra("toLine CTHopDong đúng thứ tự trường",
                DuLieu.dsCTHopDong.timTheoKhoa("HDTP004", "P001").toLine().equals("HDTP004|P001|300000|2|600000"));
        kiemTra("toLine CTKhachO đúng thứ tự trường",
                DuLieu.dsCTKhachO.timTheoKhoa("HDTP004", "P001", "KH001").toLine().equals("HDTP004|P001|KH001"));

        System.out.println();
        System.out.println("Kết quả: " + dat + " đạt, " + truot + " trượt");
        System.exit(truot == 0 ? 0 : 1);
    }
}