package project.borrowhen.common.util;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;

public class TimeAgoUtil {

    public static String toTimeAgo(Timestamp timestamp) {
        if (timestamp == null) return "";

        LocalDateTime dateTime = timestamp.toLocalDateTime();
        LocalDateTime now = LocalDateTime.now();

        Duration duration = Duration.between(dateTime, now);
        long seconds = duration.getSeconds();

        if (seconds < 60) {
            return seconds + " seconds ago";
        }

        long minutes = seconds / 60;
        if (minutes < 60) {
            return minutes + " minutes ago";
        }

        long hours = minutes / 60;
        if (hours < 24) {
            long min = minutes % 60;
            return hours + " hours " + min + " minutes ago";
        }

        long days = hours / 24;
        if (days < 7) {
            long hr = hours % 24;
            return days + " days " + hr + " hours ago";
        }

        long weeks = days / 7;
        if (days < 30) {
            long dayRemainder = days % 7;
            return weeks + " weeks " + dayRemainder + " days ago";
        }

        long months = days / 30;
        if (days < 365) {
            long dayRemainder = days % 30;
            return months + " months " + dayRemainder + " days ago";
        }

        long years = days / 365;
        long monthRemainder = (days % 365) / 30;
        return years + " years " + monthRemainder + " months ago";
    }
}
