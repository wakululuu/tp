package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.AddressBookBuilder.getTypicalAddressBook;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SHIFT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_WORKER;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_SHIFT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_WORKER;
import static seedu.address.testutil.TypicalShifts.SHIFT_A;
import static seedu.address.testutil.TypicalWorkers.BENSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.tag.Leave;
import seedu.address.model.tag.Role;

public class CancelLeaveCommandTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new CancelLeaveCommand(null, INDEX_FIRST_WORKER));
        assertThrows(NullPointerException.class, () -> new CancelLeaveCommand(INDEX_FIRST_SHIFT, null));
        assertThrows(NullPointerException.class, () -> new CancelLeaveCommand(null, null));
    }

    @Test
    public void execute_LeaveInModel_success() throws Exception {
        ModelManager model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addWorker(BENSON);
        model.addShift(SHIFT_A);
        Assignment assignment = new Assignment(SHIFT_A, BENSON, new Leave());
        model.addAssignment(assignment);

        assertTrue(model.hasAssignment(assignment));

        CommandResult result = new CancelLeaveCommand(INDEX_FIRST_SHIFT, INDEX_FIRST_WORKER).execute(model);

        assertEquals(String.format(CancelLeaveCommand.MESSAGE_CANCEL_LEAVE_SUCCESS_PREFIX
                + UnassignCommand.MESSAGE_UNASSIGN_SUCCESS, assignment), result.getFeedbackToUser());
        assertFalse(model.hasAssignment(assignment));

    }

    @Test
    public void execute_LeaveNotInModel_throwsCommandException() {
        ModelManager model = new ModelManager(new AddressBook(), new UserPrefs());
        assertThrows(CommandException.class,
                () -> new CancelLeaveCommand(INDEX_FIRST_SHIFT, INDEX_FIRST_WORKER).execute(model));

        model.addShift(SHIFT_A);
        model.addWorker(BENSON);
        assertThrows(CommandException.class,
                () -> new CancelLeaveCommand(INDEX_FIRST_SHIFT, INDEX_FIRST_WORKER).execute(model));
    }

    @Test
    public void execute_AssignmentInModelNotLeave_throwsCommandException() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addWorker(BENSON);
        model.addShift(SHIFT_A);
        model.addAssignment(new Assignment(SHIFT_A, BENSON, Role.createRole("Cashier")));
        assertThrows(CommandException.class,
                () -> new CancelLeaveCommand(INDEX_FIRST_SHIFT, INDEX_FIRST_WORKER).execute(model));
    }

    @Test
    public void equals() {
        CancelLeaveCommand firstIndexes = new CancelLeaveCommand(INDEX_FIRST_SHIFT, INDEX_FIRST_WORKER);
        CancelLeaveCommand secondIndexes = new CancelLeaveCommand(INDEX_SECOND_SHIFT, INDEX_SECOND_WORKER);
        CancelLeaveCommand firstShiftSecondWorker = new CancelLeaveCommand(INDEX_FIRST_SHIFT, INDEX_SECOND_WORKER);

        assertTrue(firstIndexes.equals(firstIndexes)); // same object
        assertTrue(firstIndexes.equals(new CancelLeaveCommand(INDEX_FIRST_SHIFT, INDEX_FIRST_WORKER))); // same values
        assertFalse(firstIndexes.equals(123)); // different type
        assertFalse(firstIndexes.equals(null)); // null
        assertFalse(firstIndexes.equals(secondIndexes)); // different values
        assertFalse(firstIndexes.equals(firstShiftSecondWorker)); // different worker
        assertFalse(secondIndexes.equals(firstShiftSecondWorker)); // different shift
    }

}
