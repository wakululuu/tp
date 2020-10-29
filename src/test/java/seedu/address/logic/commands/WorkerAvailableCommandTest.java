package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.NOT_FOUND_ROLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_CASHIER;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_CHEF;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
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
import seedu.address.model.tag.Role;

/**
 * Contains integration tests (interaction with the Model)
 * and unit tests for WorkerAvailableCommand.
 */
public class WorkerAvailableCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_invalidShiftIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredShiftList().size() + 1);
        Role role = Role.createRole(VALID_ROLE_CASHIER);
        WorkerAvailableCommand workerAvailableCommand = new WorkerAvailableCommand(outOfBoundIndex, role);

        assertCommandFailure(workerAvailableCommand, model, Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidShiftIndexFilteredList_failure() {
        showShiftAtIndex(model, INDEX_FIRST_SHIFT);
        Index outOfBoundIndex = INDEX_SECOND_SHIFT;
        // ensures that outOfBoundIndex is still in bounds of McScheduler shift list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getShiftList().size());

        WorkerAvailableCommand workerAvailableCommand = new WorkerAvailableCommand(outOfBoundIndex,
                Role.createRole(VALID_ROLE_CASHIER));

        assertCommandFailure(workerAvailableCommand, model, Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_roleNotFound_throwsCommandException() {
        WorkerAvailableCommand workerAvailableCommand = new WorkerAvailableCommand(INDEX_FIRST_SHIFT,
                Role.createRole(NOT_FOUND_ROLE));

        assertCommandFailure(workerAvailableCommand, model,
                String.format(Messages.MESSAGE_ROLE_NOT_FOUND, NOT_FOUND_ROLE));
    }

    @Test
    public void equals() {
        Role role = Role.createRole(VALID_ROLE_CASHIER);
        Role differentRole = Role.createRole(VALID_ROLE_CHEF);
        final WorkerAvailableCommand standardCommand = new WorkerAvailableCommand(INDEX_FIRST_SHIFT, role);

        // same values -> returns true
        Role roleCopy = Role.createRole(VALID_ROLE_CASHIER);
        WorkerAvailableCommand commandWithSameValues = new WorkerAvailableCommand(INDEX_FIRST_SHIFT, roleCopy);
        assertEquals(commandWithSameValues, standardCommand);

        // same object -> returns true
        assertEquals(standardCommand, standardCommand);

        // null -> returns false
        assertNotEquals(standardCommand, null);

        // different types -> returns false
        assertNotEquals(new ClearCommand(), standardCommand);

        // different index -> returns false
        assertNotEquals(standardCommand, new WorkerAvailableCommand(INDEX_SECOND_SHIFT, role));

        // different role -> returns false
        assertNotEquals(standardCommand, new WorkerAvailableCommand(INDEX_FIRST_SHIFT, differentRole));
    }

}
