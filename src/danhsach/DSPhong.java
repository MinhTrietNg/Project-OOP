package danhsach;

import app.DuLieu;
import core.NhapLieu;
import core.TepTin;
import java.util.Arrays;
import model.CTHopDong;
import model.Phong;
import model.PhongDeluxe;
import model.PhongSuite;
import model.PhongThuong;

// mảng Phong[] chứa lẫn cả 3 loại phòng (đa hình): gọi xuat() thì mỗi loại tự in phần riêng
public class DSPhong {

    private static final String FILE = "data/phong.txt";
    private Phong[] ds = new Phong[0];
    private int n = 0;

    public Phong[] getDs() {
        return ds;
    }

    public int getN() {
        return n;
    }

    // ================= CÁC HÀM BẮT BUỘC (gói 50%) =================

    // Thêm (không tham số): chọn loại phòng, nhập, tự sinh mã
    public void them() {
        Phong p = taoTheoLoai();
        p.nhap();
        if (trungTenP(p.getTenP(), "")) {
            System.out.println("Số phòng " + p.getTenP() + " đã có, không thêm.");
            return;
        }
        p.setMaP(sinhMa());
        p.setTinhTrang(Phong.TRONG);
        them(p);
        System.out.println("Đã thêm phòng " + p.getMaP());
    }

    // Thêm (có tham số): nới mảng ra 1 rồi đặt BẢN SAO vào cuối
    public void them(Phong p) {
        ds = Arrays.copyOf(ds, n + 1);
        ds[n] = saoChep(p);
        n++;
    }

    public void timTheoMa() {
        String ma = NhapLieu.nhapChuoi("Nhập mã phòng cần tìm");
        Phong p = timTheoMa(ma);
        if (p == null) {
            System.out.println("Không tìm thấy phòng có mã: " + ma);
        } else {
            p.xuat();
        }
    }

    // chỉ trả về, không in; không thấy thì null
    public Phong timTheoMa(String ma) {
        for (int i = 0; i < n; i++) {
            if (ds[i].getMaP().equalsIgnoreCase(ma)) {
                return ds[i];
            }
        }
        return null;
    }

    public void sua() {
        String ma = NhapLieu.nhapChuoi("Nhập mã phòng cần sửa");
        sua(ma);
    }

    // nhập vào bản tạm, hợp lệ rồi mới thay vào mảng; mã và tình trạng giữ nguyên
    public void sua(String ma) {
        for (int i = 0; i < n; i++) {
            if (ds[i].getMaP().equalsIgnoreCase(ma)) {
                Phong tam = saoChep(ds[i]);
                tam.nhap();
                if (trungTenP(tam.getTenP(), ma)) {
                    System.out.println("Số phòng " + tam.getTenP() + " đã có, không sửa.");
                    return;
                }
                ds[i] = tam;
                System.out.println("Đã cập nhật phòng " + ma);
                return;
            }
        }
        System.out.println("Không tìm thấy phòng có mã: " + ma);
    }

    public void xoa() {
        String ma = NhapLieu.nhapChuoi("Nhập mã phòng cần xóa");
        xoa(ma);
    }

    // chỉ xóa được khi phòng chưa có trong hợp đồng nào (mục 6.5)
    public void xoa(String ma) {
        for (CTHopDong ct : DuLieu.dsCTHopDong.getDs()) {
            if (ct.getMaP().equalsIgnoreCase(ma)) {
                System.out.println("Phòng đã có trong hợp đồng, không xóa được.");
                return;
            }
        }
        for (int i = 0; i < n; i++) {
            if (ds[i].getMaP().equalsIgnoreCase(ma)) {
                // dời các phần tử phía sau lên 1 ô rồi cắt bỏ ô cuối
                for (int j = i; j < n - 1; j++) {
                    ds[j] = ds[j + 1];
                }
                ds = Arrays.copyOf(ds, n - 1);
                n--;
                System.out.println("Đã xóa phòng có mã: " + ma);
                return;
            }
        }
        System.out.println("Không tìm thấy phòng có mã: " + ma);
    }

    public void xuat() {
        xuat(ds);
    }

    // in 1 mảng bất kỳ (dùng lại cho phòng trống, lọc, sắp xếp)
    public void xuat(Phong[] mang) {
        if (mang.length == 0) {
            System.out.println("Danh sách trống.");
        }
        for (Phong p : mang) {
            p.xuat();
        }
    }

    // mã mới = số lớn nhất đang có + 1;  "P007".substring(1) = "007" -> 7
    public String sinhMa() {
        int max = 0;
        for (int i = 0; i < n; i++) {
            int so = Integer.parseInt(ds[i].getMaP().substring(1));
            if (so > max) {
                max = so;
            }
        }
        return String.format("P%03d", max + 1);
    }

    // ================= GIAO TIẾP VỚI MODULE KHÁC (mục 6.6) =================

    public Phong[] dsPhongTrong() {
        Phong[] kq = new Phong[0];
        for (int i = 0; i < n; i++) {
            if (ds[i].getTinhTrang().equals(Phong.TRONG)) {
                kq = Arrays.copyOf(kq, kq.length + 1);
                kq[kq.length - 1] = ds[i];
            }
        }
        return kq;
    }

    // thuê phòng / trả phòng gọi hàm này, đổi xong ghi file luôn
    public void capNhatTinhTrang(String maP, String tinhTrang) {
        Phong p = timTheoMa(maP);
        if (p != null) {
            p.setTinhTrang(tinhTrang);
            ghiFile();
        }
    }

    // ================= HÀM NÂNG CAO (gói 70%) =================

    // số phòng đã có ai dùng chưa; maBoQua là mã của chính phòng đang sửa
    public boolean trungTenP(String tenP, String maBoQua) {
        for (int i = 0; i < n; i++) {
            if (ds[i].getTenP().equalsIgnoreCase(tenP) && !ds[i].getMaP().equalsIgnoreCase(maBoQua)) {
                return true;
            }
        }
        return false;
    }

    public void locTheoLoai() {
        System.out.println("  1. Thường   2. Deluxe   3. Suite");
        int chon = NhapLieu.nhapSoNguyen("Chọn loại phòng", 1, 3);
        String[] cacLoai = {Phong.THUONG, Phong.DELUXE, Phong.SUITE};
        xuat(locTheoLoai(cacLoai[chon - 1]));
    }

    public Phong[] locTheoLoai(String loai) {
        Phong[] kq = new Phong[0];
        for (int i = 0; i < n; i++) {
            if (ds[i].getLoaiPhong().equals(loai)) {
                kq = Arrays.copyOf(kq, kq.length + 1);
                kq[kq.length - 1] = ds[i];
            }
        }
        return kq;
    }

    public void sapXepTheoGia() {
        System.out.println("Phòng theo giá thuê tăng dần:");
        xuat(sapXepTheoGiaTang());
    }

    // sắp xếp nổi bọt trên BẢN SAO (không đổi thứ tự gốc):
    // so 2 phòng đứng cạnh nhau, phòng trước đắt hơn thì đổi chỗ
    public Phong[] sapXepTheoGiaTang() {
        Phong[] kq = Arrays.copyOf(ds, n);
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (kq[j].getDonGia() > kq[j + 1].getDonGia()) {
                    Phong tam = kq[j];
                    kq[j] = kq[j + 1];
                    kq[j + 1] = tam;
                }
            }
        }
        return kq;
    }

    // ================= HÀM PHỤ =================

    // hỏi loại rồi tạo đúng lớp con (chưa nhập gì)
    private Phong taoTheoLoai() {
        System.out.println("  1. Thường   2. Deluxe   3. Suite");
        int chon = NhapLieu.nhapSoNguyen("Chọn loại phòng", 1, 3);
        if (chon == 2) {
            return new PhongDeluxe();
        }
        if (chon == 3) {
            return new PhongSuite();
        }
        return new PhongThuong();
    }

    // chép bằng constructor sao chép của đúng lớp con (instanceof để biết p thật sự là loại nào)
    private Phong saoChep(Phong p) {
        if (p instanceof PhongDeluxe) {
            return new PhongDeluxe((PhongDeluxe) p);
        }
        if (p instanceof PhongSuite) {
            return new PhongSuite((PhongSuite) p);
        }
        return new PhongThuong((PhongThuong) p);
    }

    // ================= ĐỌC / GHI FILE =================

    // phong.txt: loai|maP|tenP|donGia|soNguoiToiDa|slGiuong|tinhTrang (+ phần riêng)
    public void docFile() {
        ds = new Phong[0];
        n = 0;
        for (String[] t : TepTin.doc(FILE)) {
            try {
                // mã sai dạng sẽ làm sinhMa() lỗi, nên loại ngay từ lúc đọc
                if (!t[1].matches("P\\d+") || !(t[6].equals(Phong.TRONG) || t[6].equals(Phong.DA_THUE))) {
                    throw new IllegalArgumentException();
                }
                them(docDong(t));
            } catch (RuntimeException e) {
                System.out.println("Bỏ qua dòng lỗi trong " + FILE + ": " + String.join("|", t));
            }
        }
    }

    // trường đầu cho biết loại phòng, từ đó biết tạo lớp con nào và đọc thêm mấy trường
    private Phong docDong(String[] t) {
        String ma = t[1];
        String ten = t[2];
        double gia = Double.parseDouble(t[3]);
        int soNguoi = Integer.parseInt(t[4]);
        int giuong = Integer.parseInt(t[5]);
        String tinhTrang = t[6];
        if (t[0].equals(Phong.DELUXE)) {
            return new PhongDeluxe(ma, ten, gia, soNguoi, giuong, tinhTrang, t[7], Boolean.parseBoolean(t[8]), t[9]);
        }
        if (t[0].equals(Phong.SUITE)) {
            return new PhongSuite(ma, ten, gia, soNguoi, giuong, tinhTrang, Boolean.parseBoolean(t[7]),
                    Integer.parseInt(t[8]));
        }
        if (t[0].equals(Phong.THUONG)) {
            return new PhongThuong(ma, ten, gia, soNguoi, giuong, tinhTrang);
        }
        throw new IllegalArgumentException();
    }

    public void ghiFile() {
        String[] cacDong = new String[n];
        for (int i = 0; i < n; i++) {
            cacDong[i] = ds[i].toLine();
        }
        TepTin.ghi(FILE, cacDong);
    }
}
