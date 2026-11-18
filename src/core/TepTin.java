package core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

// đọc / ghi file dữ liệu (UTF-8, mỗi dòng 1 đối tượng, các trường nối bằng |)
// gom về 1 chỗ để các lớp DS... chỉ còn vài dòng docFile() / ghiFile()
public final class TepTin {

    private TepTin() {
    }

    // trả về mảng các dòng, mỗi dòng đã tách thành mảng trường
    // vd dòng "NV001|An|0901234567" -> {"NV001", "An", "0901234567"}
    // file chưa có thì trả mảng rỗng
    public static String[][] doc(String duongDan) {
        String[][] kq = new String[0][];
        File f = new File(duongDan);
        if (!f.exists()) {
            return kq;
        }
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    kq = Arrays.copyOf(kq, kq.length + 1);
                    kq[kq.length - 1] = DinhDang.tach(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Lỗi đọc file " + duongDan);
        }
        return kq;
    }

    // ghi đè cả file, mỗi phần tử của cacDong là 1 dòng
    public static void ghi(String duongDan, String[] cacDong) {
        new File(duongDan).getParentFile().mkdirs();
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(duongDan), StandardCharsets.UTF_8))) {
            for (String dong : cacDong) {
                bw.write(dong);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Lỗi ghi file " + duongDan);
        }
    }
}
