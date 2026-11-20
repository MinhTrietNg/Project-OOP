import app.DuLieu;
import core.DinhDang;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import model.CTHopDong;
import model.CTSuDungDV;
import model.DichVu;
import model.HDThuePhong;
import model.HangHoa;
import model.HoaDon;
import model.NhanVien;

// kiểm thử tự động các hàm có tham số của module 3 (không cần gõ phím, không đọc/ghi file)
// chạy: xem mục "Kiểm thử" trong docs/HUONG_DAN_MUC3.md
public class KiemThuModule3 {

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

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, "UTF-8"));

        // ----- dữ liệu mẫu, chỉ nằm trong bộ nhớ -----
        DuLieu.dsNhanVien.them(new NhanVien("NV001", "Nguyễn Văn An", "0901234567", "Lễ tân", 8000000));
        DuLieu.dsNhanVien.them(new NhanVien("NV002", "Trần Thị Bình", "0912345678", "Quản lý", 15000000));
        DuLieu.dsHangHoa.them(new HangHoa("HH001", "Nước suối", 5000, 10));
        DuLieu.dsHangHoa.them(new HangHoa("HH002", "Khăn tắm", 40000, 3));
        DuLieu.dsDichVu.them(new DichVu("DV001", "Nước suối", 15000, "HH001"));
        DuLieu.dsDichVu.them(new DichVu("DV002", "Giặt ủi", 50000, ""));
        DuLieu.dsDichVu.them(new DichVu("DV004", "Ăn sáng", 100000, ""));

        // hợp đồng 16/11 -> 18/11/2026 (2 ngày), cọc 500.000, thuê 2 phòng
        HDThuePhong hd1 = new HDThuePhong("HDTP001", "KH001", "NV001", LocalDate.of(2026, 11, 16),
                500000, LocalDate.of(2026, 11, 16), LocalDate.of(2026, 11, 18));
        HDThuePhong hd2 = new HDThuePhong("HDTP002", "KH002", "NV001", LocalDate.of(2026, 11, 16),
                0, LocalDate.of(2026, 11, 20), LocalDate.of(2026, 11, 21));
        CTHopDong p1 = new CTHopDong("HDTP001", "P001", 300000, 2, 0);
        CTHopDong p2 = new CTHopDong("HDTP001", "P002", 600000, 1, 0);
        p1.tinhThanhTien(hd1.soNgayThue());
        p2.tinhThanhTien(hd1.soNgayThue());
        CTHopDong[] cacPhong1 = {p1, p2};

        // ----- model -----
        kiemTra("sinhMa dịch vụ = số lớn nhất + 1 (DV001, DV002, DV004 -> DV005)",
                DuLieu.dsDichVu.sinhMa().equals("DV005"));
        kiemTra("sinhMa hóa đơn khi rỗng ra HDON001", DuLieu.dsHoaDon.sinhMa().equals("HDON001"));
        kiemTra("toLine dịch vụ không dùng hàng kết thúc bằng |",
                DuLieu.dsDichVu.timTheoMa("DV002").toLine().equals("DV002|Giặt ủi|50000|"));
        kiemTra("... tách lại vẫn đủ 4 trường, maH rỗng",
                DinhDang.tach("DV002|Giặt ủi|50000|").length == 4
                && DinhDang.tach("DV002|Giặt ủi|50000|")[3].isEmpty());
        kiemTra("coDungHang: DV001 có, DV002 không",
                DuLieu.dsDichVu.timTheoMa("DV001").coDungHang() && !DuLieu.dsDichVu.timTheoMa("DV002").coDungHang());
        kiemTra("trongKhoang tính cả 2 ngày biên",
                CTSuDungDV.trongKhoang(LocalDate.of(2026, 11, 16), hd1.getNgayNhanPhong(), hd1.getNgayTraPhong())
                && CTSuDungDV.trongKhoang(LocalDate.of(2026, 11, 18), hd1.getNgayNhanPhong(), hd1.getNgayTraPhong()));
        kiemTra("tìm dịch vụ không dấu \"giat\" ra Giặt ủi",
                DuLieu.dsDichVu.timTheoTen("giat").length == 1);
        kiemTra("trungTen dịch vụ: \"giặt ủi\" đã có (không phân biệt hoa thường)",
                DuLieu.dsDichVu.trungTen("giặt ủi", ""));
        kiemTra("trungTen dịch vụ: bỏ qua chính dịch vụ đang sửa",
                !DuLieu.dsDichVu.trungTen("Giặt ủi", "DV002"));
        kiemTra("trungTen hàng: \"Khăn tắm\" đã có, \"Xà phòng\" chưa có",
                DuLieu.dsHangHoa.trungTen("Khăn tắm", "") && !DuLieu.dsHangHoa.trungTen("Xà phòng", ""));

        // ----- ghi dịch vụ -----
        kiemTra("ghi 3 nước suối (dùng hàng, đủ kho) trả true",
                DuLieu.dsCTSuDungDV.ghiDichVu(hd1, DuLieu.dsDichVu.timTheoMa("DV001"), LocalDate.of(2026, 11, 17), 3));
        kiemTra("... kho HH001 giảm 10 -> 7", DuLieu.dsHangHoa.timTheoMa("HH001").getSlTon() == 7);
        CTSuDungDV daGhi = DuLieu.dsCTSuDungDV.layTheoHopDong("HDTP001")[0];
        kiemTra("... chép đơn giá 15000 và thành tiền 3 x 15000 = 45000",
                daGhi.getDonGia() == 15000 && daGhi.getThanhTien() == 45000);
        kiemTra("ghi 8 nước suối (kho còn 7) trả false",
                !DuLieu.dsCTSuDungDV.ghiDichVu(hd1, DuLieu.dsDichVu.timTheoMa("DV001"), LocalDate.of(2026, 11, 17), 8));
        kiemTra("... kho giữ nguyên 7 và không ghi thêm dòng",
                DuLieu.dsHangHoa.timTheoMa("HH001").getSlTon() == 7 && DuLieu.dsCTSuDungDV.getN() == 1);
        kiemTra("ghi ngày ngoài thời gian thuê (19/11) trả false",
                !DuLieu.dsCTSuDungDV.ghiDichVu(hd1, DuLieu.dsDichVu.timTheoMa("DV002"), LocalDate.of(2026, 11, 19), 1));
        kiemTra("ngày sai không trừ kho",
                !DuLieu.dsCTSuDungDV.ghiDichVu(hd1, DuLieu.dsDichVu.timTheoMa("DV001"), LocalDate.of(2026, 11, 15), 1)
                && DuLieu.dsHangHoa.timTheoMa("HH001").getSlTon() == 7);
        kiemTra("ghi giặt ủi (không dùng hàng) trả true",
                DuLieu.dsCTSuDungDV.ghiDichVu(hd1, DuLieu.dsDichVu.timTheoMa("DV002"), LocalDate.of(2026, 11, 18), 1));
        kiemTra("... kho các hàng không đổi",
                DuLieu.dsHangHoa.timTheoMa("HH001").getSlTon() == 7 && DuLieu.dsHangHoa.timTheoMa("HH002").getSlTon() == 3);
        kiemTra("ghi đúng bằng số tồn (7/7) trả true, kho về 0",
                DuLieu.dsCTSuDungDV.ghiDichVu(hd2, DuLieu.dsDichVu.timTheoMa("DV001"), LocalDate.of(2026, 11, 20), 7)
                && DuLieu.dsHangHoa.timTheoMa("HH001").getSlTon() == 0);
        kiemTra("layTheoHopDong: HDTP001 có 2 dòng, HDTP002 có 1 dòng",
                DuLieu.dsCTSuDungDV.layTheoHopDong("HDTP001").length == 2
                && DuLieu.dsCTSuDungDV.layTheoHopDong("HDTP002").length == 1);
        kiemTra("tongSoLuong DV001 = 3 + 7 = 10", DuLieu.dsCTSuDungDV.tongSoLuong("DV001") == 10);

        // ----- đổi giá dịch vụ không làm đổi chi tiết đã ghi -----
        DuLieu.dsDichVu.timTheoMa("DV001").setDonGia(20000);
        kiemTra("đổi giá DV001 thì dòng đã ghi vẫn giữ 15000",
                DuLieu.dsCTSuDungDV.layTheoHopDong("HDTP001")[0].getDonGia() == 15000);

        // ----- lập hóa đơn -----
        HoaDon h1 = DuLieu.dsHoaDon.lapHoaDon(hd1, "NV002", cacPhong1);
        kiemTra("lập hóa đơn trả về hóa đơn mã HDON001", h1 != null && h1.getMaHDon().equals("HDON001"));
        kiemTra("tongTienThue = (300000 + 600000) x 2 ngày = 1.800.000", h1.getTongTienThue() == 1800000);
        kiemTra("tongTienDV = 45000 + 50000 = 95000", h1.getTongTienDV() == 95000);
        kiemTra("tongCong = 1.895.000", h1.getTongCong() == 1895000);
        kiemTra("trạng thái mới lập là CHUA_TT", h1.getTrangThai().equals(HoaDon.CHUA_TT));
        kiemTra("ngày lập là hôm nay", h1.getNgayLapHD().equals(LocalDate.now()));
        kiemTra("soConPhaiTra = 1.895.000 - cọc 500.000", h1.soConPhaiTra(hd1.getTienCoc()) == 1395000);
        kiemTra("timTheoHopDong HDTP001 ra HDON001, HDTP002 ra null",
                DuLieu.dsHoaDon.timTheoHopDong("HDTP001") == h1 && DuLieu.dsHoaDon.timTheoHopDong("HDTP002") == null);
        kiemTra("lập hóa đơn lần 2 cho cùng hợp đồng trả null",
                DuLieu.dsHoaDon.lapHoaDon(hd1, "NV002", cacPhong1) == null && DuLieu.dsHoaDon.getN() == 1);
        kiemTra("hợp đồng đã có hóa đơn thì không ghi dịch vụ được",
                !DuLieu.dsCTSuDungDV.ghiDichVu(hd1, DuLieu.dsDichVu.timTheoMa("DV002"), LocalDate.of(2026, 11, 17), 1));
        HoaDon h2 = DuLieu.dsHoaDon.lapHoaDon(hd2, "NV001", new CTHopDong[0]);
        kiemTra("hóa đơn thứ 2 mã HDON002, chỉ có tiền dịch vụ 7 x 15000",
                h2.getMaHDon().equals("HDON002") && h2.getTongCong() == 105000);

        // ----- thanh toán -----
        kiemTra("dsChuaThanhToan có 2 hóa đơn", DuLieu.dsHoaDon.dsChuaThanhToan().length == 2);
        kiemTra("thanhToan HDON001 trả true", DuLieu.dsHoaDon.thanhToan("HDON001"));
        kiemTra("... trạng thái thành DA_TT", h1.daThanhToan());
        kiemTra("thanh toán lần 2 trả false", !DuLieu.dsHoaDon.thanhToan("HDON001"));
        kiemTra("thanh toán mã không có trả false", !DuLieu.dsHoaDon.thanhToan("HDON999"));
        kiemTra("dsChuaThanhToan còn 1 hóa đơn", DuLieu.dsHoaDon.dsChuaThanhToan().length == 1);
        LocalDate homNay = LocalDate.now();
        kiemTra("lọc hóa đơn hôm nay -> hôm nay ra 2", DuLieu.dsHoaDon.locTheoKhoangNgay(homNay, homNay).length == 2);
        kiemTra("lọc hóa đơn năm 2000 ra 0",
                DuLieu.dsHoaDon.locTheoKhoangNgay(LocalDate.of(2000, 1, 1), LocalDate.of(2000, 12, 31)).length == 0);

        // ----- xóa có kiểm tra tham chiếu -----
        DuLieu.dsDichVu.xoa("DV001");
        kiemTra("không xóa được dịch vụ đã có khách dùng", DuLieu.dsDichVu.timTheoMa("DV001") != null);
        DuLieu.dsDichVu.xoa("DV004");
        kiemTra("xóa được dịch vụ chưa ai dùng", DuLieu.dsDichVu.timTheoMa("DV004") == null && DuLieu.dsDichVu.getN() == 2);
        kiemTra("sau khi xóa DV004, sinhMa ra DV003", DuLieu.dsDichVu.sinhMa().equals("DV003"));
        DuLieu.dsHangHoa.xoa("HH001");
        kiemTra("không xóa được hàng đang gắn với dịch vụ", DuLieu.dsHangHoa.timTheoMa("HH001") != null);
        DuLieu.dsNhanVien.xoa("NV002");
        kiemTra("không xóa được nhân viên đã lập hóa đơn", DuLieu.dsNhanVien.timTheoMa("NV002") != null);

        // ----- định dạng dòng file -----
        kiemTra("toLine hóa đơn đúng thứ tự trường",
                h1.toLine().equals("HDON001|HDTP001|NV002|" + DinhDang.ngay(homNay) + "|1800000|95000|1895000|DA_TT"));
        kiemTra("toLine chi tiết dịch vụ ghi ngày dd/MM/yyyy",
                DuLieu.dsCTSuDungDV.layTheoHopDong("HDTP001")[0].toLine().equals("HDTP001|DV001|17/11/2026|3|15000|45000"));

        System.out.println();
        System.out.println("Kết quả: " + dat + " đạt, " + truot + " trượt");
        System.exit(truot == 0 ? 0 : 1);
    }
}
