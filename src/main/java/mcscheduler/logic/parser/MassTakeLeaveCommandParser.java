package mcscheduler.logic.parser;

import static java.util.Objects.requireNonNull;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_SHIFT_DAY;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_SHIFT_TIME;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_WORKER;

import mcscheduler.commons.core.Messages;
import mcscheduler.commons.core.index.Index;
import mcscheduler.commons.exceptions.IllegalValueException;
import mcscheduler.logic.commands.MassTakeLeaveCommand;
import mcscheduler.logic.parser.exceptions.ParseException;
import mcscheduler.model.shift.ShiftDay;
import mcscheduler.model.shift.ShiftTime;

public class MassTakeLeaveCommandParser implements Parser<MassTakeLeaveCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the MassTakeLeaveCommand
     * and returns a MassTakeLeaveCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format.
     */
    public MassTakeLeaveCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argumentMultimap = ArgumentTokenizer.tokenize(args,
                PREFIX_WORKER, PREFIX_SHIFT_DAY, PREFIX_SHIFT_TIME);

        if (!ParserUtil.arePrefixesPresent(argumentMultimap, PREFIX_WORKER, PREFIX_SHIFT_DAY, PREFIX_SHIFT_TIME)) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    MassTakeLeaveCommand.MESSAGE_USAGE));
        }

        if (argumentMultimap.getAllValues(PREFIX_SHIFT_DAY).size() != 2
            || argumentMultimap.getAllValues(PREFIX_SHIFT_TIME).size() != 2) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    MassTakeLeaveCommand.MESSAGE_USAGE));
        }

        Index workerIndex;
        ShiftDay startDay;
        ShiftTime startTime;
        ShiftDay endDay;
        ShiftTime endTime;
        try {
            workerIndex = ParserUtil.parseIndex(argumentMultimap.getValue(PREFIX_WORKER).get());
            startDay = ParserUtil.parseShiftDay(argumentMultimap.getAllValues(PREFIX_SHIFT_DAY).get(0));
            startTime = ParserUtil.parseShiftTime(argumentMultimap.getAllValues(PREFIX_SHIFT_TIME).get(0));
            endDay = ParserUtil.parseShiftDay(argumentMultimap.getAllValues(PREFIX_SHIFT_DAY).get(1));
            endTime = ParserUtil.parseShiftTime(argumentMultimap.getAllValues(PREFIX_SHIFT_TIME).get(1));
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    ive.getMessage() + MassTakeLeaveCommand.MESSAGE_USAGE), ive);
        }

        return new MassTakeLeaveCommand(workerIndex, startDay, startTime, endDay, endTime);
    }

}
