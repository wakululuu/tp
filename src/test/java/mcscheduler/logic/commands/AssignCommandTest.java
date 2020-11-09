package mcscheduler.logic.commands;

import static mcscheduler.logic.commands.CommandTestUtil.VALID_ROLE_CASHIER;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_ROLE_CHEF;
import static mcscheduler.logic.commands.CommandTestUtil.assertCommandFailure;
import static mcscheduler.testutil.TypicalIndexes.INDEX_FIRST_SHIFT;
import static mcscheduler.testutil.TypicalIndexes.INDEX_FIRST_WORKER;
import static mcscheduler.testutil.TypicalIndexes.INDEX_SECOND_SHIFT;
import static mcscheduler.testutil.TypicalIndexes.INDEX_THIRD_SHIFT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import mcscheduler.commons.core.Messages;
import mcscheduler.commons.core.index.Index;
import mcscheduler.logic.commands.exceptions.CommandException;
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

public class AssignCommandTest {

    @Test
    public void constructor_nullShiftIndex_throwsNullPointerException() {
        Set<WorkerRolePair> validWorkerRole = new HashSet<>();
        validWorkerRole.add(new WorkerRolePair(INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CASHIER)));
        Assert.assertThrows(NullPointerException.class, () -> new AssignCommand(null, validWorkerRole));
    }

    @Test
    public void execute_assignmentAcceptedByModel_assignSuccessful() throws Exception {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());

        Set<WorkerRolePair> validWorkerRole = new HashSet<>();
        validWorkerRole.add(new WorkerRolePair(INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CASHIER)));
        AssignCommand validAssignCommand = new AssignCommand(INDEX_SECOND_SHIFT, validWorkerRole);
        CommandResult commandResult = validAssignCommand.execute(model);

        Shift shiftToAssign = TestUtil.getShift(model, INDEX_SECOND_SHIFT);
        Worker workerToAssign = TestUtil.getWorker(model, INDEX_FIRST_WORKER);
        Assignment validAssignment = new AssignmentBuilder()
                .withShift(shiftToAssign)
                .withWorker(workerToAssign)
                .withRole(VALID_ROLE_CASHIER)
                .build();

        assertEquals(String.format(AssignCommand.MESSAGE_ASSIGN_SUCCESS, 1, validAssignment + "\n"),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validAssignment), model.getFullAssignmentList());
    }

    @Test
    public void execute_invalidShiftIndex_throwsCommandException() {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());
        Index outOfBoundIndex = TestUtil.getOutOfBoundShiftIndex(model);

        Set<WorkerRolePair> validWorkerRole = new HashSet<>();
        validWorkerRole.add(new WorkerRolePair(INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CHEF)));
        AssignCommand assignCommand = new AssignCommand(outOfBoundIndex, validWorkerRole);

        assertCommandFailure(assignCommand, model,
                CommandUtil.printOutOfBoundsShiftIndexError(outOfBoundIndex, AssignCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_invalidWorkerIndex_throwsCommandException() {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());
        Index outOfBoundIndex = TestUtil.getOutOfBoundWorkerIndex(model);

        Set<WorkerRolePair> invalidWorkerIndex = new HashSet<>();
        invalidWorkerIndex.add(new WorkerRolePair(outOfBoundIndex, Role.createRole(VALID_ROLE_CASHIER)));
        AssignCommand assignCommand = new AssignCommand(INDEX_FIRST_SHIFT, invalidWorkerIndex);

        assertCommandFailure(assignCommand, model,
                CommandUtil.printOutOfBoundsWorkerIndexError(outOfBoundIndex, AssignCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_duplicateAssignment_throwsCommandException() throws Exception {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());

        Set<WorkerRolePair> validWorkerRole = new HashSet<>();
        validWorkerRole.add(new WorkerRolePair(INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CASHIER)));
        AssignCommand assignCommand = new AssignCommand(INDEX_SECOND_SHIFT, validWorkerRole);
        assignCommand.execute(model);

        Assignment assignmentName = new Assignment(TestUtil.getShift(model, INDEX_SECOND_SHIFT),
                TestUtil.getWorker(model, INDEX_FIRST_WORKER), Role.createRole(VALID_ROLE_CASHIER));

        Assert.assertThrows(CommandException.class,
                String.format(AssignCommand.MESSAGE_DUPLICATE_ASSIGNMENT, assignmentName), () ->
                        assignCommand.execute(model));
    }

    @Test
    public void execute_workerNotFitForRole_throwsCommandException() {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());

        Set<WorkerRolePair> notFitWorkerRole = new HashSet<>();
        notFitWorkerRole.add(new WorkerRolePair(INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CHEF)));
        AssignCommand assignCommand = new AssignCommand(INDEX_THIRD_SHIFT, notFitWorkerRole);

        String workerName = TestUtil.getWorker(model, INDEX_FIRST_WORKER).getName().toString();

        Assert.assertThrows(CommandException.class,
                String.format(Messages.MESSAGE_INVALID_ASSIGNMENT_WORKER_ROLE, workerName, VALID_ROLE_CHEF), () ->
                        assignCommand.execute(model));
    }

    @Test
    public void execute_workerUnavailable_throwsCommandException() {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());

        Set<WorkerRolePair> validWorkerRole = new HashSet<>();
        validWorkerRole.add(new WorkerRolePair(INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CASHIER)));
        AssignCommand assignCommand = new AssignCommand(INDEX_THIRD_SHIFT, validWorkerRole);

        String workerName = TestUtil.getWorker(model, INDEX_FIRST_WORKER).getName().toString();
        Shift shift = TestUtil.getShift(model, INDEX_THIRD_SHIFT);

        Assert.assertThrows(CommandException.class,
                String.format(Messages.MESSAGE_INVALID_ASSIGNMENT_UNAVAILABLE, workerName,
                        shift.getShiftDay(), shift.getShiftTime()), () ->
                        assignCommand.execute(model));
    }

    @Test
    public void equals() {
        Set<WorkerRolePair> validWorkerRole = new HashSet<>();
        validWorkerRole.add(new WorkerRolePair(INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CASHIER)));

        AssignCommand assignCommand1 = new AssignCommand(INDEX_FIRST_SHIFT, validWorkerRole);
        AssignCommand assignCommand2 = new AssignCommand(INDEX_SECOND_SHIFT, validWorkerRole);

        // same object -> returns true
        assertEquals(assignCommand1, assignCommand1);

        // same values -> returns true
        AssignCommand assignCommand1Copy = new AssignCommand(INDEX_FIRST_SHIFT, validWorkerRole);
        assertEquals(assignCommand1, assignCommand1Copy);

        // different types -> returns false
        assertNotEquals(assignCommand1, 1);

        // null -> returns false
        assertNotEquals(assignCommand1, null);

        // different assignment -> returns false
        assertNotEquals(assignCommand1, assignCommand2);
    }

}
