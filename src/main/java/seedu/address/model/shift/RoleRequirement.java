package seedu.address.model.shift;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.address.model.tag.Role;

public class RoleRequirement {

    private final Role role;
    private final int quantity;

    public RoleRequirement(Role role, int quantity) {
        requireAllNonNull(role, quantity);
        this.role = role;
        this.quantity = quantity;
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
                && role.equals(((RoleRequirement) other).quantity));
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
                .append(" Quantity Required: ")
                .append(getQuantity());
        return builder.toString();
    }

}
