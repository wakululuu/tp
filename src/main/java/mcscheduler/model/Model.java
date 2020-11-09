package mcscheduler.model;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import mcscheduler.commons.core.GuiSettings;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.role.Leave;
import mcscheduler.model.role.Role;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.worker.Worker;

/**
 * The API of the Model component.
 */
public interface Model {
    //@@author
    /** {@code Predicate} that always evaluate to true */
    Predicate<Worker> PREDICATE_SHOW_ALL_WORKERS = unused -> true;
    Predicate<Shift> PREDICATE_SHOW_ALL_SHIFTS = unused -> true;
    Predicate<Role> PREDICATE_SHOW_ALL_ROLES_WITHOUT_LEAVE = role -> !Leave.isLeave(role);

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
     * Returns the user prefs' McScheduler file path.
     */
    Path getMcSchedulerFilePath();

    /**
     * Sets the user prefs' McScheduler file path.
     */
    void setMcSchedulerFilePath(Path mcSchedulerFilePath);

    /**
     * Replaces McScheduler data with the data in {@code mcScheduler}.
     */
    void setMcScheduler(ReadOnlyMcScheduler mcScheduler);

    /** Returns the McScheduler */
    ReadOnlyMcScheduler getMcScheduler();

    // worker-level operations

    /**
     * Returns true if a worker with the same identity as {@code worker} exists in the McScheduler.
     */
    boolean hasWorker(Worker worker);

    /**
     * Deletes the given worker.
     * The worker must exist in the McScheduler.
     */
    void deleteWorker(Worker target);

    /**
     * Adds the given worker.
     * {@code worker} must not already exist in the McScheduler.
     */
    void addWorker(Worker worker);

    /**
     * Replaces the given worker {@code target} with {@code editedWorker}.
     * {@code target} must exist in the McScheduler.
     * The worker identity of {@code editedWorker} must not be the same as another existing worker in the McScheduler.
     */
    void setWorker(Worker target, Worker editedWorker);

    /** Returns the pay earned by a worker as float value */
    int calculateWorkerShiftsAssigned(Worker worker);

    /** Returns an unmodifiable view of the full worker list */
    ObservableList<Worker> getFullWorkerList();

    /** Returns an unmodifiable view of the filtered worker list */
    ObservableList<Worker> getFilteredWorkerList();

    /**
     * Updates the filter of the filtered worker list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredWorkerList(Predicate<Worker> predicate);

    // shift-level operations

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
     * Returns an unmodifiable view of the full shift list
     */
    ObservableList<Shift> getFullShiftList();

    /**
     * Returns an unmodifiable view of the filtered shift list
     */
    ObservableList<Shift> getFilteredShiftList();

    //@@author wakululuu
    // assignment-level operations

    /**
     * Returns true if an assignment with the same identity as {@code assignment} exists in the McScheduler.
     */
    boolean hasAssignment(Assignment assignment);

    /**
     * Deletes the given assignment.
     * The assignment must exist in the McScheduler.
     */
    void deleteAssignment(Assignment target);

    /**
     * Adds the given assignment.
     * {@code assignment} must not already exist in the McScheduler.
     */
    void addAssignment(Assignment assignment);

    /**
     * Replaces the given assignment {@code target} with {@code editedAssignment}.
     * {@code target} must exist in the McScheduler.
     * The assignment identity of {@code editedAssignment} must not be the same as another existing assignment in the
     * McScheduler.
     */
    void setAssignment(Assignment target, Assignment editedAssignment);

    /**
     * Returns an {@code Optional} containing assignment with same identity as query.
     * If no assignment matching query found, an empty Optional is returned.
     */
    Optional<Assignment> getAssignment(Assignment toGet);

    /** Returns an unmodifiable view of the full assignment list */
    ObservableList<Assignment> getFullAssignmentList();

    // role-level operations

    /**
     * Returns true if a role with the same identity as {@code role} exists in the McScheduler.
     */
    boolean hasRole(Role role);

    /**
     * Deletes the given role.
     * The role must exist in the McScheduler.
     */
    void deleteRole(Role target);

    /**
     * Adds the given role.
     * {@code role} must not already exist in the McScheduler.
     */
    void addRole(Role role);

    /**
     * Replaces the given role {@code target} with {@code editedRole}.
     * {@code target} must exist in the McScheduler.
     * The role identity of {@code editedRole} must not be the same as another existing role in the
     * McScheduler.
     */
    void setRole(Role target, Role editedRole);

    /** Returns an unmodifiable view of the filtered role list */
    ObservableList<Role> getFilteredRoleList();

    /**
     * Updates the filter of the filtered role list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredRoleList(Predicate<Role> predicate);

}
