package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SHIFT_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SHIFT_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WORKER;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.shift.Shift;
import seedu.address.model.shift.ShiftDay;
import seedu.address.model.shift.ShiftTime;
import seedu.address.model.worker.Worker;

public class MassCancelLeaveCommand extends Command {

    public static final String COMMAND_WORD = "mass-cancel-leave";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Cancels leave for the specified worker during "
            + "all shifts between the two specified date times by the index number used in the last worker listings."
            + "\nParameters: "
            + PREFIX_WORKER + "WORKER_INDEX (must be a positive integer) "
            + PREFIX_SHIFT_DAY + "START_DAY (must be Mon, Tue, Wed, Thu, Fri, Sat or Sun) "
            + PREFIX_SHIFT_TIME + "START_TIME (must be AM or PM) "
            + PREFIX_SHIFT_DAY + "END_DAY "
            + PREFIX_SHIFT_TIME + "END_TIME\n"
            + "Example: " + COMMAND_WORD
            + " w/2 d/Mon t/PM d/Wed t/AM ";

    public static final String MESSAGE_MASS_CANCEL_LEAVE_SUCCESS = "Leave cancelled from shift {%1$s} to shift {%2$s}.";
    public static final String MESSAGE_NO_LEAVE_FOUND = "No leave found between shift {%1$s} to {%2$s}.";

    private final Index workerIndex;
    private final ShiftDay startDay;
    private final ShiftTime startTime;
    private final ShiftDay endDay;
    private final ShiftTime endTime;

    /**
     * Creates a MassCancelLeaveCommand to cancel all leaves of the specified {@code Worker} between the specified
     * dates and times.
     *
     * @param workerIndex of the worker in the filtered worker list.
     * @param startDay of the time period to search for leaves to cancel.
     * @param startTime of the time period to search for leaves to cancel.
     * @param endDay of the time period to search for leaves to cancel.
     * @param endTime of the time period to search for leaves to cancel.
     */
    public MassCancelLeaveCommand(Index workerIndex, ShiftDay startDay, ShiftTime startTime,
                                  ShiftDay endDay, ShiftTime endTime) {
        requireAllNonNull(workerIndex, startDay, startTime, endDay, endTime);

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
            throw new CommandException(Messages.MESSAGE_INVALID_WORKER_DISPLAYED_INDEX);
        }
        Worker worker = lastShownWorkerList.get(workerIndex.getZeroBased());

        List<Shift> shiftsInDayTimeRange =
                CommandUtil.generateShiftsInDayTimeRange(startDay, startTime, endDay, endTime);

        List<Assignment> assignmentsToRemove = shiftsInDayTimeRange
                .stream()
                .map(shift -> new Assignment(shift, worker))
                .filter(assignment -> CommandUtil.hasLeaveAssignment(model, assignment))
                .map(assignment -> model.getAssignment(assignment).get())
                .collect(Collectors.toList());

        Shift startShift = new Shift(startDay, startTime, Collections.emptySet());
        Shift endShift = new Shift(endDay, endTime, Collections.emptySet());


        if (assignmentsToRemove.size() == 0) {
            throw new CommandException(String.format(MESSAGE_NO_LEAVE_FOUND, startShift, endShift));
        }

        assignmentsToRemove.forEach(model::deleteAssignment);

        return new CommandResult(String.format(MESSAGE_MASS_CANCEL_LEAVE_SUCCESS, startShift, endShift));

    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof MassCancelLeaveCommand)) {
            return false;
        }

        MassCancelLeaveCommand c = (MassCancelLeaveCommand) other;
        return this.workerIndex == c.workerIndex
                && this.startDay == c.startDay
                && this.startTime == c.startTime
                && this.endDay == c.endDay
                && this.endTime == c.endTime;
    }
}
