package danhsach;

import core.DanhSach;
import core.NhapLieu;
import core.TepTin;
import java.util.Arrays;
import model.CTKhachO;

public class DSCTKhachO implements DanhSach<CTKhachO> {

    private static final String FILE = "data/ctkhacho.txt";
    private CTKhachO[] ds = new CTKhachO[0];
    private int n = 0;

    @Override
    public CTKhachO[] getDs() {
        return ds;
    }

    @Override
    public int getN() {
        return n;
    }

    // ================= CÁC HÀM BẮT BUỘC (Danh sách chi tiết - mục 2.9) =================

    // Thêm (có tham số):
    // Kiểm tra tránh trùng bộ 3 khóa (maHDong, maP, maKH) trước khi nới mảng thêm vào cuối.
    @Override
    public void them(CTKhachO ct) {
        if (timTheoKhoa(ct.getMaHDong(), ct.getMaP(), ct.getMaKH()) != null) {
            System.out.println("Khách " + ct.getMaKH() + " đã được ghi nhận ở phòng " + ct.getMaP() + " trong hợp đồng này.");
            return;
        }
        ds = Arrays.copyOf(ds, n + 1);
        ds[n] = new CTKhachO(ct);
        n++;
    }

    // Sửa & Xóa (không tham số & có tham số):
    // Theo quy định mục 2.9 và 6.5, danh sách chi tiết không cài đặt sua() và xoa().

    @Override
    public void xuat() {
        xuat(ds);
    }

    public void xuat(CTKhachO[] mang) {
        if (mang.length == 0) {
            System.out.println("Danh sách trống.");
            return;
        }
        for (CTKhachO ct : mang) {
            ct.xuat();
        }
    }

    // ================= HÀM GIAO TIẾP MODULE & TRA CỨU (mục 6.6) =================

    // Tìm chính xác 1 bản ghi theo bộ 3 khóa (maHDong, maP, maKH), không thấy trả về null
    public CTKhachO timTheoKhoa(String maHDong, String maP, String maKH) {
        for (int i = 0; i < n; i++) {
            if (ds[i].getMaHDong().equalsIgnoreCase(maHDong)
                    && ds[i].getMaP().equalsIgnoreCase(maP)
                    && ds[i].getMaKH().equalsIgnoreCase(maKH)) {
                return ds[i];
            }
        }
        return null;
    }

    // Lấy khách ở theo phòng trong hợp đồng (không tham số):
    public void layTheoPhong() {
        String maHDong = NhapLieu.nhapChuoi("Nhập mã hợp đồng");
        String maP = NhapLieu.nhapChuoi("Nhập mã phòng");
        xuat(layTheoPhong(maHDong, maP));
    }

    // Lấy khách ở theo phòng trong hợp đồng (có tham số):
    public CTKhachO[] layTheoPhong(String maHDong, String maP) {
        CTKhachO[] kq = new CTKhachO[0];
        for (int i = 0; i < n; i++) {
            if (ds[i].getMaHDong().equalsIgnoreCase(maHDong)
                    && ds[i].getMaP().equalsIgnoreCase(maP)) {
                kq = Arrays.copyOf(kq, kq.length + 1);
                kq[kq.length - 1] = ds[i];
            }
        }
        return kq;
    }

    // Lấy toàn bộ khách ở trong một hợp đồng
    public CTKhachO[] layTheoHopDong(String maHDong) {
        CTKhachO[] kq = new CTKhachO[0];
        for (int i = 0; i < n; i++) {
            if (ds[i].getMaHDong().equalsIgnoreCase(maHDong)) {
                kq = Arrays.copyOf(kq, kq.length + 1);
                kq[kq.length - 1] = ds[i];
            }
        }
        return kq;
    }

    // ================= ĐỌC / GHI FILE =================

    // ctkhacho.txt: maHDong|maP|maKH
    @Override
    public void docFile() {
        ds = new CTKhachO[0];
        n = 0;
        for (String[] t : TepTin.doc(FILE)) {
            try {
                them(new CTKhachO(t[0], t[1], t[2]));
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