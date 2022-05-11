package scheduler.util.time;

import java.time.*;

/**
 * Provides methods for working with dates, times, timezones, and business hours.
 * */
public class TimeUtils {
    private static final ZoneId hqZone = ZoneId.of("America/New_York");
    private static LocalTime open = LocalTime.of(8,0); // 8 AM
    private static LocalTime close = LocalTime.of(22, 0); // 10 PM

    /**
     * Convert a LocalDateTime from one Timezone to another Timezone.
     * @param dateTime the datetime
     * @param oldZone the old timezone
     * @param newZone the new timezone
     * @return the LocalDateTime at the new timezone
     * */
    public static LocalDateTime convert(LocalDateTime dateTime, ZoneId oldZone, ZoneId newZone) {
        ZonedDateTime zoned = dateTime.atZone(oldZone);

        return zoned.withZoneSameInstant(newZone).toLocalDateTime();
    }


    /**
     * Convert a LocalTime from one Timezone to another Timezone.
     * @param time the time
     * @param oldZone the old timezone
     * @param newZone the new timezone
     * @return the LocalTime at the new timezone
     * */
    public static LocalTime convert(LocalTime time, ZoneId oldZone, ZoneId newZone) {
        var today = LocalDate.from(LocalDateTime.now());

        return convert(LocalDateTime.of(today, time), oldZone, newZone).toLocalTime();
    }

    /**
     * Checks if the specified start and end date times are within open business hours.
     * @param start the start datetime
     * @param end the end datetime
     * @param localZone the timezone of the start and end params
     * @return true if the time interval falls within open business hours
     * */
    public static boolean isInOpenHours(LocalDateTime start, LocalDateTime end, ZoneId localZone) {
        ZonedDateTime zonedStart = start.atZone(localZone);
        ZonedDateTime zonedEnd = end.atZone(localZone);

        ZonedDateTime hqStart = zonedStart.withZoneSameInstant(hqZone);
        ZonedDateTime hqEnd = zonedEnd.withZoneSameInstant(hqZone);

        if (hqStart.getDayOfMonth() != hqEnd.getDayOfMonth())
            return false;

        if (hqStart.toLocalTime().equals(open) || hqStart.toLocalTime().isAfter(open))
            return hqEnd.toLocalTime().equals(close) || hqEnd.toLocalTime().isBefore(close);

        return false;
    }

    /**
     * Get the open time in the system default timezone.
     * @return the open time in user's default timezone
     * */
    public static LocalTime getLocalOpen() {
        return convert(open, hqZone, ZoneId.systemDefault());
    }

    /**
     * Get the open time in the system default timezone.
     * @return the open time in user's default timezone
     * */
    public static LocalTime getLocalClose() {
        return convert(close, hqZone, ZoneId.systemDefault());
    }
}
