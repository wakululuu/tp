package mcscheduler.model.worker;

import static java.util.Objects.requireNonNull;
import static mcscheduler.commons.util.AppUtil.checkArgument;

import java.util.Objects;
import mcscheduler.commons.util.*;

public class Pay {

    public static final String MESSAGE_CONSTRAINTS =
            "Hourly pay should contain numbers with a maximum of 2 decimal places.";
    public static final String VALIDATION_REGEX = "^[0-9]+(\\.[0-9]{1,2})?";
    public final float value;

    /**
     * Constructs a {@code Pay}.
     *
     * @param amount A valid hourly pay amount.
     */
    public Pay(String amount) {
        requireNonNull(amount);
        AppUtil.checkArgument(isValidPay(amount), MESSAGE_CONSTRAINTS);
        value = Float.valueOf(amount);
    }

    /**
     * Returns true if a given string is a valid phone number.
     */
    public static boolean isValidPay(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Pay // instanceof handles nulls
                && value == ((Pay) other).value); // state check
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

}
