package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.commands.CommandUtil.isWorkerUnavailable;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SHIFT_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SHIFT_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WORKER;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.shift.Shift;
import seedu.address.model.shift.ShiftDay;
import seedu.address.model.shift.ShiftTime;
import seedu.address.model.tag.Leave;
import seedu.address.model.worker.Worker;

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

    public static final String MESSAGE_MASS_TAKE_LEAVE_SUCCESS = "Leave added from shift {%1$s} to shift {%2$s}.";

    private final Index workerIndex;
    private final ShiftDay startDay;
    private final ShiftTime startTime;
    private final ShiftDay endDay;
    private final ShiftTime endTime;

    public MassTakeLeaveCommand(Index workerIndex, ShiftDay startDay, ShiftTime startTime,
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

        ArrayList<Shift> shiftsToTakeLeaveFrom = new ArrayList<>();
        ArrayList<ShiftDay> days = ShiftDay.getAllDays();
        ArrayList<ShiftTime> times = ShiftTime.getAllTimes();

        int index = days.indexOf(startDay) * 2 + times.indexOf(startTime);
        while(index < days.size() * times.size() * 3) { // prevents infinite loop just in case
            ShiftDay day = days.get((index / times.size()) % days.size());
            ShiftTime time = times.get(index % times.size());
            shiftsToTakeLeaveFrom.add(new Shift(day, time, Collections.emptySet()));
            index++;
            if (day.equals(endDay) && time.equals(endTime)) {
                break;
            }
        }

        StringBuilder errorMessageForShiftsWithOtherAssignments = new StringBuilder();
        ArrayList<Shift> shiftsAlreadyWithLeave = new ArrayList<>();
        for (Shift shift: shiftsToTakeLeaveFrom) {
            Assignment toCheck = new Assignment(shift, worker);
            if (hasNonLeaveAssignment(model, toCheck)) {
                errorMessageForShiftsWithOtherAssignments
                        .append(String.format(AssignCommand.MESSAGE_DUPLICATE_ASSIGNMENT + " {%1$s}", toCheck) + "\n");
            } else if (hasLeaveAssignment(model, toCheck) || isWorkerUnavailable(worker, shift)) {
                shiftsAlreadyWithLeave.add(shift);
            }
        }

        if (errorMessageForShiftsWithOtherAssignments.length() > 0) {
            throw new CommandException(errorMessageForShiftsWithOtherAssignments.toString());
        }
        shiftsToTakeLeaveFrom.removeAll(shiftsAlreadyWithLeave);

        for (Shift shift: shiftsToTakeLeaveFrom) {
            if (!model.hasShift(shift)) {
                model.addShift(shift);
            }
            model.addAssignment(new Assignment(shift, worker, new Leave()));
        }

        return new CommandResult(String.format(MESSAGE_MASS_TAKE_LEAVE_SUCCESS,
                new Shift(startDay, startTime, Collections.emptySet()),
                new Shift(endDay, endTime, Collections.emptySet())));

    }

    private boolean hasNonLeaveAssignment(Model model, Assignment toCheck) {
        return model.hasAssignment(toCheck)
                && !(model.getAssignment(toCheck).get().getRole().equals(new Leave()));
    }

    private boolean hasLeaveAssignment(Model model, Assignment toCheck) {
        return model.hasAssignment(toCheck)
                && model.getAssignment(toCheck).get().getRole().equals(new Leave());
    }
}
