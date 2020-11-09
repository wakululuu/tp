package mcscheduler.logic.commands;

import static java.util.Objects.requireNonNull;
import static mcscheduler.commons.core.Messages.MESSAGE_DO_NOT_MODIFY_LEAVE;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_ROLE_REQUIREMENT;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_SHIFT_DAY;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_SHIFT_TIME;
import static mcscheduler.model.Model.PREDICATE_SHOW_ALL_SHIFTS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
import mcscheduler.model.shift.ShiftDay;
import mcscheduler.model.shift.ShiftTime;

/**
 * Edits the details of an existing shift in the App.
 */
public class ShiftEditCommand extends Command {

    public static final String COMMAND_WORD = "shift-edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the shift identified "
            + "by the index number used in the displayed worker list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_SHIFT_DAY + "DAY] "
            + "[" + PREFIX_SHIFT_TIME + "TIME] "
            + "[" + PREFIX_ROLE_REQUIREMENT + "ROLE QUANTITY_NEEDED]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_SHIFT_DAY + "Wed "
            + PREFIX_ROLE_REQUIREMENT + "Cashier 5";

    public static final String MESSAGE_EDIT_SHIFT_SUCCESS = "Edited Shift: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided";
    public static final String MESSAGE_DUPLICATE_SHIFT = "This shift already exists in the App";
    public static final String MESSAGE_UNASSIGN_WORKERS = "Some workers must be unassigned from their roles"
            + " to make this edit";

    private final Index index;
    private final EditShiftDescriptor editShiftDescriptor;

    /**
     * Creates a ShiftEditCommand from the given required information.
     * @param index of the shift in the filtered shift list to edit.
     * @param editShiftDescriptor details to edit the shift with.
     */
    public ShiftEditCommand(Index index, EditShiftDescriptor editShiftDescriptor) {
        CollectionUtil.requireAllNonNull(index, editShiftDescriptor);

        this.index = index;
        this.editShiftDescriptor = new EditShiftDescriptor(editShiftDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Shift> lastShownList = model.getFilteredShiftList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(
                    CommandUtil.printOutOfBoundsShiftIndexError(index, MESSAGE_USAGE));
        }

        Shift shiftToEdit = lastShownList.get(index.getZeroBased());
        Shift editedShift = createEditedShift(shiftToEdit, editShiftDescriptor);

        if (!shiftToEdit.isSameShift(editedShift) && model.hasShift(editedShift)) {
            throw new CommandException(MESSAGE_DUPLICATE_SHIFT);
        }
        if (editedShift.getRoleRequirements()
                .stream()
                .anyMatch(roleRequirement -> Leave.isLeave(roleRequirement.getRole()))) {
            throw new CommandException(MESSAGE_DO_NOT_MODIFY_LEAVE);
        }

        Set<RoleRequirement> roleRequirementSet = editedShift.getRoleRequirements();
        for (RoleRequirement requirement : roleRequirementSet) {
            if (!model.hasRole(requirement.getRole())) {
                throw new CommandException(String.format(Messages.MESSAGE_ROLE_NOT_FOUND, requirement.getRole()));
            }
        }

        model.setShift(shiftToEdit, editedShift);
        editShiftInAssignments(model, shiftToEdit, editedShift);
        model.updateFilteredShiftList(PREDICATE_SHOW_ALL_SHIFTS);

        return new CommandResult(String.format(MESSAGE_EDIT_SHIFT_SUCCESS, editedShift));
    }

    private static Shift createEditedShift(Shift shiftToEdit, EditShiftDescriptor editShiftDescriptor) {
        assert shiftToEdit != null;

        ShiftDay updatedDay = editShiftDescriptor.getShiftDay().orElse(shiftToEdit.getShiftDay());
        ShiftTime updatedTime = editShiftDescriptor.getShiftTime().orElse(shiftToEdit.getShiftTime());
        Set<RoleRequirement> updatedRoleRequirements = editShiftDescriptor.getRoleRequirements()
                .orElse(shiftToEdit.getRoleRequirements());

        return new Shift(updatedDay, updatedTime, updatedRoleRequirements);
    }

    private void editShiftInAssignments(Model model, Shift shiftToEdit, Shift editedShift) throws CommandException {
        CollectionUtil.requireAllNonNull(model, shiftToEdit, editedShift);
        List<Assignment> fullAssignmentList = model.getFullAssignmentList();
        List<Assignment> assignmentsToDelete = new ArrayList<>();
        List<Assignment> assignmentsToEdit = new ArrayList<>();
        Set<Role> newRoles = editedShift.getRoles();

        for (Assignment assignment : fullAssignmentList) {
            if (shiftToEdit.isSameShift(assignment.getShift())) {
                Role assignmentRole = assignment.getRole();
                if ((!newRoles.contains(assignmentRole) && !Leave.isLeave(assignmentRole))
                        || assignment.getWorker().isUnavailable(editedShift)) {
                    // This accounts for the case where the shift no longer has the role specified in the assignment
                    assignmentsToDelete.add(assignment);
                } else if (shiftToEdit.countRoleQuantityFilled(model, assignmentRole)
                        > getQuantityRequiredForRole(editedShift, assignmentRole)) {
                    // This accounts for the case where the quantity needed for a particular role is less than the
                    // current quantity filled
                    throw new CommandException(MESSAGE_UNASSIGN_WORKERS);
                } else {
                    assignmentsToEdit.add(assignment);
                }
            }
        }

        for (Assignment assignment : assignmentsToDelete) {
            model.deleteAssignment(assignment);
        }

        for (Assignment assignment : assignmentsToEdit) {
            Assignment updatedAssignment = createEditedAssignment(assignment, editedShift);
            model.setAssignment(assignment, updatedAssignment);
        }
    }

    private static int getQuantityRequiredForRole(Shift shift, Role role) {
        if (Leave.isLeave(role)) {
            return Integer.MAX_VALUE;
        }

        Set<RoleRequirement> roleRequirements = shift.getRoleRequirements();
        int quantityRequiredForRole = 0;
        for (RoleRequirement roleRequirement : roleRequirements) {
            if (roleRequirement.getRole().equals(role)) {
                quantityRequiredForRole = roleRequirement.getQuantityRequired();
            }
        }
        assert quantityRequiredForRole != 0;
        return quantityRequiredForRole;
    }

    private static Assignment createEditedAssignment(Assignment assignmentToEdit, Shift editedShift) {
        CollectionUtil.requireAllNonNull(assignmentToEdit, editedShift);
        return new Assignment(editedShift, assignmentToEdit.getWorker(), assignmentToEdit.getRole());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ShiftEditCommand)) {
            return false;
        }

        ShiftEditCommand s = (ShiftEditCommand) other;
        return index.equals(s.index)
                && editShiftDescriptor.equals(s.editShiftDescriptor);
    }

    public static class EditShiftDescriptor {
        private ShiftDay day;
        private ShiftTime time;
        private Set<RoleRequirement> roleRequirements;

        public EditShiftDescriptor() {}

        /**
         * Copy constructor.
         * A new {@code Set} is created for {@code roleRequirements} internally in
         * {@link EditShiftDescriptor#setRoleRequirements(Set)}
         */
        public EditShiftDescriptor(EditShiftDescriptor toCopy) {
            setShiftDay(toCopy.day);
            setShiftTime(toCopy.time);
            setRoleRequirements(toCopy.roleRequirements);
        }

        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(day, time, roleRequirements);
        }

        public void setShiftDay(ShiftDay day) {
            this.day = day;
        }

        public Optional<ShiftDay> getShiftDay() {
            return Optional.ofNullable(day);
        }

        public void setShiftTime(ShiftTime time) {
            this.time = time;
        }

        public Optional<ShiftTime> getShiftTime() {
            return Optional.ofNullable(time);
        }

        /**
         * Sets {@code roleRequirements} to this object's {@code roleRequirements}.
         * A defensive copy of {@code roleRequirements} is used internally.
         */
        public void setRoleRequirements(Set<RoleRequirement> roleRequirements) {
            this.roleRequirements = (roleRequirements != null) ? new HashSet<>(roleRequirements) : null;
        }

        /**
         * Returns an unmodifiable role requirements set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code roleRequirements} is null.
         */
        public Optional<Set<RoleRequirement>> getRoleRequirements() {
            return (roleRequirements != null)
                    ? Optional.of(Collections.unmodifiableSet(roleRequirements))
                    : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            if (!(other instanceof EditShiftDescriptor)) {
                return false;
            }

            EditShiftDescriptor e = (EditShiftDescriptor) other;

            return getShiftDay().equals(e.getShiftDay())
                    && getShiftTime().equals(e.getShiftTime())
                    && getRoleRequirements().equals(e.getRoleRequirements());
        }

    }

}
