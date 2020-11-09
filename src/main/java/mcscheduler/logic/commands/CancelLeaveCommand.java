package mcscheduler.logic.commands;

import static java.util.Objects.requireNonNull;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_SHIFT;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_WORKER;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import mcscheduler.commons.core.Messages;
import mcscheduler.commons.core.index.Index;
import mcscheduler.commons.util.CollectionUtil;
import mcscheduler.logic.commands.exceptions.CommandException;
import mcscheduler.model.Model;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.assignment.exceptions.AssignmentNotFoundException;
import mcscheduler.model.role.Leave;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.worker.Worker;


/**
 * Cancel a worker's leave for a particular shift.
 */
public class CancelLeaveCommand extends Command {

    public static final String COMMAND_WORD = "cancel-leave";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Cancels the specified worker(s)'s leave from the "
            + "specified shift by the index numbers used in the last worker and shift listings. "
            + "\nParameters: "
            + PREFIX_SHIFT + "SHIFT_INDEX (must be a positive integer) "
            + PREFIX_WORKER + "WORKER_INDEX (must be a positive integer)...\n"
            + "Example: " + COMMAND_WORD
            + " s/1 "
            + "w/4 "
            + "w/2";

    public static final String MESSAGE_CANCEL_LEAVE_SUCCESS_PREFIX = "[Leave Cancelled] ";
    public static final String MESSAGE_CANCEL_LEAVE_SUCCESS = MESSAGE_CANCEL_LEAVE_SUCCESS_PREFIX
            + UnassignCommand.MESSAGE_UNASSIGN_SUCCESS;
    public static final String MESSAGE_WORKER_NOT_ON_LEAVE =
            "%1$s ( w/%2$d ) is not on leave. Please remove %1$s from the \"cancel-leave\" command";;

    private final Index shiftIndex;
    private final Set<Index> workerIndexes;

    /**
     * Creates a CancelLeaveCommand to cancel the leave of the specified {@code Worker}
     * from the specified {@code Shift}.
     *
     * @param shiftIndex of the shift in the filtered shift list to cancel the worker's leave from.
     * @param workerIndexes of the worker(s) in the filtered worker list whose leave is to be cancelled.
     */
    public CancelLeaveCommand(Index shiftIndex, Set<Index> workerIndexes) {
        CollectionUtil.requireAllNonNull(shiftIndex, workerIndexes);

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

        StringBuilder assignStringBuilder = new StringBuilder();
        for (Index workerIndex: workerIndexes) {
            if (workerIndex.getZeroBased() >= lastShownWorkerList.size()) {
                throw new CommandException(
                        CommandUtil.printOutOfBoundsWorkerIndexError(workerIndex, MESSAGE_USAGE));
            }

            Worker workerToCancelLeave = lastShownWorkerList.get(workerIndex.getZeroBased());
            Shift shiftToCancelLeaveFrom = lastShownShiftList.get(shiftIndex.getZeroBased());
            Assignment assignmentToCancelLeave = new Assignment(shiftToCancelLeaveFrom, workerToCancelLeave);

            Optional<Assignment> assignmentInModelOptional = model.getAssignment(assignmentToCancelLeave);
            if (assignmentInModelOptional.isEmpty()) {
                throw new CommandException(String.format(MESSAGE_WORKER_NOT_ON_LEAVE,
                        workerToCancelLeave.getName(), workerIndex.getOneBased()));
            }

            Assignment assignmentInModel = assignmentInModelOptional.get();
            if (!Leave.isLeave(assignmentInModel.getRole())) {
                throw new CommandException(String.format(MESSAGE_WORKER_NOT_ON_LEAVE,
                        workerToCancelLeave.getName(), workerIndex.getOneBased()));
            }

            try {
                model.deleteAssignment(assignmentInModel);
            } catch (AssignmentNotFoundException ex) {
                throw new CommandException(String.format(Messages.MESSAGE_NO_ASSIGNMENT_FOUND,
                        workerToCancelLeave, shiftToCancelLeaveFrom));
            }
            assignStringBuilder.append(assignmentInModel + "\n");
        }

        return new CommandResult(String.format(
                MESSAGE_CANCEL_LEAVE_SUCCESS, workerIndexes.size(), assignStringBuilder.toString()));
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
                && workerIndexes.toString().equals(e.workerIndexes.toString());
    }
}
