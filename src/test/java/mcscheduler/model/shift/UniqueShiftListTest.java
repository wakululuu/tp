package mcscheduler.model.shift;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import mcscheduler.logic.commands.CommandTestUtil;
import mcscheduler.model.shift.exceptions.DuplicateShiftException;
import mcscheduler.model.shift.exceptions.ShiftNotFoundException;
import mcscheduler.testutil.Assert;
import mcscheduler.testutil.ShiftBuilder;
import mcscheduler.testutil.TypicalShifts;

public class UniqueShiftListTest {

    private final UniqueShiftList uniqueShiftList = new UniqueShiftList();

    @Test
    public void contains() {
        // null
        Assert.assertThrows(NullPointerException.class, () -> uniqueShiftList.contains(null));

        // shift not in list
        assertFalse(uniqueShiftList.contains(TypicalShifts.SHIFT_A));

        // shift in list
        uniqueShiftList.add(TypicalShifts.SHIFT_A);
        assertTrue(uniqueShiftList.contains(TypicalShifts.SHIFT_A));

        // same identity shift in list
        assertTrue(uniqueShiftList.contains(new ShiftBuilder(TypicalShifts.SHIFT_A)
            .withRoleRequirements(CommandTestUtil.VALID_ROLE_REQUIREMENT_CHEF).build()));

    }

    @Test
    public void add_nullShift_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> uniqueShiftList.add(null));
    }

    @Test
    public void add_duplicateShift_throwsDuplicateShiftException() {
        uniqueShiftList.add(TypicalShifts.SHIFT_A);
        Assert.assertThrows(DuplicateShiftException.class, () -> uniqueShiftList.add(TypicalShifts.SHIFT_A));
    }

    @Test
    public void setShift_nullShift_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> uniqueShiftList.setShift(null, TypicalShifts.SHIFT_A));
        Assert.assertThrows(NullPointerException.class, () -> uniqueShiftList.setShift(TypicalShifts.SHIFT_A, null));
    }

    @Test
    public void setShift_targetShiftNotInList_throwsShiftNotFoundException() {
        Assert.assertThrows(ShiftNotFoundException.class, () ->
            uniqueShiftList.setShift(TypicalShifts.SHIFT_A, TypicalShifts.SHIFT_B));
    }

    @Test
    public void setShift_editedShiftHasDifferentNonUniqueIdentity_throwsDuplicateShiftException() {
        uniqueShiftList.add(TypicalShifts.SHIFT_A);
        uniqueShiftList.add(TypicalShifts.SHIFT_B);
        Assert.assertThrows(DuplicateShiftException.class, () ->
            uniqueShiftList.setShift(TypicalShifts.SHIFT_A, TypicalShifts.SHIFT_B));
    }

    @Test
    public void setShift_editedShiftHasSameIdentity_success() {
        // exact same values
        uniqueShiftList.add(TypicalShifts.SHIFT_A);
        uniqueShiftList.setShift(TypicalShifts.SHIFT_A, TypicalShifts.SHIFT_A);
        UniqueShiftList expected = new UniqueShiftList();
        expected.add(TypicalShifts.SHIFT_A);
        assertEquals(uniqueShiftList, expected);


        // different field values
        Shift editedShift = new ShiftBuilder(TypicalShifts.SHIFT_A).withRoleRequirements(
            CommandTestUtil.VALID_ROLE_REQUIREMENT_CHEF).build();
        uniqueShiftList.setShift(TypicalShifts.SHIFT_A, editedShift);
        expected = new UniqueShiftList();
        expected.add(editedShift);
        assertEquals(uniqueShiftList, expected);
    }

    @Test
    public void setShift_editedShiftHasDifferentIdentity_success() {
        uniqueShiftList.add(TypicalShifts.SHIFT_A);
        uniqueShiftList.setShift(TypicalShifts.SHIFT_A, TypicalShifts.SHIFT_B);
        UniqueShiftList expected = new UniqueShiftList();
        expected.add(TypicalShifts.SHIFT_B);
        assertEquals(uniqueShiftList, expected);
    }

    @Test
    public void remove_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> uniqueShiftList.remove(null));
    }

    @Test
    public void remove_shiftNotInList_throwsShiftNotFoundException() {
        Assert.assertThrows(ShiftNotFoundException.class, () -> uniqueShiftList.remove(TypicalShifts.SHIFT_A));
    }

    @Test
    public void remove_shiftInList_success() {
        uniqueShiftList.add(TypicalShifts.SHIFT_A);
        uniqueShiftList.remove(TypicalShifts.SHIFT_A);
        assertEquals(uniqueShiftList, new UniqueShiftList());
    }

    @Test
    public void setShifts_nullUniqueShiftList_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> uniqueShiftList.setShifts((UniqueShiftList) null));
    }

    @Test
    public void setShifts_uniqueShiftList_success() {
        uniqueShiftList.add(TypicalShifts.SHIFT_A);
        UniqueShiftList anotherList = new UniqueShiftList();
        anotherList.add(TypicalShifts.SHIFT_B);
        uniqueShiftList.setShifts(anotherList);
        assertEquals(uniqueShiftList, anotherList);
    }

    @Test
    public void setShifts_nullList_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> uniqueShiftList.setShifts((List<Shift>) null));
    }

    @Test
    public void setShifts_listWithDuplicates_throwsDuplicateShiftException() {
        List<Shift> listWithDuplicates = Arrays.asList(TypicalShifts.SHIFT_A, TypicalShifts.SHIFT_A);
        Assert.assertThrows(DuplicateShiftException.class, () -> uniqueShiftList.setShifts(listWithDuplicates));
    }

    @Test
    public void setShifts_list_success() {
        List<Shift> list = Arrays.asList(TypicalShifts.SHIFT_A, TypicalShifts.SHIFT_B);
        uniqueShiftList.setShifts(list);
        UniqueShiftList expected = new UniqueShiftList();
        expected.add(TypicalShifts.SHIFT_A);
        expected.add(TypicalShifts.SHIFT_B);
        assertEquals(expected, uniqueShiftList);
    }

    @Test
    public void asUnmodifiableObservableList_modify_throwsUnsupportedOperationException() {
        Assert.assertThrows(UnsupportedOperationException.class, () -> uniqueShiftList
            .asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void equals() {
        uniqueShiftList.add(TypicalShifts.SHIFT_A);

        // same object
        assertEquals(uniqueShiftList, uniqueShiftList);

        // null
        assertNotEquals(uniqueShiftList, null);

        // different type
        assertNotEquals(uniqueShiftList, 123);

        // same content
        UniqueShiftList sameList = new UniqueShiftList();
        sameList.setShifts(uniqueShiftList);
        assertEquals(sameList, uniqueShiftList);

        // different content
        UniqueShiftList differentList = new UniqueShiftList();
        differentList.add(TypicalShifts.SHIFT_B);
        differentList.add(TypicalShifts.SHIFT_A);
        assertNotEquals(differentList, uniqueShiftList);
    }

}
