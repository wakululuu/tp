
package mcscheduler.model.shift;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import mcscheduler.commons.util.AppUtil;

/**
 * Represents a Shift's time in the McScheduler.
 * Guarantees: immutable; is valid as declared in {@link #isValidTime(String)}
 */
public class ShiftTime {

    public static final String MESSAGE_CONSTRAINTS =
            "Shift time should only contain one of the following values: AM, PM\n";
    public final ShiftTimeValue time;

    /**
     * Constructs a {@code ShiftTime}.
     *
     * @param time A valid time.
     */
    public ShiftTime(String time) {
        requireNonNull(time);
        time = time.toUpperCase();
        AppUtil.checkArgument(isValidTime(time), MESSAGE_CONSTRAINTS);
        this.time = ShiftTimeValue.valueOf(time);
    }

    private ShiftTime(ShiftTimeValue value) {
        this.time = value;
    }

    /**
     * Returns true is a given string is a valid time.
     */
    public static boolean isValidTime(String test) {
        try {
            ShiftTimeValue.valueOf(test.toUpperCase());
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    /**
     * Returns a list of all possible {@code ShiftTime}s.
     */
    public static ArrayList<ShiftTime> getAllTimes() {
        return new ArrayList<ShiftTime>(Arrays.stream(ShiftTimeValue.values())
                .map(value -> new ShiftTime(value))
                .collect(Collectors.toList()));
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
