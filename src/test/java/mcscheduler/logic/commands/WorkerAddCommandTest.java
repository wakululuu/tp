package mcscheduler.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import mcscheduler.commons.core.GuiSettings;
import mcscheduler.commons.core.Messages;
import mcscheduler.logic.commands.exceptions.CommandException;
import mcscheduler.model.McScheduler;
import mcscheduler.model.Model;
import mcscheduler.model.ReadOnlyMcScheduler;
import mcscheduler.model.ReadOnlyUserPrefs;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.role.Role;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.worker.Worker;
import mcscheduler.testutil.Assert;
import mcscheduler.testutil.WorkerBuilder;

public class WorkerAddCommandTest {

    @Test
    public void constructor_nullWorker_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new WorkerAddCommand(null));
    }

    @Test
    public void execute_workerAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingWorkerAdded modelStub = new ModelStubAcceptingWorkerAdded(
                Role.createRole(CommandTestUtil.VALID_ROLE_CASHIER));
        Worker validWorker = new WorkerBuilder().build();

        CommandResult commandResult = new WorkerAddCommand(validWorker).execute(modelStub);

        assertEquals(String.format(WorkerAddCommand.MESSAGE_SUCCESS, validWorker), commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validWorker), modelStub.workersAdded);
    }

    @Test
    public void execute_duplicateWorker_throwsCommandException() {
        Worker validWorker = new WorkerBuilder().build();
        WorkerAddCommand addCommand = new WorkerAddCommand(validWorker);
        ModelStub modelStub = new ModelStubWithWorker(validWorker);

        Assert.assertThrows(CommandException.class, WorkerAddCommand.MESSAGE_DUPLICATE_WORKER, () ->
                addCommand.execute(modelStub));
    }

    @Test
    public void execute_roleNotFound_throwsCommandException() {
        Worker validWorker = new WorkerBuilder().build();
        WorkerAddCommand addCommand = new WorkerAddCommand(validWorker);
        ModelStub modelStub = new ModelStubAcceptingWorkerAdded();

        Assert.assertThrows(CommandException.class,
                String.format(Messages.MESSAGE_ROLE_NOT_FOUND, CommandTestUtil.VALID_ROLE_CASHIER), () ->
                        addCommand.execute(modelStub));
    }

    //@@author
    @Test
    public void equals() {
        Worker alice = new WorkerBuilder().withName("Alice").build();
        Worker bob = new WorkerBuilder().withName("Bob").build();
        WorkerAddCommand addAliceCommand = new WorkerAddCommand(alice);
        WorkerAddCommand addBobCommand = new WorkerAddCommand(bob);

        // same object -> returns true
        assertEquals(addAliceCommand, addAliceCommand);

        // same values -> returns true
        WorkerAddCommand addAliceCommandCopy = new WorkerAddCommand(alice);
        assertEquals(addAliceCommandCopy, addAliceCommand);

        // different types -> returns false
        assertNotEquals(addAliceCommand, 1);

        // null -> returns false
        assertNotEquals(addAliceCommand, null);

        // different worker -> returns false
        assertNotEquals(addBobCommand, addAliceCommand);
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getMcSchedulerFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setMcSchedulerFilePath(Path mcSchedulerFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addWorker(Worker worker) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setMcScheduler(ReadOnlyMcScheduler newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyMcScheduler getMcScheduler() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasWorker(Worker worker) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteWorker(Worker target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setWorker(Worker target, Worker editedWorker) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public int calculateWorkerShiftsAssigned(Worker worker) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Worker> getFullWorkerList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Worker> getFilteredWorkerList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredWorkerList(Predicate<Worker> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasShift(Shift shift) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteShift(Shift target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addShift(Shift shift) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setShift(Shift target, Shift editedShift) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredShiftList(Predicate<Shift> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Shift> getFullShiftList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Shift> getFilteredShiftList() {
            throw new AssertionError("This method should not be called.");
        }

        //@@author wakululuu
        @Override
        public boolean hasAssignment(Assignment assignment) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteAssignment(Assignment target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addAssignment(Assignment assignment) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAssignment(Assignment target, Assignment editedAssignment) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Optional<Assignment> getAssignment(Assignment toGet) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Assignment> getFullAssignmentList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasRole(Role role) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteRole(Role target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addRole(Role role) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setRole(Role target, Role editedRole) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Role> getFilteredRoleList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredRoleList(Predicate<Role> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    //@@author
    /**
     * A Model stub that contains a single worker.
     */
    private class ModelStubWithWorker extends ModelStub {
        private final Worker worker;

        ModelStubWithWorker(Worker worker) {
            requireNonNull(worker);
            this.worker = worker;
        }

        @Override
        public boolean hasWorker(Worker worker) {
            requireNonNull(worker);
            return this.worker.isSameWorker(worker);
        }
    }

    /**
     * A Model stub that always accept the worker being added.
     */
    private class ModelStubAcceptingWorkerAdded extends ModelStub {
        final ArrayList<Worker> workersAdded = new ArrayList<>();
        final ArrayList<Role> validRoles = new ArrayList<>();

        ModelStubAcceptingWorkerAdded(Role... role) {
            requireNonNull(role);
            validRoles.addAll(Arrays.asList(role));
        }

        @Override
        public boolean hasWorker(Worker worker) {
            requireNonNull(worker);
            return workersAdded.stream().anyMatch(worker::isSameWorker);
        }

        @Override
        public void addWorker(Worker worker) {
            requireNonNull(worker);
            workersAdded.add(worker);
        }

        @Override
        public boolean hasRole(Role role) {
            requireNonNull(role);
            return validRoles.stream().anyMatch(role::equals);
        }

        @Override
        public ReadOnlyMcScheduler getMcScheduler() {
            return new McScheduler();
        }
    }

}
