import app.DuLieu;
import core.TimKiem;
import java.time.LocalDate;
import model.CTPhieuNhap;
import model.HangHoa;
import model.NhanVien;
import model.PhieuNhap;

// kiểm thử tự động các hàm có tham số của module 4 (không cần gõ phím, không đọc/ghi file)
// chạy: xem mục "Chạy kiểm thử" trong docs/HUONG_DAN_MUC4.md
public class KiemThuModule4 {

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
        DuLieu.dsNhanVien.them(new NhanVien("NV001", "Nguyễn Văn An", "0901234567", "Lễ tân", 8000000));
        DuLieu.dsNhanVien.them(new NhanVien("NV003", "Lê Đức Cường", "0987654321", "Bảo vệ", 6000000));
        DuLieu.dsNhanVien.them(new NhanVien("NV002", "Trần Thị Bình", "0912345678", "Quản lý", 15000000));
        DuLieu.dsHangHoa.them(new HangHoa("HH001", "Nước suối", 5000, 10));
        DuLieu.dsHangHoa.them(new HangHoa("HH002", "Khăn tắm", 40000, 3));
        DuLieu.dsPhieuNhap.them(new PhieuNhap("PN001", "NCC001", "NV001", LocalDate.of(2026, 11, 1), 500000));
        DuLieu.dsPhieuNhap.them(new PhieuNhap("PN002", "NCC001", "NV002", LocalDate.of(2026, 11, 30), 200000));
        DuLieu.dsPhieuNhap.them(new PhieuNhap("PN003", "NCC001", "NV002", LocalDate.of(2026, 12, 1), 100000));
        DuLieu.dsCTPhieuNhap.them(new CTPhieuNhap("PN001", "HH001", 100, 5000, 500000));

        // ----- sinh mã -----
        kiemTra("sinhMa = số lớn nhất + 1 (NV001, NV003, NV002 -> NV004)",
                DuLieu.dsNhanVien.sinhMa().equals("NV004"));
        kiemTra("sinhMa khi danh sách rỗng ra 001",
                new danhsach.DSNhaCC().sinhMa().equals("NCC001"));

        // ----- trừ / cộng kho -----
        kiemTra("truKho đủ hàng trả true", DuLieu.dsHangHoa.truKho("HH001", 4));
        kiemTra("... và tồn giảm 10 -> 6", DuLieu.dsHangHoa.timTheoMa("HH001").getSlTon() == 6);
        kiemTra("truKho không đủ hàng trả false", !DuLieu.dsHangHoa.truKho("HH001", 7));
        kiemTra("... và tồn giữ nguyên 6", DuLieu.dsHangHoa.timTheoMa("HH001").getSlTon() == 6);
        kiemTra("truKho mã không có trả false", !DuLieu.dsHangHoa.truKho("HH999", 1));
        DuLieu.dsHangHoa.congKho("HH001", 20);
        kiemTra("congKho cộng đúng 6 + 20 = 26", DuLieu.dsHangHoa.timTheoMa("HH001").getSlTon() == 26);

        // ----- trùng số điện thoại -----
        kiemTra("trungSdt: sđt của người khác -> true", DuLieu.dsNhanVien.trungSdt("0901234567", ""));
        kiemTra("trungSdt: sđt của chính mình khi sửa -> false",
                !DuLieu.dsNhanVien.trungSdt("0901234567", "NV001"));

        // ----- tìm không dấu -----
        kiemTra("boDau(\"Nguyễn ĐỨC\") = \"nguyen duc\"", TimKiem.boDau("Nguyễn ĐỨC").equals("nguyen duc"));
        kiemTra("tìm \"duc\" ra đúng Lê Đức Cường",
                DuLieu.dsNhanVien.timTheoTen("duc").length == 1
                && DuLieu.dsNhanVien.timTheoTen("duc")[0].getMaNV().equals("NV003"));
        kiemTra("tìm \"xyz\" ra mảng rỗng", DuLieu.dsNhanVien.timTheoTen("xyz").length == 0);

        // ----- sắp xếp -----
        NhanVien[] theoLuong = DuLieu.dsNhanVien.sapXepTheoLuongGiam();
        kiemTra("sắp lương giảm: NV002 > NV001 > NV003",
                theoLuong[0].getMaNV().equals("NV002") && theoLuong[1].getMaNV().equals("NV001")
                && theoLuong[2].getMaNV().equals("NV003"));
        kiemTra("sắp xếp không đổi thứ tự gốc", DuLieu.dsNhanVien.getDs()[0].getMaNV().equals("NV001"));
        HangHoa[] theoTon = DuLieu.dsHangHoa.sapXepTheoTonTang();
        kiemTra("sắp tồn tăng: HH002 (3) trước HH001 (26)", theoTon[0].getMaH().equals("HH002"));

        // ----- hàng sắp hết, giá trị tồn -----
        kiemTra("dsSapHet(5) chỉ có HH002", DuLieu.dsHangHoa.dsSapHet(5).length == 1);
        kiemTra("dsSapHet(3) có tính bằng ngưỡng", DuLieu.dsHangHoa.dsSapHet(3).length == 1);
        kiemTra("tổng giá trị tồn = 26*5000 + 3*40000",
                DuLieu.dsHangHoa.tongGiaTriTon() == 26 * 5000 + 3 * 40000);

        // ----- tính tiền chi tiết, phiếu -----
        CTPhieuNhap ct1 = new CTPhieuNhap("PN009", "HH001", 3, 12000, 0);
        CTPhieuNhap ct2 = new CTPhieuNhap("PN009", "HH002", 2, 50000, 0);
        ct1.tinhThanhTien();
        ct2.tinhThanhTien();
        kiemTra("thanhTien = 3 x 12000 = 36000", ct1.getThanhTien() == 36000);
        PhieuNhap pn = new PhieuNhap();
        pn.tinhTongTien(new CTPhieuNhap[] {ct1, ct2});
        kiemTra("tongTien = 36000 + 100000", pn.getTongTien() == 136000);
        kiemTra("layTheoPhieu PN001 có 1 dòng", DuLieu.dsCTPhieuNhap.layTheoPhieu("PN001").length == 1);

        // ----- lọc ngày, tổng theo tháng -----
        PhieuNhap[] thang11 = DuLieu.dsPhieuNhap.locTheoKhoangNgay(
                LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30));
        kiemTra("lọc 01/11-30/11 lấy cả 2 ngày biên (2 phiếu)", thang11.length == 2);
        kiemTra("tổng tiền nhập tháng 11/2026 = 700000", DuLieu.dsPhieuNhap.tongTienNhap(11, 2026) == 700000);
        kiemTra("tổng tiền nhập tháng 12/2025 = 0", DuLieu.dsPhieuNhap.tongTienNhap(12, 2025) == 0);

        // ----- định dạng dòng file -----
        kiemTra("toLine nhân viên đúng thứ tự trường",
                DuLieu.dsNhanVien.timTheoMa("NV001").toLine().equals("NV001|Nguyễn Văn An|0901234567|Lễ tân|8000000"));
        kiemTra("toLine phiếu nhập ghi ngày dd/MM/yyyy",
                DuLieu.dsPhieuNhap.timTheoMa("PN001").toLine().equals("PN001|NCC001|NV001|01/11/2026|500000"));

        // ----- ca biên: trừ đúng bằng số tồn thì được, kho về 0 -----
        DuLieu.dsHangHoa.them(new HangHoa("HH003", "Bàn chải", 3000, 5));
        kiemTra("truKho đúng bằng số tồn (5/5) trả true", DuLieu.dsHangHoa.truKho("HH003", 5));
        kiemTra("... và tồn về 0", DuLieu.dsHangHoa.timTheoMa("HH003").getSlTon() == 0);

        // ----- lập phiếu nhập (B3): cộng kho, cập nhật giá, lưu phiếu + chi tiết -----
        PhieuNhap pnMoi = new PhieuNhap("PN004", "NCC001", "NV001", LocalDate.of(2027, 1, 5), 0);
        CTPhieuNhap ctMoi = new CTPhieuNhap("PN004", "HH003", 10, 3500, 0);
        ctMoi.tinhThanhTien();
        DuLieu.dsPhieuNhap.lapPhieu(pnMoi, new CTPhieuNhap[] {ctMoi});
        kiemTra("lapPhieu cộng kho HH003: 0 + 10 = 10", DuLieu.dsHangHoa.timTheoMa("HH003").getSlTon() == 10);
        kiemTra("lapPhieu cập nhật giá nhập HH003 = 3500",
                DuLieu.dsHangHoa.timTheoMa("HH003").getDonGia() == 3500);
        kiemTra("lapPhieu lưu phiếu PN004 (tổng 35000) và 1 dòng chi tiết",
                DuLieu.dsPhieuNhap.timTheoMa("PN004") != null
                && DuLieu.dsPhieuNhap.timTheoMa("PN004").getTongTien() == 35000
                && DuLieu.dsCTPhieuNhap.layTheoPhieu("PN004").length == 1);

        System.out.println();
        System.out.println("Kết quả: " + dat + " đạt, " + truot + " trượt");
        System.exit(truot == 0 ? 0 : 1);
    }
}
