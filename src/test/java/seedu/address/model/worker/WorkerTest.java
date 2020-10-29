package seedu.address.model.worker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
//import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PAY_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_CASHIER;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_CHEF;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalWorkers.ALICE;
import static seedu.address.testutil.TypicalWorkers.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.WorkerBuilder;

public class WorkerTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Worker worker = new WorkerBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> worker.getRoles().remove(0));
    }

    @Test
    public void isSameWorker() {
        // same object -> returns true
        assertTrue(ALICE.isSameWorker(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSameWorker(null));

        // different phone -> returns false
        Worker editedAlice = new WorkerBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.isSameWorker(editedAlice));

        // different name -> returns false
        editedAlice = new WorkerBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSameWorker(editedAlice));

        // same name, same phone, different attributes -> returns true
        editedAlice = new WorkerBuilder(ALICE).withPay(VALID_PAY_BOB).withAddress(VALID_ADDRESS_BOB)
                .withRoles(VALID_ROLE_CASHIER).build();
        assertTrue(ALICE.isSameWorker(editedAlice));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Worker aliceCopy = new WorkerBuilder(ALICE).build();
        assertEquals(aliceCopy, ALICE);

        // same object -> returns true
        assertEquals(ALICE, ALICE);

        // null -> returns false
        assertNotEquals(ALICE, null);

        // different type -> returns false
        assertNotEquals(ALICE, 5);

        // different worker -> returns false
        assertNotEquals(BOB, ALICE);

        // different name -> returns false
        Worker editedAlice = new WorkerBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertNotEquals(editedAlice, ALICE);

        // different phone -> returns false
        editedAlice = new WorkerBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertNotEquals(editedAlice, ALICE);

        // different pay -> returns false
        editedAlice = new WorkerBuilder(ALICE).withPay(VALID_PAY_BOB).build();
        assertNotEquals(editedAlice, ALICE);

        // different address -> returns false
        editedAlice = new WorkerBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertNotEquals(editedAlice, ALICE);

        // different roles -> returns false
        editedAlice = new WorkerBuilder(ALICE).withRoles(VALID_ROLE_CHEF).build();
        assertNotEquals(editedAlice, ALICE);
    }
}
