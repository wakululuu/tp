package mcscheduler.logic.parser;

import static java.util.Objects.requireNonNull;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_SHIFT;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_WORKER;
import static mcscheduler.logic.parser.ParserUtil.arePrefixesPresent;

import java.util.Set;

import mcscheduler.commons.core.Messages;
import mcscheduler.commons.core.index.Index;
import mcscheduler.commons.exceptions.IllegalValueException;
import mcscheduler.logic.commands.TakeLeaveCommand;
import mcscheduler.logic.parser.exceptions.ParseException;

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
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    TakeLeaveCommand.MESSAGE_USAGE));
        }

        Index shiftIndex;
        Set<Index> workerIndexes;
        try {
            shiftIndex = ParserUtil.parseIndex(argumentMultimap.getValue(PREFIX_SHIFT).get());
            workerIndexes = ParserUtil.parseIndexes(argumentMultimap.getAllValues(PREFIX_WORKER));
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    ive.getMessage() + TakeLeaveCommand.MESSAGE_USAGE, ive));
        }

        return new TakeLeaveCommand(shiftIndex, workerIndexes);
    }
}
