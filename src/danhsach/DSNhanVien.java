package danhsach;

import app.DuLieu;
import core.DinhDang;
import core.NhapLieu;
import core.TepTin;
import core.TimKiem;
import java.util.Arrays;
import model.HDThuePhong;
import model.HoaDon;
import model.NhanVien;
import model.PhieuNhap;

public class DSNhanVien {

    private static final String FILE = "data/nhanvien.txt";
    private NhanVien[] ds = new NhanVien[0];
    private int n = 0;

    public NhanVien[] getDs() {
        return ds;
    }

    public int getN() {
        return n;
    }

    // ================= CÁC HÀM BẮT BUỘC (gói 50%) =================

    // Thêm (không tham số): nhập từ bàn phím, tự sinh mã
    public void them() {
        NhanVien nv = new NhanVien();
        nv.nhap();
        // thêm mới: chưa có mã nên không bỏ qua ai
        if (trungSdt(nv.getSdt(), "")) {
            System.out.println("Số điện thoại đã có nhân viên khác dùng, không thêm.");
            return;
        }
        nv.setMaNV(sinhMa());
        them(nv);
        System.out.println("Đã thêm nhân viên " + nv.getMaNV());
    }

    // Thêm (có tham số): nới mảng ra 1 rồi đặt BẢN SAO vào cuối
    public void them(NhanVien nv) {
        ds = Arrays.copyOf(ds, n + 1);
        ds[n] = new NhanVien(nv);
        n++;
    }

    public void timTheoMa() {
        String ma = NhapLieu.nhapChuoi("Nhập mã nhân viên cần tìm");
        NhanVien nv = timTheoMa(ma);
        if (nv == null) {
            System.out.println("Không tìm thấy nhân viên có mã: " + ma);
        } else {
            nv.xuat();
        }
    }

    // chỉ trả về, không in; không thấy thì null
    public NhanVien timTheoMa(String ma) {
        for (int i = 0; i < n; i++) {
            if (ds[i].getMaNV().equalsIgnoreCase(ma)) {
                return ds[i];
            }
        }
        return null;
    }

    public void sua() {
        String ma = NhapLieu.nhapChuoi("Nhập mã nhân viên cần sửa");
        sua(ma);
    }

    // nhập vào BẢN TẠM trước, hợp lệ rồi mới chép sang, để nhập lỗi không làm hỏng dữ liệu cũ
    public void sua(String ma) {
        NhanVien nv = timTheoMa(ma);
        if (nv == null) {
            System.out.println("Không tìm thấy nhân viên có mã: " + ma);
            return;
        }
        NhanVien tam = new NhanVien(nv);
        tam.nhap();
        if (trungSdt(tam.getSdt(), ma)) {
            System.out.println("Số điện thoại đã có nhân viên khác dùng, không sửa.");
            return;
        }
        nv.setHoTen(tam.getHoTen());
        nv.setSdt(tam.getSdt());
        nv.setChucVu(tam.getChucVu());
        nv.setLuongCB(tam.getLuongCB());
        System.out.println("Đã cập nhật nhân viên " + ma);
    }

    public void xoa() {
        String ma = NhapLieu.nhapChuoi("Nhập mã nhân viên cần xóa");
        xoa(ma);
    }

    // chỉ xóa được khi nhân viên chưa lập chứng từ nào (mục 6.5)
    public void xoa(String ma) {
        for (PhieuNhap pn : DuLieu.dsPhieuNhap.getDs()) {
            if (pn.getMaNV().equalsIgnoreCase(ma)) {
                System.out.println("Nhân viên đã lập phiếu nhập, không xóa được.");
                return;
            }
        }
        for (HoaDon hd : DuLieu.dsHoaDon.getDs()) {
            if (hd.getMaNV().equalsIgnoreCase(ma)) {
                System.out.println("Nhân viên đã lập hóa đơn, không xóa được.");
                return;
            }
        }
        for (HDThuePhong hd : DuLieu.dsHDThuePhong.getDs()) {
            if (hd.getMaNV().equalsIgnoreCase(ma)) {
                System.out.println("Nhân viên đã lập hợp đồng, không xóa được.");
                return;
            }
        }

        for (int i = 0; i < n; i++) {
            if (ds[i].getMaNV().equalsIgnoreCase(ma)) {
                // dời các phần tử phía sau lên 1 ô rồi cắt bỏ ô cuối
                for (int j = i; j < n - 1; j++) {
                    ds[j] = ds[j + 1];
                }
                ds = Arrays.copyOf(ds, n - 1);
                n--;
                System.out.println("Đã xóa nhân viên có mã: " + ma);
                return;
            }
        }
        System.out.println("Không tìm thấy nhân viên có mã: " + ma);
    }

    public void xuat() {
        xuat(ds);
    }

    // in 1 mảng bất kỳ (dùng lại cho kết quả tìm kiếm, sắp xếp)
    public void xuat(NhanVien[] mang) {
        if (mang.length == 0) {
            System.out.println("Danh sách trống.");
            return;
        }
        for (NhanVien nv : mang) {
            nv.xuat();
        }
    }

    // mã mới = số lớn nhất đang có + 1;  "NV007".substring(2) = "007" -> 7
    public String sinhMa() {
        int max = 0;
        for (int i = 0; i < n; i++) {
            int so = Integer.parseInt(ds[i].getMaNV().substring(2));
            if (so > max) {
                max = so;
            }
        }
        return String.format("NV%03d", max + 1);
    }

    // cho chỗ khác chọn 1 nhân viên: hỏi mã tới khi đúng, gõ 0 thì trả null
    public NhanVien chon() {
        while (true) {
            String ma = NhapLieu.nhapChuoi("Nhập mã nhân viên (0 để thoát)");
            if (ma.equals("0")) {
                return null;
            }
            NhanVien nv = timTheoMa(ma);
            if (nv != null) {
                return nv;
            }
            System.out.println("  Không có nhân viên mã " + ma);
        }
    }

    // ================= HÀM NÂNG CAO (gói 70%) =================

    // sđt đã có người KHÁC dùng chưa; maBoQua là mã của chính người đang sửa
    public boolean trungSdt(String sdt, String maBoQua) {
        for (int i = 0; i < n; i++) {
            if (ds[i].getSdt().equals(sdt) && !ds[i].getMaNV().equalsIgnoreCase(maBoQua)) {
                return true;
            }
        }
        return false;
    }

    public void timTheoTen() {
        String tuKhoa = NhapLieu.nhapChuoi("Nhập tên (gõ không dấu cũng được)");
        xuat(timTheoTen(tuKhoa));
    }

    // gom các kết quả khớp vào mảng mới
    public NhanVien[] timTheoTen(String tuKhoa) {
        NhanVien[] kq = new NhanVien[0];
        for (int i = 0; i < n; i++) {
            if (TimKiem.chua(ds[i].getHoTen(), tuKhoa)) {
                kq = Arrays.copyOf(kq, kq.length + 1);
                kq[kq.length - 1] = ds[i];
            }
        }
        return kq;
    }

    public void sapXepTheoLuong() {
        System.out.println("Nhân viên theo lương cơ bản giảm dần:");
        xuat(sapXepTheoLuongGiam());
    }

    // sắp xếp nổi bọt trên BẢN SAO (không đổi thứ tự gốc):
    // so 2 người đứng cạnh nhau, người trước lương thấp hơn thì đổi chỗ
    public NhanVien[] sapXepTheoLuongGiam() {
        NhanVien[] kq = Arrays.copyOf(ds, n);
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (kq[j].getLuongCB() < kq[j + 1].getLuongCB()) {
                    NhanVien tam = kq[j];
                    kq[j] = kq[j + 1];
                    kq[j + 1] = tam;
                }
            }
        }
        return kq;
    }

    // với mỗi chức vụ: đếm số người và cộng lương (vòng lặp lồng)
    public void thongKeTheoChucVu() {
        for (String chucVu : NhanVien.CAC_CHUC_VU) {
            int soNguoi = 0;
            double tongLuong = 0;
            for (int i = 0; i < n; i++) {
                if (ds[i].getChucVu().equals(chucVu)) {
                    soNguoi++;
                    tongLuong += ds[i].getLuongCB();
                }
            }
            System.out.println(chucVu + ": " + soNguoi + " người, tổng lương " + DinhDang.tien(tongLuong));
        }
        System.out.println("Toàn khách sạn: " + n + " nhân viên");
    }

    // ================= ĐỌC / GHI FILE =================

    // nhanvien.txt: maNV|hoTen|sdt|chucVu|luongCB
    public void docFile() {
        ds = new NhanVien[0];
        n = 0;
        for (String[] t : TepTin.doc(FILE)) {
            try {
                them(new NhanVien(t[0], t[1], t[2], t[3], Double.parseDouble(t[4])));
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
