package mcscheduler.model.role;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import mcscheduler.testutil.Assert;

public class RoleTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> Role.createRole(null));
    }

    @Test
    public void constructor_invalidRoleName_throwsIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> Role.createRole(""));
    }

    @Test
    public void isValidRoleName() {
        // null
        Assert.assertThrows(NullPointerException.class, () -> Role.isValidRoleName(null));

        // invalid names
        assertFalse(Role.isValidRoleName("")); // empty string
        assertFalse(Role.isValidRoleName("sigh @__@")); // symbols and letters

        // valid names
        assertTrue(Role.isValidRoleName("cashier")); // simple role name
        assertTrue(Role.isValidRoleName("Burger Flipper")); // with space
        assertTrue(Role.isValidRoleName("Level 2 Chef")); //with numbers
    }

}
