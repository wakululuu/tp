package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Role;

/**
 * Jackson-friendly version of {@link Role}.
 */
class JsonAdaptedRole extends JsonAdaptedTag {

    /**
     * Constructs a {@code JsonAdaptedRole} with the given {@code roleName}.
     */
    @JsonCreator
    public JsonAdaptedRole(String roleName) {
        super(roleName);
    }

    /**
     * Converts a given {@code Role} into this class for Jackson use.
     * Note that {@code Role} objects are also {@code Tag} objects.
     */
    public JsonAdaptedRole(Role source) {
        super(source);
    }

    /**
     * Converts this Jackson-friendly adapted role object into the model's {@code Role} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted tag.
     */
    @Override
    public Role toModelType() throws IllegalValueException {
        if (!Role.isValidTagName(tagName)) {
            throw new IllegalValueException((Role.MESSAGE_CONSTRAINTS));
        }
        return Role.createRole(tagName);
    }

}
