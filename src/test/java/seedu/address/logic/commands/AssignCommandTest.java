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
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_WORKER;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_SHIFT;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_WORKER;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.assignment.WorkerRolePair;
import seedu.address.model.assignment.exceptions.DuplicateAssignmentException;
import seedu.address.model.shift.Shift;
import seedu.address.model.tag.Role;
import seedu.address.model.worker.Worker;
import seedu.address.testutil.AssignmentBuilder;

public class AssignCommandTest {

    @Test
    public void constructor_nullShiftIndex_throwsNullPointerException() {
        Set<WorkerRolePair> validWorkerRole = new HashSet<>();
        validWorkerRole.add(new WorkerRolePair(INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CASHIER)));
        assertThrows(NullPointerException.class, () -> new AssignCommand(null, validWorkerRole));
    }

    @Test
    public void execute_assignmentAcceptedByModel_assignSuccessful() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Set<WorkerRolePair> validWorkerRole = new HashSet<>();
        validWorkerRole.add(new WorkerRolePair(INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CASHIER)));
        AssignCommand validAssignCommand = new AssignCommand(INDEX_SECOND_SHIFT, validWorkerRole);
        CommandResult commandResult = validAssignCommand.execute(model);

        Shift shiftToAssign = model.getFilteredShiftList().get(INDEX_SECOND_SHIFT.getZeroBased());
        Worker workerToAssign = model.getFilteredWorkerList().get(INDEX_FIRST_WORKER.getZeroBased());
        Assignment validAssignment = new AssignmentBuilder().withShift(shiftToAssign)
                .withWorker(workerToAssign)
                .withRole(VALID_ROLE_CASHIER).build();

        assertEquals(String.format(AssignCommand.MESSAGE_ASSIGN_SUCCESS, 1, validAssignment + "\n"),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validAssignment), model.getFullAssignmentList());
    }

    @Test
    public void execute_invalidShiftIndex_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredShiftList().size() + 1);

        Set<WorkerRolePair> validWorkerRole = new HashSet<>();
        validWorkerRole.add(new WorkerRolePair(INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CHEF)));
        AssignCommand assignCommand = new AssignCommand(outOfBoundIndex, validWorkerRole);

        assertCommandFailure(assignCommand, model, Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidWorkerIndex_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredWorkerList().size() + 1);

        Set<WorkerRolePair> invalidWorkerIndex = new HashSet<>();
        invalidWorkerIndex.add(new WorkerRolePair(outOfBoundIndex, Role.createRole(VALID_ROLE_CASHIER)));
        AssignCommand assignCommand = new AssignCommand(INDEX_FIRST_SHIFT, invalidWorkerIndex);

        assertCommandFailure(assignCommand, model,
                    String.format(Messages.MESSAGE_INVALID_WORKER_DISPLAYED_INDEX, outOfBoundIndex.getOneBased()));
    }

    @Test
    public void execute_duplicateAssignment_throwsCommandException() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Set<WorkerRolePair> validWorkerRole = new HashSet<>();
        validWorkerRole.add(new WorkerRolePair(INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CASHIER)));
        AssignCommand assignCommand = new AssignCommand(INDEX_SECOND_SHIFT, validWorkerRole);
        assignCommand.execute(model);

        assertThrows(CommandException.class, AssignCommand.MESSAGE_DUPLICATE_ASSIGNMENT, () ->
                assignCommand.execute(model));
    }

    @Test
    public void execute_workerNotFitForRole_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Set<WorkerRolePair> notFitWorkerRole = new HashSet<>();
        notFitWorkerRole.add(new WorkerRolePair(INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CHEF)));
        AssignCommand assignCommand = new AssignCommand(INDEX_THIRD_SHIFT, notFitWorkerRole);

        String workerName = model.getFilteredWorkerList().get(INDEX_FIRST_WORKER.getZeroBased()).getName().toString();

        assertThrows(CommandException.class,
                String.format(Messages.MESSAGE_INVALID_ASSIGNMENT_WORKER_ROLE, workerName, VALID_ROLE_CHEF), () ->
                assignCommand.execute(model));
    }

    @Test
    public void execute_workerUnavailable_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Set<WorkerRolePair> validWorkerRole = new HashSet<>();
        validWorkerRole.add(new WorkerRolePair(INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CASHIER)));
        AssignCommand assignCommand = new AssignCommand(INDEX_THIRD_SHIFT, validWorkerRole);

        String workerName = model.getFilteredWorkerList().get(INDEX_FIRST_WORKER.getZeroBased()).getName().toString();
        String shiftName = model.getFilteredShiftList().get(INDEX_THIRD_SHIFT.getZeroBased()).toString();

        assertThrows(CommandException.class,
                String.format(Messages.MESSAGE_INVALID_ASSIGNMENT_UNAVAILABLE, workerName, shiftName), () ->
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
