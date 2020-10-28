package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SHIFT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WORKER;

import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.assignment.WorkerRolePair;
import seedu.address.model.tag.Leave;

/**
 * Assign a worker to take leave for a particular shift.
 */
public class TakeLeaveCommand extends Command {

    public static final String COMMAND_WORD = "take-leave";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Assigns the specified worker(s) to take leave "
            + "during the specified shift by the index numbers used in the last worker and shift listings. "
            + "\nParameters: "
            + PREFIX_SHIFT + "SHIFT_INDEX (must be a positive integer) "
            + "{" + PREFIX_WORKER + "WORKER_INDEX (must be a positive integer)}...\n"
            + "Example: " + COMMAND_WORD
            + " s/4 "
            + "w/1 "
            + "w/3";

    public static final String MESSAGE_TAKE_LEAVE_SUCCESS_PREFIX = "[Leave taken] ";

    private final Index shiftIndex;
    private final Set<Index> workerIndexes;

    /**
     * Creates a TakeLeaveCommand to assign
     * @param shiftIndex of the shift in the filtered shift list to assign the leave to.
     * @param workerIndexes of the worker(s) in the filtered worker list who are taking leave.
     */
    public TakeLeaveCommand(Index shiftIndex, Set<Index> workerIndexes) {
        requireAllNonNull(shiftIndex, workerIndexes);

        this.shiftIndex = shiftIndex;
        this.workerIndexes = workerIndexes;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Set<WorkerRolePair> workerLeaves = new HashSet<>();
        for (Index i: workerIndexes) {
            workerLeaves.add(new WorkerRolePair(i, new Leave()));
        }
        CommandResult commandResult = new AssignCommand(shiftIndex, workerLeaves).execute(model);

        return new CommandResult(MESSAGE_TAKE_LEAVE_SUCCESS_PREFIX + commandResult.getFeedbackToUser());
    }

    @Override
    public boolean equals(Object other) {
        // same object
        if (other == this) {
            return true;
        }

        // different type including null
        if (!(other instanceof TakeLeaveCommand)) {
            return false;
        }

        TakeLeaveCommand c = (TakeLeaveCommand) other;
        return shiftIndex.equals(c.shiftIndex)
                && workerIndexes.equals(c.workerIndexes);
    }

}
