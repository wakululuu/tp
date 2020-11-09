package mcscheduler.logic.commands;

import static java.util.Objects.requireNonNull;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_ROLE;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_SHIFT;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_SHIFT_NEW;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_SHIFT_OLD;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_WORKER;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_WORKER_NEW;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_WORKER_OLD;

import java.util.List;

import mcscheduler.commons.core.Messages;
import mcscheduler.commons.core.index.Index;
import mcscheduler.commons.util.CollectionUtil;
import mcscheduler.logic.commands.exceptions.CommandException;
import mcscheduler.model.Model;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.role.Leave;
import mcscheduler.model.role.Role;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.worker.Worker;

public class ReassignCommand extends Command {
    public static final String COMMAND_WORD = "reassign";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits an assignment in the "
            + "McScheduler by the index numbers used in the last worker and shift listings. "
            + "\nParameters: \n"
            + "1. " + PREFIX_WORKER_OLD + "OLD_WORKER_INDEX (must be a positive integer) "
            + PREFIX_WORKER_NEW + "NEW_WORKER_INDEX (must be a positive integer "
            + PREFIX_SHIFT_OLD + "OLD_SHIFT_INDEX (must be a positive integer) "
            + PREFIX_SHIFT_NEW + "NEW_SHIFT_INDEX (must be a positive integer) "
            + PREFIX_ROLE + "ROLE\n"
            + "2. " + PREFIX_WORKER + "WORKER_INDEX (must be a positive integer) "
            + PREFIX_SHIFT + "SHIFT_INDEX (must be a positive integer) "
            + PREFIX_ROLE + "ROLE\n"
            + "Examples:\n" + COMMAND_WORD
            + " wo/1 "
            + "wn/2 "
            + "so/1 "
            + "sn/2 "
            + "r/Cashier\n"
            + COMMAND_WORD + " w/1 s/1 r/Chef";

    public static final String MESSAGE_REASSIGN_SUCCESS = "Reassignment made: %1$s | Previous Role: %2$s ";
    public static final String MESSAGE_DUPLICATE_ASSIGNMENT_WITH_SAME_ROLE =
            "This assignment already exists in the McScheduler";
    public static final String MESSAGE_DUPLICATE_ASSIGNMENT_WITH_DIFFERENT_ROLE =
            "%1$s is already assigned to a role in the shift.";
    public static final String MESSAGE_INVALID_REASSIGNMENT_OF_LEAVE =
            "%1$s is on leave on Shift (%2$s). This leave assignment cannot be reassigned.";
    public static final String MESSAGE_ASSIGNMENT_NOT_FOUND = "The assignment to be edited does not exist";

    private final Index oldShiftIndex;
    private final Index newShiftIndex;
    private final Index oldWorkerIndex;
    private final Index newWorkerIndex;
    private final Role newRole;

    /**
     * Creates a ReassignCommand to edit an assignment of the specified {@code Shift}, {@code Worker} and {@code Role}.
     *
     * @param oldWorkerIndex of the worker in the filtered worker list.
     * @param newWorkerIndex of the worker in the filtered worker list.
     * @param oldShiftIndex  of the old shift in the filtered shift list.
     * @param newShiftIndex  of the new shift to be assigned in the filtered shift list.
     * @param newRole        of the worker in the new shift.
     */
    public ReassignCommand(Index oldWorkerIndex, Index newWorkerIndex, Index oldShiftIndex,
                           Index newShiftIndex, Role newRole) {
        CollectionUtil.requireAllNonNull(oldWorkerIndex, newWorkerIndex, oldShiftIndex, newShiftIndex, newRole);
        this.oldWorkerIndex = oldWorkerIndex;
        this.newWorkerIndex = newWorkerIndex;
        this.oldShiftIndex = oldShiftIndex;
        this.newShiftIndex = newShiftIndex;
        this.newRole = newRole;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Worker> lastShownWorkerList = model.getFilteredWorkerList();
        List<Shift> lastShownShiftList = model.getFilteredShiftList();

        if (oldShiftIndex.getZeroBased() >= lastShownShiftList.size()) {
            throw new CommandException(
                    CommandUtil.printOutOfBoundsShiftIndexError(oldShiftIndex, MESSAGE_USAGE));
        }
        if (newShiftIndex.getZeroBased() >= lastShownShiftList.size()) {
            throw new CommandException(
                    CommandUtil.printOutOfBoundsShiftIndexError(newShiftIndex, MESSAGE_USAGE));
        }
        if (oldWorkerIndex.getZeroBased() >= lastShownWorkerList.size()) {
            throw new CommandException(
                    CommandUtil.printOutOfBoundsWorkerIndexError(oldWorkerIndex, MESSAGE_USAGE));
        }
        if (newWorkerIndex.getZeroBased() >= lastShownWorkerList.size()) {
            throw new CommandException(
                    CommandUtil.printOutOfBoundsWorkerIndexError(newWorkerIndex, MESSAGE_USAGE));
        }
        if (!model.hasRole(newRole)) {
            throw new CommandException(String.format(Messages.MESSAGE_ROLE_NOT_FOUND, newRole));
        }

        Worker oldWorker = lastShownWorkerList.get(oldWorkerIndex.getZeroBased());
        Shift oldShift = lastShownShiftList.get(oldShiftIndex.getZeroBased());
        Assignment assignmentToRemove = new Assignment(oldShift, oldWorker);

        if (!model.hasAssignment(assignmentToRemove)) {
            throw new CommandException(MESSAGE_ASSIGNMENT_NOT_FOUND);
        }

        assignmentToRemove = model.getAssignment(assignmentToRemove).get();

        assert assignmentToRemove.getRole() != null; // dummy assignment has been replaced with actual assignment

        boolean isLeaveAssignment = Leave.isLeave(assignmentToRemove.getRole());

        // Disallow reassigning of leave
        if (isLeaveAssignment) {
            throw new CommandException(String.format(MESSAGE_INVALID_REASSIGNMENT_OF_LEAVE, oldWorker.getName(),
                    oldShift.toCondensedString()));
        }

        Worker newWorker = lastShownWorkerList.get(newWorkerIndex.getZeroBased());
        Shift newShift = lastShownShiftList.get(newShiftIndex.getZeroBased());
        Assignment assignmentToAdd = new Assignment(newShift, newWorker, newRole);

        boolean hasSameWorker = oldWorker.equals(newWorker);
        boolean hasSameShift = oldShift.equals(newShift);
        boolean isExistingAssignment = model.hasAssignment(assignmentToAdd);
        boolean hasSameRole = assignmentToAdd.getRole().equals(assignmentToRemove.getRole());


        // For cases where reassign is done on same worker in same shift, role needs to be checked
        // Duplicate assignment if role is the same
        if (hasSameWorker && hasSameShift && isExistingAssignment && hasSameRole) {
            throw new CommandException(MESSAGE_DUPLICATE_ASSIGNMENT_WITH_SAME_ROLE);
        }

        // For cases where reassign is called on different shifts and workers
        // Only check whether the new worker is already assigned to a role in the new shift
        if ((!hasSameWorker || !hasSameShift) && isExistingAssignment) {
            throw new CommandException(String.format(
                    MESSAGE_DUPLICATE_ASSIGNMENT_WITH_DIFFERENT_ROLE, newWorker.getName()));
        }

        if (!newWorker.isFitForRole(newRole)) {
            throw new CommandException(String.format(Messages.MESSAGE_INVALID_ASSIGNMENT_WORKER_ROLE,
                    newWorker.getName(), newRole));
        }
        if (newWorker.isUnavailable(newShift)) {
            throw new CommandException(String.format(Messages.MESSAGE_INVALID_ASSIGNMENT_UNAVAILABLE,
                    newWorker.getName(), newShift.getShiftDay(), newShift.getShiftTime()));
        }

        if (!newShift.isRoleRequired(newRole)) {
            throw new CommandException(
                String.format(Messages.MESSAGE_INVALID_ASSIGNMENT_NOT_REQUIRED, newRole, newShift));
        }

        model.setAssignment(assignmentToRemove, assignmentToAdd);

        return new CommandResult(
                String.format(MESSAGE_REASSIGN_SUCCESS, assignmentToAdd, assignmentToRemove.getRole()));
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
                && oldWorkerIndex.equals(e.oldWorkerIndex)
                && newWorkerIndex.equals(e.newWorkerIndex)
                && newShiftIndex.equals(e.newShiftIndex)
                && newRole.equals(e.newRole);
    }
}
