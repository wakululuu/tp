package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.shift.RoleRequirement;

/**
 * Jackson-friendly version of {@link RoleRequirement}.
 */
public class JsonAdaptedRoleRequirement {

    private final JsonAdaptedRole role;
    private final int quantity;

    /**
     * Constructs a {@code JsonAdaptedRoleRequirement} with the given details
     */
    @JsonCreator
    public JsonAdaptedRoleRequirement(@JsonProperty("role") JsonAdaptedRole role,
                                      @JsonProperty("quantity") int quantity) {
        this.role = role;
        this.quantity = quantity;
    }

    /**
     * Converts a given {@code RoleRequirement} into this class for Jackson use.
     */
    public JsonAdaptedRoleRequirement(RoleRequirement source) {
        role = new JsonAdaptedRole(source.getRole());
        quantity = source.getQuantity();
    }

    /**
     * Converts this Jackson-friendly adapted role requirement into the model's {@code RoleRequirement} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted role requirement.
     */
    public RoleRequirement toModelType() throws IllegalValueException {
        return new RoleRequirement(role.toModelType(), quantity);
    }
}
