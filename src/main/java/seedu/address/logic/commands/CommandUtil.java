package seedu.address.logic.commands;

import java.util.Set;

import seedu.address.model.shift.Shift;
import seedu.address.model.worker.Unavailability;
import seedu.address.model.worker.Worker;

/**
 * Contains utility methods for Commands.
 */
public class CommandUtil {

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

}
