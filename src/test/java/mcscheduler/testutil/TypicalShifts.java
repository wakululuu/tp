package mcscheduler.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mcscheduler.model.shift.Shift;

/**
 * A utility class containing a list of {@code Shift} objects to be used in tests.
 */
public class TypicalShifts {

    public static final Shift SHIFT_A = new ShiftBuilder().withShiftDay("MON")
            .withShiftTime("AM")
            .withRoleRequirements("cashier 1 1").build();
    public static final Shift SHIFT_B = new ShiftBuilder().withShiftDay("FRI")
            .withShiftTime("PM")
            .withRoleRequirements("cashier 2 0", "chef 3 1").build();
    public static final Shift SHIFT_C = new ShiftBuilder().withShiftDay("TUE")
            .withShiftTime("AM")
            .withRoleRequirements("janitor 4 0", "cashier 3 1").build();
    public static final Shift SHIFT_D = new ShiftBuilder().withShiftDay("TUE")
            .withShiftTime("PM")
            .withRoleRequirements("cashier 2 0", "janitor 2 0", "chef 2 0").build();

    public static List<Shift> getTypicalShifts() {
        return new ArrayList<>(Arrays.asList(SHIFT_A, SHIFT_B, SHIFT_C, SHIFT_D));
    }

}
