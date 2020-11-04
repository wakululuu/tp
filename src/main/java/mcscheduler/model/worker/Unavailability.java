package mcscheduler.model.worker;

import static java.util.Objects.requireNonNull;

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
    private final String unavailability;

    /**
     * Constructs an {@code Unavailability}.
     *
     * @param unavailability A valid unavailability.
     */
    public Unavailability(String unavailability) {
        requireNonNull(unavailability);
        AppUtil.checkArgument(isValidUnavailability(unavailability), MESSAGE_CONSTRAINTS);
        this.day = new ShiftDay(unavailability.split(" ")[0]);

        // To account for white spaces between day and time
        int timeIndex = unavailability.split(" ").length - 1;
        this.time = new ShiftTime(unavailability.split(" ")[timeIndex]);
        this.unavailability = unavailability;
    }

    /**
     * Returns true if a given string is a valid unavailability.
     */
    public static boolean isValidUnavailability(String test) {
        String[] splitString = test.split(" ");
        boolean hasTwoKeywords = test.split("\\s+").length == 2;
        try {
            if (!hasTwoKeywords) {
                return false;
            }
            ShiftDayValue.valueOf(splitString[0].toUpperCase());

            // To account for white spaces between day and time
            int shiftTimeIndex = splitString.length - 1;
            ShiftTimeValue.valueOf(splitString[shiftTimeIndex].toUpperCase());
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
        return unavailability;
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
