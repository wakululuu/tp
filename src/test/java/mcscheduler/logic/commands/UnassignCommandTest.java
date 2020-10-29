package mcscheduler.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import mcscheduler.commons.core.Messages;
import mcscheduler.commons.core.index.Index;
import mcscheduler.model.Model;
import mcscheduler.model.ModelManager;
import mcscheduler.model.UserPrefs;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.assignment.WorkerRolePair;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.tag.Role;
import mcscheduler.model.worker.Worker;
import mcscheduler.testutil.Assert;
import mcscheduler.testutil.AssignmentBuilder;
import mcscheduler.testutil.McSchedulerBuilder;
import mcscheduler.testutil.TypicalIndexes;

public class UnassignCommandTest {

    @Test
    public void constructor_nullShiftIndex_throwsNullPointerException() {
        Set<Index> validWorker = new HashSet<>();
        validWorker.add(TypicalIndexes.INDEX_FIRST_WORKER);
        Assert.assertThrows(NullPointerException.class, () ->
            new UnassignCommand(null, validWorker));
    }

    @Test
    public void constructor_nullWorkerIndex_throwsNullPointerException() {
        Set<Index> nullWorker = new HashSet<>();
        nullWorker.add(null);
        Assert.assertThrows(NullPointerException.class, () ->
            new UnassignCommand(TypicalIndexes.INDEX_FIRST_SHIFT, nullWorker));
    }

    @Test
    public void constructor_nullWorkerIndexSet_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () ->
            new UnassignCommand(TypicalIndexes.INDEX_FIRST_SHIFT, null));
    }

    @Test
    public void execute_unassignmentAcceptedByModel_unassignSuccessful() throws Exception {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());

        Set<WorkerRolePair> validWorkerRole = new HashSet<>();
        validWorkerRole.add(new WorkerRolePair(
            TypicalIndexes.INDEX_FIRST_WORKER, Role.createRole(CommandTestUtil.VALID_ROLE_CASHIER)));
        AssignCommand validAssignCommand = new AssignCommand(TypicalIndexes.INDEX_SECOND_SHIFT, validWorkerRole);
        validAssignCommand.execute(model);

        Set<Index> workerIndex = new HashSet<>();
        workerIndex.add(TypicalIndexes.INDEX_FIRST_WORKER);
        UnassignCommand validUnassignCommand = new UnassignCommand(TypicalIndexes.INDEX_SECOND_SHIFT, workerIndex);
        CommandResult commandResult = validUnassignCommand.execute(model);

        Shift shiftToUnassign = model.getFilteredShiftList().get(TypicalIndexes.INDEX_SECOND_SHIFT.getZeroBased());
        Worker workerToUnassign = model.getFilteredWorkerList().get(TypicalIndexes.INDEX_FIRST_WORKER.getZeroBased());
        // the model has the role of cashier
        Assignment validAssignment = new AssignmentBuilder().withShift(shiftToUnassign)
            .withWorker(workerToUnassign).withRole(CommandTestUtil.VALID_ROLE_CASHIER).build();

        assertEquals(String.format(UnassignCommand.MESSAGE_UNASSIGN_SUCCESS + "\n", 1, validAssignment),
            commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(), model.getFullAssignmentList());
    }

    @Test
    public void execute_invalidShiftIndex_throwsCommandException() {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredShiftList().size() + 1);

        Set<Index> workerIndex = new HashSet<>();
        workerIndex.add(TypicalIndexes.INDEX_FIRST_WORKER);
        UnassignCommand unassignCommand = new UnassignCommand(outOfBoundIndex, workerIndex);

        CommandTestUtil.assertCommandFailure(unassignCommand, model, Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidWorkerIndex_throwsCommandException() {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredWorkerList().size() + 1);

        Set<Index> outOfBoundWorkerIndex = new HashSet<>();
        outOfBoundWorkerIndex.add(outOfBoundIndex);
        UnassignCommand unassignCommand = new UnassignCommand(TypicalIndexes.INDEX_FIRST_SHIFT, outOfBoundWorkerIndex);

        CommandTestUtil.assertCommandFailure(unassignCommand, model, Messages.MESSAGE_INVALID_WORKER_DISPLAYED_INDEX);
    }

    @Test
    public void execute_assignmentNotInModel_throwsCommandException() {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());

        Set<Index> workerIndex = new HashSet<>();
        workerIndex.add(TypicalIndexes.INDEX_FIRST_WORKER);
        UnassignCommand unassignCommand = new UnassignCommand(TypicalIndexes.INDEX_SECOND_SHIFT, workerIndex);

        String assignmentName =
            new Assignment(model.getFilteredShiftList().get(TypicalIndexes.INDEX_SECOND_SHIFT.getZeroBased()),
                model.getFilteredWorkerList().get(TypicalIndexes.INDEX_FIRST_WORKER.getZeroBased()))
                .toString();

        CommandTestUtil.assertCommandFailure(unassignCommand, model,
            String.format(UnassignCommand.MESSAGE_ASSIGNMENT_NOT_FOUND, assignmentName));
    }

    @Test
    public void equals() {
        Set<Index> workerIndex = new HashSet<>();
        workerIndex.add(TypicalIndexes.INDEX_FIRST_WORKER);

        UnassignCommand unassignCommand1 = new UnassignCommand(TypicalIndexes.INDEX_FIRST_SHIFT, workerIndex);
        UnassignCommand unassignCommand2 = new UnassignCommand(TypicalIndexes.INDEX_SECOND_SHIFT, workerIndex);

        // same object -> returns true
        assertEquals(unassignCommand1, unassignCommand1);

        // same values -> returns true
        UnassignCommand unassignCommand1Copy = new UnassignCommand(TypicalIndexes.INDEX_FIRST_SHIFT, workerIndex);
        assertEquals(unassignCommand1, unassignCommand1Copy);

        // different types -> returns false
        assertNotEquals(unassignCommand1, 1);

        // null -> returns false
        assertNotEquals(unassignCommand1, null);

        // different assignment -> returns false
        assertNotEquals(unassignCommand1, unassignCommand2);
    }

}
