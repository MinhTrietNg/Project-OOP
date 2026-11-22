# Module 4 — Nhân sự, Kho & Thống kê

Tài liệu đọc kèm code của module 4: có gì, học theo khuôn nào, các hàm khó chạy ra sao, cách kiểm thử và cần làm gì khi ghép với 3 module còn lại.

## 1. Module 4 gồm những file nào

| Tầng | File | Vai trò |
|---|---|---|
| `model/` | `NhanVien` | Nhân viên, **kế thừa** `Nguoi` (hoTen, sdt) |
| | `NhaCC` | Nhà cung cấp |
| | `HangHoa` | Hàng trong kho, có `slTon` |
| | `PhieuNhap` | Đầu phiếu nhập (ai nhập, của ai, ngày nào, tổng tiền) |
| | `CTPhieuNhap` | Từng dòng hàng trong phiếu |
| `danhsach/` | `DSNhanVien`, `DSNhaCC`, `DSHangHoa`, `DSPhieuNhap`, `DSCTPhieuNhap` | Mỗi lớp quản lý 1 mảng + đọc/ghi 1 file |
| `menu/` | `MenuNhanVien`, `MenuKho`, `MenuThongKe` | Chỉ hiện menu và gọi hàm của danh sách |
| `core/` | `TepTin` | Đọc/ghi file dùng chung cho 5 danh sách |
| | `TimKiem` | Tìm theo tên không phân biệt dấu |
| `app/` | `DuLieu` | Khai báo 5 danh sách của module 4 |
| `test/` | `KiemThuModule4` | 32 ca kiểm thử tự động |

`model/Nguoi.java` là lớp cha dùng chung với `KhachHang` (module 1).

### So với ERD vẽ tay

| ERD giấy | Code | Lý do |
|---|---|---|
| `NhanVien` có `Ho`, `Ten`, `SDT` riêng | `NhanVien extends Nguoi` (`hoTen`, `sdt`) | Dùng chung phần "người" với `KhachHang`, thể hiện kế thừa |
| `HangHoa`: `MaH`, `TenH`, `DonGia` | thêm `slTon` | Cần biết tồn kho để trừ khi dùng dịch vụ và để thống kê |

## 2. Học thuộc theo khuôn

Cả module chỉ có **3 khuôn** lặp đi lặp lại. Thuộc 3 khuôn này là viết lại được gần hết.

### Khuôn A — lớp model (5 lớp giống nhau)

```text
sc       : private static final Scanner sc = new Scanner(System.in, "UTF-8");  (Scanner riêng của lớp)
thuộc tính private
3 constructor: không tham số / đầy đủ / sao chép (nhận 1 đối tượng cùng lớp)
get / set
nhap()   : chỉ hỏi các trường người dùng gõ, qua NhapLieu.nhapXxx(sc, ...) (không hỏi mã, không hỏi trường tính toán)
xuat()   : 1 dòng System.out.println(a + " | " + b + ...)
toLine() : DinhDang.dong(các trường theo đúng thứ tự file)
```

### Khuôn B — lớp danh sách (thứ tự hàm giống hệt nhau trong 4 lớp có mã)

```text
sc (Scanner riêng của lớp, nếu lớp có hỏi bàn phím), ds (mảng), n (số phần tử), getDs(), getN()
them()      -> new X(); x.nhap(); (NV: trungSdt(sdt, ""), NCC/HangHoa: trungTen(ten, "") thì báo, return); x.setMa(sinhMa()); them(x)
them(x)     -> ds = Arrays.copyOf(ds, n + 1); ds[n] = new X(x); n++;
timTheoMa() -> nhập mã, gọi timTheoMa(ma); null thì báo "Không tìm thấy...", không thì in
timTheoMa(ma) -> for, equalsIgnoreCase, return ds[i]; hết vòng return null
sua(), sua(ma) -> timTheoMa; null thì báo; rồi:
                 NhaCC: nhập vào bản tạm, trungTen(ten, ma) thì báo, không thì chép sang
                 HangHoa: chỉ nhập tên mới, trungTen(ten, ma) thì báo, không thì setTenH (donGia, slTon tự cập nhật)
                 NhanVien: nhập vào bản tạm, trungSdt(sdt, ma) thì báo, không thì chép sang
xoa(), xoa(ma) -> kiểm tra bị tham chiếu chưa; for tìm i; dời ds[j] = ds[j+1]; copyOf(n - 1); n--
xuat(), xuat(mang) -> rỗng thì in "Danh sách trống." và return; không thì for in từng phần tử
sinhMa()    -> max của Integer.parseInt(ma.substring(độ dài tiền tố)); format("NV%03d", max + 1)
chon()      -> while(true): nhập mã, "0" thì return null, có thì return
--- gói 70% ---
--- docFile(): for (String[] t : TepTin.doc(FILE)) { try { them(new X(t[0], ...)); }
               catch (Exception e) { in "Bỏ qua 1 dòng lỗi" } }
--- ghiFile(): gom toLine() vào mảng, TepTin.ghi(FILE, mảng)
```

### Khuôn C — hàm lọc / thống kê

```java
// LỌC: gom phần tử thỏa điều kiện vào mảng mới
X[] kq = new X[0];
for (int i = 0; i < n; i++) {
    if (điều kiện) {
        kq = Arrays.copyOf(kq, kq.length + 1);
        kq[kq.length - 1] = ds[i];
    }
}
return kq;

// THỐNG KÊ THEO NHÓM: vòng ngoài duyệt nhóm, vòng trong cộng dồn
for (Nhom g : cacNhom) {
    int dem = 0;
    double tong = 0;
    for (int i = 0; i < n; i++) {
        if (ds[i] thuộc g) { dem++; tong += ...; }
    }
    System.out.println(g + ": " + dem + ", " + tong);
}
```

Khuôn C dùng cho: `timTheoTen`, `dsSapHet`, `locTheoKhoangNgay`, `layTheoPhieu` (lọc) và `thongKeTheoNhaCC`, `thongKeTheoNhanVien`, `thongKeTheoChucVu`, `thongKeTheoThang` (thống kê).

## 3. Đọc code theo thứ tự này

1. `model/HangHoa.java` — khuôn A đơn giản nhất.
2. `model/NhanVien.java` — khuôn A + kế thừa: `super(...)`, `super.nhap()`, `super.xuat()`.
3. `danhsach/DSNhaCC.java` — khuôn B ngắn nhất.
4. `danhsach/DSHangHoa.java` — thêm `truKho`, `congKho`, sắp xếp.
5. `danhsach/DSPhieuNhap.java` — **nghiệp vụ chính**: `them()` → `nhapCacDong()` → `lapPhieu(pn, cacDong)`.
6. `core/TepTin.java`, `core/TimKiem.java` — 2 lớp tiện ích.
7. `menu/` — menu chỉ gọi hàm, không có logic.

## 4. Phần nào thuộc gói nào

Trong mỗi lớp `DS...` code được chia bằng dòng `===== CÁC HÀM BẮT BUỘC (gói 50%) =====` và `===== HÀM NÂNG CAO (gói 70%) =====`.

**Gói 50%**: 5 lớp model; `them`, `timTheoMa`, `sua`, `xoa` có kiểm tra tham chiếu, `xuat`, `sinhMa`, `docFile`, `ghiFile`; lập phiếu nhập (cộng kho, cập nhật giá, tính tiền); `truKho`, `congKho`, `layTheoPhieu`; thống kê tồn kho.

**Gói 70%**:

| Hàm | Ở đâu | Ý chính |
|---|---|---|
| Lập phiếu qua **mảng tạm**, hủy giữa chừng không hỏng kho | `DSPhieuNhap.them/nhapCacDong/lapPhieu` | Gom đủ, hỏi "Lưu phiếu nhập" rồi mới lưu |
| Chặn 1 hàng 2 lần trong 1 phiếu | `DSPhieuNhap.daCoTrongPhieu` | Duyệt mảng tạm |
| Sửa qua **bản tạm** | `DSNhanVien.sua(ma)` | Constructor sao chép |
| Chặn trùng số điện thoại | `DSNhanVien.trungSdt` | Bỏ qua chính người đang sửa |
| Chặn trùng tên hàng, tên nhà cung cấp | `DSHangHoa.trungTen`, `DSNhaCC.trungTen` | Cùng khuôn với `trungSdt`, so `equalsIgnoreCase` |
| Doanh thu theo dịch vụ (menu Thống kê mục 10) | gọi `DSCTSuDungDV.thongKeTheoDichVu` của module 3 | Chỗ nào có tiền thì có thống kê |
| Tìm không dấu | `TimKiem` + `timTheoTen` | `Normalizer` NFD |
| Sắp lương giảm dần, tồn kho tăng dần | `sapXepTheoLuongGiam`, `sapXepTheoTonTang` | Bubble sort trên bản sao |
| Hàng sắp hết, tổng giá trị tồn | `DSHangHoa.dsSapHet`, `tongGiaTriTon` | Khuôn C |
| Lọc phiếu theo khoảng ngày | `DSPhieuNhap.locTheoKhoangNgay` | `isBefore` / `isAfter` |
| Chi phí nhập 12 tháng; theo NCC; theo nhân viên; theo chức vụ | `thongKe...` | Khuôn C |
| Bỏ qua dòng file lỗi | `docFile()` | `try/catch` |

## 5. Giải thích các đoạn khó

### 5.1 Mảng tự nới / thu (vì không dùng `ArrayList`)

`Arrays.copyOf(ds, n + 1)` tạo mảng mới dài hơn 1 ô và chép phần tử cũ sang. Xóa ở vị trí `i`: dời các phần tử sau lên (`ds[j] = ds[j + 1]`) rồi `Arrays.copyOf(ds, n - 1)` để cắt ô cuối.

`them(x)` lưu **bản sao** `new NhanVien(nv)`, để bên ngoài sửa `nv` sau đó không ảnh hưởng dữ liệu trong danh sách.

### 5.2 Sinh mã

`"NV007".substring(2)` → `"007"` → `7`. Lấy số lớn nhất rồi cộng 1. Không dùng `n + 1`: có NV001, NV002, NV003, xóa NV002 thì `n + 1 = 3` trùng NV003.

### 5.3 Lập phiếu nhập

```text
them()          B1  chọn NCC, chọn NV (gõ 0 = hủy) -> tạo đầu phiếu, ngày = hôm nay
nhapCacDong()   B2  lặp: chọn hàng (0 = kết thúc) -> trùng thì chọn lại -> nhập sl, giá
                    -> thêm vào MẢNG TẠM -> hỏi "nhập thêm?"
                    mảng rỗng -> hủy, kho không đổi
                    hỏi "Lưu phiếu nhập": k -> "Đã hủy lập phiếu nhập.", kho không đổi
lapPhieu(pn, cacDong)  B3  mỗi dòng: congKho, hh = timTheoMa(maH), hh.setDonGia(giá mới), thêm chi tiết
                    tính tổng tiền -> thêm phiếu
```

Chưa lưu gì cho tới B3. Nếu cộng kho ngay ở B2 mà người dùng hủy thì tồn kho sai. `lapPhieu` là hàm public có tham số (giống `lapHopDong`, `lapHoaDon`) nên kiểm thử gọi thẳng được.

### 5.4 Tìm không dấu

`Normalizer.normalize(s, NFD)` tách `"ễ"` thành `"e"` + các dấu; `replaceAll("\\p{M}", "")` xóa dấu. Riêng `đ` là chữ riêng nên phải `replace('đ', 'd')`. Bỏ dấu và viết thường cả 2 chuỗi rồi mới `contains`.

### 5.5 Bubble sort

So 2 phần tử **đứng cạnh nhau**, sai thứ tự thì đổi chỗ; mỗi lượt `i` đẩy phần tử "nặng" nhất còn lại xuống cuối nên vòng trong chạy tới `n - 1 - i`. Hai hàm sắp xếp giống hệt nhau, chỉ khác dấu so sánh: `<` (giảm dần) hay `>` (tăng dần). Sắp trên **bản sao** `Arrays.copyOf(ds, n)` nên thứ tự trong file không đổi.

### 5.6 Đọc / ghi file qua `TepTin`

`TepTin.doc(FILE)` trả về `String[][]`: mỗi phần tử là 1 dòng đã tách theo `|`. `docFile()` chỉ còn việc dựng đối tượng từ `t[0], t[1]...`. Dòng nào lỗi (thiếu trường, sai số, sai ngày) thì nhảy vào `catch (Exception e)`, báo "Bỏ qua 1 dòng lỗi" và đọc tiếp.

## 6. Kiểm thử

### Chạy kiểm thử

Từ thư mục gốc project:

```powershell
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src, test -Filter *.java).FullName
```

```powershell
java -cp out KiemThuModule4
```

Kết quả mong đợi: `Kết quả: 32 đạt, 0 trượt`. Lớp test chỉ dùng dữ liệu trong bộ nhớ, không đọc/ghi thư mục `data/`.

Các ca được kiểm: sinh mã (kể cả danh sách rỗng), trừ/cộng kho (đủ, thiếu, mã sai, **trừ đúng bằng tồn**), trùng sđt, tìm không dấu, sắp xếp (đúng thứ tự, không đổi mảng gốc), hàng sắp hết (kể cả bằng ngưỡng), tính thành tiền / tổng tiền, lọc ngày (lấy cả 2 ngày biên), tổng theo tháng, định dạng dòng file, lập phiếu (cộng kho, cập nhật giá, lưu phiếu + chi tiết).

### Chạy chương trình

```powershell
java -cp out app.Main
```

Thử nhanh: menu 6 thêm 2 nhân viên → 7 › 1 thêm nhà cung cấp → 7 › 2 thêm 2 hàng → 7 › 3 › 1 lập phiếu → menu 8 xem thống kê 4–9.

### Phản biện logic đã làm

| Vấn đề tìm ra | Hậu quả | Xử lý |
|---|---|---|
| Số lượng nhập cho tới `Integer.MAX_VALUE` | `slTon` (int) cộng dồn bị tràn thành **số âm** | Giới hạn 1–100.000 mỗi dòng |
| `NhapLieu.nhapSoThuc` nhận `Infinity` | Ghi file văng lỗi, chương trình dừng | **Đã sửa**: `nhapSoThuc` từ chối `Infinity` (`Double.isInfinite`) |
| Gõ 0 khi chọn hàng tưởng là hủy cả phiếu | Phiếu vẫn được lưu ngoài ý muốn | Ghi rõ "gõ 0 để kết thúc" và hỏi "Lưu phiếu nhập" trước khi lưu |
| Sửa hàng hóa cho đổi tay `donGia` | Trái mục 4.5 (giá chỉ tự cập nhật khi nhập hàng) | `sua` chỉ sửa tên |
| Trùng sđt chỉ báo sau khi nhập hết các ô | Bất tiện, không sai dữ liệu | Giữ nguyên cho code đơn giản |

## 7. Chỗ module 4 nối với các module khác

1. **`NhanVien extends Nguoi`**: `Nguoi` dùng chung với `KhachHang`; `NhanVien.xuat()` gọi `super.xuat()` để in `hoTen | sdt | ` (chưa xuống dòng) rồi in tiếp phần riêng.
2. **`DSNhanVien.xoa(ma)`** kiểm tra `PhieuNhap`, `HoaDon`, `HDThuePhong`; **`DSHangHoa.xoa(ma)`** kiểm tra `CTPhieuNhap`, `DichVu`.
3. **`MenuThongKe`**: doanh thu theo tháng/năm dùng `HoaDon.daThanhToan()`; doanh thu theo phòng dùng `DuLieu.dsCTHopDong.layTheoPhong(maP)` + `DuLieu.dsHoaDon.timTheoHopDong(maHDong)`; tình trạng phòng duyệt `DuLieu.dsPhong`.
4. Module Dịch vụ gọi `DuLieu.dsHangHoa.truKho(maH, sl)` khi ghi dịch vụ, rồi `DuLieu.dsHangHoa.ghiFile()`.
5. Module Thuê phòng / Hóa đơn chọn nhân viên lập bằng `DuLieu.dsNhanVien.xuat()` rồi `DuLieu.dsNhanVien.chon()` (trả `null` nếu gõ `0`).

## 8. Câu hỏi vấn đáp hay gặp

- **Đóng gói ở đâu?** Thuộc tính `private`, truy cập qua get/set; `slTon` không có ô nhập tay, chỉ đổi qua `congKho`/`truKho`.
- **Kế thừa ở đâu?** `NhanVien extends Nguoi`; constructor gọi `super(...)`, `nhap/xuat` gọi `super.nhap()/super.xuat()`.
- **Đa hình / interface?** Mọi lớp model cài `DoiTuong` và `@Override` `nhap`, `xuat`, `toLine`; `Nguoi` là lớp abstract cài một phần interface, `NhanVien` cài nốt `toLine()`.
- **Vì sao `CTPhieuNhap` lưu `donGiaNhap` mà không lấy `HangHoa.donGia`?** Giá hàng đổi sau mỗi lần nhập; phiếu cũ phải giữ đúng giá lúc nhập.
- **Vì sao không xóa phiếu nhập?** Là chứng từ; xóa làm tồn kho và thống kê sai.
- **Vì sao xóa nhân viên phải kiểm tra trước?** Phiếu nhập, hợp đồng, hóa đơn đang giữ `maNV`; xóa thì chứng từ trỏ tới người không còn tồn tại.
- **Vì sao `timTheoMa(ma)` không in?** Để module khác dùng lại; hàm không tham số mới lo phần nhập/in.
- **Vì sao sắp xếp trên bản sao?** Để thứ tự lưu trong file không bị đổi mỗi lần xem báo cáo.
