package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.RoleDeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

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
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, RoleDeleteCommand.MESSAGE_USAGE), pe);
        }
    }

}
