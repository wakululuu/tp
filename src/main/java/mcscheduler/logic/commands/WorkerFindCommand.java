package mcscheduler.logic.commands;

import static java.util.Objects.requireNonNull;

import mcscheduler.commons.core.Messages;
import mcscheduler.model.Model;
import mcscheduler.model.worker.NameContainsKeywordsPredicate;

/**
 * Finds and lists all workers in the McScheduler whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class WorkerFindCommand extends Command {

    public static final String COMMAND_WORD = "worker-find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all workers whose names contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie";

    private final NameContainsKeywordsPredicate predicate;

    public WorkerFindCommand(NameContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredWorkerList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_WORKERS_LISTED_OVERVIEW, model.getFilteredWorkerList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof WorkerFindCommand // instanceof handles nulls
                && predicate.equals(((WorkerFindCommand) other).predicate)); // state check
    }
}
