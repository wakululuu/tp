package seedu.address.model.shift;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.person.Person;
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
    private final Map<Person, Role> workers = new HashMap<>();

    /**
     * Standard constructor, start with empty {@code workers}. Every field must be present and not null
     */
    public Shift(ShiftDay shiftDay, ShiftTime shiftTime, Set<RoleRequirement> roleRequirements) {
        requireAllNonNull(shiftDay, shiftTime, roleRequirements);
        this.shiftDay = shiftDay;
        this.shiftTime = shiftTime;
        this.roleRequirements.addAll(roleRequirements);
    }

    /**
     * Constructor with non-empty {@code workers} for Assign and Unassign commands.
     * Every field must be present and not null.
     */
    public Shift(ShiftDay shiftDay, ShiftTime shiftTime, Set<RoleRequirement> roleRequirements,
            Map<Person, Role> workers) {
        requireAllNonNull(shiftDay, shiftTime, roleRequirements);
        this.shiftDay = shiftDay;
        this.shiftTime = shiftTime;
        this.roleRequirements.addAll(roleRequirements);
        this.workers.putAll(workers);
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

    public Map<Person, Role> getWorkers() {
        return workers;
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

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(" Day: ")
                .append(getShiftDay())
                .append(" Time: ")
                .append(getShiftTime());
        getRoleRequirements().forEach(builder::append);
        return builder.toString();
    }

}
