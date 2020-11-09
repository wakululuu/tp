package mcscheduler.logic.commands;

import static mcscheduler.logic.commands.CommandTestUtil.assertCommandFailure;
import static mcscheduler.logic.commands.CommandTestUtil.assertCommandSuccess;
import static mcscheduler.logic.commands.CommandTestUtil.showWorkerAtIndex;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import mcscheduler.commons.core.index.Index;
import mcscheduler.model.Model;
import mcscheduler.model.ModelManager;
import mcscheduler.model.UserPrefs;
import mcscheduler.model.worker.Worker;
import mcscheduler.testutil.McSchedulerBuilder;
import mcscheduler.testutil.TestUtil;
import mcscheduler.testutil.TypicalIndexes;

//@@author
/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code WorkerDeleteCommand}.
 */
public class WorkerDeleteCommandTest {

    private final Model model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Worker workerToDelete = TestUtil.getWorker(model, TypicalIndexes.INDEX_FIRST_WORKER);
        WorkerDeleteCommand workerDeleteCommand = new WorkerDeleteCommand(TypicalIndexes.INDEX_FIRST_WORKER);

        String expectedMessage = String.format(WorkerDeleteCommand.MESSAGE_DELETE_WORKER_SUCCESS, workerToDelete);

        ModelManager expectedModel = new ModelManager(model.getMcScheduler(), new UserPrefs());
        expectedModel.deleteWorker(workerToDelete);

        assertCommandSuccess(workerDeleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = TestUtil.getOutOfBoundWorkerIndex(model);
        WorkerDeleteCommand workerDeleteCommand = new WorkerDeleteCommand(outOfBoundIndex);

        assertCommandFailure(workerDeleteCommand, model,
                CommandUtil.printOutOfBoundsWorkerIndexError(outOfBoundIndex, WorkerDeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showWorkerAtIndex(model, TypicalIndexes.INDEX_FIRST_WORKER);

        Worker workerToDelete = TestUtil.getWorker(model, TypicalIndexes.INDEX_FIRST_WORKER);
        WorkerDeleteCommand workerDeleteCommand = new WorkerDeleteCommand(TypicalIndexes.INDEX_FIRST_WORKER);

        String expectedMessage = String.format(WorkerDeleteCommand.MESSAGE_DELETE_WORKER_SUCCESS, workerToDelete);

        Model expectedModel = new ModelManager(model.getMcScheduler(), new UserPrefs());
        expectedModel.deleteWorker(workerToDelete);
        showNoWorker(expectedModel);

        assertCommandSuccess(workerDeleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showWorkerAtIndex(model, TypicalIndexes.INDEX_FIRST_WORKER);

        Index outOfBoundIndex = TypicalIndexes.INDEX_SECOND_WORKER;
        // ensures that outOfBoundIndex is still in bounds of the McScheduler list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getMcScheduler().getWorkerList().size());

        WorkerDeleteCommand workerDeleteCommand = new WorkerDeleteCommand(outOfBoundIndex);

        assertCommandFailure(workerDeleteCommand, model,
                CommandUtil.printOutOfBoundsWorkerIndexError(outOfBoundIndex, WorkerDeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void equals() {
        WorkerDeleteCommand deleteFirstCommand = new WorkerDeleteCommand(TypicalIndexes.INDEX_FIRST_WORKER);
        WorkerDeleteCommand deleteSecondCommand = new WorkerDeleteCommand(TypicalIndexes.INDEX_SECOND_WORKER);

        // same object -> returns true
        assertEquals(deleteFirstCommand, deleteFirstCommand);

        // same values -> returns true
        WorkerDeleteCommand deleteFirstCommandCopy = new WorkerDeleteCommand(TypicalIndexes.INDEX_FIRST_WORKER);
        assertEquals(deleteFirstCommandCopy, deleteFirstCommand);

        // different types -> returns false
        assertNotEquals(deleteFirstCommand, 1);

        // null -> returns false
        assertNotEquals(deleteFirstCommand, null);

        // different worker -> returns false
        assertNotEquals(deleteSecondCommand, deleteFirstCommand);
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoWorker(Model model) {
        model.updateFilteredWorkerList(p -> false);

        assertTrue(model.getFilteredWorkerList().isEmpty());
    }
}
