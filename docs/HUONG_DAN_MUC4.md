# Module 4 — Nhân sự, Kho & Thống kê

Tài liệu đọc kèm code của module 4: có gì, đọc theo thứ tự nào, các hàm khó chạy ra sao, và cần làm gì khi ghép với 3 module còn lại.

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
| `core/` | `TimKiem` | Tìm theo tên không phân biệt dấu (file mới, không đụng file chung) |
| `app/` | `DuLieu` | Thêm 5 danh sách của module 4 |

`model/Nguoi.java` là lớp của **module 1**. Mình viết tạm đúng theo mục 4.1 của `MO_TA_DO_AN.md` để `NhanVien` biên dịch được. Khi ghép thì dùng bản của module 1 (xem mục 6).

## 2. So với ERD vẽ tay

Code theo `MO_TA_DO_AN.md` (bản nhóm đã thống nhất từ ERD), nên có vài chỗ khác ERD trên giấy:

| ERD giấy | Code | Lý do |
|---|---|---|
| `NhanVien` có `Ho`, `Ten`, `SDT` riêng | `NhanVien extends Nguoi` (`hoTen`, `sdt`) | Dùng chung phần "người" với `KhachHang`, thể hiện kế thừa |
| `HangHoa`: `MaH`, `TenH`, `DonGia` | thêm `slTon` | Cần biết tồn kho để trừ khi dùng dịch vụ và để thống kê |
| `PhieuNhap`, `CTPhieuNhap` | giữ nguyên các trường | — |

## 3. Đọc code theo thứ tự này

1. `model/HangHoa.java` — lớp đơn giản nhất: thuộc tính, 3 constructor, get/set, `nhap/xuat/toLine`.
2. `model/NhanVien.java` — xem `super(...)`, `super.nhap()`, `super.xuat()`: kế thừa từ `Nguoi`.
3. `danhsach/DSNhaCC.java` — mẫu danh sách đầy đủ: thêm, tìm, sửa, xóa, sinh mã, đọc/ghi file.
4. `danhsach/DSHangHoa.java` — thêm `congKho`, `truKho`, sắp xếp, hàng sắp hết.
5. `danhsach/DSPhieuNhap.java` — **nghiệp vụ chính**: hàm `them()` lập phiếu.
6. `menu/MenuKho.java`, `menu/MenuThongKe.java` — menu chỉ gọi hàm, không có logic.

## 4. Phần nào thuộc gói nào

Trong mỗi lớp `DS...` code được chia bằng các dòng chú thích `===== CÁC HÀM BẮT BUỘC (gói 50%) =====` và `===== HÀM NÂNG CAO (gói 70%) =====`.

**Gói 50%** (bắt buộc theo mục 2.9, 6.4 của mô tả):

- 5 lớp model, mỗi lớp 3 constructor, get/set, `nhap()`, `xuat()`, `toLine()`.
- `them`, `timTheoMa`, `sua`, `xoa` (có kiểm tra tham chiếu), `xuat`, `sinhMa`, `docFile`, `ghiFile`.
- Lập phiếu nhập: cộng kho, cập nhật giá nhập, tính `thanhTien`, `tongTien`.
- `truKho`, `congKho`, `layTheoPhieu` cho module khác gọi.
- Thống kê tồn kho.

**Gói 70%** (hàm khó thêm):

| Hàm | Ở đâu | Kỹ thuật |
|---|---|---|
| Lập phiếu bằng **mảng tạm**, hủy giữa chừng không hỏng kho | `DSPhieuNhap.them()` | Gom dữ liệu trước, lưu sau |
| Chặn 1 hàng xuất hiện 2 lần trong 1 phiếu | `DSPhieuNhap.daCoTrongPhieu()` | Duyệt mảng tạm |
| Sửa qua **bản tạm**, nhập sai không làm hỏng dữ liệu cũ | `DSNhanVien.sua(ma)` | Constructor sao chép |
| Chặn trùng số điện thoại nhân viên | `DSNhanVien.trungSdt()` | Duyệt + bỏ qua chính mình |
| Tìm theo tên, gõ không dấu vẫn ra | `core/TimKiem` + `timTheoTen()` | `Normalizer` NFD |
| Sắp xếp lương giảm dần | `DSNhanVien.sapXepTheoLuongGiam()` | Selection sort trên bản sao |
| Tồn kho tăng dần + tổng giá trị | `DSHangHoa.xuatTonKho()` | Bubble sort trên bản sao |
| Hàng sắp hết theo ngưỡng | `DSHangHoa.dsSapHet()` | Lọc ra mảng mới |
| Lọc phiếu theo khoảng ngày | `DSPhieuNhap.locTheoKhoangNgay()` | `LocalDate.isBefore/isAfter` |
| Chi phí nhập 12 tháng của 1 năm | `DSPhieuNhap.thongKeTheoThang()` | Gộp theo tháng |
| Nhập hàng theo NCC + NCC nhập nhiều nhất | `DSPhieuNhap.thongKeTheoNhaCC()` | Vòng lặp lồng + tìm max |
| Số phiếu mỗi nhân viên lập | `DSPhieuNhap.thongKeTheoNhanVien()` | Vòng lặp lồng |
| Số người, tổng lương theo chức vụ | `DSNhanVien.thongKeTheoChucVu()` | 3 mảng song song |
| Bỏ qua dòng file bị lỗi thay vì dừng chương trình | `docFile()` mọi lớp | `try/catch` trong vòng lặp |

## 5. Giải thích các đoạn khó

### 5.1 Mảng tự nới / thu (vì không dùng `ArrayList`)

```java
ds = Arrays.copyOf(ds, n + 1);   // tạo mảng mới dài hơn 1 ô, chép phần tử cũ sang
ds[n++] = new NhanVien(nv);      // đặt BẢN SAO vào ô cuối rồi tăng n
```

Xóa phần tử ở vị trí `i`: dời các phần tử sau lên 1 ô (`ds[j] = ds[j + 1]`), rồi `Arrays.copyOf(ds, n - 1)` để cắt ô cuối.

`them(x)` lưu **bản sao** (`new NhanVien(nv)`) chứ không lưu chính `nv`, để bên ngoài sửa `nv` sau đó cũng không ảnh hưởng dữ liệu trong danh sách.

### 5.2 Sinh mã

`"NV007".substring(2)` → `"007"` → `7`. Lấy số lớn nhất rồi cộng 1, định dạng lại bằng `String.format("NV%03d", ...)`. Không dùng `n + 1`: có NV001, NV002, NV003, xóa NV002 thì `n + 1 = 3` sẽ trùng NV003.

### 5.3 Lập phiếu nhập (`DSPhieuNhap.them()`)

```text
B1  chọn NCC ──(0: hủy)──► thoát
    chọn NV  ──(0: hủy)──► thoát
    tạo PhieuNhap: mã tự sinh, ngày = hôm nay

B2  lặp:  chọn hàng (0: dừng)
          ├─ đã có trong phiếu? → báo, chọn lại
          └─ nhập sl, giá → thêm vào MẢNG TẠM cacDong
          hỏi "nhập thêm?" (k → dừng)
    cacDong rỗng? → hủy, kho không đổi

B3  với mỗi dòng: congKho, setDonGia = giá nhập mới, thêm vào DSCTPhieuNhap
    tinhTongTien(cacDong) → thêm phiếu → in phiếu
```

Ý chính: **chưa lưu gì cho tới B3**. Nếu cộng kho ngay ở B2 mà người dùng hủy thì tồn kho sai.

### 5.4 Tìm không dấu (`core/TimKiem`)

`Normalizer.normalize(s, NFD)` tách `"ễ"` thành `"e"` + các dấu. `replaceAll("\\p{M}", "")` xóa hết dấu (`\p{M}` là nhóm ký tự dấu). Riêng `đ` là chữ riêng chứ không phải `d` + dấu nên phải `replace('đ', 'd')`. Hai chuỗi đều được bỏ dấu và viết thường rồi mới `contains`.

### 5.5 Hai thuật toán sắp xếp

- **Selection sort** (`sapXepTheoLuongGiam`): vòng `i` tìm phần tử lớn nhất trong đoạn `[i..cuối]`, đổi chỗ về vị trí `i`.
- **Bubble sort** (`sapXepTheoTonTang`): so 2 phần tử kề nhau, sai thứ tự thì đổi; mỗi lượt đẩy phần tử lớn nhất còn lại xuống cuối.

Cả hai sắp trên **bản sao** `Arrays.copyOf(ds, n)` nên thứ tự trong file không đổi.

### 5.6 Mảng song song (`thongKeTheoChucVu`)

3 mảng cùng chỉ số: `chucVu[k]` có `soNguoi[k]` người, tổng lương `tongLuong[k]`. Gặp chức vụ mới thì nới cả 3 mảng thêm 1 ô.

## 6. Khi ghép code với 3 module còn lại

1. **`Nguoi`**: dùng bản của module 1, xóa bản tạm của mình. Bản đó phải có `getHoTen()`, `getSdt()`, `setHoTen()`, `setSdt()`, constructor `(hoTen, sdt)` và constructor sao chép. `NhanVien.xuat()` gọi `super.xuat()` để in họ tên, sđt trên **cùng 1 dòng** bảng; nếu `Nguoi.xuat()` của module 1 in kiểu khác thì bảng nhân viên sẽ lệch cột, cần thống nhất.
2. **`DuLieu.java`**: giữ 5 dòng khai báo + 5 dòng `docFile()` của module 4, thêm của các bạn vào.
3. **`DSNhanVien.xoa(ma)`**: bỏ comment 2 vòng kiểm tra `HDThuePhong`, `HoaDon` và thêm 2 `import`.
4. **`DSHangHoa.xoa(ma)`**: bỏ comment vòng kiểm tra `DichVu` và thêm `import model.DichVu`.
5. **`MenuThongKe`**: bỏ comment 5 dòng `import` ở đầu file; trong 3 hàm `doanhThuTheoThangNam`, `tinhTrangPhongTheoLoai`, `doanhThuTheoPhong` xóa dòng `println` tạm và bỏ comment khối `/* ... */`.
6. Module Dịch vụ gọi `DuLieu.dsHangHoa.truKho(maH, sl)` khi ghi dịch vụ, rồi `DuLieu.dsHangHoa.ghiFile()`.
7. Module Thuê phòng / Hóa đơn chọn nhân viên lập bằng `DuLieu.dsNhanVien.xuat()` rồi `DuLieu.dsNhanVien.chon()` (trả `null` nếu người dùng gõ `0`).

## 7. Chạy thử

Từ thư mục gốc project:

```powershell
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
```

```powershell
java -cp out app.Main
```

Thứ tự thử nhanh: menu 6 thêm 2 nhân viên → menu 7 › 1 thêm nhà cung cấp → 7 › 2 thêm 2 hàng → 7 › 3 › 1 lập phiếu nhập → menu 8 xem thống kê 4–9.

## 8. Câu hỏi vấn đáp hay gặp

- **Tính đóng gói ở đâu?** Thuộc tính đều `private`, truy cập qua get/set; `slTon` không có ô nhập tay, chỉ đổi qua `congKho`/`truKho`.
- **Kế thừa ở đâu?** `NhanVien extends Nguoi`; constructor gọi `super(...)`, `nhap/xuat` gọi `super.nhap()/super.xuat()`.
- **Đa hình / interface?** Mọi lớp model cài `DoiTuong` (`nhap`, `xuat`, `toLine`) và `@Override` các hàm đó; `Nguoi` là lớp abstract cài một phần interface, lớp con cài nốt `toLine()`.
- **Vì sao `CTPhieuNhap` lưu `donGiaNhap` mà không lấy `HangHoa.donGia`?** Giá hàng đổi sau mỗi lần nhập; phiếu cũ phải giữ đúng giá lúc nhập.
- **Vì sao không xóa phiếu nhập?** Là chứng từ; xóa sẽ làm tồn kho và thống kê sai.
- **Vì sao xóa nhân viên phải kiểm tra trước?** Phiếu nhập, hợp đồng, hóa đơn đang giữ `maNV`; xóa thì các chứng từ đó trỏ tới nhân viên không còn tồn tại.
- **Vì sao `timTheoMa(ma)` không in?** Để module khác dùng lại được; hàm không tham số mới lo phần nhập/in.
