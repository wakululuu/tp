package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_FIRST_SHIFT;
import static seedu.address.logic.commands.CommandTestUtil.DESC_SECOND_SHIFT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DAY_MON;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DAY_TUE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_REQUIREMENT_CASHIER;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_REQUIREMENT_CHEF;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TIME_AM;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TIME_PM;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showShiftAtIndex;
import static seedu.address.testutil.AddressBookBuilder.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SHIFT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_SHIFT;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.shift.Shift;
import seedu.address.testutil.EditShiftDescriptorBuilder;
import seedu.address.testutil.ShiftBuilder;

public class ShiftEditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Shift editedShift = new ShiftBuilder().build();
        ShiftEditCommand.EditShiftDescriptor descriptor = new EditShiftDescriptorBuilder(editedShift).build();
        ShiftEditCommand shiftEditCommand = new ShiftEditCommand(INDEX_FIRST_SHIFT, descriptor);

        String expectedMessage = String.format(ShiftEditCommand.MESSAGE_EDIT_SHIFT_SUCCESS, editedShift);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setShift(model.getFilteredShiftList().get(0), editedShift);

        assertCommandSuccess(shiftEditCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastShift = Index.fromOneBased(model.getFilteredShiftList().size());
        Shift lastShift = model.getFilteredShiftList().get(indexLastShift.getZeroBased());

        ShiftBuilder shiftInList = new ShiftBuilder(lastShift);
        Shift editedShift = shiftInList.withShiftDay(VALID_DAY_TUE)
                .withRoleRequirements(VALID_ROLE_REQUIREMENT_CASHIER, VALID_ROLE_REQUIREMENT_CHEF).build();

        ShiftEditCommand.EditShiftDescriptor descriptor = new EditShiftDescriptorBuilder().withShiftDay(VALID_DAY_TUE)
                .withRoleRequirements(VALID_ROLE_REQUIREMENT_CASHIER, VALID_ROLE_REQUIREMENT_CHEF).build();
        ShiftEditCommand shiftEditCommand = new ShiftEditCommand(indexLastShift, descriptor);

        String expectedMessage = String.format(ShiftEditCommand.MESSAGE_EDIT_SHIFT_SUCCESS, editedShift);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setShift(lastShift, editedShift);

        assertCommandSuccess(shiftEditCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        ShiftEditCommand shiftEditCommand =
                new ShiftEditCommand(INDEX_FIRST_SHIFT, new ShiftEditCommand.EditShiftDescriptor());
        Shift editedShift = model.getFilteredShiftList().get(INDEX_FIRST_SHIFT.getZeroBased());

        String expectedMessage = String.format(ShiftEditCommand.MESSAGE_EDIT_SHIFT_SUCCESS, editedShift);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(shiftEditCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showShiftAtIndex(model, INDEX_FIRST_SHIFT);

        Shift shiftInFilteredList = model.getFilteredShiftList().get(INDEX_FIRST_SHIFT.getZeroBased());
        Shift editedShift = new ShiftBuilder(shiftInFilteredList).withShiftTime(VALID_TIME_PM).build();
        ShiftEditCommand shiftEditCommand = new ShiftEditCommand(INDEX_FIRST_SHIFT,
                new EditShiftDescriptorBuilder().withShiftTime(VALID_TIME_PM).build());

        String expectedMessage = String.format(ShiftEditCommand.MESSAGE_EDIT_SHIFT_SUCCESS, editedShift);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setShift(model.getFilteredShiftList().get(0), editedShift);

        assertCommandSuccess(shiftEditCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateShiftUnfilteredList_failure() {
        Shift firstShift = model.getFilteredShiftList().get(INDEX_FIRST_SHIFT.getZeroBased());
        ShiftEditCommand.EditShiftDescriptor descriptor = new EditShiftDescriptorBuilder(firstShift).build();
        ShiftEditCommand shiftEditCommand = new ShiftEditCommand(INDEX_SECOND_SHIFT, descriptor);

        assertCommandFailure(shiftEditCommand, model, ShiftEditCommand.MESSAGE_DUPLICATE_SHIFT);
    }

    @Test
    public void execute_duplicateShiftFilteredList_failure() {
        showShiftAtIndex(model, INDEX_FIRST_SHIFT);

        // edit shift in filtered list into a duplicate in address book
        Shift shiftInList = model.getAddressBook().getShiftList().get(INDEX_SECOND_SHIFT.getZeroBased());
        ShiftEditCommand shiftEditCommand = new ShiftEditCommand(INDEX_FIRST_SHIFT,
                new EditShiftDescriptorBuilder(shiftInList).build());

        assertCommandFailure(shiftEditCommand, model, ShiftEditCommand.MESSAGE_DUPLICATE_SHIFT);
    }

    @Test
    public void execute_invalidShiftIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredShiftList().size() + 1);
        ShiftEditCommand.EditShiftDescriptor descriptor = new EditShiftDescriptorBuilder().withShiftDay(VALID_DAY_MON)
                .withShiftTime(VALID_TIME_AM).build();
        ShiftEditCommand shiftEditCommand = new ShiftEditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(shiftEditCommand, model, Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidShiftIndexFilteredList_failure() {
        showShiftAtIndex(model, INDEX_FIRST_SHIFT);
        Index outOfBoundIndex = INDEX_SECOND_SHIFT;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getShiftList().size());

        ShiftEditCommand shiftEditCommand = new ShiftEditCommand(outOfBoundIndex,
                new EditShiftDescriptorBuilder().withShiftDay(VALID_DAY_MON).withShiftTime(VALID_TIME_PM).build());

        assertCommandFailure(shiftEditCommand, model, Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final ShiftEditCommand standardCommand = new ShiftEditCommand(INDEX_FIRST_SHIFT, DESC_FIRST_SHIFT);

        // same values -> returns true
        ShiftEditCommand.EditShiftDescriptor copyDescriptor =
                new ShiftEditCommand.EditShiftDescriptor(DESC_FIRST_SHIFT);
        ShiftEditCommand commandWithSameValues = new ShiftEditCommand(INDEX_FIRST_SHIFT, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new ShiftEditCommand(INDEX_SECOND_SHIFT, DESC_FIRST_SHIFT)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new ShiftEditCommand(INDEX_FIRST_SHIFT, DESC_SECOND_SHIFT)));
    }


}
