package mcscheduler.model.assignment;

import java.util.Objects;

import mcscheduler.commons.util.CollectionUtil;
import mcscheduler.model.role.Role;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.worker.Worker;

/**
 * Represents a {@code Worker}, {@code Shift} and {@code Role} Assignment in the McScheduler.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Assignment {

    // Identity fields
    private final Shift shift;
    private final Worker worker;

    // Data fields
    private final Role role;

    /**
     * Standard constructor.
     * Every field must be present and not null.
     */
    public Assignment(Shift shift, Worker worker, Role role) {
        CollectionUtil.requireAllNonNull(shift, worker, role);
        this.shift = shift;
        this.worker = worker;
        this.role = role;
    }

    /**
     * Constructor to create a dummy assignment with a null {@code Role}.
     * Every field must be present and not null.
     */
    public Assignment(Shift shift, Worker worker) {
        CollectionUtil.requireAllNonNull(shift, worker);
        this.shift = shift;
        this.worker = worker;
        this.role = null;
    }

    public Shift getShift() {
        return shift;
    }

    public Worker getWorker() {
        return worker;
    }

    public Role getRole() {
        return role;
    }

    /**
     * Returns true if both assignments involve the same shift and worker.
     */
    public boolean isSameAssignment(Assignment otherAssignment) {
        if (otherAssignment == this) {
            return true;
        }

        return otherAssignment != null
                && otherAssignment.getShift().isSameShift(getShift())
                && otherAssignment.getWorker().isSameWorker(getWorker());
    }

    /**
     * Returns true if both assignments have the same identity fields.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Assignment)) {
            return false;
        }

        Assignment otherAssignment = (Assignment) other;
        return otherAssignment.getShift().equals(getShift())
                && otherAssignment.getWorker().equals(getWorker());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(shift, worker, role);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Shift: ")
                .append(getShift().toCondensedString())
                .append(" - Worker: ")
                .append(getWorker().getName());
        if (role != null) {
            builder.append(" (Role: ")
                    .append(getRole().getRole())
                    .append(")");
        }
        return builder.toString();
    }

}
