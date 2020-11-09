package mcscheduler.model.worker;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import mcscheduler.commons.util.CollectionUtil;
import mcscheduler.model.role.Leave;
import mcscheduler.model.role.Role;
import mcscheduler.model.shift.Shift;

/**
 * Represents a Worker in the McScheduler.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Worker {

    // Identity fields
    private final Name name;
    private final Phone phone;

    // Data fields
    private final Pay pay;
    private final Address address;
    private final Set<Role> roles = new HashSet<>();
    private final Set<Unavailability> unavailableTimings = new HashSet<>();

    /**
     * Standard constructor, start with empty {@code shifts}. Every field must be present and not null.
     */
    public Worker(Name name, Phone phone, Pay pay, Address address, Set<Role> roles,
                  Set<Unavailability> unavailableTimings) {
        CollectionUtil.requireAllNonNull(name, phone, pay, address, roles, unavailableTimings);
        this.name = name;
        this.phone = phone;
        this.pay = pay;
        this.address = address;
        this.roles.addAll(roles);
        this.unavailableTimings.addAll(unavailableTimings);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Pay getPay() {
        return pay;
    }

    public Address getAddress() {
        return address;
    }

    /**
     * Returns an immutable role set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     * @return
     */
    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    /**
     * Returns an immutable availability set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Unavailability> getUnavailableTimings() {
        return Collections.unmodifiableSet(unavailableTimings);
    }

    /**
     * Returns true if the worker's role set contains the specified role.
     */
    public boolean isFitForRole(Role role) {
        if (role instanceof Leave) {
            return true;
        }
        return roles.contains(role);
    }

    /**
     * Returns true if the worker is unavailable for the specified shift.
     */
    public boolean isUnavailable(Shift shiftToAssign) {
        Set<Unavailability> workerUnavailableTimings = getUnavailableTimings();
        for (Unavailability unavailability : workerUnavailableTimings) {
            boolean hasSameDay = unavailability.getDay().equals(shiftToAssign.getShiftDay());
            boolean hasSameTime = unavailability.getTime().equals(shiftToAssign.getShiftTime());
            if (hasSameDay && hasSameTime) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if both workers of the same name have the same phone number.
     */
    public boolean isSameWorker(Worker otherWorker) {
        if (otherWorker == this) {
            return true;
        }

        return otherWorker != null
                && otherWorker.getName().equals(getName())
                && otherWorker.getPhone().equals(getPhone());
    }

    /**
     * Returns true if both workers have the same identity and data fields.
     * This defines a stronger notion of equality between two workers.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Worker)) {
            return false;
        }

        Worker otherWorker = (Worker) other;
        return otherWorker.getName().equals(getName())
                && otherWorker.getPhone().equals(getPhone())
                && otherWorker.getPay().equals(getPay())
                && otherWorker.getAddress().equals(getAddress())
                && otherWorker.getRoles().equals(getRoles())
                && otherWorker.getUnavailableTimings().equals(getUnavailableTimings());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, pay, address, roles, unavailableTimings);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Phone: ")
                .append(getPhone().toReadableString())
                .append(" Hourly pay: ")
                .append(getPay())
                .append(" Address: ")
                .append(getAddress())
                .append(" Roles: [");
        getRoles().forEach(role -> builder.append(role + ", "));
        if (getRoles().size() > 0) {
            builder.setLength(builder.length() - 2);
        }
        builder.append("]");
        builder.append(" Unavailable Timings: ");
        getUnavailableTimings().forEach(builder::append);
        return builder.toString();
    }

}
