package mcscheduler.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import mcscheduler.commons.core.Messages;
import mcscheduler.commons.core.index.Index;
import mcscheduler.logic.commands.exceptions.CommandException;
import mcscheduler.model.Model;
import mcscheduler.model.ModelManager;
import mcscheduler.model.UserPrefs;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.role.Role;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.worker.Worker;
import mcscheduler.testutil.Assert;
import mcscheduler.testutil.AssignmentBuilder;
import mcscheduler.testutil.McSchedulerBuilder;
import mcscheduler.testutil.TypicalIndexes;

public class ReassignCommandTest {
    @Test
    public void constructor_nullOldWorkerIndex_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () ->
            new ReassignCommand(null, TypicalIndexes.INDEX_FIRST_WORKER, TypicalIndexes.INDEX_FIRST_SHIFT,
                TypicalIndexes.INDEX_SECOND_SHIFT,
                Role.createRole(CommandTestUtil.VALID_ROLE_CHEF)));
    }

    @Test
    public void constructor_nullNewWorkerIndex_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () ->
            new ReassignCommand(TypicalIndexes.INDEX_FIRST_WORKER, null, TypicalIndexes.INDEX_FIRST_SHIFT,
                TypicalIndexes.INDEX_SECOND_SHIFT,
                Role.createRole(CommandTestUtil.VALID_ROLE_CHEF)));
    }

    @Test
    public void constructor_nullOldShiftIndex_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () ->
            new ReassignCommand(TypicalIndexes.INDEX_FIRST_WORKER, TypicalIndexes.INDEX_SECOND_WORKER, null,
                TypicalIndexes.INDEX_SECOND_SHIFT,
                Role.createRole(CommandTestUtil.VALID_ROLE_CHEF)));
    }

    @Test
    public void constructor_nullNewShiftIndex_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () ->
            new ReassignCommand(TypicalIndexes.INDEX_FIRST_WORKER, TypicalIndexes.INDEX_SECOND_WORKER,
                TypicalIndexes.INDEX_FIRST_SHIFT, null,
                Role.createRole(CommandTestUtil.VALID_ROLE_CHEF)));
    }

    @Test
    public void constructor_nullRole_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new ReassignCommand(TypicalIndexes.INDEX_FIRST_WORKER,
            TypicalIndexes.INDEX_SECOND_WORKER, TypicalIndexes.INDEX_FIRST_SHIFT, TypicalIndexes.INDEX_SECOND_SHIFT,
            null));
    }

    @Test
    public void execute_reassignmentAcceptedByModel_reassignSuccessful() throws Exception {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcSchedulerWithAssignments(), new UserPrefs());

        ReassignCommand validReassignCommand = new ReassignCommand(TypicalIndexes.INDEX_THIRD_WORKER,
            TypicalIndexes.INDEX_SECOND_WORKER, TypicalIndexes.INDEX_THIRD_SHIFT, TypicalIndexes.INDEX_SECOND_SHIFT,
            Role.createRole(CommandTestUtil.VALID_ROLE_CHEF));
        CommandResult commandResult = validReassignCommand.execute(model);
        Shift oldShift = model.getFilteredShiftList().get(TypicalIndexes.INDEX_THIRD_SHIFT.getZeroBased());
        Worker oldWorker = model.getFilteredWorkerList().get(TypicalIndexes.INDEX_THIRD_WORKER.getZeroBased());
        Assignment oldAssignment = new AssignmentBuilder().withShift(oldShift).withWorker(oldWorker)
            .withRole(CommandTestUtil.VALID_ROLE_CASHIER).build();

        Shift newShift = model.getFilteredShiftList().get(TypicalIndexes.INDEX_SECOND_SHIFT.getZeroBased());
        Worker newWorker = model.getFilteredWorkerList().get(TypicalIndexes.INDEX_SECOND_WORKER.getZeroBased());
        Assignment validReassignment = new AssignmentBuilder().withShift(newShift)
            .withWorker(newWorker)
            .withRole(CommandTestUtil.VALID_ROLE_CHEF).build();

        assertEquals(String.format(ReassignCommand.MESSAGE_REASSIGN_SUCCESS, validReassignment),
            commandResult.getFeedbackToUser());
        assertFalse(model.hasAssignment(oldAssignment));
        assertTrue(model.hasAssignment(validReassignment));
        model.setAssignment(validReassignment, oldAssignment);
    }

    @Test
    public void execute_invalidWorkerIndex_throwsCommandException() {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcSchedulerWithAssignments(), new UserPrefs());
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredWorkerList().size() + 1);
        ReassignCommand reassignCommand = new ReassignCommand(TypicalIndexes.INDEX_THIRD_WORKER, outOfBoundIndex,
            TypicalIndexes.INDEX_THIRD_SHIFT, TypicalIndexes.INDEX_SECOND_SHIFT, Role.createRole(
            CommandTestUtil.VALID_ROLE_CASHIER));

        CommandTestUtil.assertCommandFailure(reassignCommand, model, Messages.MESSAGE_INVALID_WORKER_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidShiftIndex_throwsCommandException() {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcSchedulerWithAssignments(), new UserPrefs());
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredShiftList().size() + 1);
        ReassignCommand reassignCommand = new ReassignCommand(TypicalIndexes.INDEX_THIRD_WORKER, outOfBoundIndex,
            TypicalIndexes.INDEX_THIRD_SHIFT, outOfBoundIndex, Role.createRole(CommandTestUtil.VALID_ROLE_CASHIER));

        CommandTestUtil.assertCommandFailure(reassignCommand, model, Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_duplicateAssignmentCreated_throwsCommandException() {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcSchedulerWithAssignments(), new UserPrefs());

        ReassignCommand reassignCommand = new ReassignCommand(
            TypicalIndexes.INDEX_THIRD_WORKER, TypicalIndexes.INDEX_THIRD_WORKER,
            TypicalIndexes.INDEX_THIRD_SHIFT, TypicalIndexes.INDEX_THIRD_SHIFT, Role.createRole(
            CommandTestUtil.VALID_ROLE_CASHIER));

        Assert.assertThrows(CommandException.class, ReassignCommand.MESSAGE_DUPLICATE_ASSIGNMENT, () ->
            reassignCommand.execute(model));
    }

    @Test
    public void execute_workerNotFitForRole_throwsCommandException() {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcSchedulerWithAssignments(), new UserPrefs());
        ReassignCommand reassignCommand = new ReassignCommand(
            TypicalIndexes.INDEX_THIRD_WORKER, TypicalIndexes.INDEX_SECOND_WORKER,
            TypicalIndexes.INDEX_THIRD_SHIFT, TypicalIndexes.INDEX_THIRD_SHIFT, Role.createRole(
            CommandTestUtil.VALID_ROLE_JANITOR));
        Assert.assertThrows(CommandException.class, Messages.MESSAGE_INVALID_ASSIGNMENT_WORKER_ROLE, () ->
            reassignCommand.execute(model));
    }

    @Test
    public void execute_workerUnavailable_throwsCommandException() {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcSchedulerWithAssignments(), new UserPrefs());
        ReassignCommand reassignCommand = new ReassignCommand(
            TypicalIndexes.INDEX_THIRD_WORKER, TypicalIndexes.INDEX_FIRST_WORKER,
            TypicalIndexes.INDEX_THIRD_SHIFT, TypicalIndexes.INDEX_THIRD_SHIFT, Role.createRole(
            CommandTestUtil.VALID_ROLE_CASHIER));

        Assert.assertThrows(CommandException.class, Messages.MESSAGE_INVALID_ASSIGNMENT_UNAVAILABLE, () ->
            reassignCommand.execute(model));
    }

    @Test
    public void execute_assignmentNotInModel_throwsCommandException() {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcSchedulerWithAssignments(), new UserPrefs());
        ReassignCommand reassignCommand = new ReassignCommand(
            TypicalIndexes.INDEX_SECOND_WORKER, TypicalIndexes.INDEX_FIRST_WORKER,
            TypicalIndexes.INDEX_FIRST_SHIFT, TypicalIndexes.INDEX_SECOND_SHIFT, Role.createRole(
            CommandTestUtil.VALID_ROLE_CASHIER));

        CommandTestUtil.assertCommandFailure(reassignCommand, model, ReassignCommand.MESSAGE_ASSIGNMENT_NOT_FOUND);
    }

    @Test
    public void equals() {
        ReassignCommand firstCommand = new ReassignCommand(
            TypicalIndexes.INDEX_THIRD_WORKER, TypicalIndexes.INDEX_SECOND_WORKER,
            TypicalIndexes.INDEX_THIRD_SHIFT, TypicalIndexes.INDEX_THIRD_SHIFT, Role.createRole(
            CommandTestUtil.VALID_ROLE_CASHIER));
        ReassignCommand secondCommand = new ReassignCommand(
            TypicalIndexes.INDEX_THIRD_WORKER, TypicalIndexes.INDEX_FOURTH_WORKER,
            TypicalIndexes.INDEX_THIRD_SHIFT, TypicalIndexes.INDEX_THIRD_SHIFT, Role.createRole(
            CommandTestUtil.VALID_ROLE_CASHIER));

        // same object -> returns true
        assertEquals(firstCommand, firstCommand);

        // same values -> returns true
        ReassignCommand firstCommandCopy = new ReassignCommand(
            TypicalIndexes.INDEX_THIRD_WORKER, TypicalIndexes.INDEX_SECOND_WORKER,
            TypicalIndexes.INDEX_THIRD_SHIFT, TypicalIndexes.INDEX_THIRD_SHIFT, Role.createRole(
            CommandTestUtil.VALID_ROLE_CASHIER));
        assertEquals(firstCommand, firstCommandCopy);

        // different types -> returns false
        assertNotEquals(firstCommand, 1);

        // null -> returns false
        assertNotEquals(firstCommand, null);

        // different assignment -> returns false
        assertNotEquals(firstCommand, secondCommand);
    }

}
