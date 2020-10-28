package seedu.address.model.shift;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.Role;

public class RoleRequirementTest {

    private RoleRequirement roleRequirement = new RoleRequirement("cleaner 3");
    private RoleRequirement differentRoleRoleRequirement = new RoleRequirement("cashier 3");
    private RoleRequirement differentQuantityRoleRequirement = new RoleRequirement("cleaner 7");
    private RoleRequirement differentRoleRequirement = new RoleRequirement("cashier 7");

    @Test
    public void stringConstructor_inputWithSpaces_correctlyParsed() {
        String testRole = "Deep Fryer Technician 2";
        Role role = Role.createRole(testRole);
        String testQuantity = "5";
        int quantity = Integer.parseInt(testQuantity);

        assertEquals(new RoleRequirement(testRole + " " + testQuantity),
                new RoleRequirement(role, quantity));
    }

    @Test
    public void isValidRoleRequirement() {
        // null
        assertThrows(NullPointerException.class, () -> RoleRequirement.isValidRoleRequirement(null));

        // invalid role requirement
        assertFalse(RoleRequirement.isValidRoleRequirement("")); // empty
        assertFalse(RoleRequirement.isValidRoleRequirement(" ")); // whitespace
        assertFalse(RoleRequirement.isValidRoleRequirement("      ")); // lots of whitespace
        assertFalse(RoleRequirement.isValidRoleRequirement("role")); // just a role
        assertFalse(RoleRequirement.isValidRoleRequirement("123")); // just a quantity
        assertFalse(RoleRequirement.isValidRoleRequirement("role -4")); // negative quantity
        assertFalse(RoleRequirement.isValidRoleRequirement("role@$##@ 1")); // invalid role

        // valid role requirement
        assertTrue(RoleRequirement.isValidRoleRequirement("cashier 1")); // valid
        assertTrue(RoleRequirement.isValidRoleRequirement("     cashier   1")); // valid (whitespace surrounding)
        assertTrue(RoleRequirement.isValidRoleRequirement("deep fryer 4 5")); // with spaces

    }

    @Test
    public void isSameRoleRequirement() {
        // same object
        assertTrue(roleRequirement.isSameRoleRequirement(roleRequirement));

        // null
        assertFalse(roleRequirement.isSameRoleRequirement(null));

        // different role only -> returns false
        assertFalse(roleRequirement.isSameRoleRequirement(differentRoleRoleRequirement));

        // different quantity only -> returns true
        assertTrue(roleRequirement.isSameRoleRequirement(differentQuantityRoleRequirement));

        // completely different -> returns false
        assertFalse(roleRequirement.isSameRoleRequirement(differentRoleRequirement));
    }

    @Test
    public void equals() {
        // same object
        assertFalse(!roleRequirement.equals(roleRequirement));

        // same values
        assertEquals(new RoleRequirement(roleRequirement.getRole(),
                roleRequirement.getQuantity()), roleRequirement);

        // null -> returns false
        assertNotEquals(roleRequirement, null);

        // different type -> returns false
        assertNotEquals(roleRequirement, 123);

        // different any field -> returns false
        assertNotEquals(differentRoleRoleRequirement, roleRequirement); // different role only
        assertNotEquals(differentQuantityRoleRequirement, roleRequirement); // different quantity only
        assertNotEquals(differentRoleRequirement, roleRequirement); // completely different
    }

}
