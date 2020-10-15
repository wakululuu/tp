package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Role;
import seedu.address.model.worker.Address;
import seedu.address.model.worker.Name;
import seedu.address.model.worker.Pay;
import seedu.address.model.worker.Phone;
import seedu.address.model.worker.ShiftRoleAssignment;
import seedu.address.model.worker.Worker;
//import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Worker}.
 */
class JsonAdaptedWorker {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Worker's %s field is missing!";

    private final String name;
    private final String phone;
    private final String pay;
    private final String address;
    private final List<JsonAdaptedRole> roles = new ArrayList<>();
    private final List<JsonAdaptedShiftRoleAssignment> shiftRoleAssignments = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedWorker} with the given worker details.
     */
    @JsonCreator
    public JsonAdaptedWorker(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
                             @JsonProperty("pay") String pay, @JsonProperty("address") String address,
                             @JsonProperty("roles") List<JsonAdaptedRole> roles,
                             @JsonProperty("shiftRoleAssignments")
                                         List<JsonAdaptedShiftRoleAssignment> shiftRoleAssignments) {
        this.name = name;
        this.phone = phone;
        this.pay = pay;
        this.address = address;
        if (roles != null) {
            this.roles.addAll(roles);
        }
        if (shiftRoleAssignments != null) {
            this.shiftRoleAssignments.addAll(shiftRoleAssignments);
        }
    }

    /**
     * Converts a given {@code Worker} into this class for Jackson use.
     */
    public JsonAdaptedWorker(Worker source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        pay = String.valueOf(source.getPay().value);
        address = source.getAddress().value;
        roles.addAll(source.getRoles().stream()
                .map(JsonAdaptedRole::new)
                .collect(Collectors.toList()));
        shiftRoleAssignments.addAll(source.getShiftRoleAssignments()
                .stream()
                .map(JsonAdaptedShiftRoleAssignment::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted worker object into the model's {@code Worker} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted worker.
     */
    public Worker toModelType() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (pay == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Pay.class.getSimpleName()));
        }
        if (!Pay.isValidPay(pay)) {
            throw new IllegalValueException(Pay.MESSAGE_CONSTRAINTS);
        }
        final Pay modelPay = new Pay(pay);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        List<Role> rolesBuilder = new ArrayList<>();
        for (JsonAdaptedRole role : roles) {
            rolesBuilder.add(role.toModelType());
        }
        final Set<Role> modelRoles = new HashSet<>(rolesBuilder);

        List<ShiftRoleAssignment> shiftRoleAssignmentsBuilder = new ArrayList<>();
        for (JsonAdaptedShiftRoleAssignment assignment : shiftRoleAssignments) {
            shiftRoleAssignmentsBuilder.add(assignment.toModelType());
        }
        final Set<ShiftRoleAssignment> modelShiftRoleAssignments = new HashSet<>(shiftRoleAssignmentsBuilder);

        return new Worker(modelName, modelPhone, modelPay, modelAddress, modelRoles, modelShiftRoleAssignments);
    }

}
