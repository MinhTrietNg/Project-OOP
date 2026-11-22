package danhsach;

import app.DuLieu;
import core.NhapLieu;
import core.TepTin;
import core.TimKiem;
import java.util.Arrays;
import java.util.Scanner;
import model.CTKhachO;
import model.HDThuePhong;
import model.KhachHang;

public class DSKhachHang {

    // Scanner riêng của lớp này; không close() vì sẽ đóng luôn System.in
    private static final Scanner sc = new Scanner(System.in, "UTF-8");

    private static final String FILE = "data/khachhang.txt";
    private KhachHang[] ds = new KhachHang[0];
    private int n = 0;

    public KhachHang[] getDs() {
        return ds;
    }

    public int getN() {
        return n;
    }

    // ================= CÁC HÀM BẮT BUỘC (gói 50%) =================

    // Thêm (không tham số): nhập từ bàn phím, tự sinh mã
    public void them() {
        KhachHang kh = new KhachHang();
        kh.nhap();
        // thêm mới: chưa có mã nên không bỏ qua ai
        if (trungSdt(kh.getSdt(), "")) {
            System.out.println("Số điện thoại đã có khách khác dùng, không thêm.");
            return;
        }
        kh.setMaKH(sinhMa());
        them(kh);
        System.out.println("Đã thêm khách hàng " + kh.getMaKH());
    }

    // Thêm (có tham số): nới mảng ra 1 rồi đặt BẢN SAO vào cuối
    public void them(KhachHang kh) {
        ds = Arrays.copyOf(ds, n + 1);
        ds[n] = new KhachHang(kh);
        n++;
    }

    public void timTheoMa() {
        String ma = NhapLieu.nhapChuoi(sc, "Nhập mã khách hàng cần tìm");
        KhachHang kh = timTheoMa(ma);
        if (kh == null) {
            System.out.println("Không tìm thấy khách hàng có mã: " + ma);
        } else {
            kh.xuat();
        }
    }

    // chỉ trả về, không in; không thấy thì null
    public KhachHang timTheoMa(String ma) {
        for (int i = 0; i < n; i++) {
            if (ds[i].getMaKH().equalsIgnoreCase(ma)) {
                return ds[i];
            }
        }
        return null;
    }

    public void sua() {
        String ma = NhapLieu.nhapChuoi(sc, "Nhập mã khách hàng cần sửa");
        sua(ma);
    }

    // nhập vào bản tạm trước, hợp lệ rồi mới chép sang để không làm hỏng dữ liệu cũ
    public void sua(String ma) {
        KhachHang kh = timTheoMa(ma);
        if (kh == null) {
            System.out.println("Không tìm thấy khách hàng có mã: " + ma);
            return;
        }
        KhachHang tam = new KhachHang(kh);
        tam.nhap();
        if (trungSdt(tam.getSdt(), ma)) {
            System.out.println("Số điện thoại đã có khách khác dùng, không sửa.");
            return;
        }
        kh.setHoTen(tam.getHoTen());
        kh.setSdt(tam.getSdt());
        kh.setDiaChi(tam.getDiaChi());
        System.out.println("Đã cập nhật khách hàng " + ma);
    }

    public void xoa() {
        String ma = NhapLieu.nhapChuoi(sc, "Nhập mã khách hàng cần xóa");
        xoa(ma);
    }

    // chỉ xóa được khi khách chưa đứng tên hợp đồng và chưa ở phòng nào (mục 6.5)
    public void xoa(String ma) {
        for (HDThuePhong hd : DuLieu.dsHDThuePhong.getDs()) {
            if (hd.getMaKH().equalsIgnoreCase(ma)) {
                System.out.println("Khách đã đứng tên hợp đồng, không xóa được.");
                return;
            }
        }
        for (CTKhachO ko : DuLieu.dsCTKhachO.getDs()) {
            if (ko.getMaKH().equalsIgnoreCase(ma)) {
                System.out.println("Khách đã có trong danh sách khách ở, không xóa được.");
                return;
            }
        }
        for (int i = 0; i < n; i++) {
            if (ds[i].getMaKH().equalsIgnoreCase(ma)) {
                // dời các phần tử phía sau lên 1 ô rồi cắt bỏ ô cuối
                for (int j = i; j < n - 1; j++) {
                    ds[j] = ds[j + 1];
                }
                ds = Arrays.copyOf(ds, n - 1);
                n--;
                System.out.println("Đã xóa khách hàng có mã: " + ma);
                return;
            }
        }
        System.out.println("Không tìm thấy khách hàng có mã: " + ma);
    }

    public void xuat() {
        xuat(ds);
    }

    // in 1 mảng bất kỳ (dùng lại cho kết quả tìm kiếm)
    public void xuat(KhachHang[] mang) {
        if (mang.length == 0) {
            System.out.println("Danh sách trống.");
            return;
        }
        for (KhachHang kh : mang) {
            kh.xuat();
        }
    }

    // mã mới = số lớn nhất đang có + 1;  "KH007".substring(2) = "007" -> 7
    public String sinhMa() {
        int max = 0;
        for (int i = 0; i < n; i++) {
            int so = Integer.parseInt(ds[i].getMaKH().substring(2));
            if (so > max) {
                max = so;
            }
        }
        return String.format("KH%03d", max + 1);
    }

    // ================= HÀM NÂNG CAO (gói 70%) =================

    // sđt đã có khách KHÁC dùng chưa; maBoQua là mã của chính khách đang sửa
    public boolean trungSdt(String sdt, String maBoQua) {
        for (int i = 0; i < n; i++) {
            if (ds[i].getSdt().equals(sdt) && !ds[i].getMaKH().equalsIgnoreCase(maBoQua)) {
                return true;
            }
        }
        return false;
    }

    public void timTheoSdt() {
        String sdt = NhapLieu.nhapChuoi(sc, "Nhập số điện thoại cần tìm");
        KhachHang kh = timTheoSdt(sdt);
        if (kh == null) {
            System.out.println("Không tìm thấy khách hàng có số: " + sdt);
        } else {
            kh.xuat();
        }
    }

    // sđt không trùng nên tìm được tối đa 1 khách; không thấy thì null
    public KhachHang timTheoSdt(String sdt) {
        for (int i = 0; i < n; i++) {
            if (ds[i].getSdt().equals(sdt)) {
                return ds[i];
            }
        }
        return null;
    }

    public void timTheoTen() {
        String tuKhoa = NhapLieu.nhapChuoi(sc, "Nhập tên (gõ không dấu cũng được)");
        xuat(timTheoTen(tuKhoa));
    }

    // gom các kết quả khớp vào mảng mới
    public KhachHang[] timTheoTen(String tuKhoa) {
        KhachHang[] kq = new KhachHang[0];
        for (int i = 0; i < n; i++) {
            if (TimKiem.chua(ds[i].getHoTen(), tuKhoa)) {
                kq = Arrays.copyOf(kq, kq.length + 1);
                kq[kq.length - 1] = ds[i];
            }
        }
        return kq;
    }

    // ================= ĐỌC / GHI FILE =================

    // khachhang.txt: maKH|hoTen|sdt|diaChi
    public void docFile() {
        ds = new KhachHang[0];
        n = 0;
        for (String[] t : TepTin.doc(FILE)) {
            try {
                them(new KhachHang(t[0], t[1], t[2], t[3]));
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
