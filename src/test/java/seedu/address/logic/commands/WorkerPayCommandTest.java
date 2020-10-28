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
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code WorkerDeleteCommand}.
 */
public class WorkerPayCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Worker selectedWorker = model.getFilteredWorkerList().get(INDEX_FIRST_WORKER.getZeroBased());
        WorkerPayCommand workerPayCommand = new WorkerPayCommand(INDEX_FIRST_WORKER);

        float calculatedPay = model.calculateWorkerPay(selectedWorker);
        String expectedMessage =
                String.format(WorkerPayCommand.MESSAGE_SHOW_PAY_SUCCESS, selectedWorker.getName(), calculatedPay);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        assertCommandSuccess(workerPayCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredWorkerList().size() + 1);
        WorkerPayCommand workerPayCommand = new WorkerPayCommand(outOfBoundIndex);

        assertCommandFailure(workerPayCommand, model, Messages.MESSAGE_INVALID_WORKER_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showWorkerAtIndex(model, INDEX_FIRST_WORKER);

        Worker selectedWorker = model.getFilteredWorkerList().get(INDEX_FIRST_WORKER.getZeroBased());
        WorkerPayCommand workerPayCommand = new WorkerPayCommand(INDEX_FIRST_WORKER);

        float calculatedPay = model.calculateWorkerPay(selectedWorker);
        String expectedMessage =
                String.format(WorkerPayCommand.MESSAGE_SHOW_PAY_SUCCESS, selectedWorker.getName(), calculatedPay);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        showWorkerAtIndex(expectedModel, INDEX_FIRST_WORKER);

        assertCommandSuccess(workerPayCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showWorkerAtIndex(model, INDEX_FIRST_WORKER);

        Index outOfBoundIndex = INDEX_SECOND_WORKER;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getWorkerList().size());

        WorkerPayCommand workerPayCommand = new WorkerPayCommand(outOfBoundIndex);

        assertCommandFailure(workerPayCommand, model, Messages.MESSAGE_INVALID_WORKER_DISPLAYED_INDEX);
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
