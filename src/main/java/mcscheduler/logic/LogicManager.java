package mcscheduler.logic;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import mcscheduler.commons.core.GuiSettings;
import mcscheduler.commons.core.LogsCenter;
import mcscheduler.logic.commands.Command;
import mcscheduler.logic.commands.CommandResult;
import mcscheduler.logic.commands.exceptions.CommandException;
import mcscheduler.logic.parser.McSchedulerParser;
import mcscheduler.logic.parser.exceptions.ParseException;
import mcscheduler.model.Model;
import mcscheduler.model.ReadOnlyMcScheduler;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.role.Role;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.worker.Worker;
import mcscheduler.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_MESSAGE = "Could not save data to file: ";
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final McSchedulerParser mcSchedulerParser;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        mcSchedulerParser = new McSchedulerParser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult;
        Command command = mcSchedulerParser.parseCommand(commandText);
        commandResult = command.execute(model);

        try {
            storage.saveMcScheduler(model.getMcScheduler());
        } catch (IOException ioe) {
            throw new CommandException(FILE_OPS_ERROR_MESSAGE + ioe.getMessage(), ioe);
        }

        return commandResult;
    }

    @Override
    public ReadOnlyMcScheduler getMcScheduler() {
        return model.getMcScheduler();
    }

    @Override
    public ObservableList<Worker> getFilteredWorkerList() {
        return model.getFilteredWorkerList();
    }

    @Override
    public ObservableList<Shift> getFilteredShiftList() {
        return model.getFilteredShiftList();
    }

    @Override
    public ObservableList<Role> getFilteredRoleList() {
        return model.getFilteredRoleList();
    }

    @Override
    public ObservableList<Assignment> getFullAssignmentList() {
        return model.getFullAssignmentList();
    }

    @Override
    public Path getMcSchedulerFilePath() {
        return model.getMcSchedulerFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}
