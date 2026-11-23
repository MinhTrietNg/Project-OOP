package core;

// khung chung của 13 lớp danh sách: lớp nào cũng quản lý 1 mảng T[] và đọc/ghi 1 file
// T là lớp đối tượng được quản lý, vd DSKhachHang implements DanhSach<KhachHang>
public interface DanhSach<T> {

    // mảng đang giữ (đúng getN() phần tử)
    T[] getDs();

    // số phần tử
    int getN();

    // thêm 1 phần tử đã có sẵn (đọc file, module khác tạo ra); trùng khóa thì không thêm, trả false
    boolean them(T x);

    // in toàn bộ danh sách, mỗi phần tử 1 dòng
    void xuat();

    // đọc file data/xxx.txt vào mảng
    void docFile();

    // ghi toàn bộ mảng ra file data/xxx.txt
    void ghiFile();
}
