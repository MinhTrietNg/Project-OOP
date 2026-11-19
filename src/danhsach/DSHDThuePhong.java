package danhsach;

import app.DuLieu;
import core.DinhDang;
import core.NhapLieu;
import core.TepTin;
import core.TimKiem;
import java.time.LocalDate;
import java.util.Arrays;
import model.CTHopDong;
import model.CTKhachO;
import model.HDThuePhong;
import model.KhachHang;
import model.NhanVien;
import model.Phong;

public class DSHDThuePhong {

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
    //   B1. lapHopDong() : tìm/thêm khách đứng tên, chọn NV, nhập ngày nhận/trả, tiền cọc
    //   B2. Chọn phòng   : chọn >= 1 phòng TRONG, nhập slNguoi, chốt donGia và thanhTien
    //   B3. Ghi khách ở  : mỗi phòng chọn 1..slNguoi khách ở (chưa có thì thêm mới)
    //   B4. lapHopDong(hd, dsCT, dsKO): lưu hợp đồng, chi tiết HĐ, chi tiết khách ở và đổi phòng -> DA_THUE
    // Menu gọi xong thì ghi file: hdthuephong, cthopdong, ctkhacho, khachhang.

    public void lapHopDong() {
        // 1. Kiểm tra còn phòng trống không
        Phong[] phongTrong = DuLieu.dsPhong.dsPhongTrong();
        if (phongTrong.length == 0) {
            System.out.println("Hiện không còn phòng trống để thuê.");
            return;
        }

        // 2. Tìm khách đứng tên (theo mã hoặc SĐT), chưa có thì thêm mới
        KhachHang khDaiDien = chonHoacThemKhach("khách đứng tên hợp đồng");
        if (khDaiDien == null) {
            return;
        }

        // 3. Chọn nhân viên lập
        NhanVien nv = DuLieu.dsNhanVien.chon();
        if (nv == null) {
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

            String maP = NhapLieu.nhapChuoi("Nhập mã phòng muốn thuê (0 để kết thúc chọn phòng)");
            if (maP.equals("0")) {
                if (dsCT.length == 0) {
                    System.out.println("  Hợp đồng phải có ít nhất 1 phòng!");
                    continue;
                }
                break;
            }

            Phong p = DuLieu.dsPhong.timTheoMa(maP);
            if (p == null || !p.getTinhTrang().equals(Phong.TRONG)) {
                System.out.println("  Phòng không tồn tại hoặc không ở trạng thái TRONG.");
                continue;
            }

            // Kiểm tra phòng này đã được chọn trong chính hợp đồng đang lập chưa
            boolean daChon = false;
            for (CTHopDong item : dsCT) {
                if (item.getMaP().equalsIgnoreCase(p.getMaP())) {
                    daChon = true;
                    break;
                }
            }
            if (daChon) {
                System.out.println("  Phòng " + p.getMaP() + " đã có trong hợp đồng này rồi.");
                continue;
            }

            // Nhập chi tiết thuê cho phòng p
            CTHopDong ct = new CTHopDong();
            ct.setMaHDong(hd.getMaHDong());
            ct.setMaP(p.getMaP());
            ct.setDonGia(p.getDonGia());
            ct.nhap(p.getSoNguoiToiDa());
            ct.tinhThanhTien(soNgay);

            dsCT = Arrays.copyOf(dsCT, dsCT.length + 1);
            dsCT[dsCT.length - 1] = ct;

            // 6. Ghi khách ở (CTKhachO) cho phòng này: từ 1 tới slNguoi khách
            System.out.println("Nhập danh sách khách ở phòng " + p.getMaP() + " (tối đa " + ct.getSlNguoi() + " người):");
            for (int k = 1; k <= ct.getSlNguoi(); k++) {
                if (k > 1) {
                    String tiep = NhapLieu.nhapChuoi("Nhập thêm khách thứ " + k + " cho phòng " + p.getMaP() + "? (c/k)");
                    if (!tiep.equalsIgnoreCase("c")) {
                        break;
                    }
                }
                KhachHang khO = chonHoacThemKhach("khách ở thứ " + k + " (phòng " + p.getMaP() + ")");
                if (khO == null) {
                    k--;
                    continue;
                }
                // Tránh trùng khách trong cùng phòng
                boolean trungKhach = false;
                for (CTKhachO ko : dsKO) {
                    if (ko.getMaP().equalsIgnoreCase(p.getMaP()) && ko.getMaKH().equalsIgnoreCase(khO.getMaKH())) {
                        trungKhach = true;
                        break;
                    }
                }
                if (trungKhach) {
                    System.out.println("  Khách này đã được thêm vào phòng " + p.getMaP() + " rồi.");
                    k--;
                    continue;
                }
                dsKO = Arrays.copyOf(dsKO, dsKO.length + 1);
                dsKO[dsKO.length - 1] = new CTKhachO(hd.getMaHDong(), p.getMaP(), khO.getMaKH());
            }

            if (DuLieu.dsPhong.dsPhongTrong().length == dsCT.length) {
                System.out.println("Đã chọn hết các phòng trống hiện có.");
                break;
            }
        }

        // 7. Chốt dữ liệu vào các danh sách
        lapHopDong(hd, dsCT, dsKO);
        System.out.println("Đã lập hợp đồng " + hd.getMaHDong() + " thành công!");
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

    // Hàm phụ trợ: tìm khách theo mã hoặc SĐT, nếu chưa có thì cho thêm mới ngay tại chỗ
    private KhachHang chonHoacThemKhach(String vaiTro) {
        String tuKhoa = NhapLieu.nhapChuoi("Nhập mã KH hoặc SĐT của " + vaiTro + " (0 để hủy)");
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

    // Thêm (không tham số): nhập thông tin cơ bản của 1 hợp đồng từ bàn phím rồi gọi them(hd)
    public void them() {
        String maKH = NhapLieu.nhapChuoi("Nhập mã khách hàng đứng tên");
        if (DuLieu.dsKhachHang.timTheoMa(maKH) == null) {
            System.out.println("Không tìm thấy khách hàng có mã: " + maKH);
            return;
        }
        String maNV = NhapLieu.nhapChuoi("Nhập mã nhân viên lập");
        if (DuLieu.dsNhanVien.timTheoMa(maNV) == null) {
            System.out.println("Không tìm thấy nhân viên có mã: " + maNV);
            return;
        }

        HDThuePhong hd = new HDThuePhong();
        hd.setMaHDong(sinhMa());
        hd.setMaKH(maKH.toUpperCase());
        hd.setMaNV(maNV.toUpperCase());
        hd.setNgayLapHD(LocalDate.now());
        hd.nhap();

        them(hd);
        System.out.println("Đã thêm hợp đồng " + hd.getMaHDong());
    }

    // Thêm (có tham số): nới mảng ds thêm 1 phần tử và sao chép đối tượng hd vào cuối mảng
    public void them(HDThuePhong hd) {
        ds = Arrays.copyOf(ds, n + 1);
        ds[n] = new HDThuePhong(hd);
        n++;
    }

    // Tìm theo mã (không tham số): nhập mã từ bàn phím rồi in kết quả ra màn hình
    public void timTheoMa() {
        String ma = NhapLieu.nhapChuoi("Nhập mã hợp đồng cần tìm");
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
        String ma = NhapLieu.nhapChuoi("Nhập mã hợp đồng cần sửa ngày trả phòng");
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

        hd.nhapNgayTra();

        long soNgay = hd.soNgayThue();
        for (CTHopDong ct : DuLieu.dsCTHopDong.layTheoHopDong(hd.getMaHDong())) {
            ct.setThanhTien(ct.getDonGia() * soNgay);
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

    public HDThuePhong chon() {
        while (true) {
            String ma = NhapLieu.nhapChuoi("Nhập mã hợp đồng (0 để thoát)");
            if (ma.equals("0")) {
                return null;
            }
            HDThuePhong hd = timTheoMa(ma);
            if (hd != null) {
                return hd;
            }
            System.out.println("  Không có hợp đồng mã " + ma);
        }
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

    // Tìm theo mã khách hàng (không tham số)
    public void timTheoMaKH() {
        String maKH = NhapLieu.nhapChuoi("Nhập mã khách hàng");
        xuat(timTheoMaKH(maKH));
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
        String tuKhoa = NhapLieu.nhapChuoi("Nhập tên khách đứng tên (gõ không dấu cũng được)");
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
                if (!t[0].matches("HDTP\\d+")) {
                    throw new IllegalArgumentException();
                }
                them(new HDThuePhong(
                        t[0],
                        t[1],
                        t[2],
                        DinhDang.docNgay(t[3]),
                        Double.parseDouble(t[4]),
                        DinhDang.docNgay(t[5]),
                        DinhDang.docNgay(t[6])
                ));
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