package mcscheduler.commons.core;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format\n%1$s";
    public static final String MESSAGE_UNEXPECTED_ARGUMENT = "Unexpected argument for command \"%1$s\":\n%2$s";

    public static final String MESSAGE_INVALID_PARSE_VALUE = "The %1$s provided is invalid: '%2$s'.\n%3$s";

    public static final String MESSAGE_INVALID_DISPLAYED_INDEX = "The index provided is invalid: %1$s\n";
    public static final String MESSAGE_INVALID_WORKER_DISPLAYED_INDEX =
            "The worker index provided is invalid: %1$d (out of bounds)\n";
    public static final String MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX =
            "The shift index provided is invalid: %1$d (out of bounds)\n";
    public static final String MESSAGE_INVALID_ROLE_DISPLAYED_INDEX =
            "The role index provided is invalid: %1$d (out of bounds)\n";

    public static final String MESSAGE_WORKERS_LISTED_OVERVIEW = "%1$d workers listed!";
    public static final String MESSAGE_SHIFTS_LISTED_OVERVIEW = "%1$d shifts listed!";
    public static final String MESSAGE_ROLE_NOT_FOUND = "This role does not exist in the McScheduler: %1$s";
    public static final String MESSAGE_DO_NOT_MODIFY_LEAVE =
            "Leave is a system default and should not be added as a role.";
    public static final String MESSAGE_DO_NOT_PARSE_LEAVE_ASSIGN =
            "Use the take-leave command for assigning or reassigning leaves.";
    public static final String MESSAGE_DO_NOT_UNASSIGN_LEAVE =
            "Use the cancel-leave command for cancelling leaves.";
    public static final String MESSAGE_INVALID_ASSIGNMENT_UNAVAILABLE = "%1$s is not available on %2$s %3$s and "
            + "cannot be assigned a role or leave";
    public static final String MESSAGE_ROLE_NOT_EDITED = "The role has not been edited: %1$s";
    public static final String MESSAGE_DUPLICATE_ROLE = "This role already exists in the McScheduler";
    public static final String MESSAGE_INVALID_ASSIGNMENT_WORKER_ROLE = "%1$s is not fit for the %2$s role";
    public static final String MESSAGE_NO_ASSIGNMENT_FOUND =
            "No assignment found between worker: %1$s and shift: %2$s";
    public static final String MESSAGE_NO_LEAVE_FOUND =
            "No leave found between worker: %1$s and shift: %2$s";
    public static final String MESSAGE_INVALID_ASSIGNMENT_NOT_REQUIRED =
            "The %1$s role is not required or is filled for shift:%2$s";

    public static final String MESSAGE_UNABLE_TO_LOAD_SAVE =
            "Unable to load save file - it is not of the correct format."
            + "\nIf you wish to fix the file, do that before exiting the McScheduler."
            + "\nYou can also exit the app and delete the save file to allow the "
            + "system to generate sample data for your use."
            + "\nShould you wish to continue from a blank save file, "
            + "you can continue to input commands from the current state and the corrupted file will be saved over.";


}
