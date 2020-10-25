package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SHIFT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WORKER;
import static seedu.address.logic.parser.ParserUtil.arePrefixesPresent;

import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.CancelLeaveCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new CancelLeaveCommand object.
 */
public class CancelLeaveCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the CancelLeaveCommand
     * and returns a CancelLeaveCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format.
     */
    public CancelLeaveCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argumentMultimap = ArgumentTokenizer.tokenize(args, PREFIX_SHIFT, PREFIX_WORKER);

        if (!arePrefixesPresent(argumentMultimap, PREFIX_SHIFT, PREFIX_WORKER)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, CancelLeaveCommand.MESSAGE_USAGE));
        }

        Index shiftIndex;
        Set<Index> workerIndexes;
        try {
            shiftIndex = ParserUtil.parseIndex(argumentMultimap.getValue(PREFIX_SHIFT).get());
            workerIndexes = ParserUtil.parseIndexes(argumentMultimap.getAllValues(PREFIX_WORKER));
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    CancelLeaveCommand.MESSAGE_USAGE), ive);
        }

        return new CancelLeaveCommand(shiftIndex, workerIndexes);
    }

}
