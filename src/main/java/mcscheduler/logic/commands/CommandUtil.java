package mcscheduler.logic.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import mcscheduler.commons.core.Messages;
import mcscheduler.commons.core.index.Index;
import mcscheduler.model.Model;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.role.Leave;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.shift.ShiftDay;
import mcscheduler.model.shift.ShiftTime;
import mcscheduler.model.worker.Unavailability;
import mcscheduler.model.worker.Worker;

/**
 * Contains utility methods for Commands.
 */
public class CommandUtil {

    /**
     * Checks if a worker is unavailable at a given shift.
     */
    public static boolean isWorkerUnavailable(Worker workerToAssign, Shift shiftToAssign) {
        Set<Unavailability> workerUnavailableTimings = workerToAssign.getUnavailableTimings();
        for (Unavailability unavailability : workerUnavailableTimings) {
            boolean hasSameDay = unavailability.getDay().equals(shiftToAssign.getShiftDay());
            boolean hasSameTime = unavailability.getTime().equals(shiftToAssign.getShiftTime());
            if (hasSameDay && hasSameTime) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if model contains an assignment with same identity as {@code toCheck} that is not a leave.
     */
    public static boolean hasNonLeaveAssignment(Model model, Assignment toCheck) {
        return model.hasAssignment(toCheck)
                && !Leave.isLeave(model.getAssignment(toCheck).get().getRole());
    }

    /**
     * Check if model contains a leave with same identity as {@code toCheck}.
     */
    public static boolean hasLeaveAssignment(Model model, Assignment toCheck) {
        return model.hasAssignment(toCheck)
                && Leave.isLeave(model.getAssignment(toCheck).get().getRole());
    }

    /**
     * Create a list of shifts between the given dates and times (inclusive).
     * The list generated will loop properly between the last {@code ShiftDay} back to the first.
     */
    public static List<Shift> generateShiftsInDayTimeRange(ShiftDay startDay, ShiftTime startTime,
                                                           ShiftDay endDay, ShiftTime endTime) {
        ArrayList<Shift> shiftsToTakeLeaveFrom = new ArrayList<>();
        ArrayList<ShiftDay> days = ShiftDay.getAllDays();
        ArrayList<ShiftTime> times = ShiftTime.getAllTimes();

        int index = days.indexOf(startDay) * 2 + times.indexOf(startTime);
        while (index < days.size() * times.size() * 3) { // prevents infinite loop just in case
            ShiftDay day = days.get((index / times.size()) % days.size());
            ShiftTime time = times.get(index % times.size());
            shiftsToTakeLeaveFrom.add(new Shift(day, time, Collections.emptySet()));
            index++;
            if (day.equals(endDay) && time.equals(endTime)) {
                break;
            }
        }

        return shiftsToTakeLeaveFrom;
    }

    /**
     * Prints the corresponding OutOfBoundsRoleIndexError for a given command
     * @param roleIndex Out of bounds role index
     * @param messageUsage Usage message for a given command
     * @return OutOfBoundsRoleIndexError String
     */
    public static String printOutOfBoundsRoleIndexError(Index roleIndex, String messageUsage) {
        return String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                String.format(Messages.MESSAGE_INVALID_ROLE_DISPLAYED_INDEX, roleIndex.getOneBased())
                        + messageUsage);
    }

    /**
     * Prints the corresponding OutOfBoundsWorkerIndexError for a given command
     * @param workerIndex Out of bounds worker index
     * @param messageUsage Usage message for a given command
     * @return OutOfBoundsWorkerIndexError String
     */
    public static String printOutOfBoundsWorkerIndexError(Index workerIndex, String messageUsage) {
        return String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                String.format(Messages.MESSAGE_INVALID_WORKER_DISPLAYED_INDEX, workerIndex.getOneBased())
                        + messageUsage);
    }

    /**
     * Prints the corresponding OutOfBoundsShiftIndexError for a given command
     * @param shiftIndex Out of bounds shift index
     * @param messageUsage Usage message for a given command
     * @return OutOfBoundsShiftIndexError String
     */
    public static String printOutOfBoundsShiftIndexError(Index shiftIndex, String messageUsage) {
        return String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                String.format(Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX, shiftIndex.getOneBased())
                        + messageUsage);
    }

}
