package mcscheduler.logic.commands;

import static mcscheduler.logic.commands.CommandTestUtil.VALID_ROLE_CASHIER;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_ROLE_CHEF;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_ROLE_JANITOR;
import static mcscheduler.logic.commands.CommandTestUtil.assertCommandFailure;
import static mcscheduler.testutil.TypicalIndexes.INDEX_FIRST_SHIFT;
import static mcscheduler.testutil.TypicalIndexes.INDEX_FIRST_WORKER;
import static mcscheduler.testutil.TypicalIndexes.INDEX_FOURTH_WORKER;
import static mcscheduler.testutil.TypicalIndexes.INDEX_SECOND_SHIFT;
import static mcscheduler.testutil.TypicalIndexes.INDEX_SECOND_WORKER;
import static mcscheduler.testutil.TypicalIndexes.INDEX_THIRD_SHIFT;
import static mcscheduler.testutil.TypicalIndexes.INDEX_THIRD_WORKER;
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
import mcscheduler.testutil.TestUtil;

//@@author tnsyn
public class ReassignCommandTest {
    @Test
    public void constructor_nullOldWorkerIndex_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () ->
                new ReassignCommand(null, INDEX_FIRST_WORKER, INDEX_FIRST_SHIFT, INDEX_SECOND_SHIFT,
                        Role.createRole(VALID_ROLE_CHEF)));
    }

    @Test
    public void constructor_nullNewWorkerIndex_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () ->
                new ReassignCommand(INDEX_FIRST_WORKER, null, INDEX_FIRST_SHIFT, INDEX_SECOND_SHIFT,
                        Role.createRole(VALID_ROLE_CHEF)));
    }

    @Test
    public void constructor_nullOldShiftIndex_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () ->
                new ReassignCommand(INDEX_FIRST_WORKER, INDEX_SECOND_WORKER, null, INDEX_SECOND_SHIFT,
                        Role.createRole(VALID_ROLE_CHEF)));
    }

    @Test
    public void constructor_nullNewShiftIndex_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () ->
                new ReassignCommand(INDEX_FIRST_WORKER, INDEX_SECOND_WORKER, INDEX_FIRST_SHIFT, null,
                        Role.createRole(VALID_ROLE_CHEF)));
    }

    @Test
    public void constructor_nullRole_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () ->
                new ReassignCommand(INDEX_FIRST_WORKER, INDEX_SECOND_WORKER, INDEX_FIRST_SHIFT, INDEX_SECOND_SHIFT,
                        null));
    }

    @Test
    public void execute_reassignmentAcceptedByModel_reassignSuccessful() throws Exception {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcSchedulerWithAssignments(), new UserPrefs());

        ReassignCommand validReassignCommand = new ReassignCommand(INDEX_THIRD_WORKER, INDEX_SECOND_WORKER,
                INDEX_THIRD_SHIFT, INDEX_SECOND_SHIFT, Role.createRole(VALID_ROLE_CHEF));
        CommandResult commandResult = validReassignCommand.execute(model);
        Shift oldShift = TestUtil.getShift(model, INDEX_THIRD_SHIFT);
        Worker oldWorker = TestUtil.getWorker(model, INDEX_THIRD_WORKER);
        Assignment oldAssignment = new AssignmentBuilder().withShift(oldShift).withWorker(oldWorker)
            .withRole(VALID_ROLE_CASHIER).build();

        Shift newShift = TestUtil.getShift(model, INDEX_SECOND_SHIFT);
        Worker newWorker = TestUtil.getWorker(model, INDEX_SECOND_WORKER);
        Assignment validReassignment = new AssignmentBuilder().withShift(newShift)
            .withWorker(newWorker)
            .withRole(VALID_ROLE_CHEF).build();

        assertEquals(String.format(ReassignCommand.MESSAGE_REASSIGN_SUCCESS,
                validReassignment, oldAssignment.getRole()), commandResult.getFeedbackToUser());
        assertFalse(model.hasAssignment(oldAssignment));
        assertTrue(model.hasAssignment(validReassignment));
        model.setAssignment(validReassignment, oldAssignment);
    }

    @Test
    public void execute_invalidWorkerIndex_throwsCommandException() {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcSchedulerWithAssignments(), new UserPrefs());
        Index outOfBoundIndex = TestUtil.getOutOfBoundWorkerIndex(model);
        ReassignCommand reassignCommand = new ReassignCommand(INDEX_THIRD_WORKER, outOfBoundIndex, INDEX_THIRD_SHIFT,
                INDEX_SECOND_SHIFT, Role.createRole(VALID_ROLE_CASHIER));

        assertCommandFailure(reassignCommand, model,
                CommandUtil.printOutOfBoundsWorkerIndexError(outOfBoundIndex, ReassignCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_invalidShiftIndex_throwsCommandException() {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcSchedulerWithAssignments(), new UserPrefs());
        Index outOfBoundIndex = TestUtil.getOutOfBoundShiftIndex(model);
        ReassignCommand reassignCommand = new ReassignCommand(INDEX_THIRD_WORKER, outOfBoundIndex, INDEX_THIRD_SHIFT,
                outOfBoundIndex, Role.createRole(VALID_ROLE_CASHIER));

        assertCommandFailure(reassignCommand, model,
                CommandUtil.printOutOfBoundsShiftIndexError(outOfBoundIndex, ReassignCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_duplicateAssignmentCreated_throwsCommandException() {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcSchedulerWithAssignments(), new UserPrefs());

        ReassignCommand reassignCommand = new ReassignCommand(INDEX_THIRD_WORKER, INDEX_THIRD_WORKER, INDEX_THIRD_SHIFT,
                INDEX_THIRD_SHIFT, Role.createRole(VALID_ROLE_CASHIER));

        Assert.assertThrows(CommandException.class, ReassignCommand.MESSAGE_DUPLICATE_ASSIGNMENT_WITH_SAME_ROLE, () ->
                reassignCommand.execute(model));
    }

    @Test
    public void execute_workerNotFitForRole_throwsCommandException() {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcSchedulerWithAssignments(), new UserPrefs());
        ReassignCommand reassignCommand = new ReassignCommand(INDEX_THIRD_WORKER, INDEX_SECOND_WORKER,
                INDEX_THIRD_SHIFT, INDEX_THIRD_SHIFT, Role.createRole(VALID_ROLE_JANITOR));
        Worker workerToReassignTo = TestUtil.getWorker(model, INDEX_SECOND_WORKER);
        Assert.assertThrows(CommandException.class, String.format(Messages.MESSAGE_INVALID_ASSIGNMENT_WORKER_ROLE,
                workerToReassignTo.getName(), Role.createRole(VALID_ROLE_JANITOR)), () ->
                reassignCommand.execute(model));
    }

    @Test
    public void execute_workerUnavailable_throwsCommandException() {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcSchedulerWithAssignments(), new UserPrefs());
        ReassignCommand reassignCommand = new ReassignCommand(INDEX_THIRD_WORKER, INDEX_FIRST_WORKER, INDEX_THIRD_SHIFT,
                INDEX_THIRD_SHIFT, Role.createRole(VALID_ROLE_CASHIER));
        Worker workerToReassignTo = TestUtil.getWorker(model, INDEX_FIRST_WORKER);
        Shift shiftToReassignTo = TestUtil.getShift(model, INDEX_THIRD_SHIFT);
        Assert.assertThrows(CommandException.class, String.format(Messages.MESSAGE_INVALID_ASSIGNMENT_UNAVAILABLE,
                workerToReassignTo.getName(), shiftToReassignTo.getShiftDay(), shiftToReassignTo.getShiftTime()), () ->
                reassignCommand.execute(model));
    }

    @Test
    public void execute_assignmentNotInModel_throwsCommandException() {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcSchedulerWithAssignments(), new UserPrefs());
        ReassignCommand reassignCommand = new ReassignCommand(INDEX_SECOND_WORKER, INDEX_FIRST_WORKER,
                INDEX_FIRST_SHIFT, INDEX_SECOND_SHIFT, Role.createRole(VALID_ROLE_CASHIER));

        assertCommandFailure(reassignCommand, model, ReassignCommand.MESSAGE_ASSIGNMENT_NOT_FOUND);
    }

    @Test
    public void equals() {
        ReassignCommand firstCommand = new ReassignCommand(INDEX_THIRD_WORKER, INDEX_SECOND_WORKER, INDEX_THIRD_SHIFT,
                INDEX_THIRD_SHIFT, Role.createRole(VALID_ROLE_CASHIER));
        ReassignCommand secondCommand = new ReassignCommand(INDEX_THIRD_WORKER, INDEX_FOURTH_WORKER, INDEX_THIRD_SHIFT,
                INDEX_THIRD_SHIFT, Role.createRole(VALID_ROLE_CASHIER));

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
