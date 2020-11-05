package mcscheduler.model.worker;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import mcscheduler.testutil.Assert;

public class UnavailabilityTest {
    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Unavailability(null));
    }

    @Test
    public void constructor_invalidUnavailability_throwsIllegalArgumentException() {
        String invalidUnavailability = "buirb";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Unavailability(invalidUnavailability));
    }

    @Test
    public void isValidUnavailability() {
        // null unavailability
        Assert.assertThrows(NullPointerException.class, () -> Unavailability.isValidUnavailability(null));

        // invalid unavailability
        assertFalse(Unavailability.isValidUnavailability("")); // empty string
        assertFalse(Unavailability.isValidUnavailability(" ")); // spaces only
        assertFalse(Unavailability.isValidUnavailability("diub PM")); // invalid day
        assertFalse(Unavailability.isValidUnavailability("MON dhiw")); //invalid time
        assertFalse(Unavailability.isValidUnavailability("diub oub")); // invalid day and time

        // valid unavailability
        assertTrue(Unavailability.isValidUnavailability("MON AM")); // regular day and AM
        assertTrue(Unavailability.isValidUnavailability("MON PM")); // regular day and PM
        assertTrue(Unavailability.isValidUnavailability("MON     PM")); // empty spaces between valid day and time
    }
}

