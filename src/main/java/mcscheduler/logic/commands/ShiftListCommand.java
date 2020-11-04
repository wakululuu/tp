package mcscheduler.logic.commands;

import static java.util.Objects.requireNonNull;

import mcscheduler.model.Model;

/**
 * Lists all shifts in the McScheduler to the user.
 */
public class ShiftListCommand extends Command {

    public static final String COMMAND_WORD = "shift-list";

    public static final String MESSAGE_SUCCESS = "Listed all shifts";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
