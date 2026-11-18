package app;

import danhsach.DSCTPhieuNhap;
import danhsach.DSHangHoa;
import danhsach.DSNhaCC;
import danhsach.DSNhanVien;
import danhsach.DSPhieuNhap;

// giữ 1 bản duy nhất của mỗi danh sách, module nào thêm danh sách thì khai báo ở đây và gọi docFile() trong docTatCa()
public final class DuLieu {

    // ----- module 4: Nhân sự, Kho & Thống kê -----
    public static final DSNhanVien dsNhanVien = new DSNhanVien();
    public static final DSNhaCC dsNhaCC = new DSNhaCC();
    public static final DSHangHoa dsHangHoa = new DSHangHoa();
    public static final DSPhieuNhap dsPhieuNhap = new DSPhieuNhap();
    public static final DSCTPhieuNhap dsCTPhieuNhap = new DSCTPhieuNhap();

    private DuLieu() {
    }

    public static void docTatCa() {
        // ----- module 4 -----
        dsNhanVien.docFile();
        dsNhaCC.docFile();
        dsHangHoa.docFile();
        dsPhieuNhap.docFile();
        dsCTPhieuNhap.docFile();
    }
}
