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
import static seedu.address.testutil.TypicalWorkers.ALICE;
import static seedu.address.testutil.TypicalWorkers.BENSON;

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

public class MassTakeLeaveCommandTest {

    private Model model = new ModelManager(new AddressBook(), new UserPrefs());
    private ShiftDay mon = new ShiftDay(VALID_DAY_MON);
    private ShiftDay tue = new ShiftDay(VALID_DAY_TUE);
    private ShiftTime am = new ShiftTime(VALID_TIME_AM);
    private ShiftTime pm = new ShiftTime(VALID_TIME_PM);

    @BeforeEach
    public void initModel() {
        model = new ModelManager(new AddressBook(), new UserPrefs());
    }

    @Test
    public void constructor_nullInputs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new MassTakeLeaveCommand(null, mon, am, mon, am));
        assertThrows(NullPointerException.class, () ->
                new MassTakeLeaveCommand(INDEX_FIRST_WORKER, null, am, mon, am));
        assertThrows(NullPointerException.class, () ->
                new MassTakeLeaveCommand(INDEX_FIRST_WORKER, mon, null, mon, am));
        assertThrows(NullPointerException.class, () ->
                new MassTakeLeaveCommand(INDEX_FIRST_WORKER, mon, am, null , am));
        assertThrows(NullPointerException.class, () ->
                new MassTakeLeaveCommand(INDEX_FIRST_WORKER, mon, am, mon, null));
    }

    @Test
    public void execute_correctInputs_successWithNoLeaveOnUnavailableDayTime() throws Exception {

        model.addWorker(BENSON);
        model.addShift(SHIFT_A);

        CommandResult result = new MassTakeLeaveCommand(INDEX_FIRST_WORKER, mon, am, tue, am).execute(model);

        ModelManager expectedModel = new ModelManager(new AddressBook(), new UserPrefs());
        expectedModel.addWorker(BENSON);
        expectedModel.addShift(SHIFT_A);
        Shift shiftMonPm = new Shift(mon, pm, Collections.emptySet());
        expectedModel.addShift(shiftMonPm);
        Shift shiftTueAm = new Shift(tue, am, Collections.emptySet());
        expectedModel.addShift(shiftTueAm);
        expectedModel.addAssignment(new Assignment(shiftMonPm, BENSON, new Leave()));
        expectedModel.addAssignment(new Assignment(shiftTueAm, BENSON, new Leave()));

        assertEquals(model, expectedModel);
        assertEquals(result.getFeedbackToUser(), String.format(MassTakeLeaveCommand.MESSAGE_MASS_TAKE_LEAVE_SUCCESS,
                new Shift(mon, am, Collections.emptySet()), shiftTueAm));
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        assertThrows(CommandException.class, Messages.MESSAGE_INVALID_WORKER_DISPLAYED_INDEX, () ->
                new MassTakeLeaveCommand(INDEX_FIRST_WORKER, mon, am, mon, am).execute(model));
    }

    @Test
    public void execute_someShiftHasNonLeaveAssignment_throwsCommandException() {
        Assignment assignment1 = new Assignment(SHIFT_A, ALICE, Role.createRole(VALID_ROLE_CASHIER));
        model.addWorker(ALICE);
        model.addShift(SHIFT_A);
        model.addAssignment(assignment1);
        String expectedError1 = String.format(MassTakeLeaveCommand.MESSAGE_DUPLICATE_ASSIGNMENT, assignment1);

        // 1 assignment present
        assertThrows(CommandException.class, expectedError1, () ->
                new MassTakeLeaveCommand(INDEX_FIRST_WORKER, mon, am, tue, pm).execute(model));

        Assignment assignment2 = new Assignment(SHIFT_C, ALICE, Role.createRole(VALID_ROLE_CASHIER));
        model.addShift(SHIFT_C);
        model.addAssignment(assignment2);
        String expectedError2 = String.format(MassTakeLeaveCommand.MESSAGE_DUPLICATE_ASSIGNMENT, assignment2);

        // 2 assignments present
        assertThrows(CommandException.class, expectedError1 + expectedError2, () ->
                new MassTakeLeaveCommand(INDEX_FIRST_WORKER, mon, am, tue, pm).execute(model));

    }

    @Test
    public void equals() {
        MassTakeLeaveCommand massTakeLeaveCommand1 = new MassTakeLeaveCommand(INDEX_FIRST_WORKER, mon, am, tue, pm);
        MassTakeLeaveCommand massTakeLeaveCommand1copy = new MassTakeLeaveCommand(INDEX_FIRST_WORKER, mon, am, tue, pm);

        // same object
        assertEquals(massTakeLeaveCommand1, massTakeLeaveCommand1);

        // different type
        assertNotEquals(massTakeLeaveCommand1, null);
        assertNotEquals(massTakeLeaveCommand1, 123);

        // same/different values
        assertEquals(massTakeLeaveCommand1, massTakeLeaveCommand1copy);
        assertNotEquals(massTakeLeaveCommand1, new MassTakeLeaveCommand(INDEX_SECOND_WORKER, mon, am, tue , pm));
        assertNotEquals(massTakeLeaveCommand1, new MassTakeLeaveCommand(INDEX_FIRST_WORKER, tue, am, tue, pm));
        assertNotEquals(massTakeLeaveCommand1, new MassTakeLeaveCommand(INDEX_FIRST_WORKER, mon, pm, tue, pm));
        assertNotEquals(massTakeLeaveCommand1, new MassTakeLeaveCommand(INDEX_FIRST_WORKER, mon, am, mon, pm));
        assertNotEquals(massTakeLeaveCommand1, new MassTakeLeaveCommand(INDEX_FIRST_WORKER, mon, am, tue, am));
    }

}
