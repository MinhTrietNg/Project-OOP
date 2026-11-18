import app.DuLieu;
import java.time.LocalDate;
import model.CTHopDong;
import model.CTKhachO;
import model.HDThuePhong;
import model.HoaDon;
import model.KhachHang;
import model.Phong;
import model.PhongDeluxe;
import model.PhongThuong;

// kiểm thử tự động các hàm có tham số của module 2 (không cần gõ phím)
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
        // ----- dữ liệu mẫu, chỉ nằm trong bộ nhớ -----
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
                LocalDate.of(2026, 11, 10), LocalDate.of(2026, 11, 13));
        HDThuePhong hd3 = new HDThuePhong("HDTP003", "KH002", "NV001",
                LocalDate.of(2026, 11, 15), 200000,
                LocalDate.of(2026, 11, 15), LocalDate.of(2026, 11, 17));
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
                LocalDate.of(2026, 11, 20), LocalDate.of(2026, 11, 21));
        kiemTra("soNgayThue thuê qua 1 đêm = 1 ngày", hd1Dem.soNgayThue() == 1);

        // ----- lập hợp đồng (giao dịch liên bảng) -----
        HDThuePhong hd4 = new HDThuePhong("HDTP004", "KH001", "NV001",
                LocalDate.of(2026, 11, 19), 400000,
                LocalDate.of(2026, 11, 19), LocalDate.of(2026, 11, 21));
        CTHopDong ct1 = new CTHopDong("HDTP004", "P001", 300000, 2, 600000);
        CTHopDong ct2 = new CTHopDong("HDTP004", "P002", 600000, 1, 1200000);
        CTKhachO ko1 = new CTKhachO("HDTP004", "P001", "KH001");
        CTKhachO ko2 = new CTKhachO("HDTP004", "P001", "KH002");
        CTKhachO ko3 = new CTKhachO("HDTP004", "P002", "KH001");

        DuLieu.dsHDThuePhong.lapHopDong(hd4, new CTHopDong[]{ct1, ct2}, new CTKhachO[]{ko1, ko2, ko3});

        kiemTra("lapHopDong lưu thành công hợp đồng HDTP004",
                DuLieu.dsHDThuePhong.timTheoMa("HDTP004") != null);
        kiemTra("lapHopDong lưu đủ 2 chi tiết phòng cho HDTP004",
                DuLieu.dsCTHopDong.layTheoHopDong("HDTP004").length == 2);
        kiemTra("lapHopDong lưu đủ 2 khách ở phòng P001 của HDTP004",
                DuLieu.dsCTKhachO.layTheoPhong("HDTP004", "P001").length == 2);
        kiemTra("lapHopDong lưu đủ 3 lượt khách ở trong toàn bộ HDTP004",
                DuLieu.dsCTKhachO.layTheoHopDong("HDTP004").length == 3);
        kiemTra("lapHopDong đổi trạng thái P001, P002 sang DA_THUE",
                DuLieu.dsPhong.timTheoMa("P001").getTinhTrang().equals(Phong.DA_THUE)
                && DuLieu.dsPhong.timTheoMa("P002").getTinhTrang().equals(Phong.DA_THUE));
        kiemTra("... phòng P003 không thuê vẫn giữ TRONG",
                DuLieu.dsPhong.timTheoMa("P003").getTinhTrang().equals(Phong.TRONG));

        // ----- chống trùng khóa chính ở 2 bảng chi tiết -----
        int soCTTruoc = DuLieu.dsCTHopDong.getN();
        DuLieu.dsCTHopDong.them(new CTHopDong("HDTP004", "P001", 300000, 1, 600000));
        kiemTra("DSCTHopDong chặn trùng cặp khóa (HDTP004, P001)",
                DuLieu.dsCTHopDong.getN() == soCTTruoc);

        int soKOTruoc = DuLieu.dsCTKhachO.getN();
        DuLieu.dsCTKhachO.them(new CTKhachO("HDTP004", "P001", "KH001"));
        kiemTra("DSCTKhachO chặn trùng bộ 3 khóa (HDTP004, P001, KH001)",
                DuLieu.dsCTKhachO.getN() == soKOTruoc);

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
                hd1.toLine().equals("HDTP001|KH001|NV001|10/11/2026|500000|10/11/2026|13/11/2026"));
        kiemTra("toLine CTHopDong đúng thứ tự trường",
                ct1.toLine().equals("HDTP004|P001|300000|2|600000"));
        kiemTra("toLine CTKhachO đúng thứ tự trường",
                ko1.toLine().equals("HDTP004|P001|KH001"));

        System.out.println();
        System.out.println("Kết quả: " + dat + " đạt, " + truot + " trượt");
        System.exit(truot == 0 ? 0 : 1);
    }
}