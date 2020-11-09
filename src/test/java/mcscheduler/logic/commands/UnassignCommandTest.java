package mcscheduler.logic.commands;

import static mcscheduler.logic.commands.CommandTestUtil.VALID_ROLE_CASHIER;
import static mcscheduler.logic.commands.CommandTestUtil.assertCommandFailure;
import static mcscheduler.testutil.TypicalIndexes.INDEX_FIRST_SHIFT;
import static mcscheduler.testutil.TypicalIndexes.INDEX_FIRST_WORKER;
import static mcscheduler.testutil.TypicalIndexes.INDEX_SECOND_SHIFT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import mcscheduler.commons.core.index.Index;
import mcscheduler.model.Model;
import mcscheduler.model.ModelManager;
import mcscheduler.model.UserPrefs;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.assignment.WorkerRolePair;
import mcscheduler.model.role.Role;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.worker.Worker;
import mcscheduler.testutil.Assert;
import mcscheduler.testutil.AssignmentBuilder;
import mcscheduler.testutil.McSchedulerBuilder;
import mcscheduler.testutil.TestUtil;

public class UnassignCommandTest {

    @Test
    public void constructor_nullShiftIndex_throwsNullPointerException() {
        Set<Index> validWorker = new HashSet<>();
        validWorker.add(INDEX_FIRST_WORKER);
        Assert.assertThrows(NullPointerException.class, () -> new UnassignCommand(null, validWorker));
    }

    @Test
    public void constructor_nullWorkerIndex_throwsNullPointerException() {
        Set<Index> nullWorker = new HashSet<>();
        nullWorker.add(null);
        Assert.assertThrows(NullPointerException.class, () -> new UnassignCommand(INDEX_FIRST_SHIFT, nullWorker));
    }

    @Test
    public void constructor_nullWorkerIndexSet_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new UnassignCommand(INDEX_FIRST_SHIFT, null));
    }

    @Test
    public void execute_unassignmentAcceptedByModel_unassignSuccessful() throws Exception {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());

        Set<WorkerRolePair> validWorkerRole = new HashSet<>();
        validWorkerRole.add(new WorkerRolePair(INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CASHIER)));
        AssignCommand validAssignCommand = new AssignCommand(INDEX_SECOND_SHIFT, validWorkerRole);
        validAssignCommand.execute(model);

        Set<Index> workerIndex = new HashSet<>();
        workerIndex.add(INDEX_FIRST_WORKER);
        UnassignCommand validUnassignCommand = new UnassignCommand(INDEX_SECOND_SHIFT, workerIndex);
        CommandResult commandResult = validUnassignCommand.execute(model);

        Shift shiftToUnassign = TestUtil.getShift(model, INDEX_SECOND_SHIFT);
        Worker workerToUnassign = TestUtil.getWorker(model, INDEX_FIRST_WORKER);
        // the model has the role of cashier
        Assignment validAssignment = new AssignmentBuilder()
                .withShift(shiftToUnassign)
                .withWorker(workerToUnassign)
                .withRole(VALID_ROLE_CASHIER)
                .build();

        assertEquals(String.format(UnassignCommand.MESSAGE_UNASSIGN_SUCCESS + "\n", 1, validAssignment),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(), model.getFullAssignmentList());
    }

    @Test
    public void execute_invalidShiftIndex_throwsCommandException() {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());
        Index outOfBoundIndex = TestUtil.getOutOfBoundShiftIndex(model);

        Set<Index> workerIndex = new HashSet<>();
        workerIndex.add(INDEX_FIRST_WORKER);
        UnassignCommand unassignCommand = new UnassignCommand(outOfBoundIndex, workerIndex);

        assertCommandFailure(unassignCommand, model,
                CommandUtil.printOutOfBoundsShiftIndexError(outOfBoundIndex, UnassignCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_invalidWorkerIndex_throwsCommandException() {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());
        Index outOfBoundIndex = TestUtil.getOutOfBoundWorkerIndex(model);

        Set<Index> outOfBoundWorkerIndex = new HashSet<>();
        outOfBoundWorkerIndex.add(outOfBoundIndex);
        UnassignCommand unassignCommand = new UnassignCommand(INDEX_FIRST_SHIFT, outOfBoundWorkerIndex);

        assertCommandFailure(unassignCommand, model,
                CommandUtil.printOutOfBoundsWorkerIndexError(outOfBoundIndex, UnassignCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_assignmentNotInModel_throwsCommandException() {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());

        Set<Index> workerIndex = new HashSet<>();
        workerIndex.add(INDEX_FIRST_WORKER);
        UnassignCommand unassignCommand = new UnassignCommand(INDEX_SECOND_SHIFT, workerIndex);

        String assignmentName = new Assignment(TestUtil.getShift(model, INDEX_SECOND_SHIFT),
                TestUtil.getWorker(model, INDEX_FIRST_WORKER)).toString();

        assertCommandFailure(unassignCommand, model,
                String.format(UnassignCommand.MESSAGE_ASSIGNMENT_NOT_FOUND, assignmentName));
    }

    @Test
    public void equals() {
        Set<Index> workerIndex = new HashSet<>();
        workerIndex.add(INDEX_FIRST_WORKER);

        UnassignCommand unassignCommand1 = new UnassignCommand(INDEX_FIRST_SHIFT, workerIndex);
        UnassignCommand unassignCommand2 = new UnassignCommand(INDEX_SECOND_SHIFT, workerIndex);

        // same object -> returns true
        assertEquals(unassignCommand1, unassignCommand1);

        // same values -> returns true
        UnassignCommand unassignCommand1Copy = new UnassignCommand(INDEX_FIRST_SHIFT, workerIndex);
        assertEquals(unassignCommand1, unassignCommand1Copy);

        // different types -> returns false
        assertNotEquals(unassignCommand1, 1);

        // null -> returns false
        assertNotEquals(unassignCommand1, null);

        // different assignment -> returns false
        assertNotEquals(unassignCommand1, unassignCommand2);
    }

}
