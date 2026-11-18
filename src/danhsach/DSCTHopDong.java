package danhsach;

import core.NhapLieu;
import core.TepTin;
import java.util.Arrays;
import model.CTHopDong;

public class DSCTHopDong {

    private static final String FILE = "data/cthopdong.txt";
    private CTHopDong[] ds = new CTHopDong[0];
    private int n = 0;

    public CTHopDong[] getDs() {
        return ds;
    }

    public int getN() {
        return n;
    }

    // ================= CÁC HÀM BẮT BUỘC (Danh sách chi tiết - mục 2.9) =================

    // Thêm (có tham số): chỉ có bản có tham số
    // Kiểm tra tránh trùng cặp khóa (maHDong, maP) trước khi nới mảng thêm vào cuối.
    public void them(CTHopDong ct) {
        if (timTheoKhoa(ct.getMaHDong(), ct.getMaP()) != null) {
            System.out.println("Phòng " + ct.getMaP() + " đã có trong hợp đồng " + ct.getMaHDong());
            return;
        }
        ds = Arrays.copyOf(ds, n + 1);
        ds[n] = new CTHopDong(ct);
        n++;
    }

    // Sửa & Xóa (không tham số & có tham số):
    // Theo quy định mục 2.9 và 6.5, danh sách chi tiết không cài đặt sua() và xoa() độc lập.

    public void xuat() {
        xuat(ds);
    }

    public void xuat(CTHopDong[] mang) {
        if (mang.length == 0) {
            System.out.println("Danh sách trống.");
            return;
        }
        for (CTHopDong ct : mang) {
            ct.xuat();
        }
    }

    // ================= HÀM GIAO TIẾP MODULE & TRA CỨU (mục 6.6) =================

    // Tìm chính xác 1 dòng chi tiết theo cặp khóa (maHDong, maP), không thấy trả về null
    public CTHopDong timTheoKhoa(String maHDong, String maP) {
        for (int i = 0; i < n; i++) {
            if (ds[i].getMaHDong().equalsIgnoreCase(maHDong)
                    && ds[i].getMaP().equalsIgnoreCase(maP)) {
                return ds[i];
            }
        }
        return null;
    }

    // Lấy theo hợp đồng (không tham số):
    public void layTheoHopDong() {
        String maHDong = NhapLieu.nhapChuoi("Nhập mã hợp đồng");
        xuat(layTheoHopDong(maHDong));
    }

    // Lấy theo hợp đồng (có tham số):
    public CTHopDong[] layTheoHopDong(String maHDong) {
        CTHopDong[] kq = new CTHopDong[0];
        for (int i = 0; i < n; i++) {
            if (ds[i].getMaHDong().equalsIgnoreCase(maHDong)) {
                kq = Arrays.copyOf(kq, kq.length + 1);
                kq[kq.length - 1] = ds[i];
            }
        }
        return kq;
    }

    // Lấy tất cả chi tiết hợp đồng của một phòng:
    public CTHopDong[] layTheoPhong(String maP) {
        CTHopDong[] kq = new CTHopDong[0];
        for (int i = 0; i < n; i++) {
            if (ds[i].getMaP().equalsIgnoreCase(maP)) {
                kq = Arrays.copyOf(kq, kq.length + 1);
                kq[kq.length - 1] = ds[i];
            }
        }
        return kq;
    }

    // ================= ĐỌC / GHI FILE =================

    // cthopdong.txt: maHDong|maP|donGia|slNguoi|thanhTien
    public void docFile() {
        ds = new CTHopDong[0];
        n = 0;
        for (String[] t : TepTin.doc(FILE)) {
            try {
                if (!t[0].matches("HDTP\\d+") || !t[1].matches("P\\d+")) {
                    throw new IllegalArgumentException();
                }
                them(new CTHopDong(
                        t[0],
                        t[1],
                        Double.parseDouble(t[2]),
                        Integer.parseInt(t[3]),
                        Double.parseDouble(t[4])
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