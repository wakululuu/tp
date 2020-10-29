package mcscheduler.logic.commands;

import static mcscheduler.logic.commands.CommandTestUtil.VALID_ROLE_CASHIER;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_ROLE_CHEF;
import static mcscheduler.logic.commands.CommandTestUtil.assertCommandFailure;
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
import mcscheduler.model.shift.Shift;
import mcscheduler.model.tag.Role;
import mcscheduler.model.worker.Worker;
import mcscheduler.testutil.Assert;
import mcscheduler.testutil.AssignmentBuilder;
import mcscheduler.testutil.McSchedulerBuilder;
import mcscheduler.testutil.TypicalIndexes;

public class AssignCommandTest {

    @Test
    public void constructor_nullShiftIndex_throwsNullPointerException() {
        Set<WorkerRolePair> validWorkerRole = new HashSet<>();
        validWorkerRole.add(new WorkerRolePair(TypicalIndexes.INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CASHIER)));
        Assert.assertThrows(NullPointerException.class, () -> new AssignCommand(null, validWorkerRole));
    }

    @Test
    public void execute_assignmentAcceptedByModel_assignSuccessful() throws Exception {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());

        Set<WorkerRolePair> validWorkerRole = new HashSet<>();
        validWorkerRole.add(new WorkerRolePair(TypicalIndexes.INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CASHIER)));
        AssignCommand validAssignCommand = new AssignCommand(TypicalIndexes.INDEX_SECOND_SHIFT, validWorkerRole);
        CommandResult commandResult = validAssignCommand.execute(model);

        Shift shiftToAssign = model.getFilteredShiftList().get(TypicalIndexes.INDEX_SECOND_SHIFT.getZeroBased());
        Worker workerToAssign = model.getFilteredWorkerList().get(TypicalIndexes.INDEX_FIRST_WORKER.getZeroBased());
        Assignment validAssignment = new AssignmentBuilder().withShift(shiftToAssign)
            .withWorker(workerToAssign)
            .withRole(VALID_ROLE_CASHIER).build();

        assertEquals(String.format(AssignCommand.MESSAGE_ASSIGN_SUCCESS, 1, validAssignment + "\n"),
            commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validAssignment), model.getFullAssignmentList());
    }

    @Test
    public void execute_invalidShiftIndex_throwsCommandException() {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredShiftList().size() + 1);

        Set<WorkerRolePair> validWorkerRole = new HashSet<>();
        validWorkerRole.add(new WorkerRolePair(TypicalIndexes.INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CHEF)));
        AssignCommand assignCommand = new AssignCommand(outOfBoundIndex, validWorkerRole);

        assertCommandFailure(assignCommand, model, Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidWorkerIndex_throwsCommandException() {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredWorkerList().size() + 1);

        Set<WorkerRolePair> invalidWorkerIndex = new HashSet<>();
        invalidWorkerIndex.add(new WorkerRolePair(outOfBoundIndex, Role.createRole(VALID_ROLE_CASHIER)));
        AssignCommand assignCommand = new AssignCommand(TypicalIndexes.INDEX_FIRST_SHIFT, invalidWorkerIndex);

        assertCommandFailure(assignCommand, model,
            String.format(Messages.MESSAGE_INVALID_WORKER_DISPLAYED_INDEX, outOfBoundIndex.getOneBased()));
    }

    @Test
    public void execute_duplicateAssignment_throwsCommandException() throws Exception {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());

        Set<WorkerRolePair> validWorkerRole = new HashSet<>();
        validWorkerRole.add(new WorkerRolePair(TypicalIndexes.INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CASHIER)));
        AssignCommand assignCommand = new AssignCommand(TypicalIndexes.INDEX_SECOND_SHIFT, validWorkerRole);
        assignCommand.execute(model);

        Assignment assignmentName =
            new Assignment(model.getFilteredShiftList().get(TypicalIndexes.INDEX_SECOND_SHIFT.getZeroBased()),
                model.getFilteredWorkerList().get(TypicalIndexes.INDEX_FIRST_WORKER.getZeroBased()),
                Role.createRole(VALID_ROLE_CASHIER));

        Assert.assertThrows(CommandException.class,
            String.format(AssignCommand.MESSAGE_DUPLICATE_ASSIGNMENT, assignmentName), () ->
                assignCommand.execute(model));
    }

    @Test
    public void execute_workerNotFitForRole_throwsCommandException() {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());

        Set<WorkerRolePair> notFitWorkerRole = new HashSet<>();
        notFitWorkerRole.add(new WorkerRolePair(TypicalIndexes.INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CHEF)));
        AssignCommand assignCommand = new AssignCommand(TypicalIndexes.INDEX_THIRD_SHIFT, notFitWorkerRole);

        String workerName =
            model.getFilteredWorkerList().get(TypicalIndexes.INDEX_FIRST_WORKER.getZeroBased()).getName().toString();

        Assert.assertThrows(CommandException.class,
            String.format(Messages.MESSAGE_INVALID_ASSIGNMENT_WORKER_ROLE, workerName, VALID_ROLE_CHEF), () ->
                assignCommand.execute(model));
    }

    @Test
    public void execute_workerUnavailable_throwsCommandException() {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());

        Set<WorkerRolePair> validWorkerRole = new HashSet<>();
        validWorkerRole.add(new WorkerRolePair(TypicalIndexes.INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CASHIER)));
        AssignCommand assignCommand = new AssignCommand(TypicalIndexes.INDEX_THIRD_SHIFT, validWorkerRole);

        String workerName =
            model.getFilteredWorkerList().get(TypicalIndexes.INDEX_FIRST_WORKER.getZeroBased()).getName().toString();
        String shiftName = model.getFilteredShiftList().get(TypicalIndexes.INDEX_THIRD_SHIFT.getZeroBased()).toString();

        Assert.assertThrows(CommandException.class,
            String.format(Messages.MESSAGE_INVALID_ASSIGNMENT_UNAVAILABLE, workerName, shiftName), () ->
                assignCommand.execute(model));
    }

    @Test
    public void equals() {
        Set<WorkerRolePair> validWorkerRole = new HashSet<>();
        validWorkerRole.add(new WorkerRolePair(TypicalIndexes.INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CASHIER)));

        AssignCommand assignCommand1 = new AssignCommand(TypicalIndexes.INDEX_FIRST_SHIFT, validWorkerRole);
        AssignCommand assignCommand2 = new AssignCommand(TypicalIndexes.INDEX_SECOND_SHIFT, validWorkerRole);

        // same object -> returns true
        assertEquals(assignCommand1, assignCommand1);

        // same values -> returns true
        AssignCommand assignCommand1Copy = new AssignCommand(TypicalIndexes.INDEX_FIRST_SHIFT, validWorkerRole);
        assertEquals(assignCommand1, assignCommand1Copy);

        // different types -> returns false
        assertNotEquals(assignCommand1, 1);

        // null -> returns false
        assertNotEquals(assignCommand1, null);

        // different assignment -> returns false
        assertNotEquals(assignCommand1, assignCommand2);
    }

}
