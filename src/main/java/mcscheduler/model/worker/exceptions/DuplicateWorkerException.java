package mcscheduler.model.worker.exceptions;

/**
 * Signals that the operation will result in duplicate Workers (Workers are considered duplicates if they have the same
 * identity).
 */
public class DuplicateWorkerException extends RuntimeException {
    public DuplicateWorkerException() {
        super("Operation would result in duplicate workers");
    }
}
