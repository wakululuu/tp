package mcscheduler.logic.commands;

import static java.util.Objects.requireNonNull;

import mcscheduler.model.McScheduler;
import mcscheduler.model.Model;

/**
 * Clears the McScheduler.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "The McScheduler has been cleared!";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setMcScheduler(new McScheduler());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
