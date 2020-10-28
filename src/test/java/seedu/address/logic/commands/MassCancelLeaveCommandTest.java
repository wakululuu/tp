package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DAY_MON;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DAY_TUE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_CASHIER;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TIME_AM;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TIME_PM;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_WORKER;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_WORKER;
import static seedu.address.testutil.TypicalShifts.SHIFT_A;
import static seedu.address.testutil.TypicalShifts.SHIFT_C;
import static seedu.address.testutil.TypicalShifts.SHIFT_D;
import static seedu.address.testutil.TypicalWorkers.ALICE;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.shift.Shift;
import seedu.address.model.shift.ShiftDay;
import seedu.address.model.shift.ShiftTime;
import seedu.address.model.tag.Leave;
import seedu.address.model.tag.Role;

public class MassCancelLeaveCommandTest {

    private Model model = new ModelManager(new AddressBook(), new UserPrefs());
    private ShiftDay mon = new ShiftDay(VALID_DAY_MON);
    private ShiftTime am = new ShiftTime(VALID_TIME_AM);
    private ShiftDay tue = new ShiftDay(VALID_DAY_TUE);
    private ShiftTime pm = new ShiftTime(VALID_TIME_PM);

    @BeforeEach
    public void initModel() {
        model = new ModelManager(new AddressBook(), new UserPrefs());
    }

    @Test
    public void constructor_nullInputs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new MassCancelLeaveCommand(null, mon, am, mon, am));
        assertThrows(NullPointerException.class, () ->
                new MassCancelLeaveCommand(INDEX_FIRST_WORKER, null, am, mon, am));
        assertThrows(NullPointerException.class, () ->
                new MassCancelLeaveCommand(INDEX_FIRST_WORKER, mon, null, mon, am));
        assertThrows(NullPointerException.class, () ->
                new MassCancelLeaveCommand(INDEX_FIRST_WORKER, mon, am, null , am));
        assertThrows(NullPointerException.class, () ->
                new MassCancelLeaveCommand(INDEX_FIRST_WORKER, mon, am, mon, null));
    }

    @Test
    public void execute_correctInputs_successfullyRemoveLeavesOnly() throws Exception {
        model.addWorker(ALICE);
        model.addShift(SHIFT_A);
        model.addShift(SHIFT_C);
        model.addShift(SHIFT_D);
        model.addAssignment(new Assignment(SHIFT_A, ALICE, Role.createRole(VALID_ROLE_CASHIER)));
        Model expectedModel = new ModelManager(model.getAddressBook(), model.getUserPrefs());

        // 1 leave to remove
        Assignment leave1 = new Assignment(SHIFT_C, ALICE, new Leave());
        model.addAssignment(leave1);

        String expectedMessage1 = String.format(MassCancelLeaveCommand.MESSAGE_MASS_CANCEL_LEAVE_SUCCESS,
                new Shift(mon, am, Collections.emptySet()), new Shift(tue, pm, Collections.emptySet()));
        CommandResult commandResult1 = new MassCancelLeaveCommand(INDEX_FIRST_WORKER, mon, am, tue, pm).execute(model);

        assertEquals(expectedMessage1, commandResult1.getFeedbackToUser());
        assertEquals(model, expectedModel);

        // 2 leaves to remove, flipped dates order
        Assignment leave2 = new Assignment(SHIFT_D, ALICE, new Leave());
        model.addAssignment(leave1);
        model.addAssignment(leave2);

        String expectedMessage2 = String.format(MassCancelLeaveCommand.MESSAGE_MASS_CANCEL_LEAVE_SUCCESS,
                new Shift(tue, am, Collections.emptySet()), new Shift(mon, pm, Collections.emptySet()));
        CommandResult commandResult2 = new MassCancelLeaveCommand(INDEX_FIRST_WORKER, tue, am, mon, pm).execute(model);
        assertEquals(expectedMessage2, commandResult2.getFeedbackToUser());
        assertEquals(model, expectedModel);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        assertThrows(CommandException.class, Messages.MESSAGE_INVALID_WORKER_DISPLAYED_INDEX, () ->
                new MassCancelLeaveCommand(INDEX_FIRST_WORKER, mon, am, mon, am).execute(model));
    }

    @Test
    public void execute_noLeavesInRange_throwsCommandException() {
        model.addWorker(ALICE);
        model.addShift(SHIFT_A);
        Shift shiftMonAm = new Shift(mon, am, Collections.emptySet());
        assertThrows(CommandException.class, String.format(
                MassCancelLeaveCommand.MESSAGE_NO_LEAVE_FOUND, shiftMonAm, shiftMonAm), () ->
                new MassCancelLeaveCommand(INDEX_FIRST_WORKER, mon, am, mon, am).execute(model));
    }

    @Test
    public void equals() {
        MassCancelLeaveCommand massCancelLeaveCommand1 = new MassCancelLeaveCommand(
                INDEX_FIRST_WORKER, mon, am, tue, pm);
        MassCancelLeaveCommand massCancelLeaveCommand1copy = new MassCancelLeaveCommand(
                INDEX_FIRST_WORKER, mon, am, tue, pm);

        // same object
        assertEquals(massCancelLeaveCommand1, massCancelLeaveCommand1);

        // different type
        assertNotEquals(massCancelLeaveCommand1, null);
        assertNotEquals(massCancelLeaveCommand1, 123);

        // same/different values
        assertEquals(massCancelLeaveCommand1, massCancelLeaveCommand1copy);
        assertNotEquals(massCancelLeaveCommand1, new MassTakeLeaveCommand(INDEX_SECOND_WORKER, mon, am, tue , pm));
        assertNotEquals(massCancelLeaveCommand1, new MassTakeLeaveCommand(INDEX_FIRST_WORKER, tue, am, tue, pm));
        assertNotEquals(massCancelLeaveCommand1, new MassTakeLeaveCommand(INDEX_FIRST_WORKER, mon, pm, tue, pm));
        assertNotEquals(massCancelLeaveCommand1, new MassTakeLeaveCommand(INDEX_FIRST_WORKER, mon, am, mon, pm));
        assertNotEquals(massCancelLeaveCommand1, new MassTakeLeaveCommand(INDEX_FIRST_WORKER, mon, am, tue, am));
    }

}
