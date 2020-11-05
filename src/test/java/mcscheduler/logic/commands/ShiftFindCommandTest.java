package mcscheduler.logic.commands;

import static mcscheduler.commons.core.Messages.MESSAGE_SHIFTS_LISTED_OVERVIEW;
import static mcscheduler.logic.commands.CommandTestUtil.assertCommandSuccess;
import static mcscheduler.testutil.TypicalShifts.SHIFT_A;
import static mcscheduler.testutil.TypicalShifts.SHIFT_B;
import static mcscheduler.testutil.TypicalShifts.SHIFT_C;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import mcscheduler.model.Model;
import mcscheduler.model.ModelManager;
import mcscheduler.model.UserPrefs;
import mcscheduler.model.shift.ShiftDayOrTimeContainsKeywordsPredicate;
import mcscheduler.testutil.McSchedulerBuilder;

public class ShiftFindCommandTest {
    private final Model model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());
    private final Model expectedModel = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());

    @Test
    public void execute_zeroKeywords_noShiftFound() {
        String expectedMessage = String.format(MESSAGE_SHIFTS_LISTED_OVERVIEW, 0);
        ShiftDayOrTimeContainsKeywordsPredicate predicate =
                new ShiftDayOrTimeContainsKeywordsPredicate(Collections.emptyList());
        ShiftFindCommand command = new ShiftFindCommand(predicate);
        expectedModel.updateFilteredShiftList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredShiftList());
    }

    @Test
    public void execute_multipleKeywords_multipleShiftsFound() {
        String expectedMessage = String.format(MESSAGE_SHIFTS_LISTED_OVERVIEW, 3);
        ShiftDayOrTimeContainsKeywordsPredicate predicate =
                new ShiftDayOrTimeContainsKeywordsPredicate(Arrays.asList("am", "frI"));
        ShiftFindCommand command = new ShiftFindCommand(predicate);
        expectedModel.updateFilteredShiftList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(SHIFT_A, SHIFT_B, SHIFT_C), model.getFilteredShiftList());
    }
}
