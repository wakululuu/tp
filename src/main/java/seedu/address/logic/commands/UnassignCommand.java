package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SHIFT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WORKER;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.shift.Shift;
import seedu.address.model.shift.WorkerRoleAssignment;
import seedu.address.model.worker.ShiftRoleAssignment;
import seedu.address.model.worker.Worker;

/**
 * Removes a worker from a shift.
 */
public class UnassignCommand extends Command {
    public static final String COMMAND_WORD = "unassign";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Unassigns the specified worker from the specified "
            + "shift by the index numbers used in the last worker and shift listings. "
            + "\nParameters: "
            + PREFIX_SHIFT + "SHIFT_INDEX (must be a positive integer) "
            + PREFIX_WORKER + "WORKER_INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD
            + " s/1 "
            + "w/4";

    public static final String MESSAGE_UNASSIGN_SUCCESS = "Shift assignment removed:\n"
            + "Shift: %1$s, Worker: %2$s";

    private final Index shiftIndex;
    private final Index workerIndex;

    /**
     * Creates an UnassignCommand to unassign the specified {@code Worker} from the specified {@code Shift}.
     *
     * @param shiftIndex of the shift in the filtered shift list to unassign the worker from.
     * @param workerIndex of the worker in the filtered worker list to unassign from the shift.
     */
    public UnassignCommand(Index shiftIndex, Index workerIndex) {
        requireAllNonNull(shiftIndex, workerIndex);

        this.shiftIndex = shiftIndex;
        this.workerIndex = workerIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Worker> lastShownWorkerList = model.getFilteredWorkerList();
        List<Shift> lastShownShiftList = model.getFilteredShiftList();

        if (shiftIndex.getZeroBased() >= lastShownShiftList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
        }
        if (workerIndex.getZeroBased() >= lastShownWorkerList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_WORKER_DISPLAYED_INDEX);
        }

        Worker workerToUnassign = lastShownWorkerList.get(workerIndex.getZeroBased());
        Shift shiftToUnassign = lastShownShiftList.get(shiftIndex.getZeroBased());

        Worker unassignedWorker = createUnassignedWorker(workerToUnassign, shiftToUnassign);
        Shift unassignedShift = createUnassignedShift(shiftToUnassign, workerToUnassign);

        model.setWorker(workerToUnassign, unassignedWorker);
        model.setShift(shiftToUnassign, unassignedShift);

        return new CommandResult(String.format(MESSAGE_UNASSIGN_SUCCESS, unassignedShift.toCondensedString(),
                unassignedWorker.getName()));
    }

    /**
     * Creates and returns a {@code Worker} with the {@code shiftToUnassign} unassigned.
     */
    private static Worker createUnassignedWorker(Worker workerToUnassign, Shift shiftToUnassign) {
        assert workerToUnassign != null;

        Set<ShiftRoleAssignment> updatedShiftRoleAssignments = new HashSet<>(
                workerToUnassign.getShiftRoleAssignments());
        for (ShiftRoleAssignment assignment : updatedShiftRoleAssignments) {
            Shift shift = assignment.getShift();
            if (shift.isSameShift(shiftToUnassign)) {
                updatedShiftRoleAssignments.remove(assignment);
            }
        }

        return new Worker(workerToUnassign.getName(), workerToUnassign.getPhone(), workerToUnassign.getPay(),
                workerToUnassign.getAddress(), workerToUnassign.getRoles(), updatedShiftRoleAssignments);
    }

    /**
     * Creates and returns a {@code Shift} with the {@code workerToUnassign} unassigned.
     */
    private static Shift createUnassignedShift(Shift shiftToUnassign, Worker workerToUnassign) {
        assert shiftToUnassign != null;

        Set<WorkerRoleAssignment> updatedWorkerRoleAssignments = new HashSet<>(
                shiftToUnassign.getWorkerRoleAssignments());
        for (WorkerRoleAssignment assignment : updatedWorkerRoleAssignments) {
            Worker worker = assignment.getWorker();
            if (worker.isSameWorker(workerToUnassign)) {
                updatedWorkerRoleAssignments.remove(assignment);
            }
        }

        return new Shift(shiftToUnassign.getShiftDay(), shiftToUnassign.getShiftTime(),
                shiftToUnassign.getRoleRequirements(), updatedWorkerRoleAssignments);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UnassignCommand)) {
            return false;
        }

        // state check
        UnassignCommand e = (UnassignCommand) other;
        return shiftIndex.equals(e.shiftIndex)
                && workerIndex.equals(e.workerIndex);
    }
}

