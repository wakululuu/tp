package mcscheduler.model.shift;

import java.util.Arrays;

import mcscheduler.commons.core.Messages;
import mcscheduler.logic.commands.ShiftFindCommand;
import mcscheduler.logic.parser.Parser;
import mcscheduler.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ShiftFindCommand object
 */
public class ShiftFindCommandParser implements Parser<ShiftFindCommand> {

    /**
     * Parses the given {@code String} of arguments int he context of the ShiftFindCommand and returns a
     * ShiftFindCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format.
     */
    public ShiftFindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ShiftFindCommand.MESSAGE_USAGE));
        }

        String[] keywords = trimmedArgs.split("\\s+");

        return new ShiftFindCommand(new ShiftDayOrTimeContainsKeywordsPredicate(Arrays.asList(keywords)));
    }
}
