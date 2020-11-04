package mcscheduler.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import mcscheduler.commons.core.index.Index;
import mcscheduler.logic.commands.exceptions.CommandException;
import mcscheduler.model.ModelManager;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.role.Leave;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.worker.Worker;
import mcscheduler.testutil.Assert;
import mcscheduler.testutil.TypicalIndexes;
import mcscheduler.testutil.TypicalShifts;
import mcscheduler.testutil.TypicalWorkers;

public class TakeLeaveCommandTest {

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        Set<Index> validIndex = new HashSet<>();
        validIndex.add(TypicalIndexes.INDEX_FIRST_WORKER);
        Assert.assertThrows(NullPointerException.class, () -> new TakeLeaveCommand(null, validIndex));
        Assert
            .assertThrows(NullPointerException.class, () ->
                new TakeLeaveCommand(TypicalIndexes.INDEX_FIRST_SHIFT, null));
        Assert.assertThrows(NullPointerException.class, () -> new TakeLeaveCommand(null, null));
    }

    @Test
    public void execute_correctIndexes_success() throws Exception {
        List<Worker> workers = Arrays.asList(TypicalWorkers.ALICE);
        List<Shift> shifts = Arrays.asList(TypicalShifts.SHIFT_A);
        ModelStubAcceptingLeaveAdded model = new ModelStubAcceptingLeaveAdded(workers, shifts);

        Set<Index> validIndex = new HashSet<>();
        validIndex.add(TypicalIndexes.INDEX_FIRST_WORKER);
        TakeLeaveCommand takeLeaveCommand = new TakeLeaveCommand(TypicalIndexes.INDEX_FIRST_SHIFT, validIndex);
        CommandResult result;
        result = takeLeaveCommand.execute(model);

        Assignment assignment = new Assignment(TypicalShifts.SHIFT_A, TypicalWorkers.ALICE, new Leave());

        assertEquals(String.format(TakeLeaveCommand.MESSAGE_TAKE_LEAVE_SUCCESS_PREFIX
            + AssignCommand.MESSAGE_ASSIGN_SUCCESS, 1, assignment) + "\n", result.getFeedbackToUser());
        assertEquals(Arrays.asList(assignment), model.assignments);

    }

    @Test
    public void execute_workerNotAvailable_throwsCommandException() {
        List<Worker> workers = Arrays.asList(TypicalWorkers.BENSON);
        List<Shift> shifts = Arrays.asList(TypicalShifts.SHIFT_A);
        ModelStubAcceptingLeaveAdded model = new ModelStubAcceptingLeaveAdded(workers, shifts);

        Set<Index> validIndex = new HashSet<>();
        validIndex.add(TypicalIndexes.INDEX_FIRST_WORKER);
        TakeLeaveCommand takeLeaveCommand = new TakeLeaveCommand(TypicalIndexes.INDEX_FIRST_SHIFT, validIndex);
        Assert.assertThrows(CommandException.class, () -> takeLeaveCommand.execute(model));
    }

    @Test
    public void execute_workerAlreadyHasAssignment_throwsCommandException() {
        List<Worker> workers = Arrays.asList(TypicalWorkers.DANIEL);
        List<Shift> shifts = Arrays.asList(TypicalShifts.SHIFT_A);
        ModelStubAlreadyHasAssignment model = new ModelStubAlreadyHasAssignment(workers, shifts);

        Set<Index> validIndex = new HashSet<>();
        validIndex.add(TypicalIndexes.INDEX_FIRST_WORKER);
        TakeLeaveCommand takeLeaveCommand = new TakeLeaveCommand(TypicalIndexes.INDEX_FIRST_SHIFT, validIndex);
        Assert.assertThrows(CommandException.class, () -> takeLeaveCommand.execute(model));
    }

    @Test
    public void execute_invalidWorkerIndex_throwsCommandException() {
        List<Worker> workers = Arrays.asList();
        List<Shift> shifts = Arrays.asList(TypicalShifts.SHIFT_A);
        ModelStubAcceptingLeaveAdded model = new ModelStubAcceptingLeaveAdded(workers, shifts);

        Set<Index> validIndex = new HashSet<>();
        validIndex.add(TypicalIndexes.INDEX_FIRST_WORKER);
        TakeLeaveCommand takeLeaveCommand = new TakeLeaveCommand(TypicalIndexes.INDEX_FIRST_SHIFT, validIndex);
        Assert.assertThrows(CommandException.class, () -> takeLeaveCommand.execute(model));
    }

    @Test
    public void execute_invalidShiftIndex_throwsCommandException() {
        List<Worker> workers = Arrays.asList(TypicalWorkers.ALICE);
        List<Shift> shifts = Arrays.asList();
        ModelStubAcceptingLeaveAdded model = new ModelStubAcceptingLeaveAdded(workers, shifts);

        Set<Index> validIndex = new HashSet<>();
        validIndex.add(TypicalIndexes.INDEX_FIRST_WORKER);
        Assert.assertThrows(CommandException.class, () ->
            new TakeLeaveCommand(TypicalIndexes.INDEX_FIRST_SHIFT, validIndex).execute(model));
    }

    @Test
    public void equals() {
        Set<Index> validIndex = new HashSet<>();
        validIndex.add(TypicalIndexes.INDEX_FIRST_WORKER);
        Set<Index> validIndexTwo = new HashSet<>();
        validIndexTwo.add(TypicalIndexes.INDEX_SECOND_WORKER);
        TakeLeaveCommand firstIndexes = new TakeLeaveCommand(TypicalIndexes.INDEX_FIRST_SHIFT, validIndex);
        TakeLeaveCommand secondIndexes = new TakeLeaveCommand(TypicalIndexes.INDEX_SECOND_SHIFT, validIndexTwo);
        TakeLeaveCommand firstShiftSecondWorker = new TakeLeaveCommand(TypicalIndexes.INDEX_FIRST_SHIFT, validIndexTwo);

        assertTrue(firstIndexes.equals(firstIndexes)); // same object
        assertTrue(
            firstIndexes.equals(new TakeLeaveCommand(TypicalIndexes.INDEX_FIRST_SHIFT, validIndex))); // same values
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
            for (Worker worker : workers) {
                addWorker(worker);
            }
            for (Shift shift : shifts) {
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
     * {@code hasAssignment()} and returns a mock assignment for {@code getAssignment}
     */
    private class ModelStubAlreadyHasAssignment extends ModelStubAcceptingLeaveAdded {

        public ModelStubAlreadyHasAssignment(List<Worker> workers, List<Shift> shifts) {
            super(workers, shifts);
        }

        @Override
        public boolean hasAssignment(Assignment assignment) {
            return true;
        }

        @Override
        public Optional<Assignment> getAssignment(Assignment toGet) {
            return Optional.of(toGet);
        }
    }

}
