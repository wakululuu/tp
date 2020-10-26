package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SHIFT_NEW;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SHIFT_OLD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WORKER;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_SHIFTS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_WORKERS;

import java.util.List;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.assignment.exceptions.AssignmentNotFoundException;
import seedu.address.model.shift.Shift;
import seedu.address.model.tag.Role;
import seedu.address.model.worker.Unavailability;
import seedu.address.model.worker.Worker;

public class ReassignCommand extends Command {
    public static final String COMMAND_WORD = "reassign";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Reassigns a worker to a new shift in the "
            + "McScheduler by the index numbers used in the last worker and shift listings. "
            + "\nParameters: "
            + PREFIX_WORKER + "WORKER_INDEX (must be a positive integer) "
            + PREFIX_SHIFT_OLD + "OLD_SHIFT_INDEX (must be a positive integer) "
            + PREFIX_SHIFT_NEW + "NEW_SHIFT_INDEX (must be a positive integer) "
            + PREFIX_ROLE + "ROLE\n"
            + "Example: " + COMMAND_WORD
            + " w/1 "
            + "so/1 "
            + "sn/2 "
            + "r/Cashier";

    public static final String MESSAGE_REASSIGN_SUCCESS = "Reassignment made:\n%1$s";
    public static final String MESSAGE_DUPLICATE_ASSIGNMENT = "This assignment already exists in the McScheduler";

    private final Index oldShiftIndex;
    private final Index newShiftIndex;
    private final Index workerIndex;
    private final Role newRole;

    /**
     * Creates a ReassignCommand to edit an assignment of the specified {@code Shift}, {@code Worker} and {@code Role}.
     *
     * @param workerIndex of the worker in the filtered worker list.
     * @param oldShiftIndex  of the old shift in the filtered shift list.
     * @param newShiftIndex  of the new shift to be assigned in the filtered shift list.
     * @param newRole        of the worker in the new shift.
     */
    public ReassignCommand(Index workerIndex, Index oldShiftIndex,
                           Index newShiftIndex, Role newRole) {
        requireAllNonNull(workerIndex, oldShiftIndex, newShiftIndex, newRole);
        this.workerIndex = workerIndex;
        this.oldShiftIndex = oldShiftIndex;
        this.newShiftIndex = newShiftIndex;
        this.newRole = newRole;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        try {
            List<Worker> lastShownWorkerList = model.getFilteredWorkerList();
            List<Shift> lastShownShiftList = model.getFilteredShiftList();
            List<Assignment> assignmentList = model.getFullAssignmentList();

            if (oldShiftIndex.getZeroBased() >= lastShownShiftList.size()
                    || newShiftIndex.getZeroBased() >= lastShownShiftList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
            }
            if (workerIndex.getZeroBased() >= lastShownWorkerList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_WORKER_DISPLAYED_INDEX);
            }

            Worker workerToReassign = lastShownWorkerList.get(workerIndex.getZeroBased());
            Shift shiftToRemove = lastShownShiftList.get(oldShiftIndex.getZeroBased());
            Assignment assignmentToRemove = new Assignment(shiftToRemove, workerToReassign);

            for (Assignment a : assignmentList) {
                if (a.equals(assignmentToRemove)) {
                    assignmentToRemove = new Assignment(shiftToRemove, workerToReassign, a.getRole());
                }
            }

            Shift shiftToReassign = lastShownShiftList.get(newShiftIndex.getZeroBased());
            Assignment reassignmentToAdd = new Assignment(shiftToReassign, workerToReassign, newRole);

            if (model.hasAssignment(reassignmentToAdd) &&
                    reassignmentToAdd.getRole().equals(assignmentToRemove.getRole())) {
                throw new CommandException(MESSAGE_DUPLICATE_ASSIGNMENT);
            }

            if (isWorkerUnavailable(workerToReassign, shiftToReassign)) {
                throw new CommandException(Messages.MESSAGE_INVALID_ASSIGNMENT);
            }

            model.deleteAssignment(assignmentToRemove);
            model.addAssignment(reassignmentToAdd);
            model.updateFilteredShiftList(PREDICATE_SHOW_ALL_SHIFTS);
            model.updateFilteredWorkerList(PREDICATE_SHOW_ALL_WORKERS);

            return new CommandResult(String.format(MESSAGE_REASSIGN_SUCCESS, reassignmentToAdd));
        } catch (AssignmentNotFoundException e) {
            throw new CommandException("The old assignment does not exist");
        }
    }

    private static boolean isWorkerUnavailable(Worker workerToAssign, Shift shiftToAssign) {
        Set<Unavailability> workerUnavailableTimings = workerToAssign.getUnavailableTimings();
        for (Unavailability unavailability : workerUnavailableTimings) {
            boolean hasSameDay = unavailability.getDay().equals(shiftToAssign.getShiftDay());
            boolean hasSameTime = unavailability.getTime().equals(shiftToAssign.getShiftTime());
            if (hasSameDay && hasSameTime) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ReassignCommand)) {
            return false;
        }

        // state check
        ReassignCommand e = (ReassignCommand) other;
        return oldShiftIndex.equals(e.oldShiftIndex)
                && workerIndex.equals(e.workerIndex)
                && newShiftIndex.equals(e.newShiftIndex)
                && newRole.equals(e.newRole);
    }
}
