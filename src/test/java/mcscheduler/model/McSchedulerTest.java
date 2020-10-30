package mcscheduler.model;

import static mcscheduler.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_ROLE_CASHIER;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_ROLE_REQUIREMENT_CHEF;
import static mcscheduler.testutil.Assert.assertThrows;
import static mcscheduler.testutil.McSchedulerBuilder.getTypicalMcScheduler;
import static mcscheduler.testutil.TypicalShifts.SHIFT_A;
import static mcscheduler.testutil.TypicalWorkers.ALICE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.role.Role;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.shift.exceptions.DuplicateShiftException;
import mcscheduler.model.worker.Worker;
import mcscheduler.model.worker.exceptions.DuplicateWorkerException;
import mcscheduler.testutil.McSchedulerBuilder;
import mcscheduler.testutil.ShiftBuilder;
import mcscheduler.testutil.WorkerBuilder;

public class McSchedulerTest {

    private final McScheduler mcScheduler = new McScheduler();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), mcScheduler.getWorkerList());
        assertEquals(Collections.emptyList(), mcScheduler.getShiftList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> mcScheduler.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyMcScheduler_replacesData() {
        McScheduler newData = getTypicalMcScheduler();
        mcScheduler.resetData(newData);
        assertEquals(newData, mcScheduler);
    }

    @Test
    public void resetData_withDuplicateWorkers_throwsDuplicateWorkerException() {
        // Two workers with the same identity fields
        Worker editedAlice = new WorkerBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withRoles(VALID_ROLE_CASHIER)
            .build();
        List<Worker> newWorkers = Arrays.asList(ALICE, editedAlice);
        McSchedulerStub newData = McSchedulerStub.createMcSchedulerStubWithWorkers(newWorkers);

        assertThrows(DuplicateWorkerException.class, () -> mcScheduler.resetData(newData));
    }

    @Test
    public void hasWorker_nullWorker_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> mcScheduler.hasWorker(null));
    }

    @Test
    public void hasWorker_workerNotInMcScheduler_returnsFalse() {
        assertFalse(mcScheduler.hasWorker(ALICE));
    }

    @Test
    public void hasWorker_workerInMcScheduler_returnsTrue() {
        mcScheduler.addWorker(ALICE);
        assertTrue(mcScheduler.hasWorker(ALICE));
    }

    @Test
    public void hasWorker_workerWithSameIdentityFieldsInMcScheduler_returnsTrue() {
        mcScheduler.addWorker(ALICE);
        Worker editedAlice = new WorkerBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withRoles(VALID_ROLE_CASHIER)
            .build();
        assertTrue(mcScheduler.hasWorker(editedAlice));
    }

    @Test
    public void getWorkerList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> mcScheduler.getWorkerList().remove(0));
    }

    @Test
    public void resetData_withDuplicateShifts_throwsDuplicateShiftException() {
        Shift editedShift = new ShiftBuilder(SHIFT_A).withRoleRequirements(VALID_ROLE_REQUIREMENT_CHEF)
            .build();
        List<Shift> newShifts = Arrays.asList(SHIFT_A, editedShift);
        McSchedulerStub newData = McSchedulerStub.createMcSchedulerStubWithShifts(newShifts);

        assertThrows(DuplicateShiftException.class, () -> mcScheduler.resetData(newData));
    }


    @Test
    public void hasShift_nullShift_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> mcScheduler.hasShift(null));
    }

    @Test
    public void hasShift_shiftNotInMcScheduler_returnsFalse() {
        assertFalse(mcScheduler.hasShift(SHIFT_A));
    }

    @Test
    public void hasShift_shiftInMcScheduler_returnsTrue() {
        mcScheduler.addShift(SHIFT_A);
        assertTrue(mcScheduler.hasShift(SHIFT_A));
    }

    @Test
    public void hasShift_workerWithSameIdentityFieldsInMcScheduler_returnsTrue() {
        mcScheduler.addShift(SHIFT_A);
        Shift editedShift = new ShiftBuilder(SHIFT_A).withRoleRequirements(VALID_ROLE_REQUIREMENT_CHEF)
            .build();
        assertTrue(mcScheduler.hasShift(editedShift));
    }

    @Test
    public void getShiftList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> mcScheduler.getShiftList().remove(0));
    }

    @Test
    public void equals() {

        mcScheduler.addWorker(ALICE);
        mcScheduler.addShift(SHIFT_A);
        McScheduler noWorkerMcScheduler = new McSchedulerBuilder().withShift(SHIFT_A).build();
        McScheduler noShiftMcScheduler = new McSchedulerBuilder().withWorker(ALICE).build();
        McScheduler emptyMcScheduler = new McScheduler();

        //same object returns true
        assertEquals(mcScheduler, mcScheduler);

        //different class object returns false
        assertNotEquals(mcScheduler, 123);

        //same content returns true
        assertEquals(new McScheduler(mcScheduler), mcScheduler);

        //same shifts different workers returns false
        assertNotEquals(noWorkerMcScheduler, mcScheduler);

        //same workers different shifts returns false
        assertNotEquals(noShiftMcScheduler, mcScheduler);

        //different workers different shifts returns false
        assertNotEquals(emptyMcScheduler, mcScheduler);

    }

    /**
     * A stub ReadOnlyMcScheduler whose workers list can violate interface constraints.
     */
    private static class McSchedulerStub implements ReadOnlyMcScheduler {
        private final ObservableList<Worker> workers = FXCollections.observableArrayList();
        private final ObservableList<Shift> shifts = FXCollections.observableArrayList();
        private final ObservableList<Assignment> assignments = FXCollections.observableArrayList();
        private final ObservableList<Role> validRoles = FXCollections.observableArrayList();

        private McSchedulerStub(Collection<Worker> workers, Collection<Shift> shifts,
                                Collection<Assignment> assignments, Collection<Role> validRoles) {
            this.workers.setAll(workers);
            this.shifts.setAll(shifts);
            this.assignments.setAll(assignments);
            this.validRoles.setAll(validRoles);
        }

        public static McSchedulerStub createMcSchedulerStubWithWorkers(Collection<Worker> workers) {
            return new McSchedulerStub(workers, Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList());
        }

        public static McSchedulerStub createMcSchedulerStubWithShifts(Collection<Shift> shifts) {
            return new McSchedulerStub(Collections.emptyList(), shifts, Collections.emptyList(),
                Collections.emptyList());
        }

        @Override
        public ObservableList<Worker> getWorkerList() {
            return workers;
        }

        @Override
        public ObservableList<Shift> getShiftList() {
            return shifts;
        }

        @Override
        public ObservableList<Assignment> getAssignmentList() {
            return assignments;
        }

        @Override
        public ObservableList<Role> getRoleList() {
            return validRoles;
        }
    }

}
