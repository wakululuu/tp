package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SHIFT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WORKER;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_SHIFTS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_WORKERS;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.assignment.Assignment;
//import seedu.address.model.assignment.exceptions.AssignmentNotFoundException;
import seedu.address.model.shift.Shift;
import seedu.address.model.worker.Worker;

/**
 * Deletes a shift, worker and shift assignment from the McScheduler.
 */
public class UnassignCommand extends Command {
    public static final String COMMAND_WORD = "unassign";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes a shift, worker and role assignment from the "
            + "McScheduler by the index numbers used in the last worker and shift listings. "
            + "\nParameters: "
            + PREFIX_SHIFT + "SHIFT_INDEX (must be a positive integer) "
            + PREFIX_WORKER + "WORKER_INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD
            + " s/1 "
            + "w/4";

    public static final String MESSAGE_UNASSIGN_SUCCESS = "Shift assignment removed:\n%1$s";

    private final Index shiftIndex;
    private final Index workerIndex;

    /**
     * Creates an UnassignCommand to delete the assignment of the specified {@code Shift}, {@code Worker} and
     * {@code Role}.
     *
     * @param shiftIndex of the shift in the filtered shift list.
     * @param workerIndex of the worker in the filtered worker list.
     */
    public UnassignCommand(Index shiftIndex, Index workerIndex) {
        requireAllNonNull(shiftIndex, workerIndex);

        this.shiftIndex = shiftIndex;
        this.workerIndex = workerIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
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
        Assignment assignmentToDelete = new Assignment(shiftToUnassign, workerToUnassign);
        model.deleteAssignment(assignmentToDelete);

        model.updateFilteredShiftList(PREDICATE_SHOW_ALL_SHIFTS);
        model.updateFilteredWorkerList(PREDICATE_SHOW_ALL_WORKERS);

        return new CommandResult(String.format(MESSAGE_UNASSIGN_SUCCESS, assignmentToDelete));
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

