package mcscheduler.commons.core;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_WORKER_DISPLAYED_INDEX = "The worker index provided is invalid: [%1$d]";
    public static final String MESSAGE_WORKERS_LISTED_OVERVIEW = "%1$d workers listed!";
    public static final String MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX = "The shift index provided is invalid: [%1$d]";
    public static final String MESSAGE_INVALID_ROLE_DISPLAYED_INDEX = "The role index provided is invalid: [%1$d]";
    public static final String MESSAGE_ROLE_NOT_FOUND = "This role does not exist in the McScheduler: [%1$s]";
    public static final String MESSAGE_INVALID_ASSIGNMENT_UNAVAILABLE = "[%1$s] is not available for [%2$s]";
    public static final String MESSAGE_INVALID_ASSIGNMENT_WORKER_ROLE = "[%1$s] is not fit for the role [%2$s]";
    public static final String MESSAGE_INVALID_ASSIGNMENT = "The worker is not available for this shift";
    public static final String MESSAGE_NO_ASSIGNMENT_FOUND =
            "No assignment found between worker: %1$s and shift: %2$s";
    public static final String MESSAGE_NO_LEAVE_FOUND =
            "No leave found between worker: %1$s and shift: %2$s";
    public static final String MESSAGE_INVALID_ASSIGNMENT_NOT_REQUIRED = "The role is not required for this shift";


}
