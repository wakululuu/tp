package seedu.address.model.shift;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_REQUIREMENT_CHEF;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalShifts.SHIFT_A;
import static seedu.address.testutil.TypicalShifts.SHIFT_B;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.shift.exceptions.DuplicateShiftException;
import seedu.address.model.shift.exceptions.ShiftNotFoundException;
import seedu.address.testutil.ShiftBuilder;

public class UniqueShiftListTest {

    private UniqueShiftList uniqueShiftList = new UniqueShiftList();

    @Test
    public void contains() {
        // null
        assertThrows(NullPointerException.class, () -> uniqueShiftList.contains(null));

        // shift not in list
        assertFalse(uniqueShiftList.contains(SHIFT_A));

        // shift in list
        uniqueShiftList.add(SHIFT_A);
        assertTrue(uniqueShiftList.contains(SHIFT_A));

        // same identity shift in list
        assertTrue(uniqueShiftList.contains(new ShiftBuilder(SHIFT_A)
                .withRoleRequirements(VALID_ROLE_REQUIREMENT_CHEF).build()));

    }

    @Test
    public void add_nullShift_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueShiftList.add(null));
    }

    @Test
    public void add_duplicateShift_throwsDuplicateShiftException() {
        uniqueShiftList.add(SHIFT_A);
        assertThrows(DuplicateShiftException.class, () -> uniqueShiftList.add(SHIFT_A));
    }

    @Test
    public void setShift_nullShift_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueShiftList.setShift(null, SHIFT_A));
        assertThrows(NullPointerException.class, () -> uniqueShiftList.setShift(SHIFT_A, null));
    }

    @Test
    public void setShift_targetShiftNotInList_throwsShiftNotFoundException() {
        assertThrows(ShiftNotFoundException.class, () -> uniqueShiftList.setShift(SHIFT_A, SHIFT_B));
    }

    @Test
    public void setShift_editedShiftHasDifferentNonUniqueIdentity_throwsDuplicateShiftException() {
        uniqueShiftList.add(SHIFT_A);
        uniqueShiftList.add(SHIFT_B);
        assertThrows(DuplicateShiftException.class, () -> uniqueShiftList.setShift(SHIFT_A, SHIFT_B));
    }

    @Test
    public void setShift_editedShiftHasSameIdentity_success() {
        // exact same values
        uniqueShiftList.add(SHIFT_A);
        uniqueShiftList.setShift(SHIFT_A, SHIFT_A);
        UniqueShiftList expected = new UniqueShiftList();
        expected.add(SHIFT_A);
        assertEquals(uniqueShiftList, expected);


        // different field values
        Shift editedShift = new ShiftBuilder(SHIFT_A).withRoleRequirements(VALID_ROLE_REQUIREMENT_CHEF).build();
        uniqueShiftList.setShift(SHIFT_A, editedShift);
        expected = new UniqueShiftList();
        expected.add(editedShift);
        assertEquals(uniqueShiftList, expected);
    }

    @Test
    public void setShift_editedShiftHasDifferentIdentity_success() {
        uniqueShiftList.add(SHIFT_A);
        uniqueShiftList.setShift(SHIFT_A, SHIFT_B);
        UniqueShiftList expected = new UniqueShiftList();
        expected.add(SHIFT_B);
        assertEquals(uniqueShiftList, expected);
    }

    @Test
    public void remove_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueShiftList.remove(null));
    }

    @Test
    public void remove_shiftNotInList_throwsShiftNotFoundException() {
        assertThrows(ShiftNotFoundException.class, () -> uniqueShiftList.remove(SHIFT_A));
    }

    @Test
    public void remove_shiftInList_success() {
        uniqueShiftList.add(SHIFT_A);
        uniqueShiftList.remove(SHIFT_A);
        assertEquals(uniqueShiftList, new UniqueShiftList());
    }

    @Test
    public void setShifts_nullUniqueShiftList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueShiftList.setShifts((UniqueShiftList) null));
    }

    @Test
    public void setShifts_uniqueShiftList_success() {
        uniqueShiftList.add(SHIFT_A);
        UniqueShiftList anotherList = new UniqueShiftList();
        anotherList.add(SHIFT_B);
        uniqueShiftList.setShifts(anotherList);
        assertEquals(uniqueShiftList, anotherList);
    }

    @Test
    public void setShifts_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueShiftList.setShifts((List<Shift>) null));
    }

    @Test
    public void setShifts_listWithDuplicates_throwsDuplicateShiftException() {
        List<Shift> listWithDuplicates = Arrays.asList(SHIFT_A, SHIFT_A);
        assertThrows(DuplicateShiftException.class, () -> uniqueShiftList.setShifts(listWithDuplicates));
    }

    @Test
    public void setShifts_list_success() {
        List<Shift> list = Arrays.asList(SHIFT_A, SHIFT_B);
        uniqueShiftList.setShifts(list);
        UniqueShiftList expected = new UniqueShiftList();
        expected.add(SHIFT_A);
        expected.add(SHIFT_B);
        assertEquals(expected, uniqueShiftList);
    }

    @Test
    public void asUnmodifiableObservableList_modify_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> uniqueShiftList
                .asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void equals() {
        uniqueShiftList.add(SHIFT_A);

        // same object
        assertTrue(uniqueShiftList.equals(uniqueShiftList));

        // null
        assertFalse(uniqueShiftList.equals(null));

        // different type
        assertFalse(uniqueShiftList.equals(123));

        // same content
        UniqueShiftList sameList = new UniqueShiftList();
        sameList.setShifts(uniqueShiftList);
        assertTrue(uniqueShiftList.equals(sameList));

        // different content
        UniqueShiftList differentList = new UniqueShiftList();
        differentList.add(SHIFT_B);
        differentList.add(SHIFT_A);
        assertFalse(uniqueShiftList.equals(differentList));
    }

}
