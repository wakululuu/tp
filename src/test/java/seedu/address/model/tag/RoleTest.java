package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class RoleTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Role(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Role(""));
    }

    @Test
    public void isValidRoleName() {
        // null
        assertThrows(NullPointerException.class, () -> Role.isValidTagName(null));

        // invalid names
        assertFalse(Role.isValidTagName("")); // empty string
        assertFalse(Role.isValidTagName("sigh @__@")); // symbols and letters

        // valid names
        assertTrue(Role.isValidTagName("cashier")); // simple role name
        assertTrue(Role.isValidTagName("Burger Flipper")); // with space
        assertTrue(Role.isValidTagName("Level 2 Chef")); //with numbers
    }

}
