package mcscheduler.model.shift;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;

import mcscheduler.commons.util.AppUtil;

/**
 * Represents a Shift's day in the McScheduler.
 * Guarantees: immutable; is valid as declared in {@link #isValidDay(String)}
 */
public class ShiftDay {


    public static final String MESSAGE_CONSTRAINTS =
            "Shift day should only contain one of the following values: MON, TUE, WED, THU, FRI, SAT, SUN\n";
    public final ShiftDayValue day;

    /**
     * Constructs a {@code ShiftDay}.
     *
     * @param day A valid day.
     */
    public ShiftDay(String day) {
        requireNonNull(day);
        day = day.toUpperCase();
        AppUtil.checkArgument(isValidDay(day), MESSAGE_CONSTRAINTS);
        this.day = ShiftDayValue.valueOf(day);
    }

    private ShiftDay(ShiftDayValue value) {
        this.day = value;
    }

    /**
     * Returns true if a given string is a valid day.
     */
    public static boolean isValidDay(String test) {
        try {
            ShiftDayValue.valueOf(test.toUpperCase());
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    /**
     * Returns a list of all possible {@code ShiftDay}s.
     */
    public static ArrayList<ShiftDay> getAllDays() {
        ArrayList<ShiftDay> allDays = new ArrayList<>();
        for (ShiftDayValue value : ShiftDayValue.values()) {
            allDays.add(new ShiftDay(value));
        }
        return allDays;
    }

    @Override
    public String toString() {
        return day.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof ShiftDay
                && day.equals(((ShiftDay) other).day));
    }

    @Override
    public int hashCode() {
        return day.hashCode();
    }

}
