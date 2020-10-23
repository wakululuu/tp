package seedu.address.model.shift;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.address.model.tag.Leave;
import seedu.address.model.tag.Role;
import seedu.address.model.worker.Worker;

/**
 * Represents a WorkerRoleAssignment in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class WorkerRoleAssignment {

    // Identity fields
    private final Worker worker;
    private final Role role;

    /**
     * Every field must be present and not null.
     */
    public WorkerRoleAssignment(Worker worker, Role role) {
        requireAllNonNull(worker, role);
        this.worker = worker;
        this.role = role;
    }

    public Worker getWorker() {
        return worker;
    }

    public Role getRole() {
        return role;
    }

    /**
     * Returns true if this assignment represents a Leave.
     */
    public boolean isLeave() {
        return role instanceof Leave;
    }

    /**
     * Returns true if both workerRoleAssignments have the same identity fields.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof WorkerRoleAssignment)) {
            return false;
        }

        WorkerRoleAssignment otherAssignment = (WorkerRoleAssignment) other;
        return otherAssignment.getWorker().equals(getWorker())
                && otherAssignment.getRole().equals(getRole());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(worker, role);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getWorker())
                .append(" Role: ")
                .append(getRole());
        return builder.toString();
    }

}
