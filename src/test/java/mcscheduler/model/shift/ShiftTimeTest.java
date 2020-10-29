package mcscheduler.model.shift;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import mcscheduler.testutil.Assert;

public class ShiftTimeTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new ShiftTime(null));
    }

    @Test
    public void constructor_invalidShiftTime_throwsIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new ShiftTime(""));
    }

    @Test
    public void constructor_validShiftTime() {
        assertEquals(new ShiftTime("am"), new ShiftTime("AM")); // non-caps equal to caps
        assertEquals(new ShiftTime("pM"), new ShiftTime("PM")); // mixed cases no difference
    }

    @Test
    public void isValidTime() {
        // null
        Assert.assertThrows(NullPointerException.class, () -> ShiftTime.isValidTime(null));

        // invalid time
        assertFalse(ShiftTime.isValidTime("mp")); // not AM, PM

        // valid time
        assertTrue(ShiftTime.isValidTime("PM")); // caps time
        assertTrue(ShiftTime.isValidTime("am")); // non-caps
        assertTrue(ShiftTime.isValidTime("pM")); // mix caps and non-caps
    }

}
