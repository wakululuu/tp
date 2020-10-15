package seedu.address.testutil;

import seedu.address.model.AddressBook;
import seedu.address.model.shift.Shift;
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
     * Returns an {@code AddressBook} with all the typical workers.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Worker worker : TypicalWorkers.getTypicalWorkers()) {
            ab.addWorker(worker);
        }
        for (Shift shift : TypicalShifts.getTypicalShifts()) {
            ab.addShift(shift);
        }
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
