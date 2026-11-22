package danhsach;

import app.DuLieu;
import core.DinhDang;
import core.NhapLieu;
import core.TepTin;
import core.TimKiem;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;
import model.CTHopDong;
import model.CTKhachO;
import model.CTSuDungDV;
import model.HDThuePhong;
import model.KhachHang;
import model.NhanVien;
import model.Phong;

public class DSHDThuePhong {

    // Scanner riêng của lớp này; không close() vì sẽ đóng luôn System.in
    private static final Scanner sc = new Scanner(System.in, "UTF-8");

    private static final String FILE = "data/hdthuephong.txt";
    private HDThuePhong[] ds = new HDThuePhong[0];
    private int n = 0;

    public HDThuePhong[] getDs() {
        return ds;
    }

    public int getN() {
        return n;
    }

    // ================= LẬP HỢP ĐỒNG (nghiệp vụ mục 6.2) =================
    //   B1. them()     : tìm/thêm khách đứng tên, chọn NV, nhập ngày nhận/trả, tiền cọc
    //   B2. Chọn phòng : chọn >= 1 phòng TRONG, nhập slNguoi, chốt donGia và thanhTien
    //   B3. nhapKhachO : mỗi phòng chọn 1..slNguoi khách ở (chưa có thì thêm mới)
    //   B4. lapHopDong(hd, dsCT, dsKO): lưu hợp đồng, chi tiết HĐ, chi tiết khách ở và đổi phòng -> DA_THUE
    // Gõ 0 ở bất kỳ bước nào thì hủy, các danh sách giữ nguyên.
    // Menu gọi xong thì ghi file: hdthuephong, cthopdong, ctkhacho, khachhang, phong.

    // Thêm (không tham số): lập hợp đồng thuê phòng từ bàn phím
    public void them() {
        // 1. Kiểm tra có nhân viên và còn phòng trống
        if (DuLieu.dsNhanVien.getN() == 0) {
            System.out.println("Cần có nhân viên trước khi lập hợp đồng.");
            return;
        }
        if (DuLieu.dsPhong.dsPhongTrong().length == 0) {
            System.out.println("Hiện không còn phòng trống để thuê.");
            return;
        }

        // 2. Tìm khách đứng tên (theo mã hoặc SĐT), chưa có thì thêm mới
        KhachHang khDaiDien = chonHoacThemKhach("khách đứng tên hợp đồng");
        if (khDaiDien == null) {
            System.out.println("Đã hủy lập hợp đồng.");
            return;
        }

        // 3. Chọn nhân viên lập
        DuLieu.dsNhanVien.xuat();
        NhanVien nv = DuLieu.dsNhanVien.chon();
        if (nv == null) {
            System.out.println("Đã hủy lập hợp đồng.");
            return;
        }

        // 4. Tạo đối tượng hợp đồng & nhập ngày nhận, ngày trả, tiền cọc
        HDThuePhong hd = new HDThuePhong();
        hd.setMaHDong(sinhMa());
        hd.setMaKH(khDaiDien.getMaKH());
        hd.setMaNV(nv.getMaNV());
        hd.setNgayLapHD(LocalDate.now());
        hd.nhap();

        long soNgay = hd.soNgayThue();
        CTHopDong[] dsCT = new CTHopDong[0];
        CTKhachO[] dsKO = new CTKhachO[0];

        // 5. Chọn từ 1 phòng trở lên trong danh sách phòng TRONG
        while (true) {
            System.out.println("--- Danh sách phòng TRONG ---");
            DuLieu.dsPhong.xuat(DuLieu.dsPhong.dsPhongTrong());

            String maP = NhapLieu.nhapChuoi(sc, "Nhập mã phòng muốn thuê (0 để kết thúc chọn phòng)");
            if (maP.equals("0")) {
                if (dsCT.length == 0) {
                    System.out.println("Đã hủy lập hợp đồng.");
                    return;
                }
                break;
            }

            Phong p = DuLieu.dsPhong.timTheoMa(maP);
            if (p == null || !p.getTinhTrang().equals(Phong.TRONG)) {
                System.out.println("  Phòng không tồn tại hoặc không ở trạng thái TRONG.");
                continue;
            }

            // Kiểm tra phòng này đã được chọn trong chính hợp đồng đang lập chưa
            if (daChonPhong(dsCT, p.getMaP())) {
                System.out.println("  Phòng " + p.getMaP() + " đã có trong hợp đồng này rồi.");
                continue;
            }

            // Nhập chi tiết thuê cho phòng p
            CTHopDong ct = taoCTHopDong(hd, p, soNgay);
            dsCT = Arrays.copyOf(dsCT, dsCT.length + 1);
            dsCT[dsCT.length - 1] = ct;

            // 6. Ghi khách ở (CTKhachO) cho phòng này: từ 1 tới slNguoi khách
            dsKO = nhapKhachO(hd.getMaHDong(), p.getMaP(), ct.getSlNguoi(), dsKO);
            if (dsKO == null) {
                System.out.println("Đã hủy lập hợp đồng.");
                return;
            }
        }

        // 7. Chốt dữ liệu vào các danh sách
        lapHopDong(hd, dsCT, dsKO);
        System.out.println("Đã lập hợp đồng " + hd.getMaHDong() + " thành công!");
    }

    // Nhập 1..slNguoi khách ở cho 1 phòng, nối vào cuối mảng dsKO rồi trả mảng mới.
    // Gõ 0 thì trả null (hủy cả hợp đồng). Khách trùng sẽ bị DSCTKhachO.them() chặn lúc chốt.
    private CTKhachO[] nhapKhachO(String maHDong, String maP, int slNguoi, CTKhachO[] dsKO) {
        int dem = 0;
        while (dem < slNguoi) {
            if (dem > 0 && !NhapLieu.nhapCoKhong(sc, "Thêm khách ở phòng " + maP)) {
                break;
            }
            KhachHang kh = chonHoacThemKhach("khách ở thứ " + (dem + 1) + " phòng " + maP);
            if (kh == null) {
                return null;
            }
            if (daCoKhach(dsKO, maP, kh.getMaKH())) {
                System.out.println("  Khách " + kh.getMaKH() + " đã có trong phòng " + maP + ", chọn khách khác.");
                continue;
            }
            dsKO = Arrays.copyOf(dsKO, dsKO.length + 1);
            dsKO[dsKO.length - 1] = new CTKhachO(maHDong, maP, kh.getMaKH());
            dem++;
        }
        return dsKO;
    }

    // Hàm có tham số: lưu hợp đồng, các chi tiết phòng, khách ở và đổi tình trạng phòng sang DA_THUE
    public void lapHopDong(HDThuePhong hd, CTHopDong[] dsCT, CTKhachO[] dsKO) {
        them(hd);
        for (CTHopDong ct : dsCT) {
            DuLieu.dsCTHopDong.them(ct);
            DuLieu.dsPhong.capNhatTinhTrang(ct.getMaP(), Phong.DA_THUE);
        }
        for (CTKhachO ko : dsKO) {
            DuLieu.dsCTKhachO.them(ko);
        }
    }

    // Hàm phụ trợ: phòng maP đã có trong mảng chi tiết đang lập chưa
    private boolean daChonPhong(CTHopDong[] dsCT, String maP) {
        for (CTHopDong ct : dsCT) {
            if (ct.getMaP().equalsIgnoreCase(maP)) {
                return true;
            }
        }
        return false;
    }

    // Hàm phụ trợ: khách maKH đã được ghi ở phòng maP trong mảng đang lập chưa
    private boolean daCoKhach(CTKhachO[] dsKO, String maP, String maKH) {
        for (CTKhachO ko : dsKO) {
            if (ko.getMaP().equalsIgnoreCase(maP) && ko.getMaKH().equalsIgnoreCase(maKH)) {
                return true;
            }
        }
        return false;
    }

    // Hàm phụ trợ: tạo chi tiết thuê cho phòng p (giá chép từ phòng, nhập số người, tính thành tiền)
    private CTHopDong taoCTHopDong(HDThuePhong hd, Phong p, long soNgay) {
        CTHopDong ct = new CTHopDong();
        ct.setMaHDong(hd.getMaHDong());
        ct.setMaP(p.getMaP());
        ct.setDonGia(p.getDonGia());
        ct.nhap(p.getSoNguoiToiDa());
        ct.tinhThanhTien(soNgay);
        return ct;
    }

    // Hàm phụ trợ: tìm khách theo mã hoặc SĐT, nếu chưa có thì cho thêm mới ngay tại chỗ
    private KhachHang chonHoacThemKhach(String vaiTro) {
        String tuKhoa = NhapLieu.nhapChuoi(sc, "Nhập mã KH hoặc SĐT của " + vaiTro + " (0 để hủy)");
        if (tuKhoa.equals("0")) {
            return null;
        }
        KhachHang kh = DuLieu.dsKhachHang.timTheoMa(tuKhoa);
        if (kh == null) {
            kh = DuLieu.dsKhachHang.timTheoSdt(tuKhoa);
        }
        if (kh != null) {
            System.out.println("  -> Đã chọn khách: " + kh.getMaKH() + " - " + kh.getHoTen());
            return kh;
        }
        System.out.println("  Chưa có khách hàng này, tiến hành thêm khách mới:");
        int truoc = DuLieu.dsKhachHang.getN();
        DuLieu.dsKhachHang.them();
        // them() có thể từ chối (trùng SĐT), khi đó không được lấy nhầm khách cuối mảng
        if (DuLieu.dsKhachHang.getN() == truoc) {
            return null;
        }
        KhachHang[] dsKH = DuLieu.dsKhachHang.getDs();
        return dsKH[dsKH.length - 1];
    }

    // ================= CÁC HÀM BẮT BUỘC (gói 50%) =================

    // Thêm (có tham số): nới mảng ds thêm 1 phần tử và sao chép đối tượng hd vào cuối mảng
    public void them(HDThuePhong hd) {
        ds = Arrays.copyOf(ds, n + 1);
        ds[n] = new HDThuePhong(hd);
        n++;
    }

    // Tìm theo mã (không tham số): nhập mã từ bàn phím rồi in kết quả ra màn hình
    public void timTheoMa() {
        String ma = NhapLieu.nhapChuoi(sc, "Nhập mã hợp đồng cần tìm");
        HDThuePhong hd = timTheoMa(ma);
        if (hd == null) {
            System.out.println("Không tìm thấy hợp đồng có mã: " + ma);
        } else {
            hd.xuat();
        }
    }

    // Tìm theo mã (có tham số): chỉ tìm và trả về đối tượng (không in), không thấy trả về null
    public HDThuePhong timTheoMa(String ma) {
        for (int i = 0; i < n; i++) {
            if (ds[i].getMaHDong().equalsIgnoreCase(ma)) {
                return ds[i];
            }
        }
        return null;
    }

    // Sửa (không tham số): dùng cho menu, hỏi người dùng nhập mã hợp đồng rồi gọi sua(ma)
    public void sua() {
        String ma = NhapLieu.nhapChuoi(sc, "Nhập mã hợp đồng cần sửa ngày trả phòng");
        sua(ma);
    }

    // Sửa (có tham số): xử lý nghiệp vụ sửa hợp đồng theo mã truyền vào (mục 6.2).
    // - Chỉ được sửa ngayTraPhong khi hợp đồng CHƯA lập hóa đơn (nếu đã có hóa đơn thì chặn sửa).
    // - Sau khi đổi ngày trả, tự động tính lại thanhTien = donGia * soNgayThue() cho toàn bộ CTHopDong thuộc hợp đồng này.
    public void sua(String ma) {
        HDThuePhong hd = timTheoMa(ma);
        if (hd == null) {
            System.out.println("Không tìm thấy hợp đồng có mã: " + ma);
            return;
        }
        if (DuLieu.dsHoaDon.timTheoHopDong(hd.getMaHDong()) != null) {
            System.out.println("Hợp đồng đã lập hóa đơn, không thể sửa.");
            return;
        }

        LocalDate ngayTraCu = hd.getNgayTraPhong();
        hd.nhapNgayTra();
        // đã có dịch vụ dùng sau ngày trả mới thì không cho rút ngắn, trả lại ngày cũ
        for (CTSuDungDV sd : DuLieu.dsCTSuDungDV.layTheoHopDong(hd.getMaHDong())) {
            if (sd.getNgaySD().isAfter(hd.getNgayTraPhong())) {
                hd.setNgayTraPhong(ngayTraCu);
                System.out.println("Đã có dịch vụ dùng ngày " + DinhDang.ngay(sd.getNgaySD())
                        + ", ngày trả không được sớm hơn ngày đó. Không sửa.");
                return;
            }
        }

        long soNgay = hd.soNgayThue();
        for (CTHopDong ct : DuLieu.dsCTHopDong.layTheoHopDong(hd.getMaHDong())) {
            ct.tinhThanhTien(soNgay);
        }
        System.out.println("Đã cập nhật ngày trả phòng cho hợp đồng " + hd.getMaHDong());
    }

    // Xóa (không tham số & có tham số):
    // Theo quy định mục 6.5 của đồ án, KHÔNG XÓA các lớp chứng từ (HDThuePhong, HoaDon, PhieuNhap)
    // và các lớp chi tiết để bảo toàn lịch sử giao dịch -> lớp này không cài đặt xoa() và xoa(String ma).

    public void xuat() {
        xuat(ds);
    }

    public void xuat(HDThuePhong[] mang) {
        if (mang.length == 0) {
            System.out.println("Danh sách trống.");
            return;
        }
        for (HDThuePhong hd : mang) {
            hd.xuat();
        }
    }

    // Tự sinh mã mới dựa trên số lớn nhất hiện có + 1 ("HDTP005".substring(4) = "005" -> 5 -> "HDTP006")
    public String sinhMa() {
        int max = 0;
        for (int i = 0; i < n; i++) {
            int so = Integer.parseInt(ds[i].getMaHDong().substring(4));
            if (so > max) {
                max = so;
            }
        }
        return String.format("HDTP%03d", max + 1);
    }

    // ================= HÀM NÂNG CAO & GIAO TIẾP MODULE =================

    // Lọc các hợp đồng chưa lập hóa đơn (dùng cho MenuDichVu, MenuHoaDon và chức năng sửa hợp đồng)
    public HDThuePhong[] dsChuaThanhToan() {
        HDThuePhong[] kq = new HDThuePhong[0];
        for (int i = 0; i < n; i++) {
            if (DuLieu.dsHoaDon.timTheoHopDong(ds[i].getMaHDong()) == null) {
                kq = Arrays.copyOf(kq, kq.length + 1);
                kq[kq.length - 1] = ds[i];
            }
        }
        return kq;
    }

    // Tìm theo mã khách hàng (có tham số)
    public HDThuePhong[] timTheoMaKH(String maKH) {
        HDThuePhong[] kq = new HDThuePhong[0];
        for (int i = 0; i < n; i++) {
            if (ds[i].getMaKH().equalsIgnoreCase(maKH)) {
                kq = Arrays.copyOf(kq, kq.length + 1);
                kq[kq.length - 1] = ds[i];
            }
        }
        return kq;
    }

    // Tìm theo tên khách đứng tên (không tham số)
    public void timTheoTenKH() {
        String tuKhoa = NhapLieu.nhapChuoi(sc, "Nhập tên khách đứng tên (gõ không dấu cũng được)");
        xuat(timTheoTenKH(tuKhoa));
    }

    // Tìm theo tên khách đứng tên (có tham số) - hỗ trợ tìm gần đúng không dấu qua TimKiem.chua
    public HDThuePhong[] timTheoTenKH(String tuKhoa) {
        HDThuePhong[] kq = new HDThuePhong[0];
        for (int i = 0; i < n; i++) {
            KhachHang kh = DuLieu.dsKhachHang.timTheoMa(ds[i].getMaKH());
            if (kh != null && TimKiem.chua(kh.getHoTen(), tuKhoa)) {
                kq = Arrays.copyOf(kq, kq.length + 1);
                kq[kq.length - 1] = ds[i];
            }
        }
        return kq;
    }

    // ================= ĐỌC / GHI FILE =================

    // hdthuephong.txt: maHDong|maKH|maNV|ngayLapHD|tienCoc|ngayNhanPhong|ngayTraPhong
    public void docFile() {
        ds = new HDThuePhong[0];
        n = 0;
        for (String[] t : TepTin.doc(FILE)) {
            try {
                them(new HDThuePhong(
                        t[0],
                        t[1],
                        t[2],
                        DinhDang.docNgay(t[3]),
                        Double.parseDouble(t[4]),
                        DinhDang.docNgay(t[5]),
                        DinhDang.docNgay(t[6])
                ));
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