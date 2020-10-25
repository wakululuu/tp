package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.shift.Shift;
import seedu.address.model.worker.Worker;
import seedu.address.testutil.WorkerBuilder;

public class WorkerAddCommandTest {

    @Test
    public void constructor_nullWorker_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new WorkerAddCommand(null));
    }

    @Test
    public void execute_workerAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingWorkerAdded modelStub = new ModelStubAcceptingWorkerAdded();
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

        assertThrows(CommandException.class, WorkerAddCommand.MESSAGE_DUPLICATE_WORKER, ()
            -> addCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Worker alice = new WorkerBuilder().withName("Alice").build();
        Worker bob = new WorkerBuilder().withName("Bob").build();
        WorkerAddCommand addAliceCommand = new WorkerAddCommand(alice);
        WorkerAddCommand addBobCommand = new WorkerAddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        WorkerAddCommand addAliceCommandCopy = new WorkerAddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different worker -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
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
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addWorker(Worker worker) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
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
        public ObservableList<Assignment> getFilteredAssignmentList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredAssignmentList(Predicate<Assignment> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

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
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

}
