package danhsach;

import app.DuLieu;
import core.DinhDang;
import core.NhapLieu;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import model.CTPhieuNhap;
import model.HangHoa;
import model.NhaCC;
import model.NhanVien;
import model.PhieuNhap;

// phiếu nhập là chứng từ: chỉ lập mới và xem, không sửa, không xóa (mục 6.5)
public class DSPhieuNhap {

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

    // Các bước:
    //   B1. chọn nhà cung cấp, nhân viên lập; ngày nhập = hôm nay
    //   B2. nhập từng mặt hàng vào 1 MẢNG TẠM (chưa đụng tới kho)
    //   B3. xong hết mới lưu: cộng kho, cập nhật giá nhập, thêm chi tiết, tính tổng tiền
    // Nhờ mảng tạm nên người dùng hủy giữa chừng thì kho không bị cộng sai.
    // Sau khi gọi hàm này, menu ghi file: dsPhieuNhap, dsCTPhieuNhap, dsHangHoa.
    public void them() {
        if (DuLieu.dsNhaCC.getN() == 0 || DuLieu.dsNhanVien.getN() == 0
                || DuLieu.dsHangHoa.getN() == 0) {
            System.out.println("Cần có ít nhất 1 nhà cung cấp, 1 nhân viên và 1 hàng hóa trước khi nhập hàng.");
            return;
        }

        // ----- B1 -----
        DuLieu.dsNhaCC.xuat();
        NhaCC ncc = DuLieu.dsNhaCC.chon();
        if (ncc == null) {
            System.out.println("Đã hủy lập phiếu.");
            return;
        }
        DuLieu.dsNhanVien.xuat();
        NhanVien nv = DuLieu.dsNhanVien.chon();
        if (nv == null) {
            System.out.println("Đã hủy lập phiếu.");
            return;
        }
        PhieuNhap pn = new PhieuNhap();
        pn.nhap();                       // ngày nhập = hôm nay
        pn.setMaPN(sinhMa());
        pn.setMaNCC(ncc.getMaNCC());
        pn.setMaNV(nv.getMaNV());

        // ----- B2 -----
        CTPhieuNhap[] cacDong = new CTPhieuNhap[0];
        DuLieu.dsHangHoa.xuat();
        while (true) {
            HangHoa hh = DuLieu.dsHangHoa.chon();
            if (hh == null) {
                break;                   // gõ 0: thôi chọn hàng
            }
            if (daCoTrongPhieu(cacDong, hh.getMaH())) {
                System.out.println("  Hàng " + hh.getMaH() + " đã có trong phiếu, chọn hàng khác.");
                continue;
            }
            System.out.println("  " + hh.getTenH() + " | tồn: " + hh.getSlTon()
                    + " | giá nhập lần trước: " + DinhDang.tien(hh.getDonGia()));
            CTPhieuNhap ct = new CTPhieuNhap();
            ct.nhap();                   // sl, đơn giá nhập, tự tính thành tiền
            ct.setMaPN(pn.getMaPN());
            ct.setMaH(hh.getMaH());
            cacDong = Arrays.copyOf(cacDong, cacDong.length + 1);
            cacDong[cacDong.length - 1] = ct;

            if (!NhapLieu.nhapCoKhong("Nhập thêm mặt hàng khác")) {
                break;
            }
        }
        if (cacDong.length == 0) {
            System.out.println("Phiếu phải có ít nhất 1 mặt hàng. Đã hủy lập phiếu.");
            return;
        }

        // ----- B3 -----
        for (CTPhieuNhap ct : cacDong) {
            DuLieu.dsHangHoa.congKho(ct.getMaH(), ct.getSl());
            DuLieu.dsHangHoa.timTheoMa(ct.getMaH()).setDonGia(ct.getDonGiaNhap());
            DuLieu.dsCTPhieuNhap.them(ct);
        }
        pn.tinhTongTien(cacDong);
        them(pn);

        System.out.println("Đã lập phiếu nhập " + pn.getMaPN() + ":");
        xuatChiTiet(pn.getMaPN());
    }

    public void them(PhieuNhap pn) {
        ds = Arrays.copyOf(ds, n + 1);
        ds[n++] = new PhieuNhap(pn);
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

    // ================= CÁC HÀM BẮT BUỘC KHÁC (gói 50%) =================

    // tìm theo mã thì in luôn chi tiết phiếu cho tiện
    public void timTheoMa() {
        String ma = NhapLieu.nhapChuoi("Nhập mã phiếu nhập");
        xuatChiTiet(ma);
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
        PhieuNhap.inTieuDe();
        for (PhieuNhap pn : mang) {
            pn.xuat();
        }
    }

    // in 1 phiếu đầy đủ: đầu phiếu (có tên NCC, tên NV) + bảng các mặt hàng
    public void xuatChiTiet(String maPN) {
        PhieuNhap pn = timTheoMa(maPN);
        if (pn == null) {
            System.out.println("Không tìm thấy phiếu nhập có mã: " + maPN);
            return;
        }
        NhaCC ncc = DuLieu.dsNhaCC.timTheoMa(pn.getMaNCC());
        NhanVien nv = DuLieu.dsNhanVien.timTheoMa(pn.getMaNV());
        String tenNCC = (ncc == null) ? "(không rõ)" : ncc.getTenNCC();
        String tenNV = (nv == null) ? "(không rõ)" : nv.getHoTen();

        System.out.println("----------- PHIẾU NHẬP " + pn.getMaPN() + " -----------");
        System.out.println("Ngày nhập   : " + DinhDang.ngay(pn.getNgayNhap()));
        System.out.println("Nhà cung cấp: " + pn.getMaNCC() + " - " + tenNCC);
        System.out.println("Nhân viên   : " + pn.getMaNV() + " - " + tenNV);
        System.out.printf("%-4s %-6s %-22s %6s %15s %17s%n",
                "STT", "Mã H", "Tên hàng", "SL", "Đơn giá nhập", "Thành tiền");
        CTPhieuNhap[] cacDong = DuLieu.dsCTPhieuNhap.layTheoPhieu(pn.getMaPN());
        for (int i = 0; i < cacDong.length; i++) {
            CTPhieuNhap ct = cacDong[i];
            HangHoa hh = DuLieu.dsHangHoa.timTheoMa(ct.getMaH());
            String tenH = (hh == null) ? "(không rõ)" : hh.getTenH();
            System.out.printf("%-4d %-6s %-22s %6d %15s %17s%n", i + 1, ct.getMaH(), tenH,
                    ct.getSl(), DinhDang.tien(ct.getDonGiaNhap()), DinhDang.tien(ct.getThanhTien()));
        }
        System.out.println("TỔNG TIỀN   : " + DinhDang.tien(pn.getTongTien()));
    }

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
        LocalDate tu = NhapLieu.nhapNgay("Từ ngày");
        LocalDate den = NhapLieu.nhapNgay("Đến ngày");
        if (den.isBefore(tu)) {
            System.out.println("Ngày kết thúc phải sau ngày bắt đầu.");
            return;
        }
        PhieuNhap[] kq = locTheoKhoangNgay(tu, den);
        xuat(kq);
        double tong = 0;
        for (PhieuNhap pn : kq) {
            tong += pn.getTongTien();
        }
        System.out.println("Số phiếu: " + kq.length + " | Tổng tiền nhập: " + DinhDang.tien(tong));
    }

    // tu <= ngayNhap <= den  (viết là: không trước tu và không sau den)
    public PhieuNhap[] locTheoKhoangNgay(LocalDate tu, LocalDate den) {
        PhieuNhap[] kq = new PhieuNhap[0];
        int dem = 0;
        for (int i = 0; i < n; i++) {
            LocalDate d = ds[i].getNgayNhap();
            if (!d.isBefore(tu) && !d.isAfter(den)) {
                kq = Arrays.copyOf(kq, dem + 1);
                kq[dem++] = ds[i];
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

    // chi phí nhập hàng 12 tháng của 1 năm
    public void thongKeTheoThang() {
        int nam = NhapLieu.nhapSoNguyen("Năm", 2000, 2100);
        System.out.printf("%-6s %18s%n", "Tháng", "Tiền nhập hàng");
        double tongNam = 0;
        for (int thang = 1; thang <= 12; thang++) {
            double tien = tongTienNhap(thang, nam);
            System.out.printf("%-6d %18s%n", thang, DinhDang.tien(tien));
            tongNam += tien;
        }
        System.out.println("Cả năm " + nam + ": " + DinhDang.tien(tongNam));
    }

    // mỗi nhà cung cấp: số phiếu, tổng tiền; cuối cùng chỉ ra nhà cung cấp nhập nhiều tiền nhất
    public void thongKeTheoNhaCC() {
        NhaCC[] dsNCC = DuLieu.dsNhaCC.getDs();
        if (dsNCC.length == 0) {
            System.out.println("Chưa có nhà cung cấp.");
            return;
        }
        System.out.printf("%-7s %-32s %8s %18s%n", "Mã NCC", "Tên nhà cung cấp", "Số phiếu", "Tổng tiền");
        NhaCC nhieuNhat = null;
        double tienNhieuNhat = 0;
        for (NhaCC ncc : dsNCC) {
            int soPhieu = 0;
            double tong = 0;
            for (int i = 0; i < n; i++) {
                if (ds[i].getMaNCC().equalsIgnoreCase(ncc.getMaNCC())) {
                    soPhieu++;
                    tong += ds[i].getTongTien();
                }
            }
            System.out.printf("%-7s %-32s %8d %18s%n",
                    ncc.getMaNCC(), ncc.getTenNCC(), soPhieu, DinhDang.tien(tong));
            if (tong > tienNhieuNhat) {
                tienNhieuNhat = tong;
                nhieuNhat = ncc;
            }
        }
        if (nhieuNhat != null) {
            System.out.println("Nhập nhiều nhất từ: " + nhieuNhat.getTenNCC()
                    + " (" + DinhDang.tien(tienNhieuNhat) + ")");
        }
    }

    // mỗi nhân viên đã lập bao nhiêu phiếu nhập, tổng tiền bao nhiêu
    public void thongKeTheoNhanVien() {
        NhanVien[] dsNV = DuLieu.dsNhanVien.getDs();
        if (dsNV.length == 0) {
            System.out.println("Chưa có nhân viên.");
            return;
        }
        System.out.printf("%-6s %-24s %8s %18s%n", "Mã NV", "Họ tên", "Số phiếu", "Tổng tiền");
        for (NhanVien nv : dsNV) {
            int soPhieu = 0;
            double tong = 0;
            for (int i = 0; i < n; i++) {
                if (ds[i].getMaNV().equalsIgnoreCase(nv.getMaNV())) {
                    soPhieu++;
                    tong += ds[i].getTongTien();
                }
            }
            System.out.printf("%-6s %-24s %8d %18s%n",
                    nv.getMaNV(), nv.getHoTen(), soPhieu, DinhDang.tien(tong));
        }
    }

    // ================= ĐỌC / GHI FILE =================

    public void docFile() {
        ds = new PhieuNhap[0];
        n = 0;
        File f = new File(FILE);
        if (!f.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                try {
                    String[] t = DinhDang.tach(line);
                    // mã sai dạng sẽ làm sinhMa() lỗi, nên loại ngay từ lúc đọc
                    if (!t[0].matches("PN\\d+")) {
                        throw new IllegalArgumentException();
                    }
                    them(new PhieuNhap(t[0], t[1], t[2], DinhDang.docNgay(t[3]),
                            Double.parseDouble(t[4])));
                } catch (RuntimeException e) {
                    System.out.println("Bỏ qua dòng lỗi trong " + FILE + ": " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Lỗi đọc file " + FILE);
        }
    }

    public void ghiFile() {
        new File(FILE).getParentFile().mkdirs();
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(FILE), StandardCharsets.UTF_8))) {
            for (int i = 0; i < n; i++) {
                bw.write(ds[i].toLine());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Lỗi ghi file " + FILE);
        }
    }
}
