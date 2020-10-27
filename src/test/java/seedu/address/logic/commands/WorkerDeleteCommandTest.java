package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showWorkerAtIndex;
import static seedu.address.testutil.AddressBookBuilder.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_WORKER;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_WORKER;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.worker.Worker;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code WorkerDeleteCommand}.
 */
public class WorkerDeleteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Worker workerToDelete = model.getFilteredWorkerList().get(INDEX_FIRST_WORKER.getZeroBased());
        WorkerDeleteCommand workerDeleteCommand = new WorkerDeleteCommand(INDEX_FIRST_WORKER);

        String expectedMessage = String.format(WorkerDeleteCommand.MESSAGE_DELETE_WORKER_SUCCESS, workerToDelete);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteWorker(workerToDelete);

        assertCommandSuccess(workerDeleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredWorkerList().size() + 1);
        WorkerDeleteCommand workerDeleteCommand = new WorkerDeleteCommand(outOfBoundIndex);

        assertCommandFailure(workerDeleteCommand, model, Messages.MESSAGE_INVALID_WORKER_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showWorkerAtIndex(model, INDEX_FIRST_WORKER);

        Worker workerToDelete = model.getFilteredWorkerList().get(INDEX_FIRST_WORKER.getZeroBased());
        WorkerDeleteCommand workerDeleteCommand = new WorkerDeleteCommand(INDEX_FIRST_WORKER);

        String expectedMessage = String.format(WorkerDeleteCommand.MESSAGE_DELETE_WORKER_SUCCESS, workerToDelete);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteWorker(workerToDelete);
        showNoWorker(expectedModel);

        assertCommandSuccess(workerDeleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showWorkerAtIndex(model, INDEX_FIRST_WORKER);

        Index outOfBoundIndex = INDEX_SECOND_WORKER;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getWorkerList().size());

        WorkerDeleteCommand workerDeleteCommand = new WorkerDeleteCommand(outOfBoundIndex);

        assertCommandFailure(workerDeleteCommand, model, Messages.MESSAGE_INVALID_WORKER_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        WorkerDeleteCommand deleteFirstCommand = new WorkerDeleteCommand(INDEX_FIRST_WORKER);
        WorkerDeleteCommand deleteSecondCommand = new WorkerDeleteCommand(INDEX_SECOND_WORKER);

        // same object -> returns true
        assertEquals(deleteFirstCommand, deleteFirstCommand);

        // same values -> returns true
        WorkerDeleteCommand deleteFirstCommandCopy = new WorkerDeleteCommand(INDEX_FIRST_WORKER);
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
