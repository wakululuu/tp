package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_UNAVAILABILITY;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_WORKERS;

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
import seedu.address.model.shift.Shift;
import seedu.address.model.shift.WorkerRoleAssignment;
import seedu.address.model.tag.Role;
import seedu.address.model.worker.Address;
import seedu.address.model.worker.Name;
import seedu.address.model.worker.Pay;
import seedu.address.model.worker.Phone;
import seedu.address.model.worker.ShiftRoleAssignment;
import seedu.address.model.worker.Unavailability;
import seedu.address.model.worker.Worker;
//import seedu.address.model.worker.Email;

//import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing worker in the address book.
 */
public class WorkerEditCommand extends Command {

    public static final String COMMAND_WORD = "worker-edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the worker identified "
            + "by the index number used in the displayed worker list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_PAY + "HOURLY PAY] "
            //+ "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            //+ "[" + PREFIX_TAG + "TAG]...\n"
            + "[" + PREFIX_ROLE + "ROLE]...\n"
            + "[" + PREFIX_UNAVAILABILITY + "UNAVAILABLE TIMINGS]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            //+ PREFIX_EMAIL + "johndoe@example.com";
            + PREFIX_PAY + "10.20";

    public static final String MESSAGE_EDIT_WORKER_SUCCESS = "Edited worker: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_WORKER = "This worker already exists in the address book.";

    private final Index index;
    private final EditWorkerDescriptor editWorkerDescriptor;

    /**
     * @param index of the worker in the filtered worker list to edit
     * @param editWorkerDescriptor details to edit the worker with
     */
    public WorkerEditCommand(Index index, EditWorkerDescriptor editWorkerDescriptor) {
        requireNonNull(index);
        requireNonNull(editWorkerDescriptor);

        this.index = index;
        this.editWorkerDescriptor = new EditWorkerDescriptor(editWorkerDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Worker> lastShownList = model.getFilteredWorkerList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_WORKER_DISPLAYED_INDEX);
        }

        Worker workerToEdit = lastShownList.get(index.getZeroBased());
        Worker editedWorker = createEditedWorker(workerToEdit, editWorkerDescriptor);

        if (!workerToEdit.isSameWorker(editedWorker) && model.hasWorker(editedWorker)) {
            throw new CommandException(MESSAGE_DUPLICATE_WORKER);
        }

        model.setWorker(workerToEdit, editedWorker);
        editWorkerInAssignedShifts(model, workerToEdit, editedWorker);
        model.updateFilteredWorkerList(PREDICATE_SHOW_ALL_WORKERS);

        return new CommandResult(String.format(MESSAGE_EDIT_WORKER_SUCCESS, editedWorker));
    }

    /**
     * Creates and returns a {@code Worker} with the details of {@code workerToEdit}
     * edited with {@code editWorkerDescriptor}.
     */
    private static Worker createEditedWorker(Worker workerToEdit, EditWorkerDescriptor editWorkerDescriptor) {
        assert workerToEdit != null;

        Name updatedName = editWorkerDescriptor.getName().orElse(workerToEdit.getName());
        Phone updatedPhone = editWorkerDescriptor.getPhone().orElse(workerToEdit.getPhone());
        Pay updatedPay = editWorkerDescriptor.getPay().orElse(workerToEdit.getPay());
        //Email updatedEmail = editWorkerDescriptor.getEmail().orElse(workerToEdit.getEmail());
        Address updatedAddress = editWorkerDescriptor.getAddress().orElse(workerToEdit.getAddress());
        Set<Role> updatedRoles = editWorkerDescriptor.getRoles().orElse(workerToEdit.getRoles());
        Set<Unavailability> updatedUnavailabilities = editWorkerDescriptor.getUnavailableTimings()
                .orElse(workerToEdit.getUnavailableTimings());

        return new Worker(updatedName, updatedPhone, updatedPay, updatedAddress, updatedRoles, updatedUnavailabilities,
                workerToEdit.getShiftRoleAssignments());
    }


    private void editWorkerInAssignedShifts(Model model, Worker workerToEdit, Worker editedWorker) {
        requireAllNonNull(model, workerToEdit, editedWorker);
        List<Shift> fullShiftList = model.getFullShiftList();

        Stream<Shift> assignedShifts = workerToEdit.getShiftRoleAssignments()
                .stream()
                .map(ShiftRoleAssignment::getShift);

        assignedShifts.forEach(assignedShift -> {
            for (Shift shift : fullShiftList) {
                if (assignedShift.isSameShift(shift)) {
                    editWorkerInShift(model, shift, workerToEdit, editedWorker);
                    break;
                }
            }
        });
    }

    private void editWorkerInShift(Model model, Shift shift, Worker workerToEdit, Worker editedWorker) {
        Set<WorkerRoleAssignment> editedAssignments = createEditedWorkerRoleAssignments(shift, workerToEdit,
                editedWorker);
        Shift editedShift = new Shift(shift.getShiftDay(), shift.getShiftTime(),
                shift.getRoleRequirements(), editedAssignments);
        model.setShift(shift, editedShift);
    }

    private Set<WorkerRoleAssignment> createEditedWorkerRoleAssignments(Shift shift, Worker workerToEdit,
            Worker editedWorker) {
        Set<WorkerRoleAssignment> workerRoleAssignments = new HashSet<>(shift.getWorkerRoleAssignments());
        for (WorkerRoleAssignment assignment : workerRoleAssignments) {
            if (workerToEdit.isSameWorker(assignment.getWorker())) {
                WorkerRoleAssignment editedAssignment = new WorkerRoleAssignment(editedWorker, assignment.getRole());
                workerRoleAssignments.add(editedAssignment);
                workerRoleAssignments.remove(assignment);
            }
        }

        return workerRoleAssignments;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof WorkerEditCommand)) {
            return false;
        }

        // state check
        WorkerEditCommand e = (WorkerEditCommand) other;
        return index.equals(e.index)
                && editWorkerDescriptor.equals(e.editWorkerDescriptor);
    }

    /**
     * Stores the details to edit the worker with. Each non-empty field value will replace the
     * corresponding field value of the worker.
     */
    public static class EditWorkerDescriptor {
        private Name name;
        private Phone phone;
        private Pay pay;
        //private Email email;
        private Address address;
        private Set<Role> roles;
        private Set<Unavailability> unavailableTimings;

        public EditWorkerDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code roles} is used internally.
         */
        public EditWorkerDescriptor(EditWorkerDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setPay(toCopy.pay);
            //setEmail(toCopy.email);
            setAddress(toCopy.address);
            setRoles(toCopy.roles);
            setUnavailableTimings(toCopy.unavailableTimings);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, pay, address, roles,
                    unavailableTimings);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setPay(Pay pay) {
            this.pay = pay;
        }

        public Optional<Pay> getPay() {
            return Optional.ofNullable(pay);
        }

        /*
        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }
         */

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        /**
         * Sets {@code roles} to this object's {@code roles}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setRoles(Set<Role> roles) {
            this.roles = (roles != null) ? new HashSet<>(roles) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code roles} is null.
         */
        public Optional<Set<Role>> getRoles() {
            return (roles != null) ? Optional.of(Collections.unmodifiableSet(roles)) : Optional.empty();
        }

        public void setUnavailableTimings(Set<Unavailability> unavailableTimings) {
            this.unavailableTimings = (unavailableTimings != null) ? new HashSet<>(unavailableTimings) : null;
        }

        public Optional<Set<Unavailability>> getUnavailableTimings() {
            return (unavailableTimings != null)
                    ? Optional.of(Collections.unmodifiableSet(unavailableTimings)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditWorkerDescriptor)) {
                return false;
            }

            // state check
            EditWorkerDescriptor e = (EditWorkerDescriptor) other;

            return getName().equals(e.getName())
                    && getPhone().equals(e.getPhone())
                    && getPay().equals(e.getPay())
                    //&& getEmail().equals(e.getEmail())
                    && getAddress().equals(e.getAddress())
                    && getRoles().equals(e.getRoles())
                    && getUnavailableTimings().equals(e.getUnavailableTimings());
        }

    }
}
