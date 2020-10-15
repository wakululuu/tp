package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_CASHIER;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_REQUIREMENT_CHEF;
import static seedu.address.testutil.AddressBookBuilder.getTypicalAddressBook;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalShifts.SHIFT_A;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.shift.Shift;
import seedu.address.model.shift.exceptions.DuplicateShiftException;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.ShiftBuilder;

public class AddressBookTest {

    private final AddressBook addressBook = new AddressBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), addressBook.getPersonList());
        assertEquals(Collections.emptyList(), addressBook.getShiftList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withRoles(VALID_ROLE_CASHIER)
                .build();
        List<Person> newPersons = Arrays.asList(ALICE, editedAlice);
        AddressBookStub newData = AddressBookStub.createAddressBookStubWithPersons(newPersons);

        assertThrows(DuplicatePersonException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        assertTrue(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withRoles(VALID_ROLE_CASHIER)
                .build();
        assertTrue(addressBook.hasPerson(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getPersonList().remove(0));
    }

    @Test
    public void resetData_withDuplicateShifts_throwsDuplicateShiftException() {
        Shift editedShift = new ShiftBuilder(SHIFT_A).withRoleRequirements(VALID_ROLE_REQUIREMENT_CHEF)
                .build();
        List<Shift> newShifts = Arrays.asList(SHIFT_A, editedShift);
        AddressBookStub newData = AddressBookStub.createAddressBookStubWithShifts(newShifts);

        assertThrows(DuplicateShiftException.class, () -> addressBook.resetData(newData));
    }


    @Test
    public void hasShift_nullShift_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasShift(null));
    }

    @Test
    public void hasShift_shiftNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasShift(SHIFT_A));
    }

    @Test
    public void hasShift_shiftInAddressBook_returnsTrue() {
        addressBook.addShift(SHIFT_A);
        assertTrue(addressBook.hasShift(SHIFT_A));
    }

    @Test
    public void hasShift_personWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addShift(SHIFT_A);
        Shift editedShift = new ShiftBuilder(SHIFT_A).withRoleRequirements(VALID_ROLE_REQUIREMENT_CHEF)
                .build();
        assertTrue(addressBook.hasShift(editedShift));
    }

    @Test
    public void getShiftList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getShiftList().remove(0));
    }

    @Test
    public void equals() {

        addressBook.addPerson(ALICE);
        addressBook.addShift(SHIFT_A);
        AddressBook noPersonAddressBook = new AddressBookBuilder().withShift(SHIFT_A).build();
        AddressBook noShiftAddressBook = new AddressBookBuilder().withPerson(ALICE).build();
        AddressBook emptyAddressBook = new AddressBook();

        //same object returns true
        assertTrue(addressBook.equals(addressBook));

        //different class object returns false
        assertFalse(addressBook.equals(123));

        //same content returns true
        assertTrue(addressBook.equals(new AddressBook(addressBook)));

        //same shifts different persons returns false
        assertFalse(addressBook.equals(noPersonAddressBook));

        //same persons different shifts returns false
        assertFalse(addressBook.equals(noShiftAddressBook));

        //different persons different shifts returns false
        assertFalse(addressBook.equals(emptyAddressBook));

    }


    /**
     * A stub ReadOnlyAddressBook whose persons list can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();
        private final ObservableList<Shift> shifts = FXCollections.observableArrayList();

        private AddressBookStub(Collection<Person> persons, Collection<Shift> shifts) {
            this.persons.setAll(persons);
            this.shifts.setAll(shifts);
        }

        public static AddressBookStub createAddressBookStubWithPersons(Collection<Person> persons) {
            return new AddressBookStub(persons, Collections.emptyList());
        }

        public static AddressBookStub createAddressBookStubWithShifts(Collection<Shift> shifts) {
            return new AddressBookStub(Collections.emptyList(), shifts);
        }


        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }

        @Override
        public ObservableList<Shift> getShiftList() {
            return shifts;
        }
    }

}
