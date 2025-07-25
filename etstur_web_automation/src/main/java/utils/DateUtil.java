package utils;

import java.time.LocalDate;

public class DateUtil {
    public static class DateInfo {
        public String day;
        public String month;
        public String year;

        public DateInfo(int day, int month, int year) {
            this.day = String.valueOf(day);
            this.month = String.valueOf(month);
            this.year = String.valueOf(year);
        }
    }

    public static DateInfo getPlusDay(int days) {
        LocalDate targetDate = LocalDate.now().plusDays(days);

        int day = targetDate.getDayOfMonth();
        int month = targetDate.getMonthValue();
        int year = targetDate.getYear();

        return new DateInfo(day, month, year);
    }

}
