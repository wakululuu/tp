package mcscheduler.logic.commands;

import static mcscheduler.logic.commands.CommandTestUtil.DESC_FIRST_SHIFT;
import static mcscheduler.logic.commands.CommandTestUtil.DESC_SECOND_SHIFT;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_DAY_MON;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_DAY_TUE;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_ROLE_REQUIREMENT_CASHIER;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_ROLE_REQUIREMENT_CHEF;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_TIME_AM;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_TIME_PM;
import static mcscheduler.logic.commands.CommandTestUtil.assertCommandFailure;
import static mcscheduler.logic.commands.CommandTestUtil.assertCommandSuccess;
import static mcscheduler.logic.commands.CommandTestUtil.showShiftAtIndex;
import static mcscheduler.testutil.TypicalIndexes.INDEX_FIRST_SHIFT;
import static mcscheduler.testutil.TypicalIndexes.INDEX_SECOND_SHIFT;
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
import mcscheduler.testutil.TestUtil;

//@@author
public class ShiftEditCommandTest {

    private final Model model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Shift editedShift = new ShiftBuilder().build();
        ShiftEditCommand.EditShiftDescriptor descriptor = new EditShiftDescriptorBuilder(editedShift).build();
        ShiftEditCommand shiftEditCommand = new ShiftEditCommand(INDEX_FIRST_SHIFT, descriptor);

        String expectedMessage = String.format(ShiftEditCommand.MESSAGE_EDIT_SHIFT_SUCCESS, editedShift);

        Model expectedModel = new ModelManager(new McScheduler(model.getMcScheduler()), new UserPrefs());
        expectedModel.setShift(TestUtil.getShift(model, INDEX_FIRST_SHIFT), editedShift);

        assertCommandSuccess(shiftEditCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastShift = TestUtil.getLastShiftIndex(model);
        Shift lastShift = TestUtil.getShift(model, indexLastShift);

        ShiftBuilder shiftInList = new ShiftBuilder(lastShift);
        Shift editedShift = shiftInList.withShiftDay(VALID_DAY_TUE)
                .withRoleRequirements(VALID_ROLE_REQUIREMENT_CASHIER, VALID_ROLE_REQUIREMENT_CHEF).build();

        ShiftEditCommand.EditShiftDescriptor descriptor = new EditShiftDescriptorBuilder().withShiftDay(VALID_DAY_TUE)
                .withRoleRequirements(VALID_ROLE_REQUIREMENT_CASHIER, VALID_ROLE_REQUIREMENT_CHEF).build();
        ShiftEditCommand shiftEditCommand = new ShiftEditCommand(indexLastShift, descriptor);

        String expectedMessage = String.format(ShiftEditCommand.MESSAGE_EDIT_SHIFT_SUCCESS, editedShift);

        Model expectedModel = new ModelManager(new McScheduler(model.getMcScheduler()), new UserPrefs());
        expectedModel.setShift(lastShift, editedShift);

        assertCommandSuccess(shiftEditCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        ShiftEditCommand shiftEditCommand =
                new ShiftEditCommand(INDEX_FIRST_SHIFT, new ShiftEditCommand.EditShiftDescriptor());
        Shift editedShift = TestUtil.getShift(model, INDEX_FIRST_SHIFT);

        String expectedMessage = String.format(ShiftEditCommand.MESSAGE_EDIT_SHIFT_SUCCESS, editedShift);

        Model expectedModel = new ModelManager(new McScheduler(model.getMcScheduler()), new UserPrefs());

        assertCommandSuccess(shiftEditCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showShiftAtIndex(model, INDEX_FIRST_SHIFT);

        Shift shiftInFilteredList = TestUtil.getShift(model, INDEX_FIRST_SHIFT);
        Shift editedShift = new ShiftBuilder(shiftInFilteredList).withShiftTime(VALID_TIME_PM).build();
        ShiftEditCommand shiftEditCommand = new ShiftEditCommand(INDEX_FIRST_SHIFT,
                new EditShiftDescriptorBuilder().withShiftTime(VALID_TIME_PM).build());

        String expectedMessage = String.format(ShiftEditCommand.MESSAGE_EDIT_SHIFT_SUCCESS, editedShift);

        Model expectedModel = new ModelManager(new McScheduler(model.getMcScheduler()), new UserPrefs());
        expectedModel.setShift(TestUtil.getShift(model, INDEX_FIRST_SHIFT), editedShift);

        assertCommandSuccess(shiftEditCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateShiftUnfilteredList_failure() {
        Shift firstShift = TestUtil.getShift(model, INDEX_FIRST_SHIFT);
        ShiftEditCommand.EditShiftDescriptor descriptor = new EditShiftDescriptorBuilder(firstShift).build();
        ShiftEditCommand shiftEditCommand = new ShiftEditCommand(INDEX_SECOND_SHIFT, descriptor);

        assertCommandFailure(shiftEditCommand, model, ShiftEditCommand.MESSAGE_DUPLICATE_SHIFT);
    }

    @Test
    public void execute_duplicateShiftFilteredList_failure() {
        showShiftAtIndex(model, INDEX_FIRST_SHIFT);

        // edit shift in filtered list into a duplicate in the McScheduler
        Shift shiftInList = model.getMcScheduler().getShiftList().get(INDEX_SECOND_SHIFT.getZeroBased());
        ShiftEditCommand shiftEditCommand = new ShiftEditCommand(INDEX_FIRST_SHIFT,
                new EditShiftDescriptorBuilder(shiftInList).build());

        assertCommandFailure(shiftEditCommand, model, ShiftEditCommand.MESSAGE_DUPLICATE_SHIFT);
    }

    @Test
    public void execute_invalidShiftIndexUnfilteredList_failure() {
        Index outOfBoundIndex = TestUtil.getOutOfBoundShiftIndex(model);
        ShiftEditCommand.EditShiftDescriptor descriptor = new EditShiftDescriptorBuilder().withShiftDay(VALID_DAY_MON)
                .withShiftTime(VALID_TIME_AM).build();
        ShiftEditCommand shiftEditCommand = new ShiftEditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(shiftEditCommand, model,
                CommandUtil.printOutOfBoundsShiftIndexError(outOfBoundIndex, ShiftEditCommand.MESSAGE_USAGE));
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of the McScheduler
     */
    @Test
    public void execute_invalidShiftIndexFilteredList_failure() {
        showShiftAtIndex(model, INDEX_FIRST_SHIFT);
        Index outOfBoundIndex = INDEX_SECOND_SHIFT;
        // ensures that outOfBoundIndex is still in bounds of the McScheduler list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getMcScheduler().getShiftList().size());

        ShiftEditCommand shiftEditCommand = new ShiftEditCommand(outOfBoundIndex,
                new EditShiftDescriptorBuilder().withShiftDay(VALID_DAY_MON).withShiftTime(VALID_TIME_PM).build());

        assertCommandFailure(shiftEditCommand, model,
                CommandUtil.printOutOfBoundsShiftIndexError(outOfBoundIndex, ShiftEditCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_roleNotFound_throwsCommandException() {
        ShiftEditCommand shiftEditCommand = new ShiftEditCommand(INDEX_FIRST_SHIFT,
                new EditShiftDescriptorBuilder().withRoleRequirements("random role 1 0").build());

        assertCommandFailure(shiftEditCommand, model,
                String.format(Messages.MESSAGE_ROLE_NOT_FOUND, "Random role"));
    }

    @Test
    public void equals() {
        final ShiftEditCommand standardCommand = new ShiftEditCommand(INDEX_FIRST_SHIFT, DESC_FIRST_SHIFT);

        // same values -> returns true
        ShiftEditCommand.EditShiftDescriptor copyDescriptor =
                new ShiftEditCommand.EditShiftDescriptor(DESC_FIRST_SHIFT);
        ShiftEditCommand commandWithSameValues = new ShiftEditCommand(INDEX_FIRST_SHIFT, copyDescriptor);
        assertEquals(commandWithSameValues, standardCommand);

        // same object -> returns true
        assertEquals(standardCommand, standardCommand);

        // null -> returns false
        assertNotEquals(standardCommand, null);

        // different types -> returns false
        assertNotEquals(new ClearCommand(), standardCommand);

        // different index -> returns false
        assertNotEquals(new ShiftEditCommand(INDEX_SECOND_SHIFT, DESC_FIRST_SHIFT), standardCommand);

        // different descriptor -> returns false
        assertNotEquals(new ShiftEditCommand(INDEX_FIRST_SHIFT, DESC_SECOND_SHIFT), standardCommand);
    }

}
