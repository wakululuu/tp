package mcscheduler.logic.commands;

import static mcscheduler.logic.commands.CommandTestUtil.DESC_AMY;
import static mcscheduler.logic.commands.CommandTestUtil.DESC_BOB;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_ROLE_CASHIER;
import static mcscheduler.logic.commands.CommandTestUtil.assertCommandFailure;
import static mcscheduler.logic.commands.CommandTestUtil.assertCommandSuccess;
import static mcscheduler.logic.commands.CommandTestUtil.showWorkerAtIndex;
import static mcscheduler.testutil.TypicalIndexes.INDEX_FIRST_WORKER;
import static mcscheduler.testutil.TypicalIndexes.INDEX_SECOND_WORKER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import mcscheduler.commons.core.Messages;
import mcscheduler.commons.core.index.Index;
import mcscheduler.logic.commands.WorkerEditCommand.EditWorkerDescriptor;
import mcscheduler.model.McScheduler;
import mcscheduler.model.Model;
import mcscheduler.model.ModelManager;
import mcscheduler.model.UserPrefs;
import mcscheduler.model.worker.Worker;
import mcscheduler.testutil.EditWorkerDescriptorBuilder;
import mcscheduler.testutil.McSchedulerBuilder;
import mcscheduler.testutil.TestUtil;
import mcscheduler.testutil.WorkerBuilder;

//@@author
/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand)
 * and unit tests for WorkerEditCommand.
 */
public class WorkerEditCommandTest {

    private final Model model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Worker editedWorker = new WorkerBuilder().build();
        EditWorkerDescriptor descriptor = new EditWorkerDescriptorBuilder(editedWorker).build();
        WorkerEditCommand workerEditCommand = new WorkerEditCommand(INDEX_FIRST_WORKER, descriptor);

        String expectedMessage = String.format(WorkerEditCommand.MESSAGE_EDIT_WORKER_SUCCESS, editedWorker);

        Model expectedModel = new ModelManager(new McScheduler(model.getMcScheduler()), new UserPrefs());
        expectedModel.setWorker(TestUtil.getWorker(model, INDEX_FIRST_WORKER), editedWorker);

        assertCommandSuccess(workerEditCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastWorker = TestUtil.getLastWorkerIndex(model);
        Worker lastWorker = TestUtil.getWorker(model, indexLastWorker);

        WorkerBuilder workerInList = new WorkerBuilder(lastWorker);
        Worker editedWorker = workerInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withRoles(VALID_ROLE_CASHIER).build();

        EditWorkerDescriptor descriptor = new EditWorkerDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withRoles(VALID_ROLE_CASHIER).build();
        WorkerEditCommand workerEditCommand = new WorkerEditCommand(indexLastWorker, descriptor);

        String expectedMessage = String.format(WorkerEditCommand.MESSAGE_EDIT_WORKER_SUCCESS, editedWorker);

        Model expectedModel = new ModelManager(new McScheduler(model.getMcScheduler()), new UserPrefs());
        expectedModel.setWorker(lastWorker, editedWorker);

        assertCommandSuccess(workerEditCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        WorkerEditCommand workerEditCommand = new WorkerEditCommand(INDEX_FIRST_WORKER, new EditWorkerDescriptor());
        Worker editedWorker = TestUtil.getWorker(model, INDEX_FIRST_WORKER);

        String expectedMessage = String.format(WorkerEditCommand.MESSAGE_EDIT_WORKER_SUCCESS, editedWorker);

        Model expectedModel = new ModelManager(new McScheduler(model.getMcScheduler()), new UserPrefs());

        assertCommandSuccess(workerEditCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showWorkerAtIndex(model, INDEX_FIRST_WORKER);

        Worker workerInFilteredList = TestUtil.getWorker(model, INDEX_FIRST_WORKER);
        Worker editedWorker = new WorkerBuilder(workerInFilteredList).withName(VALID_NAME_BOB).build();
        WorkerEditCommand workerEditCommand = new WorkerEditCommand(INDEX_FIRST_WORKER,
                new EditWorkerDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(WorkerEditCommand.MESSAGE_EDIT_WORKER_SUCCESS, editedWorker);

        Model expectedModel = new ModelManager(new McScheduler(model.getMcScheduler()), new UserPrefs());
        expectedModel.setWorker(TestUtil.getWorker(model, INDEX_FIRST_WORKER), editedWorker);

        assertCommandSuccess(workerEditCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateWorkerUnfilteredList_failure() {
        Worker firstWorker = TestUtil.getWorker(model, INDEX_FIRST_WORKER);
        EditWorkerDescriptor descriptor = new EditWorkerDescriptorBuilder(firstWorker).build();
        WorkerEditCommand workerEditCommand = new WorkerEditCommand(INDEX_SECOND_WORKER, descriptor);

        assertCommandFailure(workerEditCommand, model, WorkerEditCommand.MESSAGE_DUPLICATE_WORKER);
    }

    @Test
    public void execute_duplicateWorkerFilteredList_failure() {
        showWorkerAtIndex(model, INDEX_FIRST_WORKER);

        // edit worker in filtered list into a duplicate in the McScheduler
        Worker workerInList = model.getMcScheduler().getWorkerList().get(INDEX_SECOND_WORKER.getZeroBased());
        WorkerEditCommand workerEditCommand = new WorkerEditCommand(INDEX_FIRST_WORKER,
                new EditWorkerDescriptorBuilder(workerInList).build());

        assertCommandFailure(workerEditCommand, model, WorkerEditCommand.MESSAGE_DUPLICATE_WORKER);
    }

    @Test
    public void execute_invalidWorkerIndexUnfilteredList_failure() {
        Index outOfBoundIndex = TestUtil.getOutOfBoundWorkerIndex(model);
        EditWorkerDescriptor descriptor = new EditWorkerDescriptorBuilder().withName(VALID_NAME_BOB).build();
        WorkerEditCommand workerEditCommand = new WorkerEditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(workerEditCommand, model,
                CommandUtil.printOutOfBoundsWorkerIndexError(outOfBoundIndex, WorkerEditCommand.MESSAGE_USAGE));
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of the McScheduler
     */
    @Test
    public void execute_invalidWorkerIndexFilteredList_failure() {
        showWorkerAtIndex(model, INDEX_FIRST_WORKER);
        Index outOfBoundIndex = INDEX_SECOND_WORKER;
        // ensures that outOfBoundIndex is still in bounds of the McScheduler list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getMcScheduler().getWorkerList().size());

        WorkerEditCommand workerEditCommand = new WorkerEditCommand(outOfBoundIndex,
                new EditWorkerDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(workerEditCommand, model,
                CommandUtil.printOutOfBoundsWorkerIndexError(outOfBoundIndex, WorkerEditCommand.MESSAGE_USAGE));
    }

    //@@author wakululuu
    @Test
    public void execute_roleNotFound_throwsCommandException() {
        WorkerEditCommand editCommand = new WorkerEditCommand(INDEX_FIRST_WORKER,
                new EditWorkerDescriptorBuilder().withRoles("random role").build());

        assertCommandFailure(editCommand, model, String.format(Messages.MESSAGE_ROLE_NOT_FOUND, "Random role"));
    }

    //@@author
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
