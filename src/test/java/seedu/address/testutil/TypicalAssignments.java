package seedu.address.testutil;

import static seedu.address.testutil.TypicalShifts.SHIFT_A;
import static seedu.address.testutil.TypicalShifts.SHIFT_B;
import static seedu.address.testutil.TypicalShifts.SHIFT_C;
import static seedu.address.testutil.TypicalWorkers.ALICE;
import static seedu.address.testutil.TypicalWorkers.BOB;
import static seedu.address.testutil.TypicalWorkers.CARL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.assignment.Assignment;

/**
 * A utility class containing a list of {@code Assignment} objects to be used in tests.
 */
public class TypicalAssignments {

    public static final Assignment ASSIGNMENT_A = new AssignmentBuilder().withWorker(ALICE)
            .withShift(SHIFT_B)
            .withRole("cashier").build();
    public static final Assignment ASSIGNMENT_B = new AssignmentBuilder().withWorker(BOB)
            .withShift(SHIFT_A)
            .withRole("chef").build();
    public static final Assignment ASSIGNMENT_C = new AssignmentBuilder().withWorker(CARL)
            .withShift(SHIFT_C)
            .withRole("cashier").build();

    public static List<Assignment> getTypicalAssignments() {
        return new ArrayList<>(Arrays.asList(ASSIGNMENT_A, ASSIGNMENT_B, ASSIGNMENT_C));
    }

}
