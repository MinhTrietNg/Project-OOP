# Module 3 — Dịch vụ & Thanh toán

Tài liệu đọc kèm code của module 3: có gì, nghiệp vụ chạy ra sao, cách kiểm thử và cần làm gì khi ghép với module 1, 2.

Module 3 viết theo đúng 3 khuôn của module 4 (xem `HUONG_DAN_MUC4.md` mục 2): khuôn A cho model, khuôn B cho danh sách, khuôn C cho lọc / thống kê.

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
| `test/` | `KiemThuModule3` | 46 ca kiểm thử tự động |

## 2. Nghiệp vụ

### 2.1 Ghi dịch vụ — `DSCTSuDungDV.ghiDichVu()`

```text
B1  nhập mã hợp đồng -> phải tồn tại và CHƯA có hóa đơn -> chọn dịch vụ (0 = hủy)
B2  nhập ngày dùng (bắt nằm trong ngày nhận -> ngày trả, tính cả 2 biên) và số lượng
B3  ghiDichVu(hd, dv, ngaySD, sl):
      kiểm tra lại hóa đơn, ngày
      dịch vụ có maH -> DuLieu.dsHangHoa.truKho(maH, sl); false = không đủ hàng -> DỪNG, không ghi
      chép donGia từ DichVu, tính thanhTien = sl x donGia, thêm chi tiết
```

Trừ kho **trước** rồi mới lưu chi tiết. `truKho` không trừ gì khi trả `false`, nên thiếu hàng thì kho và danh sách đều giữ nguyên. Menu ghi 2 file: `ctsudungdv.txt` và `hanghoa.txt`.

### 2.2 Trả phòng & lập hóa đơn — `DSHoaDon.them()`

```text
B1  nhập mã hợp đồng -> phải tồn tại và CHƯA có hóa đơn
    hôm nay còn trước ngày trả trong hợp đồng -> nhắc sửa ngày trả bên Thuê phòng trước (có thể bỏ qua)
    chọn nhân viên lập (0 = hủy)
B2  lapHoaDon(hd, maNV, cacPhong):
      tongTienThue = cộng thanhTien các CTHopDong
      tongTienDV   = cộng thanhTien các CTSuDungDV của hợp đồng
      tongCong     = tongTienThue + tongTienDV, trạng thái CHUA_TT, ngày lập = hôm nay
B3  (trong lapHoaDon) trả mọi phòng của hợp đồng về TRONG: DuLieu.dsPhong.capNhatTinhTrang(maP, Phong.TRONG)
```

Lưu ý tên hàm: `them()` (không tham số) là **cả quy trình trả phòng** ở trên; `them(HoaDon x)` (có tham số) chỉ **nới mảng ra 1 rồi đặt bản sao vào cuối**, dùng trong `lapHoaDon` và `docFile`. Menu ghi 2 file: `hoadon.txt` và `phong.txt`.

In hóa đơn (`xuatChiTiet`) hiện từng phòng, từng dịch vụ, tổng cộng, tiền cọc và **còn phải trả** = `tongCong − tienCoc`. Số này không lưu vào file (mục 7 của mô tả đồ án).

### 2.3 Thanh toán — `DSHoaDon.thanhToan()`

In hóa đơn, hỏi xác nhận, đổi `CHUA_TT` → `DA_TT` (trả lời `k` thì báo "Đã hủy thanh toán."). Hóa đơn đã thanh toán thì báo và không đổi gì. Đây là thao tác "sửa" duy nhất của hóa đơn; tiền trên hóa đơn không sửa tay được.

### 2.4 Ràng buộc đã cài

| Ràng buộc | Ở đâu |
|---|---|
| Hợp đồng đã có hóa đơn thì không ghi dịch vụ, không lập hóa đơn lần 2 | `ghiDichVu`, `lapHoaDon` (dùng `timTheoHopDong`) |
| Ngày dùng dịch vụ trong khoảng nhận – trả | `CTSuDungDV.nhap(tu, den)` + kiểm tra lại trong `ghiDichVu` |
| Không đủ hàng thì không ghi dịch vụ | `ghiDichVu` + `DSHangHoa.truKho` |
| Đổi giá dịch vụ không làm đổi tiền đã ghi | `CTSuDungDV.donGia` chép lúc dùng |
| Dịch vụ đã có khách dùng thì không xóa | `DSDichVu.xoa(ma)` |
| Không xóa chi tiết sử dụng dịch vụ, không xóa hóa đơn | Không có hàm `xoa` |

## 3. Phần nào thuộc gói nào

**Gói 50%**: 3 lớp model; `DSDichVu` đủ `them`, `sua`, `xoa`, `timTheoMa`, `xuat`, `sinhMa`, `docFile`, `ghiFile`; ghi dịch vụ có trừ kho; lập hóa đơn, thanh toán; `layTheoHopDong`, `timTheoHopDong` (mục 6.6).

**Gói 70%**:

| Hàm | Ở đâu | Ý chính |
|---|---|---|
| Tìm dịch vụ không dấu | `DSDichVu.timTheoTen` | Dùng lại `core.TimKiem` |
| Xem dịch vụ đã dùng của 1 hợp đồng | `DSCTSuDungDV.xuatTheoHopDong` | Kèm tên dịch vụ và tổng tiền |
| Thống kê theo dịch vụ (số lượt, số lượng, tiền) | `DSCTSuDungDV.thongKeTheoDichVu` | Khuôn C |
| Hóa đơn chưa thanh toán + tổng nợ | `DSHoaDon.dsChuaThanhToan` | Khuôn C |
| Lọc hóa đơn theo khoảng ngày | `DSHoaDon.locTheoKhoangNgay` | `isBefore` / `isAfter` |
| In hóa đơn đầy đủ (phòng, dịch vụ, cọc, còn phải trả) | `DSHoaDon.xuatChiTiet` | |
| Nhắc sửa ngày trả khi khách trả sớm | `DSHoaDon.them` | |
| Bỏ qua dòng file lỗi | `docFile()` | `try { them(...); } catch (Exception e) { ... }` |

## 4. Kiểm thử

Từ thư mục gốc project:

```powershell
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src, test -Filter *.java).FullName
java -cp out KiemThuModule3
```

Kết quả mong đợi: `Kết quả: 46 đạt, 0 trượt`. Lớp test chỉ dùng dữ liệu trong bộ nhớ (không đọc/ghi file), tự tạo `HDThuePhong`, `CTHopDong` bằng constructor rồi truyền vào hàm có tham số.

Các ca được kiểm: sinh mã (kể cả sau khi xóa), dòng file có `maH` rỗng, ghi dịch vụ (đủ hàng, thiếu hàng, **đúng bằng tồn**, không dùng hàng, ngày ngoài khoảng, hợp đồng đã có hóa đơn), giá chốt lúc dùng, tính tiền hóa đơn, chặn lập hóa đơn 2 lần, còn phải trả, thanh toán (lần 1, lần 2, mã sai), lọc ngày, xóa có kiểm tra tham chiếu (dịch vụ, hàng hóa, nhân viên).

## 5. Liên hệ với module khác

Module 3 gọi thẳng danh sách của module khác qua `DuLieu`, không có hàm bọc trung gian:

| Module 3 gọi | Ở đâu |
|---|---|
| `DuLieu.dsHDThuePhong.timTheoMa(maHDong)` | `DSCTSuDungDV.ghiDichVu()`, `DSHoaDon.them()`, `DSHoaDon.xuatChiTiet` (lấy tiền cọc) |
| `DuLieu.dsCTHopDong.layTheoHopDong(maHDong)` | `DSHoaDon.them()`, `DSHoaDon.xuatChiTiet` |
| `DuLieu.dsPhong.capNhatTinhTrang(maP, Phong.TRONG)` | `DSHoaDon.lapHoaDon` (trả phòng) |
| `DuLieu.dsHangHoa.truKho(maH, sl)` | `DSCTSuDungDV.ghiDichVu(hd, dv, ngaySD, sl)` |
| `DuLieu.dsNhanVien.chon()`, `timTheoMa(maNV)` | `DSHoaDon.them()`, `DSHoaDon.xuatChiTiet` |

Module khác dùng từ module 3:

- `DuLieu.dsHoaDon.timTheoHopDong(maHDong)`: khác `null` thì hợp đồng đã có hóa đơn, module 2 không cho sửa ngày trả.
- `DSNhanVien.xoa` duyệt `DuLieu.dsHoaDon.getDs()`; `DSHangHoa.xoa` duyệt `DuLieu.dsDichVu.getDs()`.
- Khi xóa khách hàng / phòng không cần kiểm tra bên module 3 (hóa đơn chỉ giữ `maHDong`).

## 6. Câu hỏi vấn đáp hay gặp

- **Vì sao `CTSuDungDV` lưu `donGia` mà không lấy `DichVu.donGia`?** Giá dịch vụ có thể đổi; hóa đơn cũ phải giữ đúng giá lúc khách dùng.
- **Vì sao lưu `tongTienThue`, `tongCong` vào file mà không tính lại?** Hóa đơn là chứng từ, chốt số tiền lúc lập (mục 7 mô tả đồ án).
- **Vì sao "còn phải trả" không lưu?** Tính được từ `tongCong` và `tienCoc`; lưu thêm thì có 2 chỗ giữ cùng 1 con số, dễ lệch.
- **Vì sao trừ kho trước rồi mới lưu chi tiết?** `truKho` tự kiểm tra đủ hàng; thiếu thì trả `false` và không đổi gì, nên dừng lại là sạch, không phải hoàn tác.
- **Vì sao `ghiDichVu`, `lapHoaDon` có bản có tham số?** Để kiểm thử tự động và để module khác gọi được mà không phải gõ phím.
- **Đa hình ở đâu?** 3 lớp model cài interface `DoiTuong` và `@Override` `nhap`, `xuat`, `toLine`. `CTSuDungDV` có 2 hàm `nhap` (nạp chồng): `nhap()` và `nhap(tu, den)`.
