package mcscheduler.logic.commands;

import static mcscheduler.logic.commands.CommandTestUtil.assertCommandFailure;
import static mcscheduler.logic.commands.CommandTestUtil.assertCommandSuccess;
import static mcscheduler.logic.commands.CommandTestUtil.showWorkerAtIndex;
import static mcscheduler.testutil.TypicalIndexes.INDEX_FIRST_WORKER;
import static mcscheduler.testutil.TypicalIndexes.INDEX_SECOND_WORKER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import mcscheduler.commons.core.index.Index;
import mcscheduler.model.Model;
import mcscheduler.model.ModelManager;
import mcscheduler.model.UserPrefs;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.worker.Worker;
import mcscheduler.testutil.McSchedulerBuilder;
import mcscheduler.testutil.TestUtil;


//@@author plosslaw
/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code WorkerDeleteCommand}.
 */
public class WorkerPayCommandTest {

    private final Model model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Worker selectedWorker = TestUtil.getWorker(model, INDEX_FIRST_WORKER);
        WorkerPayCommand workerPayCommand = new WorkerPayCommand(INDEX_FIRST_WORKER);

        int shiftsAssigned = model.calculateWorkerShiftsAssigned(selectedWorker);
        float calculatedPay = shiftsAssigned * Shift.HOURS_PER_SHIFT * selectedWorker.getPay().getValue();
        String expectedMessage =
            String.format(WorkerPayCommand.MESSAGE_SHOW_PAY_SUCCESS, selectedWorker.getName(),
                    selectedWorker.getPay().getValue(), Shift.HOURS_PER_SHIFT, shiftsAssigned, calculatedPay);

        ModelManager expectedModel = new ModelManager(model.getMcScheduler(), new UserPrefs());

        assertCommandSuccess(workerPayCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = TestUtil.getOutOfBoundWorkerIndex(model);
        WorkerPayCommand workerPayCommand = new WorkerPayCommand(outOfBoundIndex);

        assertCommandFailure(workerPayCommand, model,
                CommandUtil.printOutOfBoundsWorkerIndexError(outOfBoundIndex, WorkerPayCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showWorkerAtIndex(model, INDEX_FIRST_WORKER);

        Worker selectedWorker = TestUtil.getWorker(model, INDEX_FIRST_WORKER);
        WorkerPayCommand workerPayCommand = new WorkerPayCommand(INDEX_FIRST_WORKER);

        int shiftsAssigned = model.calculateWorkerShiftsAssigned(selectedWorker);
        float calculatedPay = shiftsAssigned * Shift.HOURS_PER_SHIFT * selectedWorker.getPay().getValue();
        String expectedMessage =
                String.format(WorkerPayCommand.MESSAGE_SHOW_PAY_SUCCESS, selectedWorker.getName(),
                        selectedWorker.getPay().getValue(), Shift.HOURS_PER_SHIFT, shiftsAssigned, calculatedPay);

        Model expectedModel = new ModelManager(model.getMcScheduler(), new UserPrefs());
        showWorkerAtIndex(expectedModel, INDEX_FIRST_WORKER);

        assertCommandSuccess(workerPayCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showWorkerAtIndex(model, INDEX_FIRST_WORKER);

        Index outOfBoundIndex = INDEX_SECOND_WORKER;
        // ensures that outOfBoundIndex is still in bounds of the McScheduler list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getMcScheduler().getWorkerList().size());

        WorkerPayCommand workerPayCommand = new WorkerPayCommand(outOfBoundIndex);

        assertCommandFailure(workerPayCommand, model,
                CommandUtil.printOutOfBoundsWorkerIndexError(outOfBoundIndex, WorkerPayCommand.MESSAGE_USAGE));
    }

    @Test
    public void equals() {
        WorkerPayCommand workerPayFirstCommand = new WorkerPayCommand(INDEX_FIRST_WORKER);
        WorkerPayCommand workerPaySecondCommand = new WorkerPayCommand(INDEX_SECOND_WORKER);

        // same object -> returns true
        assertEquals(workerPayFirstCommand, workerPayFirstCommand);

        // same values -> returns true
        WorkerPayCommand workerPayFirstCommandCopy = new WorkerPayCommand(INDEX_FIRST_WORKER);
        assertEquals(workerPayFirstCommandCopy, workerPayFirstCommand);

        // different types -> returns false
        assertNotEquals(workerPayFirstCommand, 1);

        // null -> returns false
        assertNotEquals(workerPayFirstCommand, null);

        // different worker -> returns false
        assertNotEquals(workerPaySecondCommand, workerPayFirstCommand);
    }
}
