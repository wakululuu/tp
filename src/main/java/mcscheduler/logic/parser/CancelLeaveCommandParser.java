package mcscheduler.logic.parser;

import static java.util.Objects.requireNonNull;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_SHIFT;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_WORKER;

import java.util.Set;

import mcscheduler.commons.core.Messages;
import mcscheduler.commons.core.index.Index;
import mcscheduler.commons.exceptions.IllegalValueException;
import mcscheduler.logic.commands.CancelLeaveCommand;
import mcscheduler.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new CancelLeaveCommand object.
 */
public class CancelLeaveCommandParser implements Parser<CancelLeaveCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the CancelLeaveCommand
     * and returns a CancelLeaveCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format.
     */
    public CancelLeaveCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argumentMultimap = ArgumentTokenizer.tokenize(args, PREFIX_SHIFT, PREFIX_WORKER);

        if (!ParserUtil.arePrefixesPresent(argumentMultimap, PREFIX_SHIFT, PREFIX_WORKER)) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, CancelLeaveCommand.MESSAGE_USAGE));
        }

        Index shiftIndex;
        Set<Index> workerIndexes;
        try {
            shiftIndex = ParserUtil.parseIndex(argumentMultimap.getValue(PREFIX_SHIFT).get());
            workerIndexes = ParserUtil.parseIndexes(argumentMultimap.getAllValues(PREFIX_WORKER));
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    ive.getMessage() + CancelLeaveCommand.MESSAGE_USAGE), ive);
        }

        return new CancelLeaveCommand(shiftIndex, workerIndexes);
    }

}
