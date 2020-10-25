package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SHIFT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WORKER;

import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;


/**
 * Cancel a worker's leave for a particular shift.
 */
public class CancelLeaveCommand extends Command {

    public static final String COMMAND_WORD = "cancel-leave";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Cancels the specified worker(s)'s leave from the "
            + "specified shift by the index numbers used in the last worker and shift listings. "
            + "\nParameters: "
            + PREFIX_SHIFT + "SHIFT_INDEX (must be a positive integer) "
            + PREFIX_WORKER + "WORKER_INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD
            + " s/1 "
            + "w/4 "
            + "w/2";

    public static final String MESSAGE_CANCEL_LEAVE_SUCCESS_PREFIX = "[Leave Cancelled] ";

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
        requireAllNonNull(shiftIndex, workerIndexes);

        this.shiftIndex = shiftIndex;
        this.workerIndexes = workerIndexes;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        CommandResult commandResult = new UnassignCommand(shiftIndex, workerIndexes).execute(model);

        return new CommandResult(MESSAGE_CANCEL_LEAVE_SUCCESS_PREFIX
                + commandResult.getFeedbackToUser());
    }
}
