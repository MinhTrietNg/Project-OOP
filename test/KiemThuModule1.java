
import app.DuLieu;
import danhsach.DSKhachHang;
import danhsach.DSPhong;
import java.time.LocalDate;
import model.CTHopDong;
import model.HDThuePhong;
import model.KhachHang;
import model.Phong;
import model.PhongDeluxe;
import model.PhongSuite;
import model.PhongThuong;

// kiểm thử tự động các hàm có tham số của module 1 (không cần gõ phím)
// chạy: javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src, test -Filter *.java).FullName
//       java -cp out KiemThuModule1
// lưu ý: có ghi data/phong.txt và data/khachhang.txt, sao lưu trước nếu đang có dữ liệu thật
public class KiemThuModule1 {

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
        DSPhong dsP = DuLieu.dsPhong;
        DSKhachHang dsKH = DuLieu.dsKhachHang;

        // ----- PHÒNG -----
        kiemTra("sinhMa danh sách rỗng = P001", dsP.sinhMa().equals("P001"));

        dsP.them(new PhongThuong("P001", "101", 300000, 2, 1, Phong.TRONG));
        dsP.them(new PhongDeluxe("P002", "201", 600000, 2, 1, Phong.TRONG, "Biển", true, "Gỗ sồi"));
        dsP.them(new PhongSuite("P005", "301", 1200000, 4, 2, Phong.DA_THUE, true, 2));
        kiemTra("them(p) giữ nguyên mã truyền vào", dsP.timTheoMa("p005") != null);
        kiemTra("sinhMa = số lớn nhất + 1 = P006", dsP.sinhMa().equals("P006"));
        kiemTra("mảng chứa đúng lớp con (đa hình)",
                dsP.getDs()[1] instanceof PhongDeluxe && dsP.getDs()[2].getLoaiPhong().equals(Phong.SUITE));

        PhongDeluxe ngoai = new PhongDeluxe("P009", "202", 700000, 2, 1, Phong.TRONG, "Phố", false, "Hiện đại");
        dsP.them(ngoai);
        ngoai.setView("ĐÃ ĐỔI");
        kiemTra("them(p) lưu bản sao, sửa bên ngoài không ảnh hưởng",
                ((PhongDeluxe) dsP.timTheoMa("P009")).getView().equals("Phố"));

        kiemTra("toLine PhongThuong", dsP.timTheoMa("P001").toLine().equals("THUONG|P001|101|300000|2|1|TRONG"));
        kiemTra("toLine PhongDeluxe", dsP.timTheoMa("P002").toLine()
                .equals("DELUXE|P002|201|600000|2|1|TRONG|Biển|true|Gỗ sồi"));
        kiemTra("toLine PhongSuite", dsP.timTheoMa("P005").toLine()
                .equals("SUITE|P005|301|1200000|4|2|DA_THUE|true|2"));

        kiemTra("dsPhongTrong có 3 phòng", dsP.dsPhongTrong().length == 3);
        dsP.capNhatTinhTrang("P001", Phong.DA_THUE);
        kiemTra("capNhatTinhTrang đổi sang DA_THUE",
                dsP.timTheoMa("P001").getTinhTrang().equals(Phong.DA_THUE) && dsP.dsPhongTrong().length == 2);

        kiemTra("trungTenP: số 201 đã có", dsP.trungTenP("201", ""));
        kiemTra("trungTenP: bỏ qua chính phòng đang sửa", !dsP.trungTenP("201", "P002"));
        kiemTra("locTheoLoai DELUXE có 2 phòng", dsP.locTheoLoai(Phong.DELUXE).length == 2);
        kiemTra("timTheoTen \"20\" ra 2 phòng (201, 202)", dsP.timTheoTen("20").length == 2);
        kiemTra("timTheoTen \"999\" ra mảng rỗng", dsP.timTheoTen("999").length == 0);

        Phong[] sx = dsP.sapXepTheoGiaTang();
        kiemTra("sapXepTheoGiaTang: rẻ nhất đầu, đắt nhất cuối",
                sx[0].getMaP().equals("P001") && sx[3].getMaP().equals("P005"));
        kiemTra("sắp xếp không đổi thứ tự gốc", dsP.getDs()[2].getMaP().equals("P005"));

        // ghi file rồi đọc lại vào danh sách mới
        dsP.ghiFile();
        DSPhong docLai = new DSPhong();
        docLai.docFile();
        kiemTra("ghi/đọc file phòng đủ 4 phòng, đúng lớp con",
                docLai.getN() == 4 && docLai.timTheoMa("P005") instanceof PhongSuite
                        && ((PhongDeluxe) docLai.timTheoMa("P002")).isBanCong());

        // xóa: phòng đã có trong hợp đồng thì không xóa được
        DuLieu.dsCTHopDong.them(new CTHopDong("HDTP001", "P002", 600000, 2, 1200000));
        dsP.xoa("P002");
        kiemTra("không xóa phòng đã có trong hợp đồng", dsP.timTheoMa("P002") != null);
        dsP.xoa("P009");
        kiemTra("xóa phòng chưa dùng, mảng còn đúng n phần tử",
                dsP.timTheoMa("P009") == null && dsP.getN() == 3 && dsP.getDs().length == 3);

        // ----- KHÁCH HÀNG -----
        kiemTra("sinhMa khách rỗng = KH001", dsKH.sinhMa().equals("KH001"));
        dsKH.them(new KhachHang("KH001", "Nguyễn Văn Minh", "0901112222", "TP.HCM"));
        dsKH.them(new KhachHang("KH003", "Trần Ngọc Ánh", "0903334444", "Hà Nội"));
        kiemTra("sinhMa khách = KH004", dsKH.sinhMa().equals("KH004"));
        kiemTra("timTheoSdt đúng khách", dsKH.timTheoSdt("0903334444").getMaKH().equals("KH003"));
        kiemTra("timTheoSdt không có thì null", dsKH.timTheoSdt("0999999999") == null);
        kiemTra("timTheoTen gõ không dấu", dsKH.timTheoTen("ngoc anh").length == 1);
        kiemTra("toLine KhachHang", dsKH.timTheoMa("KH001").toLine()
                .equals("KH001|Nguyễn Văn Minh|0901112222|TP.HCM"));

        dsKH.ghiFile();
        DSKhachHang khDocLai = new DSKhachHang();
        khDocLai.docFile();
        kiemTra("ghi/đọc file khách hàng", khDocLai.getN() == 2 && khDocLai.timTheoMa("KH003") != null);

        DuLieu.dsHDThuePhong.them(new HDThuePhong("HDTP001", "KH001", "NV001", LocalDate.of(2026, 11, 16),
                500000, LocalDate.of(2026, 11, 16), LocalDate.of(2026, 11, 18), 0));
        dsKH.xoa("KH001");
        kiemTra("không xóa khách đã đứng tên hợp đồng", dsKH.timTheoMa("KH001") != null);
        dsKH.xoa("KH003");
        kiemTra("xóa khách chưa dùng", dsKH.timTheoMa("KH003") == null && dsKH.getN() == 1);

        System.out.println();
        System.out.println("Kết quả: " + dat + " đạt, " + truot + " trượt");
        System.exit(truot == 0 ? 0 : 1);
    }
}
