package mcscheduler.model.shift.exceptions;

/**
 * Signals that the operation will result in duplicate Shifts (Shifts are considered duplicates if they have the same
 * identity).
 */
public class DuplicateShiftException extends RuntimeException {
    public DuplicateShiftException() {
        super("Operation would result in duplicate shifts");
    }
}

