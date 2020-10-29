package mcscheduler.logic.commands;

import static mcscheduler.logic.commands.CommandTestUtil.assertCommandSuccess;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import mcscheduler.commons.core.Messages;
import mcscheduler.commons.core.index.Index;
import mcscheduler.model.McScheduler;
import mcscheduler.model.Model;
import mcscheduler.model.ModelManager;
import mcscheduler.model.UserPrefs;
import mcscheduler.model.shift.Shift;
import mcscheduler.testutil.EditShiftDescriptorBuilder;
import mcscheduler.testutil.McSchedulerBuilder;
import mcscheduler.testutil.ShiftBuilder;
import mcscheduler.testutil.TypicalIndexes;

public class ShiftEditCommandTest {

    private Model model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Shift editedShift = new ShiftBuilder().build();
        ShiftEditCommand.EditShiftDescriptor descriptor = new EditShiftDescriptorBuilder(editedShift).build();
        ShiftEditCommand shiftEditCommand = new ShiftEditCommand(TypicalIndexes.INDEX_FIRST_SHIFT, descriptor);

        String expectedMessage = String.format(ShiftEditCommand.MESSAGE_EDIT_SHIFT_SUCCESS, editedShift);

        Model expectedModel = new ModelManager(new McScheduler(model.getMcScheduler()), new UserPrefs());
        expectedModel.setShift(model.getFilteredShiftList().get(0), editedShift);

        assertCommandSuccess(shiftEditCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastShift = Index.fromOneBased(model.getFilteredShiftList().size());
        Shift lastShift = model.getFilteredShiftList().get(indexLastShift.getZeroBased());

        ShiftBuilder shiftInList = new ShiftBuilder(lastShift);
        Shift editedShift = shiftInList.withShiftDay(CommandTestUtil.VALID_DAY_TUE)
            .withRoleRequirements(
                CommandTestUtil.VALID_ROLE_REQUIREMENT_CASHIER, CommandTestUtil.VALID_ROLE_REQUIREMENT_CHEF).build();

        ShiftEditCommand.EditShiftDescriptor descriptor = new EditShiftDescriptorBuilder().withShiftDay(
            CommandTestUtil.VALID_DAY_TUE)
            .withRoleRequirements(
                CommandTestUtil.VALID_ROLE_REQUIREMENT_CASHIER, CommandTestUtil.VALID_ROLE_REQUIREMENT_CHEF).build();
        ShiftEditCommand shiftEditCommand = new ShiftEditCommand(indexLastShift, descriptor);

        String expectedMessage = String.format(ShiftEditCommand.MESSAGE_EDIT_SHIFT_SUCCESS, editedShift);

        Model expectedModel = new ModelManager(new McScheduler(model.getMcScheduler()), new UserPrefs());
        expectedModel.setShift(lastShift, editedShift);

        assertCommandSuccess(shiftEditCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        ShiftEditCommand shiftEditCommand =
            new ShiftEditCommand(TypicalIndexes.INDEX_FIRST_SHIFT, new ShiftEditCommand.EditShiftDescriptor());
        Shift editedShift = model.getFilteredShiftList().get(TypicalIndexes.INDEX_FIRST_SHIFT.getZeroBased());

        String expectedMessage = String.format(ShiftEditCommand.MESSAGE_EDIT_SHIFT_SUCCESS, editedShift);

        Model expectedModel = new ModelManager(new McScheduler(model.getMcScheduler()), new UserPrefs());

        assertCommandSuccess(shiftEditCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        CommandTestUtil.showShiftAtIndex(model, TypicalIndexes.INDEX_FIRST_SHIFT);

        Shift shiftInFilteredList = model.getFilteredShiftList().get(TypicalIndexes.INDEX_FIRST_SHIFT.getZeroBased());
        Shift editedShift = new ShiftBuilder(shiftInFilteredList).withShiftTime(CommandTestUtil.VALID_TIME_PM).build();
        ShiftEditCommand shiftEditCommand = new ShiftEditCommand(TypicalIndexes.INDEX_FIRST_SHIFT,
            new EditShiftDescriptorBuilder().withShiftTime(CommandTestUtil.VALID_TIME_PM).build());

        String expectedMessage = String.format(ShiftEditCommand.MESSAGE_EDIT_SHIFT_SUCCESS, editedShift);

        Model expectedModel = new ModelManager(new McScheduler(model.getMcScheduler()), new UserPrefs());
        expectedModel.setShift(model.getFilteredShiftList().get(0), editedShift);

        assertCommandSuccess(shiftEditCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateShiftUnfilteredList_failure() {
        Shift firstShift = model.getFilteredShiftList().get(TypicalIndexes.INDEX_FIRST_SHIFT.getZeroBased());
        ShiftEditCommand.EditShiftDescriptor descriptor = new EditShiftDescriptorBuilder(firstShift).build();
        ShiftEditCommand shiftEditCommand = new ShiftEditCommand(TypicalIndexes.INDEX_SECOND_SHIFT, descriptor);

        CommandTestUtil.assertCommandFailure(shiftEditCommand, model, ShiftEditCommand.MESSAGE_DUPLICATE_SHIFT);
    }

    @Test
    public void execute_duplicateShiftFilteredList_failure() {
        CommandTestUtil.showShiftAtIndex(model, TypicalIndexes.INDEX_FIRST_SHIFT);

        // edit shift in filtered list into a duplicate in address book
        Shift shiftInList = model.getMcScheduler().getShiftList().get(TypicalIndexes.INDEX_SECOND_SHIFT.getZeroBased());
        ShiftEditCommand shiftEditCommand = new ShiftEditCommand(TypicalIndexes.INDEX_FIRST_SHIFT,
            new EditShiftDescriptorBuilder(shiftInList).build());

        CommandTestUtil.assertCommandFailure(shiftEditCommand, model, ShiftEditCommand.MESSAGE_DUPLICATE_SHIFT);
    }

    @Test
    public void execute_invalidShiftIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredShiftList().size() + 1);
        ShiftEditCommand.EditShiftDescriptor descriptor = new EditShiftDescriptorBuilder().withShiftDay(
            CommandTestUtil.VALID_DAY_MON)
            .withShiftTime(CommandTestUtil.VALID_TIME_AM).build();
        ShiftEditCommand shiftEditCommand = new ShiftEditCommand(outOfBoundIndex, descriptor);

        CommandTestUtil.assertCommandFailure(shiftEditCommand, model, Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidShiftIndexFilteredList_failure() {
        CommandTestUtil.showShiftAtIndex(model, TypicalIndexes.INDEX_FIRST_SHIFT);
        Index outOfBoundIndex = TypicalIndexes.INDEX_SECOND_SHIFT;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getMcScheduler().getShiftList().size());

        ShiftEditCommand shiftEditCommand = new ShiftEditCommand(outOfBoundIndex,
            new EditShiftDescriptorBuilder().withShiftDay(CommandTestUtil.VALID_DAY_MON).withShiftTime(
                CommandTestUtil.VALID_TIME_PM).build());

        CommandTestUtil.assertCommandFailure(shiftEditCommand, model, Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_roleNotFound_throwsCommandException() {
        ShiftEditCommand shiftEditCommand = new ShiftEditCommand(TypicalIndexes.INDEX_FIRST_SHIFT,
            new EditShiftDescriptorBuilder().withRoleRequirements("random role 1 0").build());

        CommandTestUtil
            .assertCommandFailure(shiftEditCommand, model,
                String.format(Messages.MESSAGE_ROLE_NOT_FOUND, "Random role"));
    }

    @Test
    public void equals() {
        final ShiftEditCommand standardCommand = new ShiftEditCommand(
            TypicalIndexes.INDEX_FIRST_SHIFT, CommandTestUtil.DESC_FIRST_SHIFT);

        // same values -> returns true
        ShiftEditCommand.EditShiftDescriptor copyDescriptor =
            new ShiftEditCommand.EditShiftDescriptor(CommandTestUtil.DESC_FIRST_SHIFT);
        ShiftEditCommand commandWithSameValues = new ShiftEditCommand(TypicalIndexes.INDEX_FIRST_SHIFT, copyDescriptor);
        assertEquals(commandWithSameValues, standardCommand);

        // same object -> returns true
        assertEquals(standardCommand, standardCommand);

        // null -> returns false
        assertNotEquals(standardCommand, null);

        // different types -> returns false
        assertNotEquals(new ClearCommand(), standardCommand);

        // different index -> returns false
        assertNotEquals(new ShiftEditCommand(TypicalIndexes.INDEX_SECOND_SHIFT, CommandTestUtil.DESC_FIRST_SHIFT),
            standardCommand);

        // different descriptor -> returns false
        assertNotEquals(new ShiftEditCommand(TypicalIndexes.INDEX_FIRST_SHIFT, CommandTestUtil.DESC_SECOND_SHIFT),
            standardCommand);
    }


}
