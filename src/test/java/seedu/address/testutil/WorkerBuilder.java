package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.tag.Role;
//import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;
import seedu.address.model.worker.Address;
//import seedu.address.model.worker.Email;
import seedu.address.model.worker.Name;
import seedu.address.model.worker.Pay;
import seedu.address.model.worker.Phone;
import seedu.address.model.worker.ShiftRoleAssignment;
import seedu.address.model.worker.Worker;

/**
 * A utility class to help with building Worker objects.
 */
public class WorkerBuilder {

    public static final String DEFAULT_NAME = "Alice Pauline";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_PAY = "12.20";
    //public static final String DEFAULT_EMAIL = "alice@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";

    private Name name;
    private Phone phone;
    private Pay pay;
    //private Email email;
    private Address address;
    private Set<Role> roles;
    private Set<ShiftRoleAssignment> shiftRoleAssignments;

    /**
     * Creates a {@code WorkerBuilder} with the default details.
     */
    public WorkerBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        pay = new Pay(DEFAULT_PAY);
        //email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        roles = new HashSet<>();
        shiftRoleAssignments = new HashSet<>();
    }

    /**
     * Initializes the WorkerBuilder with the data of {@code workerToCopy}.
     */
    public WorkerBuilder(Worker workerToCopy) {
        name = workerToCopy.getName();
        phone = workerToCopy.getPhone();
        pay = workerToCopy.getPay();
        //email = workerToCopy.getEmail();
        address = workerToCopy.getAddress();
        roles = new HashSet<>(workerToCopy.getRoles());
        shiftRoleAssignments = new HashSet<>(workerToCopy.getShiftRoleAssignments());
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
    public WorkerBuilder withRoles(String ... roles) {
        this.roles = SampleDataUtil.getRoleSet(roles);
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

    /**
     * Parses the {@code shiftRoleAssignmentss} into a {@code Set<ShiftRoleAssignments>} and set it to the
     * {@code Worker} that we are building.
     */
    public WorkerBuilder withShiftRoleAssignments(String ... shiftRoleAssignments) {
        this.shiftRoleAssignments = SampleDataUtil.getShiftRoleAssignmentSet(shiftRoleAssignments);
        return this;
    }

    /*
    /**
     * Sets the {@code Email} of the {@code Worker} that we are building.
     */
    /*
    public WorkerBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }
     */

    public Worker build() {
        return new Worker(name, phone, pay, address, roles, shiftRoleAssignments);
    }

}
