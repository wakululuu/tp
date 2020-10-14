package seedu.address.model.shift;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class ShiftTimeTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ShiftTime(null));
    }

    @Test
    public void constructor_invalidShiftTime_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ShiftTime(""));
    }

    @Test
    public void constructor_validShiftTime() {
        assertEquals(new ShiftTime("am"), new ShiftTime("AM")); // small caps equal to big caps
        assertEquals(new ShiftTime("pM"), new ShiftTime("PM")); // mixed caps no difference
    }

    @Test
    public void isValidTime() {
        // null
        assertThrows(NullPointerException.class, () -> ShiftTime.isValidTime(null));

        // invalid time
        assertFalse(ShiftTime.isValidTime("mp")); // not AM, PM
        assertFalse(ShiftTime.isValidTime("pM")); // mix caps and small caps
        assertFalse(ShiftTime.isValidTime("am")); // small caps

        // valid time
        assertTrue(ShiftTime.isValidTime("PM")); // caps time
    }

}
