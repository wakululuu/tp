package mcscheduler.logic.parser;

import mcscheduler.commons.core.Messages;
import mcscheduler.commons.core.index.Index;
import mcscheduler.logic.commands.WorkerPayCommand;
import mcscheduler.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new WorkerPayCommand object
 */
public class WorkerPayCommandParser implements Parser<WorkerPayCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the WorkerPayCommand
     * and returns a WorkerPayCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public WorkerPayCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new WorkerPayCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    pe.getMessage() + WorkerPayCommand.MESSAGE_USAGE), pe);
        }
    }
}
