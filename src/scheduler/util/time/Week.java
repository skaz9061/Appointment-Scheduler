package scheduler.util.time;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Represents a calendar week starting with Sunday.
 * */
public class Week {
    LocalDate firstDay;
    LocalDate lastDay;

    private Week(LocalDate firstDay, LocalDate lastDay) {
        this.firstDay = firstDay;
        this.lastDay = lastDay;
    }

    /**
     * Get a Week instance containing the specified date.
     * @param dateTime the date
     * @return the week of that date
     * */
    public static Week of(LocalDateTime dateTime) {
        return of(LocalDate.from(dateTime));
    }

    /**
     * Get a Week instance containing the specified date.
     * @param date the date
     * @return the week of that date
     * */
    public static Week of(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        LocalDate firstDay = date.minusDays(dayOfWeek.getValue());
        LocalDate lastDay = firstDay.plusDays(6);

        return new Week(firstDay, lastDay);
    }

    /**
     * Getter for the first day.
     * @return the first day
     * */
    public LocalDate getFirstDay() {
        return firstDay;
    }

    /**
     * Getter for the last day.
     * @return the last day
     * */
    public LocalDate getLastDay() {
        return lastDay;
    }

    /**
     * Creates a new Week instance for the next week after this one.
     * @return the next week
     * */
    public Week nextWeek() {
        return new Week(firstDay.plusDays(7), lastDay.plusDays(7));
    }

    /**
     * Creates a new Week instance for the previous week before this one.
     * @return the previous week
     * */
    public Week prevWeek() {
        return new Week(firstDay.minusDays(7), lastDay.minusDays(7));
    }

    /**
     * Test whether given date is represented by this week instance.
     * @param dateTime the date to test
     * @return true if the date is represented by this week
     * */
    public boolean contains(LocalDateTime dateTime) {
        return contains(LocalDate.from(dateTime));
    }

    /**
     * Test whether given date is represented by this week instance.
     * @param date the date to test
     * @return true if the date is represented by this week
     * */
    public boolean contains(LocalDate date) {
        if (date.isEqual(firstDay) || date.isEqual(lastDay))
            return true;

        return date.isAfter(firstDay) && date.isBefore(lastDay);
    }

    /**
     * Converts the object into a string representation.
     * @return the object as a string
     * */
    @Override
    public String toString() {
        DateTimeFormatter format = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);

        return firstDay.format(format) + " - " + lastDay.format(format);
    }

    /**
     * Gets the week as a formatted ISO string.
     * @return the formatted ISO string
     * */
    public String toISO() {
        DateTimeFormatter format = DateTimeFormatter.ISO_DATE;

        return firstDay.format(format) + " - " + lastDay.format(format);
    }
}
