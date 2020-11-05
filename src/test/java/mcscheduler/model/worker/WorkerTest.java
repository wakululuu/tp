package mcscheduler.model.worker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import mcscheduler.logic.commands.CommandTestUtil;
import mcscheduler.testutil.Assert;
import mcscheduler.testutil.TypicalWorkers;
import mcscheduler.testutil.WorkerBuilder;

public class WorkerTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Worker worker = new WorkerBuilder().build();
        Assert.assertThrows(UnsupportedOperationException.class, () -> worker.getRoles().remove(0));
    }

    @Test
    public void isSameWorker() {
        // same object -> returns true
        assertTrue(TypicalWorkers.ALICE.isSameWorker(TypicalWorkers.ALICE));

        // null -> returns false
        assertFalse(TypicalWorkers.ALICE.isSameWorker(null));

        // different phone -> returns false
        Worker editedAlice = new WorkerBuilder(TypicalWorkers.ALICE).withPhone(CommandTestUtil.VALID_PHONE_BOB).build();
        assertFalse(TypicalWorkers.ALICE.isSameWorker(editedAlice));

        // different name -> returns false
        editedAlice = new WorkerBuilder(TypicalWorkers.ALICE).withName(CommandTestUtil.VALID_NAME_BOB).build();
        assertFalse(TypicalWorkers.ALICE.isSameWorker(editedAlice));

        // same name, same phone, different attributes -> returns true
        editedAlice = new WorkerBuilder(TypicalWorkers.ALICE)
                .withPay(CommandTestUtil.VALID_PAY_BOB)
                .withAddress(CommandTestUtil.VALID_ADDRESS_BOB)
                .withRoles(CommandTestUtil.VALID_ROLE_CASHIER)
                .withUnavailableTimings(CommandTestUtil.VALID_UNAVAILABILITY).build();
        assertTrue(TypicalWorkers.ALICE.isSameWorker(editedAlice));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Worker aliceCopy = new WorkerBuilder(TypicalWorkers.ALICE).build();
        assertEquals(aliceCopy, TypicalWorkers.ALICE);

        // same object -> returns true
        assertEquals(TypicalWorkers.ALICE, TypicalWorkers.ALICE);

        // null -> returns false
        assertNotEquals(TypicalWorkers.ALICE, null);

        // different type -> returns false
        assertNotEquals(TypicalWorkers.ALICE, 5);

        // different worker -> returns false
        assertNotEquals(TypicalWorkers.BOB, TypicalWorkers.ALICE);

        // different name -> returns false
        Worker editedAlice = new WorkerBuilder(TypicalWorkers.ALICE).withName(CommandTestUtil.VALID_NAME_BOB).build();
        assertNotEquals(editedAlice, TypicalWorkers.ALICE);

        // different phone -> returns false
        editedAlice = new WorkerBuilder(TypicalWorkers.ALICE).withPhone(CommandTestUtil.VALID_PHONE_BOB).build();
        assertNotEquals(editedAlice, TypicalWorkers.ALICE);

        // different pay -> returns false
        editedAlice = new WorkerBuilder(TypicalWorkers.ALICE).withPay(CommandTestUtil.VALID_PAY_BOB).build();
        assertNotEquals(editedAlice, TypicalWorkers.ALICE);

        // different address -> returns false
        editedAlice = new WorkerBuilder(TypicalWorkers.ALICE).withAddress(CommandTestUtil.VALID_ADDRESS_BOB).build();
        assertNotEquals(editedAlice, TypicalWorkers.ALICE);

        // different roles -> returns false
        editedAlice = new WorkerBuilder(TypicalWorkers.ALICE).withRoles(CommandTestUtil.VALID_ROLE_CHEF).build();
        assertNotEquals(editedAlice, TypicalWorkers.ALICE);

        // different unavailabilities -> returns false
        editedAlice = new WorkerBuilder(TypicalWorkers.ALICE)
                .withUnavailableTimings(CommandTestUtil.VALID_UNAVAILABILITY).build();
        assertNotEquals(editedAlice, TypicalWorkers.ALICE);
    }
}
