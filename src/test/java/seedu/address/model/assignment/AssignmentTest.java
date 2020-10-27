package seedu.address.model.assignment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_CHEF;
import static seedu.address.testutil.TypicalAssignments.ASSIGNMENT_A;
import static seedu.address.testutil.TypicalAssignments.ASSIGNMENT_B;
import static seedu.address.testutil.TypicalShifts.SHIFT_B;
import static seedu.address.testutil.TypicalWorkers.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.AssignmentBuilder;

public class AssignmentTest {

    @Test
    public void isSameAssignment() {
        // null -> returns false
        assertFalse(ASSIGNMENT_A.isSameAssignment(null));

        // same object -> returns true
        assertTrue(ASSIGNMENT_A.isSameAssignment(ASSIGNMENT_A));

        // same content -> returns true
        assertTrue(ASSIGNMENT_A.isSameAssignment(new AssignmentBuilder(ASSIGNMENT_A).build()));

        // different shift -> returns false
        assertFalse(ASSIGNMENT_A.isSameAssignment(new AssignmentBuilder(ASSIGNMENT_A).withShift(SHIFT_B).build()));

        // different worker -> returns false
        assertFalse(ASSIGNMENT_A.isSameAssignment(new AssignmentBuilder(ASSIGNMENT_A).withWorker(BOB).build()));

        // different shift and worker -> returns false
        assertFalse(ASSIGNMENT_A.isSameAssignment(
                new AssignmentBuilder(ASSIGNMENT_A).withShift(SHIFT_B).withWorker(BOB).build()));

        // different role -> returns true
        assertTrue(ASSIGNMENT_A.isSameAssignment(new AssignmentBuilder(ASSIGNMENT_A).withRole(VALID_ROLE_CHEF)
                .build()));
    }

    @Test
    public void equals() {
        // null -> returns false
        assertNotEquals(ASSIGNMENT_A, null);

        // same values -> returns true
        Assignment assignmentACopy = new AssignmentBuilder(ASSIGNMENT_A).build();
        assertEquals(assignmentACopy, ASSIGNMENT_A);

        // same object -> returns true
        assertEquals(ASSIGNMENT_A, ASSIGNMENT_A);

        // different type -> returns false
        assertNotEquals(ASSIGNMENT_A, 5);

        // different object -> returns false
        assertNotEquals(ASSIGNMENT_A, ASSIGNMENT_B);

        // different shift -> returns false
        assertFalse(ASSIGNMENT_A.isSameAssignment(new AssignmentBuilder(ASSIGNMENT_A).withShift(SHIFT_B).build()));

        // different worker -> returns false
        assertFalse(ASSIGNMENT_A.isSameAssignment(new AssignmentBuilder(ASSIGNMENT_A).withWorker(BOB).build()));

        // different shift and worker -> returns false
        assertFalse(ASSIGNMENT_A.isSameAssignment(
                new AssignmentBuilder(ASSIGNMENT_A).withShift(SHIFT_B).withWorker(BOB).build()));

        // different role -> returns true
        assertTrue(ASSIGNMENT_A.isSameAssignment(new AssignmentBuilder(ASSIGNMENT_A).withRole(VALID_ROLE_CHEF)
                .build()));
    }

}
