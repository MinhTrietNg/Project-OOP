# Module 1 — Phòng & Khách hàng

Tài liệu đọc kèm code của module 1: có gì, học theo khuôn nào, các hàm khó chạy ra sao, cách kiểm thử và những gì đã làm khi ghép với 3 module còn lại.

## 1. Module 1 gồm những file nào

| Tầng | File | Vai trò |
|---|---|---|
| `model/` | `Nguoi` | Lớp **abstract**: phần chung `hoTen`, `sdt` của `KhachHang` và `NhanVien` |
| | `KhachHang` | Khách thuê / khách ở, **kế thừa** `Nguoi` |
| | `Phong` | Lớp **abstract**: phần chung của mọi phòng + phương thức abstract `getLoaiPhong()` |
| | `PhongThuong`, `PhongDeluxe`, `PhongSuite` | 3 loại phòng, **kế thừa** `Phong` |
| `danhsach/` | `DSPhong`, `DSKhachHang` | Mỗi lớp quản lý 1 mảng + đọc/ghi 1 file |
| `menu/` | `MenuPhong`, `MenuKhachHang` | Chỉ hiện menu và gọi hàm của danh sách |
| `app/` | `DuLieu` | Khai báo `dsPhong`, `dsKhachHang` |
| `test/` | `KiemThuModule1` | 27 ca kiểm thử tự động |

Module 1 còn phụ trách **khung chung** (`core/NhapLieu`, `core/DinhDang`, `core/TepTin`, `core/TimKiem`, `app/Main`, `app/DuLieu`) và **ghép code** của 4 module (mục 7).

### So với ERD vẽ tay

| ERD giấy | Code | Lý do |
|---|---|---|
| `Phong` là phòng thường, `PhongDeluxe`/`PhongSuite` kế thừa | `Phong` **abstract** + thêm `PhongThuong` | 3 loại ngang hàng; nếu `Phong` là phòng thường thì Deluxe "là một" phòng thường, sai nghĩa |
| `PhongDeluxe`: `Vuexi` | `view` | Hướng nhìn của phòng |
| `Phong`: `DoanhThu` | bỏ | Tính từ hóa đơn khi thống kê (`MO_TA_DO_AN.md` mục 7); lưu sẵn thì dễ lệch số |
| `KhachHang`: `HoTen`, `SDT` | `KhachHang extends Nguoi` | Dùng chung phần "người" với `NhanVien`, thể hiện kế thừa |

## 2. Học thuộc theo khuôn

### Khuôn A — lớp model có lớp cha abstract

```text
LỚP CHA (Phong, Nguoi) — abstract
  thuộc tính private chung
  3 constructor: không tham số / đầy đủ / sao chép
  get / set
  [Phong] public abstract String getLoaiPhong();   -> lớp con BẮT BUỘC cài
  nhap()   : hỏi các trường chung
  xuat()   : System.out.print(...)  CHƯA xuống dòng
  toLine() : DinhDang.dong(các trường chung)

LỚP CON (PhongDeluxe, KhachHang...)
  thuộc tính riêng
  3 constructor: gọi super(...) / super(khac) trước, rồi gán phần riêng
  getLoaiPhong() { return DELUXE; }
  nhap()   : super.nhap(); hỏi phần riêng
  xuat()   : super.xuat(); System.out.println(phần riêng)   -> xuống dòng ở đây
  toLine() : super.toLine() + DinhDang.PHAN_CACH + DinhDang.dong(phần riêng)
```

`PhongThuong` không có thuộc tính riêng nên chỉ có constructor, `getLoaiPhong()` và `xuat()` (gọi `super.xuat()` rồi xuống dòng).

### Khuôn B — lớp danh sách (giống hệt module 2, 3, 4)

```text
ds (mảng), n (số phần tử), getDs(), getN()
them()        -> tạo đối tượng; x.nhap(); if (trungXxx(giaTri, "")) báo, return; x.setMa(sinhMa()); them(x)
them(x)       -> ds = Arrays.copyOf(ds, n + 1); ds[n] = BẢN SAO của x; n++;
timTheoMa()   -> nhập mã, gọi timTheoMa(ma), in
timTheoMa(ma) -> for, equalsIgnoreCase, return ds[i]; hết vòng return null
sua(), sua(ma)-> nhập vào BẢN TẠM; if (trungXxx(giaTri, ma)) báo, return; hợp lệ rồi mới thay
xoa(), xoa(ma)-> kiểm tra bị tham chiếu chưa; for tìm i; dời ds[j] = ds[j+1]; copyOf(n - 1); n--
xuat(), xuat(mang) -> mảng rỗng thì in "Danh sách trống." rồi return
sinhMa()      -> max của Integer.parseInt(ma.substring(độ dài tiền tố)); format("P%03d", max + 1)
docFile()     -> ds rỗng, n = 0; for (String[] t : TepTin.doc(FILE)) { try { them(...); } catch (Exception e) { báo bỏ qua dòng lỗi } }
ghiFile()     -> gom toLine() vào mảng, TepTin.ghi(FILE, mảng)
trungXxx(giaTri, maBoQua) -> for: trùng giá trị VÀ mã khác maBoQua thì return true (gói 70%)
```

### Khuôn C — hàm lọc / sắp xếp

```java
// LỌC: gom phần tử thỏa điều kiện vào mảng mới
Phong[] kq = new Phong[0];
for (int i = 0; i < n; i++) {
    if (điều kiện) {
        kq = Arrays.copyOf(kq, kq.length + 1);
        kq[kq.length - 1] = ds[i];
    }
}
return kq;
```

Khuôn C dùng cho: `dsPhongTrong`, `locTheoLoai`, `timTheoTen`. Sắp xếp (`sapXepTheoGiaTang`) là bubble sort trên bản sao, giống module 4.

## 3. Đọc code theo thứ tự này

1. `model/Nguoi.java` → `model/KhachHang.java` — khuôn A ngắn nhất: `super(...)`, `super.nhap()`, `super.xuat()`.
2. `model/Phong.java` — lớp abstract, hằng `TRONG`/`DA_THUE`/`THUONG`/`DELUXE`/`SUITE`, phương thức abstract.
3. `model/PhongThuong.java` → `PhongDeluxe.java` → `PhongSuite.java` — mỗi lớp con cài `getLoaiPhong()` và thêm phần riêng.
4. `danhsach/DSKhachHang.java` — khuôn B gần như nguyên mẫu `MO_TA_DO_AN.md` mục 2.9.
5. `danhsach/DSPhong.java` — khuôn B + **đa hình**: `saoChep()`, `taoTheoLoai()`, `docDong()`.
6. `menu/MenuPhong.java`, `menu/MenuKhachHang.java` — chỉ gọi hàm, không có logic.

## 4. Phần nào thuộc gói nào

Trong mỗi lớp `DS...` code được chia bằng dòng `===== CÁC HÀM BẮT BUỘC (gói 50%) =====` và `===== HÀM NÂNG CAO (gói 70%) =====`.

**Gói 50%**: 6 lớp model (2 lớp abstract, kế thừa 2 tầng); `them`, `timTheoMa`, `sua`, `xoa` có kiểm tra tham chiếu, `xuat`, `sinhMa`, `docFile`, `ghiFile`; `dsPhongTrong`, `capNhatTinhTrang` cho module khác dùng.

**Gói 70%**:

| Hàm | Ở đâu | Ý chính |
|---|---|---|
| Mảng chứa lẫn 3 loại phòng (đa hình) | `DSPhong` | `Phong[]`, gọi `xuat()`/`toLine()` thì mỗi loại tự chạy bản của mình |
| Chép đúng lớp con | `DSPhong.saoChep` | `instanceof` + constructor sao chép |
| Đọc file ra đúng lớp con | `DSPhong.docDong` | Trường đầu dòng là loại phòng |
| Sửa qua **bản tạm** | `sua(ma)` cả 2 lớp | Nhập lỗi / trùng thì dữ liệu cũ không hỏng |
| Chặn trùng số phòng, trùng SĐT | `DSPhong.trungTenP`, `DSKhachHang.trungSdt` | `them()` truyền `""`, `sua(ma)` truyền `ma` để bỏ qua chính đối tượng đang sửa |
| Tìm theo SĐT, tìm tên không dấu | `DSKhachHang.timTheoSdt`, `timTheoTen` | `TimKiem.chua` |
| Tìm phòng theo tên (số phòng) | `DSPhong.timTheoTen` (menu Phòng mục 9) | Khuôn C, cùng khuôn với `DSKhachHang.timTheoTen` |
| Lọc theo loại, sắp theo giá | `DSPhong.locTheoLoai`, `sapXepTheoGiaTang` | Khuôn C, bubble sort trên bản sao |
| Bỏ qua dòng file lỗi (thiếu trường, sai số) | `docFile()` | `try { them(...); } catch (Exception e)` |

## 5. Giải thích các đoạn khó

### 5.1 Đa hình trong `DSPhong`

Mảng khai báo kiểu cha `Phong[]` nhưng từng ô chứa `PhongThuong`, `PhongDeluxe` hoặc `PhongSuite`. Khi gọi `ds[i].xuat()`, Java chạy `xuat()` của **lớp thật** của đối tượng (Deluxe in thêm view, ban công…). Nhờ vậy `xuat()`, `toLine()`, `ghiFile()` của `DSPhong` không cần biết phòng nào là loại nào.

Chỉ có 2 chỗ bắt buộc phải biết loại, vì **constructor không đa hình** (`new` phải ghi rõ lớp):

- `saoChep(p)`: dùng `instanceof` để gọi đúng `new PhongDeluxe((PhongDeluxe) p)`…
- `docDong(t)`: đọc `t[0]` (`DELUXE` / `SUITE`, còn lại là `THUONG`) để biết tạo lớp nào và đọc thêm mấy trường. Cùng hình dạng với `saoChep` và `taoTheoLoai`: 2 lệnh `if` rồi `return new PhongThuong(...)`.

`taoTheoLoai()` cũng tạo lớp theo lựa chọn của người dùng, rồi mới gọi `p.nhap()` (đa hình: hỏi đúng phần riêng của loại đó).

### 5.2 Sửa phòng qua bản tạm

```text
tam = saoChep(ds[i])   bản sao, cùng loại phòng, giữ mã và tình trạng
tam.nhap()             người dùng nhập lại
trùng số phòng?        -> báo, return (ds[i] chưa bị đụng)
ds[i] = tam            thay cả đối tượng vào mảng
```

Không chép từng thuộc tính về như `DSKhachHang.sua` vì mỗi loại phòng có thuộc tính riêng khác nhau; thay nguyên đối tượng thì 1 dòng là xong cho cả 3 loại.

### 5.3 `capNhatTinhTrang` không tự ghi file

Giống mọi hàm khác, hàm này chỉ đổi dữ liệu trong mảng; **menu gọi `ghiFile()`** (`MO_TA_DO_AN.md` mục 2.7). Tình trạng phòng bị đổi từ module khác nên menu của module đó phải ghi thêm file phòng: `MenuThuePhong` (lập hợp đồng → `DA_THUE`) và `MenuHoaDon` (trả phòng → `TRONG`) đều gọi `DuLieu.dsPhong.ghiFile()`.

Phòng mới luôn `TRONG`; menu phòng **không cho sửa tay** tình trạng (`nhap()` không hỏi), chỉ đổi qua hàm này.

### 5.4 Sinh mã

`"P007".substring(1)` → `"007"` → `7`; khách hàng là `substring(2)` vì tiền tố `KH` dài 2. Lấy số lớn nhất rồi cộng 1, không dùng `n + 1` (xóa giữa chừng sẽ ra mã trùng).

### 5.5 Xóa có kiểm tra tham chiếu

- Xóa phòng: không có `CTHopDong` nào chứa `maP` đó.
- Xóa khách: không đứng tên `HDThuePhong` nào và không có trong `CTKhachO`.

Duyệt bằng `for (CTHopDong ct : DuLieu.dsCTHopDong.getDs())` — mảng luôn có đúng `n` phần tử nên duyệt `getDs()` an toàn.

## 6. Kiểm thử

### Chạy kiểm thử

Từ thư mục gốc project:

```powershell
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src, test -Filter *.java).FullName
```

```powershell
java -cp out KiemThuModule1
```

Kết quả mong đợi: `Kết quả: 27 đạt, 0 trượt`. *(Lưu ý: test ghi `data/phong.txt` và `data/khachhang.txt` để kiểm tra đọc/ghi file; sao lưu trước nếu đang có dữ liệu thật.)*

Các ca được kiểm: sinh mã (kể cả danh sách rỗng, mã không liên tục), `them(p)` giữ mã và lưu bản sao, mảng chứa đúng lớp con, `toLine()` của 3 loại phòng và khách hàng, phòng trống, đổi tình trạng, trùng số phòng (kể cả bỏ qua chính phòng đang sửa), lọc theo loại, sắp xếp (đúng thứ tự, không đổi mảng gốc), ghi rồi đọc lại file (đúng lớp con), tìm theo SĐT / tên không dấu, xóa bị chặn khi đã có hợp đồng và xóa được khi chưa dùng.

### Chạy chương trình

```powershell
java -cp out app.Main
```

Thử nhanh: menu 6 thêm nhân viên → menu 1 thêm 3 phòng (mỗi loại 1 phòng) → menu 2 thêm khách → menu 3 lập hợp đồng → menu 8 › 2 xem tình trạng phòng → menu 5 trả phòng, thanh toán → menu 8 › 3 doanh thu theo phòng.

### Phản biện logic đã làm

| Vấn đề tìm ra | Hậu quả | Xử lý |
|---|---|---|
| Module 2 thêm khách mới rồi lấy **khách cuối mảng**, trong khi `them()` có thể từ chối vì trùng SĐT | Hợp đồng gắn nhầm khách khác | `chonHoacThemKhach` so `getN()` trước/sau, không tăng thì trả `null` |
| Sửa giá phòng khi phòng đang thuê | Có thể lo hợp đồng cũ bị đổi tiền | Không ảnh hưởng: `CTHopDong` đã chép `donGia` lúc thuê |
| Dòng file thiếu trường hoặc sai số | Chương trình dừng khi đọc | `docFile()` bắt lỗi, bỏ qua dòng đó. File do chương trình tự ghi nên mã luôn đúng dạng; không sửa tay file `data/` |
| Trùng số phòng / SĐT chỉ báo sau khi nhập hết các ô | Bất tiện, không sai dữ liệu | Giữ nguyên cho code đơn giản (giống module 4) |

## 7. Ghép code với 3 module còn lại (đã làm)

1. **`DuLieu.java`**: thêm `dsPhong`, `dsKhachHang` và 2 dòng `docFile()`.
2. **`DSHoaDon`**: bật `timHopDong`, `timHopDongKhongBao`, `layPhongCuaHopDong`, `traPhong` (trả phòng → `capNhatTinhTrang(maP, Phong.TRONG)`).
3. **`DSCTSuDungDV.timHopDong`**: bật tìm hợp đồng.
4. **`DSNhanVien.xoa(ma)`**: bật vòng kiểm tra `HDThuePhong`.
5. **`MenuHoaDon`**, **`MenuThuePhong`**: đổi tình trạng phòng xong ghi cả file phòng (mục 5.3).
6. **`MenuThongKe`**: bật `tinhTrangPhongTheoLoai` và `doanhThuTheoPhong`.
7. **`DSHDThuePhong.chonHoacThemKhach`**: sửa lỗi lấy nhầm khách (mục 6).

Các hàm module 1 cung cấp cho module khác (`MO_TA_DO_AN.md` mục 6.6): `dsPhongTrong()`, `xuat(Phong[])`, `timTheoMa(ma)`, `capNhatTinhTrang(maP, tt)`, `timTheoSdt(sdt)`.

## 8. Câu hỏi vấn đáp hay gặp

- **Lớp abstract ở đâu, vì sao?** `Phong` và `Nguoi`. Không có "phòng chung chung" hay "người chung chung" ngoài đời, nên không cho `new Phong()`; chỉ tạo qua lớp con.
- **Phương thức abstract dùng làm gì?** `getLoaiPhong()` bắt mọi lớp con tự khai báo mình là loại nào; `toLine()` và thống kê dùng nó mà không cần `instanceof`.
- **Đa hình ở đâu?** Mảng `Phong[]` chứa 3 loại; `ds[i].xuat()` chạy bản của lớp con. `nhap()`, `xuat()`, `toLine()` đều `@Override`.
- **Kế thừa ở đâu?** `PhongThuong`/`PhongDeluxe`/`PhongSuite` `extends Phong`; `KhachHang`, `NhanVien` `extends Nguoi`. Còn `Phong` và `Nguoi` `implements DoiTuong` (cài interface, không phải kế thừa lớp).
- **Vì sao vẫn phải dùng `instanceof`?** Constructor không đa hình: muốn tạo bản sao đúng loại thì phải biết lớp cụ thể.
- **Vì sao `them(x)` lưu bản sao?** Bên ngoài sửa `x` sau đó không làm đổi dữ liệu trong danh sách (đóng gói).
- **Vì sao bỏ thuộc tính doanh thu của phòng?** Tính được từ hóa đơn; lưu sẵn thì mỗi lần lập / thanh toán hóa đơn phải nhớ cập nhật, quên là lệch số.
- **Ai ghi file khi `capNhatTinhTrang` đổi tình trạng phòng?** Menu đã gọi nghiệp vụ đó (`MenuThuePhong`, `MenuHoaDon`) gọi `DuLieu.dsPhong.ghiFile()`, đúng quy tắc chung "menu ghi file".
- **Vì sao xóa phòng / khách phải kiểm tra trước?** Hợp đồng, chi tiết khách ở đang giữ mã; xóa thì chứng từ trỏ tới thứ không còn tồn tại.
