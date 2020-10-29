package mcscheduler.model.assignment.exceptions;

/**
 * Signals that the operation will result in duplicate assignments (Assignments are considered duplicates if they have
 * the same identity).
 */
public class DuplicateAssignmentException extends RuntimeException {
    public DuplicateAssignmentException() {
        super("Operation would result in duplicate assignments");
    }
}
