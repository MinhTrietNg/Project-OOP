package danhsach;

import app.DuLieu;
import core.DanhSach;
import core.NhapLieu;
import core.TepTin;
import core.TimKiem;
import java.util.Arrays;
import model.CTSuDungDV;
import model.DichVu;
import model.HangHoa;

public class DSDichVu implements DanhSach<DichVu> {

    private static final String FILE = "data/dichvu.txt";
    private DichVu[] ds = new DichVu[0];
    private int n = 0;

    @Override
    public DichVu[] getDs() {
        return ds;
    }

    @Override
    public int getN() {
        return n;
    }

    // ================= CÁC HÀM BẮT BUỘC (gói 50%) =================

    // Thêm (không tham số): nhập tên, giá, chọn hàng tiêu hao, tự sinh mã
    public void them() {
        DichVu dv = new DichVu();
        dv.nhap();
        // thêm mới: chưa có mã nên không bỏ qua ai
        if (trungTen(dv.getTenDV(), "")) {
            System.out.println("Tên dịch vụ " + dv.getTenDV() + " đã có, không thêm.");
            return;
        }
        dv.setMaH(chonMaHang());
        dv.setMaDV(sinhMa());
        them(dv);
        System.out.println("Đã thêm dịch vụ " + dv.getMaDV());
    }

    // Thêm (có tham số): đối tượng đã có sẵn (đọc file, kiểm thử); trùng mã thì không thêm
    @Override
    public boolean them(DichVu dv) {
        if (timTheoMa(dv.getMaDV()) != null) {
            return false;
        }
        ds = Arrays.copyOf(ds, n + 1);
        ds[n] = dv;
        n++;
        return true;
    }

    public void timTheoMa() {
        String ma = NhapLieu.nhapChuoi("Nhập mã dịch vụ cần tìm");
        DichVu dv = timTheoMa(ma);
        if (dv == null) {
            System.out.println("Không tìm thấy dịch vụ có mã: " + ma);
        } else {
            dv.xuat();
        }
    }

    // chỉ trả về, không in; không thấy thì null
    public DichVu timTheoMa(String ma) {
        for (int i = 0; i < n; i++) {
            if (ds[i].getMaDV().equalsIgnoreCase(ma)) {
                return ds[i];
            }
        }
        return null;
    }

    public void sua() {
        String ma = NhapLieu.nhapChuoi("Nhập mã dịch vụ cần sửa");
        sua(ma);
    }

    // sửa tên, giá, hàng tiêu hao; giá mới chỉ áp dụng cho lần dùng sau
    // (CTSuDungDV đã ghi giữ nguyên giá cũ vì đã chép donGia lúc dùng)
    // nhập vào bản tạm trước, hợp lệ rồi mới chép sang để không làm hỏng dữ liệu cũ
    public void sua(String ma) {
        DichVu dv = timTheoMa(ma);
        if (dv == null) {
            System.out.println("Không tìm thấy dịch vụ có mã: " + ma);
            return;
        }
        DichVu tam = new DichVu(dv);
        tam.nhap();
        if (trungTen(tam.getTenDV(), ma)) {
            System.out.println("Tên dịch vụ " + tam.getTenDV() + " đã có, không sửa.");
            return;
        }
        dv.setTenDV(tam.getTenDV());
        dv.setDonGia(tam.getDonGia());
        dv.setMaH(chonMaHang());
        System.out.println("Đã cập nhật dịch vụ " + ma);
    }

    public void xoa() {
        String ma = NhapLieu.nhapChuoi("Nhập mã dịch vụ cần xóa");
        xoa(ma);
    }

    // dịch vụ đã có khách dùng thì không xóa được (mục 6.5)
    public void xoa(String ma) {
        for (CTSuDungDV ct : DuLieu.dsCTSuDungDV.getDs()) {
            if (ct.getMaDV().equalsIgnoreCase(ma)) {
                System.out.println("Dịch vụ đã có khách dùng, không xóa được.");
                return;
            }
        }

        for (int i = 0; i < n; i++) {
            if (ds[i].getMaDV().equalsIgnoreCase(ma)) {
                // dời các phần tử phía sau lên 1 ô rồi cắt bỏ ô cuối
                for (int j = i; j < n - 1; j++) {
                    ds[j] = ds[j + 1];
                }
                ds = Arrays.copyOf(ds, n - 1);
                n--;
                System.out.println("Đã xóa dịch vụ có mã: " + ma);
                return;
            }
        }
        System.out.println("Không tìm thấy dịch vụ có mã: " + ma);
    }

    @Override
    public void xuat() {
        xuat(ds);
    }

    // in 1 mảng bất kỳ (dùng lại cho kết quả tìm kiếm)
    public void xuat(DichVu[] mang) {
        if (mang.length == 0) {
            System.out.println("Danh sách trống.");
            return;
        }
        for (DichVu dv : mang) {
            dv.xuat();
        }
    }

    // "DV005".substring(2) = "005" -> 5
    public String sinhMa() {
        int max = 0;
        for (int i = 0; i < n; i++) {
            int so = Integer.parseInt(ds[i].getMaDV().substring(2));
            if (so > max) {
                max = so;
            }
        }
        return String.format("DV%03d", max + 1);
    }

    // cho chỗ khác chọn 1 dịch vụ: hỏi mã tới khi đúng, gõ 0 thì trả null
    public DichVu chon() {
        while (true) {
            String ma = NhapLieu.nhapChuoi("Nhập mã dịch vụ (0 để thoát)");
            if (ma.equals("0")) {
                return null;
            }
            DichVu dv = timTheoMa(ma);
            if (dv != null) {
                return dv;
            }
            System.out.println("  Không có dịch vụ mã " + ma);
        }
    }

    // hỏi dịch vụ có tiêu hao hàng trong kho không; có thì chọn hàng, không thì trả ""
    private String chonMaHang() {
        if (DuLieu.dsHangHoa.getN() == 0) {
            System.out.println("  (Kho chưa có hàng hóa nên dịch vụ này không gắn hàng.)");
            return "";
        }
        if (!NhapLieu.nhapCoKhong("Dịch vụ có tiêu hao hàng trong kho không")) {
            return "";
        }
        DuLieu.dsHangHoa.xuat();
        HangHoa hh = DuLieu.dsHangHoa.chon();
        if (hh == null) {
            return "";
        }
        return hh.getMaH();
    }

    // ================= HÀM NÂNG CAO (gói 70%) =================

    // tên dịch vụ đã có chưa (không phân biệt hoa thường); maBoQua là mã của chính dịch vụ đang sửa
    public boolean trungTen(String ten, String maBoQua) {
        for (int i = 0; i < n; i++) {
            if (ds[i].getTenDV().equalsIgnoreCase(ten) && !ds[i].getMaDV().equalsIgnoreCase(maBoQua)) {
                return true;
            }
        }
        return false;
    }

    public void timTheoTen() {
        String tuKhoa = NhapLieu.nhapChuoi("Nhập tên dịch vụ (gõ không dấu cũng được)");
        xuat(timTheoTen(tuKhoa));
    }

    public DichVu[] timTheoTen(String tuKhoa) {
        DichVu[] kq = new DichVu[0];
        for (int i = 0; i < n; i++) {
            if (TimKiem.chua(ds[i].getTenDV(), tuKhoa)) {
                kq = Arrays.copyOf(kq, kq.length + 1);
                kq[kq.length - 1] = ds[i];
            }
        }
        return kq;
    }

    // ================= ĐỌC / GHI FILE =================

    // dichvu.txt: maDV|tenDV|donGia|maH
    @Override
    public void docFile() {
        ds = new DichVu[0];
        n = 0;
        for (String[] t : TepTin.doc(FILE)) {
            try {
                them(new DichVu(t[0], t[1], Double.parseDouble(t[2]), t[3]));
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
