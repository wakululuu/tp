package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.shift.Shift;
import seedu.address.model.shift.WorkerRoleAssignment;
import seedu.address.model.worker.ShiftRoleAssignment;
import seedu.address.model.worker.Worker;


/**
 * Deletes a shift identified using its displayed index from the McScheduler.
 */
public class ShiftDeleteCommand extends Command {

    public static final String COMMAND_WORD = "shift-delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the shift identified by the index number used in the displayed shift list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_SHIFT_SUCCESS = "Deleted Shift: %1$s";

    private final Index targetIndex;

    public ShiftDeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Shift> lastShownList = model.getFilteredShiftList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
        }

        Shift shiftToDelete = lastShownList.get(targetIndex.getZeroBased());
        deleteShiftFromAssignedWorkers(model, shiftToDelete);
        model.deleteShift(shiftToDelete);

        return new CommandResult(String.format(MESSAGE_DELETE_SHIFT_SUCCESS, shiftToDelete));
    }

    private void deleteShiftFromAssignedWorkers(Model model, Shift shiftToDelete) {
        requireAllNonNull(model, shiftToDelete);
        List<Worker> fullWorkerList = model.getFullWorkerList();

        Stream<Worker> assignedWorkers = shiftToDelete.getWorkerRoleAssignments()
                .stream()
                .map(WorkerRoleAssignment::getWorker);

        assignedWorkers.forEach(assignedWorker -> {
            for (Worker worker : fullWorkerList) {
                if (assignedWorker.isSameWorker(worker)) {
                    deleteShiftFromWorker(model, worker, shiftToDelete);
                    break;
                }
            }
        });
    }

    private void deleteShiftFromWorker(Model model, Worker worker, Shift shiftToDelete) {
        Set<ShiftRoleAssignment> editedAssignments = createEditedShiftRoleAssignments(worker,
                shiftToDelete);

        Worker editedWorker = new Worker(worker.getName(), worker.getPhone(), worker.getPay(), worker.getAddress(),
                worker.getRoles(), editedAssignments);

        model.setWorker(worker, editedWorker);
    }

    private Set<ShiftRoleAssignment> createEditedShiftRoleAssignments(Worker worker, Shift shiftToDelete) {
        Set<ShiftRoleAssignment> editedAssignments = new HashSet<>(worker.getShiftRoleAssignments());
        editedAssignments.removeIf(assignment -> shiftToDelete.isSameShift(assignment.getShift()));
        return editedAssignments;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ShiftDeleteCommand // instanceof handles nulls
                && targetIndex.equals(((ShiftDeleteCommand) other).targetIndex)); // state check
    }
}
