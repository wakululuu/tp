package mcscheduler.logic.parser;

import mcscheduler.commons.core.Messages;
import mcscheduler.commons.core.index.Index;
import mcscheduler.logic.commands.RoleDeleteCommand;
import mcscheduler.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new RoleDeleteCommand object
 */
public class RoleDeleteCommandParser implements Parser<RoleDeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the RoleDeleteCommand
     * and returns a RoleDeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RoleDeleteCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new RoleDeleteCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    pe.getMessage() + RoleDeleteCommand.MESSAGE_USAGE), pe);
        }
    }

}
