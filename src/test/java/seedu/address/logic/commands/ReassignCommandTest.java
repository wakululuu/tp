package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_CASHIER;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_CHEF;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_JANITOR;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.AddressBookBuilder.getTypicalAddressBookWithAssignments;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SHIFT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_WORKER;
import static seedu.address.testutil.TypicalIndexes.INDEX_FOURTH_WORKER;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_SHIFT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_WORKER;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_SHIFT;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_WORKER;

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

public class ReassignCommandTest {
    @Test
    public void constructor_nullOldWorkerIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new ReassignCommand(null, INDEX_FIRST_WORKER, INDEX_FIRST_SHIFT, INDEX_SECOND_SHIFT,
                        Role.createRole(VALID_ROLE_CHEF)));
    }
    @Test
    public void constructor_nullNewWorkerIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new ReassignCommand(INDEX_FIRST_WORKER, null, INDEX_FIRST_SHIFT, INDEX_SECOND_SHIFT,
                        Role.createRole(VALID_ROLE_CHEF)));
    }
    @Test
    public void constructor_nullOldShiftIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new ReassignCommand(INDEX_FIRST_WORKER, INDEX_SECOND_WORKER, null, INDEX_SECOND_SHIFT,
                        Role.createRole(VALID_ROLE_CHEF)));
    }
    @Test
    public void constructor_nullNewShiftIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new ReassignCommand(INDEX_FIRST_WORKER, INDEX_SECOND_WORKER, INDEX_FIRST_SHIFT, null,
                        Role.createRole(VALID_ROLE_CHEF)));
    }
    @Test
    public void constructor_nullRole_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ReassignCommand(INDEX_FIRST_WORKER,
                INDEX_SECOND_WORKER, INDEX_FIRST_SHIFT, INDEX_SECOND_SHIFT, null));
    }
    @Test
    public void execute_reassignmentAcceptedByModel_reassignSuccessful() throws Exception {
        Model model = new ModelManager(getTypicalAddressBookWithAssignments(), new UserPrefs());

        ReassignCommand validReassignCommand = new ReassignCommand(INDEX_THIRD_WORKER,
                INDEX_SECOND_WORKER, INDEX_THIRD_SHIFT, INDEX_SECOND_SHIFT,
                Role.createRole(VALID_ROLE_CHEF));
        CommandResult commandResult = validReassignCommand.execute(model);
        Shift oldShift = model.getFilteredShiftList().get(INDEX_THIRD_SHIFT.getZeroBased());
        Worker oldWorker = model.getFilteredWorkerList().get(INDEX_THIRD_WORKER.getZeroBased());
        Assignment oldAssignment = new AssignmentBuilder().withShift(oldShift).withWorker(oldWorker)
                .withRole(VALID_ROLE_CASHIER).build();

        Shift newShift = model.getFilteredShiftList().get(INDEX_SECOND_SHIFT.getZeroBased());
        Worker newWorker = model.getFilteredWorkerList().get(INDEX_SECOND_WORKER.getZeroBased());
        Assignment validReassignment = new AssignmentBuilder().withShift(newShift)
                .withWorker(newWorker)
                .withRole(VALID_ROLE_CHEF).build();

        assertEquals(String.format(ReassignCommand.MESSAGE_REASSIGN_SUCCESS, validReassignment),
                commandResult.getFeedbackToUser());
        assertFalse(model.hasAssignment(oldAssignment));
        assertTrue(model.hasAssignment(validReassignment));
        model.setAssignment(validReassignment, oldAssignment);
    }
    @Test
    public void execute_invalidWorkerIndex_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBookWithAssignments(), new UserPrefs());
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredWorkerList().size() + 1);
        ReassignCommand reassignCommand = new ReassignCommand(INDEX_THIRD_WORKER, outOfBoundIndex,
                INDEX_THIRD_SHIFT, INDEX_SECOND_SHIFT, Role.createRole(VALID_ROLE_CASHIER));

        assertCommandFailure(reassignCommand, model, Messages.MESSAGE_INVALID_WORKER_DISPLAYED_INDEX);
    }
    @Test
    public void execute_invalidShiftIndex_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBookWithAssignments(), new UserPrefs());
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredShiftList().size() + 1);
        ReassignCommand reassignCommand = new ReassignCommand(INDEX_THIRD_WORKER, outOfBoundIndex,
                INDEX_THIRD_SHIFT, outOfBoundIndex, Role.createRole(VALID_ROLE_CASHIER));

        assertCommandFailure(reassignCommand, model, Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
    }
    @Test
    public void execute_duplicateAssignmentCreated_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBookWithAssignments(), new UserPrefs());

        ReassignCommand reassignCommand = new ReassignCommand(INDEX_THIRD_WORKER, INDEX_THIRD_WORKER,
                INDEX_THIRD_SHIFT, INDEX_THIRD_SHIFT, Role.createRole(VALID_ROLE_CASHIER));

        assertThrows(CommandException.class, ReassignCommand.MESSAGE_DUPLICATE_ASSIGNMENT, () ->
                reassignCommand.execute(model));
    }
    @Test
    public void execute_workerNotFitForRole_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBookWithAssignments(), new UserPrefs());
        ReassignCommand reassignCommand = new ReassignCommand(INDEX_THIRD_WORKER, INDEX_SECOND_WORKER,
                INDEX_THIRD_SHIFT, INDEX_THIRD_SHIFT, Role.createRole(VALID_ROLE_JANITOR));
        assertThrows(CommandException.class, Messages.MESSAGE_INVALID_ASSIGNMENT_WORKER_ROLE, () ->
                reassignCommand.execute(model));
    }
    @Test
    public void execute_workerUnavailable_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBookWithAssignments(), new UserPrefs());
        ReassignCommand reassignCommand = new ReassignCommand(INDEX_THIRD_WORKER, INDEX_FIRST_WORKER,
                INDEX_THIRD_SHIFT, INDEX_THIRD_SHIFT, Role.createRole(VALID_ROLE_CASHIER));

        assertThrows(CommandException.class, Messages.MESSAGE_INVALID_ASSIGNMENT_UNAVAILABLE, () ->
                reassignCommand.execute(model));
    }
    @Test
    public void execute_assignmentNotInModel_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBookWithAssignments(), new UserPrefs());
        ReassignCommand reassignCommand = new ReassignCommand(INDEX_SECOND_WORKER, INDEX_FIRST_WORKER,
                INDEX_FIRST_SHIFT, INDEX_SECOND_SHIFT, Role.createRole(VALID_ROLE_CASHIER));

        assertCommandFailure(reassignCommand, model, ReassignCommand.MESSAGE_ASSIGNMENT_NOT_FOUND);
    }
    @Test
    public void equals() {
        ReassignCommand firstCommand = new ReassignCommand(INDEX_THIRD_WORKER, INDEX_SECOND_WORKER,
                INDEX_THIRD_SHIFT, INDEX_THIRD_SHIFT, Role.createRole(VALID_ROLE_CASHIER));
        ReassignCommand secondCommand = new ReassignCommand(INDEX_THIRD_WORKER, INDEX_FOURTH_WORKER,
                INDEX_THIRD_SHIFT, INDEX_THIRD_SHIFT, Role.createRole(VALID_ROLE_CASHIER));

        // same object -> returns true
        assertEquals(firstCommand, firstCommand);

        // same values -> returns true
        ReassignCommand firstCommandCopy = new ReassignCommand(INDEX_THIRD_WORKER, INDEX_SECOND_WORKER,
                INDEX_THIRD_SHIFT, INDEX_THIRD_SHIFT, Role.createRole(VALID_ROLE_CASHIER));
        assertEquals(firstCommand, firstCommandCopy);

        // different types -> returns false
        assertNotEquals(firstCommand, 1);

        // null -> returns false
        assertNotEquals(firstCommand, null);

        // different assignment -> returns false
        assertNotEquals(firstCommand, secondCommand);
    }

}
