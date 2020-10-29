package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SHIFT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WORKER_ROLE;

import java.util.Set;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AssignCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.assignment.WorkerRolePair;

/**
 * Parses input arguments and creates a new AssignCommand object
 */
public class AssignCommandParser implements Parser<AssignCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AssignCommand
     * and returns an AssignCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format.
     */
    public AssignCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
                PREFIX_SHIFT, PREFIX_WORKER_ROLE);

        if (!arePrefixesPresent(argMultimap, PREFIX_SHIFT, PREFIX_WORKER_ROLE)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignCommand.MESSAGE_USAGE));
        }

        Index shiftIndex;
        //Index workerIndex;
        Set<WorkerRolePair> workerRolePairs;
        try {
            shiftIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_SHIFT).get());
            //workerIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_WORKER).get());
            workerRolePairs = ParserUtil.parseWorkerRoles(argMultimap.getAllValues(PREFIX_WORKER_ROLE));
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AssignCommand.MESSAGE_USAGE), ive);
        }

        //Role role = ParserUtil.parseRole(argMultimap.getValue(PREFIX_ROLE).get());

        return new AssignCommand(shiftIndex, workerRolePairs);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
