package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SHIFT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_WORKER;
import static seedu.address.testutil.TypicalShifts.SHIFT_A;
import static seedu.address.testutil.TypicalWorkers.ALICE;
import static seedu.address.testutil.TypicalWorkers.DANIEL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ModelManager;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.shift.Shift;
import seedu.address.model.tag.Leave;
import seedu.address.model.worker.Worker;

public class TakeLeaveCommandTest {

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TakeLeaveCommand(null, INDEX_FIRST_WORKER));
        assertThrows(NullPointerException.class, () -> new TakeLeaveCommand(INDEX_FIRST_SHIFT, null));
        assertThrows(NullPointerException.class, () -> new TakeLeaveCommand(null, null));
    }

    @Test
    public void execute_correctIndexes_success() throws Exception {
        List<Worker> workers = Arrays.asList(DANIEL);
        List<Shift> shifts = Arrays.asList(SHIFT_A);
        ModelStubAcceptingLeaveAdded model = new ModelStubAcceptingLeaveAdded(workers, shifts);

        TakeLeaveCommand takeLeaveCommand = new TakeLeaveCommand(INDEX_FIRST_SHIFT, INDEX_FIRST_WORKER);
        CommandResult result;
        result = takeLeaveCommand.execute(model);

        Assignment assignment = new Assignment(SHIFT_A, DANIEL, new Leave());

        assertEquals(String.format(TakeLeaveCommand.MESSAGE_TAKE_LEAVE_SUCCESS_PREFIX
                + AssignCommand.MESSAGE_ASSIGN_SUCCESS, assignment), result.getFeedbackToUser());
        assertEquals(Arrays.asList(assignment), model.assignments);

    }

    @Test
    public void execute_workerNotAvailable_throwsCommandException() {
        List<Worker> workers = Arrays.asList(ALICE);
        List<Shift> shifts = Arrays.asList(SHIFT_A);
        ModelStubAcceptingLeaveAdded model = new ModelStubAcceptingLeaveAdded(workers, shifts);

        TakeLeaveCommand takeLeaveCommand = new TakeLeaveCommand(INDEX_FIRST_SHIFT, INDEX_FIRST_WORKER);
        assertThrows(CommandException.class, () -> takeLeaveCommand.execute(model));
    }

    @Test
    public void execute_workerNotInModel_throwsCommandException() {
        List<Worker> workers = Arrays.asList();
        List<Shift> shifts = Arrays.asList(SHIFT_A);
        ModelStubAcceptingLeaveAdded model = new ModelStubAcceptingLeaveAdded(workers, shifts);

        TakeLeaveCommand takeLeaveCommand = new TakeLeaveCommand(INDEX_FIRST_SHIFT, INDEX_FIRST_WORKER);
        assertThrows(CommandException.class, () -> takeLeaveCommand.execute(model));
    }

    @Test
    public void execute_shiftNotInModel_throwsCommandException() {
        List<Worker> workers = Arrays.asList(ALICE);
        List<Shift> shifts = Arrays.asList();
        ModelStubAcceptingLeaveAdded model = new ModelStubAcceptingLeaveAdded(workers, shifts);

        assertThrows(CommandException.class,
                () -> new TakeLeaveCommand(INDEX_FIRST_SHIFT, INDEX_FIRST_WORKER).execute(model));
    }

    /**
     * This stub replaces all methods used by {@code TakeLeaveCommand} for adding assignments.
     * Worker/Shift must still be present in the model.
     */
    private class ModelStubAcceptingLeaveAdded extends ModelManager {

        private final ArrayList<Assignment> assignments;

        public ModelStubAcceptingLeaveAdded(List<Worker> workers, List<Shift> shifts)  {
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

}
