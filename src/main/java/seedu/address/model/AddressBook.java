package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.shift.Shift;
import seedu.address.model.shift.UniqueShiftList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson and .isSameShift comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;
    private final UniqueShiftList shifts;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        shifts = new UniqueShiftList();
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
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    public void setShifts(List<Shift> shifts) {
        this.shifts.setShifts(shifts);
    }
    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
        setShifts(newData.getShiftList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    // shift-level methods

    /**
     * Returns true if a shift with the same identity as {@code shift} exists in the App.
     */
    public boolean hasShift(Shift shift) {
        requireNonNull(shift);
        return shifts.contains(shift);
    }

    /**
     * Adds a shift to the App.
     * The person must not already exist in the App.
     */
    public void addShift(Shift shift) {
        shifts.add(shift);
    }

    /**
     * Replaces the given shift {@code target} in the list with {@code editedShift}.
     * {@code target} must exist in the App.
     * The shift identity of {@code editedShift} must not be the same as another existing shift in the App.
     */
    public void setShift(Shift target, Shift editedShift) {
        requireNonNull(editedShift);
        shifts.setShift(target, editedShift);
    }

    /**
     * Removes given shift from the App.
     * @param key Shift to be removed. Must exist in the App.
     */
    public void removeShift(Shift key) {
        shifts.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return persons.asUnmodifiableObservableList().size() + " persons; "
                + shifts.asUnmodifiableObservableList().size() + " shifts";
        // TODO: refine later
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Shift> getShiftList() {
        return shifts.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {

        if (other == this) {
            return true;
        }

        if (!(other instanceof AddressBook)) {
            return false;
        }

        AddressBook otherAddressBook = (AddressBook) other;
        return persons.equals(otherAddressBook.persons)
                && shifts.equals(otherAddressBook.shifts);

    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }
}
