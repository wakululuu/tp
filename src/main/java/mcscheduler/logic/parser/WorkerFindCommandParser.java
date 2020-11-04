package mcscheduler.logic.parser;

import java.util.Arrays;

import mcscheduler.commons.core.Messages;
import mcscheduler.logic.commands.WorkerFindCommand;
import mcscheduler.logic.parser.exceptions.ParseException;
import mcscheduler.model.worker.NameContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new WorkerFindCommand object
 */
public class WorkerFindCommandParser implements Parser<WorkerFindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the WorkerFindCommand
     * and returns a WorkerFindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public WorkerFindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, WorkerFindCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");

        return new WorkerFindCommand(new NameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
    }

}
