package core;

import java.text.Normalizer;

// tìm gần đúng theo tên: không phân biệt hoa thường, gõ không dấu vẫn tìm được
// vd chua("Nguyễn Văn An", "van an") -> true
public final class TimKiem {

    private TimKiem() {
    }

    public static boolean chua(String goc, String tuKhoa) {
        return boDau(goc).contains(boDau(tuKhoa));
    }

    // "Nguyễn Đức" -> "nguyen duc"
    // NFD tách chữ có dấu thành chữ gốc + dấu ("ễ" -> "e" + 2 dấu), rồi xóa hết dấu đi
    public static String boDau(String s) {
        String tach = Normalizer.normalize(s, Normalizer.Form.NFD);
        String khongDau = tach.replaceAll("\\p{M}", "");
        // riêng chữ đ không phải "d + dấu" nên phải đổi tay
        return khongDau.replace('đ', 'd').replace('Đ', 'D').toLowerCase().trim();
    }
}
