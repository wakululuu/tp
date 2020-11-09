package mcscheduler.logic.commands;

import static java.util.Objects.requireNonNull;
import static mcscheduler.commons.core.Messages.MESSAGE_DUPLICATE_ROLE;
import static mcscheduler.commons.core.Messages.MESSAGE_ROLE_NOT_EDITED;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mcscheduler.commons.core.Messages;
import mcscheduler.commons.core.index.Index;
import mcscheduler.commons.util.CollectionUtil;
import mcscheduler.logic.commands.exceptions.CommandException;
import mcscheduler.model.Model;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.role.Leave;
import mcscheduler.model.role.Role;
import mcscheduler.model.shift.RoleRequirement;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.worker.Worker;

/**
 * Edits the role identified in the McScheduler.
 */
public class RoleEditCommand extends Command {

    public static final String COMMAND_WORD = "role-edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the role identified by the index number used in the displayed role list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "ROLE (must be alphanumeric and can contain spaces)\n"
            + "Example: " + COMMAND_WORD + " 1 burger flipper";

    public static final String MESSAGE_EDIT_ROLE_SUCCESS = "Edited role: %1$s | Previous role: %2$s";

    private final Index targetIndex;
    private final Role editedRole;

    /**
     * Creates a {@code RoleEditCommand}.
     *
     * @param targetIndex of the role in the filtered role list to edit.
     * @param editedRole updated role to replace the original in the model.
     */
    public RoleEditCommand(Index targetIndex, Role editedRole) {
        this.targetIndex = targetIndex;
        this.editedRole = editedRole;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Role> roleList = model.getFilteredRoleList();

        if (targetIndex.getZeroBased() >= roleList.size()) {
            throw new CommandException(
                    CommandUtil.printOutOfBoundsRoleIndexError(targetIndex, MESSAGE_USAGE));
        }

        Role roleToEdit = roleList.get(targetIndex.getZeroBased());
        assert !Leave.isLeave(roleToEdit) : "Leave should not be edited";

        if (Leave.isLeave(editedRole)) {
            throw new CommandException(Messages.MESSAGE_DO_NOT_MODIFY_LEAVE);
        }

        if (roleToEdit.equals(editedRole)) {
            throw new CommandException(String.format(MESSAGE_ROLE_NOT_EDITED, roleToEdit));
        } else if (model.hasRole(editedRole)) {
            throw new CommandException(MESSAGE_DUPLICATE_ROLE);
        }

        editRoleInShifts(model, roleToEdit);
        editRoleInWorkers(model, roleToEdit);
        editRoleInAssignments(model, roleToEdit);
        model.setRole(roleToEdit, editedRole);

        return new CommandResult(String.format(MESSAGE_EDIT_ROLE_SUCCESS, editedRole, roleToEdit));
    }

    private void editRoleInShifts(Model model, Role roleToEdit) {
        CollectionUtil.requireAllNonNull(model, roleToEdit);
        List<Shift> fullShiftList = model.getFullShiftList();

        for (Shift shift : fullShiftList) {
            Set<RoleRequirement> updatedRoleRequirements = getEditedRoleRequirements(shift.getRoleRequirements(),
                    roleToEdit);
            Shift updatedShift = new Shift(shift.getShiftDay(), shift.getShiftTime(), updatedRoleRequirements);
            model.setShift(shift, updatedShift);
        }
    }

    private Set<RoleRequirement> getEditedRoleRequirements(Set<RoleRequirement> roleRequirements, Role roleToEdit) {
        Set<RoleRequirement> roleRequirementsCopy = new HashSet<>(roleRequirements);

        for (RoleRequirement requirement : roleRequirements) {
            if (requirement.getRole().equals(roleToEdit)) {
                RoleRequirement updatedRequirement = new RoleRequirement(editedRole, requirement.getQuantityRequired(),
                        requirement.getQuantityFilled());

                roleRequirementsCopy.remove(requirement);
                roleRequirementsCopy.add(updatedRequirement);
            }
        }

        return roleRequirementsCopy;
    }

    private void editRoleInWorkers(Model model, Role roleToEdit) {
        CollectionUtil.requireAllNonNull(model, roleToEdit);
        List<Worker> fullWorkerList = model.getFullWorkerList();

        for (Worker worker : fullWorkerList) {
            Set<Role> updatedRoles = getEditedRoles(worker.getRoles(), roleToEdit);

            Worker updatedWorker = new Worker(worker.getName(), worker.getPhone(), worker.getPay(),
                    worker.getAddress(), updatedRoles, worker.getUnavailableTimings());
            model.setWorker(worker, updatedWorker);
        }
    }

    private Set<Role> getEditedRoles(Set<Role> roles, Role roleToEdit) {
        Set<Role> rolesCopy = new HashSet<>(roles);
        for (Role role : roles) {
            if (role.equals(roleToEdit)) {
                rolesCopy.remove(roleToEdit);
                rolesCopy.add(editedRole);
            }
        }
        return rolesCopy;
    }

    private void editRoleInAssignments(Model model, Role roleToEdit) {
        CollectionUtil.requireAllNonNull(model, roleToEdit);
        List<Assignment> fullAssignmentList = model.getFullAssignmentList();

        for (Assignment assignment : fullAssignmentList) {
            if (roleToEdit.equals(assignment.getRole())) {
                Assignment updatedAssignment = new Assignment(assignment.getShift(), assignment.getWorker(),
                        editedRole);
                model.setAssignment(assignment, updatedAssignment);
            }
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof RoleEditCommand // instanceof handles nulls
                && targetIndex.equals(((RoleEditCommand) other).targetIndex)); // state check
    }
}
