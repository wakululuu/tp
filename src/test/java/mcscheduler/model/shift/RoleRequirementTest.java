package mcscheduler.model.shift;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import mcscheduler.model.role.Role;
import mcscheduler.testutil.Assert;

//@@author WangZijun97
public class RoleRequirementTest {

    private static final RoleRequirement ROLE_REQUIREMENT = new RoleRequirement("cleaner 3 0");
    private static final RoleRequirement DIFFERENT_ROLE_ROLE_REQUIREMENT = new RoleRequirement("cashier 3 0");
    private static final RoleRequirement DIFFERENT_QUANTITY_REQUIRED_ROLE_REQUIREMENT =
            new RoleRequirement("cleaner 7 0");
    private static final RoleRequirement DIFFERENT_QUANTITY_FILLED_ROLE_REQUIREMENT =
            new RoleRequirement("cleaner 3 1");
    private static final RoleRequirement DIFFERENT_ROLE_REQUIREMENT = new RoleRequirement("cashier 7 2");

    @Test
    public void stringConstructor_inputWithSpaces_correctlyParsed() {
        String testRole = "Deep Fryer Technician 2";
        Role role = Role.createRole(testRole);
        String testQuantityRequired = "5";
        int quantityRequired = Integer.parseInt(testQuantityRequired);
        String testQuantityFilled = "0";
        int quantityFilled = Integer.parseInt(testQuantityFilled);

        assertEquals(new RoleRequirement(testRole + " " + testQuantityRequired + " " + testQuantityFilled),
            new RoleRequirement(role, quantityRequired, quantityFilled));
    }

    @Test
    public void isValidRoleRequirement() {
        // null
        Assert.assertThrows(NullPointerException.class, () -> RoleRequirement.isValidRoleRequirement(null));

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
        assertTrue(ROLE_REQUIREMENT.isSameRoleRequirement(ROLE_REQUIREMENT));

        // null
        assertFalse(ROLE_REQUIREMENT.isSameRoleRequirement(null));

        // different role only -> returns false
        assertFalse(ROLE_REQUIREMENT.isSameRoleRequirement(DIFFERENT_ROLE_ROLE_REQUIREMENT));

        // different quantity required only -> returns false
        assertFalse(ROLE_REQUIREMENT.isSameRoleRequirement(DIFFERENT_QUANTITY_REQUIRED_ROLE_REQUIREMENT));

        // different quantity filled only -> returns true
        assertTrue(ROLE_REQUIREMENT.isSameRoleRequirement(DIFFERENT_QUANTITY_FILLED_ROLE_REQUIREMENT));

        // completely different -> returns false
        assertFalse(ROLE_REQUIREMENT.isSameRoleRequirement(DIFFERENT_ROLE_REQUIREMENT));
    }

    @Test
    public void equals() {
        // same object
        assertFalse(!ROLE_REQUIREMENT.equals(ROLE_REQUIREMENT));

        // same values
        assertEquals(new RoleRequirement(ROLE_REQUIREMENT.getRole(),
            ROLE_REQUIREMENT.getQuantityRequired()), ROLE_REQUIREMENT);

        // null -> returns false
        assertNotEquals(ROLE_REQUIREMENT, null);

        // different type -> returns false
        assertNotEquals(ROLE_REQUIREMENT, 123);

        // different any field -> returns false
        assertNotEquals(DIFFERENT_ROLE_ROLE_REQUIREMENT, ROLE_REQUIREMENT); // different role only
        assertNotEquals(DIFFERENT_QUANTITY_REQUIRED_ROLE_REQUIREMENT, ROLE_REQUIREMENT); // different quantity only
        assertNotEquals(DIFFERENT_ROLE_REQUIREMENT, ROLE_REQUIREMENT); // completely different
    }

}
