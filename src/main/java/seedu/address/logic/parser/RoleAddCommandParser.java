package seedu.address.logic.parser;

import seedu.address.logic.commands.RoleAddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Role;

/**
 * Parses input arguments and creates a new RoleAddCommand object
 */
public class RoleAddCommandParser implements Parser<RoleAddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the RoleAddCommand
     * and returns a RoleAddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RoleAddCommand parse(String args) throws ParseException {
        Role role = ParserUtil.parseRole(args);

        return new RoleAddCommand(role);
    }
}
