package mcscheduler.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import mcscheduler.commons.exceptions.IllegalValueException;
import mcscheduler.model.worker.Unavailability;

public class JsonAdaptedUnavailability {

    private final String unavailability;

    /**
     * Constructs a {@code JsonAdaptedUnavailability} with the given {@code unavailability}.
     */
    @JsonCreator
    public JsonAdaptedUnavailability(@JsonProperty("unavailability") String unavailability) {
        this.unavailability = unavailability;
    }

    /**
     * Converts a given {@code Unavailability} into this class for Jackson use.
     */
    public JsonAdaptedUnavailability(Unavailability source) {
        this.unavailability = source.getString();
    }

    /**
     * Converts this Jackson-friendly adapted unavailability object into the model's {@code Unavailability} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted unavailability.
     */
    public Unavailability toModelType() throws IllegalValueException {
        if (!Unavailability.isValidUnavailability(unavailability)) {
            throw new IllegalValueException((Unavailability.MESSAGE_CONSTRAINTS));
        }
        return new Unavailability(unavailability);
    }
}
