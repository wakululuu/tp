package mcscheduler.model.worker;

import static java.util.Objects.requireNonNull;
import static mcscheduler.logic.parser.UnavailabilitySyntax.REGEX;

import java.util.Objects;

import mcscheduler.commons.util.AppUtil;
import mcscheduler.model.shift.ShiftDay;
import mcscheduler.model.shift.ShiftDayValue;
import mcscheduler.model.shift.ShiftTime;
import mcscheduler.model.shift.ShiftTimeValue;

/**
 * Represents a Worker's unavailability in the McScheduler.
 * Guarantees: immutable; is valid as declared in {@link #isValidUnavailability(String)}
 */
public class Unavailability {
    public static final String MESSAGE_CONSTRAINTS =
            "An unavailability can only contain one of the days: MON, TUE, WED, THU, FRI, SAT, SUN and/or "
                    + "one of the times: AM, PM\n";
    private final ShiftDay day;
    private final ShiftTime time;
    private final String unavailabilityString;

    /**
     * Constructs an {@code Unavailability}.
     *
     * @param unavailability A valid unavailability String.
     */
    public Unavailability(String unavailability) {
        requireNonNull(unavailability);
        AppUtil.checkArgument(isValidUnavailability(unavailability), MESSAGE_CONSTRAINTS);

        String[] splitString = unavailability.split(REGEX);
        String dayString = splitString[0];
        String timeString = splitString[1];
        this.day = new ShiftDay(dayString);
        this.time = new ShiftTime(timeString);
        this.unavailabilityString = String.format("%s %s", dayString.toUpperCase(), timeString.toUpperCase());
    }

    /**
     * Returns true if a given string is a valid unavailability.
     */
    public static boolean isValidUnavailability(String test) {
        String[] splitString = test.split(REGEX);
        boolean hasTwoKeywords = splitString.length == 2;
        if (!hasTwoKeywords) {
            return false;
        }
        return hasValidUnavailabilityDay(splitString) && hasValidUnavailabilityTime(splitString);
    }

    /**
     * Returns true if a given string has a valid day.
     */
    public static boolean hasValidUnavailabilityDay(String[] test) {
        try {
            ShiftDayValue.valueOf(test[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            return false;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }

    /**
     * Returns true if a given string has a valid time.
     */
    public static boolean hasValidUnavailabilityTime(String[] test) {
        try {
            ShiftTimeValue.valueOf(test[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            return false;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }

    public ShiftDay getDay() {
        return day;
    }

    public ShiftTime getTime() {
        return time;
    }

    public String getString() {
        return unavailabilityString;
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(day, time);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Unavailability)) {
            return false;
        }

        // state check
        Unavailability u = (Unavailability) other;
        return u.getDay().toString().equals(day.toString())
                && u.getTime().toString().equals(time.toString());
    }

    @Override
    public String toString() {
        return "[" + day + " " + time + "]";
    }
}
