package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.shift.Shift;

/**
 * A utility class containing a list of {@code Shift} objects to be used in tests.
 */
public class TypicalShifts {

    public static final Shift SHIFT_A = new ShiftBuilder().withShiftDay("MON")
            .withShiftTime("AM")
            .withRoleRequirements("cashier 1").build();
    public static final Shift SHIFT_B = new ShiftBuilder().withShiftDay("MON")
            .withShiftTime("PM")
            .withRoleRequirements("cashier 1", "chef 3").build();
    public static final Shift SHIFT_C = new ShiftBuilder().withShiftDay("TUE")
            .withShiftTime("AM")
            .withRoleRequirements("cleaner 4", "cashier 3").build();

    public static List<Shift> getTypicalShifts() {
        return new ArrayList<>(Arrays.asList(SHIFT_A, SHIFT_B, SHIFT_C));
    }

}
