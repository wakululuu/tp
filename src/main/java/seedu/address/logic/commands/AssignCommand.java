package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SHIFT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WORKER;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_SHIFTS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_WORKERS;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.shift.Shift;
import seedu.address.model.tag.Role;
import seedu.address.model.worker.Worker;

/**
 * Adds a shift, worker and shift assignment to the McScheduler.
 */
public class AssignCommand extends Command {
    public static final String COMMAND_WORD = "assign";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a shift, worker and role assignment to the "
            + "McScheduler by the index numbers used in the last worker and shift listings. "
            + "\nParameters: "
            + PREFIX_SHIFT + "SHIFT_INDEX (must be a positive integer) "
            + PREFIX_WORKER + "WORKER_INDEX (must be a positive integer) "
            + PREFIX_ROLE + "ROLE\n"
            + "Example: " + COMMAND_WORD
            + " s/4 "
            + "w/1 "
            + "r/Cashier";

    public static final String MESSAGE_ASSIGN_SUCCESS = "New assignment added:\n%1$s";
    public static final String MESSAGE_DUPLICATE_ASSIGNMENT = "This assignment already exists in the McScheduler";

    private final Index shiftIndex;
    private final Index workerIndex;
    private final Role role;

    /**
     * Creates an AssignCommand to add an assignment of the specified {@code Shift}, {@code Worker} and {@code Role}.
     *
     * @param shiftIndex  of the shift in the filtered shift list.
     * @param workerIndex of the worker in the filtered worker list.
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
        requireNonNull(model);

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
        Assignment assignmentToAdd = new Assignment(shiftToAssign, workerToAssign, role);

        if (model.hasAssignment(assignmentToAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_ASSIGNMENT);
        }

        model.addAssignment(assignmentToAdd);
        model.updateFilteredShiftList(PREDICATE_SHOW_ALL_SHIFTS);
        model.updateFilteredWorkerList(PREDICATE_SHOW_ALL_WORKERS);

        return new CommandResult(String.format(MESSAGE_ASSIGN_SUCCESS, assignmentToAdd));
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
