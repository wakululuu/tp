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
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.assignment.WorkerRolePair;
import seedu.address.model.shift.Shift;
import seedu.address.model.tag.Role;
import seedu.address.model.worker.Worker;
import seedu.address.testutil.AssignmentBuilder;

public class UnassignCommandTest {

    @Test
    public void constructor_nullShiftIndex_throwsNullPointerException() {
        Set<Index> validWorker = new HashSet<>();
        validWorker.add(INDEX_FIRST_WORKER);
        assertThrows(NullPointerException.class, () ->
                new UnassignCommand(null, validWorker));
    }

    @Test
    public void constructor_nullWorkerIndex_throwsNullPointerException() {
        Set<Index> nullWorker = new HashSet<>();
        nullWorker.add(null);
        assertThrows(NullPointerException.class, () ->
                new UnassignCommand(INDEX_FIRST_SHIFT, nullWorker));
    }

    @Test
    public void constructor_nullWorkerIndexSet_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new UnassignCommand(INDEX_FIRST_SHIFT, null));
    }

    @Test
    public void execute_unassignmentAcceptedByModel_unassignSuccessful() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Set<WorkerRolePair> validWorkerRole = new HashSet<>();
        validWorkerRole.add(new WorkerRolePair(INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CASHIER)));
        AssignCommand validAssignCommand = new AssignCommand(INDEX_SECOND_SHIFT, validWorkerRole);
        validAssignCommand.execute(model);

        Set<Index> workerIndex = new HashSet<>();
        workerIndex.add(INDEX_FIRST_WORKER);
        UnassignCommand validUnassignCommand = new UnassignCommand(INDEX_SECOND_SHIFT, workerIndex);
        CommandResult commandResult = validUnassignCommand.execute(model);

        Shift shiftToUnassign = model.getFilteredShiftList().get(INDEX_SECOND_SHIFT.getZeroBased());
        Worker workerToUnassign = model.getFilteredWorkerList().get(INDEX_FIRST_WORKER.getZeroBased());
        Assignment validAssignment = new AssignmentBuilder().withShift(shiftToUnassign)
                .withWorker(workerToUnassign).buildDummy();

        assertEquals(String.format(UnassignCommand.MESSAGE_UNASSIGN_SUCCESS + "\n", 1, validAssignment),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(), model.getFullAssignmentList());
    }

    @Test
    public void execute_invalidShiftIndex_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredShiftList().size() + 1);

        Set<Index> workerIndex = new HashSet<>();
        workerIndex.add(INDEX_FIRST_WORKER);
        UnassignCommand unassignCommand = new UnassignCommand(outOfBoundIndex, workerIndex);

        assertCommandFailure(unassignCommand, model, Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidWorkerIndex_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredWorkerList().size() + 1);

        Set<Index> outOfBoundWorkerIndex = new HashSet<>();
        outOfBoundWorkerIndex.add(outOfBoundIndex);
        UnassignCommand unassignCommand = new UnassignCommand(INDEX_FIRST_SHIFT, outOfBoundWorkerIndex);

        assertCommandFailure(unassignCommand, model, Messages.MESSAGE_INVALID_WORKER_DISPLAYED_INDEX);
    }

    @Test
    public void execute_assignmentNotInModel_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Set<Index> workerIndex = new HashSet<>();
        workerIndex.add(INDEX_FIRST_WORKER);
        UnassignCommand unassignCommand = new UnassignCommand(INDEX_SECOND_SHIFT, workerIndex);

        String assignmentName = new Assignment(model.getFilteredShiftList().get(INDEX_SECOND_SHIFT.getZeroBased()),
                    model.getFilteredWorkerList().get(INDEX_FIRST_WORKER.getZeroBased()))
                    .toString();

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
