package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.shift.Shift;
import seedu.address.model.worker.Worker;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the workers list.
     * This list will not contain any duplicate workers.
     */
    ObservableList<Worker> getWorkerList();

    ObservableList<Shift> getShiftList();
}
