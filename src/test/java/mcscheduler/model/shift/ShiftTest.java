package mcscheduler.model.shift;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import mcscheduler.logic.commands.CommandTestUtil;
import mcscheduler.testutil.Assert;
import mcscheduler.testutil.ShiftBuilder;
import mcscheduler.testutil.TypicalShifts;

public class ShiftTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Shift shift = new ShiftBuilder().build();
        Assert.assertThrows(UnsupportedOperationException.class, () -> shift.getRoleRequirements().remove(0));
    }

    @Test
    public void isSameShift() {
        // null
        assertFalse(new ShiftBuilder().build().isSameShift(null));

        // same object
        assertTrue(TypicalShifts.SHIFT_A.isSameShift(TypicalShifts.SHIFT_A));

        // same content
        assertTrue(TypicalShifts.SHIFT_A.isSameShift(new ShiftBuilder(TypicalShifts.SHIFT_A).build()));

        // different day
        assertFalse(TypicalShifts.SHIFT_A.isSameShift(new ShiftBuilder(TypicalShifts.SHIFT_A).withShiftDay(
            CommandTestUtil.VALID_DAY_TUE).build()));

        // different time
        assertFalse(TypicalShifts.SHIFT_A.isSameShift(new ShiftBuilder(TypicalShifts.SHIFT_A).withShiftTime(
            CommandTestUtil.VALID_TIME_PM).build()));

        // different day and time
        assertFalse(TypicalShifts.SHIFT_A.isSameShift(
            new ShiftBuilder().withShiftDay(CommandTestUtil.VALID_DAY_TUE).withShiftTime(
                CommandTestUtil.VALID_TIME_PM).build()));

        // same day and time but different role requirements
        assertTrue(TypicalShifts.SHIFT_A.isSameShift(
            new ShiftBuilder(TypicalShifts.SHIFT_A).withRoleRequirements(CommandTestUtil.VALID_ROLE_REQUIREMENT_CHEF)
                .build()));
    }

    @Test
    public void equals() {
        // null
        assertNotEquals(TypicalShifts.SHIFT_A, null);

        // same object
        assertEquals(TypicalShifts.SHIFT_A, TypicalShifts.SHIFT_A);

        // different type
        assertNotEquals(TypicalShifts.SHIFT_A, 123);

        // same values
        assertEquals(new ShiftBuilder(TypicalShifts.SHIFT_A).build(), TypicalShifts.SHIFT_A);

        // completely different
        assertNotEquals(TypicalShifts.SHIFT_C, TypicalShifts.SHIFT_B);

        // different day
        assertNotEquals(new ShiftBuilder(TypicalShifts.SHIFT_A).withShiftDay(CommandTestUtil.VALID_DAY_TUE),
            TypicalShifts.SHIFT_A);

        // different time
        assertNotEquals(new ShiftBuilder(TypicalShifts.SHIFT_A).withShiftTime(CommandTestUtil.VALID_TIME_PM),
            TypicalShifts.SHIFT_A);

        // different role requirements
        assertNotEquals(new ShiftBuilder(TypicalShifts.SHIFT_A).withRoleRequirements(
            CommandTestUtil.VALID_ROLE_REQUIREMENT_CHEF), TypicalShifts.SHIFT_A);
    }

}
