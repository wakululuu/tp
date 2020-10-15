package seedu.address.model.worker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_CASHIER;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalWorkers.ALICE;
import static seedu.address.testutil.TypicalWorkers.BOB;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.worker.exceptions.DuplicateWorkerException;
import seedu.address.model.worker.exceptions.WorkerNotFoundException;
import seedu.address.testutil.WorkerBuilder;

public class UniqueWorkerListTest {

    private final UniqueWorkerList uniqueWorkerList = new UniqueWorkerList();

    @Test
    public void contains_nullWorker_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueWorkerList.contains(null));
    }

    @Test
    public void contains_workerNotInList_returnsFalse() {
        assertFalse(uniqueWorkerList.contains(ALICE));
    }

    @Test
    public void contains_workerInList_returnsTrue() {
        uniqueWorkerList.add(ALICE);
        assertTrue(uniqueWorkerList.contains(ALICE));
    }

    @Test
    public void contains_workerWithSameIdentityFieldsInList_returnsTrue() {
        uniqueWorkerList.add(ALICE);
        Worker editedAlice = new WorkerBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withRoles(VALID_ROLE_CASHIER)
                .build();
        assertTrue(uniqueWorkerList.contains(editedAlice));
    }

    @Test
    public void add_nullWorker_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueWorkerList.add(null));
    }

    @Test
    public void add_duplicateWorker_throwsDuplicateWorkerException() {
        uniqueWorkerList.add(ALICE);
        assertThrows(DuplicateWorkerException.class, () -> uniqueWorkerList.add(ALICE));
    }

    @Test
    public void setWorker_nullTargetWorker_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueWorkerList.setWorker(null, ALICE));
    }

    @Test
    public void setWorker_nullEditedWorker_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueWorkerList.setWorker(ALICE, null));
    }

    @Test
    public void setWorker_targetWorkerNotInList_throwsWorkerNotFoundException() {
        assertThrows(WorkerNotFoundException.class, () -> uniqueWorkerList.setWorker(ALICE, ALICE));
    }

    @Test
    public void setWorker_editedWorkerIsSameWorker_success() {
        uniqueWorkerList.add(ALICE);
        uniqueWorkerList.setWorker(ALICE, ALICE);
        UniqueWorkerList expectedUniqueWorkerList = new UniqueWorkerList();
        expectedUniqueWorkerList.add(ALICE);
        assertEquals(expectedUniqueWorkerList, uniqueWorkerList);
    }

    @Test
    public void setWorker_editedWorkerHasSameIdentity_success() {
        uniqueWorkerList.add(ALICE);
        Worker editedAlice = new WorkerBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withRoles(VALID_ROLE_CASHIER)
                .build();
        uniqueWorkerList.setWorker(ALICE, editedAlice);
        UniqueWorkerList expectedUniqueWorkerList = new UniqueWorkerList();
        expectedUniqueWorkerList.add(editedAlice);
        assertEquals(expectedUniqueWorkerList, uniqueWorkerList);
    }

    @Test
    public void setWorker_editedWorkerHasDifferentIdentity_success() {
        uniqueWorkerList.add(ALICE);
        uniqueWorkerList.setWorker(ALICE, BOB);
        UniqueWorkerList expectedUniqueWorkerList = new UniqueWorkerList();
        expectedUniqueWorkerList.add(BOB);
        assertEquals(expectedUniqueWorkerList, uniqueWorkerList);
    }

    @Test
    public void setWorker_editedWorkerHasNonUniqueIdentity_throwsDuplicateWorkerException() {
        uniqueWorkerList.add(ALICE);
        uniqueWorkerList.add(BOB);
        assertThrows(DuplicateWorkerException.class, () -> uniqueWorkerList.setWorker(ALICE, BOB));
    }

    @Test
    public void remove_nullWorker_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueWorkerList.remove(null));
    }

    @Test
    public void remove_workerDoesNotExist_throwsWorkerNotFoundException() {
        assertThrows(WorkerNotFoundException.class, () -> uniqueWorkerList.remove(ALICE));
    }

    @Test
    public void remove_existingWorker_removesWorker() {
        uniqueWorkerList.add(ALICE);
        uniqueWorkerList.remove(ALICE);
        UniqueWorkerList expectedUniqueWorkerList = new UniqueWorkerList();
        assertEquals(expectedUniqueWorkerList, uniqueWorkerList);
    }

    @Test
    public void setWorkers_nullUniqueWorkerList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueWorkerList.setWorkers((UniqueWorkerList) null));
    }

    @Test
    public void setWorkers_uniqueWorkerList_replacesOwnListWithProvidedUniqueWorkerList() {
        uniqueWorkerList.add(ALICE);
        UniqueWorkerList expectedUniqueWorkerList = new UniqueWorkerList();
        expectedUniqueWorkerList.add(BOB);
        uniqueWorkerList.setWorkers(expectedUniqueWorkerList);
        assertEquals(expectedUniqueWorkerList, uniqueWorkerList);
    }

    @Test
    public void setWorkers_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueWorkerList.setWorkers((List<Worker>) null));
    }

    @Test
    public void setWorkers_list_replacesOwnListWithProvidedList() {
        uniqueWorkerList.add(ALICE);
        List<Worker> workerList = Collections.singletonList(BOB);
        uniqueWorkerList.setWorkers(workerList);
        UniqueWorkerList expectedUniqueWorkerList = new UniqueWorkerList();
        expectedUniqueWorkerList.add(BOB);
        assertEquals(expectedUniqueWorkerList, uniqueWorkerList);
    }

    @Test
    public void setWorkers_listWithDuplicateWorkers_throwsDuplicateWorkerException() {
        List<Worker> listWithDuplicateWorkers = Arrays.asList(ALICE, ALICE);
        assertThrows(DuplicateWorkerException.class, () -> uniqueWorkerList.setWorkers(listWithDuplicateWorkers));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
            -> uniqueWorkerList.asUnmodifiableObservableList().remove(0));
    }
}
