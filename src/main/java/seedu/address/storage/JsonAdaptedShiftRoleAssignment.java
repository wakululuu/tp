package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.ShiftRoleAssignment;

/**
 * Jackson-friendly version of {@link ShiftRoleAssignment}.
 */
public class JsonAdaptedShiftRoleAssignment {

    private final JsonAdaptedShift shift;
    private final JsonAdaptedRole role;

    /**
     * Constructs a {@code JsonAdaptedShiftRoleAssignment} with the given details.
     */
    @JsonCreator
    public JsonAdaptedShiftRoleAssignment(@JsonProperty("shift") JsonAdaptedShift shift,
            @JsonProperty("role") JsonAdaptedRole role) {
        this.shift = shift;
        this.role = role;
    }

    /**
     * Converts a given {@code ShiftRoleAssignment} into this class for Jackson use.
     */
    public JsonAdaptedShiftRoleAssignment(ShiftRoleAssignment source) {
        shift = new JsonAdaptedShift(source.getShift());
        role = new JsonAdaptedRole(source.getRole());
    }

    /**
     * Converts this Jackson-friendly adapted shift-role assignment into the model's {@code ShiftRoleAssignment}
     * object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted shift-role assignment.
     */
    public ShiftRoleAssignment toModelType() throws IllegalValueException {
        return new ShiftRoleAssignment(shift.toModelType(), role.toModelType());
    }
}
