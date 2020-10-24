package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showShiftAtIndex;
import static seedu.address.testutil.AddressBookBuilder.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SHIFT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_SHIFT;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.shift.Shift;

public class ShiftDeleteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Shift shiftToDelete = model.getFilteredShiftList().get(INDEX_FIRST_SHIFT.getZeroBased());
        ShiftDeleteCommand shiftDeleteCommand = new ShiftDeleteCommand(INDEX_FIRST_SHIFT);

        String expectedMessage = String.format(ShiftDeleteCommand.MESSAGE_DELETE_SHIFT_SUCCESS, shiftToDelete);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteShift(shiftToDelete);

        assertCommandSuccess(shiftDeleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredShiftList().size() + 1);
        ShiftDeleteCommand shiftDeleteCommand = new ShiftDeleteCommand(outOfBoundIndex);

        assertCommandFailure(shiftDeleteCommand, model, Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showShiftAtIndex(model, INDEX_FIRST_SHIFT);

        Shift shiftToDelete = model.getFilteredShiftList().get(INDEX_FIRST_SHIFT.getZeroBased());
        ShiftDeleteCommand shiftDeleteCommand = new ShiftDeleteCommand(INDEX_FIRST_SHIFT);

        String expectedMessage = String.format(ShiftDeleteCommand.MESSAGE_DELETE_SHIFT_SUCCESS, shiftToDelete);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteShift(shiftToDelete);
        showNoShift(expectedModel);

        assertCommandSuccess(shiftDeleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showShiftAtIndex(model, INDEX_FIRST_SHIFT);

        Index outOfBoundIndex = INDEX_SECOND_SHIFT;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getShiftList().size());

        ShiftDeleteCommand shiftDeleteCommand = new ShiftDeleteCommand(outOfBoundIndex);

        assertCommandFailure(shiftDeleteCommand, model, Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        ShiftDeleteCommand deleteFirstCommand = new ShiftDeleteCommand(INDEX_FIRST_SHIFT);
        ShiftDeleteCommand deleteSecondCommand = new ShiftDeleteCommand(INDEX_SECOND_SHIFT);

        // same object -> returns true
        assertEquals(deleteFirstCommand, deleteFirstCommand);

        // same values -> returns true
        ShiftDeleteCommand deleteFirstCommandCopy = new ShiftDeleteCommand(INDEX_FIRST_SHIFT);
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
