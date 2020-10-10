package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.shift.Shift;
import seedu.address.model.tag.Role;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;

    // Data fields
    private final Pay pay;
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();
    private final Map<Shift, Role> shifts = new HashMap<>();

    /**
     * Standard constructor, start with empty {@code shifts}. Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Pay pay, Address address, Set<Tag> tags) {
        requireAllNonNull(name, phone, pay, address, tags);
        this.name = name;
        this.phone = phone;
        this.pay = pay;
        this.address = address;
        this.tags.addAll(tags);
    }

    /**
     * Constructor with non-empty {@code shifts} for Assign and Unassign commands.
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Pay pay, Address address, Set<Tag> tags, Map<Shift, Role> shifts) {
        requireAllNonNull(name, phone, pay, address, tags, shifts);
        this.name = name;
        this.phone = phone;
        this.pay = pay;
        this.address = address;
        this.tags.addAll(tags);
        this.shifts.putAll(shifts);
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
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns an immutable shift-role map, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Map<Shift, Role> getShifts() {
        return Collections.unmodifiableMap(shifts);
    }

    /**
     * Returns true if both persons of the same name have the same phone number.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName())
                && otherPerson.getPhone().equals(getPhone());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Person)) {
            return false;
        }

        Person otherWorker = (Person) other;
        return otherWorker.getName().equals(getName())
                && otherWorker.getPhone().equals(getPhone())
                && otherWorker.getPay().equals(getPay())
                && otherWorker.getAddress().equals(getAddress())
                && otherWorker.getTags().equals(getTags())
                && otherWorker.getShifts().equals(getShifts());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, pay, address, tags, shifts);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Phone: ")
                .append(getPhone())
                .append(" Hourly pay: ")
                .append(getPay())
                .append(" Address: ")
                .append(getAddress())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        builder.append(" Shifts: ");
        getShifts().forEach((a, b) -> builder.append(a + "(" + b + ") ")); // Format: "Shift(Role) "
        return builder.toString();
    }

}
