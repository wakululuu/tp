package mcscheduler.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import mcscheduler.commons.exceptions.IllegalValueException;
import mcscheduler.model.shift.RoleRequirement;

/**
 * Jackson-friendly version of {@link RoleRequirement}.
 */
public class JsonAdaptedRoleRequirement {

    private final JsonAdaptedRole role;
    private final int quantityRequired;
    private final int quantityFilled;

    /**
     * Constructs a {@code JsonAdaptedRoleRequirement} with the given details
     */
    @JsonCreator
    public JsonAdaptedRoleRequirement(@JsonProperty("role") JsonAdaptedRole role,
            @JsonProperty("quantityRequired") int quantityRequired,
            @JsonProperty("quantityFilled") int quantityFilled) {
        this.role = role;
        this.quantityRequired = quantityRequired;
        this.quantityFilled = quantityFilled;
    }

    /**
     * Converts a given {@code RoleRequirement} into this class for Jackson use.
     */
    public JsonAdaptedRoleRequirement(RoleRequirement source) {
        role = new JsonAdaptedRole(source.getRole());
        quantityRequired = source.getQuantityRequired();
        quantityFilled = source.getQuantityFilled();
    }

    /**
     * Converts this Jackson-friendly adapted role requirement into the model's {@code RoleRequirement} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted role requirement.
     */
    public RoleRequirement toModelType() throws IllegalValueException {
        return new RoleRequirement(role.toModelType(), quantityRequired, quantityFilled);
    }
}
