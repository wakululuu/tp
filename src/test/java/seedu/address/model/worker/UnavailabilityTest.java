package seedu.address.model.worker;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class UnavailabilityTest {
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Unavailability(null));
    }

    @Test
    public void constructor_invalidUnavailability_throwsIllegalArgumentException() {
        String invalidUnavailability = "buirb";
        assertThrows(IllegalArgumentException.class, () -> new Unavailability(invalidUnavailability));
    }

    @Test
    public void isValidUnavailability() {
        // null unavailability
        assertThrows(NullPointerException.class, () -> Unavailability.isValidUnavailability(null));

        // invalid unavailability
        assertFalse(Unavailability.isValidUnavailability("")); // empty string
        assertFalse(Unavailability.isValidUnavailability(" ")); // spaces only
        assertFalse(Unavailability.isValidUnavailability("diub PM")); // invalid day
        assertFalse(Unavailability.isValidUnavailability("MON dhiw")); //invalid time
        assertFalse(Unavailability.isValidUnavailability("diub oub")); // invalid day and time

        // valid unavailability
        assertTrue(Unavailability.isValidUnavailability("MON PM")); // regular day and time
        assertTrue(Unavailability.isValidUnavailability("TUE FULL")); // full as time keyword
    }
}

