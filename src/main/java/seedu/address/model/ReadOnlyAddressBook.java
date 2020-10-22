package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.shift.Shift;
import seedu.address.model.worker.Worker;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the worker list.
     * This list will not contain any duplicate workers.
     */
    ObservableList<Worker> getWorkerList();

    /**
     * Returns an unmodifiable view of the shift list.
     * This list will not contain any duplicate shifts.
     */
    ObservableList<Shift> getShiftList();

    /**
     * Returns an unmodifiable view of the assignment list.
     * This list will not contain any duplicate assignments.
     */
    ObservableList<Assignment> getAssignmentList();
}
