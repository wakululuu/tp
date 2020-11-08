package mcscheduler.logic;

import static mcscheduler.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static mcscheduler.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static mcscheduler.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static mcscheduler.logic.commands.CommandTestUtil.PAY_DESC_AMY;
import static mcscheduler.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static mcscheduler.logic.commands.CommandTestUtil.ROLE_DESC_CASHIER;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_ROLE_CASHIER;
import static mcscheduler.testutil.Assert.assertThrows;
import static mcscheduler.testutil.TypicalWorkers.AMY;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import mcscheduler.commons.core.Messages;
import mcscheduler.commons.core.index.Index;
import mcscheduler.logic.commands.CommandResult;
import mcscheduler.logic.commands.WorkerAddCommand;
import mcscheduler.logic.commands.WorkerDeleteCommand;
import mcscheduler.logic.commands.WorkerListCommand;
import mcscheduler.logic.commands.exceptions.CommandException;
import mcscheduler.logic.parser.exceptions.ParseException;
import mcscheduler.model.Model;
import mcscheduler.model.ModelManager;
import mcscheduler.model.ReadOnlyMcScheduler;
import mcscheduler.model.UserPrefs;
import mcscheduler.model.role.Role;
import mcscheduler.model.worker.Worker;
import mcscheduler.storage.JsonMcSchedulerStorage;
import mcscheduler.storage.JsonUserPrefsStorage;
import mcscheduler.storage.StorageManager;
import mcscheduler.testutil.WorkerBuilder;

//@@author
public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy exception");

    @TempDir
    public Path temporaryFolder;

    private final Model model = new ModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        JsonMcSchedulerStorage mcSchedulerStorage = new JsonMcSchedulerStorage(
                temporaryFolder.resolve("mcScheduler.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(mcSchedulerStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "worker-delete 9";
        Index outOfBoundIndex = Index.fromOneBased(9);
        assertCommandException(deleteCommand, printOutOfBoundsWorkerIndexError(outOfBoundIndex));
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = WorkerListCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, WorkerListCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        // Setup LogicManager with JsonMcSchedulerIoExceptionThrowingStub
        JsonMcSchedulerStorage mcSchedulerStorage = new JsonMcSchedulerIoExceptionThrowingStub(
                temporaryFolder.resolve("ioExceptionMcScheduler.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(
                temporaryFolder.resolve("ioExceptionUserPrefs.json"));
        StorageManager storage = new StorageManager(mcSchedulerStorage, userPrefsStorage);
        model.addRole(Role.createRole(VALID_ROLE_CASHIER));
        logic = new LogicManager(model, storage);

        // Execute add command
        String addCommand = WorkerAddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + PAY_DESC_AMY
                + ADDRESS_DESC_AMY + ROLE_DESC_CASHIER;
        Worker expectedWorker = new WorkerBuilder(AMY).withUnavailableTimings().build();
        ModelManager expectedModel = new ModelManager();
        expectedModel.addRole(Role.createRole(VALID_ROLE_CASHIER));
        expectedModel.addWorker(expectedWorker);
        String expectedMessage = LogicManager.FILE_OPS_ERROR_MESSAGE + DUMMY_IO_EXCEPTION.getMessage();
        assertCommandFailure(addCommand, CommandException.class, expectedMessage, expectedModel);
    }

    @Test
    public void getFilteredWorkerList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredWorkerList().remove(0));
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
            Model expectedModel) throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage) {
        Model expectedModel = new ModelManager(model.getMcScheduler(), new UserPrefs());
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedModel);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     *
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    /**
     * A stub class to throw an {@code IOException} when the save method is called.
     */
    private static class JsonMcSchedulerIoExceptionThrowingStub extends JsonMcSchedulerStorage {
        private JsonMcSchedulerIoExceptionThrowingStub(Path filePath) {
            super(filePath);
        }

        @Override
        public void saveMcScheduler(ReadOnlyMcScheduler mcScheduler, Path filePath) throws IOException {
            throw DUMMY_IO_EXCEPTION;
        }
    }

    private String printOutOfBoundsWorkerIndexError(Index workerIndex) {
        return String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                String.format(Messages.MESSAGE_INVALID_WORKER_DISPLAYED_INDEX, workerIndex.getOneBased())
                        + WorkerDeleteCommand.MESSAGE_USAGE);
    }
}
