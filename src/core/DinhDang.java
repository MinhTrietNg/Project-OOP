package core;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Locale;

// gom hết quy tắc định dạng ngày, tiền, dòng file vào 1 chỗ
public final class DinhDang {

    public static final String PHAN_CACH = "|";

    // STRICT để không nhận ngày sai như 31/02, phải dùng uuuu chứ yyyy sẽ lỗi
    private static final DateTimeFormatter NGAY =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private DinhDang() {
    }

    public static String ngay(LocalDate d) {
        return d == null ? "" : d.format(NGAY);
    }

    public static LocalDate docNgay(String s) {
        return LocalDate.parse(s.trim(), NGAY);
    }

    public static String tien(double x) {
        return String.format(Locale.US, "%,.0f", x).replace(',', '.') + " đ";
    }

    // nối các trường thành 1 dòng file, vd dong(maP, tenP, donGia) -> "P001|101|300000"
    public static String dong(Object... truong) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < truong.length; i++) {
            if (i > 0) {
                sb.append(PHAN_CACH);
            }
            sb.append(chuoi(truong[i]));
        }
        return sb.toString();
    }

    // -1 để giữ lại trường rỗng ở cuối dòng
    public static String[] tach(String line) {
        return line.split("\\|", -1);
    }

    private static String chuoi(Object o) {
        if (o == null) {
            return "";
        }
        if (o instanceof LocalDate) {
            return ngay((LocalDate) o);
        }
        if (o instanceof Double) {
            // không thì 12000000.0 sẽ ghi ra thành 1.2E7
            return BigDecimal.valueOf((Double) o).stripTrailingZeros().toPlainString();
        }
        return o.toString();
    }
}
