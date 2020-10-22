package seedu.address.model.worker;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Objects;

import seedu.address.model.shift.ShiftDay;
import seedu.address.model.shift.ShiftDayValue;
import seedu.address.model.shift.ShiftTime;
import seedu.address.model.shift.ShiftTimeValue;

/**
 * Represents a Worker's unavailability in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidUnavailability(String)}
 */
public class Unavailability {
    public static final String MESSAGE_CONSTRAINTS =
            "Unavailability should only contain one of the days: MON, TUE, WED, THUR, FRI, SAT, SUN and/or "
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
        checkArgument(isValidUnavailability(unavailability), MESSAGE_CONSTRAINTS);
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
            ShiftDayValue.valueOf(splitString[0]);
            ShiftTimeValue.valueOf(splitString[1]);
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
