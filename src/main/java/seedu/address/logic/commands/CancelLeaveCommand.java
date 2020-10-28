package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SHIFT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WORKER;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.shift.Shift;
import seedu.address.model.tag.Leave;
import seedu.address.model.worker.Worker;

/**
 * Cancel a worker's leave for a particular shift.
 */
public class CancelLeaveCommand extends Command {

    public static final String COMMAND_WORD = "cancel-leave";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Cancels the specified worker's leave from the "
            + "specified shift by the index numbers used in the last worker and shift listings. "
            + "\nParameters: "
            + PREFIX_SHIFT + "SHIFT_INDEX (must be a positive integer) "
            + PREFIX_WORKER + "WORKER_INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD
            + " s/1 "
            + "w/4";

    public static final String MESSAGE_CANCEL_LEAVE_SUCCESS_PREFIX = "[Leave Cancelled] ";
    public static final String MESSAGE_CANCEL_LEAVE_SUCCESS = MESSAGE_CANCEL_LEAVE_SUCCESS_PREFIX
            + UnassignCommand.MESSAGE_UNASSIGN_SUCCESS;

    private final Index shiftIndex;
    private final Index workerIndex;

    /**
     * Creates a CancelLeaveCommand to cancel the leave of the specified {@code Worker}
     * from the specified {@code Shift}.
     *
     * @param shiftIndex of the shift in the filtered shift list to cancel the worker's leave from.
     * @param workerIndex of the worker in the filtered worker list whose leave is to be cancelled.
     */
    public CancelLeaveCommand(Index shiftIndex, Index workerIndex) {
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

        Worker workerToCancelLeave = lastShownWorkerList.get(workerIndex.getZeroBased());
        Shift shiftToCancelLeaveFrom = lastShownShiftList.get(shiftIndex.getZeroBased());
        Assignment assignmentToCancelLeave = new Assignment(shiftToCancelLeaveFrom, workerToCancelLeave);

        Optional<Assignment> assignmentInModelOptional = model.getAssignment(assignmentToCancelLeave);
        if (assignmentInModelOptional.isEmpty()) {
            throw new CommandException(String.format(Messages.MESSAGE_NO_ASSIGNMENT_FOUND,
                    workerToCancelLeave, shiftToCancelLeaveFrom));
        }

        Assignment assignmentInModel = assignmentInModelOptional.get();
        if (!(assignmentInModel.getRole().equals(new Leave()))) {
            throw new CommandException(String.format(Messages.MESSAGE_NO_LEAVE_FOUND,
                    workerToCancelLeave, shiftToCancelLeaveFrom));
        }

        model.deleteAssignment(assignmentInModel);

        return new CommandResult(String.format(MESSAGE_CANCEL_LEAVE_SUCCESS, assignmentInModel));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof CancelLeaveCommand)) {
            return false;
        }

        CancelLeaveCommand e = (CancelLeaveCommand) other;
        return shiftIndex.equals(e.shiftIndex)
                && workerIndex.equals(e.workerIndex);
    }
}
