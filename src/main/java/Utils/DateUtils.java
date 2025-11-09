package Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static Date parseToDate(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // hoặc throw RuntimeException nếu muốn
        }
    }

    public static Date addMonths(Date time, int months) {
        return new Date(time.getTime() + months * 30L * 24 * 60 * 60 * 1000); // Giả sử mỗi tháng có 30 ngày
    }
}
