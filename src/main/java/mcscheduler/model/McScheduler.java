package mcscheduler.model;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import javafx.collections.ObservableList;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.assignment.UniqueAssignmentList;
import mcscheduler.model.role.Role;
import mcscheduler.model.role.UniqueRoleList;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.shift.UniqueShiftList;
import mcscheduler.model.worker.UniqueWorkerList;
import mcscheduler.model.worker.Worker;

/**
 * Wraps all data at the McScheduler level
 * Duplicates are not allowed (by .isSameWorker, .isSameShift and .isSameAssignment comparison)
 */
public class McScheduler implements ReadOnlyMcScheduler {

    private final UniqueWorkerList workers;
    private final UniqueShiftList shifts;
    private final UniqueAssignmentList assignments;
    private final UniqueRoleList validRoles;

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
        assignments = new UniqueAssignmentList();
        validRoles = new UniqueRoleList();
    }

    public McScheduler() {}

    /**
     * Creates a McScheduler using the Workers in the {@code toBeCopied}
     */
    public McScheduler(ReadOnlyMcScheduler toBeCopied) {
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

    /**
     * Replaces the contents of the shift list with {@code shifts}.
     * {@code shifts} must not contain duplicate shifts.
     */
    public void setShifts(List<Shift> shifts) {
        this.shifts.setShifts(shifts);
    }

    /**
     * Replaces the contents of the assignment list with {@code assignments}.
     * {@code assignments} must not contain duplicate assignments.
     */
    public void setAssignments(List<Assignment> assignments) {
        this.assignments.setAssignments(assignments);
    }

    /**
     * Replaces the contents of the valid role list with {@code roles}.
     * {@code roles} must not contain duplicate roles.
     */
    public void setRoles(List<Role> roles) {
        this.validRoles.setRoles(roles);
    }

    /**
     * Resets the existing data of this {@code McScheduler} with {@code newData}.
     */
    public void resetData(ReadOnlyMcScheduler newData) {
        requireNonNull(newData);

        setWorkers(newData.getWorkerList());
        setShifts(newData.getShiftList());
        setAssignments(newData.getAssignmentList());
        setRoles(newData.getRoleList());
    }

    // worker-level operations

    /**
     * Returns true if a worker with the same identity as {@code worker} exists in the McScheduler.
     */
    public boolean hasWorker(Worker worker) {
        requireNonNull(worker);
        return workers.contains(worker);
    }

    /**
     * Adds a worker to the McScheduler.
     * The worker must not already exist in the McScheduler.
     */
    public void addWorker(Worker p) {
        workers.add(p);
    }

    /**
     * Replaces the given worker {@code target} in the list with {@code editedWorker}.
     * {@code target} must exist in the McScheduler.
     * The worker identity of {@code editedWorker} must not be the same as another existing worker in the McScheduler.
     */
    public void setWorker(Worker target, Worker editedWorker) {
        requireNonNull(editedWorker);

        workers.setWorker(target, editedWorker);
    }

    /**
     * Removes {@code key} from this {@code McScheduler}.
     * {@code key} must exist in the McScheduler.
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
     * Returns true if a shift which equals to {@code shift} exists in the App.
     */
    public boolean hasExactShift(Shift shift) {
        requireNonNull(shift);
        return shifts.containsExact(shift);
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

    // assignment-level operations

    /**
     * Returns true if an assignment with the same identity as {@code assignment} exists in the McScheduler.
     */
    public boolean hasAssignment(Assignment assignment) {
        requireNonNull(assignment);
        return assignments.contains(assignment);
    }

    /**
     * Adds an assignment to the McScheduler.
     * The assignment must not already exist in the McScheduler.
     */
    public void addAssignment(Assignment p) {
        assignments.add(p);
    }

    /**
     * Replaces the given assignment {@code target} in the list with {@code editedAssignment}.
     * {@code target} must exist in the McScheduler.
     * The assignment identity of {@code editedAssignment} must not be the same as another existing assignment in the
     * McScheduler.
     */
    public void setAssignment(Assignment target, Assignment editedAssignment) {
        requireNonNull(editedAssignment);

        assignments.setAssignment(target, editedAssignment);
    }

    /**
     * Removes {@code key} from this {@code McScheduler}.
     * {@code key} must exist in the McScheduler.
     */
    public void removeAssignment(Assignment key) {
        assignments.remove(key);
    }

    /**
     * Returns {@code Optional} containing assignment in {@code McScheduler} that has same identity as query assignment.
     * If none found, returns and empty Optional.
     */
    public Optional<Assignment> getAssignment(Assignment toGet) {
        requireNonNull(toGet);
        return assignments.getAssignment(toGet);
    }

    // role-level operations

    /**
     * Returns true if a role with the same identity as {@code role} exists in the McScheduler.
     */
    public boolean hasRole(Role role) {
        requireNonNull(role);
        return validRoles.contains(role);
    }

    /**
     * Adds a role to the McScheduler.
     * The role must not already exist in the McScheduler.
     */
    public void addRole(Role p) {
        validRoles.add(p);
    }

    /**
     * Replaces the given role {@code target} in the list with {@code editedRole}.
     * {@code target} must exist in the McScheduler.
     * The role identity of {@code editedRole} must not be the same as another existing role in the
     * McScheduler.
     */
    public void setRole(Role target, Role editedRole) {
        requireNonNull(editedRole);

        validRoles.setRole(target, editedRole);
    }

    /**
     * Removes {@code key} from this {@code McScheduler}.
     * {@code key} must exist in the McScheduler.
     */
    public void removeRole(Role key) {
        validRoles.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return workers.asUnmodifiableObservableList().size() + " workers; "
                + shifts.asUnmodifiableObservableList().size() + " shifts;"
                + assignments.asUnmodifiableObservableList().size() + " assignments"
                + validRoles.asUnmodifiableObservableList().size() + " valid roles";
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
    public ObservableList<Assignment> getAssignmentList() {
        return assignments.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Role> getRoleList() {
        return validRoles.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {

        if (other == this) {
            return true;
        }

        if (!(other instanceof McScheduler)) {
            return false;
        }

        McScheduler otherMcScheduler = (McScheduler) other;
        return workers.equals(otherMcScheduler.workers)
                && shifts.equals(otherMcScheduler.shifts)
                && assignments.equals(otherMcScheduler.assignments)
                && validRoles.equals(otherMcScheduler.validRoles);

    }

    @Override
    public int hashCode() {
        return workers.hashCode();
    }
}
