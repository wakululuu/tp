package mcscheduler.model.shift;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import mcscheduler.testutil.Assert;

public class ShiftDayTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new ShiftDay(null));
    }

    @Test
    public void constructor_invalidShiftDay_throwsIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new ShiftDay(""));
    }

    @Test
    public void constructor_validShiftDay() {
        assertEquals(new ShiftDay("wed"), new ShiftDay("WED")); // non-caps
        assertEquals(new ShiftDay("sUn"), new ShiftDay("SUN")); // mix of caps and non-caps
    }

    @Test
    public void isValidDay() {
        // null
        Assert.assertThrows(NullPointerException.class, () -> ShiftDay.isValidDay(null));

        // invalid day
        assertFalse(ShiftDay.isValidDay("NOM")); //anything that isn't the seven days

        // valid day
        assertTrue(ShiftDay.isValidDay("MON")); // caps day
        assertTrue(ShiftDay.isValidDay("tue")); // non-caps day
        assertTrue(ShiftDay.isValidDay("sUn")); // mix of caps and non-caps

    }

}
