package mcscheduler.model.assignment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import mcscheduler.logic.commands.CommandTestUtil;
import mcscheduler.model.role.Role;
import mcscheduler.testutil.Assert;
import mcscheduler.testutil.TypicalIndexes;
import mcscheduler.testutil.WorkerRolePairBuilder;


public class WorkerRolePairTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        // null input string
        Assert.assertThrows(NullPointerException.class, () -> new WorkerRolePair(null));

        // null index
        Assert.assertThrows(NullPointerException.class, () -> new WorkerRolePair(
            null, Role.createRole(CommandTestUtil.VALID_ROLE_CASHIER)));

        // null role
        Assert.assertThrows(NullPointerException.class, () ->
            new WorkerRolePair(TypicalIndexes.INDEX_FIRST_WORKER, null));
    }

    @Test
    public void constructor_invalidWorkerRolePair_throwsIllegalArgumentException() {
        String invalidInputString = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new WorkerRolePair(invalidInputString));
    }


    @Test
    public void isValidWorkerRolePair() {
        // null address
        Assert.assertThrows(NullPointerException.class, () -> WorkerRolePair.isValidWorkerRolePair(null));

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
        WorkerRolePair differentWorker =
            new WorkerRolePairBuilder().withWorkerIndex(TypicalIndexes.INDEX_SECOND_WORKER).build();
        WorkerRolePair differentRole = new WorkerRolePairBuilder().withRole(CommandTestUtil.VALID_ROLE_CHEF).build();


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
