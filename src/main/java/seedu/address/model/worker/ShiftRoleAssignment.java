package seedu.address.model.worker;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.address.model.shift.Shift;
import seedu.address.model.tag.Leave;
import seedu.address.model.tag.Role;

/**
 * Represents a ShiftRoleAssignment in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class ShiftRoleAssignment {

    // Identity fields
    private final Shift shift;
    private final Role role;

    /**
     * Every field must be present and not null.
     */
    public ShiftRoleAssignment(Shift shift, Role role) {
        requireAllNonNull(shift, role);
        this.shift = shift;
        this.role = role;
    }

    public Shift getShift() {
        return shift;
    }

    public Role getRole() {
        return role;
    }

    /**
     * Returns true if the role assignment refers to the worker taking leave.
     */
    public boolean isLeave() {
        return role instanceof Leave;
    }

    /**
     * Returns true if both shiftRoleAssignments have the same identity fields.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ShiftRoleAssignment)) {
            return false;
        }

        ShiftRoleAssignment otherAssignment = (ShiftRoleAssignment) other;
        return otherAssignment.getShift().equals(getShift())
                && otherAssignment.getRole().equals(getRole());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(shift, role);
    }

    @Override
    public String toString() {
        return getShift()
                + " Role: "
                + getRole();
    }

}
