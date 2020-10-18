package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.shift.Shift;
import seedu.address.model.worker.Worker;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Worker> filteredWorkers;
    private final FilteredList<Shift> filteredShifts;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredWorkers = new FilteredList<>(this.addressBook.getWorkerList());
        filteredShifts = new FilteredList<>(this.addressBook.getShiftList());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    // Worker related methods
    @Override
    public boolean hasWorker(Worker worker) {
        requireNonNull(worker);
        return addressBook.hasWorker(worker);
    }

    @Override
    public void deleteWorker(Worker target) {
        addressBook.removeWorker(target);
    }

    @Override
    public void addWorker(Worker worker) {
        addressBook.addWorker(worker);
        updateFilteredWorkerList(PREDICATE_SHOW_ALL_WORKERS);
    }

    @Override
    public void setWorker(Worker target, Worker editedWorker) {
        requireAllNonNull(target, editedWorker);

        addressBook.setWorker(target, editedWorker);
    }

    @Override
    public ObservableList<Worker> getFullWorkerList() {
        return addressBook.getWorkerList();
    }

    // Shift related methods
    @Override
    public boolean hasShift(Shift shift) {
        requireNonNull(shift);
        return addressBook.hasShift(shift);
    }

    @Override
    public void deleteShift(Shift target) {
        addressBook.removeShift(target);
    }

    @Override
    public void addShift(Shift shift) {
        addressBook.addShift(shift);
        updateFilteredShiftList(PREDICATE_SHOW_ALL_SHIFTS);
    }

    @Override
    public void setShift(Shift target, Shift editedShift) {
        requireAllNonNull(target, editedShift);
        addressBook.setShift(target, editedShift);
    }

    @Override
    public ObservableList<Shift> getFullShiftList() {
        return addressBook.getShiftList();
    }

    //=========== Filtered Worker List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Worker} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Worker> getFilteredWorkerList() {
        return filteredWorkers;
    }

    @Override
    public void updateFilteredWorkerList(Predicate<Worker> predicate) {
        requireNonNull(predicate);
        filteredWorkers.setPredicate(predicate);
    }

    //============ Filtered Shift List Accessors ==============================================================

    @Override
    public ObservableList<Shift> getFilteredShiftList() {
        return filteredShifts;
    }

    @Override
    public void updateFilteredShiftList(Predicate<Shift> predicate) {
        requireNonNull(predicate);
        filteredShifts.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return addressBook.equals(other.addressBook)
                && userPrefs.equals(other.userPrefs)
                && filteredWorkers.equals(other.filteredWorkers)
                && filteredShifts.equals(other.filteredShifts);
    }

}
