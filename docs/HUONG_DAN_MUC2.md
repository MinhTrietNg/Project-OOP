# Module 2 — Thuê phòng

Tài liệu đọc kèm code của module 2: có gì, học theo khuôn nào, các hàm khó chạy ra sao, cách kiểm thử và cần làm gì khi ghép với 3 module còn lại.

## 1. Module 2 gồm những file nào

| Tầng | File | Vai trò |
|---|---|---|
| `model/` | `HDThuePhong` | Đầu hợp đồng thuê phòng (khách đứng tên, nhân viên lập, ngày nhận/trả, tiền cọc) |
| | `CTHopDong` | Từng phòng được thuê trong hợp đồng (chốt đơn giá, số người, thành tiền) |
| | `CTKhachO` | Từng khách ở trong từng phòng của hợp đồng |
| `danhsach/` | `DSHDThuePhong`, `DSCTHopDong`, `DSCTKhachO` | Mỗi lớp quản lý 1 mảng + đọc/ghi 1 file |
| `menu/` | `MenuThuePhong` | Chỉ hiện menu và gọi hàm của danh sách |
| `core/` | `TepTin`, `DinhDang`, `TimKiem` | Đọc/ghi file, định dạng ngày `dd/MM/uuuu`, tìm không dấu (dùng chung) |
| `app/` | `DuLieu` | Khai báo 3 danh sách của module 2 |
| `test/` | `KiemThuModule2` | 24 ca kiểm thử tự động |

Các lớp của module khác có liên kết trực tiếp với module 2: `KhachHang`, `Phong` (**module 1**), `HoaDon` (**module 3**), `NhanVien` (**module 4**).

### So với ERD vẽ tay

| ERD giấy | Code | Lý do |
|---|---|---|
| `CTHopDong` không lưu `DonGia` | thêm `donGia` | Chốt giá phòng tại thời điểm thuê; sau này bảng giá phòng đổi không làm sai hợp đồng cũ |
| Ngày nhận/trả tách từng phòng | gom lên `HDThuePhong` (`ngayNhanPhong`, `ngayTraPhong`) | Mọi phòng trong cùng hợp đồng nhận và trả cùng ngày, tránh lệch ngày giữa các phòng |
| Quan hệ nhiều–nhiều giữa Phòng và Khách ở | tách thành 2 bảng `CTHopDong` và `CTKhachO` | Biết rõ phòng nào có bao nhiêu người, giá bao nhiêu và danh sách từng khách ở phòng đó |

## 2. Học thuộc theo khuôn

Cả module chỉ có **3 khuôn** lặp đi lặp lại. Thuộc 3 khuôn này là viết lại được gần hết.

### Khuôn A — lớp model (3 lớp giống nhau)

```text
sc       : private static final Scanner sc = new Scanner(System.in, "UTF-8");  (Scanner riêng của lớp)
thuộc tính private
3 constructor: không tham số / đầy đủ / sao chép (nhận 1 đối tượng cùng lớp)
get / set
nhap()   : chỉ hỏi các trường người dùng gõ, qua NhapLieu.nhapXxx(sc, ...) (không hỏi mã, không hỏi mã liên kết, không hỏi thành tiền)
xuat()   : 1 dòng System.out.println(...)
toLine() : DinhDang.dong(các trường theo đúng thứ tự file)
```

### Khuôn B — lớp danh sách (phân biệt danh sách có mã và danh sách chi tiết)

```text
--- Cả 3 lớp đều hỏi bàn phím nên đều có Scanner riêng ---
private static final Scanner sc = new Scanner(System.in, "UTF-8");

--- Danh sách có mã (DSHDThuePhong) ---
ds (mảng), n (số phần tử), getDs(), getN()
them()   -> nghiệp vụ lập hợp đồng (giống DSHoaDon.them()), cuối cùng gọi lapHopDong(hd, dsCT, dsKO)
lapHopDong(hd, dsCT, dsKO) -> them(hd), them từng ct + đổi phòng DA_THUE, them từng ko
them(hd) -> copyOf(ds, n + 1); ds[n] = new HDThuePhong(hd); n++
timTheoMa(), timTheoMa(ma) -> for, equalsIgnoreCase, return ds[i]; hết vòng return null
sua(), sua(ma) -> kiểm tra chưa có hóa đơn; nhập lại ngayTraPhong; ct.tinhThanhTien(soNgay) cho các CTHopDong
(không có xoa() vì là chứng từ - mục 6.5)
xuat(), xuat(mang)
sinhMa() -> max của Integer.parseInt(ma.substring(4)); format("HDTP%03d", max + 1)

--- Danh sách chi tiết (DSCTHopDong, DSCTKhachO) ---
ds, n, getDs(), getN()
them(ct) -> kiểm tra timTheoKhoa(...) == null rồi mới copyOf(ds, n + 1)
(không có them() không tham số, không có sinhMa(), không có sua()/xoa() độc lập)
xuat(), xuat(mang)
```

### Khuôn C — hàm lọc / tra cứu theo khóa ngoại

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
```

Khuôn C dùng cho: `dsChuaThanhToan`, `timTheoMaKH`, `timTheoTenKH` (trong `DSHDThuePhong`), `layTheoHopDong`, `layTheoPhong` (trong `DSCTHopDong`, `DSCTKhachO`).

## 3. Đọc code theo thứ tự này

1. `model/CTKhachO.java` — khuôn A ngắn nhất (chỉ gồm 3 khóa liên kết).
2. `model/CTHopDong.java` — khuôn A + hàm tính `tinhThanhTien(long soNgay)`.
3. `model/HDThuePhong.java` — khuôn A + kiểm tra ngày nhận/trả (`nhap()`, `nhapNgayTra()`, `soNgayThue()`).
4. `danhsach/DSCTKhachO.java`, `danhsach/DSCTHopDong.java` — khuôn B cho danh sách chi tiết + chống trùng khóa.
5. `danhsach/DSHDThuePhong.java` — **nghiệp vụ chính**: `them()` -> `nhapKhachO(...)` -> `lapHopDong(hd, dsCT, dsKO)` và `sua(ma)`.
6. `menu/MenuThuePhong.java` — menu chỉ gọi hàm và gọi `ghiFile()` cho các danh sách thay đổi.

## 4. Phần nào thuộc gói nào

Trong mỗi lớp `DS...` code được chia bằng dòng `===== CÁC HÀM BẮT BUỘC (gói 50%) =====` và `===== HÀM NÂNG CAO & GIAO TIẾP MODULE =====`.

**Gói 50%**: 3 lớp model; `them`, `timTheoMa`, `sua` (sửa ngày trả khi chưa có hóa đơn), `xuat`, `sinhMa`, `docFile`, `ghiFile`; lập hợp đồng thuê phòng (chọn phòng trống, ghi khách ở, đổi tình trạng phòng sang `DA_THUE`).

**Gói 70%**:

| Hàm | Ở đâu | Ý chính |
|---|---|---|
| Lập hợp đồng gom qua **mảng tạm** rồi mới chốt | `DSHDThuePhong.them()` | Chọn đủ phòng & khách mới lưu và đổi trạng thái phòng; gõ `0` là hủy |
| Tìm/thêm khách ngay tại chỗ khi lập hợp đồng | `DSHDThuePhong.chonHoacThemKhach` | Tìm theo mã hoặc SĐT; chưa có gọi `dsKhachHang.them()` |
| Chặn trùng phòng trong 1 hợp đồng & trùng khách trong 1 phòng | `them()` + `timTheoKhoa` | Trùng phòng: kiểm tra trên mảng tạm `dsCT`. Trùng khách: `DSCTKhachO.them()` bỏ qua lúc chốt |
| Sửa ngày trả tự cập nhật lại thành tiền từng phòng | `DSHDThuePhong.sua(ma)` | Chặn nếu đã có `HoaDon`; tính lại `donGia * soNgayThue()` |
| Lọc hợp đồng chưa thanh toán | `DSHDThuePhong.dsChuaThanhToan` | Kiểm tra `dsHoaDon.timTheoHopDong(ma) == null` |
| Tìm hợp đồng theo tên khách không dấu | `DSHDThuePhong.timTheoTenKH` | Kết hợp `dsKhachHang.timTheoMa` + `TimKiem.chua` |
| Bỏ qua dòng file lỗi, ngày sai lịch | `docFile()` | `try/catch` quanh `them(...)` + `DinhDang.docNgay` |

## 5. Giải thích các đoạn khó

### 5.1 Mảng tự nới và lý do không có hàm xóa

`Arrays.copyOf(ds, n + 1)` tạo mảng mới dài hơn 1 ô và chép phần tử cũ sang; `them(x)` luôn lưu **bản sao** `new HDThuePhong(hd)` để tránh bị sửa ngoài ý muốn.

Theo mục 6.5 của đồ án, `HDThuePhong`, `CTHopDong`, `CTKhachO` thuộc nhóm **chứng từ và chi tiết** nên **không cài đặt `xoa()`** để giữ nguyên lịch sử thuê phòng.

### 5.2 Sinh mã

`"HDTP005".substring(4)` -> `"005"` -> `5`. Duyệt toàn bộ mảng tìm số lớn nhất rồi cộng 1, định dạng lại bằng `String.format("HDTP%03d", max + 1)`.

### 5.3 Lập hợp đồng qua mảng tạm

```text
them()          B1  kiểm tra có nhân viên, còn phòng TRONG -> chọn/thêm KH đứng tên -> chọn NV
                B2  tạo đầu hợp đồng, ngày lập = hôm nay -> nhập ngày nhận, trả, cọc
                B3  lặp: chọn phòng TRONG -> daChonPhong(dsCT, maP) thì báo, chọn lại
                    -> taoCTHopDong(hd, p, soNgay) -> thêm vào mảng tạm dsCT
                    -> nhapKhachO(...): 1..slNguoi khách ở -> nối vào mảng tạm dsKO
lapHopDong(...) B4  lưu hd -> lưu từng ct -> đổi phòng sang DA_THUE -> lưu từng ko
```

Chỉ tới B4 dữ liệu mới ghi vào các danh sách và đổi tình trạng phòng. Gõ `0` ở bước chọn khách, chọn nhân viên, chọn phòng (khi chưa có phòng nào) hoặc chọn khách ở thì in `Đã hủy lập hợp đồng.` và các bảng giữ nguyên (riêng khách mới thêm tại chỗ thì vẫn được giữ).

`nhapKhachO` dùng `while (dem < slNguoi)`: từ khách thứ 2 hỏi `nhapCoKhong("Thêm khách ở phòng ...")`; gõ `0` thì trả `null` để `them()` hủy cả hợp đồng; chọn lại khách đã có trong phòng thì `daCoKhach(dsKO, maP, maKH)` báo và hỏi lại.

3 hàm phụ `daChonPhong`, `daCoKhach`, `taoCTHopDong` đều là khuôn quen: hai hàm đầu "for tìm, thấy thì `return true`, hết vòng `return false`"; hàm cuối "new + set + nhap + return".

### 5.4 Chặn trùng khóa ở bảng chi tiết (`timTheoKhoa`)

- `DSCTHopDong`: cặp `(maHDong, maP)` là khóa chính -> `timTheoKhoa(maHDong, maP)` kiểm tra không cho 1 phòng xuất hiện 2 lần trong cùng hợp đồng.
- `DSCTKhachO`: bộ `(maHDong, maP, maKH)` là khóa chính -> `timTheoKhoa(maHDong, maP, maKH)` chặn ghi trùng cùng 1 khách 2 lần vào 1 phòng. Khi lập hợp đồng, `daCoKhach` đã chặn ngay lúc nhập nên tới lúc chốt không còn khách trùng.

### 5.5 Sửa ngày trả phòng và tính lại thành tiền chi tiết

Khi khách trả sớm hoặc gia hạn thêm ngày (và hợp đồng **chưa lập hóa đơn**), `DSHDThuePhong.sua(ma)` gọi `hd.nhapNgayTra()` để đảm bảo ngày trả mới vẫn sau ngày nhận phòng, sau đó duyệt `DuLieu.dsCTHopDong.layTheoHopDong(ma)` gọi `ct.tinhThanhTien(hd.soNgayThue())`. Menu sau đó ghi cả 2 file `hdthuephong.txt` và `cthopdong.txt`.

Rút ngắn ngày trả: `sua(ma)` lưu `ngayTraCu` trước khi nhập; nếu có `CTSuDungDV` của hợp đồng mà `ngaySD` sau ngày trả mới thì đặt lại `ngayTraCu`, báo `Đã có dịch vụ dùng ngày ..., ngày trả không được sớm hơn ngày đó. Không sửa.` và dừng.

### 5.6 Đọc / ghi file qua `TepTin` và `DinhDang.docNgay`

`TepTin.doc(FILE)` trả về `String[][]` đã tách sẵn theo ký tự `|`. Khi đọc `hdthuephong.txt`, các cột ngày (`t[3]`, `t[5]`, `t[6]`) được chuyển sang `LocalDate` bằng `DinhDang.docNgay(...)` (dùng `ResolverStyle.STRICT` nên tự bắt được các ngày sai như `31/02/2026`).

## 6. Kiểm thử

### Chạy kiểm thử

Từ thư mục gốc project:

```powershell
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src, test -Filter *.java).FullName
```

```powershell
java -cp out KiemThuModule2
```

Kết quả mong đợi: `Kết quả: 24 đạt, 0 trượt`.

Các ca được kiểm: sinh mã (kể cả danh sách rỗng), tính số ngày thuê (kể cả ca biên 1 đêm), tính thành tiền chi tiết, lập hợp đồng liên bảng (lưu `HDThuePhong`, `CTHopDong`, `CTKhachO` và đổi trạng thái phòng sang `DA_THUE`), chặn trùng khóa chi tiết, tra cứu theo khóa/phòng/hợp đồng, tìm theo mã KH và tên KH không dấu, lọc hợp đồng chưa thanh toán trước và sau khi lập hóa đơn, định dạng dòng file `toLine()`.

### Chạy chương trình

```powershell
java -cp out app.Main
```

Thử nhanh: menu 1 thêm 2 phòng trống -> menu 6 thêm 1 nhân viên -> menu 3 › 1 lập hợp đồng (thêm khách mới tại chỗ, thuê 2 phòng) -> menu 3 › 2 sửa ngày trả phòng -> menu 3 › 7 và 8 xem chi tiết phòng thuê & khách ở.

### Phản biện logic đã làm

| Vấn đề tìm ra | Hậu quả | Xử lý |
|---|---|---|
| Lập hợp đồng chọn lại phòng đã chọn trước đó trong cùng hợp đồng | Trùng cặp `(maHDong, maP)` hoặc nhân đôi tiền phòng | Kiểm tra trùng trên mảng tạm `dsCT` ngay lúc chọn và chặn thêm ở `DSCTHopDong.them(ct)` |
| Sửa ngày trả phòng sau khi hợp đồng đã xuất hóa đơn | Tiền trên hợp đồng lệch với `tongTienThue` đã chốt trên `HoaDon` | `sua(ma)` kiểm tra `DuLieu.dsHoaDon.timTheoHopDong(ma) == null` trước khi cho sửa |
| Người dùng gõ `0` khi chưa chọn được phòng nào | Tạo hợp đồng rỗng không có phòng thuê | `dsCT.length == 0` thì hủy cả hợp đồng (`Đã hủy lập hợp đồng.`) |
| Chưa có nhân viên mà vẫn cho lập hợp đồng | Nhập khách xong mới kẹt ở bước chọn nhân viên | Kiểm tra `dsNhanVien.getN() == 0` ngay đầu `them()` |
| File `.txt` có ngày không tồn tại (vd `31/02/2026`) | Văng `DateTimeParseException` khi khởi động | `DinhDang.docNgay` nằm trong `try/catch` của `docFile()` để bỏ qua dòng lỗi |

## 7. Khi ghép code với 3 module còn lại

1. **`DuLieu.java`**: đảm bảo đã khai báo đủ 3 danh sách (`dsHDThuePhong`, `dsCTHopDong`, `dsCTKhachO`) cùng `dsPhong`, `dsKhachHang`, và đã đưa chúng vào mảng `TAT_CA` (để `DuLieu.docTatCa()` gọi `docFile()`).
2. **`Main.java`**: tại lựa chọn `3. Thuê phòng`, gọi `MenuThuePhong.chay()`.
3. **Module 1 (`DSPhong`, `DSKhachHang`)**:
   - `DSPhong` cần có `dsPhongTrong()` và `capNhatTinhTrang(String maP, String tinhTrang)` (chỉ đổi tình trạng, không ghi file; `MenuThuePhong` tự gọi `dsPhong.ghiFile()`).
   - `DSKhachHang` cần có `timTheoSdt(String sdt)` và `them()` tự lưu khách mới vào cuối mảng `ds` để `chonHoacThemKhach()` lấy lại được ngay.
   - `DSPhong.xoa(ma)` duyệt `DuLieu.dsCTHopDong.getDs()`; `DSKhachHang.xoa(ma)` duyệt `DuLieu.dsHDThuePhong.getDs()` và `DuLieu.dsCTKhachO.getDs()` để chặn xóa nếu đã phát sinh hợp đồng.
4. **Module 3 (`DSHoaDon`, `DSCTSuDungDV`)**:
   - `DSHoaDon` cần có `timTheoHopDong(String maHDong)` trả về `null` nếu hợp đồng chưa lập hóa đơn.
   - Khi trả phòng (lập hóa đơn), module 3 gọi `DuLieu.dsCTHopDong.layTheoHopDong(maHDong)` để tính `tongTienThue` và đổi các phòng về `Phong.TRONG`.
5. **Module 4 (`DSNhanVien`, `MenuThongKe`)**:
   - Trong `DSNhanVien.xoa(ma)`: bật vòng kiểm tra `DuLieu.dsHDThuePhong.getDs()` để chặn xóa nhân viên đã từng lập hợp đồng.
   - `MenuThongKe.doanhThuTheoPhong` duyệt `DuLieu.dsCTHopDong.getDs()` theo `maP` kết hợp kiểm tra hóa đơn `DA_TT`.

## 8. Câu hỏi vấn đáp hay gặp

- **Đóng gói ở đâu?** Các thuộc tính đều `private`, truy cập qua getter/setter; `thanhTien` của `CTHopDong` không cho nhập tay mà tính qua `tinhThanhTien(soNgay)`.
- **Đa hình / interface ở đâu?** Cả 3 lớp `HDThuePhong`, `CTHopDong`, `CTKhachO` đều implements `DoiTuong` và `@Override` các phương thức `nhap()`, `xuat()`, `toLine()`.
- **Vì sao `CTHopDong` lưu thêm `donGia` mà không lấy trực tiếp từ `Phong.donGia`?** Giá phòng có thể thay đổi theo mùa; lưu `donGia` vào `CTHopDong` giúp chốt đúng giá tại thời điểm khách ký hợp đồng.
- **Vì sao `ngayNhanPhong` và `ngayTraPhong` đặt ở `HDThuePhong` thay vì `CTHopDong`?** Theo nghiệp vụ đồ án, mọi phòng trong cùng một hợp đồng nhận và trả cùng ngày; đặt ở `HDThuePhong` tránh lặp dữ liệu và tránh mâu thuẫn ngày giữa các phòng.
- **Vì sao không cho xóa hợp đồng thuê phòng?** Hợp đồng là chứng từ giao dịch gốc liên kết tới phòng, hóa đơn và sử dụng dịch vụ; xóa đi sẽ làm mất lịch sử và sai lệch doanh thu thống kê.
- **Vì sao tách thành 2 bảng `CTHopDong` và `CTKhachO`?** Một hợp đồng có nhiều phòng (`CTHopDong`), và mỗi phòng lại có nhiều khách ở cùng (`CTKhachO`); tách 2 bảng giúp chuẩn hóa dữ liệu và truy vấn được danh sách khách theo từng phòng.
- **Vì sao `sua(ma)` chặn sửa khi hợp đồng đã có hóa đơn?** Khi đã lập hóa đơn, `tongTienThue` trên hóa đơn đã được chốt để thanh toán; nếu cho sửa ngày trả sau đó sẽ làm tiền hợp đồng và tiền hóa đơn lệch nhau.
- **Vì sao các hàm tìm kiếm có tham số (`timTheoMa`, `layTheoHopDong`) chỉ trả về dữ liệu mà không in?** Để tách biệt xử lý dữ liệu và giao diện hiển thị, giúp các module khác (Hóa đơn, Dịch vụ, Thống kê) tái sử dụng trực tiếp.