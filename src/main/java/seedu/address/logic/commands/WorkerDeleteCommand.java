package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.worker.Worker;

/**
 * Deletes a worker identified using it's displayed index from the address book.
 */
public class WorkerDeleteCommand extends Command {

    public static final String COMMAND_WORD = "worker-delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the worker identified by the index number used in the displayed worker list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_WORKER_SUCCESS = "Deleted worker: %1$s";

    private final Index targetIndex;

    public WorkerDeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Worker> lastShownList = model.getFilteredWorkerList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_WORKER_DISPLAYED_INDEX);
        }

        Worker workerToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deleteWorker(workerToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_WORKER_SUCCESS, workerToDelete));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof WorkerDeleteCommand // instanceof handles nulls
                && targetIndex.equals(((WorkerDeleteCommand) other).targetIndex)); // state check
    }
}
