package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.shift.Shift;
import seedu.address.model.tag.Role;
import seedu.address.model.worker.Worker;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_WORKER = "Workers list contains duplicate worker(s).";
    public static final String MESSAGE_DUPLICATE_SHIFT = "Shifts list contains duplicate shift(s).";
    public static final String MESSAGE_DUPLICATE_ASSIGNMENT = "Assignments list contains duplicate assignment(s).";
    public static final String MESSAGE_DUPLICATE_ROLE = "Roles list contains duplicate role(s).";

    private final List<JsonAdaptedWorker> workers = new ArrayList<>();
    private final List<JsonAdaptedShift> shifts = new ArrayList<>();
    private final List<JsonAdaptedAssignment> assignments = new ArrayList<>();
    private final List<JsonAdaptedRole> validRoles = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given workers, shifts, assignments and valid roles.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("workers") List<JsonAdaptedWorker> workers,
            @JsonProperty("shifts") List<JsonAdaptedShift> shifts,
            @JsonProperty("assignments") List<JsonAdaptedAssignment> assignments,
            @JsonProperty("validRoles") List<JsonAdaptedRole> validRoles) {
        this.workers.addAll(workers);
        this.shifts.addAll(shifts);
        this.assignments.addAll(assignments);
        this.validRoles.addAll(validRoles);
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        workers.addAll(source.getWorkerList().stream().map(JsonAdaptedWorker::new).collect(Collectors.toList()));
        shifts.addAll(source.getShiftList().stream().map(JsonAdaptedShift::new).collect(Collectors.toList()));
        assignments.addAll(source.getAssignmentList().stream().map(JsonAdaptedAssignment::new)
                .collect(Collectors.toList()));
        validRoles.addAll(source.getRoleList().stream().map(JsonAdaptedRole::new).collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();
        for (JsonAdaptedWorker jsonAdaptedWorker : workers) {
            Worker worker = jsonAdaptedWorker.toModelType();
            if (addressBook.hasWorker(worker)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_WORKER);
            }
            addressBook.addWorker(worker);
        }

        for (JsonAdaptedShift jsonAdaptedShift : shifts) {
            Shift shift = jsonAdaptedShift.toModelType();
            if (addressBook.hasShift(shift)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_SHIFT);
            }
            addressBook.addShift(shift);
        }

        for (JsonAdaptedAssignment jsonAdaptedAssignment : assignments) {
            Assignment assignment = jsonAdaptedAssignment.toModelType();
            if (addressBook.hasAssignment(assignment)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_ASSIGNMENT);
            }
            addressBook.addAssignment(assignment);
        }

        for (JsonAdaptedRole jsonAdaptedRole : validRoles) {
            Role role = jsonAdaptedRole.toModelType();
            if (addressBook.hasRole(role)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_ROLE);
            }
            addressBook.addRole(role);
        }

        return addressBook;
    }

}
