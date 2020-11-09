package mcscheduler.logic.commands;

import static java.util.Objects.requireNonNull;
import static mcscheduler.logic.commands.CommandUtil.generateShiftsInDayTimeRange;
import static mcscheduler.logic.commands.CommandUtil.hasLeaveAssignment;
import static mcscheduler.logic.commands.CommandUtil.hasNonLeaveAssignment;
import static mcscheduler.logic.commands.CommandUtil.isWorkerUnavailable;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_SHIFT_DAY;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_SHIFT_TIME;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_WORKER;

import java.util.ArrayList;
import java.util.List;

import mcscheduler.commons.core.index.Index;
import mcscheduler.commons.util.CollectionUtil;
import mcscheduler.logic.commands.exceptions.CommandException;
import mcscheduler.model.Model;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.role.Leave;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.shift.ShiftDay;
import mcscheduler.model.shift.ShiftTime;
import mcscheduler.model.worker.Worker;

/**
 * Assign a worker to take leave from a given day/time to another day/time
 */
public class MassTakeLeaveCommand extends Command {

    public static final String COMMAND_WORD = "mass-take-leave";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Assigns the specified worker to take leave "
            + "during all shifts between the two specified date times by the index number used in the last "
            + "worker listings.\nParameters: "
            + PREFIX_WORKER + "WORKER_INDEX (must be a positive integer) "
            + PREFIX_SHIFT_DAY + "START_DAY (must be Mon, Tue, Wed, Thu, Fri, Sat or Sun) "
            + PREFIX_SHIFT_TIME + "START_TIME (must be AM or PM) "
            + PREFIX_SHIFT_DAY + "END_DAY "
            + PREFIX_SHIFT_TIME + "END_TIME\n"
            + "Example: " + COMMAND_WORD
            + " w/2 d/Mon t/PM d/Wed t/AM ";

    public static final String MESSAGE_MASS_TAKE_LEAVE_SUCCESS = "Leave added for %5$s from %1$s %2$s to %3$s %4$s. ";
    public static final String MESSAGE_REASSIGNED = "Reassignments made: %1$s";

    private final Index workerIndex;
    private final ShiftDay startDay;
    private final ShiftTime startTime;
    private final ShiftDay endDay;
    private final ShiftTime endTime;

    /**
     * Creates a MassTakeLeaveCommand to add leave for the specified {@code Worker} between the given dates and times.
     *
     * @param workerIndex of the worker in the filtered worker list.
     * @param startDay of the leave.
     * @param startTime of the leave.
     * @param endDay of the leave.
     * @param endTime of the leave.
     */
    public MassTakeLeaveCommand(Index workerIndex, ShiftDay startDay, ShiftTime startTime,
                                ShiftDay endDay, ShiftTime endTime) {
        CollectionUtil.requireAllNonNull(workerIndex, startDay, startTime, endDay, endTime);

        this.workerIndex = workerIndex;
        this.startDay = startDay;
        this.startTime = startTime;
        this.endDay = endDay;
        this.endTime = endTime;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Worker> lastShownWorkerList = model.getFilteredWorkerList();

        if (workerIndex.getZeroBased() >= lastShownWorkerList.size()) {
            throw new CommandException(
                    CommandUtil.printOutOfBoundsWorkerIndexError(workerIndex, MESSAGE_USAGE));
        }
        Worker worker = lastShownWorkerList.get(workerIndex.getZeroBased());

        List<Shift> shiftsToTakeLeaveFrom = generateShiftsInDayTimeRange(startDay, startTime, endDay, endTime);

        List<Assignment> reassignedAssignments = new ArrayList<>();
        ArrayList<Shift> shiftsAlreadyWithLeave = new ArrayList<>();
        for (Shift shift: shiftsToTakeLeaveFrom) {
            Assignment toAdd = new Assignment(shift, worker, new Leave());
            if (hasNonLeaveAssignment(model, toAdd)) {
                Assignment nonLeaveAssignmentInModel = model.getAssignment(toAdd).get();
                model.deleteAssignment(nonLeaveAssignmentInModel);
                model.addAssignment(toAdd);
                reassignedAssignments.add(nonLeaveAssignmentInModel);
            } else if (hasLeaveAssignment(model, toAdd) || isWorkerUnavailable(worker, shift)) {
                shiftsAlreadyWithLeave.add(shift);
            } else {
                if (!model.hasShift(shift)) {
                    model.addShift(shift);
                }
                model.addAssignment(toAdd);
            }
        }

        String resultMessage = String.format(MESSAGE_MASS_TAKE_LEAVE_SUCCESS,
                startDay, startTime, endDay, endTime, worker.getName());
        if (reassignedAssignments.size() > 0) {
            String reassignedMessage = reassignedAssignments
                    .stream()
                    .map(Assignment::toString)
                    .reduce("", (str1, str2) -> str1 + "\n" + str2);
            resultMessage += "\n" + String.format(MESSAGE_REASSIGNED, reassignedMessage);
        }

        return new CommandResult(resultMessage);


    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof MassTakeLeaveCommand)) {
            return false;
        }

        MassTakeLeaveCommand c = (MassTakeLeaveCommand) other;
        return this.workerIndex.equals(c.workerIndex)
                && this.startDay.equals(c.startDay)
                && this.startTime.equals(c.startTime)
                && this.endDay.equals(c.endDay)
                && this.endTime.equals(c.endTime);
    }


}
