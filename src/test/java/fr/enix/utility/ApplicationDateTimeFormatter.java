package fr.enix.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationDateTimeFormatter {

    private static final DateTimeFormatter DATE_TIME_FORMATTER  = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER       = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * @param datetime accepted format yyyy-MM-dd HH:mm:ss
     */
    public static LocalDateTime parseDatetimeFromString(final String datetime) {
        return LocalDateTime.parse(datetime, DATE_TIME_FORMATTER);
    }

    /**
     * @param datetime accepted format yyyy-MM-dd
     */
    public static LocalDate parseDateFromString(final String datetime) {
        return LocalDate.parse(datetime, DATE_FORMATTER);
    }
}
