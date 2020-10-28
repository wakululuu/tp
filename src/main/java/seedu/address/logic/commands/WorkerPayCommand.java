package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.worker.Worker;



/**
 * Prints the pay earned by a worker identified using their displayed index.
 */
public class WorkerPayCommand extends Command {

    public static final String COMMAND_WORD = "worker-pay";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Calculates the weekly pay earned by a worker identified by the "
            + "index number used in the displayed worker list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SHOW_PAY_SUCCESS = "Pay earned by worker (%1$s): $ %2$.2f";

    private final Index targetIndex;

    public WorkerPayCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Worker> lastShownList = model.getFilteredWorkerList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_WORKER_DISPLAYED_INDEX);
        }

        Worker selectedWorker = lastShownList.get(targetIndex.getZeroBased());
        float calculatedPay = model.calculateWorkerPay(selectedWorker);

        return new CommandResult(String.format(MESSAGE_SHOW_PAY_SUCCESS, selectedWorker.getName(), calculatedPay));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof WorkerPayCommand // instanceof handles nulls
                && targetIndex.equals(((WorkerPayCommand) other).targetIndex)); // state check
    }


}
