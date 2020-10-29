package mcscheduler.logic.commands;

import static mcscheduler.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.Test;

import mcscheduler.model.McScheduler;
import mcscheduler.model.Model;
import mcscheduler.model.ModelManager;
import mcscheduler.model.UserPrefs;
import mcscheduler.testutil.McSchedulerBuilder;

public class ClearCommandTest {

    @Test
    public void execute_emptyMcScheduler_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyMcScheduler_success() {
        Model model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());
        Model expectedModel = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());
        expectedModel.setMcScheduler(new McScheduler());

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
