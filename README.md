# Quản lý khách sạn — Đồ án Lập trình hướng đối tượng (Java)

Chương trình console quản lý một khách sạn nhỏ, viết bằng Java thuần, lưu dữ liệu bằng file `.txt`.

Các nghiệp vụ chính:

- **Phòng và khách hàng.** Có 3 loại phòng: thường, Deluxe và Suite.
- **Thuê phòng.** Một hợp đồng thuê được nhiều phòng, mỗi phòng có nhiều khách ở.
- **Dịch vụ, trả phòng và hóa đơn.** Dịch vụ có tiêu hao hàng thì tự trừ tồn kho.
- **Nhân viên và kho.** Quản lý nhà cung cấp, hàng hóa và phiếu nhập; lập phiếu nhập thì tự cộng tồn kho.
- **Thống kê.** Có 10 mục: doanh thu theo tháng, theo phòng, theo dịch vụ; tồn kho; chi phí nhập; lương theo chức vụ…

## Yêu cầu

- JDK 8 trở lên. Chỉ dùng thư viện chuẩn, không cần Maven hay Gradle.
- Mở được bằng IntelliJ IDEA, VS Code, Eclipse, hoặc chỉ cần `javac` / `java`.

## Chạy chương trình

Mọi lệnh đều chạy từ **thư mục gốc project**, vì chương trình đọc và ghi file theo đường dẫn tương đối `data/...`.

**1. Nạp dữ liệu mẫu.** Thư mục `data/` không được đưa lên git; chép nó từ `data_mau/`:

```powershell
# PowerShell
New-Item -ItemType Directory -Force data | Out-Null
Copy-Item data_mau\*.txt data\ -Force
```

```bash
# Git Bash / Linux / macOS
mkdir -p data && cp data_mau/*.txt data/
```

**2. Biên dịch và chạy:**

```powershell
# PowerShell
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out app.Main
```

```bat
:: cmd (gõ chcp 65001 trước để hiện tiếng Việt)
javac -encoding UTF-8 -d out src\app\*.java src\core\*.java src\model\*.java src\danhsach\*.java src\menu\*.java
java -cp out app.Main
```

Khi chạy, mỗi lần chỉ gõ **một dòng rồi Enter**. Không dán nhiều dòng cùng lúc và không chuyển hướng input bằng `<`. Lý do: mỗi lớp có `Scanner` riêng (xem [mục Thiết kế](#thiết-kế)).

## Kiểm thử

Thư mục `test/` có 4 lớp kiểm thử tự động, mỗi lớp ứng với một module, tổng cộng 135 ca. Các lớp này chỉ dùng dữ liệu trong bộ nhớ, không đọc hay ghi `data/`.

```powershell
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src, test -Filter *.java).FullName
java -cp out KiemThuModule1   # 30 ca
java -cp out KiemThuModule2   # 28 ca
java -cp out KiemThuModule3   # 50 ca
java -cp out KiemThuModule4   # 34 ca
```

Dòng cuối của mỗi lớp in ra `Kết quả: x đạt, 0 trượt`.

## Cấu trúc thư mục

```text
src/
  app/        Main (menu chính), DuLieu (giữ 13 danh sách dùng chung)
  core/       DoiTuong, DanhSach<T> (interface); NhapLieu, DinhDang, TepTin, TimKiem (tiện ích)
  model/      17 lớp đối tượng: Nguoi, KhachHang, NhanVien, Phong, PhongThuong, PhongDeluxe, ...
  danhsach/   13 lớp danh sách DSxxx, mỗi lớp quản lý 1 mảng và đọc/ghi 1 file
  menu/       8 lớp menu, mỗi lớp ứng với 1 mục của menu chính
test/         KiemThuModule1..4
data_mau/     13 file dữ liệu mẫu (chép sang data/ để chạy)
docs/         hướng dẫn từng module, kịch bản demo, sơ đồ lớp UML
MO_TA_DO_AN.md  đặc tả: quy ước code, sơ đồ lớp, định dạng file, nghiệp vụ
```

## Thiết kế

| Kiến thức OOP | Thể hiện ở đâu |
|---|---|
| Đóng gói | Mọi thuộc tính `private`, truy cập qua getter/setter. Mỗi lớp model có 3 constructor: không tham số, đầy đủ tham số, sao chép |
| Kế thừa, lớp trừu tượng | `Nguoi` (abstract) → `KhachHang`, `NhanVien`. `Phong` (abstract, có `getLoaiPhong()` abstract) → `PhongThuong`, `PhongDeluxe`, `PhongSuite` |
| Đa hình | `DSPhong` giữ mảng `Phong[]` chứa lẫn 3 loại phòng; `p.xuat()` và `p.toLine()` tự gọi đúng hàm của lớp con |
| Interface | `DoiTuong` (`nhap`, `xuat`, `toLine`) cho 12 lớp; `DanhSach<T>` (generic) cho 13 lớp danh sách. `DuLieu.docTatCa()` duyệt mảng `DanhSach<?>[]` |
| static | 13 danh sách `static final` trong `DuLieu`; hằng trạng thái (`Phong.TRONG`, `HoaDon.DA_TT`…); lớp tiện ích toàn hàm static |
| Nhập liệu | Lớp nào cần đọc bàn phím thì **tự khởi tạo** `private static final Scanner sc = new Scanner(System.in, "UTF-8")`. `NhapLieu` không giữ Scanner, chỉ kiểm tra dữ liệu (nhập sai thì báo và hỏi lại) và nhận `sc` qua tham số |
| Lưu trữ | Mỗi danh sách ứng với 1 file `.txt` UTF-8, mỗi dòng là 1 đối tượng, các trường ngăn bởi `\|`. Thêm, sửa hay xóa xong là ghi lại ngay |

Mã đối tượng (`P001`, `KH001`, `HDTP001`…) do chương trình tự sinh. Ngoài ra chương trình chặn trùng số điện thoại, số phòng, tên hàng / dịch vụ / nhà cung cấp, và không cho xóa đối tượng đang được tham chiếu.

## Các module

| Module | Nội dung | Lớp chính |
|---|---|---|
| 1 | Phòng & Khách hàng, khung chung (`core`, `app`) | `Phong` + 3 lớp con, `KhachHang`, `DSPhong`, `DSKhachHang`, `MenuPhong`, `MenuKhachHang` |
| 2 | Thuê phòng | `HDThuePhong`, `CTHopDong`, `CTKhachO`, `MenuThuePhong` |
| 3 | Dịch vụ & Thanh toán | `DichVu`, `CTSuDungDV`, `HoaDon`, `MenuDichVu`, `MenuHoaDon` |
| 4 | Nhân sự, Kho & Thống kê | `NhanVien`, `NhaCC`, `HangHoa`, `PhieuNhap`, `CTPhieuNhap`, `MenuNhanVien`, `MenuKho`, `MenuThongKe` |

## Tài liệu

- [MO_TA_DO_AN.md](MO_TA_DO_AN.md) — đặc tả đầy đủ: quy ước, sơ đồ lớp, định dạng 13 file dữ liệu, ràng buộc nghiệp vụ.
- [docs/SO_DO_LOP.puml](docs/SO_DO_LOP.puml) — sơ đồ lớp UML (PlantUML): lớp đối tượng và kiến trúc các tầng.
- [docs/HUONG_DAN_MUC1.md](docs/HUONG_DAN_MUC1.md) … [docs/HUONG_DAN_MUC4.md](docs/HUONG_DAN_MUC4.md) — hướng dẫn đọc code từng module.
- [docs/KICH_BAN_DEMO.md](docs/KICH_BAN_DEMO.md) — kịch bản demo và đối chiếu tiêu chí chấm với code.
