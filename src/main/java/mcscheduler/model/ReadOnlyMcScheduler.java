package mcscheduler.model;

import javafx.collections.ObservableList;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.role.Role;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.worker.Worker;

/**
 * Unmodifiable view of a McScheduler
 */
public interface ReadOnlyMcScheduler {

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

    /**
     * Returns an unmodifiable view of the role list.
     * This list will not contain any duplicate roles.
     */
    ObservableList<Role> getRoleList();
}
