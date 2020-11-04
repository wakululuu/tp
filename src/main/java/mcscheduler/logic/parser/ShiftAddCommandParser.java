package mcscheduler.logic.parser;

import static mcscheduler.logic.parser.CliSyntax.PREFIX_ROLE_REQUIREMENT;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_SHIFT_DAY;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_SHIFT_TIME;
import static mcscheduler.logic.parser.ParserUtil.arePrefixesPresent;

import java.util.Set;

import mcscheduler.commons.core.Messages;
import mcscheduler.logic.commands.ShiftAddCommand;
import mcscheduler.logic.parser.exceptions.ParseException;
import mcscheduler.model.shift.RoleRequirement;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.shift.ShiftDay;
import mcscheduler.model.shift.ShiftTime;

/**
 * Parses input arguments and creates a new ShiftAddCommand object
 */
public class ShiftAddCommandParser implements Parser<ShiftAddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the WorkerAddCommand
     * and returns an WorkerAddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ShiftAddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_SHIFT_DAY, PREFIX_SHIFT_TIME, PREFIX_ROLE_REQUIREMENT);

        if (!arePrefixesPresent(argMultimap, PREFIX_SHIFT_DAY, PREFIX_SHIFT_TIME)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ShiftAddCommand.MESSAGE_USAGE));
        }

        ShiftDay shiftDay = ParserUtil.parseShiftDay(argMultimap.getValue(PREFIX_SHIFT_DAY).get());
        ShiftTime shiftTime = ParserUtil.parseShiftTime(argMultimap.getValue(PREFIX_SHIFT_TIME).get());
        Set<RoleRequirement> roleReqList =
                ParserUtil.parseRoleRequirements(argMultimap.getAllValues(PREFIX_ROLE_REQUIREMENT));

        Shift shift = new Shift(shiftDay, shiftTime, roleReqList);

        return new ShiftAddCommand(shift);
    }
}
