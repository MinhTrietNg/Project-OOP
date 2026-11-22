package core;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

// các hàm kiểm tra dữ liệu nhập, nhập sai thì hỏi lại chứ không văng lỗi
// lớp này KHÔNG giữ Scanner: lớp nào cần nhập thì tự khởi tạo Scanner riêng rồi truyền vào, vd
//     private static final Scanner sc = new Scanner(System.in, "UTF-8");
//     String ten = NhapLieu.nhapChuoi(sc, "Họ tên");
public final class NhapLieu {

    private NhapLieu() {
    }

    public static String nhapChuoi(Scanner sc, String nhac) {
        while (true) {
            String s = docDong(sc, nhac);
            if (s.isEmpty()) {
                System.out.println("  Không được để trống.");
            } else if (s.contains(DinhDang.PHAN_CACH)) {
                System.out.println("  Không được chứa ký tự " + DinhDang.PHAN_CACH);
            } else {
                return s;
            }
        }
    }

    public static int nhapSoNguyen(Scanner sc, String nhac, int min, int max) {
        while (true) {
            String s = docDong(sc, nhac);
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

    public static double nhapSoThuc(Scanner sc, String nhac, double min) {
        while (true) {
            String s = docDong(sc, nhac);
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

    public static LocalDate nhapNgay(Scanner sc, String nhac) {
        while (true) {
            String s = docDong(sc, nhac + " (dd/MM/yyyy)");
            try {
                return DinhDang.docNgay(s);
            } catch (DateTimeParseException e) {
                System.out.println("  Ngày không hợp lệ.");
            }
        }
    }

    public static boolean nhapCoKhong(Scanner sc, String nhac) {
        while (true) {
            String s = docDong(sc, nhac + " (c/k)").toLowerCase();
            if (s.equals("c")) {
                return true;
            }
            if (s.equals("k")) {
                return false;
            }
            System.out.println("  Nhập c hoặc k.");
        }
    }

    public static String nhapSdt(Scanner sc, String nhac) {
        while (true) {
            String s = docDong(sc, nhac);
            if (s.matches("0\\d{9}")) {
                return s;
            }
            System.out.println("  Số điện thoại gồm 10 chữ số, bắt đầu bằng 0.");
        }
    }

    // vd chonMenu(sc, "QUẢN LÝ PHÒNG", "Quay lại", "Thêm phòng", "Xem tất cả")
    public static int chonMenu(Scanner sc, String tieuDe, String mucKhong, String... cacMuc) {
        System.out.println();
        System.out.println("===== " + tieuDe + " =====");
        for (int i = 0; i < cacMuc.length; i++) {
            System.out.println((i + 1) + ". " + cacMuc[i]);
        }
        System.out.println("0. " + mucKhong);
        return nhapSoNguyen(sc, "Chọn", 0, cacMuc.length);
    }

    // đọc cả dòng rồi tự parse, dùng nextInt() sẽ bị trôi lệnh nextLine() phía sau
    private static String docDong(Scanner sc, String nhac) {
        System.out.print(nhac + ": ");
        if (!sc.hasNextLine()) {
            System.out.println();
            System.out.println("Hết dữ liệu vào, thoát chương trình.");
            System.exit(0);
        }
        return sc.nextLine().trim();
    }
}
