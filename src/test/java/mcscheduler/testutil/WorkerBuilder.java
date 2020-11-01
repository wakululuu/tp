package mcscheduler.testutil;

import java.util.HashSet;
import java.util.Set;

import mcscheduler.model.role.Role;
import mcscheduler.model.util.SampleDataUtil;
import mcscheduler.model.worker.Address;
import mcscheduler.model.worker.Name;
import mcscheduler.model.worker.Pay;
import mcscheduler.model.worker.Phone;
import mcscheduler.model.worker.Unavailability;
import mcscheduler.model.worker.Worker;

/**
 * A utility class to help with building Worker objects.
 */
public class WorkerBuilder {

    public static final String DEFAULT_NAME = "Alice Pauline";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_PAY = "12.20";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_ROLE = "cashier";

    private Name name;
    private Phone phone;
    private Pay pay;
    private Address address;
    private Set<Role> roles;
    private Set<Unavailability> unavailableTimings;

    /**
     * Creates a {@code WorkerBuilder} with the default details.
     */
    public WorkerBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        pay = new Pay(DEFAULT_PAY);
        address = new Address(DEFAULT_ADDRESS);
        roles = new HashSet<>();
        roles.add(Role.createRole(DEFAULT_ROLE));
        unavailableTimings = new HashSet<>();
    }

    /**
     * Initializes the WorkerBuilder with the data of {@code workerToCopy}.
     */
    public WorkerBuilder(Worker workerToCopy) {
        name = workerToCopy.getName();
        phone = workerToCopy.getPhone();
        pay = workerToCopy.getPay();
        address = workerToCopy.getAddress();
        roles = new HashSet<>(workerToCopy.getRoles());
        unavailableTimings = new HashSet<>(workerToCopy.getUnavailableTimings());
    }

    /**
     * Sets the {@code Name} of the {@code Worker} that we are building.
     */
    public WorkerBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code roles} into a {@code Set<Role>} and set it to the {@code Worker} that we are building.
     */
    public WorkerBuilder withRoles(String... roles) {
        this.roles = SampleDataUtil.getRoleSet(roles);
        return this;
    }

    /**
     * Parses the {@code unavailableTimings} into a {@code Set<Unavailability>}
     * and set it to the {@code Worker} that we are building.
     */
    public WorkerBuilder withUnavailableTimings(String... unavailableTimings) {
        this.unavailableTimings = SampleDataUtil.getUnavailabilitySet(unavailableTimings);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Worker} that we are building.
     */
    public WorkerBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Worker} that we are building.
     */
    public WorkerBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Pay} of the {@code Worker} that we are building.
     */
    public WorkerBuilder withPay(String pay) {
        this.pay = new Pay(pay);
        return this;
    }

    public Worker build() {
        return new Worker(name, phone, pay, address, roles, unavailableTimings);
    }

}
