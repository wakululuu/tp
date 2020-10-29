package mcscheduler.logic.parser;

import static mcscheduler.logic.parser.CliSyntax.PREFIX_ROLE_REQUIREMENT;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_SHIFT_DAY;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_SHIFT_TIME;

import java.util.Set;
import java.util.stream.Stream;

import mcscheduler.commons.core.*;
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
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ShiftAddCommand.MESSAGE_USAGE));
        }

        ShiftDay shiftDay = ParserUtil.parseShiftDay(argMultimap.getValue(PREFIX_SHIFT_DAY).get());
        ShiftTime shiftTime = ParserUtil.parseShiftTime(argMultimap.getValue(PREFIX_SHIFT_TIME).get());
        Set<RoleRequirement> roleReqList =
                ParserUtil.parseRoleRequirements(argMultimap.getAllValues(PREFIX_ROLE_REQUIREMENT));

        Shift shift = new Shift(shiftDay, shiftTime, roleReqList);

        return new ShiftAddCommand(shift);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
