package mcscheduler.logic.commands;

import static java.util.Objects.requireNonNull;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_ROLE;

import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;
import mcscheduler.commons.core.Messages;
import mcscheduler.commons.core.index.Index;
import mcscheduler.logic.commands.exceptions.CommandException;
import mcscheduler.model.Model;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.role.Role;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.worker.Worker;

/**
 * Prints a list of workers who are available for the selected shift.
 * Excludes workers already assigned to the selected shift.
 */
public class WorkerAvailableCommand extends Command {

    public static final String COMMAND_WORD = "worker-avail";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists all available workers for the selected shift and role.\n"
            + "Parameters: SHIFT_INDEX (must be a positive integer) "
            + PREFIX_ROLE + "ROLE\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_ROLE + "Cashier";

    public static final String MESSAGE_HAS_AVAIL_WORKERS_SUCCESS =
            "List of available workers for shift %1$s (Role: %2$s) \n%3$s";

    public static final String MESSAGE_NO_AVAIL_WORKERS_SUCCESS =
            "Could not find any available workers for shift %1$s (Role: %2$s).";

    public static final String MESSAGE_ROLE_FULL_OR_NOT_REQUIRED =
            "The specified role is not required by the selected shift or has already been fully filled";

    private final Index targetIndex;
    private final Role role;

    /**
     * @param targetIndex of the shift in the filtered shift list to check
     * @param role of the shift to be filled by available workers
     */
    public WorkerAvailableCommand(Index targetIndex, Role role) {
        this.targetIndex = targetIndex;
        this.role = role;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Shift> lastShownShiftList = model.getFilteredShiftList();

        if (targetIndex.getZeroBased() >= lastShownShiftList.size()) {
            throw new CommandException(
                    CommandUtil.printOutOfBoundsShiftIndexError(targetIndex, MESSAGE_USAGE));
        }

        if (!model.hasRole(role)) {
            throw new CommandException(String.format(Messages.MESSAGE_ROLE_NOT_FOUND, role));
        }

        Shift selectedShift = lastShownShiftList.get(targetIndex.getZeroBased());
        if (!selectedShift.isRoleRequired(role)) {
            throw new CommandException(MESSAGE_ROLE_FULL_OR_NOT_REQUIRED);
        }

        List<Pair<Worker, Index>> availableWorkers = findAvailableWorkers(model, selectedShift);

        if (availableWorkers.size() == 0) {
            return new CommandResult(
                    String.format(MESSAGE_NO_AVAIL_WORKERS_SUCCESS, targetIndex.getOneBased(), role.getRole()));
        }

        String printableListOfAvailableWorkers = printListOfAvailableWorkers(availableWorkers);

        return new CommandResult(
                String.format(MESSAGE_HAS_AVAIL_WORKERS_SUCCESS, targetIndex.getOneBased(), role.getRole(),
                        printableListOfAvailableWorkers));
    }

    private List<Pair<Worker, Index>> findAvailableWorkers(Model model, Shift selectedShift) {
        List<Pair<Worker, Index>> availableWorkers = new ArrayList<>();

        List<Worker> lastShownWorkerList = model.getFilteredWorkerList();
        List<Assignment> fullAssignmentList = model.getFullAssignmentList();

        List<Assignment> assignmentsForSelectedShift = new ArrayList<>();

        for (Assignment assignment : fullAssignmentList) {
            if (selectedShift.equals(assignment.getShift())) {
                assignmentsForSelectedShift.add(assignment);
            }
        }

        for (int i = 0; i < lastShownWorkerList.size(); i++) {
            Worker worker = lastShownWorkerList.get(i);
            if (checkIfWorkerAlreadyAssigned(assignmentsForSelectedShift, worker) || !worker.isFitForRole(this.role)) {
                continue;
            }

            if (!worker.isUnavailable(selectedShift)) {
                Index currentIndex = Index.fromZeroBased(i);
                availableWorkers.add(new Pair<>(worker, currentIndex));
            }
        }
        return availableWorkers;
    }

    private boolean checkIfWorkerAlreadyAssigned(List<Assignment> assignmentList, Worker worker) {
        for (Assignment assignment : assignmentList) {
            if (worker.equals(assignment.getWorker())) {
                return true;
            }
        }
        return false;
    }

    private String printListOfAvailableWorkers(List<Pair<Worker, Index>> availableWorkers) {
        String output = "";
        for (Pair<Worker, Index> pair : availableWorkers) {
            output += String.format("%1$s\t%2$s\n", pair.getValue().getOneBased(), pair.getKey().getName());
        }

        return output;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof WorkerAvailableCommand // instanceof handles nulls
                && targetIndex.equals(((WorkerAvailableCommand) other).targetIndex)
                && role.equals(((WorkerAvailableCommand) other).role)); // state check
    }
}
