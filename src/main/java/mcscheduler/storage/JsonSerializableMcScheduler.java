package mcscheduler.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import mcscheduler.commons.exceptions.IllegalValueException;
import mcscheduler.model.McScheduler;
import mcscheduler.model.ReadOnlyMcScheduler;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.role.Role;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.worker.Worker;

/**
 * An Immutable McScheduler that is serializable to JSON format.
 */
@JsonRootName(value = "mcscheduler")
class JsonSerializableMcScheduler {

    public static final String MESSAGE_DUPLICATE_WORKER = "Workers list contains duplicate worker(s).";
    public static final String MESSAGE_DUPLICATE_SHIFT = "Shifts list contains duplicate shift(s).";
    public static final String MESSAGE_DUPLICATE_ASSIGNMENT = "Assignments list contains duplicate assignment(s).";
    public static final String MESSAGE_DUPLICATE_ROLE = "Roles list contains duplicate role(s).";

    private final List<JsonAdaptedWorker> workers = new ArrayList<>();
    private final List<JsonAdaptedShift> shifts = new ArrayList<>();
    private final List<JsonAdaptedAssignment> assignments = new ArrayList<>();
    private final List<JsonAdaptedRole> validRoles = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableMcScheduler} with the given workers, shifts, assignments and valid roles.
     */
    @JsonCreator
    public JsonSerializableMcScheduler(@JsonProperty("workers") List<JsonAdaptedWorker> workers,
            @JsonProperty("shifts") List<JsonAdaptedShift> shifts,
            @JsonProperty("assignments") List<JsonAdaptedAssignment> assignments,
            @JsonProperty("validRoles") List<JsonAdaptedRole> validRoles) {
        this.workers.addAll(workers);
        this.shifts.addAll(shifts);
        this.assignments.addAll(assignments);
        this.validRoles.addAll(validRoles);
    }

    /**
     * Converts a given {@code ReadOnlyMcScheduler} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableMcScheduler}.
     */
    public JsonSerializableMcScheduler(ReadOnlyMcScheduler source) {
        workers.addAll(source.getWorkerList().stream().map(JsonAdaptedWorker::new).collect(Collectors.toList()));
        shifts.addAll(source.getShiftList().stream().map(JsonAdaptedShift::new).collect(Collectors.toList()));
        assignments.addAll(source.getAssignmentList().stream().map(JsonAdaptedAssignment::new)
                .collect(Collectors.toList()));
        validRoles.addAll(source.getRoleList().stream().map(JsonAdaptedRole::new).collect(Collectors.toList()));
    }

    /**
     * Converts this McScheduler into the model's {@code McScheduler} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public McScheduler toModelType() throws IllegalValueException {
        McScheduler mcScheduler = new McScheduler();
        for (JsonAdaptedWorker jsonAdaptedWorker : workers) {
            Worker worker = jsonAdaptedWorker.toModelType();
            if (mcScheduler.hasWorker(worker)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_WORKER);
            }
            mcScheduler.addWorker(worker);
        }

        for (JsonAdaptedShift jsonAdaptedShift : shifts) {
            Shift shift = jsonAdaptedShift.toModelType();
            if (mcScheduler.hasShift(shift)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_SHIFT);
            }
            mcScheduler.addShift(shift);
        }

        for (JsonAdaptedAssignment jsonAdaptedAssignment : assignments) {
            Assignment assignment = jsonAdaptedAssignment.toModelType();
            if (mcScheduler.hasAssignment(assignment)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_ASSIGNMENT);
            }
            mcScheduler.addAssignment(assignment);
        }

        for (JsonAdaptedRole jsonAdaptedRole : validRoles) {
            Role role = jsonAdaptedRole.toModelType();
            if (mcScheduler.hasRole(role)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_ROLE);
            }
            mcScheduler.addRole(role);
        }

        return mcScheduler;
    }

}
