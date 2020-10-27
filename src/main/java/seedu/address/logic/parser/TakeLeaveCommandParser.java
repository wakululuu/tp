package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SHIFT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WORKER;
import static seedu.address.logic.parser.ParserUtil.arePrefixesPresent;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.TakeLeaveCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new TakeLeaveCommand object.
 */
public class TakeLeaveCommandParser implements Parser<TakeLeaveCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the TakeLeaveCommand
     * and returns an TakeLeaveCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format.
     */
    public TakeLeaveCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argumentMultimap = ArgumentTokenizer.tokenize(args,
                PREFIX_SHIFT, PREFIX_WORKER);

        if (!arePrefixesPresent(argumentMultimap, PREFIX_SHIFT, PREFIX_WORKER)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    TakeLeaveCommand.MESSAGE_USAGE));
        }

        Index shiftIndex;
        Index workerIndex;
        try {
            shiftIndex = ParserUtil.parseIndex(argumentMultimap.getValue(PREFIX_SHIFT).get());
            workerIndex = ParserUtil.parseIndex(argumentMultimap.getValue(PREFIX_WORKER).get());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    TakeLeaveCommand.MESSAGE_USAGE, ive));
        }

        return new TakeLeaveCommand(shiftIndex, workerIndex);
    }
}
