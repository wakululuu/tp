package mcscheduler.logic.commands;

import static mcscheduler.logic.commands.CommandTestUtil.NOT_FOUND_ROLE;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_ROLE_CASHIER;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_ROLE_CHEF;
import static mcscheduler.logic.commands.CommandTestUtil.assertCommandFailure;
import static mcscheduler.logic.commands.CommandTestUtil.showShiftAtIndex;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import mcscheduler.commons.core.Messages;
import mcscheduler.commons.core.index.Index;
import mcscheduler.model.Model;
import mcscheduler.model.ModelManager;
import mcscheduler.model.UserPrefs;
import mcscheduler.model.role.Role;
import mcscheduler.testutil.McSchedulerBuilder;
import mcscheduler.testutil.TestUtil;
import mcscheduler.testutil.TypicalIndexes;

//@@author plosslaw
/**
 * Contains integration tests (interaction with the Model)
 * and unit tests for WorkerAvailableCommand.
 */
public class WorkerAvailableCommandTest {
    private final Model model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());

    @Test
    public void execute_invalidShiftIndexUnfilteredList_failure() {
        Index outOfBoundIndex = TestUtil.getOutOfBoundShiftIndex(model);
        Role role = Role.createRole(VALID_ROLE_CASHIER);
        WorkerAvailableCommand workerAvailableCommand = new WorkerAvailableCommand(outOfBoundIndex, role);

        assertCommandFailure(workerAvailableCommand, model,
                CommandUtil.printOutOfBoundsShiftIndexError(outOfBoundIndex, WorkerAvailableCommand.MESSAGE_USAGE));
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of the McScheduler
     */
    @Test
    public void execute_invalidShiftIndexFilteredList_failure() {
        showShiftAtIndex(model, TypicalIndexes.INDEX_FIRST_SHIFT);
        Index outOfBoundIndex = TypicalIndexes.INDEX_SECOND_SHIFT;
        // ensures that outOfBoundIndex is still in bounds of McScheduler shift list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getMcScheduler().getShiftList().size());

        WorkerAvailableCommand workerAvailableCommand = new WorkerAvailableCommand(outOfBoundIndex,
            Role.createRole(VALID_ROLE_CASHIER));

        assertCommandFailure(workerAvailableCommand, model,
                CommandUtil.printOutOfBoundsShiftIndexError(outOfBoundIndex, WorkerAvailableCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_roleNotFound_throwsCommandException() {
        WorkerAvailableCommand workerAvailableCommand = new WorkerAvailableCommand(TypicalIndexes.INDEX_FIRST_SHIFT,
            Role.createRole(NOT_FOUND_ROLE));

        assertCommandFailure(workerAvailableCommand, model,
                String.format(Messages.MESSAGE_ROLE_NOT_FOUND, NOT_FOUND_ROLE));
    }

    @Test
    public void equals() {
        Role role = Role.createRole(VALID_ROLE_CASHIER);
        Role differentRole = Role.createRole(VALID_ROLE_CHEF);
        final WorkerAvailableCommand standardCommand =
            new WorkerAvailableCommand(TypicalIndexes.INDEX_FIRST_SHIFT, role);

        // same values -> returns true
        Role roleCopy = Role.createRole(VALID_ROLE_CASHIER);
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
