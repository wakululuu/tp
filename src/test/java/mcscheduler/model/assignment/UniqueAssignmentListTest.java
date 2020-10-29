package mcscheduler.model.assignment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import mcscheduler.logic.commands.CommandTestUtil;
import mcscheduler.model.assignment.exceptions.AssignmentNotFoundException;
import mcscheduler.model.assignment.exceptions.DuplicateAssignmentException;
import mcscheduler.testutil.Assert;
import mcscheduler.testutil.AssignmentBuilder;
import mcscheduler.testutil.TypicalAssignments;

public class UniqueAssignmentListTest {

    private final UniqueAssignmentList uniqueAssignmentList = new UniqueAssignmentList();

    @Test
    public void contains_nullAssignment_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> uniqueAssignmentList.contains(null));
    }

    @Test
    public void contains_assignmentNotInList_returnsFalse() {
        assertFalse(uniqueAssignmentList.contains(TypicalAssignments.ASSIGNMENT_A));
    }

    @Test
    public void contains_assignmentInList_returnsTrue() {
        uniqueAssignmentList.add(TypicalAssignments.ASSIGNMENT_A);
        assertTrue(uniqueAssignmentList.contains(TypicalAssignments.ASSIGNMENT_A));
    }

    @Test
    public void contains_assignmentWithSameIdentityFieldsInList_returnsTrue() {
        uniqueAssignmentList.add(TypicalAssignments.ASSIGNMENT_A);
        Assignment editedAssignmentA = new AssignmentBuilder(TypicalAssignments.ASSIGNMENT_A).withRole(
            CommandTestUtil.VALID_ROLE_CHEF).build();
        assertTrue(uniqueAssignmentList.contains(editedAssignmentA));
    }

    @Test
    public void add_nullAssignment_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> uniqueAssignmentList.add(null));
    }

    @Test
    public void add_duplicateAssignmentWithSameRole_throwsDuplicateAssignmentException() {
        uniqueAssignmentList.add(TypicalAssignments.ASSIGNMENT_A);
        Assert.assertThrows(DuplicateAssignmentException.class, () -> uniqueAssignmentList.add(
            TypicalAssignments.ASSIGNMENT_A));
    }

    @Test
    public void add_duplicateAssignmentWithDifferentRole_throwsDuplicateAssignmentException() {
        uniqueAssignmentList.add(TypicalAssignments.ASSIGNMENT_A);
        Assignment editedAssignmentA = new AssignmentBuilder(TypicalAssignments.ASSIGNMENT_A).withRole(
            CommandTestUtil.VALID_ROLE_CHEF).build();
        Assert.assertThrows(DuplicateAssignmentException.class, () -> uniqueAssignmentList.add(editedAssignmentA));
    }

    @Test
    public void setAssignment_nullTargetAssignment_throwsNullPointerException() {
        Assert
            .assertThrows(NullPointerException.class, () ->
                uniqueAssignmentList.setAssignment(null, TypicalAssignments.ASSIGNMENT_A));
    }

    @Test
    public void setAssignment_nullEditedAssignment_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> uniqueAssignmentList.setAssignment(
            TypicalAssignments.ASSIGNMENT_A, null));
    }

    @Test
    public void setAssignment_targetAssignmentNotInList_throwsAssignmentNotFoundException() {
        Assert.assertThrows(AssignmentNotFoundException.class, () ->
            uniqueAssignmentList.setAssignment(TypicalAssignments.ASSIGNMENT_A, TypicalAssignments.ASSIGNMENT_B));
    }

    @Test
    public void setAssignment_editedAssignmentIsSameAssignment_success() {
        uniqueAssignmentList.add(TypicalAssignments.ASSIGNMENT_A);
        uniqueAssignmentList.setAssignment(TypicalAssignments.ASSIGNMENT_A, TypicalAssignments.ASSIGNMENT_A);
        UniqueAssignmentList expectedUniqueAssignmentList = new UniqueAssignmentList();
        expectedUniqueAssignmentList.add(TypicalAssignments.ASSIGNMENT_A);
        assertEquals(expectedUniqueAssignmentList, uniqueAssignmentList);
    }

    @Test
    public void setAssignment_editedAssignmentHasSameIdentity_success() {
        uniqueAssignmentList.add(TypicalAssignments.ASSIGNMENT_A);
        Assignment editedAssignmentA = new AssignmentBuilder(TypicalAssignments.ASSIGNMENT_A).withRole(
            CommandTestUtil.VALID_ROLE_CHEF).build();
        uniqueAssignmentList.setAssignment(TypicalAssignments.ASSIGNMENT_A, editedAssignmentA);
        UniqueAssignmentList expectedUniqueAssignmentList = new UniqueAssignmentList();
        expectedUniqueAssignmentList.add(editedAssignmentA);
        assertEquals(expectedUniqueAssignmentList, uniqueAssignmentList);
    }

    @Test
    public void setAssignment_editedAssignmentHasDifferentIdentity_success() {
        uniqueAssignmentList.add(TypicalAssignments.ASSIGNMENT_A);
        uniqueAssignmentList.setAssignment(TypicalAssignments.ASSIGNMENT_A, TypicalAssignments.ASSIGNMENT_B);
        UniqueAssignmentList expectedUniqueAssignmentList = new UniqueAssignmentList();
        expectedUniqueAssignmentList.add(TypicalAssignments.ASSIGNMENT_B);
        assertEquals(expectedUniqueAssignmentList, uniqueAssignmentList);
    }

    @Test
    public void setAssignment_editedAssignmentHasNonUniqueIdentity_throwsDuplicateAssignmentException() {
        uniqueAssignmentList.add(TypicalAssignments.ASSIGNMENT_A);
        uniqueAssignmentList.add(TypicalAssignments.ASSIGNMENT_B);
        Assert.assertThrows(DuplicateAssignmentException.class, () ->
            uniqueAssignmentList.setAssignment(TypicalAssignments.ASSIGNMENT_A, TypicalAssignments.ASSIGNMENT_B));
    }

    @Test
    public void remove_nullAssignment_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> uniqueAssignmentList.remove(null));
    }

    @Test
    public void remove_assignmentDoesNotExist_throwsAssignmentNotFoundException() {
        Assert.assertThrows(AssignmentNotFoundException.class, () -> uniqueAssignmentList.remove(
            TypicalAssignments.ASSIGNMENT_A));
    }

    @Test
    public void remove_existingAssignment_removesAssignment() {
        uniqueAssignmentList.add(TypicalAssignments.ASSIGNMENT_A);
        uniqueAssignmentList.remove(TypicalAssignments.ASSIGNMENT_A);
        UniqueAssignmentList expectedUniqueAssignmentList = new UniqueAssignmentList();
        assertEquals(expectedUniqueAssignmentList, uniqueAssignmentList);
    }

    @Test
    public void setAssignments_nullUniqueAssignmentList_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () ->
            uniqueAssignmentList.setAssignments((UniqueAssignmentList) null));
    }

    @Test
    public void setAssignments_uniqueAssignmentList_replacesOwnListWithProvidedUniqueAssignmentList() {
        uniqueAssignmentList.add(TypicalAssignments.ASSIGNMENT_A);
        UniqueAssignmentList expectedUniqueAssignmentList = new UniqueAssignmentList();
        expectedUniqueAssignmentList.add(TypicalAssignments.ASSIGNMENT_B);
        uniqueAssignmentList.setAssignments(expectedUniqueAssignmentList);
        assertEquals(expectedUniqueAssignmentList, uniqueAssignmentList);
    }

    @Test
    public void setAssignments_nullList_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () ->
            uniqueAssignmentList.setAssignments((List<Assignment>) null));
    }

    @Test
    public void setAssignments_list_replacesOwnListWithProvidedList() {
        uniqueAssignmentList.add(TypicalAssignments.ASSIGNMENT_A);
        List<Assignment> assignmentList = Collections.singletonList(TypicalAssignments.ASSIGNMENT_B);
        uniqueAssignmentList.setAssignments(assignmentList);
        UniqueAssignmentList expectedUniqueAssignmentList = new UniqueAssignmentList();
        expectedUniqueAssignmentList.add(TypicalAssignments.ASSIGNMENT_B);
        assertEquals(expectedUniqueAssignmentList, uniqueAssignmentList);
    }

    @Test
    public void setAssignments_listWithDuplicateAssignments_throwsDuplicateAssignmentException() {
        List<Assignment> listWithDuplicateAssignments =
            Arrays.asList(TypicalAssignments.ASSIGNMENT_A, TypicalAssignments.ASSIGNMENT_A);
        Assert.assertThrows(DuplicateAssignmentException.class, () ->
            uniqueAssignmentList.setAssignments(listWithDuplicateAssignments));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        Assert.assertThrows(UnsupportedOperationException.class, () ->
            uniqueAssignmentList.asUnmodifiableObservableList().remove(0));
    }
}
