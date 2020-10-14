package seedu.address.logic.commands;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.shift.Shift;
import seedu.address.testutil.PersonBuilder;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

public class ShiftAddCommandTest {

    @Test
    public void constructor_nullShift_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ShiftAddCommand(null));
    }

    @Test
    public void execute_shiftAcceptedByModel_addSuccessful() throws Exception {
        ShiftAddCommandTest.ModelStubAcceptingPersonAdded modelStub = new ShiftAddCommandTest.ModelStubAcceptingPersonAdded();
        Person validPerson = new PersonBuilder().build();

        CommandResult commandResult = new AddCommand(validPerson).execute(modelStub);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validPerson), commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validPerson), modelStub.personsAdded);
    }

    @Test
    public void execute_duplicateShift_throwsCommandException() {
        Person validPerson = new PersonBuilder().build();
        AddCommand addCommand = new AddCommand(validPerson);
        ShiftAddCommandTest.ModelStub modelStub = new ShiftAddCommandTest.ModelStubWithPerson(validPerson);

        assertThrows(CommandException.class, AddCommand.MESSAGE_DUPLICATE_PERSON, () -> addCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Shift shift1 = new ShiftBuilder().withName("Alice").build();
        Shift shift2 = new ShiftBuilder().withName("Bob").build();
        ShiftAddCommand addShift1Command = new ShiftAddCommand(shift1);
        ShiftAddCommand addShift2Command = new ShiftAddCommand(shift2);

        // same object -> returns true
        assertTrue(addShift1Command.equals(addShift1Command));

        // same values -> returns true
        ShiftAddCommand addShift1CommandCopy = new ShiftAddCommand(shift1);
        assertTrue(addShift1Command.equals(addShift1CommandCopy));

        // different types -> returns false
        assertFalse(addShift1Command.equals(1));

        // null -> returns false
        assertFalse(addShift1Command.equals(null));

        // different person -> returns false
        assertFalse(addShift1Command.equals(addShift2Command));
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
        public void addPerson(Person person) {
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
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
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
        public ObservableList<Shift> getFilteredShiftList() {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single person.
     */
    private class ModelStubWithPerson extends ShiftAddCommandTest.ModelStub {
        private final Person person;

        ModelStubWithPerson(Person person) {
            requireNonNull(person);
            this.person = person;
        }

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return this.person.isSamePerson(person);
        }
    }

    /**
     * A Model stub that always accept the person being added.
     */
    private class ModelStubAcceptingPersonAdded extends ShiftAddCommandTest.ModelStub {
        final ArrayList<Person> personsAdded = new ArrayList<>();

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return personsAdded.stream().anyMatch(person::isSamePerson);
        }

        @Override
        public void addPerson(Person person) {
            requireNonNull(person);
            personsAdded.add(person);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

}
