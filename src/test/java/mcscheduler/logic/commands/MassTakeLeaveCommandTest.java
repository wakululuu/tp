package mcscheduler.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mcscheduler.commons.core.Messages;
import mcscheduler.logic.commands.exceptions.CommandException;
import mcscheduler.model.McScheduler;
import mcscheduler.model.Model;
import mcscheduler.model.ModelManager;
import mcscheduler.model.UserPrefs;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.shift.ShiftDay;
import mcscheduler.model.shift.ShiftTime;
import mcscheduler.model.tag.Leave;
import mcscheduler.model.tag.Role;
import mcscheduler.testutil.Assert;
import mcscheduler.testutil.TypicalIndexes;
import mcscheduler.testutil.TypicalShifts;
import mcscheduler.testutil.TypicalWorkers;

public class MassTakeLeaveCommandTest {

    private Model model = new ModelManager(new McScheduler(), new UserPrefs());
    private ShiftDay mon = new ShiftDay(CommandTestUtil.VALID_DAY_MON);
    private ShiftDay tue = new ShiftDay(CommandTestUtil.VALID_DAY_TUE);
    private ShiftTime am = new ShiftTime(CommandTestUtil.VALID_TIME_AM);
    private ShiftTime pm = new ShiftTime(CommandTestUtil.VALID_TIME_PM);

    @BeforeEach
    public void initModel() {
        model = new ModelManager(new McScheduler(), new UserPrefs());
    }

    @Test
    public void constructor_nullInputs_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () ->
            new MassTakeLeaveCommand(null, mon, am, mon, am));
        Assert.assertThrows(NullPointerException.class, () ->
            new MassTakeLeaveCommand(TypicalIndexes.INDEX_FIRST_WORKER, null, am, mon, am));
        Assert.assertThrows(NullPointerException.class, () ->
            new MassTakeLeaveCommand(TypicalIndexes.INDEX_FIRST_WORKER, mon, null, mon, am));
        Assert.assertThrows(NullPointerException.class, () ->
            new MassTakeLeaveCommand(TypicalIndexes.INDEX_FIRST_WORKER, mon, am, null, am));
        Assert.assertThrows(NullPointerException.class, () ->
            new MassTakeLeaveCommand(TypicalIndexes.INDEX_FIRST_WORKER, mon, am, mon, null));
    }

    @Test
    public void execute_correctInputs_successWithNoLeaveOnUnavailableDayTime() throws Exception {

        model.addWorker(TypicalWorkers.BENSON);
        model.addShift(TypicalShifts.SHIFT_A);

        CommandResult result =
            new MassTakeLeaveCommand(TypicalIndexes.INDEX_FIRST_WORKER, mon, am, tue, am).execute(model);

        ModelManager expectedModel = new ModelManager(new McScheduler(), new UserPrefs());
        expectedModel.addWorker(TypicalWorkers.BENSON);
        expectedModel.addShift(TypicalShifts.SHIFT_A);
        Shift shiftMonPm = new Shift(mon, pm, Collections.emptySet());
        expectedModel.addShift(shiftMonPm);
        Shift shiftTueAm = new Shift(tue, am, Collections.emptySet());
        expectedModel.addShift(shiftTueAm);
        expectedModel.addAssignment(new Assignment(shiftMonPm, TypicalWorkers.BENSON, new Leave()));
        expectedModel.addAssignment(new Assignment(shiftTueAm, TypicalWorkers.BENSON, new Leave()));

        assertEquals(model, expectedModel);
        assertEquals(result.getFeedbackToUser(), String.format(MassTakeLeaveCommand.MESSAGE_MASS_TAKE_LEAVE_SUCCESS,
            new Shift(mon, am, Collections.emptySet()), shiftTueAm));
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Assert.assertThrows(CommandException.class, Messages.MESSAGE_INVALID_WORKER_DISPLAYED_INDEX, () ->
            new MassTakeLeaveCommand(TypicalIndexes.INDEX_FIRST_WORKER, mon, am, mon, am).execute(model));
    }

    @Test
    public void execute_someShiftHasNonLeaveAssignment_throwsCommandException() {
        Assignment assignment1 = new Assignment(
            TypicalShifts.SHIFT_A, TypicalWorkers.ALICE, Role.createRole(CommandTestUtil.VALID_ROLE_CASHIER));
        model.addWorker(TypicalWorkers.ALICE);
        model.addShift(TypicalShifts.SHIFT_A);
        model.addAssignment(assignment1);
        String expectedError1 = String.format(MassTakeLeaveCommand.MESSAGE_DUPLICATE_ASSIGNMENT, assignment1);

        // 1 assignment present
        Assert.assertThrows(CommandException.class, expectedError1, () ->
            new MassTakeLeaveCommand(TypicalIndexes.INDEX_FIRST_WORKER, mon, am, tue, pm).execute(model));

        Assignment assignment2 = new Assignment(
            TypicalShifts.SHIFT_C, TypicalWorkers.ALICE, Role.createRole(CommandTestUtil.VALID_ROLE_CASHIER));
        model.addShift(TypicalShifts.SHIFT_C);
        model.addAssignment(assignment2);
        String expectedError2 = String.format(MassTakeLeaveCommand.MESSAGE_DUPLICATE_ASSIGNMENT, assignment2);

        // 2 assignments present
        Assert.assertThrows(CommandException.class, expectedError1 + expectedError2, () ->
            new MassTakeLeaveCommand(TypicalIndexes.INDEX_FIRST_WORKER, mon, am, tue, pm).execute(model));

    }

    @Test
    public void equals() {
        MassTakeLeaveCommand massTakeLeaveCommand1 =
            new MassTakeLeaveCommand(TypicalIndexes.INDEX_FIRST_WORKER, mon, am, tue, pm);
        MassTakeLeaveCommand massTakeLeaveCommand1Copy =
            new MassTakeLeaveCommand(TypicalIndexes.INDEX_FIRST_WORKER, mon, am, tue, pm);
        MassTakeLeaveCommand massTakeLeaveCommand1AnotherCopy = new MassTakeLeaveCommand(
            TypicalIndexes.INDEX_FIRST_WORKER, new ShiftDay("MON"), new ShiftTime("AM"), new ShiftDay("TUE"),
            new ShiftTime("PM"));

        // same object
        assertEquals(massTakeLeaveCommand1, massTakeLeaveCommand1);

        // different type
        assertNotEquals(massTakeLeaveCommand1, null);
        assertNotEquals(massTakeLeaveCommand1, 123);

        // same/different values
        assertEquals(massTakeLeaveCommand1, massTakeLeaveCommand1Copy);
        assertEquals(massTakeLeaveCommand1, massTakeLeaveCommand1AnotherCopy); // not same object this time
        assertNotEquals(massTakeLeaveCommand1,
            new MassTakeLeaveCommand(TypicalIndexes.INDEX_SECOND_WORKER, mon, am, tue, pm));
        assertNotEquals(massTakeLeaveCommand1,
            new MassTakeLeaveCommand(TypicalIndexes.INDEX_FIRST_WORKER, tue, am, tue, pm));
        assertNotEquals(massTakeLeaveCommand1,
            new MassTakeLeaveCommand(TypicalIndexes.INDEX_FIRST_WORKER, mon, pm, tue, pm));
        assertNotEquals(massTakeLeaveCommand1,
            new MassTakeLeaveCommand(TypicalIndexes.INDEX_FIRST_WORKER, mon, am, mon, pm));
        assertNotEquals(massTakeLeaveCommand1,
            new MassTakeLeaveCommand(TypicalIndexes.INDEX_FIRST_WORKER, mon, am, tue, am));
    }

}
