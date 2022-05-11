package scheduler.util.time;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents the Hour and Minute of a time in 15 minute increments. It is the object used in the time spinners.
 * */
public class Time {
    private final LocalTime time;

    public Time() {
        time = LocalTime.of(0,0);
    }

    private Time(LocalTime time) {
        this.time = time;
    }

    /**
     * Gets the Time object 15 minutes after this.
     * @return the new time
     * */
    public Time next15() {
        return new Time(time.plusMinutes(15));
    }

    /**
     * Gets the Time object 15 minutes before this.
     * @return the new time
     * */
    public Time prev15() {
        return new Time(time.minusMinutes(15));
    }

    /**
     * Converts the object into a string representation.
     * @return the object as a string
     * */
    @Override
    public String toString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);

        return time.format(dateTimeFormatter);
    }

    /**
     * Checks if the object has equal values.
     * @param o the object to check
     * @return true if the objects are equal
     * */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Time time1 = (Time) o;
        return time.getHour() == time1.time.getHour() && time.getMinute() == time1.time.getMinute();
    }

    /**
     * Provides a hash of the object's fields.
     * @return the hash value
     * */
    @Override
    public int hashCode() {
        return Objects.hash(time.getHour(), time.getMinute());
    }

    /**
     * Gets all Time objects for an entire day in 15 minute intervals.
     * @return the list of times
     * */
    public static List<Time> allDayIntervals() {
        return allDayIntervals(new ArrayList<>());
    }

    /**
     * Recursive helper function to get all Time objects for an entire day in 15 minute intervals.
     * @param list the current list of times
     * @return the list of times
     * */
    private static List<Time> allDayIntervals(List<Time> list) {
        Time next;

        if (list.size() > 0) {
            Time last = list.get(list.size() - 1);
            next = last.next15();
        } else {
            next = new Time();
        }

        if (!list.contains(next)) {
            list.add(next);
            return allDayIntervals(list);
        }
        else {
            return list;
        }
    }

    /**
     * Gets the next 15 minute interval Time object after now.
     * @return the next closest time
     * */
    public static Time closestTime() {
        return closestTime(LocalDateTime.now());
    }

    /**
     * Gets the next 15 minute interval Time object after specified date time.
     * @param dateTime the date time
     * @return the next closest Time object
     * */
    public static Time closestTime(LocalDateTime dateTime) {
        return closestTime(LocalTime.from(dateTime));
    }

    /**
     * Gets the next 15 minute interval Time object after specified time.
     * @param time the local time
     * @return the next closest Time object
     * */
    public static Time closestTime(LocalTime time) {
        int min = time.getMinute();
        int diff = min % 15;

        if (diff == 0)
            return new Time(time);
        else
            return new Time(time.plusMinutes(15 - diff));
    }

    /**
     * Get a new Time object a specified number of hours in the future.
     * @param hours the number of hours
     * @return the future time
     * */
    public Time plusHours(int hours) {
        return new Time(time.plusHours(hours));
    }

    /**
     * Tests if this time is before the other time.
     * @param t the other Time
     * @return true if before the other time
     * */
    public boolean isBefore(Time t) {
        return time.isBefore(t.time);
    }

    /**
     * Tests if this time is after the other time.
     * @param t the other Time
     * @return true if after the other time
     * */
    public boolean isAfter(Time t) {
        return time.isAfter(t.time);
    }

    /**
     * Converts this to a LocalTime object.
     * @return the LocalTime
     * */
    public LocalTime toLocalTime() {
        return LocalTime.of(time.getHour(), time.getMinute());
    }

    /**
     * Creates a time object from a LocalDateTime
     * @param dateTime the LocalDateTime
     * @return the Time object
     * */
    public static Time from(LocalDateTime dateTime) {
        return new Time(dateTime.toLocalTime());
    }

    /**
     * Creates a time object from a LocalTime
     * @param time the LocalTime
     * @return the Time object
     * */
    public static Time from(LocalTime time) {
        return new Time(time);
    }
}
