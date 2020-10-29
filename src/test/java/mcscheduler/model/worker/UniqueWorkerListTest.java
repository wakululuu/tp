package mcscheduler.model.worker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import mcscheduler.logic.commands.CommandTestUtil;
import mcscheduler.model.worker.exceptions.DuplicateWorkerException;
import mcscheduler.model.worker.exceptions.WorkerNotFoundException;
import mcscheduler.testutil.Assert;
import mcscheduler.testutil.TypicalWorkers;
import mcscheduler.testutil.WorkerBuilder;

public class UniqueWorkerListTest {

    private final UniqueWorkerList uniqueWorkerList = new UniqueWorkerList();

    @Test
    public void contains_nullWorker_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> uniqueWorkerList.contains(null));
    }

    @Test
    public void contains_workerNotInList_returnsFalse() {
        assertFalse(uniqueWorkerList.contains(TypicalWorkers.ALICE));
    }

    @Test
    public void contains_workerInList_returnsTrue() {
        uniqueWorkerList.add(TypicalWorkers.ALICE);
        assertTrue(uniqueWorkerList.contains(TypicalWorkers.ALICE));
    }

    @Test
    public void contains_workerWithSameIdentityFieldsInList_returnsTrue() {
        uniqueWorkerList.add(TypicalWorkers.ALICE);
        Worker editedAlice =
            new WorkerBuilder(TypicalWorkers.ALICE).withAddress(CommandTestUtil.VALID_ADDRESS_BOB).withRoles(
                CommandTestUtil.VALID_ROLE_CASHIER)
                .build();
        assertTrue(uniqueWorkerList.contains(editedAlice));
    }

    @Test
    public void add_nullWorker_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> uniqueWorkerList.add(null));
    }

    @Test
    public void add_duplicateWorker_throwsDuplicateWorkerException() {
        uniqueWorkerList.add(TypicalWorkers.ALICE);
        Assert.assertThrows(DuplicateWorkerException.class, () -> uniqueWorkerList.add(TypicalWorkers.ALICE));
    }

    @Test
    public void setWorker_nullTargetWorker_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> uniqueWorkerList.setWorker(null, TypicalWorkers.ALICE));
    }

    @Test
    public void setWorker_nullEditedWorker_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> uniqueWorkerList.setWorker(TypicalWorkers.ALICE, null));
    }

    @Test
    public void setWorker_targetWorkerNotInList_throwsWorkerNotFoundException() {
        Assert.assertThrows(WorkerNotFoundException.class, () ->
            uniqueWorkerList.setWorker(TypicalWorkers.ALICE, TypicalWorkers.ALICE));
    }

    @Test
    public void setWorker_editedWorkerIsSameWorker_success() {
        uniqueWorkerList.add(TypicalWorkers.ALICE);
        uniqueWorkerList.setWorker(TypicalWorkers.ALICE, TypicalWorkers.ALICE);
        UniqueWorkerList expectedUniqueWorkerList = new UniqueWorkerList();
        expectedUniqueWorkerList.add(TypicalWorkers.ALICE);
        assertEquals(expectedUniqueWorkerList, uniqueWorkerList);
    }

    @Test
    public void setWorker_editedWorkerHasSameIdentity_success() {
        uniqueWorkerList.add(TypicalWorkers.ALICE);
        Worker editedAlice =
            new WorkerBuilder(TypicalWorkers.ALICE).withAddress(CommandTestUtil.VALID_ADDRESS_BOB).withRoles(
                CommandTestUtil.VALID_ROLE_CASHIER)
                .build();
        uniqueWorkerList.setWorker(TypicalWorkers.ALICE, editedAlice);
        UniqueWorkerList expectedUniqueWorkerList = new UniqueWorkerList();
        expectedUniqueWorkerList.add(editedAlice);
        assertEquals(expectedUniqueWorkerList, uniqueWorkerList);
    }

    @Test
    public void setWorker_editedWorkerHasDifferentIdentity_success() {
        uniqueWorkerList.add(TypicalWorkers.ALICE);
        uniqueWorkerList.setWorker(TypicalWorkers.ALICE, TypicalWorkers.BOB);
        UniqueWorkerList expectedUniqueWorkerList = new UniqueWorkerList();
        expectedUniqueWorkerList.add(TypicalWorkers.BOB);
        assertEquals(expectedUniqueWorkerList, uniqueWorkerList);
    }

    @Test
    public void setWorker_editedWorkerHasNonUniqueIdentity_throwsDuplicateWorkerException() {
        uniqueWorkerList.add(TypicalWorkers.ALICE);
        uniqueWorkerList.add(TypicalWorkers.BOB);
        Assert.assertThrows(DuplicateWorkerException.class, () ->
            uniqueWorkerList.setWorker(TypicalWorkers.ALICE, TypicalWorkers.BOB));
    }

    @Test
    public void remove_nullWorker_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> uniqueWorkerList.remove(null));
    }

    @Test
    public void remove_workerDoesNotExist_throwsWorkerNotFoundException() {
        Assert.assertThrows(WorkerNotFoundException.class, () -> uniqueWorkerList.remove(TypicalWorkers.ALICE));
    }

    @Test
    public void remove_existingWorker_removesWorker() {
        uniqueWorkerList.add(TypicalWorkers.ALICE);
        uniqueWorkerList.remove(TypicalWorkers.ALICE);
        UniqueWorkerList expectedUniqueWorkerList = new UniqueWorkerList();
        assertEquals(expectedUniqueWorkerList, uniqueWorkerList);
    }

    @Test
    public void setWorkers_nullUniqueWorkerList_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> uniqueWorkerList.setWorkers((UniqueWorkerList) null));
    }

    @Test
    public void setWorkers_uniqueWorkerList_replacesOwnListWithProvidedUniqueWorkerList() {
        uniqueWorkerList.add(TypicalWorkers.ALICE);
        UniqueWorkerList expectedUniqueWorkerList = new UniqueWorkerList();
        expectedUniqueWorkerList.add(TypicalWorkers.BOB);
        uniqueWorkerList.setWorkers(expectedUniqueWorkerList);
        assertEquals(expectedUniqueWorkerList, uniqueWorkerList);
    }

    @Test
    public void setWorkers_nullList_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> uniqueWorkerList.setWorkers((List<Worker>) null));
    }

    @Test
    public void setWorkers_list_replacesOwnListWithProvidedList() {
        uniqueWorkerList.add(TypicalWorkers.ALICE);
        List<Worker> workerList = Collections.singletonList(TypicalWorkers.BOB);
        uniqueWorkerList.setWorkers(workerList);
        UniqueWorkerList expectedUniqueWorkerList = new UniqueWorkerList();
        expectedUniqueWorkerList.add(TypicalWorkers.BOB);
        assertEquals(expectedUniqueWorkerList, uniqueWorkerList);
    }

    @Test
    public void setWorkers_listWithDuplicateWorkers_throwsDuplicateWorkerException() {
        List<Worker> listWithDuplicateWorkers = Arrays.asList(TypicalWorkers.ALICE, TypicalWorkers.ALICE);
        Assert
            .assertThrows(DuplicateWorkerException.class, () -> uniqueWorkerList.setWorkers(listWithDuplicateWorkers));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        Assert.assertThrows(UnsupportedOperationException.class, ()
            -> uniqueWorkerList.asUnmodifiableObservableList().remove(0));
    }
}
