package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.shift.RoleRequirement;
import seedu.address.model.shift.Shift;
import seedu.address.model.tag.Role;
import seedu.address.model.worker.Worker;

/**
 * Deletes a role identified using its displayed index from the address book.
 */
public class RoleDeleteCommand extends Command {

    public static final String COMMAND_WORD = "role-delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the role identified by the index number used in the displayed role list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_ROLE_SUCCESS = "Deleted role: %1$s";

    private final Index targetIndex;

    public RoleDeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Role> roleList = model.getFilteredRoleList();

        if (targetIndex.getZeroBased() >= roleList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ROLE_DISPLAYED_INDEX);
        }

        Role roleToDelete = roleList.get(targetIndex.getZeroBased());
        deleteRoleFromShifts(model, roleToDelete);
        deleteRoleFromWorkers(model, roleToDelete);
        deleteRoleFromAssignments(model, roleToDelete);
        model.deleteRole(roleToDelete);

        return new CommandResult(String.format(MESSAGE_DELETE_ROLE_SUCCESS, roleToDelete));
    }

    private void deleteRoleFromShifts(Model model, Role roleToDelete) {
        requireAllNonNull(model, roleToDelete);
        List<Shift> fullShiftList = model.getFullShiftList();

        for (Shift shift : fullShiftList) {
            Set<RoleRequirement> updatedRoleRequirements = new HashSet<>(shift.getRoleRequirements());
            updatedRoleRequirements.removeIf(roleRequirement -> roleRequirement.getRole().equals(roleToDelete));

            Shift updatedShift = new Shift(shift.getShiftDay(), shift.getShiftTime(), updatedRoleRequirements);
            model.setShift(shift, updatedShift);
        }
    }

    private void deleteRoleFromWorkers(Model model, Role roleToDelete) {
        requireAllNonNull(model, roleToDelete);
        List<Worker> fullWorkerList = model.getFullWorkerList();

        for (Worker worker : fullWorkerList) {
            Set<Role> updatedRoles = new HashSet<>(worker.getRoles());
            updatedRoles.removeIf(role -> role.equals(roleToDelete));

            Worker updatedWorker = new Worker(worker.getName(), worker.getPhone(), worker.getPay(),
                    worker.getAddress(), updatedRoles, worker.getUnavailableTimings());
            model.setWorker(worker, updatedWorker);
        }
    }

    private void deleteRoleFromAssignments(Model model, Role roleToDelete) {
        requireAllNonNull(model, roleToDelete);
        List<Assignment> fullAssignmentList = model.getFullAssignmentList();
        List<Assignment> assignmentsToDelete = new ArrayList<>();

        for (Assignment assignment : fullAssignmentList) {
            if (roleToDelete.equals(assignment.getRole())) {
                assignmentsToDelete.add(assignment);
            }
        }

        assignmentsToDelete.forEach(model::deleteAssignment);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof RoleDeleteCommand // instanceof handles nulls
                && targetIndex.equals(((RoleDeleteCommand) other).targetIndex)); // state check
    }
}
