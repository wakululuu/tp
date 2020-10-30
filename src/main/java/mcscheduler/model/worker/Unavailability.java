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
            "Unavailability must contain one of the days: MON, TUE, WED, THU, FRI, SAT, SUN and "
                    + "one of the times: AM, PM, FULL";
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
        this.time = new ShiftTime(unavailability.split(" ")[1]);
        this.unavailability = unavailability;
    }

    /**
     * Returns true if a given string is a valid unavailability.
     */
    public static boolean isValidUnavailability(String test) {
        String[] splitString = test.split(" ");
        try {
            ShiftDayValue.valueOf(splitString[0].toUpperCase());
            ShiftTimeValue.valueOf(splitString[1].toUpperCase());
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
