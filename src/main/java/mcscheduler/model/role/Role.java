package mcscheduler.model.role;

import static java.util.Objects.requireNonNull;

import mcscheduler.commons.util.AppUtil;

/**
 * Represents a Worker Role in the McScheduler.
 * Guarantees: immutable, name is valid as declared in {@link #isValidRoleName(String)}
 */
public class Role {

    public static final String MESSAGE_CONSTRAINTS = "Role names should be alphanumeric and can contain spaces\n";
    public static final String MESSAGE_MAX_CHAR_LIMIT =
            "The role name is too long, please keep to within 50 characters";
    public static final String VALIDATION_REGEX = "[\\p{Alnum} ]+";

    public final String roleName;

    protected Role(String roleName) {
        requireNonNull(roleName);
        AppUtil.checkArgument(isValidRoleName(roleName), MESSAGE_CONSTRAINTS);
        this.roleName = roleName.substring(0, 1).toUpperCase() + roleName.substring(1).toLowerCase();
    }

    /**
     * Factory method for creating a {@code Role}. Returns a {@code Leave} if {@code roleName} is {@code "Leave"}.
     */
    public static Role createRole(String roleName) {
        if (roleName.equalsIgnoreCase(Leave.ROLE_NAME)) {
            return new Leave();
        }
        return new Role(roleName);
    }

    public String getRole() {
        return roleName;
    }

    /**
     * Returns true if a given string is a valid role name.
     */
    public static boolean isValidRoleName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public int hashCode() {
        return roleName.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof Role
                && roleName.equalsIgnoreCase(((Role) other).roleName));
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return roleName;
    }

}
