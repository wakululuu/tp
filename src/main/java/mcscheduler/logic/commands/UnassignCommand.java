package mcscheduler.logic.commands;

import static java.util.Objects.requireNonNull;
import static mcscheduler.commons.core.Messages.MESSAGE_DO_NOT_UNASSIGN_LEAVE;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_SHIFT;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_WORKER;

import java.util.List;
import java.util.Set;

import mcscheduler.commons.core.index.Index;
import mcscheduler.commons.util.CollectionUtil;
import mcscheduler.logic.commands.exceptions.CommandException;
import mcscheduler.model.Model;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.assignment.exceptions.AssignmentNotFoundException;
import mcscheduler.model.role.Leave;
import mcscheduler.model.role.Role;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.worker.Worker;

/**
 * Deletes a shift, worker and shift assignment from the McScheduler.
 */
public class UnassignCommand extends Command {
    public static final String COMMAND_WORD = "unassign";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes shift, worker(s) and role(s) assignment(s) "
            + "from the McScheduler by the index numbers used in the last worker and shift listings. "
            + "\nParameters: "
            + PREFIX_SHIFT + "SHIFT_INDEX (must be a positive integer) "
            + PREFIX_WORKER + "WORKER_INDEX (must be a positive integer)...\n"
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
        CollectionUtil.requireAllNonNull(shiftIndex, workerIndexes);
        CollectionUtil.requireAllNonNull(workerIndexes);

        this.shiftIndex = shiftIndex;
        this.workerIndexes = workerIndexes;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Worker> lastShownWorkerList = model.getFilteredWorkerList();
        List<Shift> lastShownShiftList = model.getFilteredShiftList();

        if (shiftIndex.getZeroBased() >= lastShownShiftList.size()) {
            throw new CommandException(
                    CommandUtil.printOutOfBoundsShiftIndexError(shiftIndex, MESSAGE_USAGE));
        }

        // Check if any is not found
        for (Index workerIndex : workerIndexes) {
            if (workerIndex.getZeroBased() >= lastShownWorkerList.size()) {
                throw new CommandException(
                        CommandUtil.printOutOfBoundsWorkerIndexError(workerIndex, MESSAGE_USAGE));
            }
            Worker workerToUnassign = lastShownWorkerList.get(workerIndex.getZeroBased());
            Shift shiftToUnassign = lastShownShiftList.get(shiftIndex.getZeroBased());
            Assignment assignmentToDelete = new Assignment(shiftToUnassign, workerToUnassign);

            if (!model.hasAssignment(assignmentToDelete)) {
                throw new CommandException(String.format(MESSAGE_ASSIGNMENT_NOT_FOUND, assignmentToDelete));
            } else if (Leave.isLeave(model.getAssignment(assignmentToDelete).get().getRole())) {
                throw new CommandException(MESSAGE_DO_NOT_UNASSIGN_LEAVE);
            }
        }

        StringBuilder unassignStringBuilder = new StringBuilder();
        // Remove assignments
        for (Index workerIndex : workerIndexes) {
            lastShownWorkerList = model.getFilteredWorkerList();
            lastShownShiftList = model.getFilteredShiftList();

            Worker workerToUnassign = lastShownWorkerList.get(workerIndex.getZeroBased());
            Shift shiftToUnassign = lastShownShiftList.get(shiftIndex.getZeroBased());
            Role roleToUnassign = getRoleToUnassign(model, shiftToUnassign, workerToUnassign);

            Assignment assignmentToDelete = new Assignment(shiftToUnassign, workerToUnassign, roleToUnassign);
            unassignStringBuilder
                    .append(assignmentToDelete)
                    .append("\n");

            try {
                model.deleteAssignment(assignmentToDelete);
            } catch (AssignmentNotFoundException e) {
                throw new CommandException(MESSAGE_ASSIGNMENT_NOT_FOUND);
            }
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
        assert false : "Role returned is null"; // a non-null role should have been returned within the for loop
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
