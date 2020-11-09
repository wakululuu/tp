package mcscheduler.model.shift;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import mcscheduler.commons.util.CollectionUtil;
import mcscheduler.model.Model;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.role.Leave;
import mcscheduler.model.role.Role;

/**
 * Represents a Shift in the McScheduler.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Shift {
    public static final Integer HOURS_PER_SHIFT = 8;

    // Identity fields
    private final ShiftDay shiftDay;
    private final ShiftTime shiftTime;

    // Data fields
    private final Set<RoleRequirement> roleRequirements = new HashSet<>();

    /**
     * Standard constructor, start with empty {@code workers}. Every field must be present and not null
     */
    public Shift(ShiftDay shiftDay, ShiftTime shiftTime, Set<RoleRequirement> roleRequirements) {
        CollectionUtil.requireAllNonNull(shiftDay, shiftTime, roleRequirements);
        this.shiftDay = shiftDay;
        this.shiftTime = shiftTime;
        this.roleRequirements.addAll(roleRequirements);
    }

    public ShiftDay getShiftDay() {
        return shiftDay;
    }

    public ShiftTime getShiftTime() {
        return shiftTime;
    }

    /**
     * Returns an immutable role requirements set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<RoleRequirement> getRoleRequirements() {
        return Collections.unmodifiableSet(roleRequirements);
    }

    /**
     * Returns an immutable role set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Role> getRoles() {
        Set<Role> roles = new HashSet<>();
        this.roleRequirements.forEach(roleRequirement -> roles.add(roleRequirement.getRole()));
        return Collections.unmodifiableSet(roles);
    }

    /**
     * Returns {@code Optional} containing RoleRequirement with same Role as role after searching in
     * roleRequirements.
     */
    public Optional<RoleRequirement> findRoleRequirement(Role role) {
        requireNonNull(role);
        return roleRequirements
                .stream()
                .filter(roleRequirement -> roleRequirement.getRole().equals(role))
                .findFirst();
    }


    /**
     * Returns true if the specified {@code role} is required in the shift and has yet to be filled.
     */
    public boolean isRoleRequired(Role role) {
        if (role instanceof Leave) {
            return true;
        }
        for (RoleRequirement requirement : roleRequirements) {
            if (requirement.getRole().equals(role)) {
                return !requirement.isFilled();
            }
        }
        return false;
    }

    /**
     * Updates the quantity filled field of each role requirement within the shift.
     * Takes a list of assignments, such as from {@code Model#getFullAssignmentList}.
     *
     * @param assignments to be counted from for updating quantity filled of each role.
     */
    public void updateRoleRequirements(List<Assignment> assignments) {
        for (RoleRequirement rr : roleRequirements) {
            rr.updateQuantityFilled(assignments.stream()
                    .filter(assignment -> assignment.getShift().isSameShift(this)));
        }
    }



    /**
     * Counts the quantity filled of the specified {@code role} in the specified {@code shift}.
     *
     * @param model storing the shift to be updated.
     * @param role whose quantity filled needs to be calculated.
     */
    public int countRoleQuantityFilled(Model model, Role role) {
        List<Assignment> assignmentList = model.getFullAssignmentList();
        int quantityFilled = 0;

        for (Assignment assignment : assignmentList) {
            if (isSameShift(assignment.getShift()) && assignment.getRole().equals(role)) {
                quantityFilled++;
            }
        }

        return quantityFilled;
    }

    /**
     * Returns true if both shifts have the same identity fields.
     * This defines a weaker notion of equality between two shifts.
     */
    public boolean isSameShift(Shift otherShift) {
        if (otherShift == this) {
            return true;
        }

        return otherShift != null
                && otherShift.getShiftDay().equals(getShiftDay())
                && otherShift.getShiftTime().equals(getShiftTime());
    }

    /**
     * Returns true if both shifts have the same identity and data fields.
     * This defines a stronger notion of equality between two shifts.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Shift)) {
            return false;
        }

        Shift otherShift = (Shift) other;
        return otherShift.getShiftDay().equals(getShiftDay())
                && otherShift.getShiftTime().equals(getShiftTime())
                && otherShift.getRoleRequirements().equals(getRoleRequirements());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(shiftDay, shiftTime, roleRequirements);
    }

    /**
     * Converts the {@code Shift} object to a one-line {@code String}.
     * @return One-line string representation of shift object.
     */
    public String toCondensedString() {
        return getShiftDay() + " " + getShiftTime();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(" Day: ")
                .append(getShiftDay())
                .append(" Time: ")
                .append(getShiftTime())
                .append(" Role requirements: ");
        getRoleRequirements().forEach(builder::append);
        return builder.toString();
    }

}
