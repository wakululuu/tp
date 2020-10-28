package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SHIFT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WORKER_ROLE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_SHIFTS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_WORKERS;

import java.util.List;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.assignment.WorkerRolePair;
import seedu.address.model.shift.Shift;
import seedu.address.model.worker.Unavailability;
import seedu.address.model.worker.Worker;

/**
 * Adds a shift, worker and shift assignment to the McScheduler.
 */
public class AssignCommand extends Command {
    public static final String COMMAND_WORD = "assign";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds shift, worker(s) and role(s) assignment(s) "
            + "to the McScheduler by the index numbers used in the last worker and shift listings. "
            + "\nParameters: "
            + PREFIX_SHIFT + "SHIFT_INDEX (must be a positive integer) "
            + "{" + PREFIX_WORKER_ROLE + "WORKER_INDEX (must be a positive integer) ROLE}...\n"
            + "Example: " + COMMAND_WORD
            + " s/4 "
            + "w/1 Cashier "
            + "w/3 Janitor";

    public static final String MESSAGE_ASSIGN_SUCCESS = "%d new assignment(s) added:\n%1$s";
    public static final String MESSAGE_DUPLICATE_ASSIGNMENT = "This assignment already exists in the McScheduler: %1$s";

    private final Index shiftIndex;
    private final Set<WorkerRolePair> workerRolePairs;

    /**
     * Creates an AssignCommand to add an assignment of the specified {@code Shift}, {@code Worker} and {@code Role}.
     *
     * @param shiftIndex  of the shift in the filtered shift list.
     * @param workerRolePairs a set of worker-roles to be assined to the shift
     */
    public AssignCommand(Index shiftIndex, Set<WorkerRolePair> workerRolePairs) {
        requireAllNonNull(shiftIndex, workerRolePairs);

        this.shiftIndex = shiftIndex;
        this.workerRolePairs = workerRolePairs;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Worker> lastShownWorkerList = model.getFilteredWorkerList();
        List<Shift> lastShownShiftList = model.getFilteredShiftList();

        if (shiftIndex.getZeroBased() >= lastShownShiftList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
        }
        for (WorkerRolePair workerRolePair : workerRolePairs) {
            if (workerRolePair.getWorkerIndex().getZeroBased() >= lastShownWorkerList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_WORKER_DISPLAYED_INDEX);
            }
        }

        StringBuilder assignStringBuilder = new StringBuilder();

        for (WorkerRolePair workerRolePair : workerRolePairs) {
            Worker workerToAssign = lastShownWorkerList.get(workerRolePair.getWorkerIndex().getZeroBased());
            Shift shiftToAssign = lastShownShiftList.get(shiftIndex.getZeroBased());
            Assignment assignmentToAdd = new Assignment(shiftToAssign, workerToAssign, workerRolePair.getRole());

            if (model.hasAssignment(assignmentToAdd)) {
                throw new CommandException(String.format(MESSAGE_DUPLICATE_ASSIGNMENT, assignmentToAdd));
            }

            if (isWorkerUnavailable(workerToAssign, shiftToAssign)) {
                throw new CommandException(String.format(Messages.MESSAGE_INVALID_ASSIGNMENT, assignmentToAdd));
            }

            assignStringBuilder.append(assignmentToAdd);
            model.addAssignment(assignmentToAdd);
        }
        model.updateFilteredShiftList(PREDICATE_SHOW_ALL_SHIFTS);
        model.updateFilteredWorkerList(PREDICATE_SHOW_ALL_WORKERS);

        return new CommandResult(
                String.format(MESSAGE_ASSIGN_SUCCESS, workerRolePairs.size(), assignStringBuilder.toString()));
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
        if (!(other instanceof AssignCommand)) {
            return false;
        }

        // state check
        AssignCommand e = (AssignCommand) other;
        return shiftIndex.equals(e.shiftIndex)
                && workerRolePairs.equals(e.workerRolePairs);
    }
}
