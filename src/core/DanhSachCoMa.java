package core;

// danh sách cho các lớp có mã riêng: tìm, xóa theo mã và tự sinh mã
public abstract class DanhSachCoMa<T extends DoiTuong & CoMa> extends DanhSach<T> {

    protected abstract String tienTo();

    // thêm mới thì tự gán mã luôn, khỏi ai quên
    @Override
    public void them(T x) {
        if (x.getMa() == null || x.getMa().isEmpty()) {
            x.setMa(sinhMa());
        }
        super.them(x);
    }

    public T timTheoMa(String ma) {
        if (ma == null) {
            return null;
        }
        for (T x : ds) {
            if (x.getMa().equalsIgnoreCase(ma.trim())) {
                return x;
            }
        }
        return null;
    }

    public boolean xoaTheoMa(String ma) {
        T x = timTheoMa(ma);
        if (x == null) {
            return false;
        }
        ds.remove(x);
        return true;
    }

    // lấy số lớn nhất + 1 chứ không dùng size() + 1, vì xóa giữa chừng sẽ bị trùng mã
    public String sinhMa() {
        int max = 0;
        for (T x : ds) {
            try {
                int so = Integer.parseInt(x.getMa().substring(tienTo().length()));
                if (so > max) {
                    max = so;
                }
            } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                // mã bị sửa tay sai định dạng thì bỏ qua
            }
        }
        return tienTo() + String.format("%03d", max + 1);
    }
}
