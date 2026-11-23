package danhsach;

import app.DuLieu;
import core.DanhSach;
import core.DinhDang;
import core.NhapLieu;
import core.TepTin;
import core.TimKiem;
import java.util.Arrays;
import model.CTPhieuNhap;
import model.DichVu;
import model.HangHoa;

public class DSHangHoa implements DanhSach<HangHoa> {

    private static final String FILE = "data/hanghoa.txt";
    private HangHoa[] ds = new HangHoa[0];
    private int n = 0;

    @Override
    public HangHoa[] getDs() {
        return ds;
    }

    @Override
    public int getN() {
        return n;
    }

    // ================= CÁC HÀM BẮT BUỘC (gói 50%) =================

    // hàng mới có slTon = 0 (constructor không tham số đã gán), chỉ tăng khi lập phiếu nhập
    public void them() {
        HangHoa hh = new HangHoa();
        hh.nhap();
        // thêm mới: chưa có mã nên không bỏ qua ai
        if (trungTen(hh.getTenH(), "")) {
            System.out.println("Tên hàng " + hh.getTenH() + " đã có, không thêm.");
            return;
        }
        hh.setMaH(sinhMa());
        them(hh);
        System.out.println("Đã thêm hàng " + hh.getMaH() + " (tồn kho = 0, hãy lập phiếu nhập)");
    }

    @Override
    public void them(HangHoa hh) {
        ds = Arrays.copyOf(ds, n + 1);
        ds[n] = new HangHoa(hh);
        n++;
    }

    public void timTheoMa() {
        String ma = NhapLieu.nhapChuoi("Nhập mã hàng cần tìm");
        HangHoa hh = timTheoMa(ma);
        if (hh == null) {
            System.out.println("Không tìm thấy hàng có mã: " + ma);
        } else {
            hh.xuat();
        }
    }

    public HangHoa timTheoMa(String ma) {
        for (int i = 0; i < n; i++) {
            if (ds[i].getMaH().equalsIgnoreCase(ma)) {
                return ds[i];
            }
        }
        return null;
    }

    public void sua() {
        String ma = NhapLieu.nhapChuoi("Nhập mã hàng cần sửa");
        sua(ma);
    }

    // chỉ sửa tên: donGia tự cập nhật khi lập phiếu nhập, slTon chỉ đổi qua congKho / truKho (mục 4.5)
    public void sua(String ma) {
        HangHoa hh = timTheoMa(ma);
        if (hh == null) {
            System.out.println("Không tìm thấy hàng có mã: " + ma);
            return;
        }
        String ten = NhapLieu.nhapChuoi("Tên hàng");
        if (trungTen(ten, ma)) {
            System.out.println("Tên hàng " + ten + " đã có, không sửa.");
            return;
        }
        hh.setTenH(ten);
        System.out.println("Đã cập nhật hàng " + ma);
    }

    public void xoa() {
        String ma = NhapLieu.nhapChuoi("Nhập mã hàng cần xóa");
        xoa(ma);
    }

    // hàng đã từng nhập kho hoặc đang gắn với dịch vụ thì không xóa được (mục 6.5)
    public void xoa(String ma) {
        for (CTPhieuNhap ct : DuLieu.dsCTPhieuNhap.getDs()) {
            if (ct.getMaH().equalsIgnoreCase(ma)) {
                System.out.println("Hàng đã có trong phiếu nhập, không xóa được.");
                return;
            }
        }
        for (DichVu dv : DuLieu.dsDichVu.getDs()) {
            if (dv.getMaH().equalsIgnoreCase(ma)) {
                System.out.println("Hàng đang dùng cho dịch vụ " + dv.getMaDV() + ", không xóa được.");
                return;
            }
        }

        for (int i = 0; i < n; i++) {
            if (ds[i].getMaH().equalsIgnoreCase(ma)) {
                for (int j = i; j < n - 1; j++) {
                    ds[j] = ds[j + 1];
                }
                ds = Arrays.copyOf(ds, n - 1);
                n--;
                System.out.println("Đã xóa hàng có mã: " + ma);
                return;
            }
        }
        System.out.println("Không tìm thấy hàng có mã: " + ma);
    }

    @Override
    public void xuat() {
        xuat(ds);
    }

    public void xuat(HangHoa[] mang) {
        if (mang.length == 0) {
            System.out.println("Danh sách trống.");
            return;
        }
        for (HangHoa hh : mang) {
            hh.xuat();
        }
    }

    // "HH005".substring(2) = "005" -> 5
    public String sinhMa() {
        int max = 0;
        for (int i = 0; i < n; i++) {
            int so = Integer.parseInt(ds[i].getMaH().substring(2));
            if (so > max) {
                max = so;
            }
        }
        return String.format("HH%03d", max + 1);
    }

    public HangHoa chon() {
        while (true) {
            String ma = NhapLieu.nhapChuoi("Nhập mã hàng (0 để thoát)");
            if (ma.equals("0")) {
                return null;
            }
            HangHoa hh = timTheoMa(ma);
            if (hh != null) {
                return hh;
            }
            System.out.println("  Không có hàng mã " + ma);
        }
    }

    // ---------- 2 hàm cho module khác gọi (mục 6.6) ----------

    // module Dịch vụ gọi khi khách dùng dịch vụ có tiêu hao hàng
    // không đủ hàng thì KHÔNG trừ và trả false để bên kia báo lỗi
    public boolean truKho(String maH, int sl) {
        HangHoa hh = timTheoMa(maH);
        if (hh == null || hh.getSlTon() < sl) {
            return false;
        }
        hh.setSlTon(hh.getSlTon() - sl);
        return true;
    }

    // DSPhieuNhap gọi khi lập phiếu nhập
    public void congKho(String maH, int sl) {
        HangHoa hh = timTheoMa(maH);
        if (hh != null) {
            hh.setSlTon(hh.getSlTon() + sl);
        }
    }

    // ================= HÀM NÂNG CAO (gói 70%) =================

    // tên hàng đã có chưa (không phân biệt hoa thường); maBoQua là mã của chính hàng đang sửa
    public boolean trungTen(String ten, String maBoQua) {
        for (int i = 0; i < n; i++) {
            if (ds[i].getTenH().equalsIgnoreCase(ten) && !ds[i].getMaH().equalsIgnoreCase(maBoQua)) {
                return true;
            }
        }
        return false;
    }

    public void timTheoTen() {
        String tuKhoa = NhapLieu.nhapChuoi("Nhập tên hàng (gõ không dấu cũng được)");
        xuat(timTheoTen(tuKhoa));
    }

    public HangHoa[] timTheoTen(String tuKhoa) {
        HangHoa[] kq = new HangHoa[0];
        for (int i = 0; i < n; i++) {
            if (TimKiem.chua(ds[i].getTenH(), tuKhoa)) {
                kq = Arrays.copyOf(kq, kq.length + 1);
                kq[kq.length - 1] = ds[i];
            }
        }
        return kq;
    }

    public void xuatSapHet() {
        int nguong = NhapLieu.nhapSoNguyen("Ngưỡng cảnh báo (tồn <= ngưỡng)", 0, 100000);
        HangHoa[] kq = dsSapHet(nguong);
        System.out.println("Có " + kq.length + " mặt hàng cần nhập thêm:");
        xuat(kq);
    }

    // hàng có tồn kho <= ngưỡng
    public HangHoa[] dsSapHet(int nguong) {
        HangHoa[] kq = new HangHoa[0];
        for (int i = 0; i < n; i++) {
            if (ds[i].getSlTon() <= nguong) {
                kq = Arrays.copyOf(kq, kq.length + 1);
                kq[kq.length - 1] = ds[i];
            }
        }
        return kq;
    }

    // báo cáo tồn kho (mục 6.4): tồn ít nhất in trước + tổng giá trị
    public void xuatTonKho() {
        xuat(sapXepTheoTonTang());
        System.out.println("Tổng giá trị hàng trong kho: " + DinhDang.tien(tongGiaTriTon()));
    }

    // sắp xếp nổi bọt trên BẢN SAO: người trước tồn nhiều hơn thì đổi chỗ
    // (giống hệt DSNhanVien.sapXepTheoLuongGiam, chỉ khác điều kiện so sánh)
    public HangHoa[] sapXepTheoTonTang() {
        HangHoa[] kq = Arrays.copyOf(ds, n);
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (kq[j].getSlTon() > kq[j + 1].getSlTon()) {
                    HangHoa tam = kq[j];
                    kq[j] = kq[j + 1];
                    kq[j + 1] = tam;
                }
            }
        }
        return kq;
    }

    public double tongGiaTriTon() {
        double tong = 0;
        for (int i = 0; i < n; i++) {
            tong += ds[i].giaTriTon();
        }
        return tong;
    }

    // ================= ĐỌC / GHI FILE =================

    // hanghoa.txt: maH|tenH|donGia|slTon
    @Override
    public void docFile() {
        ds = new HangHoa[0];
        n = 0;
        for (String[] t : TepTin.doc(FILE)) {
            try {
                them(new HangHoa(t[0], t[1], Double.parseDouble(t[2]), Integer.parseInt(t[3])));
            } catch (Exception e) {
                System.out.println("Bỏ qua 1 dòng lỗi trong " + FILE);
            }
        }
    }

    @Override
    public void ghiFile() {
        String[] cacDong = new String[n];
        for (int i = 0; i < n; i++) {
            cacDong[i] = ds[i].toLine();
        }
        TepTin.ghi(FILE, cacDong);
    }
}
