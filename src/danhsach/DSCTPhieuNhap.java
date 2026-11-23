package danhsach;

import core.DanhSach;
import core.TepTin;
import java.util.Arrays;
import model.CTPhieuNhap;

// danh sách chi tiết: không có mã riêng, không sửa, không xóa (mục 6.5)
public class DSCTPhieuNhap implements DanhSach<CTPhieuNhap> {

    private static final String FILE = "data/ctphieunhap.txt";
    private CTPhieuNhap[] ds = new CTPhieuNhap[0];
    private int n = 0;

    @Override
    public CTPhieuNhap[] getDs() {
        return ds;
    }

    @Override
    public int getN() {
        return n;
    }

    // chỉ có bản có tham số: chi tiết được tạo bên trong DSPhieuNhap.them()
    // 1 phiếu không có 2 dòng cùng mặt hàng: trùng cặp (maPN, maH) thì không thêm, trả false
    @Override
    public boolean them(CTPhieuNhap ct) {
        for (int i = 0; i < n; i++) {
            if (ds[i].getMaPN().equalsIgnoreCase(ct.getMaPN()) && ds[i].getMaH().equalsIgnoreCase(ct.getMaH())) {
                return false;
            }
        }
        ds = Arrays.copyOf(ds, n + 1);
        ds[n] = ct;
        n++;
        return true;
    }

    // lấy các mặt hàng của 1 phiếu (mục 6.6)
    public CTPhieuNhap[] layTheoPhieu(String maPN) {
        CTPhieuNhap[] kq = new CTPhieuNhap[0];
        for (int i = 0; i < n; i++) {
            if (ds[i].getMaPN().equalsIgnoreCase(maPN)) {
                kq = Arrays.copyOf(kq, kq.length + 1);
                kq[kq.length - 1] = ds[i];
            }
        }
        return kq;
    }

    @Override
    public void xuat() {
        if (n == 0) {
            System.out.println("Danh sách trống.");
            return;
        }
        for (int i = 0; i < n; i++) {
            ds[i].xuat();
        }
    }

    // ctphieunhap.txt: maPN|maH|sl|donGiaNhap|thanhTien
    @Override
    public void docFile() {
        ds = new CTPhieuNhap[0];
        n = 0;
        for (String[] t : TepTin.doc(FILE)) {
            try {
                them(new CTPhieuNhap(t[0], t[1], Integer.parseInt(t[2]),
                        Double.parseDouble(t[3]), Double.parseDouble(t[4])));
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
