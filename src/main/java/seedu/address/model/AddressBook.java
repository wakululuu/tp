package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.model.person.Worker;
import seedu.address.model.person.UniquePersonList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the worker list with {@code workers}.
     * {@code workers} must not contain duplicate workers.
     */
    public void setPersons(List<Worker> workers) {
        this.persons.setPersons(workers);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
    }

    //// worker-level operations

    /**
     * Returns true if a worker with the same identity as {@code worker} exists in the address book.
     */
    public boolean hasPerson(Worker worker) {
        requireNonNull(worker);
        return persons.contains(worker);
    }

    /**
     * Adds a worker to the address book.
     * The worker must not already exist in the address book.
     */
    public void addPerson(Worker p) {
        persons.add(p);
    }

    /**
     * Replaces the given worker {@code target} in the list with {@code editedWorker}.
     * {@code target} must exist in the address book.
     * The worker identity of {@code editedWorker} must not be the same as another existing worker in the address book.
     */
    public void setPerson(Worker target, Worker editedWorker) {
        requireNonNull(editedWorker);

        persons.setPerson(target, editedWorker);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Worker key) {
        persons.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return persons.asUnmodifiableObservableList().size() + " persons";
        // TODO: refine later
    }

    @Override
    public ObservableList<Worker> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddressBook // instanceof handles nulls
                && persons.equals(((AddressBook) other).persons));
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }
}
