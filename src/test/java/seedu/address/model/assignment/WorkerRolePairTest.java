package seedu.address.model.assignment;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Role;
import seedu.address.model.worker.Worker;
import seedu.address.testutil.WorkerBuilder;
import seedu.address.testutil.WorkerRolePairBuilder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.ROLE_DESC_CHEF;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PAY_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_CASHIER;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_CHEF;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WORKER_INDEX_2;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_WORKER;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_WORKER;
import static seedu.address.testutil.TypicalWorkers.ALICE;
import static seedu.address.testutil.TypicalWorkers.BOB;

public class WorkerRolePairTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        // null input string
        assertThrows(NullPointerException.class, () -> new WorkerRolePair(null));

        // null index
        assertThrows(NullPointerException.class, () -> new WorkerRolePair(
                    null, Role.createRole(VALID_ROLE_CASHIER)));

        // null role
        assertThrows(NullPointerException.class, () -> new WorkerRolePair(INDEX_FIRST_WORKER, null));
    }

    @Test
    public void constructor_invalidWorkerRolePair_throwsIllegalArgumentException() {
        String invalidInputString = "";
        assertThrows(IllegalArgumentException.class, () -> new WorkerRolePair(invalidInputString));
    }


    @Test
    public void isValidWorkerRolePair() {
        // null address
        assertThrows(NullPointerException.class, () -> WorkerRolePair.isValidWorkerRolePair(null));

        // invalid workerRolePairs
        assertFalse(WorkerRolePair.isValidWorkerRolePair("")); // empty string
        assertFalse(WorkerRolePair.isValidWorkerRolePair("8 ")); // no role
        assertFalse(WorkerRolePair.isValidWorkerRolePair("cashier 9")); // wrong order
        assertFalse(WorkerRolePair.isValidWorkerRolePair("0 cashier")); // 0 index

        // valid workerRolePairs
        assertTrue(WorkerRolePair.isValidWorkerRolePair("1 cashier"));
        assertTrue(WorkerRolePair.isValidWorkerRolePair("1090 c")); // big index
    }


    @Test
    public void equals() {
        WorkerRolePair pair = new WorkerRolePairBuilder().build();
        WorkerRolePair samePair = new WorkerRolePairBuilder().build();
        WorkerRolePair differentWorker = new WorkerRolePairBuilder().withWorkerIndex(INDEX_SECOND_WORKER).build();
        WorkerRolePair differentRole = new WorkerRolePairBuilder().withRole(VALID_ROLE_CHEF).build();


        // same object -> returns true
        assertEquals(pair, pair);

        // same values -> returns true
        assertEquals(pair, samePair);

        // null -> returns false
        assertNotEquals(pair, null);

        // different type -> returns false
        assertNotEquals(pair, 5);

        // different worker -> returns false
        assertNotEquals(pair, differentWorker);

        // different role -> returns false
        assertNotEquals(pair, differentRole);
    }
}
