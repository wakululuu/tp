package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SHIFT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_WORKER;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_SHIFT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_WORKER;
import static seedu.address.testutil.TypicalShifts.SHIFT_A;
import static seedu.address.testutil.TypicalWorkers.ALICE;
import static seedu.address.testutil.TypicalWorkers.BENSON;
import static seedu.address.testutil.TypicalWorkers.DANIEL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ModelManager;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.shift.Shift;
import seedu.address.model.tag.Leave;
import seedu.address.model.worker.Worker;

public class TakeLeaveCommandTest {

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        Set<Index> validIndex = new HashSet<>();
        validIndex.add(INDEX_FIRST_WORKER);
        assertThrows(NullPointerException.class, () -> new TakeLeaveCommand(null, validIndex));
        assertThrows(NullPointerException.class, () -> new TakeLeaveCommand(INDEX_FIRST_SHIFT, null));
        assertThrows(NullPointerException.class, () -> new TakeLeaveCommand(null, null));
    }

    @Test
    public void execute_correctIndexes_success() throws Exception {
        List<Worker> workers = Arrays.asList(ALICE);
        List<Shift> shifts = Arrays.asList(SHIFT_A);
        ModelStubAcceptingLeaveAdded model = new ModelStubAcceptingLeaveAdded(workers, shifts);

        Set<Index> validIndex = new HashSet<>();
        validIndex.add(INDEX_FIRST_WORKER);
        TakeLeaveCommand takeLeaveCommand = new TakeLeaveCommand(INDEX_FIRST_SHIFT, validIndex);
        CommandResult result;
        result = takeLeaveCommand.execute(model);

        Assignment assignment = new Assignment(SHIFT_A, ALICE, new Leave());

        assertEquals(String.format(TakeLeaveCommand.MESSAGE_TAKE_LEAVE_SUCCESS_PREFIX
                + AssignCommand.MESSAGE_ASSIGN_SUCCESS, 1, assignment) + "\n", result.getFeedbackToUser());
        assertEquals(Arrays.asList(assignment), model.assignments);

    }

    @Test
    public void execute_workerNotAvailable_throwsCommandException() {
        List<Worker> workers = Arrays.asList(BENSON);
        List<Shift> shifts = Arrays.asList(SHIFT_A);
        ModelStubAcceptingLeaveAdded model = new ModelStubAcceptingLeaveAdded(workers, shifts);

        Set<Index> validIndex = new HashSet<>();
        validIndex.add(INDEX_FIRST_WORKER);
        TakeLeaveCommand takeLeaveCommand = new TakeLeaveCommand(INDEX_FIRST_SHIFT, validIndex);
        assertThrows(CommandException.class, () -> takeLeaveCommand.execute(model));
    }

    @Test
    public void execute_workerAlreadyHasAssignment_throwsCommandException() {
        List<Worker> workers = Arrays.asList(DANIEL);
        List<Shift> shifts = Arrays.asList(SHIFT_A);
        ModelStubAlreadyHasAssignment model = new ModelStubAlreadyHasAssignment(workers, shifts);

        Set<Index> validIndex = new HashSet<>();
        validIndex.add(INDEX_FIRST_WORKER);
        TakeLeaveCommand takeLeaveCommand = new TakeLeaveCommand(INDEX_FIRST_SHIFT, validIndex);
        assertThrows(CommandException.class, () -> takeLeaveCommand.execute(model));
    }

    @Test
    public void execute_invalidWorkerIndex_throwsCommandException() {
        List<Worker> workers = Arrays.asList();
        List<Shift> shifts = Arrays.asList(SHIFT_A);
        ModelStubAcceptingLeaveAdded model = new ModelStubAcceptingLeaveAdded(workers, shifts);

        Set<Index> validIndex = new HashSet<>();
        validIndex.add(INDEX_FIRST_WORKER);
        TakeLeaveCommand takeLeaveCommand = new TakeLeaveCommand(INDEX_FIRST_SHIFT, validIndex);
        assertThrows(CommandException.class, () -> takeLeaveCommand.execute(model));
    }

    @Test
    public void execute_invalidShiftIndex_throwsCommandException() {
        List<Worker> workers = Arrays.asList(ALICE);
        List<Shift> shifts = Arrays.asList();
        ModelStubAcceptingLeaveAdded model = new ModelStubAcceptingLeaveAdded(workers, shifts);

        Set<Index> validIndex = new HashSet<>();
        validIndex.add(INDEX_FIRST_WORKER);
        assertThrows(CommandException.class, () ->
                new TakeLeaveCommand(INDEX_FIRST_SHIFT, validIndex).execute(model));
    }

    @Test
    public void equals() {
        Set<Index> validIndex = new HashSet<>();
        validIndex.add(INDEX_FIRST_WORKER);
        Set<Index> validIndexTwo = new HashSet<>();
        validIndexTwo.add(INDEX_SECOND_WORKER);
        TakeLeaveCommand firstIndexes = new TakeLeaveCommand(INDEX_FIRST_SHIFT, validIndex);
        TakeLeaveCommand secondIndexes = new TakeLeaveCommand(INDEX_SECOND_SHIFT, validIndexTwo);
        TakeLeaveCommand firstShiftSecondWorker = new TakeLeaveCommand(INDEX_FIRST_SHIFT, validIndexTwo);

        assertTrue(firstIndexes.equals(firstIndexes)); // same object
        assertTrue(firstIndexes.equals(new TakeLeaveCommand(INDEX_FIRST_SHIFT, validIndex))); // same values
        assertFalse(firstIndexes.equals(123)); // different type
        assertFalse(firstIndexes.equals(null)); // null
        assertFalse(firstIndexes.equals(secondIndexes)); // different values
        assertFalse(firstIndexes.equals(firstShiftSecondWorker)); // different worker
        assertFalse(secondIndexes.equals(firstShiftSecondWorker)); // different shift
    }

    /**
     * This stub replaces all methods used by {@code TakeLeaveCommand} for adding assignments.
     * Worker/Shift must still be present in the model.
     */
    private class ModelStubAcceptingLeaveAdded extends ModelManager {

        private final ArrayList<Assignment> assignments;

        public ModelStubAcceptingLeaveAdded(List<Worker> workers, List<Shift> shifts) {
            super();
            for (Worker worker: workers) {
                addWorker(worker);
            }
            for (Shift shift: shifts) {
                addShift(shift);
            }
            this.assignments = new ArrayList<>();
        }

        @Override
        public boolean hasAssignment(Assignment assignment) {
            return false;
        }

        @Override
        public void addAssignment(Assignment assignment) {
            this.assignments.add(assignment);
        }

    }

    /**
     * Similarly, this stub replaces all methods regarding assignment, but always returns true for
     * {@code hasAssignment()}.
     */
    private class ModelStubAlreadyHasAssignment extends ModelStubAcceptingLeaveAdded {

        public ModelStubAlreadyHasAssignment(List<Worker> workers, List<Shift> shifts) {
            super(workers, shifts);
        }

        @Override
        public boolean hasAssignment(Assignment assignment) {
            return true;
        }
    }

}
