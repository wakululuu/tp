package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_CASHIER;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_CHEF;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_JANITOR;

import seedu.address.model.AddressBook;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.shift.Shift;
import seedu.address.model.tag.Role;
import seedu.address.model.worker.Worker;

/**
 * A utility class to help with building Addressbook objects.
 * Example usage: <br>
 *     {@code AddressBook ab = new AddressBookBuilder().withWorker("John", "Doe").build();}
 */
public class AddressBookBuilder {

    private AddressBook addressBook;

    public AddressBookBuilder() {
        addressBook = new AddressBook();
    }

    public AddressBookBuilder(AddressBook addressBook) {
        this.addressBook = addressBook;
    }

    /**
     * Returns an {@code AddressBook} with all the typical workers, shifts and roles.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Worker worker : TypicalWorkers.getTypicalWorkers()) {
            ab.addWorker(worker);
        }
        for (Shift shift : TypicalShifts.getTypicalShifts()) {
            ab.addShift(shift);
        }
        ab.addRole(Role.createRole(VALID_ROLE_CASHIER));
        ab.addRole(Role.createRole(VALID_ROLE_CHEF));
        ab.addRole(Role.createRole(VALID_ROLE_JANITOR));
        return ab;
    }
    /**
     * Returns an {@code AddressBook} with all the typical workers, shifts, assignments and roles.
     */
    public static AddressBook getTypicalAddressBookWithAssignments() {
        AddressBook ab = new AddressBook();
        for (Worker worker : TypicalWorkers.getTypicalWorkers()) {
            ab.addWorker(worker);
        }
        for (Shift shift : TypicalShifts.getTypicalShifts()) {
            ab.addShift(shift);
        }
        for (Assignment assignment : TypicalAssignments.getTypicalAssignments()) {
            ab.addAssignment(assignment);
        }
        ab.addRole(Role.createRole(VALID_ROLE_CASHIER));
        ab.addRole(Role.createRole(VALID_ROLE_CHEF));
        ab.addRole(Role.createRole(VALID_ROLE_JANITOR));
        return ab;
    }

    /**
     * Adds a new {@code Worker} to the {@code AddressBook} that we are building.
     */
    public AddressBookBuilder withWorker(Worker worker) {
        addressBook.addWorker(worker);
        return this;
    }

    /**
     * Adds a new {@code Shift} to the {@code AddressBook} that we are building.
     */
    public AddressBookBuilder withShift(Shift shift) {
        addressBook.addShift(shift);
        return this;
    }

    public AddressBook build() {
        return addressBook;
    }
}
