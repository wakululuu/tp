package mcscheduler.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import mcscheduler.commons.core.Messages;
import mcscheduler.commons.core.index.Index;
import mcscheduler.model.Model;
import mcscheduler.model.ModelManager;
import mcscheduler.model.UserPrefs;
import mcscheduler.model.tag.Role;
import mcscheduler.testutil.McSchedulerBuilder;
import mcscheduler.testutil.TypicalIndexes;

/**
 * Contains integration tests (interaction with the Model)
 * and unit tests for WorkerAvailableCommand.
 */
public class WorkerAvailableCommandTest {
    private Model model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());

    @Test
    public void execute_invalidShiftIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredShiftList().size() + 1);
        Role role = Role.createRole(CommandTestUtil.VALID_ROLE_CASHIER);
        WorkerAvailableCommand workerAvailableCommand = new WorkerAvailableCommand(outOfBoundIndex, role);

        CommandTestUtil
            .assertCommandFailure(workerAvailableCommand, model, Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidShiftIndexFilteredList_failure() {
        CommandTestUtil.showShiftAtIndex(model, TypicalIndexes.INDEX_FIRST_SHIFT);
        Index outOfBoundIndex = TypicalIndexes.INDEX_SECOND_SHIFT;
        // ensures that outOfBoundIndex is still in bounds of McScheduler shift list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getMcScheduler().getShiftList().size());

        WorkerAvailableCommand workerAvailableCommand = new WorkerAvailableCommand(outOfBoundIndex,
            Role.createRole(CommandTestUtil.VALID_ROLE_CASHIER));

        CommandTestUtil
            .assertCommandFailure(workerAvailableCommand, model, Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_roleNotFound_throwsCommandException() {
        WorkerAvailableCommand workerAvailableCommand = new WorkerAvailableCommand(TypicalIndexes.INDEX_FIRST_SHIFT,
            Role.createRole(CommandTestUtil.NOT_FOUND_ROLE));

        CommandTestUtil.assertCommandFailure(workerAvailableCommand, model,
            String.format(Messages.MESSAGE_ROLE_NOT_FOUND, CommandTestUtil.NOT_FOUND_ROLE));
    }

    @Test
    public void equals() {
        Role role = Role.createRole(CommandTestUtil.VALID_ROLE_CASHIER);
        Role differentRole = Role.createRole(CommandTestUtil.VALID_ROLE_CHEF);
        final WorkerAvailableCommand standardCommand =
            new WorkerAvailableCommand(TypicalIndexes.INDEX_FIRST_SHIFT, role);

        // same values -> returns true
        Role roleCopy = Role.createRole(CommandTestUtil.VALID_ROLE_CASHIER);
        WorkerAvailableCommand commandWithSameValues =
            new WorkerAvailableCommand(TypicalIndexes.INDEX_FIRST_SHIFT, roleCopy);
        assertEquals(commandWithSameValues, standardCommand);

        // same object -> returns true
        assertEquals(standardCommand, standardCommand);

        // null -> returns false
        assertNotEquals(standardCommand, null);

        // different types -> returns false
        assertNotEquals(new ClearCommand(), standardCommand);

        // different index -> returns false
        assertNotEquals(standardCommand, new WorkerAvailableCommand(TypicalIndexes.INDEX_SECOND_SHIFT, role));

        // different role -> returns false
        assertNotEquals(standardCommand, new WorkerAvailableCommand(TypicalIndexes.INDEX_FIRST_SHIFT, differentRole));
    }

}
