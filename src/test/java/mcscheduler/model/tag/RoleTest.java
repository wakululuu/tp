package mcscheduler.model.tag;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static mcscheduler.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;
import mcscheduler.testutil.*;

public class RoleTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> Role.createRole(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> Role.createRole(""));
    }

    @Test
    public void isValidRoleName() {
        // null
        Assert.assertThrows(NullPointerException.class, () -> Role.isValidTagName(null));

        // invalid names
        assertFalse(Role.isValidTagName("")); // empty string
        assertFalse(Role.isValidTagName("sigh @__@")); // symbols and letters

        // valid names
        assertTrue(Role.isValidTagName("cashier")); // simple role name
        assertTrue(Role.isValidTagName("Burger Flipper")); // with space
        assertTrue(Role.isValidTagName("Level 2 Chef")); //with numbers
    }

}
