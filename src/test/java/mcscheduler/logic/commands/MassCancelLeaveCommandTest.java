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
import mcscheduler.model.role.Leave;
import mcscheduler.model.role.Role;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.shift.ShiftDay;
import mcscheduler.model.shift.ShiftTime;
import mcscheduler.testutil.Assert;
import mcscheduler.testutil.TypicalIndexes;
import mcscheduler.testutil.TypicalShifts;
import mcscheduler.testutil.TypicalWorkers;

public class MassCancelLeaveCommandTest {

    private Model model = new ModelManager(new McScheduler(), new UserPrefs());
    private ShiftDay mon = new ShiftDay(CommandTestUtil.VALID_DAY_MON);
    private ShiftTime am = new ShiftTime(CommandTestUtil.VALID_TIME_AM);
    private ShiftDay tue = new ShiftDay(CommandTestUtil.VALID_DAY_TUE);
    private ShiftTime pm = new ShiftTime(CommandTestUtil.VALID_TIME_PM);

    @BeforeEach
    public void initModel() {
        model = new ModelManager(new McScheduler(), new UserPrefs());
    }

    @Test
    public void constructor_nullInputs_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () ->
            new MassCancelLeaveCommand(null, mon, am, mon, am));
        Assert.assertThrows(NullPointerException.class, () ->
            new MassCancelLeaveCommand(TypicalIndexes.INDEX_FIRST_WORKER, null, am, mon, am));
        Assert.assertThrows(NullPointerException.class, () ->
            new MassCancelLeaveCommand(TypicalIndexes.INDEX_FIRST_WORKER, mon, null, mon, am));
        Assert.assertThrows(NullPointerException.class, () ->
            new MassCancelLeaveCommand(TypicalIndexes.INDEX_FIRST_WORKER, mon, am, null, am));
        Assert.assertThrows(NullPointerException.class, () ->
            new MassCancelLeaveCommand(TypicalIndexes.INDEX_FIRST_WORKER, mon, am, mon, null));
    }

    @Test
    public void execute_correctInputs_successfullyRemoveLeavesOnly() throws Exception {
        model.addWorker(TypicalWorkers.ALICE);
        model.addShift(TypicalShifts.SHIFT_A);
        model.addShift(TypicalShifts.SHIFT_C);
        model.addShift(TypicalShifts.SHIFT_D);
        model.addAssignment(new Assignment(
            TypicalShifts.SHIFT_A, TypicalWorkers.ALICE, Role.createRole(CommandTestUtil.VALID_ROLE_CASHIER)));
        Model expectedModel = new ModelManager(model.getMcScheduler(), model.getUserPrefs());

        // 1 leave to remove
        Assignment leave1 = new Assignment(TypicalShifts.SHIFT_C, TypicalWorkers.ALICE, new Leave());
        model.addAssignment(leave1);

        String expectedMessage1 = String.format(MassCancelLeaveCommand.MESSAGE_MASS_CANCEL_LEAVE_SUCCESS,
            new Shift(mon, am, Collections.emptySet()), new Shift(tue, pm, Collections.emptySet()));
        CommandResult commandResult1 =
            new MassCancelLeaveCommand(TypicalIndexes.INDEX_FIRST_WORKER, mon, am, tue, pm).execute(model);

        assertEquals(expectedMessage1, commandResult1.getFeedbackToUser());
        assertEquals(model, expectedModel);

        // 2 leaves to remove, flipped dates order
        Assignment leave2 = new Assignment(TypicalShifts.SHIFT_D, TypicalWorkers.ALICE, new Leave());
        model.addAssignment(leave1);
        model.addAssignment(leave2);

        String expectedMessage2 = String.format(MassCancelLeaveCommand.MESSAGE_MASS_CANCEL_LEAVE_SUCCESS,
            new Shift(tue, am, Collections.emptySet()), new Shift(mon, pm, Collections.emptySet()));
        CommandResult commandResult2 =
            new MassCancelLeaveCommand(TypicalIndexes.INDEX_FIRST_WORKER, tue, am, mon, pm).execute(model);
        assertEquals(expectedMessage2, commandResult2.getFeedbackToUser());
        assertEquals(model, expectedModel);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Assert.assertThrows(CommandException.class, Messages.MESSAGE_INVALID_WORKER_DISPLAYED_INDEX, () ->
            new MassCancelLeaveCommand(TypicalIndexes.INDEX_FIRST_WORKER, mon, am, mon, am).execute(model));
    }

    @Test
    public void execute_noLeavesInRange_throwsCommandException() {
        model.addWorker(TypicalWorkers.ALICE);
        model.addShift(TypicalShifts.SHIFT_A);
        Shift shiftMonAm = new Shift(mon, am, Collections.emptySet());
        Assert.assertThrows(CommandException.class, String.format(
            MassCancelLeaveCommand.MESSAGE_NO_LEAVE_FOUND, shiftMonAm, shiftMonAm), () ->
            new MassCancelLeaveCommand(TypicalIndexes.INDEX_FIRST_WORKER, mon, am, mon, am).execute(model));
    }

    @Test
    public void equals() {
        MassCancelLeaveCommand massCancelLeaveCommand1 = new MassCancelLeaveCommand(
            TypicalIndexes.INDEX_FIRST_WORKER, mon, am, tue, pm);
        MassCancelLeaveCommand massCancelLeaveCommand1copy = new MassCancelLeaveCommand(
            TypicalIndexes.INDEX_FIRST_WORKER, mon, am, tue, pm);

        // same object
        assertEquals(massCancelLeaveCommand1, massCancelLeaveCommand1);

        // different type
        assertNotEquals(massCancelLeaveCommand1, null);
        assertNotEquals(massCancelLeaveCommand1, 123);

        // same/different values
        assertEquals(massCancelLeaveCommand1, massCancelLeaveCommand1copy);
        assertNotEquals(massCancelLeaveCommand1,
            new MassTakeLeaveCommand(TypicalIndexes.INDEX_SECOND_WORKER, mon, am, tue, pm));
        assertNotEquals(massCancelLeaveCommand1,
            new MassTakeLeaveCommand(TypicalIndexes.INDEX_FIRST_WORKER, tue, am, tue, pm));
        assertNotEquals(massCancelLeaveCommand1,
            new MassTakeLeaveCommand(TypicalIndexes.INDEX_FIRST_WORKER, mon, pm, tue, pm));
        assertNotEquals(massCancelLeaveCommand1,
            new MassTakeLeaveCommand(TypicalIndexes.INDEX_FIRST_WORKER, mon, am, mon, pm));
        assertNotEquals(massCancelLeaveCommand1,
            new MassTakeLeaveCommand(TypicalIndexes.INDEX_FIRST_WORKER, mon, am, tue, am));
    }

}
