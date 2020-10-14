package seedu.address.model.shift;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.address.model.person.Person;
import seedu.address.model.tag.Role;

/**
 * Represents a PersonRoleAssignment in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class PersonRoleAssignment {

    // Identity fields
    private final Person person;
    private final Role role;

    /**
     * Every field must be present and not null.
     */
    public PersonRoleAssignment(Person person, Role role) {
        requireAllNonNull(person, role);
        this.person = person;
        this.role = role;
    }

    public Person getPerson() {
        return person;
    }

    public Role getRole() {
        return role;
    }

    /**
     * Returns true if both personRoleAssignments have the same identity fields.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof PersonRoleAssignment)) {
            return false;
        }

        PersonRoleAssignment otherAssignment = (PersonRoleAssignment) other;
        return otherAssignment.getPerson().equals(getPerson())
                && otherAssignment.getRole().equals(getRole());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(person, role);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getPerson())
                .append(" Role: ")
                .append(getRole());
        return builder.toString();
    }

}
