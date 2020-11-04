package mcscheduler.logic.parser;

import mcscheduler.commons.core.Messages;
import mcscheduler.commons.core.index.Index;
import mcscheduler.logic.commands.WorkerDeleteCommand;
import mcscheduler.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new WorkerDeleteCommand object
 */
public class WorkerDeleteCommandParser implements Parser<WorkerDeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the WorkerDeleteCommand
     * and returns a WorkerDeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public WorkerDeleteCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new WorkerDeleteCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    pe.getMessage() + WorkerDeleteCommand.MESSAGE_USAGE), pe);
        }
    }

}
