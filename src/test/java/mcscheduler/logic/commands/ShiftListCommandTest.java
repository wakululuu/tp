package mcscheduler.logic.commands;

import static mcscheduler.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mcscheduler.model.Model;
import mcscheduler.model.ModelManager;
import mcscheduler.model.UserPrefs;
import mcscheduler.testutil.McSchedulerBuilder;
import mcscheduler.testutil.TypicalIndexes;

public class ShiftListCommandTest {
    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());
        expectedModel = new ModelManager(model.getMcScheduler(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(new ShiftListCommand(), model, ShiftListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        CommandTestUtil.showShiftAtIndex(model, TypicalIndexes.INDEX_FIRST_SHIFT);
        assertCommandSuccess(new ShiftListCommand(), model, ShiftListCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
