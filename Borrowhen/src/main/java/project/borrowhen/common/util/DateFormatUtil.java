package project.borrowhen.common.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class DateFormatUtil {

    // Formatter for date-only string
    private static final DateTimeFormatter DATE_ONLY_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    /**
     * Get current timestamp
     * @return java.sql.Timestamp
     */
    public static Timestamp getCurrentTimestamp() {
        return Timestamp.valueOf(LocalDateTime.now());
    }

    /**
     * Format a Timestamp to date-only string: MM/dd/yyyy
     * @param timestamp java.sql.Timestamp
     * @return formatted date string or empty if null
     */
    public static String formatTimestampToString(Timestamp timestamp) {
        if (timestamp == null) return "";
        return timestamp.toLocalDateTime().format(DATE_ONLY_FORMATTER);
    }
}
