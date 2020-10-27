package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_CASHIER;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.AddressBookBuilder.getTypicalAddressBook;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SHIFT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_WORKER;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_SHIFT;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.assignment.exceptions.AssignmentNotFoundException;
import seedu.address.model.shift.Shift;
import seedu.address.model.tag.Role;
import seedu.address.model.worker.Worker;
import seedu.address.testutil.AssignmentBuilder;

public class UnassignCommandTest {

    @Test
    public void constructor_nullShiftIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new UnassignCommand(null, INDEX_FIRST_WORKER));
    }

    @Test
    public void constructor_nullWorkerIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new UnassignCommand(INDEX_FIRST_SHIFT, null));
    }

    @Test
    public void execute_unassignmentAcceptedByModel_unassignSuccessful() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        AssignCommand validAssignCommand = new AssignCommand(INDEX_SECOND_SHIFT, INDEX_FIRST_WORKER,
                Role.createRole(VALID_ROLE_CASHIER));
        validAssignCommand.execute(model);

        UnassignCommand validUnassignCommand = new UnassignCommand(INDEX_SECOND_SHIFT, INDEX_FIRST_WORKER);
        CommandResult commandResult = validUnassignCommand.execute(model);

        Shift shiftToUnassign = model.getFilteredShiftList().get(INDEX_SECOND_SHIFT.getZeroBased());
        Worker workerToUnassign = model.getFilteredWorkerList().get(INDEX_FIRST_WORKER.getZeroBased());
        Assignment validAssignment = new AssignmentBuilder().withShift(shiftToUnassign)
                .withWorker(workerToUnassign).buildDummy();

        assertEquals(String.format(UnassignCommand.MESSAGE_UNASSIGN_SUCCESS, validAssignment),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(), model.getFullAssignmentList());
    }

    @Test
    public void execute_invalidShiftIndex_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredShiftList().size() + 1);
        UnassignCommand unassignCommand = new UnassignCommand(outOfBoundIndex, INDEX_FIRST_WORKER);

        assertCommandFailure(unassignCommand, model, Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidWorkerIndex_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredWorkerList().size() + 1);
        UnassignCommand unassignCommand = new UnassignCommand(INDEX_FIRST_SHIFT, outOfBoundIndex);

        assertCommandFailure(unassignCommand, model, Messages.MESSAGE_INVALID_WORKER_DISPLAYED_INDEX);
    }

    @Test
    public void execute_assignmentNotInModel_throwsAssignmentNotFoundException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        UnassignCommand unassignCommand = new UnassignCommand(INDEX_SECOND_SHIFT, INDEX_FIRST_WORKER);

        assertThrows(AssignmentNotFoundException.class, () -> unassignCommand.execute(model));
    }

    @Test
    public void equals() {
        UnassignCommand unassignCommand1 = new UnassignCommand(INDEX_FIRST_SHIFT, INDEX_FIRST_WORKER);
        UnassignCommand unassignCommand2 = new UnassignCommand(INDEX_SECOND_SHIFT, INDEX_FIRST_WORKER);

        // same object -> returns true
        assertEquals(unassignCommand1, unassignCommand1);

        // same values -> returns true
        UnassignCommand unassignCommand1Copy = new UnassignCommand(INDEX_FIRST_SHIFT, INDEX_FIRST_WORKER);
        assertEquals(unassignCommand1, unassignCommand1Copy);

        // different types -> returns false
        assertNotEquals(unassignCommand1, 1);

        // null -> returns false
        assertNotEquals(unassignCommand1, null);

        // different assignment -> returns false
        assertNotEquals(unassignCommand1, unassignCommand2);
    }

}
