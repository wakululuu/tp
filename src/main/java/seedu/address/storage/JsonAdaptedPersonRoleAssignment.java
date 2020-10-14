package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.shift.PersonRoleAssignment;

/**
 * Jackson-friendly version of {@link PersonRoleAssignment}.
 */
public class JsonAdaptedPersonRoleAssignment {

    private final JsonAdaptedPerson person;
    private final JsonAdaptedRole role;

    /**
     * Constructs a {@code JsonAdaptedPersonRoleAssignment} with the given details.
     */
    @JsonCreator
    public JsonAdaptedPersonRoleAssignment(@JsonProperty("person") JsonAdaptedPerson person,
            @JsonProperty("role") JsonAdaptedRole role) {
        this.person = person;
        this.role = role;
    }

    /**
     * Converts a given {@code PersonRoleAssignment} into this class for Jackson use.
     */
    public JsonAdaptedPersonRoleAssignment(PersonRoleAssignment source) {
        person = new JsonAdaptedPerson(source.getPerson());
        role = new JsonAdaptedRole(source.getRole());
    }

    /**
     * Converts this Jackson-friendly adapted person-role assignment into the model's {@code PersonRoleAssignment}
     * object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person-role assignment.
     */
    public PersonRoleAssignment toModelType() throws IllegalValueException {
        return new PersonRoleAssignment(person.toModelType(), role.toModelType());
    }
}
