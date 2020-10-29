package mcscheduler.logic.commands;

import static mcscheduler.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mcscheduler.model.Model;
import mcscheduler.model.ModelManager;
import mcscheduler.model.UserPrefs;
import mcscheduler.model.worker.Worker;
import mcscheduler.testutil.McSchedulerBuilder;
import mcscheduler.testutil.WorkerBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code WorkerAddCommand}.
 */
public class WorkerAddCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());
    }

    @Test
    public void execute_newWorker_success() {
        Worker validWorker = new WorkerBuilder().build();

        Model expectedModel = new ModelManager(model.getMcScheduler(), new UserPrefs());
        expectedModel.addWorker(validWorker);

        assertCommandSuccess(new WorkerAddCommand(validWorker), model,
            String.format(WorkerAddCommand.MESSAGE_SUCCESS, validWorker), expectedModel);
    }

    @Test
    public void execute_duplicateWorker_throwsCommandException() {
        Worker workerInList = model.getMcScheduler().getWorkerList().get(0);
        CommandTestUtil
            .assertCommandFailure(new WorkerAddCommand(workerInList), model, WorkerAddCommand.MESSAGE_DUPLICATE_WORKER);
    }

}
