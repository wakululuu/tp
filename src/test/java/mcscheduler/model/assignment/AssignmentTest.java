package mcscheduler.model.assignment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import mcscheduler.logic.commands.CommandTestUtil;
import mcscheduler.testutil.AssignmentBuilder;
import mcscheduler.testutil.TypicalAssignments;
import mcscheduler.testutil.TypicalShifts;
import mcscheduler.testutil.TypicalWorkers;

public class AssignmentTest {

    @Test
    public void isSameAssignment() {
        // null -> returns false
        assertFalse(TypicalAssignments.ASSIGNMENT_A.isSameAssignment(null));

        // same object -> returns true
        assertTrue(TypicalAssignments.ASSIGNMENT_A.isSameAssignment(TypicalAssignments.ASSIGNMENT_A));

        // same content -> returns true
        assertTrue(
            TypicalAssignments.ASSIGNMENT_A
                .isSameAssignment(new AssignmentBuilder(TypicalAssignments.ASSIGNMENT_A).build()));

        // different shift -> returns false
        assertFalse(
            TypicalAssignments.ASSIGNMENT_A
                .isSameAssignment(new AssignmentBuilder(TypicalAssignments.ASSIGNMENT_A).withShift(
                    TypicalShifts.SHIFT_B).build()));

        // different worker -> returns false
        assertFalse(
            TypicalAssignments.ASSIGNMENT_A
                .isSameAssignment(new AssignmentBuilder(TypicalAssignments.ASSIGNMENT_A).withWorker(
                    TypicalWorkers.BOB).build()));

        // different shift and worker -> returns false
        assertFalse(TypicalAssignments.ASSIGNMENT_A.isSameAssignment(
            new AssignmentBuilder(TypicalAssignments.ASSIGNMENT_A).withShift(TypicalShifts.SHIFT_B).withWorker(
                TypicalWorkers.BOB).build()));

        // different role -> returns true
        assertTrue(
            TypicalAssignments.ASSIGNMENT_A
                .isSameAssignment(new AssignmentBuilder(TypicalAssignments.ASSIGNMENT_A).withRole(
                    CommandTestUtil.VALID_ROLE_CHEF)
                    .build()));
    }

    @Test
    public void equals() {
        // null -> returns false
        assertNotEquals(TypicalAssignments.ASSIGNMENT_A, null);

        // same values -> returns true
        Assignment assignmentACopy = new AssignmentBuilder(TypicalAssignments.ASSIGNMENT_A).build();
        assertEquals(assignmentACopy, TypicalAssignments.ASSIGNMENT_A);

        // same object -> returns true
        assertEquals(TypicalAssignments.ASSIGNMENT_A, TypicalAssignments.ASSIGNMENT_A);

        // different type -> returns false
        assertNotEquals(TypicalAssignments.ASSIGNMENT_A, 5);

        // different object -> returns false
        assertNotEquals(TypicalAssignments.ASSIGNMENT_A, TypicalAssignments.ASSIGNMENT_B);

        // different shift -> returns false
        assertFalse(
            TypicalAssignments.ASSIGNMENT_A
                .isSameAssignment(new AssignmentBuilder(TypicalAssignments.ASSIGNMENT_A).withShift(
                    TypicalShifts.SHIFT_B).build()));

        // different worker -> returns false
        assertFalse(
            TypicalAssignments.ASSIGNMENT_A
                .isSameAssignment(new AssignmentBuilder(TypicalAssignments.ASSIGNMENT_A).withWorker(
                    TypicalWorkers.BOB).build()));

        // different shift and worker -> returns false
        assertFalse(TypicalAssignments.ASSIGNMENT_A.isSameAssignment(
            new AssignmentBuilder(TypicalAssignments.ASSIGNMENT_A).withShift(TypicalShifts.SHIFT_B).withWorker(
                TypicalWorkers.BOB).build()));

        // different role -> returns true
        assertTrue(
            TypicalAssignments.ASSIGNMENT_A
                .isSameAssignment(new AssignmentBuilder(TypicalAssignments.ASSIGNMENT_A).withRole(
                    CommandTestUtil.VALID_ROLE_CHEF)
                    .build()));
    }

}
