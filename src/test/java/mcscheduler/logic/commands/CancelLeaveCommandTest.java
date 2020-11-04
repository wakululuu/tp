package mcscheduler.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import mcscheduler.commons.core.index.Index;
import mcscheduler.logic.commands.exceptions.CommandException;
import mcscheduler.model.McScheduler;
import mcscheduler.model.Model;
import mcscheduler.model.ModelManager;
import mcscheduler.model.UserPrefs;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.role.Leave;
import mcscheduler.model.role.Role;
import mcscheduler.testutil.Assert;
import mcscheduler.testutil.TypicalIndexes;
import mcscheduler.testutil.TypicalShifts;
import mcscheduler.testutil.TypicalWorkers;

public class CancelLeaveCommandTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Set<Index> validIndex = new HashSet<>();
        validIndex.add(TypicalIndexes.INDEX_FIRST_WORKER);
        Assert.assertThrows(NullPointerException.class, () -> new CancelLeaveCommand(null, validIndex));
        Assert.assertThrows(NullPointerException.class, () ->
            new CancelLeaveCommand(TypicalIndexes.INDEX_FIRST_SHIFT, null));
        Assert.assertThrows(NullPointerException.class, () -> new CancelLeaveCommand(null, null));
    }

    @Test
    public void execute_leaveInModel_success() throws Exception {
        ModelManager model = new ModelManager(new McScheduler(), new UserPrefs());
        model.addWorker(TypicalWorkers.BENSON);
        model.addShift(TypicalShifts.SHIFT_A);
        Assignment assignment = new Assignment(TypicalShifts.SHIFT_A, TypicalWorkers.BENSON, new Leave());
        model.addAssignment(assignment);

        assertTrue(model.hasAssignment(assignment));

        Set<Index> validIndex = new HashSet<>();
        validIndex.add(TypicalIndexes.INDEX_FIRST_WORKER);
        CommandResult result = new CancelLeaveCommand(TypicalIndexes.INDEX_FIRST_SHIFT, validIndex).execute(model);

        assertEquals(String.format(CancelLeaveCommand.MESSAGE_CANCEL_LEAVE_SUCCESS_PREFIX
                + UnassignCommand.MESSAGE_UNASSIGN_SUCCESS, validIndex.size(), assignment) + "\n",
            result.getFeedbackToUser());
        assertFalse(model.hasAssignment(assignment));

    }

    @Test
    public void execute_leaveNotInModel_throwsCommandException() {
        ModelManager model = new ModelManager(new McScheduler(), new UserPrefs());

        Set<Index> validIndex = new HashSet<>();
        validIndex.add(TypicalIndexes.INDEX_FIRST_WORKER);
        Assert.assertThrows(CommandException.class, () ->
            new CancelLeaveCommand(TypicalIndexes.INDEX_FIRST_SHIFT, validIndex).execute(model));

        model.addShift(TypicalShifts.SHIFT_A);
        model.addWorker(TypicalWorkers.BENSON);
        Assert.assertThrows(CommandException.class, () ->
            new CancelLeaveCommand(TypicalIndexes.INDEX_FIRST_SHIFT, validIndex).execute(model));
    }

    @Test
    public void execute_assignmentInModelNotLeave_throwsCommandException() {
        Model model = new ModelManager(new McScheduler(), new UserPrefs());
        model.addWorker(TypicalWorkers.BENSON);
        model.addShift(TypicalShifts.SHIFT_A);
        model.addAssignment(new Assignment(TypicalShifts.SHIFT_A, TypicalWorkers.BENSON, Role.createRole("Cashier")));

        Set<Index> validIndex = new HashSet<>();
        validIndex.add(TypicalIndexes.INDEX_FIRST_WORKER);
        Assert.assertThrows(CommandException.class, () ->
            new CancelLeaveCommand(TypicalIndexes.INDEX_FIRST_SHIFT, validIndex).execute(model));
    }

    @Test
    public void equals() {
        Set<Index> validIndex = new HashSet<>();
        validIndex.add(TypicalIndexes.INDEX_FIRST_WORKER);
        Set<Index> validIndexTwo = new HashSet<>();
        validIndexTwo.add(TypicalIndexes.INDEX_SECOND_WORKER);
        CancelLeaveCommand firstIndexes = new CancelLeaveCommand(TypicalIndexes.INDEX_FIRST_SHIFT, validIndex);
        CancelLeaveCommand secondIndexes = new CancelLeaveCommand(TypicalIndexes.INDEX_SECOND_SHIFT, validIndexTwo);
        CancelLeaveCommand firstShiftSecondWorker =
            new CancelLeaveCommand(TypicalIndexes.INDEX_FIRST_SHIFT, validIndexTwo);

        assertTrue(firstIndexes.equals(firstIndexes)); // same object
        assertTrue(
            firstIndexes.equals(new CancelLeaveCommand(TypicalIndexes.INDEX_FIRST_SHIFT, validIndex))); // same values
        assertFalse(firstIndexes.equals(123)); // different type
        assertFalse(firstIndexes.equals(null)); // null
        assertFalse(firstIndexes.equals(secondIndexes)); // different values
        assertFalse(firstIndexes.equals(firstShiftSecondWorker)); // different worker
        assertFalse(secondIndexes.equals(firstShiftSecondWorker)); // different shift
    }

}
