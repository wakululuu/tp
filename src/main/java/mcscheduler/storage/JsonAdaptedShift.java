package mcscheduler.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import mcscheduler.commons.exceptions.IllegalValueException;
import mcscheduler.model.shift.RoleRequirement;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.shift.ShiftDay;
import mcscheduler.model.shift.ShiftTime;

/**
 * Jackson-friendly version of {@link Shift}
 */
public class JsonAdaptedShift {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Shift's %s field is missing!";

    private final String day;
    private final String time;
    private final List<JsonAdaptedRoleRequirement> roleRequirements = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedShift} with the given shift information.
     */
    @JsonCreator
    public JsonAdaptedShift(@JsonProperty("day") String day, @JsonProperty("time") String time,
            @JsonProperty("roleRequirements") List<JsonAdaptedRoleRequirement> roleRequirements) {
        this.day = day;
        this.time = time;
        if (roleRequirements != null) {
            this.roleRequirements.addAll(roleRequirements);
        }
    }

    /**
     * Converts a given {@code Shift} into this class for Jackson use.
     */
    public JsonAdaptedShift(Shift source) {
        day = source.getShiftDay().toString();
        time = source.getShiftTime().toString();
        roleRequirements.addAll(source.getRoleRequirements().stream()
                .map(JsonAdaptedRoleRequirement::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this shift into the models' {@code Shift} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public Shift toModelType() throws IllegalValueException {
        if (day == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, ShiftDay.class.getSimpleName()));
        }
        if (!ShiftDay.isValidDay(day)) {
            throw new IllegalValueException(ShiftDay.MESSAGE_CONSTRAINTS);
        }
        final ShiftDay modelDay = new ShiftDay(day);

        if (time == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, ShiftTime.class.getSimpleName()));
        }
        if (!ShiftTime.isValidTime(time)) {
            throw new IllegalValueException((ShiftTime.MESSAGE_CONSTRAINTS));
        }
        final ShiftTime modelTime = new ShiftTime(time);

        final List<RoleRequirement> roleRequirementsBuilder = new ArrayList<>();
        for (JsonAdaptedRoleRequirement req : roleRequirements) {
            roleRequirementsBuilder.add(req.toModelType());
        }
        final Set<RoleRequirement> modelRoleRequirements = new HashSet<>(roleRequirementsBuilder);

        return new Shift(modelDay, modelTime, modelRoleRequirements);
    }

}
