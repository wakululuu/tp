package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SHIFT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WORKER;

import java.util.List;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.shift.Shift;
import seedu.address.model.tag.Role;
import seedu.address.model.worker.Worker;

/**
 * Deletes a shift, worker and shift assignment from the McScheduler.
 */
public class UnassignCommand extends Command {
    public static final String COMMAND_WORD = "unassign";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes shift, worker(s) and role(s) assignment(s) "
            + "from the McScheduler by the index numbers used in the last worker and shift listings. "
            + "\nParameters: "
            + PREFIX_SHIFT + "SHIFT_INDEX (must be a positive integer) "
            + "{" + PREFIX_WORKER + "WORKER_INDEX (must be a positive integer)}...\n"
            + "Example: " + COMMAND_WORD
            + " s/1 "
            + "w/4 "
            + "w/2";

    public static final String MESSAGE_UNASSIGN_SUCCESS = "%1$d shift assignment(s) removed:\n%2$s";
    public static final String MESSAGE_ASSIGNMENT_NOT_FOUND =
                "This assignment does not exist in the McScheduler: [%1$s]";

    private final Index shiftIndex;
    private final Set<Index> workerIndexes;

    /**
     * Creates an UnassignCommand to delete the assignment of the specified {@code Shift}, {@code Worker} and
     * {@code Role}.
     *
     * @param shiftIndex of the shift in the filtered shift list.
     * @param workerIndexes of the worker(s) in the filtered worker list.
     */
    public UnassignCommand(Index shiftIndex, Set<Index> workerIndexes) {
        requireAllNonNull(shiftIndex, workerIndexes);
        requireAllNonNull(workerIndexes);

        this.shiftIndex = shiftIndex;
        this.workerIndexes = workerIndexes;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Worker> lastShownWorkerList = model.getFilteredWorkerList();
        List<Shift> lastShownShiftList = model.getFilteredShiftList();

        if (shiftIndex.getZeroBased() >= lastShownShiftList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
        }
        StringBuilder unassignStringBuilder = new StringBuilder();

        // Check if any is not found
        for (Index workerIndex : workerIndexes) {
            if (workerIndex.getZeroBased() >= lastShownWorkerList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_WORKER_DISPLAYED_INDEX);
            }
            Worker workerToUnassign = lastShownWorkerList.get(workerIndex.getZeroBased());
            Shift shiftToUnassign = lastShownShiftList.get(shiftIndex.getZeroBased());
            Assignment assignmentToDelete = new Assignment(shiftToUnassign, workerToUnassign);

            if (!model.hasAssignment(assignmentToDelete)) {
                throw new CommandException(String.format(MESSAGE_ASSIGNMENT_NOT_FOUND, assignmentToDelete));
            }
        }

        // Remove assignments
        for (Index workerIndex : workerIndexes) {
            Worker workerToUnassign = lastShownWorkerList.get(workerIndex.getZeroBased());
            Shift shiftToUnassign = lastShownShiftList.get(shiftIndex.getZeroBased());
            Role roleToUnassign = getRoleToUnassign(model, shiftToUnassign, workerToUnassign);
            Assignment assignmentToDelete = new Assignment(shiftToUnassign, workerToUnassign, roleToUnassign);
            model.deleteAssignment(assignmentToDelete);
            Shift.updateRoleRequirements(model, shiftToUnassign, roleToUnassign);

            unassignStringBuilder.append(assignmentToDelete);
            unassignStringBuilder.append("\n");
        }

        return new CommandResult(
                String.format(MESSAGE_UNASSIGN_SUCCESS, workerIndexes.size(), unassignStringBuilder.toString()));
    }

    private static Role getRoleToUnassign(Model model, Shift shiftToUnassign, Worker workerToUnassign) {
        List<Assignment> assignmentList = model.getFullAssignmentList();
        for (Assignment assignment : assignmentList) {
            if (assignment.getShift().isSameShift(shiftToUnassign)
                    && assignment.getWorker().isSameWorker(workerToUnassign)) {
                return assignment.getRole();
            }
        }
        assert false; // a role should have been returned within the for loop
        return null;
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
                && workerIndexes.equals(e.workerIndexes);
    }
}
