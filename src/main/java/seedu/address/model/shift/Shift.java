package seedu.address.model.shift;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.Model;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.tag.Role;

/**
 * Represents a Shift in the App.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Shift {

    // Identity fields
    private final ShiftDay shiftDay;
    private final ShiftTime shiftTime;

    // Data fields
    private final Set<RoleRequirement> roleRequirements = new HashSet<>();

    /**
     * Standard constructor, start with empty {@code workers}. Every field must be present and not null
     */
    public Shift(ShiftDay shiftDay, ShiftTime shiftTime, Set<RoleRequirement> roleRequirements) {
        requireAllNonNull(shiftDay, shiftTime, roleRequirements);
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
     * Returns true if the specified {@code role} is required in the shift and has yet to be filled.
     */
    public boolean isRoleRequired(Role role) {
        for (RoleRequirement requirement : roleRequirements) {
            if (requirement.getRole().equals(role)) {
                return !requirement.isFilled();
            }
        }
        return false;
    }

    /**
     * Updates the quantity of the specified {@code role} that is filled in the specified {@code shift}.
     *
     * @param model storing the shift to be updated.
     * @param shiftToUpdate in the model.
     * @param role of the role requirement to be updated.
     */
    public static void updateRoleRequirements(Model model, Shift shiftToUpdate, Role role) {
        requireAllNonNull(model, shiftToUpdate, role);
        int quantityFilled = countRoleQuantityFilled(model, shiftToUpdate, role);
        Set<RoleRequirement> updatedRoleRequirements = getUpdatedRoleRequirements(shiftToUpdate, role, quantityFilled);

        Shift updatedShift = new Shift(shiftToUpdate.getShiftDay(), shiftToUpdate.getShiftTime(),
                updatedRoleRequirements);
        model.setShift(shiftToUpdate, updatedShift);
    }

    /**
     * Counts the quantity filled of the specified {@code role} in the specified {@code shift}.
     *
     * @param model storing the shift to be updated.
     * @param shiftToUpdate in the model.
     * @param role whose quantity filled needs to be calculated.
     */
    public static int countRoleQuantityFilled(Model model, Shift shiftToUpdate, Role role) {
        List<Assignment> assignmentList = model.getFullAssignmentList();
        int quantityFilled = 0;

        for (Assignment assignment : assignmentList) {
            if (assignment.getShift().isSameShift(shiftToUpdate) && assignment.getRole().equals(role)) {
                quantityFilled++;
            }
        }

        return quantityFilled;
    }

    private static Set<RoleRequirement> getUpdatedRoleRequirements(Shift shiftToUpdate, Role role, int quantityFilled) {
        Set<RoleRequirement> updatedRoleRequirements = new HashSet<>(shiftToUpdate.getRoleRequirements());

        for (RoleRequirement requirement : updatedRoleRequirements) {
            if (requirement.getRole().equals(role)) {
                RoleRequirement updatedRoleRequirement = new RoleRequirement(role, requirement.getQuantityRequired(),
                        quantityFilled);
                assert updatedRoleRequirement.getQuantityFilled() <= updatedRoleRequirement.getQuantityRequired();

                updatedRoleRequirements.remove(requirement);
                updatedRoleRequirements.add(updatedRoleRequirement);
                break;
            }
        }

        return updatedRoleRequirements;
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
