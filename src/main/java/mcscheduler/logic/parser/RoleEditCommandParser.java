package mcscheduler.logic.parser;

import mcscheduler.commons.core.Messages;
import mcscheduler.commons.core.index.Index;
import mcscheduler.logic.commands.RoleEditCommand;
import mcscheduler.logic.parser.exceptions.ParseException;
import mcscheduler.model.role.Role;

/**
 * Parses input arguments and creates a new RoleEditCommand object
 */
public class RoleEditCommandParser implements Parser<RoleEditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the RoleEditCommand
     * and returns a RoleEditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RoleEditCommand parse(String args) throws ParseException {
        String[] argArray = args.trim().split(" ", 2);

        try {
            Index index = ParserUtil.parseIndex(argArray[0]);
            Role role = ParserUtil.parseRole(argArray[1]);
            return new RoleEditCommand(index, role);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    RoleEditCommand.MESSAGE_USAGE));
        } catch (ParseException pe) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    pe.getMessage() + RoleEditCommand.MESSAGE_USAGE), pe);
        }
    }

}
