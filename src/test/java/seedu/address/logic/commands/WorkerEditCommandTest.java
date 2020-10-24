package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_CASHIER;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showWorkerAtIndex;
import static seedu.address.testutil.AddressBookBuilder.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_WORKER;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_WORKER;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.WorkerEditCommand.EditWorkerDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.worker.Worker;
import seedu.address.testutil.EditWorkerDescriptorBuilder;
import seedu.address.testutil.WorkerBuilder;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand)
 * and unit tests for WorkerEditCommand.
 */
public class WorkerEditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Worker editedWorker = new WorkerBuilder().build();
        EditWorkerDescriptor descriptor = new EditWorkerDescriptorBuilder(editedWorker).build();
        WorkerEditCommand workerEditCommand = new WorkerEditCommand(INDEX_FIRST_WORKER, descriptor);

        String expectedMessage = String.format(WorkerEditCommand.MESSAGE_EDIT_WORKER_SUCCESS, editedWorker);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setWorker(model.getFilteredWorkerList().get(0), editedWorker);

        assertCommandSuccess(workerEditCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastWorker = Index.fromOneBased(model.getFilteredWorkerList().size());
        Worker lastWorker = model.getFilteredWorkerList().get(indexLastWorker.getZeroBased());

        WorkerBuilder workerInList = new WorkerBuilder(lastWorker);
        Worker editedWorker = workerInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withRoles(VALID_ROLE_CASHIER).build();

        EditWorkerDescriptor descriptor = new EditWorkerDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withRoles(VALID_ROLE_CASHIER).build();
        WorkerEditCommand workerEditCommand = new WorkerEditCommand(indexLastWorker, descriptor);

        String expectedMessage = String.format(WorkerEditCommand.MESSAGE_EDIT_WORKER_SUCCESS, editedWorker);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setWorker(lastWorker, editedWorker);

        assertCommandSuccess(workerEditCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        WorkerEditCommand workerEditCommand = new WorkerEditCommand(INDEX_FIRST_WORKER, new EditWorkerDescriptor());
        Worker editedWorker = model.getFilteredWorkerList().get(INDEX_FIRST_WORKER.getZeroBased());

        String expectedMessage = String.format(WorkerEditCommand.MESSAGE_EDIT_WORKER_SUCCESS, editedWorker);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(workerEditCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showWorkerAtIndex(model, INDEX_FIRST_WORKER);

        Worker workerInFilteredList = model.getFilteredWorkerList().get(INDEX_FIRST_WORKER.getZeroBased());
        Worker editedWorker = new WorkerBuilder(workerInFilteredList).withName(VALID_NAME_BOB).build();
        WorkerEditCommand workerEditCommand = new WorkerEditCommand(INDEX_FIRST_WORKER,
                new EditWorkerDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(WorkerEditCommand.MESSAGE_EDIT_WORKER_SUCCESS, editedWorker);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setWorker(model.getFilteredWorkerList().get(0), editedWorker);

        assertCommandSuccess(workerEditCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateWorkerUnfilteredList_failure() {
        Worker firstWorker = model.getFilteredWorkerList().get(INDEX_FIRST_WORKER.getZeroBased());
        EditWorkerDescriptor descriptor = new EditWorkerDescriptorBuilder(firstWorker).build();
        WorkerEditCommand workerEditCommand = new WorkerEditCommand(INDEX_SECOND_WORKER, descriptor);

        assertCommandFailure(workerEditCommand, model, WorkerEditCommand.MESSAGE_DUPLICATE_WORKER);
    }

    @Test
    public void execute_duplicateWorkerFilteredList_failure() {
        showWorkerAtIndex(model, INDEX_FIRST_WORKER);

        // edit worker in filtered list into a duplicate in address book
        Worker workerInList = model.getAddressBook().getWorkerList().get(INDEX_SECOND_WORKER.getZeroBased());
        WorkerEditCommand workerEditCommand = new WorkerEditCommand(INDEX_FIRST_WORKER,
                new EditWorkerDescriptorBuilder(workerInList).build());

        assertCommandFailure(workerEditCommand, model, WorkerEditCommand.MESSAGE_DUPLICATE_WORKER);
    }

    @Test
    public void execute_invalidWorkerIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredWorkerList().size() + 1);
        EditWorkerDescriptor descriptor = new EditWorkerDescriptorBuilder().withName(VALID_NAME_BOB).build();
        WorkerEditCommand workerEditCommand = new WorkerEditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(workerEditCommand, model, Messages.MESSAGE_INVALID_WORKER_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidWorkerIndexFilteredList_failure() {
        showWorkerAtIndex(model, INDEX_FIRST_WORKER);
        Index outOfBoundIndex = INDEX_SECOND_WORKER;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getWorkerList().size());

        WorkerEditCommand workerEditCommand = new WorkerEditCommand(outOfBoundIndex,
                new EditWorkerDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(workerEditCommand, model, Messages.MESSAGE_INVALID_WORKER_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final WorkerEditCommand standardCommand = new WorkerEditCommand(INDEX_FIRST_WORKER, DESC_AMY);

        // same values -> returns true
        EditWorkerDescriptor copyDescriptor = new EditWorkerDescriptor(DESC_AMY);
        WorkerEditCommand commandWithSameValues = new WorkerEditCommand(INDEX_FIRST_WORKER, copyDescriptor);
        assertEquals(commandWithSameValues, standardCommand);

        // same object -> returns true
        assertEquals(standardCommand, standardCommand);

        // null -> returns false
        assertNotEquals(standardCommand, null);

        // different types -> returns false
        assertNotEquals(new ClearCommand(), standardCommand);

        // different index -> returns false
        assertNotEquals(new WorkerEditCommand(INDEX_SECOND_WORKER, DESC_AMY), standardCommand);

        // different descriptor -> returns false
        assertNotEquals(new WorkerEditCommand(INDEX_FIRST_WORKER, DESC_BOB), standardCommand);
    }

}
