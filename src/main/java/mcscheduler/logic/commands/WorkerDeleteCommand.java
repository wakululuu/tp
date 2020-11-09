package mcscheduler.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import mcscheduler.commons.core.index.Index;
import mcscheduler.commons.util.CollectionUtil;
import mcscheduler.logic.commands.exceptions.CommandException;
import mcscheduler.model.Model;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.worker.Worker;

/**
 * Deletes a worker identified using its displayed index from the McScheduler.
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
            throw new CommandException(
                    CommandUtil.printOutOfBoundsWorkerIndexError(targetIndex, MESSAGE_USAGE));
        }

        Worker workerToDelete = lastShownList.get(targetIndex.getZeroBased());
        deleteWorkerFromAssignments(model, workerToDelete);
        model.deleteWorker(workerToDelete);

        return new CommandResult(String.format(MESSAGE_DELETE_WORKER_SUCCESS, workerToDelete));
    }

    private void deleteWorkerFromAssignments(Model model, Worker workerToDelete) {
        CollectionUtil.requireAllNonNull(model, workerToDelete);
        List<Assignment> fullAssignmentList = model.getFullAssignmentList();
        List<Assignment> assignmentsToDelete = new ArrayList<>();

        for (Assignment assignment : fullAssignmentList) {
            if (workerToDelete.isSameWorker(assignment.getWorker())) {
                assignmentsToDelete.add(assignment);
            }
        }

        assignmentsToDelete.forEach(model::deleteAssignment);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof WorkerDeleteCommand // instanceof handles nulls
                && targetIndex.equals(((WorkerDeleteCommand) other).targetIndex)); // state check
    }
}
