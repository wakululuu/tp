package mcscheduler.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import mcscheduler.commons.exceptions.IllegalValueException;
import mcscheduler.model.role.Role;
import mcscheduler.model.worker.Address;
import mcscheduler.model.worker.Name;
import mcscheduler.model.worker.Pay;
import mcscheduler.model.worker.Phone;
import mcscheduler.model.worker.Unavailability;
import mcscheduler.model.worker.Worker;

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
    private final List<JsonAdaptedUnavailability> unavailableTimings = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedWorker} with the given worker details.
     */
    @JsonCreator
    public JsonAdaptedWorker(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
            @JsonProperty("pay") String pay, @JsonProperty("address") String address,
            @JsonProperty("roles") List<JsonAdaptedRole> roles,
            @JsonProperty("unavailableTimings") List<JsonAdaptedUnavailability> unavailableTimings) {
        this.name = name;
        this.phone = phone;
        this.pay = pay;
        this.address = address;
        if (roles != null) {
            this.roles.addAll(roles);
        }
        if (unavailableTimings != null) {
            this.unavailableTimings.addAll(unavailableTimings);
        }
    }

    /**
     * Converts a given {@code Worker} into this class for Jackson use.
     */
    public JsonAdaptedWorker(Worker source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        pay = String.valueOf(source.getPay().getValue());
        address = source.getAddress().value;
        roles.addAll(source.getRoles().stream()
                .map(JsonAdaptedRole::new)
                .collect(Collectors.toList()));
        unavailableTimings.addAll(source.getUnavailableTimings().stream()
                .map(JsonAdaptedUnavailability::new)
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

        List<Unavailability> unavailabilitiesBuilder = new ArrayList<>();
        for (JsonAdaptedUnavailability unavailability : unavailableTimings) {
            unavailabilitiesBuilder.add(unavailability.toModelType());
        }
        final Set<Unavailability> modelUnavailabilities = new HashSet<>(unavailabilitiesBuilder);

        return new Worker(modelName, modelPhone, modelPay, modelAddress, modelRoles, modelUnavailabilities);
    }

}
