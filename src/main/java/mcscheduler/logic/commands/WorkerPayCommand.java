package mcscheduler.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import mcscheduler.commons.core.index.Index;
import mcscheduler.logic.commands.exceptions.CommandException;
import mcscheduler.model.Model;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.worker.Worker;

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

    public static final String MESSAGE_SHOW_PAY_SUCCESS = "%1$s's pay for the week:\n"
            + "$%2$.2f/hr x %3$dhr/shift x %4$,d shift(s) = $%5$,.2f";

    private final Index targetIndex;

    public WorkerPayCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Worker> lastShownList = model.getFilteredWorkerList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(
                    CommandUtil.printOutOfBoundsWorkerIndexError(targetIndex, MESSAGE_USAGE));
        }

        Worker selectedWorker = lastShownList.get(targetIndex.getZeroBased());
        int numShiftsAssigned = model.calculateWorkerShiftsAssigned(selectedWorker);
        float calculatedPay = numShiftsAssigned * selectedWorker.getPay().getValue() * Shift.HOURS_PER_SHIFT;

        return new CommandResult(String.format(MESSAGE_SHOW_PAY_SUCCESS, selectedWorker.getName(),
                selectedWorker.getPay().getValue(), Shift.HOURS_PER_SHIFT, numShiftsAssigned, calculatedPay));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof WorkerPayCommand // instanceof handles nulls
                && targetIndex.equals(((WorkerPayCommand) other).targetIndex)); // state check
    }


}
