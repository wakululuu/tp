package seedu.address.model.shift;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DAY_TUE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_REQUIREMENT_CHEF;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TIME_PM;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalShifts.SHIFT_A;
import static seedu.address.testutil.TypicalShifts.SHIFT_B;
import static seedu.address.testutil.TypicalShifts.SHIFT_C;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.ShiftBuilder;

public class ShiftTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Shift shift = new ShiftBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> shift.getRoleRequirements().remove(0));
    }

    @Test
    public void isSameShift() {
        // null
        assertFalse(new ShiftBuilder().build().isSameShift(null));

        // same object
        assertTrue(SHIFT_A.isSameShift(SHIFT_A));

        // same content
        assertTrue(SHIFT_A.isSameShift(new ShiftBuilder(SHIFT_A).build()));

        // different day
        assertFalse(SHIFT_A.isSameShift(new ShiftBuilder(SHIFT_A).withShiftDay(VALID_DAY_TUE).build()));

        // different time
        assertFalse(SHIFT_A.isSameShift(new ShiftBuilder(SHIFT_A).withShiftTime(VALID_TIME_PM).build()));

        // different day and time
        assertFalse(SHIFT_A.isSameShift(
                new ShiftBuilder().withShiftDay(VALID_DAY_TUE).withShiftTime(VALID_TIME_PM).build()));

        // same day and time but different role requirements
        assertTrue(SHIFT_A.isSameShift(
                new ShiftBuilder(SHIFT_A).withRoleRequirements(VALID_ROLE_REQUIREMENT_CHEF).build()));
    }

    @Test
    public void equals() {
        // null
        assertNotEquals(SHIFT_A, null);

        // same object
        assertEquals(SHIFT_A, SHIFT_A);

        // different type
        assertNotEquals(SHIFT_A, 123);

        // same values
        assertEquals(new ShiftBuilder(SHIFT_A).build(), SHIFT_A);

        // completely different
        assertNotEquals(SHIFT_C, SHIFT_B);

        // different day
        assertNotEquals(new ShiftBuilder(SHIFT_A).withShiftDay(VALID_DAY_TUE), SHIFT_A);

        // different time
        assertNotEquals(new ShiftBuilder(SHIFT_A).withShiftTime(VALID_TIME_PM), SHIFT_A);

        // different role requirements
        assertNotEquals(new ShiftBuilder(SHIFT_A).withRoleRequirements(VALID_ROLE_REQUIREMENT_CHEF), SHIFT_A);
    }

}
