
package seedu.address.model.shift;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Shift's time in the App.
 * Guarantees: immutable; is valid as declared in {@link #isValidTime(String)}
 */
public class ShiftTime {

    public static final String MESSAGE_CONSTRAINTS =
            "Shift time should only contain one of the following values: AM, PM";
    public final ShiftTimeValue time;

    /**
     * Constructs a {@code ShiftTime}.
     *
     * @param time A valid time.
     */
    public ShiftTime(String time) {
        requireNonNull(time);
        time = time.toUpperCase();
        checkArgument(isValidTime(time), MESSAGE_CONSTRAINTS);
        this.time = ShiftTimeValue.valueOf(time);
    }


    /**
     * Returns true is a given string is a valid time.
     */
    public static boolean isValidTime(String test) {
        try {
            ShiftTimeValue.valueOf(test);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return time.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof ShiftTime
                && time.equals(((ShiftTime) other).time));
    }

    @Override
    public int hashCode() {
        return time.hashCode();
    }
}
