package mcscheduler.logic.parser;

import mcscheduler.commons.core.Messages;
import mcscheduler.commons.core.index.Index;
import mcscheduler.logic.commands.ShiftDeleteCommand;
import mcscheduler.logic.parser.exceptions.ParseException;


/**
 * Parses input arguments and creates a new ShiftDeleteCommand object
 */
public class ShiftDeleteCommandParser implements Parser<ShiftDeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ShiftDeleteCommand
     * and returns a ShiftDeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ShiftDeleteCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new ShiftDeleteCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    pe.getMessage() + ShiftDeleteCommand.MESSAGE_USAGE), pe);
        }
    }
}
