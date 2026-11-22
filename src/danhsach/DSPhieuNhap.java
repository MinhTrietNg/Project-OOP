package danhsach;

import app.DuLieu;
import core.DinhDang;
import core.NhapLieu;
import core.TepTin;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;
import model.CTPhieuNhap;
import model.HangHoa;
import model.NhaCC;
import model.NhanVien;
import model.PhieuNhap;

// phiếu nhập là chứng từ: chỉ lập mới và xem, không sửa, không xóa (mục 6.5)
public class DSPhieuNhap {

    // Scanner riêng của lớp này; không close() vì sẽ đóng luôn System.in
    private static final Scanner sc = new Scanner(System.in, "UTF-8");

    private static final String FILE = "data/phieunhap.txt";
    private PhieuNhap[] ds = new PhieuNhap[0];
    private int n = 0;

    public PhieuNhap[] getDs() {
        return ds;
    }

    public int getN() {
        return n;
    }

    // ================= LẬP PHIẾU NHẬP (nghiệp vụ mục 6.4) =================
    //   B1. them()        : chọn nhà cung cấp, nhân viên; tạo đầu phiếu
    //   B2. nhapCacDong() : nhập từng mặt hàng vào MẢNG TẠM, chưa đụng tới kho
    //   B3. lapPhieu(...) : cộng kho, cập nhật giá nhập, lưu chi tiết và phiếu
    // Hủy ở B1, B2 hoặc trả lời "k" khi hỏi lưu thì chưa có gì bị lưu, nên kho không bị cộng sai.
    // Menu gọi xong thì ghi 3 file: phiếu nhập, chi tiết phiếu nhập, hàng hóa.

    public void them() {
        if (DuLieu.dsNhaCC.getN() == 0 || DuLieu.dsNhanVien.getN() == 0
                || DuLieu.dsHangHoa.getN() == 0) {
            System.out.println("Cần có nhà cung cấp, nhân viên và hàng hóa trước khi nhập hàng.");
            return;
        }

        // ----- B1 -----
        DuLieu.dsNhaCC.xuat();
        NhaCC ncc = DuLieu.dsNhaCC.chon();
        if (ncc == null) {
            System.out.println("Đã hủy lập phiếu nhập.");
            return;
        }
        DuLieu.dsNhanVien.xuat();
        NhanVien nv = DuLieu.dsNhanVien.chon();
        if (nv == null) {
            System.out.println("Đã hủy lập phiếu nhập.");
            return;
        }
        PhieuNhap pn = new PhieuNhap();
        pn.nhap();                       // ngày nhập = hôm nay
        pn.setMaPN(sinhMa());
        pn.setMaNCC(ncc.getMaNCC());
        pn.setMaNV(nv.getMaNV());

        // ----- B2 -----
        CTPhieuNhap[] cacDong = nhapCacDong(pn.getMaPN());
        if (cacDong.length == 0) {
            System.out.println("Phiếu phải có ít nhất 1 mặt hàng. Đã hủy lập phiếu nhập.");
            return;
        }
        if (!NhapLieu.nhapCoKhong(sc, "Lưu phiếu nhập")) {
            System.out.println("Đã hủy lập phiếu nhập.");
            return;
        }

        // ----- B3 -----
        lapPhieu(pn, cacDong);
        System.out.println("Đã lập phiếu nhập " + pn.getMaPN() + ":");
        xuatChiTiet(pn.getMaPN());
    }

    // B2: hỏi từng mặt hàng tới khi người dùng gõ 0 hoặc trả lời "k"
    private CTPhieuNhap[] nhapCacDong(String maPN) {
        CTPhieuNhap[] cacDong = new CTPhieuNhap[0];
        DuLieu.dsHangHoa.xuat();
        System.out.println("Chọn từng mặt hàng, gõ 0 để kết thúc.");
        while (true) {
            HangHoa hh = DuLieu.dsHangHoa.chon();
            if (hh == null) {
                return cacDong;
            }
            if (daCoTrongPhieu(cacDong, hh.getMaH())) {
                System.out.println("  Hàng " + hh.getMaH() + " đã có trong phiếu, chọn hàng khác.");
                continue;
            }
            System.out.println("  " + hh.getTenH() + " - giá nhập lần trước: " + DinhDang.tien(hh.getDonGia()));
            CTPhieuNhap ct = new CTPhieuNhap();
            ct.nhap();                   // sl, đơn giá nhập, tự tính thành tiền
            ct.setMaPN(maPN);
            ct.setMaH(hh.getMaH());
            cacDong = Arrays.copyOf(cacDong, cacDong.length + 1);
            cacDong[cacDong.length - 1] = ct;

            if (!NhapLieu.nhapCoKhong(sc, "Nhập thêm mặt hàng khác")) {
                return cacDong;
            }
        }
    }

    // 1 phiếu không được có 2 dòng cùng 1 mặt hàng
    private boolean daCoTrongPhieu(CTPhieuNhap[] cacDong, String maH) {
        for (CTPhieuNhap ct : cacDong) {
            if (ct.getMaH().equalsIgnoreCase(maH)) {
                return true;
            }
        }
        return false;
    }

    // B3 (có tham số, module khác / kiểm thử gọi được):
    // tới đây dữ liệu đã đầy đủ, mới thật sự thay đổi kho
    public void lapPhieu(PhieuNhap pn, CTPhieuNhap[] cacDong) {
        for (CTPhieuNhap ct : cacDong) {
            DuLieu.dsHangHoa.congKho(ct.getMaH(), ct.getSl());
            HangHoa hh = DuLieu.dsHangHoa.timTheoMa(ct.getMaH());
            hh.setDonGia(ct.getDonGiaNhap());
            DuLieu.dsCTPhieuNhap.them(ct);
        }
        pn.tinhTongTien(cacDong);
        them(pn);
    }

    public void them(PhieuNhap pn) {
        ds = Arrays.copyOf(ds, n + 1);
        ds[n] = new PhieuNhap(pn);
        n++;
    }

    // ================= CÁC HÀM BẮT BUỘC KHÁC (gói 50%) =================

    // tìm thấy thì in luôn chi tiết phiếu
    public void timTheoMa() {
        String ma = NhapLieu.nhapChuoi(sc, "Nhập mã phiếu nhập cần tìm");
        PhieuNhap pn = timTheoMa(ma);
        if (pn == null) {
            System.out.println("Không tìm thấy phiếu nhập có mã: " + ma);
        } else {
            xuatChiTiet(ma);
        }
    }

    public PhieuNhap timTheoMa(String ma) {
        for (int i = 0; i < n; i++) {
            if (ds[i].getMaPN().equalsIgnoreCase(ma)) {
                return ds[i];
            }
        }
        return null;
    }

    public void xuat() {
        xuat(ds);
    }

    public void xuat(PhieuNhap[] mang) {
        if (mang.length == 0) {
            System.out.println("Danh sách trống.");
            return;
        }
        for (PhieuNhap pn : mang) {
            pn.xuat();
        }
    }

    // in 1 phiếu đầy đủ: đầu phiếu (kèm tên NCC, tên NV) + từng mặt hàng
    // NCC, NV, hàng đã nằm trong phiếu thì không xóa được (mục 6.5) nên luôn tìm thấy
    public void xuatChiTiet(String maPN) {
        PhieuNhap pn = timTheoMa(maPN);
        if (pn == null) {
            System.out.println("Không tìm thấy phiếu nhập có mã: " + maPN);
            return;
        }
        NhaCC ncc = DuLieu.dsNhaCC.timTheoMa(pn.getMaNCC());
        NhanVien nv = DuLieu.dsNhanVien.timTheoMa(pn.getMaNV());
        System.out.println("----- PHIẾU NHẬP " + pn.getMaPN() + " -----");
        System.out.println("Ngày nhập   : " + DinhDang.ngay(pn.getNgayNhap()));
        System.out.println("Nhà cung cấp: " + ncc.getTenNCC());
        System.out.println("Nhân viên   : " + nv.getHoTen());

        CTPhieuNhap[] cacDong = DuLieu.dsCTPhieuNhap.layTheoPhieu(pn.getMaPN());
        for (int i = 0; i < cacDong.length; i++) {
            CTPhieuNhap ct = cacDong[i];
            HangHoa hh = DuLieu.dsHangHoa.timTheoMa(ct.getMaH());
            System.out.println((i + 1) + ". " + hh.getTenH() + ": " + ct.getSl() + " x "
                    + DinhDang.tien(ct.getDonGiaNhap()) + " = " + DinhDang.tien(ct.getThanhTien()));
        }
        System.out.println("Tổng tiền   : " + DinhDang.tien(pn.getTongTien()));
    }

    // "PN012".substring(2) = "012" -> 12
    public String sinhMa() {
        int max = 0;
        for (int i = 0; i < n; i++) {
            int so = Integer.parseInt(ds[i].getMaPN().substring(2));
            if (so > max) {
                max = so;
            }
        }
        return String.format("PN%03d", max + 1);
    }

    // ================= HÀM NÂNG CAO (gói 70%) =================

    public void locTheoKhoangNgay() {
        LocalDate tu = NhapLieu.nhapNgay(sc, "Từ ngày");
        LocalDate den = NhapLieu.nhapNgay(sc, "Đến ngày");
        if (den.isBefore(tu)) {
            System.out.println("Ngày kết thúc phải sau ngày bắt đầu.");
            return;
        }
        PhieuNhap[] kq = locTheoKhoangNgay(tu, den);
        xuat(kq);
        System.out.println("Có " + kq.length + " phiếu");
    }

    // tu <= ngayNhap <= den   (tức là: KHÔNG trước tu và KHÔNG sau den)
    public PhieuNhap[] locTheoKhoangNgay(LocalDate tu, LocalDate den) {
        PhieuNhap[] kq = new PhieuNhap[0];
        for (int i = 0; i < n; i++) {
            LocalDate d = ds[i].getNgayNhap();
            if (!d.isBefore(tu) && !d.isAfter(den)) {
                kq = Arrays.copyOf(kq, kq.length + 1);
                kq[kq.length - 1] = ds[i];
            }
        }
        return kq;
    }

    // tổng tiền nhập hàng trong 1 tháng của 1 năm
    public double tongTienNhap(int thang, int nam) {
        double tong = 0;
        for (int i = 0; i < n; i++) {
            LocalDate d = ds[i].getNgayNhap();
            if (d.getMonthValue() == thang && d.getYear() == nam) {
                tong += ds[i].getTongTien();
            }
        }
        return tong;
    }

    // 3 thống kê dưới cùng 1 khuôn: vòng ngoài duyệt nhóm, vòng trong cộng dồn phiếu thuộc nhóm đó

    public void thongKeTheoThang() {
        int nam = NhapLieu.nhapSoNguyen(sc, "Năm", 2000, 2100);
        double tongNam = 0;
        for (int thang = 1; thang <= 12; thang++) {
            double tien = tongTienNhap(thang, nam);
            System.out.println("Tháng " + thang + ": " + DinhDang.tien(tien));
            tongNam += tien;
        }
        System.out.println("Cả năm " + nam + ": " + DinhDang.tien(tongNam));
    }

    public void thongKeTheoNhaCC() {
        for (NhaCC ncc : DuLieu.dsNhaCC.getDs()) {
            int soPhieu = 0;
            double tong = 0;
            for (int i = 0; i < n; i++) {
                if (ds[i].getMaNCC().equalsIgnoreCase(ncc.getMaNCC())) {
                    soPhieu++;
                    tong += ds[i].getTongTien();
                }
            }
            System.out.println(ncc.getTenNCC() + ": " + soPhieu + " phiếu, " + DinhDang.tien(tong));
        }
    }

    public void thongKeTheoNhanVien() {
        for (NhanVien nv : DuLieu.dsNhanVien.getDs()) {
            int soPhieu = 0;
            double tong = 0;
            for (int i = 0; i < n; i++) {
                if (ds[i].getMaNV().equalsIgnoreCase(nv.getMaNV())) {
                    soPhieu++;
                    tong += ds[i].getTongTien();
                }
            }
            System.out.println(nv.getHoTen() + ": " + soPhieu + " phiếu, " + DinhDang.tien(tong));
        }
    }

    // ================= ĐỌC / GHI FILE =================

    // phieunhap.txt: maPN|maNCC|maNV|ngayNhap|tongTien
    public void docFile() {
        ds = new PhieuNhap[0];
        n = 0;
        for (String[] t : TepTin.doc(FILE)) {
            try {
                them(new PhieuNhap(t[0], t[1], t[2], DinhDang.docNgay(t[3]), Double.parseDouble(t[4])));
            } catch (Exception e) {
                System.out.println("Bỏ qua 1 dòng lỗi trong " + FILE);
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
