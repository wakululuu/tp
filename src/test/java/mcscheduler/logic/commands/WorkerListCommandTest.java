package mcscheduler.logic.commands;

import static mcscheduler.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mcscheduler.model.Model;
import mcscheduler.model.ModelManager;
import mcscheduler.model.UserPrefs;
import mcscheduler.testutil.McSchedulerBuilder;
import mcscheduler.testutil.TypicalIndexes;

/**
 * Contains integration tests (interaction with the Model) and unit tests for WorkerListCommand.
 */
public class WorkerListCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());
        expectedModel = new ModelManager(model.getMcScheduler(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(new WorkerListCommand(), model, WorkerListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        CommandTestUtil.showWorkerAtIndex(model, TypicalIndexes.INDEX_FIRST_WORKER);
        assertCommandSuccess(new WorkerListCommand(), model, WorkerListCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
