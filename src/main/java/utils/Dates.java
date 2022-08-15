package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Date;

public class Dates extends BaseUtils {

    private final String datePattern;

    public Dates() {
        datePattern = "dd/MM/yyyy";
    }

    public String getDate(long delta) {
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(datePattern);
        LocalDateTime date = LocalDateTime.now().plusDays(delta);
        final DayOfWeek dow = DayOfWeek.of(date.get(ChronoField.DAY_OF_WEEK));

        switch (dow) {
            case SATURDAY:
                delta = (delta > 0L) ? 2L : -1L;
                return dtf.format(date.plusDays(delta));

            case SUNDAY:
                delta = (delta > 0L) ? 1L : -2L;
                return dtf.format(date.plusDays(delta));

            default:
                return dtf.format(date);
        }
    }

    public LocalDate dateValueOf(String date) {
        final String[] dateSplit = date.split("/");
        final String dateStr = dateSplit[2] + "-" + dateSplit[1] + "-" + dateSplit[0];

        return LocalDate.parse(dateStr);
    }

    public Date parseDateFrom(String date) {
        try {
            return new SimpleDateFormat(datePattern).parse(date);

        } catch (ParseException pe) {
            errorLog(pe);
        }
        return null;
    }
}
