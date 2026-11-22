# Kịch bản demo — Quản lý khách sạn

Tài liệu để cả nhóm cầm khi vấn đáp: chuẩn bị máy, chỉ code theo từng tiêu chí chấm, gõ phím cho từng chức năng, và các "bẫy" cô hay thử.

Mọi thông báo trong tài liệu được chép từ lần chạy thật trên bộ dữ liệu `data_mau/`. Dòng nào có ngày lập là **hôm nay** (hợp đồng, hóa đơn, phiếu nhập mới) thì sẽ ra đúng ngày máy lúc demo.

Cách đọc phím: `1 → 5` nghĩa là gõ `1` Enter rồi `5` Enter. Chữ trong ngoặc kép là chữ cần gõ.

---

## 1. Chuẩn bị

### 1.1 Biên dịch và chạy (luôn đứng ở **thư mục gốc project**)

```bat
chcp 65001
javac -encoding UTF-8 -d out src\app\*.java src\core\*.java src\model\*.java src\danhsach\*.java src\menu\*.java
java -cp out app.Main
```

- `chcp 65001` chỉ cần khi chạy bằng **cmd** (để hiện tiếng Việt). PowerShell / terminal của IDE thì bỏ qua.
- Phải chạy từ thư mục gốc vì chương trình đọc file theo đường dẫn tương đối `data/...`.
- Nếu console gõ tiếng Việt có dấu bị lỗi, gõ không dấu cho phần tên/địa chỉ (không ảnh hưởng chức năng).
- **Gõ từng dòng, Enter từng lần.** Không dán nhiều dòng cùng lúc và không chạy `java app.Main < file.txt`: mỗi lớp có Scanner riêng với bộ đệm riêng, Scanner nhận dữ liệu trước sẽ giữ luôn các dòng phía sau, làm chương trình báo "Hết dữ liệu vào".

### 1.2 Nạp lại dữ liệu mẫu (làm trước buổi demo và mỗi khi muốn chạy lại từ đầu)

```bat
mkdir data
copy /Y data_mau\*.txt data\
```

PowerShell: `Copy-Item data_mau\*.txt data\ -Force` · Git Bash: `cp data_mau/*.txt data/`

### 1.3 Dữ liệu mẫu có gì

| Danh mục | Nội dung |
|---|---|
| Nhân viên | NV001 Lan (Lễ tân), NV002 Hùng (Quản lý), NV003 Tâm (Thủ kho), NV004 Hà (Phục vụ) |
| Phòng | P001–P003 thường (101, 102, 103), P004–P006 Deluxe (201, 202, 203), P007–P008 Suite (301, 302). **P002 đang `DA_THUE`** |
| Khách hàng | KH001–KH006 (đều đã có trong hợp đồng → không xóa được) |
| Nhà cung cấp | NCC001 Nước giải khát Sài Gòn, NCC002 Thực phẩm Minh Phát |
| Hàng hóa | HH001 Nước suối (tồn 138), HH002 Bia lon (66), HH003 Mì ly (**5**, sắp hết), HH004 Khăn giấy (50) |
| Dịch vụ | DV001 Nước suối → HH001, DV002 Bia lon → HH002, DV003 Mì ly → HH003, DV004 Giặt ủi (không dùng hàng) |
| Phiếu nhập | PN001 (15/10, 928.000 đ), PN002 (20/10, 190.000 đ), PN003 (10/11, 513.000 đ) |
| Hợp đồng | HDTP001 (25–28/10, P001) và HDTP002 (08–11/11, P004 + P007) đã trả phòng, hóa đơn **đã thanh toán**. **HDTP003** (17–20/11, P002, khách KH006) **đang ở, chưa có hóa đơn** → dùng để demo ghi dịch vụ → trả phòng → thanh toán |
| Hóa đơn | HDON001 = 1.010.000 đ (tháng 10), HDON002 = 5.750.000 đ (tháng 11) |

---

## 2. Tiêu chí chấm → mở chỗ nào trong code

| Tiêu chí | Mở file / chỉ vào | Câu nói khi trình bày |
|---|---|---|
| **Đọc / ghi file** | `core/TepTin.java`: `doc()`, `ghi()` (UTF-8, mỗi dòng 1 đối tượng, trường nối bằng `\|`). Mỗi `DSxxx` có `docFile()` / `ghiFile()`. `app/DuLieu.docTatCa()` được gọi đầu `Main.main()`. Menu gọi `ghiFile()` ngay sau thêm/sửa/xóa (vd `menu/MenuKhachHang.java`). | "13 danh sách, 13 file txt; mở chương trình là đọc hết, thay đổi gì là ghi lại ngay." Demo: thêm 1 khách rồi mở `data/khachhang.txt` thấy dòng mới. |
| **Interface** | `core/DoiTuong.java` (`nhap()`, `xuat()`, `toLine()`). Được implement bởi `Nguoi`, `Phong` và 10 lớp còn lại: `HDThuePhong`, `CTHopDong`, `CTKhachO`, `DichVu`, `CTSuDungDV`, `HoaDon`, `NhaCC`, `HangHoa`, `PhieuNhap`, `CTPhieuNhap`. **Interface generic** `core/DanhSach.java` (`getDs()`, `getN()`, `them(T)`, `xuat()`, `docFile()`, `ghiFile()`): 13 lớp `DSxxx implements DanhSach<Xxx>`. `app/DuLieu.docTatCa()` duyệt mảng `DanhSach<?>[] TAT_CA` và gọi `d.docFile()`. | "Mọi đối tượng đều phải nhập, xuất và ghi ra được 1 dòng file, nên nhóm ép bằng interface `DoiTuong`. Mọi danh sách đều giữ 1 mảng và đọc/ghi 1 file, nên ép bằng `DanhSach<T>`; nhờ vậy `DuLieu` gom 13 danh sách vào 1 mảng và đọc file bằng 1 vòng for." |
| **Đa hình** (bắt buộc) | `model/Phong.java` là `abstract`, có `abstract String getLoaiPhong()`. `PhongThuong` override `getLoaiPhong`, `xuat`; `PhongDeluxe`, `PhongSuite` override `getLoaiPhong`, `nhap`, `xuat`, `toLine` (gọi `super.` rồi thêm phần riêng). Gọi qua mảng cha `Phong[]` trong `danhsach/DSPhong.java`: `xuat(Phong[] mang)` → `p.xuat()`; `ghiFile()` → `ds[i].toLine()`; `them()` → `Phong p = taoTheoLoai(); p.nhap();`. `menu/MenuThongKe.tinhTrangPhongTheoLoai()` → `p.getLoaiPhong()`. Thêm: `Nguoi` (abstract) → `KhachHang`, `NhanVien` override `nhap()`, `xuat()` (gọi `super.nhap()`, `super.xuat()`) và cài `toLine()`. | "Một mảng `Phong[]` chứa lẫn 3 loại phòng; cùng một lệnh `p.xuat()` nhưng Deluxe in thêm view, ban công, Suite in số phòng con — Java tự gọi đúng hàm của lớp con lúc chạy." Demo: menu 1 → 5. |
| **static** | `app/DuLieu.java`: 13 danh sách `public static final` (mỗi danh sách chỉ 1 bản dùng chung). Mỗi lớp có hỏi bàn phím (34 lớp, vd `model/Nguoi.java`, `menu/MenuKho.java`) tự khai báo `private static final Scanner sc`: 1 Scanner cho cả lớp, dùng chung giữa các đối tượng của lớp đó. `core/NhapLieu.java`: các hàm kiểm tra static, nhận Scanner qua tham số. `core/DinhDang`, `core/TepTin`, `core/TimKiem`: lớp tiện ích toàn hàm static. Hằng: `Phong.TRONG/DA_THUE/THUONG/DELUXE/SUITE`, `HoaDon.CHUA_TT/DA_TT`, `NhanVien.CAC_CHUC_VU`. Hàm static trong model: `CTSuDungDV.trongKhoang()`. | "Cái gì dùng chung, không thuộc riêng đối tượng nào thì để static: dữ liệu chung, hàm kiểm tra nhập, hằng trạng thái. Scanner thì mỗi lớp tự khởi tạo riêng, không lấy từ lớp khác, nên lớp nào cũng tự chạy được; `NhapLieu` chỉ kiểm tra dữ liệu." |
| **Nhập hàng → tăng tồn** | `danhsach/DSPhieuNhap.java` `lapPhieu()`: với mỗi dòng gọi `DuLieu.dsHangHoa.congKho(maH, sl)` và cập nhật giá nhập. `danhsach/DSHangHoa.java` `congKho()`. | "Kho chỉ được cộng ở đúng 1 chỗ là lúc lưu phiếu nhập; hủy giữa chừng thì chưa cộng gì." Demo mục 3.7c. |
| **Dùng dịch vụ → giảm tồn** | `danhsach/DSCTSuDungDV.java` `ghiDichVu(hd, dv, ngaySD, sl)`: nếu dịch vụ có hàng thì gọi `DuLieu.dsHangHoa.truKho(...)` **trước khi lưu**. `DSHangHoa.truKho()` trả `false` khi không đủ hàng (không trừ). | "Trừ kho trước, đủ hàng mới ghi dịch vụ; thiếu hàng thì báo lỗi, kho giữ nguyên." Demo mục 3.4b và bẫy 4.12. |
| **Mỗi đối tượng 1 dòng** | `xuat()` của từng model, vd `KhachHang.xuat()`: in mã, gọi `super.xuat()` (họ tên, SĐT) rồi `println(diaChi)`. `Phong.xuat()` in phần chung, lớp con in phần riêng rồi xuống dòng. `DSxxx.xuat()` chỉ lặp và gọi `xuat()`. | "Mỗi lớp tự in mình trên 1 dòng, danh sách chỉ việc lặp." |
| **Thống kê (có tiền)** | `menu/MenuThongKe.java` (10 mục): `doanhThuTheoThangNam()`, `doanhThuTheoPhong()`, `DSHangHoa.xuatTonKho()` (tổng giá trị tồn), `DSPhieuNhap.thongKeTheoThang()` / `thongKeTheoNhaCC()` / `thongKeTheoNhanVien()`, `DSNhanVien.thongKeTheoChucVu()` (tổng lương), `DSCTSuDungDV.thongKeTheoDichVu()`. Chỗ khác có tiền: `DSHoaDon.xuatChuaThanhToan()` (tổng chưa thu), `DSHoaDon.xuatChiTiet()` (còn phải trả), `DSCTSuDungDV.xuatTheoHopDong()` (tổng tiền DV). | "Chỗ nào có tiền thì có thống kê: doanh thu, chi phí nhập, lương, giá trị tồn kho, tiền dịch vụ." Demo mục 3.8. |
| **Kiểm tra nhập, không lưu trùng** | `core/NhapLieu.java` (sai thì báo và hỏi lại: chữ vào ô số, số âm, ngày sai, `Infinity`, ký tự `\|`). Chống trùng: `DSKhachHang.trungSdt`, `DSNhanVien.trungSdt`, `DSPhong.trungTenP`, `DSHangHoa/DSDichVu/DSNhaCC.trungTen`. Mã tự sinh `sinhMa()` = số lớn nhất + 1. Xóa có kiểm tra tham chiếu trong `xoa(ma)` của 6 danh mục. | "Người dùng không bao giờ gõ mã nên không thể trùng mã; còn SĐT, số phòng, tên hàng/dịch vụ/NCC thì kiểm tra trước khi lưu." |
| **Gói code thuộc lòng** | 50%: `DSKhachHang`, `DSNhanVien`, `DSPhong` (`them`, `sua`, `xoa`, `timTheoTen`, `xuat`). 70%: `DuLieu`, `DSHoaDon.lapHoaDon()` + `HoaDon.tinhTien()`, `DSPhieuNhap.lapPhieu()`, các hàm thống kê ở trên. | |

---

## 3. Kịch bản theo chức năng

Chạy **liên tục theo thứ tự** từ dữ liệu mẫu vừa nạp (mục 1.2). Mỗi bước bắt đầu ở **menu chính**; cuối bước gõ `0` để về menu chính.

### 3.1 Menu 1 — Quản lý phòng

| # | Gõ | Thấy |
|---|---|---|
| a | `1 → 5` (xem tất cả) | 8 phòng, mỗi phòng 1 dòng; Deluxe có thêm `View … \| Có ban công \| Nội thất …`, Suite có `2 phòng con \| Có phòng khách riêng` (**đa hình**) |
| b | `1 → 1 → 2` (Deluxe) → "204" → "800000" → "2" → "1" → "Núi" → "c" → "Gỗ óc chó" | `Đã thêm phòng P009` (mã tự sinh, tình trạng TRONG) |
| c | `1 → 2` → "P009" → "204" → "850000" → "2" → "1" → "Núi" → "k" → "Hiện đại" | `Đã cập nhật phòng P009` |
| d | `1 → 4` → "P009" | `P009 \| DELUXE \| Phòng 204 \| 850.000 đ/ngày \| … \| Không ban công \| Nội thất Hiện đại` |
| e | `1 → 3` → "P009" | `Đã xóa phòng có mã: P009` |
| f | `1 → 6` (phòng trống) | 7 phòng, không có P002 (đang thuê) |
| g | `1 → 7 → 3` (lọc Suite) | P007, P008 |
| h | `1 → 8` (sắp theo giá) | `Phòng theo giá thuê tăng dần:` 101, 103, 102, 201, 203, 202, 301, 302 |
| i | `1 → 9` → "20" (tìm theo tên/số phòng) | 201, 202, 203 |

### 3.2 Menu 2 — Quản lý khách hàng

| # | Gõ | Thấy |
|---|---|---|
| a | `2 → 7` | KH001–KH006 |
| b | `2 → 1` → "Hoàng Anh Tuấn" → "0912345678" → "Gò Vấp, TP.HCM" | `Đã thêm khách hàng KH007` |
| c | `2 → 2` → "KH007" → "Hoàng Anh Tuấn" → "0912345679" → "Quận 3, TP.HCM" | `Đã cập nhật khách hàng KH007` |
| d | `2 → 4` → "KH007" · `2 → 5` → "0912345679" · `2 → 6` → "tuan" | Cả 3 đều ra `KH007 \| Hoàng Anh Tuấn \| 0912345679 \| Quận 3, TP.HCM` (tìm tên gõ không dấu được) |
| e | `2 → 3` → "KH007" | `Đã xóa khách hàng có mã: KH007` |

### 3.3 Menu 3 — Thuê phòng

**a. Lập hợp đồng 2 phòng, có thêm khách mới tại chỗ**

`3 → 1`, rồi lần lượt:

1. "0908889999" (tìm khách theo SĐT) → `-> Đã chọn khách: KH005 - Võ Quốc Bảo`
2. "NV001" (nhân viên lập)
3. Ngày nhận "25/11/2026", ngày trả "27/11/2026", tiền cọc "500000"
4. Phòng "P005" → số người "2" → khách 1 "KH005" → `Thêm khách ở phòng P005 (c/k)` "c" → khách 2 "0933333333" → `Chưa có khách hàng này, tiến hành thêm khách mới:` → "Bùi Thanh Tâm" → "0933333333" → "Vũng Tàu" → `Đã thêm khách hàng KH007`
5. Phòng "P008" → số người "1" → khách "KH003"
6. "0" (kết thúc chọn phòng) → `Đã lập hợp đồng HDTP004 thành công!`

> Ngày nhận phải từ hôm nay trở đi. Nếu demo sau 25/11 thì đổi sang ngày sau hôm nay.

| # | Gõ | Thấy |
|---|---|---|
| b | `3 → 4` (tất cả) | HDTP001–HDTP004, mỗi HĐ 1 dòng, có số ngày và tiền cọc |
| c | `3 → 5` (chưa thanh toán) | HDTP003, HDTP004 |
| d | `3 → 3` → "HDTP004" · `3 → 6` → "bao" | `HDTP004 \| KH: KH005 \| NV: NV001 \| Lập: <hôm nay> \| Nhận: 25/11/2026 \| Trả: 27/11/2026 (2 ngày) \| Cọc: 500.000 đ` |
| e | `3 → 7` → "HDTP004" | `Phòng P005 \| Đơn giá: 700.000 đ \| Số người: 2 \| Thành tiền: 1.400.000 đ` và `Phòng P008 … Thành tiền: 3.000.000 đ` |
| f | `3 → 8` → "HDTP004" → "P005" | `HĐ HDTP004 \| Phòng P005 \| Khách KH005` và `… Khách KH007` |
| g | `3 → 2` → "HDTP004" → "28/11/2026" (khách ở thêm 1 ngày) | `Đã cập nhật ngày trả phòng cho hợp đồng HDTP004` |
| h | `3 → 7` → "HDTP004" | Thành tiền tự tính lại: P005 `2.100.000 đ`, P008 `4.500.000 đ` |

### 3.4 Menu 4 — Dịch vụ

| # | Gõ | Thấy |
|---|---|---|
| a | `4 → 5` | DV001–DV004, ghi rõ `dùng hàng HH00x` / `không dùng hàng` |
| b | `4 → 1` → "Ăn sáng buffet" → "120000" → "k" | `Đã thêm dịch vụ DV005` |
| c | `4 → 2` → "DV005" → "Ăn sáng buffet" → "150000" → "k" | `Đã cập nhật dịch vụ DV005` |
| d | `4 → 4` → "DV005" · `4 → 6` → "an sang" | `DV005 \| Ăn sáng buffet \| 150.000 đ \| không dùng hàng` |
| e | `4 → 3` → "DV005" | `Đã xóa dịch vụ có mã: DV005` |
| f | `4 → 7` → "HDTP003" → "DV004" → "19/11/2026" → "2" | `Đã ghi Giặt ủi cho hợp đồng HDTP003:` `HDTP003 \| DV004 \| 19/11/2026 \| 2 x 50.000 đ = 100.000 đ` |
| g | **Trừ kho:** `7 → 2 → 4` → "HH001" (xem trước) | `HH001 \| Nước suối \| giá nhập 4.500 đ \| tồn 138` |
|   | `4 → 7` → "HDTP003" → "DV001" → "19/11/2026" → "3" | `Đã ghi Nước suối cho hợp đồng HDTP003:` `… 3 x 15.000 đ = 45.000 đ` |
|   | `7 → 2 → 4` → "HH001" (xem lại) | `tồn 135` (giảm đúng 3) |
| h | `4 → 8` → "HDTP003" | 4 dòng dịch vụ, `Tổng tiền dịch vụ: 235.000 đ` |
| i | `4 → 9` (thống kê theo dịch vụ) | `Nước suối: 2 lượt, 10 đơn vị, 150.000 đ` · `Bia lon: 1 lượt, 6 đơn vị, 180.000 đ` · `Mì ly: 1 lượt, 4 đơn vị, 80.000 đ` · `Giặt ủi: 1 lượt, 1 đơn vị, 50.000 đ` — **chỉ tính hợp đồng đã thanh toán**, nên dịch vụ vừa ghi cho HDTP003 chưa được cộng (sẽ cộng sau bước 3.5c) |

### 3.5 Menu 5 — Trả phòng & hóa đơn

| # | Gõ | Thấy |
|---|---|---|
| a | `5 → 1` → "HDTP003" → "NV001" | `Đã lập hóa đơn HDON003:` rồi in chi tiết (xem khung dưới) |
| b | `5 → 5` (chưa thanh toán) | `HDON003 … = 1.285.000 đ \| Chưa thanh toán` · `Có 1 hóa đơn chưa thanh toán, tổng 1.285.000 đ` |
| c | `5 → 2` → "HDON003" → "c" | In lại hóa đơn, `Đã thanh toán hóa đơn HDON003` |
| d | `5 → 3` → "HDON001" | Hóa đơn tháng 10: tổng `1.010.000 đ`, cọc `500.000 đ`, còn phải trả `510.000 đ`, `Đã thanh toán` |
| e | `5 → 4` | 3 hóa đơn, mỗi hóa đơn 1 dòng: `phòng … + DV … = … \| Đã thanh toán` |
| f | `5 → 6` → "01/11/2026" → "30/11/2026" | HDON002, HDON003 · `Có 2 hóa đơn` |
| g | `1 → 6` (phòng trống) | Có lại **P002** — trả phòng đã đổi phòng về TRONG |

```text
----- HÓA ĐƠN HDON003 -----
Hợp đồng    : HDTP003
Ngày lập    : <hôm nay>
Nhân viên   : Nguyễn Thị Lan
Tiền phòng:
  Phòng P002: 1.050.000 đ
  Cộng tiền phòng: 1.050.000 đ
Tiền dịch vụ:
  18/11/2026 Nước suối: 2 x 15.000 đ = 30.000 đ
  18/11/2026 Mì ly: 3 x 20.000 đ = 60.000 đ
  19/11/2026 Giặt ủi: 2 x 50.000 đ = 100.000 đ
  19/11/2026 Nước suối: 3 x 15.000 đ = 45.000 đ
  Cộng tiền dịch vụ: 235.000 đ
TỔNG CỘNG   : 1.285.000 đ
Đã cọc      : 300.000 đ
Còn phải trả: 985.000 đ
Trạng thái  : Chưa thanh toán
```

### 3.6 Menu 6 — Nhân viên

| # | Gõ | Thấy |
|---|---|---|
| a | `6 → 5` | NV001–NV004 |
| b | `6 → 1` → "Đỗ Văn Khoa" → "0911000005" → "5" (Bảo vệ) → "6000000" | `Đã thêm nhân viên NV005` |
| c | `6 → 2` → "NV005" → "Đỗ Văn Khoa" → "0911000005" → "5" → "6500000" | `Đã cập nhật nhân viên NV005` |
| d | `6 → 4` → "NV005" | `NV005 \| Đỗ Văn Khoa \| 0911000005 \| Bảo vệ \| 6.500.000 đ` |
| e | `6 → 6` → "lan" | `NV001 \| Nguyễn Thị Lan \| …` |
| f | `6 → 7` (lương giảm dần) | NV002 15tr, NV003 9tr, NV001 8tr, NV004 7tr, NV005 6,5tr |
| g | `6 → 3` → "NV005" | `Đã xóa nhân viên có mã: NV005` |

### 3.7 Menu 7 — Kho

**a. Nhà cung cấp** (`7 → 1`, rồi:)

| Gõ | Thấy |
|---|---|
| `5` | NCC001, NCC002 |
| `1` → "Công ty Khăn Việt" → "88 Hai Bà Trưng, Quận 1" → "0283334444" | `Đã thêm nhà cung cấp NCC003` |
| `2` → "NCC003" → "Công ty Khăn Việt" → "90 Hai Bà Trưng, Quận 1" → "0283334444" | `Đã cập nhật nhà cung cấp NCC003` |
| `4` → "NCC003" · `6` → "khan" | `NCC003 \| Công ty Khăn Việt \| 90 Hai Bà Trưng, Quận 1 \| 0283334444` |
| `3` → "NCC003" | `Đã xóa nhà cung cấp có mã: NCC003` |

**b. Hàng hóa** (`7 → 2`, rồi:)

| Gõ | Thấy |
|---|---|
| `5` | HH001 tồn 135, HH002 66, HH003 5, HH004 50 |
| `1` → "Bàn chải đánh răng" → "3000" | `Đã thêm hàng HH005 (tồn kho = 0, hãy lập phiếu nhập)` |
| `2` → "HH005" → "Bàn chải" | `Đã cập nhật hàng HH005` (chỉ sửa tên; giá và tồn chỉ đổi qua phiếu nhập / dịch vụ) |
| `4` → "HH005" · `6` → "ban chai" | `HH005 \| Bàn chải \| giá nhập 3.000 đ \| tồn 0` |
| `3` → "HH005" | `Đã xóa hàng có mã: HH005` |
| `7` → "10" (hàng sắp hết) | `Có 1 mặt hàng cần nhập thêm:` `HH003 \| Mì ly \| … \| tồn 5` |

**c. Phiếu nhập — nhập hàng làm TĂNG tồn** (`7 → 3`, rồi:)

| Gõ | Thấy |
|---|---|
| `2` | PN001, PN002, PN003 |
| `3` → "PN001" | Chi tiết: `1. Nước suối: 100 x 4.000 đ = 400.000 đ`, `2. Bia lon: 48 x 11.000 đ = 528.000 đ`, `Tổng tiền   : 928.000 đ` |
| `4` → "01/10/2026" → "31/10/2026" | PN001, PN002 · `Có 2 phiếu` |
| `1` → "NCC002" → "NV003" → "HH003" → "24" → "8000" → "c" → "HH004" → "20" → "2000" → "k" → `Lưu phiếu nhập (c/k)` "c" | `Đã lập phiếu nhập PN004:` `1. Mì ly: 24 x 8.000 đ = 192.000 đ` `2. Khăn giấy: 20 x 2.000 đ = 40.000 đ` `Tổng tiền   : 232.000 đ` |
| `0 → 2 → 5` (xem lại hàng) | **Mì ly tồn 5 → 29** (giá nhập thành 8.000 đ), **Khăn giấy 50 → 70** |

### 3.8 Menu 8 — Thống kê (số liệu sau khi đã chạy 3.1 → 3.7)

| Gõ | Thấy |
|---|---|
| `8 → 1` → "2026" → "10" | `Doanh thu: 1.010.000 đ` (trước khi chạy 3.5 thì tháng 11 = `5.750.000 đ`) |
| `8 → 1` → "2026" → "11" | `Doanh thu: 7.035.000 đ` |
| `8 → 1` → "2026" → "0" (cả năm) | `Doanh thu: 8.045.000 đ` |
| `8 → 2` | `THUONG: 3 trống, 0 đã thuê` · `DELUXE: 2 trống, 1 đã thuê` · `SUITE: 1 trống, 1 đã thuê` |
| `8 → 3` | `Phòng 101: 900.000 đ`, `Phòng 102: 1.050.000 đ`, `Phòng 201: 1.800.000 đ`, `Phòng 301: 3.600.000 đ`, phòng khác 0 đ |
| `8 → 4` (tồn kho) | Sắp theo tồn tăng dần (Mì ly 29 … Nước suối 135), `Tổng giá trị hàng trong kho: 1.771.500 đ` |
| `8 → 5` → "10" | `Có 0 mặt hàng cần nhập thêm:` (vì vừa nhập Mì ly). Trước bước 3.7c sẽ ra Mì ly tồn 5 |
| `8 → 6` → "2026" | `Tháng 10: 1.118.000 đ`, `Tháng 11: 745.000 đ`, `Cả năm 2026: 1.863.000 đ` |
| `8 → 7` | `Công ty Nước giải khát Sài Gòn: 2 phiếu, 1.441.000 đ` · `Công ty Thực phẩm Minh Phát: 2 phiếu, 422.000 đ` |
| `8 → 8` | `Trần Văn Hùng: 1 phiếu, 513.000 đ` · `Lê Minh Tâm: 3 phiếu, 1.350.000 đ` (NV khác 0) |
| `8 → 9` | `Lễ tân: 1 người, tổng lương 8.000.000 đ` … `Bảo vệ: 0 người` · `Toàn khách sạn: 4 nhân viên` |
| `8 → 10` | HDON003 đã thanh toán nên giờ có cả dịch vụ của HDTP003: `Nước suối: 4 lượt, 15 đơn vị, 225.000 đ` · `Bia lon: 1 lượt, 6 đơn vị, 180.000 đ` · `Mì ly: 2 lượt, 7 đơn vị, 140.000 đ` · `Giặt ủi: 2 lượt, 3 đơn vị, 150.000 đ` |

---

## 4. Bẫy cô hay thử

Mỗi bẫy chạy được ngay trên dữ liệu mẫu vừa nạp (trừ khi ghi "sau bước …"). Câu trong ngoặc là thông báo chương trình in ra; nhập sai thì chương trình **hỏi lại**, không dừng.

| # | Bẫy | Gõ | Kết quả |
|---|---|---|---|
| 4.1 | **Trùng mã** | Không có ô nào cho gõ mã: mã do `sinhMa()` sinh = số lớn nhất + 1. Thử: `2 → 1` "Khách A" "0921000001" "Huế" → `KH007`; `2 → 1` "Khách B" "0921000002" "Huế" → `KH008`; `2 → 3` "KH007"; `2 → 1` "Khách C" "0921000003" "Huế" | Khách C nhận **`KH009`**, không phải KH008 (nếu dùng n + 1 sẽ trùng KH008). |
| 4.2 | Trùng SĐT khách | `2 → 1` → "Người Trùng" → "0901112222" → "Huế" | `Số điện thoại đã có khách khác dùng, không thêm.` |
|  | Sửa thành SĐT của khách khác | `2 → 2` → "KH001" → "Nguyễn Văn Minh" → "0903334444" → "Quận 1" | `Số điện thoại đã có khách khác dùng, không sửa.` |
|  | Trùng SĐT nhân viên | `6 → 1` → "Trùng Số" → "0911000001" → "1" → "7000000" | `Số điện thoại đã có nhân viên khác dùng, không thêm.` |
|  | SĐT sai dạng | `2 → 1` → "A" → "12345" | `Số điện thoại gồm 10 chữ số, bắt đầu bằng 0.` (hỏi lại) |
| 4.3 | Trùng số phòng | `1 → 1 → 1` → "101" → "300000" → "2" → "1" | `Số phòng 101 đã có, không thêm.` |
|  | Sửa thành số phòng đã có | `1 → 2` → "P003" → "101" → "300000" → "2" → "1" | `Số phòng 101 đã có, không sửa.` |
| 4.4 | Trùng tên hàng (không phân biệt hoa thường) | `7 → 2 → 1` → "MÌ LY" → "8000" | `Tên hàng MÌ LY đã có, không thêm.` |
|  | Trùng tên dịch vụ | `4 → 1` → "nước SUỐI" → "20000" | `Tên dịch vụ nước SUỐI đã có, không thêm.` |
|  | Sửa dịch vụ thành tên đã có | `4 → 2` → "DV004" → "Bia lon" → "50000" | `Tên dịch vụ Bia lon đã có, không sửa.` |
|  | Trùng tên nhà cung cấp | `7 → 1 → 1` → "công ty thực phẩm MINH PHÁT" → "Q5" → "0281111111" | `Nhà cung cấp công ty thực phẩm MINH PHÁT đã có, không thêm.` |
|  | Sửa NCC thành tên đã có | `7 → 1 → 2` → "NCC002" → "Công ty Nước giải khát Sài Gòn" → "x" → "0287654321" | `Nhà cung cấp Công ty Nước giải khát Sài Gòn đã có, không sửa.` |
| 4.5 | Chữ vào ô số | Ở menu gõ "abc"; giá phòng "abc"; tiền cọc "abc"; số lượng DV "abc" | `Phải là số nguyên.` / `Phải là số.` (hỏi lại) |
|  | Chọn ngoài menu | Menu chính gõ "9" | `Phải từ 0 đến 8.` |
|  | Để trống / có ký tự `\|` | Họ tên Enter trống; địa chỉ "A\|B" | `Không được để trống.` / `Không được chứa ký tự \|` |
| 4.6 | Số âm | Giá phòng "-5"; lương "-1"; tiền cọc "-100"; đơn giá nhập "-8000" | `Phải lớn hơn hoặc bằng 1 đ.` (giá phòng, đơn giá nhập) / `Phải lớn hơn hoặc bằng 0 đ.` (lương, cọc) |
|  | Số lượng 0 hoặc quá lớn | SL nhập hàng "0"; SL dịch vụ "1001"; số người tối đa "25"; chức vụ "9" | `Phải từ 1 đến 100000.` / `Phải từ 1 đến 1000.` / `Phải từ 1 đến 20.` / `Phải từ 1 đến 5.` |
| 4.7 | Ngày sai 31/02 | Lập HĐ: ngày nhận "31/02/2026" (hoặc "abc") | `Ngày không hợp lệ.` |
|  | Ngày nhận trước hôm nay | Ngày nhận "01/11/2026" | `Ngày nhận phòng không được trước ngày lập hợp đồng.` |
| 4.8 | Ngày trả ≤ ngày nhận | Nhận "25/11/2026", trả "25/11/2026" hoặc "24/11/2026" | `Ngày trả phải sau ngày nhận phòng.` |
|  | Ngày dùng DV ngoài thời gian thuê | `4 → 7` → "HDTP003" → "DV004" → "25/11/2026" | `Ngày sử dụng phải từ 17/11/2026 đến 20/11/2026.` |
| 4.9 | Quá sức chứa phòng | Lập HĐ, chọn "P005" (tối đa 3 người), gõ "5" | `Phải từ 1 đến 3.` |
| 4.10 | Thuê phòng đang DA_THUE | Lập HĐ, chọn "P002" (hoặc mã không có "P999") | `Phòng không tồn tại hoặc không ở trạng thái TRONG.` |
|  | Chọn 1 phòng 2 lần trong cùng HĐ | Chọn "P005" xong lại gõ "P005" | `Phòng P005 đã có trong hợp đồng này rồi.` |
|  | Chọn 1 khách 2 lần trong cùng phòng | Phòng "P005" → "2" người → khách 1 "KH005" → "c" → khách 2 "KH005" | `Khách KH005 đã có trong phòng P005, chọn khách khác.` rồi hỏi lại `Thêm khách ở phòng P005 (c/k)` |
| 4.11 | Xóa phòng đã có HĐ | `1 → 3` → "P001" | `Phòng đã có trong hợp đồng, không xóa được.` |
|  | Xóa khách đứng tên HĐ | `2 → 3` → "KH001" | `Khách đã đứng tên hợp đồng, không xóa được.` |
|  | Xóa khách đang ở cùng | `2 → 3` → "KH002" | `Khách đã có trong danh sách khách ở, không xóa được.` |
|  | Xóa nhân viên | `6 → 3` → "NV003" / "NV001" / "NV004" | `Nhân viên đã lập phiếu nhập…` / `…đã lập hóa đơn…` / `…đã lập hợp đồng, không xóa được.` |
|  | Xóa hàng đã nhập kho | `7 → 2 → 3` → "HH001" | `Hàng đã có trong phiếu nhập, không xóa được.` |
|  | Xóa hàng đang gắn dịch vụ | `7 → 2 → 1` "Bàn chải" "3000" → `0 0` → `4 → 1` "Bàn chải" "5000" "c" "HH005" → `0` → `7 → 2 → 3` "HH005" | `Hàng đang dùng cho dịch vụ DV005, không xóa được.` |
|  | Xóa dịch vụ đã có khách dùng | `4 → 3` → "DV001" | `Dịch vụ đã có khách dùng, không xóa được.` |
|  | Xóa NCC đã có phiếu nhập | `7 → 1 → 3` → "NCC001" | `Nhà cung cấp đã có phiếu nhập, không xóa được.` |
| 4.12 | Dùng DV khi kho không đủ | `4 → 7` → "HDTP003" → "DV003" (Mì ly, tồn 5) → "18/11/2026" → "10" | `Kho không đủ hàng HH003 cho dịch vụ này, không ghi được.` (tồn vẫn 5; ghi "5" thì được và tồn về 0) |
|  | Ghi DV cho HĐ đã có hóa đơn | `4 → 7` → "HDTP001" | `Hợp đồng đã lập hóa đơn, không ghi thêm dịch vụ được.` |
| 4.13 | Lập hóa đơn 2 lần | Sau 3.5a: `5 → 1` → "HDTP003" (hoặc ngay từ đầu: "HDTP001") | `Hợp đồng này đã có hóa đơn HDON003.` |
|  | Sửa ngày trả HĐ đã có hóa đơn | `3 → 2` → "HDTP001" | `Hợp đồng đã lập hóa đơn, không thể sửa.` |
|  | Rút ngắn ngày trả trước ngày đã dùng dịch vụ | Sau 3.4f (Giặt ủi 19/11): `3 → 2` → "HDTP003" → "18/11/2026" | `Đã có dịch vụ dùng ngày 19/11/2026, ngày trả không được sớm hơn ngày đó. Không sửa.` (ngày trả vẫn 20/11) |
|  | Trả phòng sớm hơn hợp đồng | Sau 3.3: `5 → 1` → "HDTP004" → "k" | `Lưu ý: ngày trả trong hợp đồng là 28/11/2026. Nếu khách trả sớm, hãy sửa ngày trả ở menu Thuê phòng trước.` → `Đã hủy lập hóa đơn.` |
| 4.14 | Thanh toán 2 lần | `5 → 2` → "HDON001" | `Hóa đơn HDON001 đã thanh toán rồi.` |
|  | Không xác nhận thanh toán | Sau 3.5a: `5 → 2` → "HDON003" → "k" | `Đã hủy thanh toán.` (vẫn CHUA_TT) |
| 4.15 | **Hủy giữa chừng (gõ 0)** | Lập HĐ: `3 → 1` → "0" | `Đã hủy lập hợp đồng.` |
|  |  | Lập HĐ tới bước nhân viên → "0" | `Đã hủy lập hợp đồng.` |
|  |  | Lập HĐ tới bước chọn phòng, chưa chọn phòng nào → "0" | `Đã hủy lập hợp đồng.` (phòng vẫn TRONG, không sinh HDTP004) |
|  |  | Lập HĐ tới bước khách ở thứ 2 → "0" | `Đã hủy lập hợp đồng.` |
|  |  | Ghi DV: `4 → 7` → "HDTP003" → "0" | `Đã hủy ghi dịch vụ.` |
|  |  | Trả phòng: `5 → 1` → "HDTP003" → "0" (ở bước nhân viên) | `Đã hủy lập hóa đơn.` |
|  |  | Phiếu nhập: `7 → 3 → 1` → "0" · hoặc chọn NCC rồi "0" ở nhân viên | `Đã hủy lập phiếu nhập.` |
|  |  | Phiếu nhập: chọn NCC, NV rồi "0" ngay ở mã hàng | `Phiếu phải có ít nhất 1 mặt hàng. Đã hủy lập phiếu nhập.` |
|  |  | Phiếu nhập: nhập đủ hàng, tới `Lưu phiếu nhập (c/k)` gõ "k" | `Đã hủy lập phiếu nhập.` — xem lại kho: tồn **không đổi** |
| 4.16 | `Infinity` / `1e400` / `NaN` vào ô tiền | Giá phòng "Infinity"; tiền cọc "Infinity", "1e400", "NaN" | `Phải lớn hơn hoặc bằng 1 đ.` / `Phải lớn hơn hoặc bằng 0 đ.` (hỏi lại) |
| 4.17 | Cọc nhiều hơn tổng tiền | `3 → 1` → "KH001" → "NV001" → nhận hôm nay, trả ngày mai → cọc "5000000" → phòng "P001" → "1" → "KH001" → "0" → `0` → `5 → 1` → "HDTP004" → "c" → "NV001" | Hóa đơn in `Đã cọc : 5.000.000 đ` và `Trả lại khách: 4.700.000 đ` (không in số âm) |
| 4.18 | Hóa đơn > 1 triệu | Mục 3.5a (HDON003 = `1.285.000 đ`), `5 → 3` "HDON002" (`5.750.000 đ`) | Tiền in có dấu chấm ngăn cách; trong `data/hoadon.txt` ghi số thường `1285000`, `5750000` (không bị dạng `1.2E7`, nhờ `DinhDang.dong()`) |
