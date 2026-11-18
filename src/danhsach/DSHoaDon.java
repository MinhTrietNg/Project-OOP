package danhsach;

import app.DuLieu;
import core.DinhDang;
import core.NhapLieu;
import core.TepTin;
import java.time.LocalDate;
import java.util.Arrays;
import model.CTHopDong;
import model.CTSuDungDV;
import model.DichVu;
import model.HDThuePhong;
import model.HoaDon;
import model.NhanVien;

// hóa đơn là chứng từ: chỉ lập mới, thanh toán và xem; không sửa tiền, không xóa (mục 6.5)
public class DSHoaDon {

    private static final String FILE = "data/hoadon.txt";
    private HoaDon[] ds = new HoaDon[0];
    private int n = 0;

    public HoaDon[] getDs() {
        return ds;
    }

    public int getN() {
        return n;
    }

    // ================= TRẢ PHÒNG & LẬP HÓA ĐƠN (nghiệp vụ mục 6.3) =================
    //   B1. them()     : nhập mã hợp đồng (chưa có hóa đơn), chọn nhân viên lập
    //   B2. lapHoaDon(): lấy các phòng + các lần dùng dịch vụ, tính tiền, lưu hóa đơn CHUA_TT
    //   B3.              trả mọi phòng của hợp đồng về TRONG
    // Menu gọi xong thì ghi file hóa đơn (và file phòng khi đã ghép module Phòng).

    public void them() {
        if (DuLieu.dsNhanVien.getN() == 0) {
            System.out.println("Cần có nhân viên trước khi lập hóa đơn.");
            return;
        }

        // ----- B1 -----
        String maHDong = NhapLieu.nhapChuoi("Nhập mã hợp đồng trả phòng");
        HDThuePhong hd = timHopDong(maHDong);
        if (hd == null) {
            return;
        }
        HoaDon cu = timTheoHopDong(hd.getMaHDong());
        if (cu != null) {
            System.out.println("Hợp đồng này đã có hóa đơn " + cu.getMaHDon() + ".");
            return;
        }
        // trả sớm hơn hợp đồng thì phải sửa ngày trả bên Thuê phòng trước, để tiền phòng tính lại đúng
        if (LocalDate.now().isBefore(hd.getNgayTraPhong())) {
            System.out.println("  Lưu ý: ngày trả trong hợp đồng là " + DinhDang.ngay(hd.getNgayTraPhong())
                    + ". Nếu khách trả sớm, hãy sửa ngày trả ở menu Thuê phòng trước.");
            if (!NhapLieu.nhapCoKhong("Vẫn lập hóa đơn theo ngày trong hợp đồng")) {
                System.out.println("Đã hủy lập hóa đơn.");
                return;
            }
        }
        DuLieu.dsNhanVien.xuat();
        NhanVien nv = DuLieu.dsNhanVien.chon();
        if (nv == null) {
            System.out.println("Đã hủy lập hóa đơn.");
            return;
        }

        // ----- B2, B3 -----
        HoaDon hdon = lapHoaDon(hd, nv.getMaNV(), layPhongCuaHopDong(hd.getMaHDong()));
        if (hdon != null) {
            System.out.println("Đã lập hóa đơn " + hdon.getMaHDon() + ":");
            xuatChiTiet(hdon.getMaHDon());
        }
    }

    // bản có tham số: tính tiền, lưu hóa đơn, trả phòng; hợp đồng đã có hóa đơn thì trả null
    // cacPhong = các CTHopDong của hợp đồng (truyền vào để kiểm thử được khi chưa ghép module 2)
    public HoaDon lapHoaDon(HDThuePhong hd, String maNV, CTHopDong[] cacPhong) {
        if (timTheoHopDong(hd.getMaHDong()) != null) {
            System.out.println("Hợp đồng này đã có hóa đơn.");
            return null;
        }
        CTSuDungDV[] cacDV = DuLieu.dsCTSuDungDV.layTheoHopDong(hd.getMaHDong());

        HoaDon hdon = new HoaDon();
        hdon.nhap();                         // ngày lập = hôm nay, trạng thái CHUA_TT
        hdon.setMaHDon(sinhMa());
        hdon.setMaHDong(hd.getMaHDong());
        hdon.setMaNV(maNV);
        hdon.tinhTien(cacPhong, cacDV);
        them(hdon);

        traPhong(cacPhong);
        return timTheoMa(hdon.getMaHDon());
    }

    public void them(HoaDon hdon) {
        ds = Arrays.copyOf(ds, n + 1);
        ds[n] = new HoaDon(hdon);
        n++;
    }

    // ================= THANH TOÁN =================
    // hóa đơn không sửa tay; "sửa" duy nhất là đổi trạng thái CHUA_TT -> DA_TT

    public void thanhToan() {
        String ma = NhapLieu.nhapChuoi("Nhập mã hóa đơn cần thanh toán");
        HoaDon hdon = timTheoMa(ma);
        if (hdon == null) {
            System.out.println("Không tìm thấy hóa đơn có mã: " + ma);
            return;
        }
        if (hdon.daThanhToan()) {
            System.out.println("Hóa đơn " + hdon.getMaHDon() + " đã thanh toán rồi.");
            return;
        }
        xuatChiTiet(hdon.getMaHDon());
        if (NhapLieu.nhapCoKhong("Xác nhận khách đã thanh toán")) {
            thanhToan(hdon.getMaHDon());
            System.out.println("Đã thanh toán hóa đơn " + hdon.getMaHDon());
        }
    }

    // trả false nếu không có hóa đơn hoặc đã thanh toán rồi
    public boolean thanhToan(String ma) {
        HoaDon hdon = timTheoMa(ma);
        if (hdon == null || hdon.daThanhToan()) {
            return false;
        }
        hdon.setTrangThai(HoaDon.DA_TT);
        return true;
    }

    // ================= CÁC HÀM BẮT BUỘC KHÁC (gói 50%) =================

    // tìm theo mã thì in luôn chi tiết hóa đơn
    public void timTheoMa() {
        String ma = NhapLieu.nhapChuoi("Nhập mã hóa đơn");
        xuatChiTiet(ma);
    }

    public HoaDon timTheoMa(String ma) {
        for (int i = 0; i < n; i++) {
            if (ds[i].getMaHDon().equalsIgnoreCase(ma)) {
                return ds[i];
            }
        }
        return null;
    }

    // hợp đồng đã có hóa đơn chưa; chưa thì null (mục 6.6)
    public HoaDon timTheoHopDong(String maHDong) {
        for (int i = 0; i < n; i++) {
            if (ds[i].getMaHDong().equalsIgnoreCase(maHDong)) {
                return ds[i];
            }
        }
        return null;
    }

    public void xuat() {
        xuat(ds);
    }

    public void xuat(HoaDon[] mang) {
        if (mang.length == 0) {
            System.out.println("Danh sách trống.");
        }
        for (HoaDon hdon : mang) {
            hdon.xuat();
        }
    }

    // in 1 hóa đơn đầy đủ: đầu hóa đơn, từng phòng, từng dịch vụ, tổng, cọc, còn phải trả
    public void xuatChiTiet(String maHDon) {
        HoaDon hdon = timTheoMa(maHDon);
        if (hdon == null) {
            System.out.println("Không tìm thấy hóa đơn có mã: " + maHDon);
            return;
        }
        NhanVien nv = DuLieu.dsNhanVien.timTheoMa(hdon.getMaNV());
        System.out.println("----- HÓA ĐƠN " + hdon.getMaHDon() + " -----");
        System.out.println("Hợp đồng    : " + hdon.getMaHDong());
        System.out.println("Ngày lập    : " + DinhDang.ngay(hdon.getNgayLapHD()));
        System.out.println("Nhân viên   : " + (nv == null ? hdon.getMaNV() : nv.getHoTen()));

        System.out.println("Tiền phòng:");
        for (CTHopDong ct : layPhongCuaHopDong(hdon.getMaHDong())) {
            System.out.println("  Phòng " + ct.getMaP() + ": " + DinhDang.tien(ct.getThanhTien()));
        }
        System.out.println("  Cộng tiền phòng: " + DinhDang.tien(hdon.getTongTienThue()));

        System.out.println("Tiền dịch vụ:");
        for (CTSuDungDV ct : DuLieu.dsCTSuDungDV.layTheoHopDong(hdon.getMaHDong())) {
            DichVu dv = DuLieu.dsDichVu.timTheoMa(ct.getMaDV());
            String tenDV = (dv == null) ? ct.getMaDV() : dv.getTenDV();
            System.out.println("  " + DinhDang.ngay(ct.getNgaySD()) + " " + tenDV + ": " + ct.getSl()
                    + " x " + DinhDang.tien(ct.getDonGia()) + " = " + DinhDang.tien(ct.getThanhTien()));
        }
        System.out.println("  Cộng tiền dịch vụ: " + DinhDang.tien(hdon.getTongTienDV()));

        System.out.println("TỔNG CỘNG   : " + DinhDang.tien(hdon.getTongCong()));
        HDThuePhong hd = timHopDongKhongBao(hdon.getMaHDong());
        if (hd != null) {
            System.out.println("Đã cọc      : " + DinhDang.tien(hd.getTienCoc()));
            System.out.println("Còn phải trả: " + DinhDang.tien(hdon.soConPhaiTra(hd.getTienCoc())));
        }
        System.out.println("Trạng thái  : " + (hdon.daThanhToan() ? "Đã thanh toán" : "Chưa thanh toán"));
    }

    // "HDON012".substring(4) = "012" -> 12
    public String sinhMa() {
        int max = 0;
        for (int i = 0; i < n; i++) {
            int so = Integer.parseInt(ds[i].getMaHDon().substring(4));
            if (so > max) {
                max = so;
            }
        }
        return String.format("HDON%03d", max + 1);
    }

    // ================= HÀM NÂNG CAO (gói 70%) =================

    public void xuatChuaThanhToan() {
        HoaDon[] kq = dsChuaThanhToan();
        xuat(kq);
        double tong = 0;
        for (HoaDon hdon : kq) {
            tong += hdon.getTongCong();
        }
        System.out.println("Có " + kq.length + " hóa đơn chưa thanh toán, tổng " + DinhDang.tien(tong));
    }

    public HoaDon[] dsChuaThanhToan() {
        HoaDon[] kq = new HoaDon[0];
        for (int i = 0; i < n; i++) {
            if (!ds[i].daThanhToan()) {
                kq = Arrays.copyOf(kq, kq.length + 1);
                kq[kq.length - 1] = ds[i];
            }
        }
        return kq;
    }

    public void locTheoKhoangNgay() {
        LocalDate tu = NhapLieu.nhapNgay("Từ ngày");
        LocalDate den = NhapLieu.nhapNgay("Đến ngày");
        if (den.isBefore(tu)) {
            System.out.println("Ngày kết thúc phải sau ngày bắt đầu.");
            return;
        }
        HoaDon[] kq = locTheoKhoangNgay(tu, den);
        xuat(kq);
        System.out.println("Có " + kq.length + " hóa đơn");
    }

    // tu <= ngayLapHD <= den
    public HoaDon[] locTheoKhoangNgay(LocalDate tu, LocalDate den) {
        HoaDon[] kq = new HoaDon[0];
        for (int i = 0; i < n; i++) {
            LocalDate d = ds[i].getNgayLapHD();
            if (!d.isBefore(tu) && !d.isAfter(den)) {
                kq = Arrays.copyOf(kq, kq.length + 1);
                kq[kq.length - 1] = ds[i];
            }
        }
        return kq;
    }

    // ================= CỬA GHÉP VỚI MODULE PHÒNG, THUÊ PHÒNG =================
    // 3 hàm dưới là chỗ DUY NHẤT của DSHoaDon cần danh sách của module 1, 2.
    // KHI GHÉP: trong mỗi hàm xóa các dòng tạm, bỏ comment khối /* ... */ (và import model.Phong)

    // tìm hợp đồng theo mã; không thấy thì báo và trả null
    private HDThuePhong timHopDong(String maHDong) {
        System.out.println("Cần module Thuê phòng (DSHDThuePhong), sẽ bật khi ghép code.");
        return null;
        /*
        HDThuePhong hd = DuLieu.dsHDThuePhong.timTheoMa(maHDong);
        if (hd == null) {
            System.out.println("Không tìm thấy hợp đồng có mã: " + maHDong);
        }
        return hd;
        */
    }

    // như trên nhưng không in gì (dùng khi in hóa đơn để lấy tiền cọc)
    private HDThuePhong timHopDongKhongBao(String maHDong) {
        return null;
        // return DuLieu.dsHDThuePhong.timTheoMa(maHDong);
    }

    // các phòng (CTHopDong) của hợp đồng
    private CTHopDong[] layPhongCuaHopDong(String maHDong) {
        return new CTHopDong[0];
        // return DuLieu.dsCTHopDong.layTheoHopDong(maHDong);
    }

    // trả phòng: mọi phòng của hợp đồng về TRONG (bước 3 mục 6.3)
    private void traPhong(CTHopDong[] cacPhong) {
        /*
        for (CTHopDong ct : cacPhong) {
            DuLieu.dsPhong.capNhatTinhTrang(ct.getMaP(), Phong.TRONG);
        }
        */
    }

    // ================= ĐỌC / GHI FILE =================

    // hoadon.txt: maHDon|maHDong|maNV|ngayLapHD|tongTienThue|tongTienDV|tongCong|trangThai
    public void docFile() {
        ds = new HoaDon[0];
        n = 0;
        for (String[] t : TepTin.doc(FILE)) {
            try {
                if (!t[0].matches("HDON\\d+")
                        || !(t[7].equals(HoaDon.CHUA_TT) || t[7].equals(HoaDon.DA_TT))) {
                    throw new IllegalArgumentException();
                }
                them(new HoaDon(t[0], t[1], t[2], DinhDang.docNgay(t[3]), Double.parseDouble(t[4]),
                        Double.parseDouble(t[5]), Double.parseDouble(t[6]), t[7]));
            } catch (RuntimeException e) {
                System.out.println("Bỏ qua dòng lỗi trong " + FILE + ": " + String.join("|", t));
            }
        }
    }

    public void ghiFile() {
        String[] cacDong = new String[n];
        for (int i = 0; i < n; i++) {
            cacDong[i] = ds[i].toLine();
        }
        TepTin.ghi(FILE, cacDong);
    }
}
