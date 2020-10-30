package mcscheduler.logic.commands;

import static mcscheduler.logic.commands.CommandTestUtil.assertCommandSuccess;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import mcscheduler.commons.core.Messages;
import mcscheduler.commons.core.index.Index;
import mcscheduler.model.Model;
import mcscheduler.model.ModelManager;
import mcscheduler.model.UserPrefs;
import mcscheduler.model.shift.Shift;
import mcscheduler.testutil.McSchedulerBuilder;
import mcscheduler.testutil.TestUtil;
import mcscheduler.testutil.TypicalIndexes;

public class ShiftDeleteCommandTest {

    private final Model model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Shift shiftToDelete = TestUtil.getShift(model, TypicalIndexes.INDEX_FIRST_SHIFT);
        ShiftDeleteCommand shiftDeleteCommand = new ShiftDeleteCommand(TypicalIndexes.INDEX_FIRST_SHIFT);

        String expectedMessage = String.format(ShiftDeleteCommand.MESSAGE_DELETE_SHIFT_SUCCESS, shiftToDelete);

        ModelManager expectedModel = new ModelManager(model.getMcScheduler(), new UserPrefs());
        expectedModel.deleteShift(shiftToDelete);

        assertCommandSuccess(shiftDeleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = TestUtil.getOutOfBoundShiftIndex(model);
        ShiftDeleteCommand shiftDeleteCommand = new ShiftDeleteCommand(outOfBoundIndex);

        CommandTestUtil.assertCommandFailure(shiftDeleteCommand, model, Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        CommandTestUtil.showShiftAtIndex(model, TypicalIndexes.INDEX_FIRST_SHIFT);

        Shift shiftToDelete = TestUtil.getShift(model, TypicalIndexes.INDEX_FIRST_SHIFT);
        ShiftDeleteCommand shiftDeleteCommand = new ShiftDeleteCommand(TypicalIndexes.INDEX_FIRST_SHIFT);

        String expectedMessage = String.format(ShiftDeleteCommand.MESSAGE_DELETE_SHIFT_SUCCESS, shiftToDelete);

        Model expectedModel = new ModelManager(model.getMcScheduler(), new UserPrefs());
        expectedModel.deleteShift(shiftToDelete);
        showNoShift(expectedModel);

        assertCommandSuccess(shiftDeleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        CommandTestUtil.showShiftAtIndex(model, TypicalIndexes.INDEX_FIRST_SHIFT);

        Index outOfBoundIndex = TypicalIndexes.INDEX_SECOND_SHIFT;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getMcScheduler().getShiftList().size());

        ShiftDeleteCommand shiftDeleteCommand = new ShiftDeleteCommand(outOfBoundIndex);

        CommandTestUtil.assertCommandFailure(shiftDeleteCommand, model, Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        ShiftDeleteCommand deleteFirstCommand = new ShiftDeleteCommand(TypicalIndexes.INDEX_FIRST_SHIFT);
        ShiftDeleteCommand deleteSecondCommand = new ShiftDeleteCommand(TypicalIndexes.INDEX_SECOND_SHIFT);

        // same object -> returns true
        assertEquals(deleteFirstCommand, deleteFirstCommand);

        // same values -> returns true
        ShiftDeleteCommand deleteFirstCommandCopy = new ShiftDeleteCommand(TypicalIndexes.INDEX_FIRST_SHIFT);
        assertEquals(deleteFirstCommandCopy, deleteFirstCommand);

        // different types -> returns false
        assertNotEquals(deleteFirstCommand, 1);

        // null -> returns false
        assertNotEquals(deleteFirstCommand, null);

        // different shift -> returns false
        assertNotEquals(deleteSecondCommand, deleteFirstCommand);
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoShift(Model model) {
        model.updateFilteredShiftList(p -> false);

        assertTrue(model.getFilteredShiftList().isEmpty());
    }
}
