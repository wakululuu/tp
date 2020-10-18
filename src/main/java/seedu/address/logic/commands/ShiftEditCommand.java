package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE_REQUIREMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SHIFT_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SHIFT_TIME;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_SHIFTS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.shift.RoleRequirement;
import seedu.address.model.shift.Shift;
import seedu.address.model.shift.ShiftDay;
import seedu.address.model.shift.ShiftTime;
import seedu.address.model.shift.WorkerRoleAssignment;
import seedu.address.model.worker.ShiftRoleAssignment;
import seedu.address.model.worker.Worker;

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

    private final Index index;
    private final EditShiftDescriptor editShiftDescriptor;

    /**
     * Creates a ShiftEditCommand from the given required information.
     * @param index of the shift in the filtered shift list to edit.
     * @param editShiftDescriptor detaiils to edit the shift with.
     */
    public ShiftEditCommand(Index index, EditShiftDescriptor editShiftDescriptor) {
        requireAllNonNull(index, editShiftDescriptor);

        this.index = index;
        this.editShiftDescriptor = new EditShiftDescriptor(editShiftDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Shift> lastShownList = model.getFilteredShiftList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
        }

        Shift shiftToEdit = lastShownList.get(index.getZeroBased());
        Shift editedShift = createEditedShift(shiftToEdit, editShiftDescriptor);

        if (!shiftToEdit.isSameShift(editedShift) && model.hasShift(editedShift)) {
            throw new CommandException(MESSAGE_DUPLICATE_SHIFT);
        }

        editShiftInAssignedWorkers(model, shiftToEdit, editedShift);
        model.setShift(shiftToEdit, editedShift);
        model.updateFilteredShiftList(PREDICATE_SHOW_ALL_SHIFTS);

        return new CommandResult(String.format(MESSAGE_EDIT_SHIFT_SUCCESS, editedShift));
    }

    private static Shift createEditedShift(Shift shiftToEdit, EditShiftDescriptor editShiftDescriptor) {
        assert shiftToEdit != null;

        ShiftDay updatedDay = editShiftDescriptor.getShiftDay().orElse(shiftToEdit.getShiftDay());
        ShiftTime updatedTime = editShiftDescriptor.getShiftTime().orElse(shiftToEdit.getShiftTime());
        Set<RoleRequirement> updatedRoleRequirements = editShiftDescriptor.getRoleRequirements()
                .orElse(shiftToEdit.getRoleRequirements());

        return new Shift(updatedDay, updatedTime, updatedRoleRequirements, shiftToEdit.getWorkerRoleAssignments());
    }

    private void editShiftInAssignedWorkers(Model model, Shift shiftToEdit, Shift editedShift) {
        requireAllNonNull(model, shiftToEdit, editedShift);
        List<Worker> fullWorkerList = model.getFullWorkerList();

        Stream<Worker> assignedWorkers = shiftToEdit.getWorkerRoleAssignments()
                .stream()
                .map(WorkerRoleAssignment::getWorker);

        assignedWorkers.forEach(assignedWorker -> {
            for (Worker worker : fullWorkerList) {
                if (assignedWorker.isSameWorker(worker)) {
                    editShiftInWorker(model, worker, shiftToEdit, editedShift);
                    break;
                }
            }
        });
    }

    private void editShiftInWorker(Model model, Worker worker, Shift shiftToEdit, Shift editedShift) {
        Set<ShiftRoleAssignment> editedAssignments = createEditedShiftRoleAssignments(worker, shiftToEdit,
                editedShift);
        Worker editedWorker = new Worker(worker.getName(), worker.getPhone(), worker.getPay(), worker.getAddress(),
                worker.getRoles(), editedAssignments);
        model.setWorker(worker, editedWorker);
    }

    private Set<ShiftRoleAssignment> createEditedShiftRoleAssignments(Worker worker, Shift shiftToEdit,
            Shift editedShift) {
        Set<ShiftRoleAssignment> shiftRoleAssignments = new HashSet<>(worker.getShiftRoleAssignments());

        for (ShiftRoleAssignment assignment : shiftRoleAssignments) {
            if (shiftToEdit.isSameShift(assignment.getShift())) {
                ShiftRoleAssignment editedAssignment = new ShiftRoleAssignment(editedShift, assignment.getRole());
                shiftRoleAssignments.add(editedAssignment);
                shiftRoleAssignments.remove(assignment);
            }
        }

        return shiftRoleAssignments;
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
