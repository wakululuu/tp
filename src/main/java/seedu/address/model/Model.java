package seedu.address.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.person.Worker;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Worker> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

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
    boolean hasPerson(Worker worker);

    /**
     * Deletes the given worker.
     * The worker must exist in the address book.
     */
    void deletePerson(Worker target);

    /**
     * Adds the given worker.
     * {@code worker} must not already exist in the address book.
     */
    void addPerson(Worker worker);

    /**
     * Replaces the given worker {@code target} with {@code editedWorker}.
     * {@code target} must exist in the address book.
     * The worker identity of {@code editedWorker} must not be the same as another existing worker in the address book.
     */
    void setPerson(Worker target, Worker editedWorker);

    /** Returns an unmodifiable view of the filtered worker list */
    ObservableList<Worker> getFilteredPersonList();

    /**
     * Updates the filter of the filtered worker list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Worker> predicate);
}
