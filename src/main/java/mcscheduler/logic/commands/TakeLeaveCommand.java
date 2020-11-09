package mcscheduler.logic.commands;

import static java.util.Objects.requireNonNull;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_SHIFT;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_WORKER;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mcscheduler.commons.core.index.Index;
import mcscheduler.commons.util.CollectionUtil;
import mcscheduler.logic.commands.exceptions.CommandException;
import mcscheduler.model.Model;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.assignment.WorkerRolePair;
import mcscheduler.model.role.Leave;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.worker.Worker;

/**
 * Assigns a worker to take leave for a particular shift.
 */
public class TakeLeaveCommand extends Command {

    public static final String COMMAND_WORD = "take-leave";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Assigns the specified worker(s) to take leave "
            + "during the specified shift by the index numbers used in the last worker and shift listings. "
            + "\nParameters: "
            + PREFIX_SHIFT + "SHIFT_INDEX (must be a positive integer) "
            + PREFIX_WORKER + "WORKER_INDEX (must be a positive integer)...\n"
            + "Example: " + COMMAND_WORD
            + " s/4 "
            + "w/1 "
            + "w/3";

    public static final String MESSAGE_TAKE_LEAVE_SUCCESS_PREFIX = "[Leave taken] ";
    public static final String MESSAGE_WORKER_ALREADY_ON_LEAVE =
            "%1$s ( w/%2$d ) is already on leave. Please remove %1$s from the \"take-leave\" command";

    private final Index shiftIndex;
    private final Set<Index> workerIndexes;

    /**
     * Creates a TakeLeaveCommand to assign
     * @param shiftIndex of the shift in the filtered shift list to assign the leave to.
     * @param workerIndexes of the worker(s) in the filtered worker list who are taking leave.
     */
    public TakeLeaveCommand(Index shiftIndex, Set<Index> workerIndexes) {
        CollectionUtil.requireAllNonNull(shiftIndex, workerIndexes);

        this.shiftIndex = shiftIndex;
        this.workerIndexes = workerIndexes;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<WorkerRolePair> workerToTakeLeavePairs = new ArrayList<>();
        List<Index> workerToReplaceAssignmentIndexes = new ArrayList<>();
        separateWorkerIndexes(workerToTakeLeavePairs, workerToReplaceAssignmentIndexes, model);

        checkWorkerAlreadyOnLeave(model, shiftIndex);

        CommandResult assignCommandResult = new CommandResult("");
        if (!workerToTakeLeavePairs.isEmpty()) {
            assignCommandResult = new AssignCommand(shiftIndex, new HashSet<>(workerToTakeLeavePairs))
                    .execute(model);
        }
        List<String> reassignCommandResultMessages = new ArrayList<>();
        for (Index workerIndex : workerToReplaceAssignmentIndexes) {
            reassignCommandResultMessages.add(new ReassignCommand(
                    workerIndex, workerIndex, shiftIndex, shiftIndex, new Leave()).execute(model).getFeedbackToUser());
        }
        String compiledReassignMessages = reassignCommandResultMessages
                .stream()
                .reduce("", (str1, str2) -> str1 + "\n" + str2);

        return new CommandResult(MESSAGE_TAKE_LEAVE_SUCCESS_PREFIX
                + assignCommandResult.getFeedbackToUser()
                + compiledReassignMessages);
    }

    private void separateWorkerIndexes(List<WorkerRolePair> workerToTakeLeavePairs,
                                       List<Index> workerToReplaceAssignmentIndexes,
                                       Model model) throws CommandException {

        List<Worker> lastShownWorkerList = model.getFilteredWorkerList();
        List<Shift> lastShownShiftList = model.getFilteredShiftList();

        if (shiftIndex.getZeroBased() >= lastShownShiftList.size()) {
            throw new CommandException(
                    CommandUtil.printOutOfBoundsShiftIndexError(shiftIndex, MESSAGE_USAGE));
        }
        Shift shiftToTakeLeaveFrom = lastShownShiftList.get(shiftIndex.getZeroBased());

        for (Index workerIndex : workerIndexes) {
            if (workerIndex.getZeroBased() >= lastShownWorkerList.size()) {
                throw new CommandException(
                        CommandUtil.printOutOfBoundsWorkerIndexError(workerIndex, MESSAGE_USAGE));
            }
            Worker workerToTakeLeave = lastShownWorkerList.get(workerIndex.getZeroBased());
            Assignment assignment = new Assignment(shiftToTakeLeaveFrom, workerToTakeLeave);
            if (model.hasAssignment(assignment)) {
                workerToReplaceAssignmentIndexes.add(workerIndex);
            } else {
                workerToTakeLeavePairs.add(new WorkerRolePair(workerIndex, new Leave()));
            }
        }
    }

    private void checkWorkerAlreadyOnLeave(Model model, Index shiftIndex) throws CommandException {
        List<Worker> lastShownWorkerList = model.getFilteredWorkerList();
        List<Shift> lastShownShiftList = model.getFilteredShiftList();
        List<Assignment> assignmentList = model.getFullAssignmentList();

        for (Index workerIndex : workerIndexes) {
            Worker worker = lastShownWorkerList.get(workerIndex.getZeroBased());
            Shift shift = lastShownShiftList.get(shiftIndex.getZeroBased());
            for (Assignment assignment : assignmentList) {
                if (!assignment.getWorker().isSameWorker(worker) || !assignment.getShift().isSameShift(shift)) {
                    continue;
                }
                if (Leave.isLeave(assignment.getRole())) {
                    throw new CommandException(
                            String.format(MESSAGE_WORKER_ALREADY_ON_LEAVE, worker.getName(),
                                    workerIndex.getOneBased()));
                }
            }
        }
    }

    @Override
    public boolean equals(Object other) {
        // same object
        if (other == this) {
            return true;
        }

        // different type including null
        if (!(other instanceof TakeLeaveCommand)) {
            return false;
        }

        TakeLeaveCommand c = (TakeLeaveCommand) other;
        return shiftIndex.equals(c.shiftIndex)
                && workerIndexes.equals(c.workerIndexes);
    }

}
