package mcscheduler.logic.commands;

import static mcscheduler.logic.commands.CommandTestUtil.VALID_ROLE_CASHIER;
import static mcscheduler.testutil.Assert.assertThrows;
import static mcscheduler.testutil.TypicalIndexes.INDEX_FIRST_WORKER;
import static mcscheduler.testutil.TypicalIndexes.INDEX_SECOND_WORKER;
import static mcscheduler.testutil.TypicalShifts.SHIFT_A;
import static mcscheduler.testutil.TypicalShifts.SHIFT_C;
import static mcscheduler.testutil.TypicalWorkers.ALICE;
import static mcscheduler.testutil.TypicalWorkers.BENSON;
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

//@@author WangZijun97
public class MassTakeLeaveCommandTest {

    private Model model = new ModelManager(new McScheduler(), new UserPrefs());
    private final ShiftDay mon = new ShiftDay(CommandTestUtil.VALID_DAY_MON);
    private final ShiftDay tue = new ShiftDay(CommandTestUtil.VALID_DAY_TUE);
    private final ShiftTime am = new ShiftTime(CommandTestUtil.VALID_TIME_AM);
    private final ShiftTime pm = new ShiftTime(CommandTestUtil.VALID_TIME_PM);

    @BeforeEach
    public void initModel() {
        model = new ModelManager(new McScheduler(), new UserPrefs());
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
            new MassTakeLeaveCommand(INDEX_FIRST_WORKER, mon, am, null, am));
        assertThrows(NullPointerException.class, () ->
            new MassTakeLeaveCommand(INDEX_FIRST_WORKER, mon, am, mon, null));
    }

    @Test
    public void execute_correctInputs_successWithNoLeaveOnUnavailableDayTime() throws Exception {

        model.addWorker(BENSON);
        model.addShift(SHIFT_A);

        CommandResult result =
            new MassTakeLeaveCommand(INDEX_FIRST_WORKER, mon, am, tue, am).execute(model);

        ModelManager expectedModel = new ModelManager(new McScheduler(), new UserPrefs());
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
        assertThrows(CommandException.class,
                String.format(Messages.MESSAGE_INVALID_WORKER_DISPLAYED_INDEX, INDEX_FIRST_WORKER.getOneBased()), () ->
                        new MassTakeLeaveCommand(INDEX_FIRST_WORKER, mon, am, mon, am).execute(model));
    }

    @Test
    public void execute_someShiftHasNonLeaveAssignment_throwsCommandException() {
        Assignment assignment1 = new Assignment(
            SHIFT_A, ALICE, Role.createRole(VALID_ROLE_CASHIER));
        model.addWorker(ALICE);
        model.addShift(SHIFT_A);
        model.addAssignment(assignment1);
        String expectedError1 = String.format(MassTakeLeaveCommand.MESSAGE_DUPLICATE_ASSIGNMENT, assignment1);

        // 1 assignment present
        assertThrows(CommandException.class, expectedError1, () ->
            new MassTakeLeaveCommand(INDEX_FIRST_WORKER, mon, am, tue, pm).execute(model));

        Assignment assignment2 = new Assignment(
                SHIFT_C, ALICE, Role.createRole(VALID_ROLE_CASHIER));
        model.addShift(SHIFT_C);
        model.addAssignment(assignment2);
        String expectedError2 = String.format(MassTakeLeaveCommand.MESSAGE_DUPLICATE_ASSIGNMENT, assignment2);

        // 2 assignments present
        assertThrows(CommandException.class, expectedError1 + expectedError2, () ->
            new MassTakeLeaveCommand(INDEX_FIRST_WORKER, mon, am, tue, pm).execute(model));

    }

    @Test
    public void equals() {
        MassTakeLeaveCommand massTakeLeaveCommand1 =
            new MassTakeLeaveCommand(INDEX_FIRST_WORKER, mon, am, tue, pm);
        MassTakeLeaveCommand massTakeLeaveCommand1Copy =
            new MassTakeLeaveCommand(INDEX_FIRST_WORKER, mon, am, tue, pm);
        MassTakeLeaveCommand massTakeLeaveCommand1AnotherCopy = new MassTakeLeaveCommand(
            INDEX_FIRST_WORKER, new ShiftDay("MON"), new ShiftTime("AM"), new ShiftDay("TUE"),
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
            new MassTakeLeaveCommand(INDEX_SECOND_WORKER, mon, am, tue, pm));
        assertNotEquals(massTakeLeaveCommand1,
            new MassTakeLeaveCommand(INDEX_FIRST_WORKER, tue, am, tue, pm));
        assertNotEquals(massTakeLeaveCommand1,
            new MassTakeLeaveCommand(INDEX_FIRST_WORKER, mon, pm, tue, pm));
        assertNotEquals(massTakeLeaveCommand1,
            new MassTakeLeaveCommand(INDEX_FIRST_WORKER, mon, am, mon, pm));
        assertNotEquals(massTakeLeaveCommand1,
            new MassTakeLeaveCommand(INDEX_FIRST_WORKER, mon, am, tue, am));
    }

}
