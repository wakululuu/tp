package mcscheduler.logic;

import java.nio.file.Path;

import javafx.collections.ObservableList;
import mcscheduler.commons.core.GuiSettings;
import mcscheduler.logic.commands.CommandResult;
import mcscheduler.logic.commands.exceptions.CommandException;
import mcscheduler.logic.parser.exceptions.ParseException;
import mcscheduler.model.Model;
import mcscheduler.model.ReadOnlyMcScheduler;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.role.Role;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.worker.Worker;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /**
     * Returns the McScheduler.
     *
     * @see Model#getMcScheduler()
     */
    ReadOnlyMcScheduler getMcScheduler();

    /** Returns an unmodifiable view of the filtered list of workers */
    ObservableList<Worker> getFilteredWorkerList();

    /** Returns an unmodifiable view of the filtered list of shifts */
    ObservableList<Shift> getFilteredShiftList();

    /** Returns an unmodifiable view of the filtered list of roles */
    ObservableList<Role> getFilteredRoleList();

    /** Returns an unmodifiable view of the full list of assigments */
    ObservableList<Assignment> getFullAssignmentList();

    /**
     * Returns the user prefs' McScheduler file path.
     */
    Path getMcSchedulerFilePath();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Set the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);
}
