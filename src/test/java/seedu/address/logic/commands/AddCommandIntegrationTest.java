package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.AddressBookBuilder.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.worker.Worker;
import seedu.address.testutil.WorkerBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newWorker_success() {
        Worker validWorker = new WorkerBuilder().build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addWorker(validWorker);

        assertCommandSuccess(new AddCommand(validWorker), model,
                String.format(AddCommand.MESSAGE_SUCCESS, validWorker), expectedModel);
    }

    @Test
    public void execute_duplicateWorker_throwsCommandException() {
        Worker workerInList = model.getAddressBook().getWorkerList().get(0);
        assertCommandFailure(new AddCommand(workerInList), model, AddCommand.MESSAGE_DUPLICATE_WORKER);
    }

}
