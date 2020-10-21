package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static seedu.address.testutil.AddressBookBuilder.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SHIFT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_WORKER;
import static seedu.address.testutil.TypicalShifts.SHIFT_A;
import static seedu.address.testutil.TypicalWorkers.ALICE;

import java.util.stream.Collectors;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.shift.Shift;
import seedu.address.model.worker.Worker;

public class AssignCommandTest {

    @Disabled
    @Test
    public void execute_typicalShiftAndPerson_correctBidirectionalAssociation() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Shift shift = model.getFilteredShiftList().get(INDEX_FIRST_SHIFT.getZeroBased());
        Worker worker = model.getFilteredWorkerList().get(INDEX_FIRST_WORKER.getZeroBased());

        // To confirm that typical shift and workers used do not contain any assignments
        assertEquals(shift, SHIFT_A);
        assertEquals(worker, ALICE);
        assertTrue(shift.getWorkerRoleAssignments().size() == 0);
        assertTrue(worker.getShiftRoleAssignments().size() == 0);

        try {
            new AssignCommand(INDEX_FIRST_SHIFT,
                    INDEX_FIRST_WORKER,
                    worker.getRoles().iterator().next()).execute(model);
        } catch (Exception e) {
            fail(e); // should not happen
        }

        Shift associatedShiftInModel = model.getFilteredShiftList().get(INDEX_FIRST_SHIFT.getZeroBased());
        Worker associatedWorkerInModel = model.getFilteredWorkerList().get(INDEX_FIRST_SHIFT.getZeroBased());

        // To verify that there is now one and only one association in the worker and shift
        assertTrue(associatedShiftInModel.getWorkerRoleAssignments().size() == 1);
        assertTrue(associatedWorkerInModel.getShiftRoleAssignments().size() == 1);

        Shift associatedShiftFromWorkerAssignment = associatedWorkerInModel
                .getShiftRoleAssignments()
                .stream()
                .collect(Collectors.toList())
                .get(0)
                .getShift();

        Worker associatedWorkerFromShiftAssignment = associatedShiftInModel
                .getWorkerRoleAssignments()
                .stream()
                .collect(Collectors.toList())
                .get(0)
                .getWorker();

        // These two checks show that the worker and shift in the model is different from the worker or shift
        // that is present within the association classes.
        assertEquals(associatedShiftFromWorkerAssignment, associatedShiftInModel);
        assertEquals(associatedWorkerFromShiftAssignment, associatedWorkerInModel);


    }

}
