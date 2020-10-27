package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
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
import seedu.address.model.tag.Role;
import seedu.address.model.worker.Worker;
import seedu.address.testutil.ShiftBuilder;

public class ShiftAddCommandTest {

    @Test
    public void constructor_nullShift_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ShiftAddCommand(null));
    }

    @Test
    public void execute_shiftAcceptedByModel_addSuccessful() throws Exception {
        ShiftAddCommandTest.ModelStubAcceptingShiftAdded modelStub =
                new ShiftAddCommandTest.ModelStubAcceptingShiftAdded();
        Shift validShift = new ShiftBuilder().build();

        CommandResult commandResult = new ShiftAddCommand(validShift).execute(modelStub);

        assertEquals(String.format(ShiftAddCommand.MESSAGE_SUCCESS, validShift), commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validShift), modelStub.shiftsAdded);
    }

    @Test
    public void execute_duplicateShift_throwsCommandException() {
        Shift validShift = new ShiftBuilder().build();
        ShiftAddCommand shiftAddCommand = new ShiftAddCommand(validShift);
        ShiftAddCommandTest.ModelStub modelStub = new ShiftAddCommandTest.ModelStubWithShift(validShift);

        assertThrows(CommandException.class,
                shiftAddCommand.MESSAGE_DUPLICATE_SHIFT, () -> shiftAddCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Shift shift1 = new ShiftBuilder().withShiftDay("FRI").withShiftTime("AM").build();
        Shift shift2 = new ShiftBuilder().withShiftDay("TUE").withShiftTime("PM").build();
        ShiftAddCommand addShift1Command = new ShiftAddCommand(shift1);
        ShiftAddCommand addShift2Command = new ShiftAddCommand(shift2);

        // same object -> returns true
        assertEquals(addShift1Command, addShift1Command);

        // same values -> returns true
        ShiftAddCommand addShift1CommandCopy = new ShiftAddCommand(shift1);
        assertEquals(addShift1CommandCopy, addShift1Command);

        // different types -> returns false
        assertNotEquals(addShift1Command, 1);

        // null -> returns false
        assertNotEquals(addShift1Command, null);

        // different shift -> returns false
        assertNotEquals(addShift2Command, addShift1Command);
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

    /**
     * A Model stub that contains a single shift.
     */
    private class ModelStubWithShift extends ShiftAddCommandTest.ModelStub {
        private final Shift shift;

        ModelStubWithShift(Shift shift) {
            requireNonNull(shift);
            this.shift = shift;
        }

        @Override
        public boolean hasShift(Shift shift) {
            requireNonNull(shift);
            return this.shift.isSameShift(shift);
        }
    }

    /**
     * A Model stub that always accept the shift being added.
     */
    private class ModelStubAcceptingShiftAdded extends ShiftAddCommandTest.ModelStub {
        final ArrayList<Shift> shiftsAdded = new ArrayList<>();

        @Override
        public boolean hasShift(Shift shift) {
            requireNonNull(shift);
            return shiftsAdded.stream().anyMatch(shift::isSameShift);
        }

        @Override
        public void addShift(Shift shift) {
            requireNonNull(shift);
            shiftsAdded.add(shift);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

}
