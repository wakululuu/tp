package seedu.address.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.shift.Shift;
import seedu.address.model.worker.Worker;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Worker> PREDICATE_SHOW_ALL_WORKERS = unused -> true;
    Predicate<Shift> PREDICATE_SHOW_ALL_SHIFTS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a worker with the same identity as {@code worker} exists in the address book.
     */
    boolean hasWorker(Worker worker);

    /**
     * Deletes the given worker.
     * The worker must exist in the address book.
     */
    void deleteWorker(Worker target);

    /**
     * Adds the given worker.
     * {@code worker} must not already exist in the address book.
     */
    void addWorker(Worker worker);

    /**
     * Replaces the given worker {@code target} with {@code editedWorker}.
     * {@code target} must exist in the address book.
     * The worker identity of {@code editedWorker} must not be the same as another existing worker in the address book.
     */
    void setWorker(Worker target, Worker editedWorker);

    /** Returns an unmodifiable view of the filtered worker list */
    ObservableList<Worker> getFilteredWorkerList();

    /**
     * Updates the filter of the filtered worker list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredWorkerList(Predicate<Worker> predicate);

    /**
     * Returns true if a shift with the same identity as {@code shift} exists in the App.
     */
    boolean hasShift(Shift shift);

    /**
     * Deletes the given shift.
     * Shift must exist in the App.
     */
    void deleteShift(Shift target);

    /**
     * Adds the given shift.
     * Shift must not already exist in the App.
     */
    void addShift(Shift shift);

    /**
     * Replaces the given shift {@code target} with {@code editedShift}.
     * {@code target} must already exist in the App.
     * There must be no shift with the same identity as {@code editedShift} that exists in the App.
     */
    void setShift(Shift target, Shift editedShift);

    /**
     * Updates the filter of the filtered shift list to filter by the given {@code predicate}
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredShiftList(Predicate<Shift> predicate);

    /**
     * Returns an unmodifiable view of the filtered shift list
     */
    ObservableList<Shift> getFilteredShiftList();

}
