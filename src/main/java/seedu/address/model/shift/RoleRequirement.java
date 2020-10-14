package seedu.address.model.shift;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.address.model.tag.Role;

/**
 * Represents a Role Requirement for a shift in the App.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class RoleRequirement {

    public static final String MESSAGE_CONSTRAINTS = "Role Requirements must be of the form ROLE <space> QUANTITY "
            + "(e.g. \'Cashier 1\'). \nQuantity must be greater than zero.";

    public static final String VALIDATION_REGEX = Role.VALIDATION_REGEX + " [1-9]\\d*$";

    private final Role role;
    private final int quantity;

    /**
     * Every field must be present and not null.
     */
    public RoleRequirement(Role role, int quantity) {
        requireAllNonNull(role, quantity);
        this.role = role;
        this.quantity = quantity;
    }

    /**
     * String version constructor for easy parsing of sample data.
     */
    public RoleRequirement(String roleRequirementInfo) {
        requireNonNull(roleRequirementInfo);
        checkArgument(isValidRoleRequirement(roleRequirementInfo), MESSAGE_CONSTRAINTS);
        int index = roleRequirementInfo.lastIndexOf(" ");
        this.role = new Role(roleRequirementInfo.substring(0, index));
        this.quantity = Integer.parseInt(roleRequirementInfo.substring(index + 1));
    }

    /**
     * Returns true if a given string is a valid role requirement
     */
    public static boolean isValidRoleRequirement(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    public Role getRole() {
        return role;
    }

    public int getQuantity() {
        return quantity;
    }

    /**
     * Returns true if both role requirements are about the same role
     * This defines a weaker notion of equality between the two role requirements.
     */
    public boolean isSameRoleRequirement(RoleRequirement otherRoleRequirement) {
        if (otherRoleRequirement == this) {
            return true;
        }

        return otherRoleRequirement != null
                && otherRoleRequirement.getRole().equals(getRole());
    }

    /**
     * Returns true only if both role requirements are about the same role and of the same quantity.
     * This defines a stronger notion of equality between two role requirements.
     */
    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof RoleRequirement
                && role.equals(((RoleRequirement) other).role)
                && quantity == ((RoleRequirement) other).quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(role, quantity);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(" Role Required: ")
                .append(getRole())
                .append(" x ")
                .append(getQuantity());
        return builder.toString();
    }

}
