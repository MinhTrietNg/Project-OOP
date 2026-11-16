# Đồ án OOP — Quản lý khách sạn

## 1. Tổng quan

Chương trình console quản lý một khách sạn nhỏ, viết bằng **Java**, lưu dữ liệu bằng **file `.txt`**.

Nghiệp vụ:

- **Phòng**: 3 loại phòng là thường, Deluxe và Suite, cùng kế thừa từ lớp trừu tượng `Phong`.
- **Khách hàng**: lưu thông tin người thuê.
- **Thuê phòng**: lập hợp đồng thuê. Một hợp đồng thuê được nhiều phòng, mỗi phòng có thể có nhiều khách ở. Mọi phòng trong cùng một hợp đồng nhận và trả cùng ngày.
- **Dịch vụ và thanh toán**: khách dùng dịch vụ trong thời gian ở; lúc trả phòng lập hóa đơn gồm tiền phòng và tiền dịch vụ.
- **Nhân sự, kho**: nhân viên lập hợp đồng, hóa đơn, phiếu nhập. Hàng hóa nhập từ nhà cung cấp vào kho và bị trừ dần khi khách dùng dịch vụ có tiêu hao hàng.
- **Thống kê**: doanh thu, tình trạng phòng, tồn kho.

Chương trình chia thành 4 module:

| Module | Lớp đối tượng | Menu |
|---|---|---|
| Phòng & Khách hàng | `Nguoi`, `KhachHang`, `Phong`, `PhongThuong`, `PhongDeluxe`, `PhongSuite` | `MenuPhong`, `MenuKhachHang` |
| Thuê phòng | `HDThuePhong`, `CTHopDong`, `CTKhachO` | `MenuThuePhong` |
| Dịch vụ & Thanh toán | `DichVu`, `CTSuDungDV`, `HoaDon` | `MenuDichVu`, `MenuHoaDon` |
| Nhân sự, Kho & Thống kê | `NhanVien`, `NhaCC`, `HangHoa`, `PhieuNhap`, `CTPhieuNhap` | `MenuNhanVien`, `MenuKho`, `MenuThongKe` |

## 2. Quy ước chung

### 2.1 Môi trường

- Java 8 trở lên, chỉ dùng thư viện chuẩn (`java.util`, `java.io`, `java.nio`, `java.time`). Không dùng thư viện ngoài.
- Không phụ thuộc IDE: mở được bằng IntelliJ, VS Code, Eclipse hoặc `javac`.
- Chạy chương trình từ **thư mục gốc project**, vì đường dẫn file dữ liệu là đường dẫn tương đối `data/...`.
- File dữ liệu đọc/ghi bằng **UTF-8**.

### 2.2 Cấu trúc thư mục

```text
src/
  app/        Main.java (menu chính)
  core/       lớp dùng chung cho mọi module (nhập liệu, đọc/ghi file, danh sách)
  model/      19 lớp đối tượng (mục 3, 4)
  danhsach/   lớp danh sách DSxxx, ví dụ DSPhong, DSCTHopDong
  menu/       MenuPhong, MenuKhachHang, MenuThuePhong, MenuDichVu,
              MenuHoaDon, MenuNhanVien, MenuKho, MenuThongKe
data/         13 file .txt (mục 5)
```

### 2.3 Đặt tên

- Tên lớp, thuộc tính, phương thức: tiếng Việt **không dấu**, đúng như sơ đồ (`PhongDeluxe`, `soNguoiToiDa`, `timTheoMa`).
- Lớp viết `PascalCase`, thuộc tính và phương thức viết `camelCase`. Package viết thường.
- Thuộc tính luôn `private`, truy cập qua getter/setter (`getDonGia()`, `setDonGia(...)`). Riêng `boolean` dùng `isBanCong()`.
- Chữ hiển thị cho người dùng (menu, thông báo) viết tiếng Việt có dấu.

### 2.4 Mã đối tượng

Mã do chương trình **tự sinh**, người dùng không nhập. Mã gồm tiền tố cộng số thứ tự 3 chữ số; số mới bằng số lớn nhất đang có cộng 1.

| Lớp | Tiền tố | Ví dụ |
|---|---|---|
| `Phong` | `P` | `P001` |
| `KhachHang` | `KH` | `KH001` |
| `NhanVien` | `NV` | `NV001` |
| `HDThuePhong` | `HDTP` | `HDTP001` |
| `HoaDon` | `HDON` | `HDON001` |
| `DichVu` | `DV` | `DV001` |
| `HangHoa` | `HH` | `HH001` |
| `NhaCC` | `NCC` | `NCC001` |
| `PhieuNhap` | `PN` | `PN001` |

Các lớp chi tiết (`CTHopDong`, `CTKhachO`, `CTSuDungDV`, `CTPhieuNhap`) không có mã riêng. Chúng được nhận diện bằng mã của các lớp chúng nối tới.

### 2.5 Kiểu dữ liệu

| Loại | Kiểu Java | Ghi chú |
|---|---|---|
| Mã, chữ | `String` | Không được chứa ký tự `\|` |
| Tiền | `double` | Đơn vị VNĐ |
| Số lượng | `int` | |
| Có/không | `boolean` | Trong file ghi `true` / `false` |
| Ngày | `java.time.LocalDate` | Hiển thị, nhập và lưu file theo dạng `dd/MM/yyyy` |
| Số ngày | `long` | `ChronoUnit.DAYS.between(ngayNhan, ngayTra)` |

### 2.6 Giá trị cố định

| Thuộc tính | Giá trị |
|---|---|
| `Phong.tinhTrang` | `TRONG`, `DA_THUE` |
| `HoaDon.trangThai` | `CHUA_TT`, `DA_TT` |
| Loại phòng (trong file) | `THUONG`, `DELUXE`, `SUITE` |

Khai báo các giá trị này thành hằng `public static final String` trong lớp tương ứng (ví dụ `Phong.TRONG`) và dùng hằng thay vì gõ chuỗi trực tiếp.

### 2.7 Menu

Menu chính:

```text
1. Quản lý phòng
2. Quản lý khách hàng
3. Thuê phòng
4. Dịch vụ
5. Trả phòng & hóa đơn
6. Nhân viên
7. Kho (nhà cung cấp, hàng hóa, phiếu nhập)
8. Thống kê
0. Thoát
```

- Mỗi mục mở một menu con, lặp cho tới khi người dùng chọn `0. Quay lại`.
- Mỗi menu danh mục (phòng, khách hàng, nhân viên, dịch vụ, hàng hóa, nhà cung cấp) có tối thiểu: thêm, sửa, xóa, tìm theo mã, xem tất cả.
- Nhập sai (gõ chữ vào ô số, ngày không hợp lệ…) thì báo lỗi và hỏi lại, không làm chương trình dừng.
- Sau mỗi thao tác thêm/sửa/xóa, ghi lại ngay các file vừa thay đổi.

## 3. Sơ đồ lớp đối tượng

```mermaid
classDiagram
    direction LR

    %% ===== Người, phòng =====
    class Nguoi {
        <<abstract>>
        -String hoTen
        -String sdt
    }
    class KhachHang {
        -String maKH
        -String diaChi
    }
    class Phong {
        <<abstract>>
        -String maP
        -String tenP
        -double donGia
        -int soNguoiToiDa
        -int slGiuong
        -String tinhTrang
        +getLoaiPhong()* String
    }
    class PhongThuong
    class PhongDeluxe {
        -String view
        -boolean banCong
        -String loaiNoiThat
    }
    class PhongSuite {
        -boolean phongKhachRieng
        -int slPhong
    }
    Nguoi <|-- KhachHang
    Nguoi <|-- NhanVien
    Phong <|-- PhongThuong
    Phong <|-- PhongDeluxe
    Phong <|-- PhongSuite

    %% ===== Thuê phòng =====
    class HDThuePhong {
        -String maHDong
        -String maKH
        -String maNV
        -LocalDate ngayLapHD
        -double tienCoc
        -LocalDate ngayNhanPhong
        -LocalDate ngayTraPhong
        +soNgayThue() long
    }
    class CTHopDong {
        -String maHDong
        -String maP
        -double donGia
        -int slNguoi
        -double thanhTien
    }
    class CTKhachO {
        -String maHDong
        -String maP
        -String maKH
    }

    %% ===== Dịch vụ, thanh toán =====
    class DichVu {
        -String maDV
        -String tenDV
        -double donGia
        -String maH
    }
    class CTSuDungDV {
        -String maHDong
        -String maDV
        -LocalDate ngaySD
        -int sl
        -double donGia
        -double thanhTien
    }
    class HoaDon {
        -String maHDon
        -String maHDong
        -String maNV
        -LocalDate ngayLapHD
        -double tongTienThue
        -double tongTienDV
        -double tongCong
        -String trangThai
    }

    %% ===== Nhân sự, kho =====
    class NhanVien {
        -String maNV
        -String chucVu
        -double luongCB
    }
    class NhaCC {
        -String maNCC
        -String tenNCC
        -String diaChi
        -String sdt
    }
    class HangHoa {
        -String maH
        -String tenH
        -double donGia
        -int slTon
    }
    class PhieuNhap {
        -String maPN
        -String maNCC
        -String maNV
        -LocalDate ngayNhap
        -double tongTien
    }
    class CTPhieuNhap {
        -String maPN
        -String maH
        -int sl
        -double donGiaNhap
        -double thanhTien
    }

    %% ===== Quan hệ =====
    KhachHang "1" -- "0..*" HDThuePhong
    NhanVien "1" -- "0..*" HDThuePhong
    HDThuePhong "1" -- "1..*" CTHopDong
    Phong "1" -- "0..*" CTHopDong
    CTHopDong "1" -- "1..*" CTKhachO
    KhachHang "1" -- "0..*" CTKhachO

    HDThuePhong "1" -- "0..*" CTSuDungDV
    DichVu "1" -- "0..*" CTSuDungDV
    HDThuePhong "1" -- "0..1" HoaDon
    NhanVien "1" -- "0..*" HoaDon
    HangHoa "0..1" -- "0..*" DichVu

    NhaCC "1" -- "0..*" PhieuNhap
    NhanVien "1" -- "0..*" PhieuNhap
    PhieuNhap "1" -- "1..*" CTPhieuNhap
    HangHoa "1" -- "0..*" CTPhieuNhap
```

Sơ đồ chỉ ghi thuộc tính và phương thức đặc trưng. Getter/setter, constructor, `nhap()`, `xuat()` lớp nào cũng có nên không vẽ lại.

### Cách đọc sơ đồ

- `A <|-- B`: **kế thừa**. B có mọi thuộc tính của A, thêm phần riêng. Lớp `<<abstract>>` không tạo đối tượng trực tiếp; phương thức có dấu `*` là abstract.
- `A "1" -- "0..*" B`: một A liên quan tới nhiều B. Bên B luôn có một thuộc tính chứa mã của A (ví dụ `CTHopDong.maP` → `Phong.maP`).
- `HangHoa "0..1" -- "0..*" DichVu`: một dịch vụ dùng **không hoặc một** loại hàng (ví dụ "Nước suối" dùng hàng nước suối, "Giặt ủi" không dùng hàng).
- Các lớp `CT...` (chi tiết) tách quan hệ nhiều–nhiều:
  - `CTHopDong`: hợp đồng nào thuê phòng nào, bao nhiêu người, giá bao nhiêu.
  - `CTKhachO`: nối `CTHopDong` (qua `maHDong` + `maP`) với `KhachHang`, cho biết khách nào ở phòng nào trong hợp đồng nào.
  - `CTSuDungDV`: hợp đồng nào dùng dịch vụ gì, ngày nào.
  - `CTPhieuNhap`: phiếu nhập nào nhập hàng gì, bao nhiêu.

## 4. Mô tả lớp

### 4.1 Người

#### `Nguoi` (abstract)

Phần chung của mọi người trong hệ thống.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| `hoTen` | `String` | Họ tên |
| `sdt` | `String` | Số điện thoại, 10 chữ số, bắt đầu bằng `0` |

#### `KhachHang` extends `Nguoi`

Người thuê phòng hoặc ở cùng trong phòng.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| `maKH` | `String` | Mã khách hàng |
| `diaChi` | `String` | Địa chỉ |

#### `NhanVien` extends `Nguoi`

Người lập hợp đồng, hóa đơn, phiếu nhập.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| `maNV` | `String` | Mã nhân viên |
| `chucVu` | `String` | Chức vụ (lễ tân, quản lý, thủ kho…) |
| `luongCB` | `double` | Lương cơ bản, ≥ 0 |

### 4.2 Phòng

#### `Phong` (abstract)

Phần chung của mọi loại phòng.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| `maP` | `String` | Mã phòng |
| `tenP` | `String` | Số phòng (ví dụ `101`), không trùng |
| `donGia` | `double` | Giá thuê một ngày, > 0 |
| `soNguoiToiDa` | `int` | Số người ở tối đa, ≥ 1 |
| `slGiuong` | `int` | Số giường, ≥ 1 |
| `tinhTrang` | `String` | `TRONG` / `DA_THUE` |

| Phương thức | Ý nghĩa |
|---|---|
| `getLoaiPhong()` (abstract) | Lớp con trả về `THUONG` / `DELUXE` / `SUITE` |

#### `PhongThuong` extends `Phong`

Phòng tiêu chuẩn, không có thuộc tính riêng.

#### `PhongDeluxe` extends `Phong`

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| `view` | `String` | Hướng nhìn của phòng |
| `banCong` | `boolean` | Có ban công hay không |
| `loaiNoiThat` | `String` | Loại nội thất |

#### `PhongSuite` extends `Phong`

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| `phongKhachRieng` | `boolean` | Có phòng khách riêng hay không |
| `slPhong` | `int` | Số phòng con trong suite, ≥ 2 |

### 4.3 Thuê phòng

#### `HDThuePhong`

Hợp đồng thuê phòng của một khách đứng tên.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| `maHDong` | `String` | Mã hợp đồng |
| `maKH` | `String` | Khách đứng tên → `KhachHang` |
| `maNV` | `String` | Nhân viên lập → `NhanVien` |
| `ngayLapHD` | `LocalDate` | Ngày lập hợp đồng |
| `tienCoc` | `double` | Tiền đặt cọc, ≥ 0 |
| `ngayNhanPhong` | `LocalDate` | Ngày nhận phòng, chung cho mọi phòng trong hợp đồng |
| `ngayTraPhong` | `LocalDate` | Ngày trả phòng, chung cho mọi phòng trong hợp đồng, sau `ngayNhanPhong` |

| Phương thức | Ý nghĩa |
|---|---|
| `soNgayThue()` | Số ngày giữa `ngayNhanPhong` và `ngayTraPhong` |

#### `CTHopDong`

Một phòng được thuê trong hợp đồng. Cặp (`maHDong`, `maP`) không trùng.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| `maHDong` | `String` | → `HDThuePhong` |
| `maP` | `String` | → `Phong` |
| `donGia` | `double` | Giá phòng **tại lúc thuê**, chép từ `Phong.donGia` |
| `slNguoi` | `int` | Số người ở phòng này, từ 1 tới `Phong.soNguoiToiDa` |
| `thanhTien` | `double` | `donGia` × `soNgayThue()` của hợp đồng |

#### `CTKhachO`

Một khách ở một phòng trong hợp đồng. Bộ (`maHDong`, `maP`, `maKH`) không trùng.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| `maHDong`, `maP` | `String` | → `CTHopDong` |
| `maKH` | `String` | → `KhachHang` |

### 4.4 Dịch vụ, thanh toán

#### `DichVu`

Dịch vụ khách sạn cung cấp (đồ uống, giặt ủi, ăn sáng…).

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| `maDV` | `String` | Mã dịch vụ |
| `tenDV` | `String` | Tên dịch vụ |
| `donGia` | `double` | Giá bán một lần dùng, > 0 |
| `maH` | `String` | Hàng hóa bị tiêu hao khi dùng → `HangHoa`; chuỗi rỗng `""` nếu dịch vụ không dùng hàng |

#### `CTSuDungDV`

Một lần khách dùng dịch vụ trong thời gian thuê.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| `maHDong` | `String` | → `HDThuePhong` |
| `maDV` | `String` | → `DichVu` |
| `ngaySD` | `LocalDate` | Ngày sử dụng, trong khoảng `ngayNhanPhong` – `ngayTraPhong` |
| `sl` | `int` | Số lượng, ≥ 1 |
| `donGia` | `double` | Giá dịch vụ tại lúc dùng, chép từ `DichVu.donGia` |
| `thanhTien` | `double` | `sl` × `donGia` |

#### `HoaDon`

Hóa đơn thanh toán, lập lúc trả phòng. Mỗi hợp đồng có tối đa một hóa đơn.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| `maHDon` | `String` | Mã hóa đơn |
| `maHDong` | `String` | → `HDThuePhong` |
| `maNV` | `String` | Nhân viên lập → `NhanVien` |
| `ngayLapHD` | `LocalDate` | Ngày lập hóa đơn |
| `tongTienThue` | `double` | Tổng `thanhTien` của các `CTHopDong` thuộc hợp đồng |
| `tongTienDV` | `double` | Tổng `thanhTien` của các `CTSuDungDV` thuộc hợp đồng |
| `tongCong` | `double` | `tongTienThue` + `tongTienDV` |
| `trangThai` | `String` | `CHUA_TT` / `DA_TT` |

### 4.5 Kho

#### `NhaCC`

Nhà cung cấp hàng hóa.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| `maNCC` | `String` | Mã nhà cung cấp |
| `tenNCC` | `String` | Tên nhà cung cấp |
| `diaChi` | `String` | Địa chỉ |
| `sdt` | `String` | Số điện thoại |

#### `HangHoa`

Hàng trong kho, dùng cho dịch vụ.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| `maH` | `String` | Mã hàng |
| `tenH` | `String` | Tên hàng |
| `donGia` | `double` | Giá nhập của lần nhập gần nhất (thêm mới thì nhập tay, sau đó cập nhật tự động mỗi lần nhập hàng) |
| `slTon` | `int` | Số lượng còn trong kho, ≥ 0. Thêm mới bằng 0; cộng khi nhập, trừ khi dùng dịch vụ |

#### `PhieuNhap`

Một lần nhập hàng từ nhà cung cấp.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| `maPN` | `String` | Mã phiếu nhập |
| `maNCC` | `String` | → `NhaCC` |
| `maNV` | `String` | Nhân viên lập → `NhanVien` |
| `ngayNhap` | `LocalDate` | Ngày nhập |
| `tongTien` | `double` | Tổng `thanhTien` các dòng `CTPhieuNhap` |

#### `CTPhieuNhap`

Một mặt hàng trong phiếu nhập. Cặp (`maPN`, `maH`) không trùng.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| `maPN` | `String` | → `PhieuNhap` |
| `maH` | `String` | → `HangHoa` |
| `sl` | `int` | Số lượng nhập, ≥ 1 |
| `donGiaNhap` | `double` | Giá nhập lần này, > 0 |
| `thanhTien` | `double` | `sl` × `donGiaNhap` |

## 5. Lưu trữ

Mỗi danh sách một file trong `data/`, mỗi dòng một đối tượng, các trường nối bằng `|` theo đúng thứ tự dưới đây. Lớp con ghi trường của lớp cha trước. Ngày ghi dạng `dd/MM/yyyy`, `boolean` ghi `true`/`false`.

| File | Thứ tự trường |
|---|---|
| `phong.txt` | `loai\|maP\|tenP\|donGia\|soNguoiToiDa\|slGiuong\|tinhTrang` + phần riêng: Deluxe `\|view\|banCong\|loaiNoiThat`, Suite `\|phongKhachRieng\|slPhong` |
| `khachhang.txt` | `maKH\|hoTen\|sdt\|diaChi` |
| `nhanvien.txt` | `maNV\|hoTen\|sdt\|chucVu\|luongCB` |
| `hdthuephong.txt` | `maHDong\|maKH\|maNV\|ngayLapHD\|tienCoc\|ngayNhanPhong\|ngayTraPhong` |
| `cthopdong.txt` | `maHDong\|maP\|donGia\|slNguoi\|thanhTien` |
| `ctkhacho.txt` | `maHDong\|maP\|maKH` |
| `dichvu.txt` | `maDV\|tenDV\|donGia\|maH` |
| `ctsudungdv.txt` | `maHDong\|maDV\|ngaySD\|sl\|donGia\|thanhTien` |
| `hoadon.txt` | `maHDon\|maHDong\|maNV\|ngayLapHD\|tongTienThue\|tongTienDV\|tongCong\|trangThai` |
| `nhacc.txt` | `maNCC\|tenNCC\|diaChi\|sdt` |
| `hanghoa.txt` | `maH\|tenH\|donGia\|slTon` |
| `phieunhap.txt` | `maPN\|maNCC\|maNV\|ngayNhap\|tongTien` |
| `ctphieunhap.txt` | `maPN\|maH\|sl\|donGiaNhap\|thanhTien` |

Ví dụ:

```text
phong.txt
THUONG|P001|101|300000.0|2|1|TRONG
DELUXE|P002|201|600000.0|2|1|DA_THUE|Biển|true|Gỗ sồi
SUITE|P003|301|1200000.0|4|2|TRONG|true|2

dichvu.txt
DV001|Nước suối|15000.0|HH001
DV002|Giặt ủi|50000.0|

hdthuephong.txt
HDTP001|KH001|NV001|16/11/2026|500000.0|16/11/2026|18/11/2026
```

## 6. Nghiệp vụ và ràng buộc

### 6.1 Phòng & Khách hàng

- Phòng mới có `tinhTrang = TRONG`. Menu phòng **không cho sửa tay** `tinhTrang`; chỉ module Thuê phòng và Hóa đơn đổi qua `capNhatTinhTrang`.
- Khi thêm phòng, người dùng chọn loại phòng trước, sau đó chương trình tạo đúng lớp con rồi gọi `nhap()`.
- `tenP` của phòng và `sdt` của khách hàng không trùng.

### 6.2 Thuê phòng

Lập hợp đồng:

1. Tìm khách đứng tên theo mã hoặc SĐT; chưa có thì thêm mới.
2. Chọn nhân viên lập, nhập `ngayNhanPhong`, `ngayTraPhong`, `tienCoc`. `ngayLapHD` là ngày hiện tại, `ngayNhanPhong` không trước `ngayLapHD`.
3. Chọn từ 1 phòng trở lên trong danh sách phòng `TRONG`. Với mỗi phòng: nhập `slNguoi`, chép `donGia` từ phòng, tính `thanhTien`.
4. Với mỗi phòng, ghi khách ở (`CTKhachO`): từ 1 tới `slNguoi` khách, chưa có thì thêm khách mới.
5. Mọi phòng đã chọn chuyển sang `DA_THUE` ngay khi lập hợp đồng. Chương trình không xử lý đặt phòng trước theo lịch.

Khi hợp đồng **chưa có hóa đơn**, được sửa `ngayTraPhong` (khách trả sớm hoặc ở thêm); sửa xong thì tính lại `thanhTien` của các `CTHopDong` thuộc hợp đồng. Khi đã có hóa đơn thì hợp đồng không sửa được nữa.

### 6.3 Dịch vụ & Thanh toán

Ghi dịch vụ:

- Chỉ ghi cho hợp đồng chưa có hóa đơn; `ngaySD` nằm trong khoảng nhận–trả phòng.
- Nếu dịch vụ có `maH`, gọi `DSHangHoa.truKho(maH, sl)`. Trả về `false` (không đủ hàng) thì báo lỗi và không ghi dịch vụ.

Trả phòng (lập hóa đơn):

1. Chọn hợp đồng chưa có hóa đơn và nhân viên lập.
2. Tính `tongTienThue`, `tongTienDV`, `tongCong`; `trangThai = CHUA_TT`.
3. Mọi phòng của hợp đồng chuyển về `TRONG`.
4. Khi in hóa đơn, hiển thị thêm **số còn phải trả** = `tongCong` − `tienCoc` (không lưu).
5. Thanh toán: đổi `trangThai` sang `DA_TT`.

### 6.4 Nhân sự, Kho & Thống kê

Nhập hàng:

1. Chọn nhà cung cấp và nhân viên lập; `ngayNhap` là ngày hiện tại.
2. Thêm từ 1 mặt hàng trở lên, mỗi hàng nhập `sl`, `donGiaNhap`; tính `thanhTien`.
3. Với mỗi hàng: gọi `DSHangHoa.congKho(maH, sl)` và cập nhật `HangHoa.donGia = donGiaNhap`.
4. Tính `tongTien` của phiếu.

Thống kê (tối thiểu):

- Doanh thu theo tháng/năm: tổng `tongCong` của các hóa đơn `DA_TT` có `ngayLapHD` trong tháng/năm đó.
- Doanh thu theo phòng (mục 7).
- Số phòng `TRONG` / `DA_THUE` theo từng loại phòng.
- Tồn kho: danh sách hàng hóa và `slTon`.

### 6.5 Xóa

- Không xóa chứng từ và chi tiết: `HDThuePhong`, `CTHopDong`, `CTKhachO`, `CTSuDungDV`, `HoaDon`, `PhieuNhap`, `CTPhieuNhap`.
- Danh mục (phòng, khách hàng, nhân viên, dịch vụ, hàng hóa, nhà cung cấp) chỉ xóa được khi **chưa được tham chiếu** ở đâu. Ví dụ: xóa phòng thì kiểm tra không có `CTHopDong` nào có `maP` đó.

### 6.6 Hàm giao tiếp giữa các module

Ngoài tìm theo mã, các danh sách cung cấp thêm:

| Hàm | Trả về | Dùng để |
|---|---|---|
| `DSPhong.dsPhongTrong()` | `ArrayList<Phong>` | Liệt kê phòng `TRONG` khi lập hợp đồng |
| `DSPhong.capNhatTinhTrang(String maP, String tinhTrang)` | `void` | Đổi tình trạng phòng và ghi file |
| `DSKhachHang.timTheoSdt(String sdt)` | `KhachHang` | Tìm khách khi lập hợp đồng; không thấy thì `null` |
| `DSCTHopDong.layTheoHopDong(String maHDong)` | `ArrayList<CTHopDong>` | Lấy các phòng của hợp đồng (tính hóa đơn, trả phòng) |
| `DSCTKhachO.layTheoPhong(String maHDong, String maP)` | `ArrayList<CTKhachO>` | Lấy khách ở một phòng trong hợp đồng |
| `DSCTSuDungDV.layTheoHopDong(String maHDong)` | `ArrayList<CTSuDungDV>` | Lấy dịch vụ đã dùng của hợp đồng |
| `DSHoaDon.timTheoHopDong(String maHDong)` | `HoaDon` | Kiểm tra hợp đồng đã có hóa đơn chưa; chưa thì `null` |
| `DSCTPhieuNhap.layTheoPhieu(String maPN)` | `ArrayList<CTPhieuNhap>` | Lấy các mặt hàng của phiếu nhập |
| `DSHangHoa.truKho(String maH, int sl)` | `boolean` | Trừ kho khi dùng dịch vụ; `false` nếu không đủ hàng (không trừ) |
| `DSHangHoa.congKho(String maH, int sl)` | `void` | Cộng kho khi nhập hàng |

## 7. Giá trị tính khi cần (không lưu)

Các giá trị dưới đây không lưu thành thuộc tính mà được tính từ dữ liệu đã có mỗi khi cần, để không có hai chỗ cùng giữ một con số rồi lệch nhau.

| Giá trị | Cách tính |
|---|---|
| Số ngày thuê của hợp đồng | `HDThuePhong.soNgayThue()` |
| Số phòng của hợp đồng | Số phần tử của `DSCTHopDong.layTheoHopDong(maHDong)` |
| Khách đứng tên hóa đơn | `HoaDon.maHDong` → `HDThuePhong.maKH` |
| Số còn phải trả | `HoaDon.tongCong` − `HDThuePhong.tienCoc` |
| Doanh thu theo phòng | Tổng `CTHopDong.thanhTien` của phòng đó, chỉ tính hợp đồng đã có hóa đơn `DA_TT` |

Ngược lại, các khoản tiền trên chứng từ (`thanhTien`, `tongTienThue`, `tongTienDV`, `tongCong`, `tongTien`) và đơn giá chép vào chi tiết (`CTHopDong.donGia`, `CTSuDungDV.donGia`) **được lưu**. Chúng được chốt lúc lập chứng từ và không đổi về sau, kể cả khi giá phòng hay giá dịch vụ thay đổi.
