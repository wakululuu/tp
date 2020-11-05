package mcscheduler.logic.commands;

import static java.util.Objects.requireNonNull;

import mcscheduler.commons.core.Messages;
import mcscheduler.model.Model;
import mcscheduler.model.shift.ShiftDayOrTimeContainsKeywordsPredicate;

/**
 * Finds and lists all shifts in the McScheduler whose day or time contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class ShiftFindCommand extends Command {

    public static final String COMMAND_WORD = "shift-find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all shifts whose days or times contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " mon tue";

    private final ShiftDayOrTimeContainsKeywordsPredicate predicate;

    public ShiftFindCommand(ShiftDayOrTimeContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredShiftList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_SHIFTS_LISTED_OVERVIEW, model.getFilteredShiftList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof ShiftFindCommand
                && predicate.equals(((ShiftFindCommand) other).predicate));
    }
}
