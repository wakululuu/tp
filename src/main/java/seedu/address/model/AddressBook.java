package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.model.shift.Shift;
import seedu.address.model.shift.UniqueShiftList;
import seedu.address.model.worker.UniqueWorkerList;
import seedu.address.model.worker.Worker;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSameWorker and .isSameShift comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniqueWorkerList workers;
    private final UniqueShiftList shifts;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        workers = new UniqueWorkerList();
        shifts = new UniqueShiftList();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Workers in the {@code toBeCopied}
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
    public void setWorkers(List<Worker> workers) {
        this.workers.setWorkers(workers);
    }

    public void setShifts(List<Shift> shifts) {
        this.shifts.setShifts(shifts);
    }
    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setWorkers(newData.getWorkerList());
        setShifts(newData.getShiftList());
    }

    //// worker-level operations

    /**
     * Returns true if a worker with the same identity as {@code worker} exists in the address book.
     */
    public boolean hasWorker(Worker worker) {
        requireNonNull(worker);
        return workers.contains(worker);
    }

    /**
     * Adds a worker to the address book.
     * The worker must not already exist in the address book.
     */
    public void addWorker(Worker p) {
        workers.add(p);
    }

    /**
     * Replaces the given worker {@code target} in the list with {@code editedWorker}.
     * {@code target} must exist in the address book.
     * The worker identity of {@code editedWorker} must not be the same as another existing worker in the address book.
     */
    public void setWorker(Worker target, Worker editedWorker) {
        requireNonNull(editedWorker);

        workers.setWorker(target, editedWorker);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removeWorker(Worker key) {
        workers.remove(key);
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
     * The worker must not already exist in the App.
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
        return workers.asUnmodifiableObservableList().size() + " workers; "
                + shifts.asUnmodifiableObservableList().size() + " shifts";
        // TODO: refine later
    }

    @Override
    public ObservableList<Worker> getWorkerList() {
        return workers.asUnmodifiableObservableList();
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
        return workers.equals(otherAddressBook.workers)
                && shifts.equals(otherAddressBook.shifts);

    }

    @Override
    public int hashCode() {
        return workers.hashCode();
    }
}
