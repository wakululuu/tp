package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SHIFT_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SHIFT_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WORKER;
import static seedu.address.logic.parser.ParserUtil.arePrefixesPresent;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.MassCancelLeaveCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.shift.ShiftDay;
import seedu.address.model.shift.ShiftTime;

public class MassCancelLeaveCommandParser implements Parser<MassCancelLeaveCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the MassTakeLeaveCommand
     * and returns a MassCancelLeaveCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format.
     */
    public MassCancelLeaveCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argumentMultimap = ArgumentTokenizer.tokenize(args,
                PREFIX_WORKER, PREFIX_SHIFT_DAY, PREFIX_SHIFT_TIME);

        if (!arePrefixesPresent(argumentMultimap, PREFIX_WORKER, PREFIX_SHIFT_DAY, PREFIX_SHIFT_TIME)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    MassCancelLeaveCommand.MESSAGE_USAGE));
        }

        if (argumentMultimap.getAllValues(PREFIX_SHIFT_DAY).size() != 2
                || argumentMultimap.getAllValues(PREFIX_SHIFT_TIME).size() != 2) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    MassCancelLeaveCommand.MESSAGE_USAGE));
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
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    MassCancelLeaveCommand.MESSAGE_USAGE), ive);
        }

        return new MassCancelLeaveCommand(workerIndex, startDay, startTime, endDay, endTime);
    }
}
