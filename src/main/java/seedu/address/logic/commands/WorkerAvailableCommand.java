package seedu.address.logic.commands;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.shift.RoleRequirement;
import seedu.address.model.shift.Shift;
import seedu.address.model.tag.Role;
import seedu.address.model.worker.Unavailability;
import seedu.address.model.worker.Worker;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javafx.util.Pair;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;

/**
 * Prints a list of workers who are available for the selected shift.
 * Excludes workers already assigned to the selected shift.
 */
public class WorkerAvailableCommand extends Command {

    public static final String COMMAND_WORD = "worker-avail";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Prints a list of workers who are available for the selected shift and role.\n"
            + "Parameters: SHIFT_INDEX (must be a positive integer) "
            + PREFIX_ROLE + "ROLE\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_ROLE + "Cashier";

    public static final String MESSAGE_HAS_AVAIL_WORKERS_SUCCESS =
            "List of available workers for shift %1$s (Role: %2$s) \n%3$s";

    public static final String MESSAGE_NO_AVAIL_WORKERS_SUCCESS =
            "Could not find any available workers for shift %1$s (Role: %2$s).";

    public static final String MESSAGE_INVALID_ROLE =
            "The specified role is not required by the selected shift or has already been fully filled";

    private final Index targetIndex;
    private final Role role;

    public WorkerAvailableCommand(Index targetIndex, Role role) {
        this.targetIndex = targetIndex;
        this.role = role;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Shift> lastShownShiftList = model.getFilteredShiftList();

        if (targetIndex.getZeroBased() >= lastShownShiftList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
        }

        if (!model.hasRole(this.role)) {
            throw new CommandException(String.format(Messages.MESSAGE_ROLE_NOT_FOUND, role));
        }

        Shift selectedShift = lastShownShiftList.get(targetIndex.getZeroBased());
        if (!selectedShift.isRoleRequired(this.role)) {
            throw new CommandException(MESSAGE_INVALID_ROLE);
        }

        List<Pair<Worker, Index>> availableWorkers = findAvailableWorkers(model, selectedShift);

        if (availableWorkers.size() == 0) {
            return new CommandResult(
                    String.format(MESSAGE_NO_AVAIL_WORKERS_SUCCESS,
                            this.targetIndex.getOneBased(), this.role.getRole()));
        }

        String printableListOfAvailableWorkers = printListOfAvailableWorkers(availableWorkers);

        return new CommandResult(
                String.format(MESSAGE_HAS_AVAIL_WORKERS_SUCCESS, this.targetIndex.getOneBased(),
                        this.role.getRole(), printableListOfAvailableWorkers));
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
            if (checkIfWorkerAlreadyAssigned(assignmentsForSelectedShift, worker) || !checkIfWorkerHasRole(worker)) {
                continue;
            }

            Set<Unavailability> workerUnavailableTimings = worker.getUnavailableTimings();

            boolean isWorkerAvailable = true;

            for (Unavailability unavail : workerUnavailableTimings) {
                if (selectedShift.getShiftDay().equals(unavail.getDay()) &&
                        selectedShift.getShiftTime().equals(unavail.getTime())) {
                    isWorkerAvailable = false;
                    break;
                }
            }
            if (isWorkerAvailable) {
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

    private boolean checkIfWorkerHasRole(Worker worker) {
        Set<Role> workerRoles = worker.getRoles();
        for (Role workerRole : workerRoles) {
            if (workerRole.equals(this.role)) {
                return true;
            }
        }
        return false;
    }

    private String printListOfAvailableWorkers(List<Pair<Worker, Index>> availableWorkers) {
        String output = "";
        for (Pair<Worker, Index> pair : availableWorkers) {
            output += String.format("%1$s    %2$s\n", pair.getValue().getOneBased(), pair.getKey().getName());
        }

        return output;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof WorkerAvailableCommand // instanceof handles nulls
                && targetIndex.equals(((WorkerAvailableCommand) other).targetIndex)); // state check
    }
}
