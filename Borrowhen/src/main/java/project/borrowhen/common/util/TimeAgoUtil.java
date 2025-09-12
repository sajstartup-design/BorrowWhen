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
            return "Now";
        }

        long minutes = seconds / 60;
        if (minutes < 60) {
            return minutes == 1 ? "A minute ago" : minutes + " minutes ago";
        }

        long hours = minutes / 60;
        if (hours < 24) {
            return hours == 1 ? "An hour ago" : hours + " hours ago";
        }

        long days = hours / 24;
        if (days < 7) {
            return days == 1 ? "A day ago" : days + " days ago";
        }

        long weeks = days / 7;
        if (days < 30) {
            return weeks == 1 ? "A week ago" : weeks + " weeks ago";
        }

        long months = days / 30;
        if (days < 365) {
            return months == 1 ? "A month ago" : months + " months ago";
        }

        long years = days / 365;
        return years == 1 ? "A year ago" : years + " years ago";
    }
}
