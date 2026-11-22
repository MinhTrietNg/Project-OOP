package danhsach;

import app.DuLieu;
import core.DinhDang;
import core.NhapLieu;
import core.TepTin;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;
import model.CTSuDungDV;
import model.DichVu;
import model.HDThuePhong;
import model.HoaDon;

// danh sách chi tiết: không có mã riêng, không sửa, không xóa (mục 6.5)
public class DSCTSuDungDV {

    // Scanner riêng của lớp này; không close() vì sẽ đóng luôn System.in
    private static final Scanner sc = new Scanner(System.in, "UTF-8");

    private static final String FILE = "data/ctsudungdv.txt";
    private CTSuDungDV[] ds = new CTSuDungDV[0];
    private int n = 0;

    public CTSuDungDV[] getDs() {
        return ds;
    }

    public int getN() {
        return n;
    }

    // ================= CÁC HÀM BẮT BUỘC (gói 50%) =================

    public void them(CTSuDungDV ct) {
        ds = Arrays.copyOf(ds, n + 1);
        ds[n] = new CTSuDungDV(ct);
        n++;
    }

    // lấy các lần dùng dịch vụ của 1 hợp đồng (mục 6.6)
    public CTSuDungDV[] layTheoHopDong(String maHDong) {
        CTSuDungDV[] kq = new CTSuDungDV[0];
        for (int i = 0; i < n; i++) {
            if (ds[i].getMaHDong().equalsIgnoreCase(maHDong)) {
                kq = Arrays.copyOf(kq, kq.length + 1);
                kq[kq.length - 1] = ds[i];
            }
        }
        return kq;
    }

    public void xuat() {
        if (n == 0) {
            System.out.println("Danh sách trống.");
            return;
        }
        for (int i = 0; i < n; i++) {
            ds[i].xuat();
        }
    }

    // ================= GHI DỊCH VỤ (nghiệp vụ mục 6.3) =================
    //   B1. ghiDichVu()   : nhập mã hợp đồng, kiểm tra chưa có hóa đơn, chọn dịch vụ
    //   B2. ct.nhap(...)  : nhập ngày dùng (trong thời gian thuê) và số lượng
    //   B3. ghiDichVu(hd, dv, ngaySD, sl): kiểm tra lại, TRỪ KHO, rồi mới lưu chi tiết
    // Không đủ hàng thì không trừ kho và không ghi gì.
    // Menu gọi xong thì ghi 2 file: chi tiết sử dụng dịch vụ, hàng hóa.

    public void ghiDichVu() {
        if (DuLieu.dsDichVu.getN() == 0) {
            System.out.println("Chưa có dịch vụ nào, hãy thêm dịch vụ trước.");
            return;
        }

        // ----- B1 -----
        String maHDong = NhapLieu.nhapChuoi(sc, "Nhập mã hợp đồng");
        HDThuePhong hd = DuLieu.dsHDThuePhong.timTheoMa(maHDong);
        if (hd == null) {
            System.out.println("Không tìm thấy hợp đồng có mã: " + maHDong);
            return;
        }
        if (DuLieu.dsHoaDon.timTheoHopDong(hd.getMaHDong()) != null) {
            System.out.println("Hợp đồng đã lập hóa đơn, không ghi thêm dịch vụ được.");
            return;
        }
        DuLieu.dsDichVu.xuat();
        DichVu dv = DuLieu.dsDichVu.chon();
        if (dv == null) {
            System.out.println("Đã hủy ghi dịch vụ.");
            return;
        }

        // ----- B2 -----
        CTSuDungDV tam = new CTSuDungDV();
        tam.nhap(hd.getNgayNhanPhong(), hd.getNgayTraPhong());

        // ----- B3 -----
        if (ghiDichVu(hd, dv, tam.getNgaySD(), tam.getSl())) {
            System.out.println("Đã ghi " + dv.getTenDV() + " cho hợp đồng " + hd.getMaHDong() + ":");
            ds[n - 1].xuat();
        }
    }

    // bản có tham số: module khác / kiểm thử gọi được, trả false (kèm thông báo) nếu không ghi được
    public boolean ghiDichVu(HDThuePhong hd, DichVu dv, LocalDate ngaySD, int sl) {
        if (DuLieu.dsHoaDon.timTheoHopDong(hd.getMaHDong()) != null) {
            System.out.println("Hợp đồng đã lập hóa đơn, không ghi thêm dịch vụ được.");
            return false;
        }
        if (!CTSuDungDV.trongKhoang(ngaySD, hd.getNgayNhanPhong(), hd.getNgayTraPhong())) {
            System.out.println("Ngày sử dụng nằm ngoài thời gian thuê.");
            return false;
        }
        // trừ kho TRƯỚC khi lưu: không đủ hàng thì truKho trả false và kho giữ nguyên
        if (dv.coDungHang() && !DuLieu.dsHangHoa.truKho(dv.getMaH(), sl)) {
            System.out.println("Kho không đủ hàng " + dv.getMaH() + " cho dịch vụ này, không ghi được.");
            return false;
        }
        CTSuDungDV ct = new CTSuDungDV(hd.getMaHDong(), dv.getMaDV(), ngaySD, sl, dv.getDonGia(), 0);
        ct.tinhThanhTien();
        them(ct);
        return true;
    }

    // ================= HÀM NÂNG CAO (gói 70%) =================

    public void xuatTheoHopDong() {
        String maHDong = NhapLieu.nhapChuoi(sc, "Nhập mã hợp đồng");
        xuatTheoHopDong(maHDong);
    }

    // in từng lần dùng (kèm tên dịch vụ) và tổng tiền dịch vụ của hợp đồng
    public void xuatTheoHopDong(String maHDong) {
        CTSuDungDV[] cacDV = layTheoHopDong(maHDong);
        if (cacDV.length == 0) {
            System.out.println("Hợp đồng " + maHDong + " chưa dùng dịch vụ nào.");
            return;
        }
        double tong = 0;
        for (int i = 0; i < cacDV.length; i++) {
            CTSuDungDV ct = cacDV[i];
            DichVu dv = DuLieu.dsDichVu.timTheoMa(ct.getMaDV());
            System.out.println((i + 1) + ". " + DinhDang.ngay(ct.getNgaySD()) + " | " + dv.getTenDV() + ": "
                    + ct.getSl() + " x " + DinhDang.tien(ct.getDonGia()) + " = " + DinhDang.tien(ct.getThanhTien()));
            tong += ct.getThanhTien();
        }
        System.out.println("Tổng tiền dịch vụ: " + DinhDang.tien(tong));
    }

    // tổng số lượng đã dùng của 1 dịch vụ
    public int tongSoLuong(String maDV) {
        int tong = 0;
        for (int i = 0; i < n; i++) {
            if (ds[i].getMaDV().equalsIgnoreCase(maDV)) {
                tong += ds[i].getSl();
            }
        }
        return tong;
    }

    // mỗi dịch vụ: số lượt ghi, tổng số lượng, tổng tiền (vòng ngoài duyệt dịch vụ, vòng trong cộng dồn)
    // chỉ tính hợp đồng đã có hóa đơn DA_TT, giống các thống kê doanh thu khác
    public void thongKeTheoDichVu() {
        for (DichVu dv : DuLieu.dsDichVu.getDs()) {
            int soLuot = 0;
            int tongSL = 0;
            double tong = 0;
            for (int i = 0; i < n; i++) {
                HoaDon hd = DuLieu.dsHoaDon.timTheoHopDong(ds[i].getMaHDong());
                if (ds[i].getMaDV().equalsIgnoreCase(dv.getMaDV()) && hd != null && hd.daThanhToan()) {
                    soLuot++;
                    tongSL += ds[i].getSl();
                    tong += ds[i].getThanhTien();
                }
            }
            System.out.println(dv.getTenDV() + ": " + soLuot + " lượt, " + tongSL
                    + " đơn vị, " + DinhDang.tien(tong));
        }
    }

    // ================= ĐỌC / GHI FILE =================

    // ctsudungdv.txt: maHDong|maDV|ngaySD|sl|donGia|thanhTien
    public void docFile() {
        ds = new CTSuDungDV[0];
        n = 0;
        for (String[] t : TepTin.doc(FILE)) {
            try {
                them(new CTSuDungDV(t[0], t[1], DinhDang.docNgay(t[2]), Integer.parseInt(t[3]),
                        Double.parseDouble(t[4]), Double.parseDouble(t[5])));
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
