package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.shift.WorkerRoleAssignment;

/**
 * Jackson-friendly version of {@link WorkerRoleAssignment}.
 */
public class JsonAdaptedWorkerRoleAssignment {

    private final JsonAdaptedWorker worker;
    private final JsonAdaptedRole role;

    /**
     * Constructs a {@code JsonAdaptedWorkerRoleAssignment} with the given details.
     */
    @JsonCreator
    public JsonAdaptedWorkerRoleAssignment(@JsonProperty("worker") JsonAdaptedWorker worker,
                                           @JsonProperty("role") JsonAdaptedRole role) {
        this.worker = worker;
        this.role = role;
    }

    /**
     * Converts a given {@code WorkerRoleAssignment} into this class for Jackson use.
     */
    public JsonAdaptedWorkerRoleAssignment(WorkerRoleAssignment source) {
        worker = new JsonAdaptedWorker(source.getWorker());
        role = new JsonAdaptedRole(source.getRole());
    }

    /**
     * Converts this Jackson-friendly adapted worker-role assignment into the model's {@code WorkerRoleAssignment}
     * object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted worker-role assignment.
     */
    public WorkerRoleAssignment toModelType() throws IllegalValueException {
        return new WorkerRoleAssignment(worker.toModelType(), role.toModelType());
    }
}
