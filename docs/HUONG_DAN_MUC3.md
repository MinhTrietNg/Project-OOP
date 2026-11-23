# Module 3 — Dịch vụ & Thanh toán

Tài liệu đọc kèm code của module 3: có gì, học thuộc theo khuôn nào, đọc code theo thứ tự nào, nghiệp vụ chạy ra sao, giải thích các đoạn khó, cách kiểm thử và chỗ nối với module khác.

Module 3 viết theo đúng 3 khuôn của module 4 (`HUONG_DAN_MUC4.md` mục 2), thêm 1 khuôn riêng cho nghiệp vụ (khuôn D). Mục 2 dưới đây chép lại các khuôn đó **bằng chính code của module 3**.

## 1. Module 3 gồm những file nào

| Tầng | File | Vai trò |
|---|---|---|
| `model/` | `DichVu` | Dịch vụ; `maH` trỏ tới hàng bị tiêu hao, `""` nếu không dùng hàng |
| | `CTSuDungDV` | 1 lần khách dùng dịch vụ trong 1 hợp đồng |
| | `HoaDon` | Hóa đơn lập lúc trả phòng, có hằng `CHUA_TT` / `DA_TT` |
| `danhsach/` | `DSDichVu` | Danh mục: thêm, sửa, xóa (có kiểm tra), tìm, `chon()` |
| | `DSCTSuDungDV` | Chi tiết + **nghiệp vụ ghi dịch vụ** (`ghiDichVu`) |
| | `DSHoaDon` | Chứng từ + **nghiệp vụ trả phòng / lập hóa đơn / thanh toán** |
| `menu/` | `MenuDichVu`, `MenuHoaDon` | Chỉ hiện menu và gọi hàm của danh sách |
| `app/` | `DuLieu` | Khai báo 3 danh sách của module 3 |
| `test/` | `KiemThuModule3` | 50 ca kiểm thử tự động |

3 lớp model đều `implements DoiTuong` (interface trong `core/`), nên đều phải có `nhap()`, `xuat()`, `toLine()`.

## 2. Học thuộc theo khuôn

Module 3 chỉ có **4 khuôn** lặp đi lặp lại. Thuộc 4 khuôn này là viết lại được gần hết module.

### Khuôn A — lớp model (3 lớp giống nhau)

```text
(hằng public static final nếu có trạng thái: HoaDon.CHUA_TT, HoaDon.DA_TT)
thuộc tính private
3 constructor: không tham số / đầy đủ / sao chép (nhận 1 đối tượng cùng lớp)
get / set
vài hàm tính nhỏ của riêng lớp (xem bảng dưới)
nhap()   : chỉ hỏi các trường người dùng gõ (không hỏi mã, mã liên kết, trường tính toán)
xuat()   : 1 dòng System.out.println(a + " | " + b + ...)
toLine() : DinhDang.dong(các trường theo đúng thứ tự file)
```

Phần **khác nhau** giữa 3 lớp chỉ nằm ở `nhap()` và các hàm tính nhỏ:

| Lớp | `nhap()` hỏi gì | Hàm tính riêng |
|---|---|---|
| `DichVu` | `tenDV`, `donGia` (`maH` do `DSDichVu` cho chọn rồi `setMaH`) | `coDungHang()` = `!maH.isEmpty()` |
| `CTSuDungDV` | `ngaySD`, `sl`. Có **2 bản** `nhap()` và `nhap(tu, den)` (nạp chồng) | `tinhThanhTien()`; `static trongKhoang(ngay, tu, den)` |
| `HoaDon` | Không hỏi gì, chỉ gán `ngayLapHD = hôm nay` | `tinhTien(cacPhong, cacDV)`, `soConPhaiTra(tienCoc)`, `daThanhToan()` |

Mẫu ngắn nhất để học thuộc là `DichVu`:

```java
public class DichVu implements DoiTuong {
    private static final Scanner sc = new Scanner(System.in, "UTF-8");   // Scanner riêng của lớp
    private String maDV;
    private String tenDV;
    private double donGia;
    private String maH;      // "" nếu không dùng hàng

    public DichVu() { ... gán "" và 0 ... }
    public DichVu(String maDV, String tenDV, double donGia, String maH) { ... }
    public DichVu(DichVu khac) { this.maDV = khac.maDV; ... }   // sao chép

    // get / set ...

    public boolean coDungHang() { return !maH.isEmpty(); }

    @Override public void nhap() {
        tenDV = NhapLieu.nhapChuoi(sc, "Tên dịch vụ");
        donGia = NhapLieu.nhapSoThuc(sc, "Đơn giá", 1);
    }
    @Override public void xuat()     { System.out.println(maDV + " | " + tenDV + " | " + ...); }
    @Override public String toLine() { return DinhDang.dong(maDV, tenDV, donGia, maH); }
}
```

### Khuôn B — lớp danh sách (3 dạng)

Phần **luôn giống nhau** ở cả 3 lớp:

```text
sc = new Scanner(System.in, "UTF-8") (Scanner riêng của lớp);  FILE = "data/xxx.txt";
X[] ds = new X[0];  int n = 0;  getDs(), getN()
them(x)   -> trùng mã thì return false; ds = Arrays.copyOf(ds, n + 1); ds[n] = x; n++; return true
             (DSCTSuDungDV không chặn trùng: 1 hợp đồng dùng cùng dịch vụ nhiều lần, nhiều ngày)
xuat()    -> rỗng thì "Danh sách trống." rồi return; không thì for in từng phần tử
docFile() -> ds rỗng, n = 0; for (String[] t : TepTin.doc(FILE)) { try { them(new X(t[0], ...)); }
             catch (Exception e) { in "Bỏ qua 1 dòng lỗi" } }
ghiFile() -> gom toLine() vào mảng String, TepTin.ghi(FILE, mảng)
```

Phần **khác nhau** tùy dạng danh sách:

| Hàm | `DSDichVu` (danh mục) | `DSCTSuDungDV` (chi tiết) | `DSHoaDon` (chứng từ) |
|---|---|---|---|
| `them()` | Nhập, chặn trùng tên, chọn hàng, sinh mã | — (thay bằng `ghiDichVu()`) | **Cả quy trình trả phòng** (mục 4.2) |
| `timTheoMa()`, `timTheoMa(ma)` | ✓ | — (không có mã) | ✓ (bản không tham số in luôn chi tiết) |
| `layTheo...` / `timTheo...` | `timTheoTen` | `layTheoHopDong(maHDong)` | `timTheoHopDong(maHDong)` |
| `sua()`, `sua(ma)` | ✓ qua bản tạm | Không sửa | Không sửa tiền; chỉ `thanhToan()` đổi trạng thái |
| `xoa()`, `xoa(ma)` | ✓ có kiểm tra tham chiếu | Không xóa | Không xóa |
| `sinhMa()` | `"DV%03d"`, `substring(2)` | — | `"HDON%03d"`, `substring(4)` |
| `chon()` | ✓ (cho `ghiDichVu` dùng) | — | — |

Quy tắc nhớ: **danh mục** thì đủ thêm/sửa/xóa/tìm; **chi tiết** và **chứng từ** thì chỉ thêm và tra cứu, vì đã ghi là thành lịch sử (`MO_TA_DO_AN.md` mục 6.5).

Thứ tự hàm trong mỗi lớp được chia bằng các dòng chú thích, đọc từ trên xuống là thấy:

```text
// ================= CÁC HÀM BẮT BUỘC (gói 50%) =================
// ================= GHI DỊCH VỤ / TRẢ PHÒNG / THANH TOÁN (nghiệp vụ) =================
// ================= HÀM NÂNG CAO (gói 70%) =================
// ================= ĐỌC / GHI FILE =================
```

### Khuôn C — hàm lọc / thống kê

Cùng khuôn với module 4. Trong module 3 có 3 hàm lọc và 1 hàm thống kê:

```java
// LỌC: dsChuaThanhToan, locTheoKhoangNgay (DSHoaDon), layTheoHopDong (DSCTSuDungDV)
HoaDon[] kq = new HoaDon[0];
for (int i = 0; i < n; i++) {
    if (!ds[i].daThanhToan()) {                 // đổi điều kiện là ra hàm lọc khác
        kq = Arrays.copyOf(kq, kq.length + 1);
        kq[kq.length - 1] = ds[i];
    }
}
return kq;

// THỐNG KÊ THEO NHÓM: DSCTSuDungDV.thongKeTheoDichVu
for (DichVu dv : DuLieu.dsDichVu.getDs()) {     // vòng ngoài: từng dịch vụ
    int soLuot = 0; int tongSL = 0; double tong = 0;
    for (int i = 0; i < n; i++) {                // vòng trong: cộng dồn các lần dùng
        if (cùng maDV && hợp đồng đã có hóa đơn DA_TT) { soLuot++; tongSL += sl; tong += thanhTien; }
    }
    System.out.println(dv.getTenDV() + ": " + soLuot + " lượt, " + ...);
}
```

### Khuôn D — nghiệp vụ 2 tầng (hàm hỏi + hàm làm)

Ghi dịch vụ và thanh toán có **cặp hàm cùng tên** (nạp chồng):

```text
hàm KHÔNG tham số (hỏi)          hàm CÓ tham số (làm)
-------------------------------  ------------------------------------------------
nhập mã, tìm, kiểm tra, chọn     kiểm tra lại các điều kiện quan trọng
gặp sai thì báo rồi return       sai thì báo và trả false, KHÔNG đổi gì
đủ thứ rồi mới gọi hàm làm  -->  đổi dữ liệu (trừ kho, thêm chi tiết, đổi trạng thái...)
in kết quả                       trả true
```

| Nghiệp vụ | Hàm hỏi | Hàm làm |
|---|---|---|
| Ghi dịch vụ | `DSCTSuDungDV.ghiDichVu()` | `ghiDichVu(hd, dv, ngaySD, sl)` → `boolean` |
| Thanh toán | `DSHoaDon.thanhToan()` | `thanhToan(ma)` → `boolean` |

Hàm làm không gõ phím nên **kiểm thử gọi thẳng được**. Trả phòng, lập hóa đơn thì không tách: cả quy trình nằm trong `DSHoaDon.them()` (mục 4.2), giống `DSHDThuePhong.them()` (module 2) và `DSPhieuNhap.them()` (module 4).

## 3. Đọc code theo thứ tự này

1. `model/DichVu.java` — khuôn A ngắn nhất, chú ý `maH = ""` và `coDungHang()`.
2. `model/CTSuDungDV.java` — 2 bản `nhap` (nạp chồng), hàm `static trongKhoang`, `tinhThanhTien()`.
3. `model/HoaDon.java` — hằng `CHUA_TT`/`DA_TT`, `tinhTien(cacPhong, cacDV)`, `soConPhaiTra(tienCoc)`.
4. `danhsach/DSDichVu.java` — khuôn B dạng danh mục, gần như nguyên mẫu `DSKhachHang` trong `MO_TA_DO_AN.md` mục 2.9; thêm `chonMaHang()` và `xoa` có kiểm tra tham chiếu.
5. `danhsach/DSCTSuDungDV.java` — **nghiệp vụ ghi dịch vụ**: `ghiDichVu()` → `tam.nhap(tu, den)` → `ghiDichVu(hd, dv, ngaySD, sl)`.
6. `danhsach/DSHoaDon.java` — **nghiệp vụ trả phòng**: `them()` (B1 → B3); rồi `thanhToan()` → `thanhToan(ma)`; rồi `xuatChiTiet(maHDon)`.
7. `danhsach/DSHangHoa.java` hàm `truKho` (module 4) và `danhsach/DSPhong.java` hàm `capNhatTinhTrang` (module 1) — 2 hàm module 3 gọi sang.
8. `menu/MenuDichVu.java`, `menu/MenuHoaDon.java` — chỉ gọi hàm và `ghiFile()`, không có logic.
9. `test/KiemThuModule3.java` — đọc để thấy từng hàm có tham số được gọi thế nào, và `them()` chạy với bàn phím giả lập.

## 4. Nghiệp vụ

### 4.1 Ghi dịch vụ — `DSCTSuDungDV.ghiDichVu()`

```text
B1  nhập mã hợp đồng -> phải tồn tại và CHƯA có hóa đơn -> chọn dịch vụ (0 = hủy)
B2  nhập ngày dùng (bắt nằm trong ngày nhận -> ngày trả, tính cả 2 biên) và số lượng (1..1000)
B3  ghiDichVu(hd, dv, ngaySD, sl):
      kiểm tra lại hóa đơn, ngày
      dịch vụ có maH -> DuLieu.dsHangHoa.truKho(maH, sl); false = không đủ hàng -> DỪNG, không ghi
      chép donGia từ DichVu, tính thanhTien = sl x donGia, thêm chi tiết
```

Trừ kho **trước** rồi mới lưu chi tiết. `truKho` không trừ gì khi trả `false`, nên thiếu hàng thì kho và danh sách đều giữ nguyên. Menu ghi 2 file: `ctsudungdv.txt` và `hanghoa.txt`.

### 4.2 Trả phòng & lập hóa đơn — `DSHoaDon.them()`

```text
B1  nhập mã hợp đồng -> phải tồn tại và CHƯA có hóa đơn
    hôm nay còn trước ngày trả trong hợp đồng -> nhắc sửa ngày trả bên Thuê phòng trước (có thể bỏ qua)
    chọn nhân viên lập (0 = hủy)
B2  hdon = new HoaDon(); set mã, mã hợp đồng, mã NV; hdon.tinhTien(cacPhong, cacDV); them(hdon)
      tongTienThue = cộng thanhTien các CTHopDong
      tongTienDV   = cộng thanhTien các CTSuDungDV của hợp đồng
      tongCong     = tongTienThue + tongTienDV, trạng thái CHUA_TT, ngày lập = hôm nay
B3  trả mọi phòng của hợp đồng về TRONG: DuLieu.dsPhong.capNhatTinhTrang(maP, Phong.TRONG)
    rồi in hóa đơn vừa lập: xuatChiTiet(maHDon)
```

Lưu ý tên hàm: `them()` (không tham số) là **cả quy trình trả phòng** ở trên; `them(HoaDon x)` (có tham số) chỉ **nới mảng ra 1 rồi đặt `x` vào cuối** (trùng mã thì trả `false`), dùng ở B2 và trong `docFile`. Menu ghi 2 file: `hoadon.txt` và `phong.txt`.

In hóa đơn (`xuatChiTiet`) hiện từng phòng, từng dịch vụ, tổng cộng, tiền cọc và **còn phải trả** = `tongCong − tienCoc`. Số này không lưu vào file (mục 7 của mô tả đồ án).

### 4.3 Thanh toán — `DSHoaDon.thanhToan()`

In hóa đơn, hỏi xác nhận, đổi `CHUA_TT` → `DA_TT` (trả lời `k` thì báo "Đã hủy thanh toán."). Hóa đơn đã thanh toán thì báo và không đổi gì. Đây là thao tác "sửa" duy nhất của hóa đơn; tiền trên hóa đơn không sửa tay được.

### 4.4 Ràng buộc đã cài

| Ràng buộc | Ở đâu |
|---|---|
| Hợp đồng đã có hóa đơn thì không ghi dịch vụ, không lập hóa đơn lần 2 | `ghiDichVu`, `DSHoaDon.them` (dùng `timTheoHopDong`) |
| Ngày dùng dịch vụ trong khoảng nhận – trả | `CTSuDungDV.nhap(tu, den)` + kiểm tra lại trong `ghiDichVu` |
| Không đủ hàng thì không ghi dịch vụ | `ghiDichVu` + `DSHangHoa.truKho` |
| Đổi giá dịch vụ không làm đổi tiền đã ghi | `CTSuDungDV.donGia` chép lúc dùng |
| Dịch vụ đã có khách dùng thì không xóa | `DSDichVu.xoa(ma)` |
| Không xóa chi tiết sử dụng dịch vụ, không xóa hóa đơn | Không có hàm `xoa` |

## 5. Phần nào thuộc gói nào

**Gói 50%**: 3 lớp model; `DSDichVu` đủ `them`, `sua`, `xoa`, `timTheoMa`, `xuat`, `sinhMa`, `docFile`, `ghiFile`; ghi dịch vụ có trừ kho; lập hóa đơn, thanh toán; `layTheoHopDong`, `timTheoHopDong` (mục 6.6 mô tả đồ án).

**Gói 70%**:

| Hàm | Ở đâu | Ý chính |
|---|---|---|
| Tìm dịch vụ không dấu | `DSDichVu.timTheoTen` | Dùng lại `core.TimKiem` |
| Chặn trùng tên dịch vụ | `DSDichVu.trungTen(ten, maBoQua)` | `them()` truyền `""`; `sua(ma)` nhập vào bản tạm, truyền `ma` để bỏ qua chính dịch vụ đang sửa |
| Xem dịch vụ đã dùng của 1 hợp đồng | `DSCTSuDungDV.xuatTheoHopDong` | Kèm tên dịch vụ và tổng tiền |
| Thống kê theo dịch vụ (số lượt, số lượng, tiền) | `DSCTSuDungDV.thongKeTheoDichVu` | Khuôn C; chỉ cộng hợp đồng có hóa đơn `DA_TT` (giống các thống kê doanh thu khác) |
| In hóa đơn đầy đủ: phòng, dịch vụ, cọc, còn phải trả / trả lại khách | `DSHoaDon.xuatChiTiet` | `conLai = soConPhaiTra(coc)`; âm (cọc dư) thì in `Trả lại khách` thay vì số âm |
| Hóa đơn chưa thanh toán + tổng nợ | `DSHoaDon.dsChuaThanhToan` | Khuôn C |
| Lọc hóa đơn theo khoảng ngày | `DSHoaDon.locTheoKhoangNgay` | `isBefore` / `isAfter` |
| Nhắc sửa ngày trả khi khách trả sớm | `DSHoaDon.them` | |
| Bỏ qua dòng file lỗi | `docFile()` | `try { them(...); } catch (Exception e) { ... }` |

## 6. Giải thích các đoạn khó

### 6.1 Trừ kho trước, lưu chi tiết sau

```java
if (dv.coDungHang() && !DuLieu.dsHangHoa.truKho(dv.getMaH(), sl)) {
    System.out.println("Kho không đủ hàng ...");
    return false;                       // chưa thêm chi tiết nào, kho cũng chưa bị trừ
}
CTSuDungDV ct = new CTSuDungDV(hd.getMaHDong(), dv.getMaDV(), ngaySD, sl, dv.getDonGia(), 0);
ct.tinhThanhTien();
them(ct);
```

- `&&` chạy **ngắn mạch**: dịch vụ không dùng hàng (giặt ủi) thì `coDungHang()` là `false`, Java không gọi `truKho` nữa.
- `truKho` (module 4) tự kiểm tra `slTon < sl` rồi mới trừ. Nên chỉ cần **1 lệnh `if`** là vừa kiểm tra vừa trừ, không cần hàm "kiểm tra đủ hàng" riêng.
- Nếu làm ngược (thêm chi tiết trước, trừ kho sau) thì khi thiếu hàng phải **xóa dòng vừa thêm**, trong khi danh sách chi tiết không có hàm xóa.
- Đúng bằng số tồn vẫn ghi được (`7 < 7` là sai nên không bị chặn), kho về 0. Có ca kiểm thử riêng cho chỗ này.

### 6.2 `them(x)` cất thẳng đối tượng, không chép

```java
HoaDon hdon = new HoaDon();   // ngày lập = hôm nay, CHUA_TT (constructor không tham số đã gán)
...
them(hdon);                   // ds[n] = hdon: mảng giữ đúng đối tượng này
...
xuatChiTiet(hdon.getMaHDon());
```

`them(x)` không tạo bản sao, nên `hdon` và phần tử cuối mảng là **cùng 1 đối tượng**; thêm xong vẫn dùng `hdon` được. `ghiDichVu()` in `ds[n - 1]` chỉ vì đó là phần tử vừa thêm (luôn ở cuối mảng).

### 6.3 Đơn giá và tiền được "chốt" lúc lập

- `CTSuDungDV.donGia` chép từ `DichVu.donGia` **lúc khách dùng**. Sau đó sửa giá dịch vụ (`DSDichVu.sua`) thì chỉ ảnh hưởng lần dùng sau.
- `HoaDon` lưu cả `tongTienThue`, `tongTienDV`, `tongCong`, tính 1 lần trong `tinhTien(...)`. Mở lại chương trình thì đọc thẳng từ file, không tính lại.
- Ngược lại, **còn phải trả** không lưu: nó tính được ngay từ `tongCong` và `tienCoc` (`MO_TA_DO_AN.md` mục 7).

### 6.4 Nạp chồng `nhap()` và hàm `static trongKhoang`

```java
public void nhap()                          { ngaySD = nhapNgay(...); nhapSoLuong(); }   // bắt buộc theo DoiTuong
public void nhap(LocalDate tu, LocalDate den) { lặp tới khi trongKhoang(ngaySD, tu, den); nhapSoLuong(); }

public static boolean trongKhoang(LocalDate ngay, LocalDate tu, LocalDate den) {
    return !ngay.isBefore(tu) && !ngay.isAfter(den);      // tính cả 2 ngày biên
}
```

- 2 hàm cùng tên khác tham số là **nạp chồng (overload)**. `nhap()` phải có vì interface `DoiTuong` bắt buộc; `nhap(tu, den)` là bản thật sự dùng khi ghi dịch vụ.
- `trongKhoang` là `static` vì nó không đọc thuộc tính nào của đối tượng, chỉ so 3 ngày truyền vào. Nhờ vậy `ghiDichVu(hd, dv, ngaySD, sl)` gọi `CTSuDungDV.trongKhoang(...)` để kiểm tra lại mà không cần tạo đối tượng.
- Viết `!isBefore && !isAfter` thay vì `isAfter && isBefore` để **2 ngày biên vẫn hợp lệ** (khách dùng dịch vụ ngay ngày nhận hoặc ngày trả).

### 6.5 `timTheoHopDong` là "khóa" của hợp đồng

Mỗi hợp đồng có tối đa 1 hóa đơn. `DuLieu.dsHoaDon.timTheoHopDong(maHDong) != null` nghĩa là hợp đồng **đã chốt**. Một hàm này dùng cho 4 chỗ:

| Chỗ gọi | Đã có hóa đơn thì |
|---|---|
| `DSCTSuDungDV.ghiDichVu` (cả 2 bản) | Không ghi thêm dịch vụ |
| `DSHoaDon.them` | Không lập hóa đơn lần 2 |
| Module 2, sửa ngày trả phòng | Không cho sửa |
| `thongKeTheoDichVu` | Lấy hóa đơn ra xem đã `DA_TT` chưa |

Hàm có tham số **kiểm tra lại** dù hàm không tham số đã kiểm, vì kiểm thử và module khác gọi thẳng hàm có tham số, bỏ qua bước hỏi.

### 6.6 Còn phải trả và trả lại khách

```java
double conLai = hdon.soConPhaiTra(hd.getTienCoc());    // tongCong - tienCoc
if (conLai >= 0) in "Còn phải trả: ..."
else             in "Trả lại khách: " + DinhDang.tien(-conLai)
```

Khách cọc nhiều hơn tổng tiền (ở 1 đêm, cọc 1 triệu) thì `conLai` âm; in "Còn phải trả: -200.000 đ" thì khó hiểu nên đổi dấu và in "Trả lại khách". Tiền cọc nằm ở hợp đồng (module 2) nên `xuatChiTiet` phải tra `DuLieu.dsHDThuePhong.timTheoMa(...)`.

### 6.7 Vì sao `xuatChiTiet` không kiểm tra `null` khi tra nhân viên, dịch vụ

`DuLieu.dsNhanVien.timTheoMa(hdon.getMaNV())` và `DuLieu.dsDichVu.timTheoMa(ct.getMaDV())` dùng luôn kết quả, không `if (nv == null)`. Lý do: nhân viên đã lập hóa đơn và dịch vụ đã có khách dùng **không xóa được** (`DSNhanVien.xoa` duyệt `dsHoaDon`, `DSDichVu.xoa` duyệt `dsCTSuDungDV`). Ràng buộc xóa ở mục 6.5 mô tả đồ án bảo đảm mã liên kết luôn tìm thấy. Điều kiện là không sửa tay file trong `data/`.

### 6.8 Sinh mã hóa đơn

`"HDON012".substring(4)` → `"012"` → `12`. Tiền tố `HDON` dài 4 nên cắt từ vị trí 4; dịch vụ `DV` dài 2 nên `substring(2)`. Lấy số lớn nhất cộng 1, không dùng `n + 1`. Hóa đơn không bao giờ bị xóa nên mã hóa đơn không bao giờ bị dùng lại. Dịch vụ thì có thể: xóa `DV004` (đang lớn nhất, chưa ai dùng) thì mã kế tiếp là lại `DV004`. Việc này không sao, vì dịch vụ chỉ xóa được khi không còn dòng nào trỏ tới mã đó.

### 6.9 Menu ghi những file nào

Hàm của danh sách **không tự ghi file**; menu ghi sau mỗi thao tác (`MO_TA_DO_AN.md` mục 2.7). Thao tác nào đổi dữ liệu của module khác thì menu ghi thêm file của module đó:

| Thao tác | File được ghi |
|---|---|
| Thêm / sửa / xóa dịch vụ | `dichvu.txt` |
| Ghi dịch vụ khách dùng | `ctsudungdv.txt` + `hanghoa.txt` (vì trừ kho) |
| Trả phòng & lập hóa đơn | `hoadon.txt` + `phong.txt` (vì phòng về `TRONG`) |
| Thanh toán | `hoadon.txt` |

## 7. Kiểm thử

### Chạy kiểm thử

Từ thư mục gốc project:

```powershell
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src, test -Filter *.java).FullName
java -cp out KiemThuModule3
```

Kết quả mong đợi: `Kết quả: 50 đạt, 0 trượt`. Lớp test chỉ dùng dữ liệu trong bộ nhớ (không đọc/ghi file), tự tạo `HDThuePhong`, `CTHopDong` bằng constructor. Ghi dịch vụ, thanh toán gọi thẳng hàm có tham số; lập hóa đơn gọi đúng `DSHoaDon.them()` với **bàn phím giả lập**: ngay đầu `main`, trước lần đầu dùng `NhapLieu` (Scanner chung chỉ tạo 1 lần), test gọi `System.setIn(new ByteArrayInputStream(...))` với chuỗi phím gõ sẵn.

Các ca được kiểm:

| Nhóm | Ca |
|---|---|
| Sinh mã, file | Mã = số lớn nhất + 1, danh sách rỗng ra `HDON001`, sau khi xóa; `toLine` dịch vụ không dùng hàng kết thúc bằng `\|` và tách lại vẫn đủ 4 trường; `toLine` hóa đơn, chi tiết đúng thứ tự và ngày `dd/MM/yyyy` |
| Model | `coDungHang`, `trongKhoang` tính cả 2 biên |
| Danh mục | Tìm không dấu, trùng tên (kể cả bỏ qua chính dịch vụ đang sửa) |
| Ghi dịch vụ | Đủ hàng (kho giảm đúng, chép giá, tính tiền); thiếu hàng (trả `false`, kho giữ nguyên, không thêm dòng); **đúng bằng tồn**; ngày ngoài khoảng (không trừ kho); không dùng hàng; giá chốt khi đổi giá dịch vụ; `layTheoHopDong`, `tongSoLuong` |
| Hóa đơn | Tiền phòng = Σ đơn giá × số ngày, tiền dịch vụ, tổng cộng, `CHUA_TT`, ngày lập hôm nay, còn phải trả; chặn lập lần 2; chặn ghi dịch vụ sau khi có hóa đơn |
| Thanh toán | Lần 1 `true` → `DA_TT`; lần 2 `false`; mã sai `false`; danh sách chưa thanh toán giảm theo |
| Lọc, xóa | Lọc theo khoảng ngày; không xóa được dịch vụ đã dùng, hàng đang gắn dịch vụ, nhân viên đã lập hóa đơn |

### Chạy chương trình

Nạp dữ liệu mẫu rồi chạy (dữ liệu mẫu có **HDTP022** đang ở, chưa có hóa đơn, dùng để thử module 3):

```powershell
Copy-Item data_mau\*.txt data\ -Force
java -cp out app.Main
```

Thử nhanh đúng các thông báo thật:

| Gõ | Thấy |
|---|---|
| `4 → 7` → "HDTP022" → "DV003" → "19/11/2026" → "6" | `Kho không đủ hàng HH003 cho dịch vụ này, không ghi được.` (Mì ly chỉ còn 5) |
| `4 → 7` → "HDTP022" → "DV001" → "25/11/2026" | `Ngày sử dụng phải từ 17/11/2026 đến 20/11/2026.` rồi hỏi lại |
| … → "19/11/2026" → "2" | `Đã ghi Nước suối cho hợp đồng HDTP022:` `HDTP022 \| DV001 \| 19/11/2026 \| 2 x 15.000 đ = 30.000 đ` |
| `0 → 5 → 1` → "HDTP022" → "NV001" | `Đã lập hóa đơn HDON022:` rồi in hóa đơn đầy đủ, `Còn phải trả: ...` |
| `5 → 2` → "HDON022" → "c" | `Đã thanh toán hóa đơn HDON022` |
| `5 → 2` → "HDON022" | `Hóa đơn HDON022 đã thanh toán rồi.` |
| `0 → 4 → 7` → "HDTP022" | `Hợp đồng đã lập hóa đơn, không ghi thêm dịch vụ được.` |

Kịch bản đầy đủ cho buổi demo: `docs/KICH_BAN_DEMO.md` mục 3.4, 3.5.

### Phản biện logic đã làm

| Vấn đề tìm ra | Hậu quả | Xử lý |
|---|---|---|
| Thêm chi tiết trước rồi mới trừ kho | Thiếu hàng thì phải xóa dòng vừa thêm, mà chi tiết không có hàm xóa | Trừ kho trước, `truKho` trả `false` thì dừng (mục 6.1) |
| Chỉ kiểm tra ngày trong `nhap(tu, den)` | Gọi thẳng `ghiDichVu(hd, dv, ngaySD, sl)` thì ngày sai vẫn lọt | Hàm có tham số kiểm tra lại ngày và hóa đơn |
| Ghi dịch vụ sau khi đã lập hóa đơn | Tiền dịch vụ mới không nằm trong hóa đơn nào | Chặn bằng `timTheoHopDong` |
| Sửa giá dịch vụ khi khách đã dùng | Hóa đơn cũ có thể bị đổi tiền | Không ảnh hưởng: `CTSuDungDV` đã chép `donGia` |
| Số lượng nhập quá lớn | `sl × donGia` và trừ kho ra số vô lý | `nhapSoLuong` chặn 1..1000 |
| Khách trả phòng sớm hơn hợp đồng | Tiền phòng tính theo số ngày cũ, tính thừa | `DSHoaDon.them` nhắc sửa ngày trả ở menu Thuê phòng trước (vẫn cho bỏ qua) |
| Cọc lớn hơn tổng tiền | In số còn phải trả âm | In `Trả lại khách: ...` |
| Thống kê dịch vụ cộng cả hợp đồng chưa thanh toán | Lệch với thống kê doanh thu | Chỉ cộng hợp đồng có hóa đơn `DA_TT` |
| Thanh toán 2 lần | | Báo "đã thanh toán rồi", không đổi gì |
| Menu vẫn ghi file khi người dùng hủy giữa chừng | Không sai, file được ghi lại y nguyên | Giữ nguyên cho code menu đơn giản (giống các module khác) |

## 8. Liên hệ với module khác

Module 3 gọi thẳng danh sách của module khác qua `DuLieu`, không có hàm bọc trung gian:

| Module 3 gọi | Ở đâu |
|---|---|
| `DuLieu.dsHDThuePhong.timTheoMa(maHDong)` | `DSCTSuDungDV.ghiDichVu()`, `DSHoaDon.them()`, `DSHoaDon.xuatChiTiet` (lấy tiền cọc) |
| `DuLieu.dsCTHopDong.layTheoHopDong(maHDong)` | `DSHoaDon.them()`, `DSHoaDon.xuatChiTiet` |
| `DuLieu.dsPhong.capNhatTinhTrang(maP, Phong.TRONG)` | `DSHoaDon.them()` (trả phòng) |
| `DuLieu.dsHangHoa.truKho(maH, sl)` | `DSCTSuDungDV.ghiDichVu(hd, dv, ngaySD, sl)` |
| `DuLieu.dsHangHoa.chon()` | `DSDichVu.chonMaHang()` (gắn hàng tiêu hao cho dịch vụ) |
| `DuLieu.dsNhanVien.chon()`, `timTheoMa(maNV)` | `DSHoaDon.them()`, `DSHoaDon.xuatChiTiet` |

Module khác dùng từ module 3:

- `DuLieu.dsHoaDon.timTheoHopDong(maHDong)`: khác `null` thì hợp đồng đã có hóa đơn, module 2 không cho sửa ngày trả.
- `DSNhanVien.xoa` duyệt `DuLieu.dsHoaDon.getDs()`; `DSHangHoa.xoa` duyệt `DuLieu.dsDichVu.getDs()`.
- `MenuThongKe` (module 4) tính doanh thu từ các hóa đơn `DA_TT`; mục "Doanh thu theo dịch vụ" gọi `DSCTSuDungDV.thongKeTheoDichVu`.
- Khi xóa khách hàng / phòng không cần kiểm tra bên module 3 (hóa đơn chỉ giữ `maHDong`).

## 9. Câu hỏi vấn đáp hay gặp

- **Vì sao `CTSuDungDV` lưu `donGia` mà không lấy `DichVu.donGia`?** Giá dịch vụ có thể đổi; hóa đơn cũ phải giữ đúng giá lúc khách dùng.
- **Vì sao lưu `tongTienThue`, `tongCong` vào file mà không tính lại?** Hóa đơn là chứng từ, chốt số tiền lúc lập (mục 7 mô tả đồ án).
- **Vì sao "còn phải trả" không lưu?** Tính được từ `tongCong` và `tienCoc`; lưu thêm thì có 2 chỗ giữ cùng 1 con số, dễ lệch.
- **Vì sao trừ kho trước rồi mới lưu chi tiết?** `truKho` tự kiểm tra đủ hàng; thiếu thì trả `false` và không đổi gì, nên dừng lại là sạch, không phải hoàn tác.
- **Vì sao `ghiDichVu`, `thanhToan` có bản có tham số?** Để kiểm thử tự động và để module khác gọi được mà không phải gõ phím.
- **Hàm có tham số đã kiểm tra rồi, sao bản không tham số còn kiểm tra?** Bản không tham số kiểm tra **sớm** để không bắt người dùng gõ hết rồi mới báo lỗi; bản có tham số kiểm tra **lại** vì có thể bị gọi thẳng.
- **Đa hình ở đâu?** 3 lớp model cài interface `DoiTuong` và `@Override` `nhap`, `xuat`, `toLine`. `CTSuDungDV` có 2 hàm `nhap` (nạp chồng): `nhap()` và `nhap(tu, den)`.
- **`static` ở đâu trong module 3?** Hằng `HoaDon.CHUA_TT`, `HoaDon.DA_TT`; hàm `CTSuDungDV.trongKhoang` (không dùng thuộc tính nào); 3 danh sách `DuLieu.dsDichVu`, `dsCTSuDungDV`, `dsHoaDon`.
- **Vì sao hóa đơn không có hàm sửa, hàm xóa?** Là chứng từ: sửa hoặc xóa thì doanh thu cũ bị đổi. Thao tác duy nhất là thanh toán (`CHUA_TT` → `DA_TT`).
- **`HoaDon.nhap()` không hỏi gì thì viết ra làm gì?** Interface `DoiTuong` bắt buộc có. Mọi trường của hóa đơn đều tự sinh, tự chọn hoặc tự tính, nên `nhap()` chỉ gán ngày lập là hôm nay.
- **Dịch vụ không dùng hàng lưu file thế nào?** `maH = ""`, dòng kết thúc bằng `|` (vd `DV004|Giặt ủi|50000|`); `TepTin.doc` vẫn tách ra đủ 4 trường, trường cuối rỗng.
- **Trả phòng thì phòng về `TRONG` ở đâu?** B3 cuối `DSHoaDon.them()`: duyệt các `CTHopDong` của hợp đồng, gọi `DuLieu.dsPhong.capNhatTinhTrang(maP, Phong.TRONG)`; `MenuHoaDon` ghi thêm `phong.txt`.
- **Khách dùng dịch vụ ngay ngày nhận phòng có được không?** Được: `trongKhoang` tính cả 2 ngày biên.
