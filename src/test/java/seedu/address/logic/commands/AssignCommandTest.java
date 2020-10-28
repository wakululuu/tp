package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_CASHIER;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_CHEF;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.AddressBookBuilder.getTypicalAddressBook;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SHIFT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_WORKER;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_SHIFT;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_SHIFT;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.shift.Shift;
import seedu.address.model.tag.Role;
import seedu.address.model.worker.Worker;
import seedu.address.testutil.AssignmentBuilder;

public class AssignCommandTest {

    @Test
    public void constructor_nullShiftIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new AssignCommand(null, INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CHEF)));
    }

    @Test
    public void constructor_nullWorkerIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new AssignCommand(INDEX_FIRST_SHIFT, null, Role.createRole(VALID_ROLE_CHEF)));
    }

    @Test
    public void constructor_nullRole_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AssignCommand(INDEX_FIRST_SHIFT, INDEX_FIRST_WORKER, null));
    }

    @Test
    public void execute_assignmentAcceptedByModel_assignSuccessful() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        AssignCommand validAssignCommand = new AssignCommand(INDEX_SECOND_SHIFT, INDEX_FIRST_WORKER,
                Role.createRole(VALID_ROLE_CASHIER));
        CommandResult commandResult = validAssignCommand.execute(model);

        Shift shiftToAssign = model.getFilteredShiftList().get(INDEX_SECOND_SHIFT.getZeroBased());
        Worker workerToAssign = model.getFilteredWorkerList().get(INDEX_FIRST_WORKER.getZeroBased());
        Assignment validAssignment = new AssignmentBuilder().withShift(shiftToAssign)
                .withWorker(workerToAssign)
                .withRole(VALID_ROLE_CASHIER).build();

        assertEquals(String.format(AssignCommand.MESSAGE_ASSIGN_SUCCESS, validAssignment),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validAssignment), model.getFullAssignmentList());
    }

    @Test
    public void execute_invalidShiftIndex_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredShiftList().size() + 1);
        AssignCommand assignCommand = new AssignCommand(outOfBoundIndex, INDEX_FIRST_WORKER,
                Role.createRole(VALID_ROLE_CASHIER));

        assertCommandFailure(assignCommand, model, Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidWorkerIndex_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredWorkerList().size() + 1);
        AssignCommand assignCommand = new AssignCommand(INDEX_FIRST_SHIFT, outOfBoundIndex,
                Role.createRole(VALID_ROLE_CASHIER));

        assertCommandFailure(assignCommand, model, Messages.MESSAGE_INVALID_WORKER_DISPLAYED_INDEX);
    }

    @Test
    public void execute_duplicateAssignment_throwsCommandException() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        AssignCommand assignCommand = new AssignCommand(INDEX_SECOND_SHIFT, INDEX_FIRST_WORKER,
                Role.createRole(VALID_ROLE_CASHIER));
        assignCommand.execute(model);

        assertThrows(CommandException.class, AssignCommand.MESSAGE_DUPLICATE_ASSIGNMENT, () ->
                assignCommand.execute(model));
    }

    @Test
    public void execute_workerNotFitForRole_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        AssignCommand assignCommand = new AssignCommand(INDEX_THIRD_SHIFT, INDEX_FIRST_WORKER,
                Role.createRole(VALID_ROLE_CHEF));

        assertThrows(CommandException.class, Messages.MESSAGE_INVALID_ASSIGNMENT_WORKER_ROLE, () ->
                assignCommand.execute(model));
    }

    @Test
    public void execute_workerUnavailable_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        AssignCommand assignCommand = new AssignCommand(INDEX_THIRD_SHIFT, INDEX_FIRST_WORKER,
                Role.createRole(VALID_ROLE_CASHIER));

        assertThrows(CommandException.class, Messages.MESSAGE_INVALID_ASSIGNMENT_UNAVAILABLE, () ->
                assignCommand.execute(model));
    }

    @Test
    public void equals() {
        AssignCommand assignCommand1 = new AssignCommand(INDEX_FIRST_SHIFT, INDEX_FIRST_WORKER,
                Role.createRole(VALID_ROLE_CASHIER));
        AssignCommand assignCommand2 = new AssignCommand(INDEX_SECOND_SHIFT, INDEX_FIRST_WORKER,
                Role.createRole(VALID_ROLE_CASHIER));

        // same object -> returns true
        assertEquals(assignCommand1, assignCommand1);

        // same values -> returns true
        AssignCommand assignCommand1Copy = new AssignCommand(INDEX_FIRST_SHIFT, INDEX_FIRST_WORKER,
                Role.createRole(VALID_ROLE_CASHIER));
        assertEquals(assignCommand1, assignCommand1Copy);

        // different types -> returns false
        assertNotEquals(assignCommand1, 1);

        // null -> returns false
        assertNotEquals(assignCommand1, null);

        // different assignment -> returns false
        assertNotEquals(assignCommand1, assignCommand2);
    }

}
