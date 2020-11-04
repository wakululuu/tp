package mcscheduler.logic.commands;

import static java.util.Objects.requireNonNull;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_SHIFT;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_WORKER_ROLE;
import static mcscheduler.model.Model.PREDICATE_SHOW_ALL_SHIFTS;
import static mcscheduler.model.Model.PREDICATE_SHOW_ALL_WORKERS;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import mcscheduler.commons.core.Messages;
import mcscheduler.commons.core.index.Index;
import mcscheduler.commons.util.CollectionUtil;
import mcscheduler.logic.commands.exceptions.CommandException;
import mcscheduler.model.Model;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.assignment.WorkerRolePair;
import mcscheduler.model.role.Role;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.worker.Worker;

/**
 * Adds a shift, worker and shift assignment to the McScheduler.
 */
public class AssignCommand extends Command {
    public static final String COMMAND_WORD = "assign";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds shift, worker(s) and role(s) assignment(s) "
            + "to the McScheduler by the index numbers used in the last worker and shift listings. "
            + "\nParameters: "
            + PREFIX_SHIFT + "SHIFT_INDEX (must be a positive integer) "
            + "{" + PREFIX_WORKER_ROLE + "WORKER_INDEX (must be a positive integer) ROLE}...\n"
            + "Example: " + COMMAND_WORD
            + " s/4 "
            + "w/1 Cashier "
            + "w/3 Janitor";

    public static final String MESSAGE_ASSIGN_SUCCESS = "%1$d new assignment(s) added:\n%2$s";
    public static final String MESSAGE_DUPLICATE_ASSIGNMENT = "This assignment already exists in the McScheduler: %1$s";

    private final Index shiftIndex;
    private final Set<WorkerRolePair> workerRolePairs;

    /**
     * Creates an AssignCommand to add an assignment of the specified {@code Shift}, {@code Worker} and {@code Role}.
     *
     * @param shiftIndex  of the shift in the filtered shift list.
     * @param workerRolePairs a set of worker-roles to be assined to the shift
     */
    public AssignCommand(Index shiftIndex, Set<WorkerRolePair> workerRolePairs) {
        CollectionUtil.requireAllNonNull(shiftIndex, workerRolePairs);

        this.shiftIndex = shiftIndex;
        this.workerRolePairs = workerRolePairs;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Worker> lastShownWorkerList = model.getFilteredWorkerList();
        List<Shift> lastShownShiftList = model.getFilteredShiftList();

        if (shiftIndex.getZeroBased() >= lastShownShiftList.size()) {
            throw new CommandException(
                    String.format(Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX, shiftIndex.getOneBased()));
        }

        List<Assignment> assignmentsToAdd = new ArrayList<>();
        // Check for: worker existence, model hasRole, worker is fit, worker is available, role required in shift
        for (WorkerRolePair workerRolePair : workerRolePairs) {
            if (workerRolePair.getWorkerIndex().getZeroBased() >= lastShownWorkerList.size()) {
                throw new CommandException(String.format(Messages.MESSAGE_INVALID_WORKER_DISPLAYED_INDEX,
                            workerRolePair.getWorkerIndex().getOneBased()));
            }
            Worker workerToAssign = lastShownWorkerList.get(workerRolePair.getWorkerIndex().getZeroBased());
            Shift shiftToAssign = lastShownShiftList.get(shiftIndex.getZeroBased());
            Role role = workerRolePair.getRole();

            if (!model.hasRole(role)) {
                throw new CommandException(String.format(Messages.MESSAGE_ROLE_NOT_FOUND, workerRolePair.getRole()));
            }
            if (!workerToAssign.isFitForRole(role)) {
                throw new CommandException(String.format(Messages.MESSAGE_INVALID_ASSIGNMENT_WORKER_ROLE,
                            workerToAssign.getName(), role));
            }
            if (workerToAssign.isUnavailable(shiftToAssign)) {
                throw new CommandException(String.format(
                        Messages.MESSAGE_INVALID_ASSIGNMENT_UNAVAILABLE, workerToAssign.getName(), shiftToAssign));
            }

            if (!shiftToAssign.isRoleRequired(role)) {
                throw new CommandException(Messages.MESSAGE_INVALID_ASSIGNMENT_NOT_REQUIRED);
            }

            Assignment assignmentToAdd = new Assignment(shiftToAssign, workerToAssign, role);
            if (model.hasAssignment(assignmentToAdd)) {
                throw new CommandException(
                    String.format(MESSAGE_DUPLICATE_ASSIGNMENT, model.getAssignment(assignmentToAdd).get()));
            }
            assignmentsToAdd.add(assignmentToAdd);
        }

        // Add assignments
        StringBuilder assignStringBuilder = new StringBuilder();

        for (Assignment assignment : assignmentsToAdd) {
            model.addAssignment(assignment);

            assignStringBuilder.append(assignment);
            assignStringBuilder.append("\n");
            Shift.updateRoleRequirements(model, assignment.getShift(), assignment.getRole());
        }
        model.updateFilteredShiftList(PREDICATE_SHOW_ALL_SHIFTS);
        model.updateFilteredWorkerList(PREDICATE_SHOW_ALL_WORKERS);

        return new CommandResult(
                String.format(MESSAGE_ASSIGN_SUCCESS, workerRolePairs.size(), assignStringBuilder.toString()));
    }


    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AssignCommand)) {
            return false;
        }

        // state check
        AssignCommand e = (AssignCommand) other;
        return shiftIndex.equals(e.shiftIndex)
                && workerRolePairs.equals(e.workerRolePairs);
    }
}
