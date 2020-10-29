package mcscheduler.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import mcscheduler.commons.exceptions.IllegalValueException;
import mcscheduler.model.worker.Unavailability;

public class JsonAdaptedUnavailability {

    private final String unavailability;

    @JsonCreator
    public JsonAdaptedUnavailability(@JsonProperty("unavailability") String unavailability) {
        this.unavailability = unavailability;
    }

    public JsonAdaptedUnavailability(Unavailability source) {
        this.unavailability = source.getString();
    }

    public Unavailability toModelType() throws IllegalValueException {
        return new Unavailability(unavailability);
    }
}
