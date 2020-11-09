package mcscheduler.logic.commands;

import static mcscheduler.logic.commands.CommandTestUtil.VALID_DAY_MON;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_DAY_TUE;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_ROLE_CASHIER;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_TIME_AM;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_TIME_PM;
import static mcscheduler.testutil.Assert.assertThrows;
import static mcscheduler.testutil.TypicalIndexes.INDEX_FIRST_WORKER;
import static mcscheduler.testutil.TypicalIndexes.INDEX_SECOND_WORKER;
import static mcscheduler.testutil.TypicalShifts.SHIFT_A;
import static mcscheduler.testutil.TypicalShifts.SHIFT_C;
import static mcscheduler.testutil.TypicalShifts.SHIFT_D;
import static mcscheduler.testutil.TypicalWorkers.ALICE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mcscheduler.logic.commands.exceptions.CommandException;
import mcscheduler.model.McScheduler;
import mcscheduler.model.Model;
import mcscheduler.model.ModelManager;
import mcscheduler.model.UserPrefs;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.role.Leave;
import mcscheduler.model.role.Role;
import mcscheduler.model.shift.ShiftDay;
import mcscheduler.model.shift.ShiftTime;

//@@author WangZijun97
public class MassCancelLeaveCommandTest {

    private Model model = new ModelManager(new McScheduler(), new UserPrefs());
    private final ShiftDay mon = new ShiftDay(VALID_DAY_MON);
    private final ShiftTime am = new ShiftTime(VALID_TIME_AM);
    private final ShiftDay tue = new ShiftDay(VALID_DAY_TUE);
    private final ShiftTime pm = new ShiftTime(VALID_TIME_PM);

    @BeforeEach
    public void initModel() {
        model = new ModelManager(new McScheduler(), new UserPrefs());
    }

    @Test
    public void constructor_nullInputs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new MassCancelLeaveCommand(null, mon, am, mon, am));
        assertThrows(NullPointerException.class, () ->
                new MassCancelLeaveCommand(INDEX_FIRST_WORKER, null, am, mon, am));
        assertThrows(NullPointerException.class, () ->
                new MassCancelLeaveCommand(INDEX_FIRST_WORKER, mon, null, mon, am));
        assertThrows(NullPointerException.class, () ->
                new MassCancelLeaveCommand(INDEX_FIRST_WORKER, mon, am, null, am));
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
        Model expectedModel = new ModelManager(model.getMcScheduler(), model.getUserPrefs());

        // 1 leave to remove
        Assignment leave1 = new Assignment(SHIFT_C, ALICE, new Leave());
        model.addAssignment(leave1);

        String expectedMessage1 = String.format(MassCancelLeaveCommand.MESSAGE_MASS_CANCEL_LEAVE_SUCCESS,
                mon, am, tue, pm, ALICE.getName());
        CommandResult commandResult1 = new MassCancelLeaveCommand(INDEX_FIRST_WORKER, mon, am, tue, pm).execute(model);

        assertEquals(expectedMessage1, commandResult1.getFeedbackToUser());
        assertEquals(model, expectedModel);

        // 2 leaves to remove, flipped dates order
        Assignment leave2 = new Assignment(SHIFT_D, ALICE, new Leave());
        model.addAssignment(leave1);
        model.addAssignment(leave2);

        String expectedMessage2 = String.format(MassCancelLeaveCommand.MESSAGE_MASS_CANCEL_LEAVE_SUCCESS,
                tue, am, mon, pm, ALICE.getName());
        CommandResult commandResult2 = new MassCancelLeaveCommand(INDEX_FIRST_WORKER, tue, am, mon, pm).execute(model);
        assertEquals(expectedMessage2, commandResult2.getFeedbackToUser());
        assertEquals(model, expectedModel);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        assertThrows(CommandException.class,
                CommandUtil.printOutOfBoundsWorkerIndexError(
                        INDEX_FIRST_WORKER, MassCancelLeaveCommand.MESSAGE_USAGE), () ->
                new MassCancelLeaveCommand(INDEX_FIRST_WORKER, mon, am, mon, am).execute(model));
    }

    @Test
    public void execute_noLeavesInRange_throwsCommandException() {
        model.addWorker(ALICE);
        model.addShift(SHIFT_A);
        assertThrows(CommandException.class,
                String.format(MassCancelLeaveCommand.MESSAGE_NO_LEAVE_FOUND, mon, am, mon, am), () ->
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
        assertNotEquals(massCancelLeaveCommand1, new MassTakeLeaveCommand(INDEX_SECOND_WORKER, mon, am, tue, pm));
        assertNotEquals(massCancelLeaveCommand1, new MassTakeLeaveCommand(INDEX_FIRST_WORKER, tue, am, tue, pm));
        assertNotEquals(massCancelLeaveCommand1, new MassTakeLeaveCommand(INDEX_FIRST_WORKER, mon, pm, tue, pm));
        assertNotEquals(massCancelLeaveCommand1, new MassTakeLeaveCommand(INDEX_FIRST_WORKER, mon, am, mon, pm));
        assertNotEquals(massCancelLeaveCommand1, new MassTakeLeaveCommand(INDEX_FIRST_WORKER, mon, am, tue, am));
    }
}
