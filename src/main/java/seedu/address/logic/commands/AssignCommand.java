package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SHIFT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WORKER;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.shift.Shift;
import seedu.address.model.shift.ShiftTime;
import seedu.address.model.shift.WorkerRoleAssignment;
import seedu.address.model.tag.Role;
import seedu.address.model.worker.ShiftRoleAssignment;
import seedu.address.model.worker.Unavailability;
import seedu.address.model.worker.Worker;

/**
 * Assigns a worker to a shift.
 */
public class AssignCommand extends Command {
    public static final String COMMAND_WORD = "assign";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Assigns the specified worker to the specified shift"
            + "by the index numbers used in the last worker and shift listings. "
            + "\nParameters: "
            + PREFIX_SHIFT + "SHIFT_INDEX (must be a positive integer) "
            + PREFIX_WORKER + "WORKER_INDEX (must be a positive integer) "
            + PREFIX_ROLE + "ROLE\n"
            + "Example: " + COMMAND_WORD
            + " s/4 "
            + "w/1 "
            + "r/Cashier";

    public static final String MESSAGE_ASSIGN_SUCCESS = "New shift assignment added:\n"
            + "Shift: %1$s, Worker: %2$s, Role: %3$s";

    private final Index shiftIndex;
    private final Index workerIndex;
    private final Role role;

    /**
     * Creates an AssignCommand to assign the specified {@code Worker} to the specified {@code Shift}.
     *
     * @param shiftIndex  of the shift in the filtered shift list to assign the worker to.
     * @param workerIndex of the worker in the filtered worker list to assign to the shift.
     * @param role        of the worker in the shift.
     */
    public AssignCommand(Index shiftIndex, Index workerIndex, Role role) {
        requireAllNonNull(shiftIndex, workerIndex, role);

        this.shiftIndex = shiftIndex;
        this.workerIndex = workerIndex;
        this.role = role;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Worker> lastShownWorkerList = model.getFilteredWorkerList();
        List<Shift> lastShownShiftList = model.getFilteredShiftList();

        if (shiftIndex.getZeroBased() >= lastShownShiftList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
        }
        if (workerIndex.getZeroBased() >= lastShownWorkerList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_WORKER_DISPLAYED_INDEX);
        }

        Worker workerToAssign = lastShownWorkerList.get(workerIndex.getZeroBased());
        Shift shiftToAssign = lastShownShiftList.get(shiftIndex.getZeroBased());

        if (isWorkerUnavailable(workerToAssign, shiftToAssign)) {
            throw new CommandException(Messages.MESSAGE_INVALID_ASSIGNMENT);
        }

        Worker assignedWorker = createAssignedWorker(workerToAssign, shiftToAssign, role);
        Shift assignedShift = createAssignedShift(shiftToAssign, workerToAssign, role);

        model.setWorker(workerToAssign, assignedWorker);
        model.setShift(shiftToAssign, assignedShift);

        return new CommandResult(String.format(MESSAGE_ASSIGN_SUCCESS, assignedShift.toCondensedString(),
                assignedWorker.getName(), role.getRole()));
    }

    private static boolean isWorkerUnavailable(Worker workerToAssign, Shift shiftToAssign) {
        Set<Unavailability> workerUnavailableTimings = workerToAssign.getUnavailableTimings();
        for (Unavailability unavailability : workerUnavailableTimings) {
            boolean hasSameDay = unavailability.getDay().equals(shiftToAssign.getShiftDay());
            boolean hasSameTime = unavailability.getTime().equals(shiftToAssign.getShiftTime());
            boolean isUnavailableWholeDay = unavailability.getTime().equals(new ShiftTime("FULL"));
            boolean isFullDayShift = shiftToAssign.getShiftTime().equals(new ShiftTime("FULL"));

            if ((hasSameDay && hasSameTime)
                    || (hasSameDay && isUnavailableWholeDay)
                    || (hasSameDay && isFullDayShift)) {
                return true;
            }
        }
        return false;
    }
    /**
     * Creates and returns a {@code Worker} with the {@code shiftToAssign} and {@code role} assigned.
     */
    private static Worker createAssignedWorker(Worker workerToAssign, Shift shiftToAssign, Role role) {
        assert workerToAssign != null;

        Set<ShiftRoleAssignment> updatedShiftRoleAssignments = new HashSet<>(
                workerToAssign.getShiftRoleAssignments());
        updatedShiftRoleAssignments.removeIf(assignment -> assignment.getShift().isSameShift(shiftToAssign));

        ShiftRoleAssignment shiftRoleToAssign = new ShiftRoleAssignment(shiftToAssign, role);
        updatedShiftRoleAssignments.add(shiftRoleToAssign);

        return new Worker(workerToAssign.getName(), workerToAssign.getPhone(), workerToAssign.getPay(),
                workerToAssign.getAddress(), workerToAssign.getRoles(), workerToAssign.getUnavailableTimings(),
                updatedShiftRoleAssignments);
    }

    /**
     * Creates and returns a {@code Shift} with the {@code workerToAssign} and {@code role} assigned.
     */
    private static Shift createAssignedShift(Shift shiftToAssign, Worker workerToAssign, Role role) {
        assert shiftToAssign != null;

        Set<WorkerRoleAssignment> updatedWorkerRoleAssignments = new HashSet<>(
                shiftToAssign.getWorkerRoleAssignments());
        updatedWorkerRoleAssignments.removeIf(assignment -> assignment.getWorker().isSameWorker(workerToAssign));

        WorkerRoleAssignment workerRoleToAssign = new WorkerRoleAssignment(workerToAssign, role);
        updatedWorkerRoleAssignments.add(workerRoleToAssign);

        return new Shift(shiftToAssign.getShiftDay(), shiftToAssign.getShiftTime(),
                shiftToAssign.getRoleRequirements(), updatedWorkerRoleAssignments);
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
                && workerIndex.equals(e.workerIndex)
                && role.equals(e.role);
    }
}
