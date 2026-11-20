package core;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

// mọi chỗ nhập từ bàn phím đều qua đây, nhập sai thì hỏi lại chứ không văng lỗi
public final class NhapLieu {

    // dùng chung 1 Scanner, nhiều Scanner cùng đọc System.in sẽ bị mất dữ liệu
    private static final Scanner SC = new Scanner(System.in, StandardCharsets.UTF_8.name());

    private NhapLieu() {
    }

    public static String nhapChuoi(String nhac) {
        while (true) {
            String s = docDong(nhac);
            if (s.isEmpty()) {
                System.out.println("  Không được để trống.");
            } else if (s.contains(DinhDang.PHAN_CACH)) {
                System.out.println("  Không được chứa ký tự " + DinhDang.PHAN_CACH);
            } else {
                return s;
            }
        }
    }

    public static int nhapSoNguyen(String nhac, int min, int max) {
        while (true) {
            String s = docDong(nhac);
            try {
                int x = Integer.parseInt(s);
                if (x >= min && x <= max) {
                    return x;
                }
                System.out.println("  Phải từ " + min + " đến " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("  Phải là số nguyên.");
            }
        }
    }

    public static double nhapSoThuc(String nhac, double min) {
        while (true) {
            String s = docDong(nhac);
            try {
                double x = Double.parseDouble(s);
                // chặn "Infinity" / "1e400", không thì lúc ghi file sẽ văng lỗi
                if (x >= min && !Double.isInfinite(x)) {
                    return x;
                }
                System.out.println("  Phải lớn hơn hoặc bằng " + DinhDang.tien(min) + ".");
            } catch (NumberFormatException e) {
                System.out.println("  Phải là số.");
            }
        }
    }

    public static LocalDate nhapNgay(String nhac) {
        while (true) {
            String s = docDong(nhac + " (dd/MM/yyyy)");
            try {
                return DinhDang.docNgay(s);
            } catch (DateTimeParseException e) {
                System.out.println("  Ngày không hợp lệ.");
            }
        }
    }

    public static boolean nhapCoKhong(String nhac) {
        while (true) {
            String s = docDong(nhac + " (c/k)").toLowerCase();
            if (s.equals("c")) {
                return true;
            }
            if (s.equals("k")) {
                return false;
            }
            System.out.println("  Nhập c hoặc k.");
        }
    }

    public static String nhapSdt(String nhac) {
        while (true) {
            String s = docDong(nhac);
            if (s.matches("0\\d{9}")) {
                return s;
            }
            System.out.println("  Số điện thoại gồm 10 chữ số, bắt đầu bằng 0.");
        }
    }

    // vd chonMenu("QUẢN LÝ PHÒNG", "Quay lại", "Thêm phòng", "Xem tất cả")
    public static int chonMenu(String tieuDe, String mucKhong, String... cacMuc) {
        System.out.println();
        System.out.println("===== " + tieuDe + " =====");
        for (int i = 0; i < cacMuc.length; i++) {
            System.out.println((i + 1) + ". " + cacMuc[i]);
        }
        System.out.println("0. " + mucKhong);
        return nhapSoNguyen("Chọn", 0, cacMuc.length);
    }

    // đọc cả dòng rồi tự parse, dùng nextInt() sẽ bị trôi lệnh nextLine() phía sau
    private static String docDong(String nhac) {
        System.out.print(nhac + ": ");
        if (!SC.hasNextLine()) {
            System.out.println();
            System.out.println("Hết dữ liệu vào, thoát chương trình.");
            System.exit(0);
        }
        return SC.nextLine().trim();
    }
}
