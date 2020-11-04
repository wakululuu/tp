package mcscheduler.model.worker;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import mcscheduler.commons.util.AppUtil;

public class Pay {

    public static final String MESSAGE_CONSTRAINTS =
            "Hourly pay should be a positive number not exceeding 1000, with a maximum of 2 decimal places\n";
    public static final String VALIDATION_REGEX = "^[0-9]+(\\.[0-9]{1,2})?$";
    public static final int MAXIMUM_PAY = 1000;

    private final float value;

    /**
     * Constructs a {@code Pay}.
     *
     * @param amount A valid hourly pay amount.
     */
    public Pay(String amount) {
        requireNonNull(amount);
        AppUtil.checkArgument(isValidPay(amount), MESSAGE_CONSTRAINTS);
        value = Float.parseFloat(amount);
    }

    public float getValue() {
        return value;
    }

    /**
     * Returns true if a given string is a valid pay.
     */
    public static boolean isValidPay(String test) {
        return test.matches(VALIDATION_REGEX)
                && Float.parseFloat(test) > 0
                && Float.parseFloat(test) <= MAXIMUM_PAY;
    }

    @Override
    public String toString() {
        return String.format("$%,.2f/hr", value);
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
