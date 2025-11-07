package com.gym.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {

    // 🗓️ Parse String -> java.util.Date
    public static Date parseToDate(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    // 🌿 Parse String -> java.time.LocalDate
    public static LocalDate parseToLocalDate(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(dateString);

            return date.toInstant()
                       .atZone(ZoneId.systemDefault())
                       .toLocalDate();

        } catch (ParseException e) {
            return null;
        }
    }

    // 🕒 HTML <input type="date"> → LocalDateTime (00:00:00)
    public static LocalDateTime parseDateInputToLocalDateTime(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateString).atStartOfDay();
    }

    // 🔁 LocalDateTime → String cho <input type="date">
    public static String formatLocalDateTimeForDateInput(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dateTime.format(formatter);
    }
}
