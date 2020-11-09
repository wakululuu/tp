package mcscheduler.model;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import mcscheduler.commons.core.GuiSettings;
import mcscheduler.commons.core.LogsCenter;
import mcscheduler.commons.util.CollectionUtil;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.role.Leave;
import mcscheduler.model.role.Role;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.worker.Worker;

/**
 * Represents the in-memory model of the McScheduler data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final McScheduler mcScheduler;
    private final UserPrefs userPrefs;
    private final FilteredList<Worker> filteredWorkers;
    private final FilteredList<Shift> filteredShifts;
    private final FilteredList<Role> filteredRoles;

    /**
     * Initializes a ModelManager with the given mcScheduler and userPrefs.
     */
    public ModelManager(ReadOnlyMcScheduler mcScheduler, ReadOnlyUserPrefs userPrefs) {
        super();
        CollectionUtil.requireAllNonNull(mcScheduler, userPrefs);

        logger.fine("Initializing with McScheduler: " + mcScheduler + " and user prefs " + userPrefs);

        this.mcScheduler = new McScheduler(mcScheduler);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredWorkers = new FilteredList<>(this.mcScheduler.getWorkerList());
        filteredShifts = new FilteredList<>(this.mcScheduler.getShiftList());
        filteredRoles = new FilteredList<>(this.mcScheduler.getRoleList());
        getFullWorkerList().forEach(this::synchronizeWorkerInAssignment);
        getFullShiftList().forEach(this::synchronizeShiftInAssignment);

    }

    public ModelManager() {
        this(new McScheduler(), new UserPrefs());
    }

    //=========== initialization synchronization ==================================

    private void synchronizeWorkerInAssignment(Worker worker) {
        new ArrayList<>(getFullAssignmentList())
                .stream()
                .filter(assignment -> assignment.getWorker().isSameWorker(worker))
                .forEach(assignment -> setAssignment(assignment,
                        new Assignment(assignment.getShift(), worker, assignment.getRole())));
    }

    private void synchronizeShiftInAssignment(Shift shift) {
        new ArrayList<>(getFullAssignmentList())
                .stream()
                .filter(assignment -> assignment.getShift().isSameShift(shift))
                .forEach(assignment -> setAssignment(assignment,
                        new Assignment(shift, assignment.getWorker(), assignment.getRole())));
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
    public Path getMcSchedulerFilePath() {
        return userPrefs.getMcSchedulerFilePath();
    }

    @Override
    public void setMcSchedulerFilePath(Path mcSchedulerFilePath) {
        requireNonNull(mcSchedulerFilePath);
        userPrefs.setMcSchedulerFilePath(mcSchedulerFilePath);
    }

    //=========== McScheduler ================================================================================

    @Override
    public void setMcScheduler(ReadOnlyMcScheduler mcScheduler) {
        this.mcScheduler.resetData(mcScheduler);
    }

    @Override
    public ReadOnlyMcScheduler getMcScheduler() {
        return mcScheduler;
    }

    // Worker related methods
    @Override
    public boolean hasWorker(Worker worker) {
        requireNonNull(worker);
        return mcScheduler.hasWorker(worker);
    }

    @Override
    public void deleteWorker(Worker target) {
        mcScheduler.removeWorker(target);
    }

    @Override
    public void addWorker(Worker worker) {
        mcScheduler.addWorker(worker);
        updateFilteredWorkerList(PREDICATE_SHOW_ALL_WORKERS);
    }

    @Override
    public void setWorker(Worker target, Worker editedWorker) {
        CollectionUtil.requireAllNonNull(target, editedWorker);

        mcScheduler.setWorker(target, editedWorker);
    }
    @Override
    public int calculateWorkerShiftsAssigned(Worker worker) {
        Integer numberOfShiftsAssigned = 0;
        ObservableList<Assignment> assignments = getFullAssignmentList();
        for (Assignment assignment : assignments) {
            if (Leave.isLeave(assignment.getRole())) {
                continue;
            }
            Worker assignedWorker = assignment.getWorker();
            if (assignedWorker.equals(worker)) {
                numberOfShiftsAssigned++;
            }
        }
        assert numberOfShiftsAssigned >= 0 : "Invalid number of shifts counted";

        return numberOfShiftsAssigned;
    }

    @Override
    public ObservableList<Worker> getFullWorkerList() {
        return mcScheduler.getWorkerList();
    }

    // Shift related methods
    @Override
    public boolean hasShift(Shift shift) {
        requireNonNull(shift);
        return mcScheduler.hasShift(shift);
    }

    @Override
    public void deleteShift(Shift target) {
        mcScheduler.removeShift(target);
    }

    @Override
    public void addShift(Shift shift) {
        mcScheduler.addShift(shift);
        updateFilteredShiftList(PREDICATE_SHOW_ALL_SHIFTS);
    }

    @Override
    public void setShift(Shift target, Shift editedShift) {
        CollectionUtil.requireAllNonNull(target, editedShift);
        mcScheduler.setShift(target, editedShift);
    }

    @Override
    public ObservableList<Shift> getFullShiftList() {
        return mcScheduler.getShiftList();
    }

    // Assignment related methods
    @Override
    public boolean hasAssignment(Assignment assignment) {
        requireNonNull(assignment);
        return mcScheduler.hasAssignment(assignment);
    }

    @Override
    public void deleteAssignment(Assignment target) {
        mcScheduler.removeAssignment(target);
        target.getShift().updateRoleRequirements(getFullAssignmentList());
    }

    @Override
    public void addAssignment(Assignment assignment) {
        mcScheduler.addAssignment(assignment);
        assignment.getShift().updateRoleRequirements(getFullAssignmentList());
    }

    @Override
    public void setAssignment(Assignment target, Assignment editedAssignment) {
        CollectionUtil.requireAllNonNull(target, editedAssignment);

        mcScheduler.setAssignment(target, editedAssignment);
        target.getShift().updateRoleRequirements(getFullAssignmentList());
        editedAssignment.getShift().updateRoleRequirements(getFullAssignmentList());
    }

    @Override
    public Optional<Assignment> getAssignment(Assignment toGet) {
        requireNonNull(toGet);
        return mcScheduler.getAssignment(toGet);
    }

    @Override
    public ObservableList<Assignment> getFullAssignmentList() {
        return mcScheduler.getAssignmentList();
    }

    // Role related methods
    @Override
    public boolean hasRole(Role role) {
        requireNonNull(role);
        if (role instanceof Leave) {
            return true;
        }
        return mcScheduler.hasRole(role);
    }

    @Override
    public void deleteRole(Role target) {
        mcScheduler.removeRole(target);
    }

    @Override
    public void addRole(Role role) {
        mcScheduler.addRole(role);
    }

    @Override
    public void setRole(Role target, Role editedRole) {
        CollectionUtil.requireAllNonNull(target, editedRole);

        mcScheduler.setRole(target, editedRole);
    }

    /**
     * Returns an unmodifiable view of the list of {@code Role} backed by the internal list of
     * {@code versionedMcScheduler}
     */
    @Override
    public ObservableList<Role> getFilteredRoleList() {
        return filteredRoles;
    }

    @Override
    public void updateFilteredRoleList(Predicate<Role> predicate) {
        requireNonNull(predicate);
        filteredRoles.setPredicate(predicate);
    }

    //=========== Filtered Worker List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Worker} backed by the internal list of
     * {@code versionedMcScheduler}
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
        return mcScheduler.equals(other.mcScheduler)
                && userPrefs.equals(other.userPrefs)
                && filteredWorkers.equals(other.filteredWorkers)
                && filteredShifts.equals(other.filteredShifts);
    }

}
