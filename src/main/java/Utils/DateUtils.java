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
    
    public static int compareDates(Date date1, Date date2) {
        return date1.compareTo(date2);
    }

    public static int checkDateDifference(Date date1, Date date2) {
        long diffInMillis = Math.abs(date1.getTime() - date2.getTime());
        long diffInDays = diffInMillis / (24 * 60 * 60 * 1000);
        return diffInDays < 15 ? 1 : 0;
    }
}
