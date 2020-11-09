package mcscheduler.logic.parser;

import static java.util.Objects.requireNonNull;
import static mcscheduler.commons.core.Messages.MESSAGE_DO_NOT_PARSE_LEAVE_ASSIGN;
import static mcscheduler.logic.parser.ParserUtil.arePrefixesPresent;

import java.util.Set;

import mcscheduler.commons.core.Messages;
import mcscheduler.commons.core.index.Index;
import mcscheduler.commons.exceptions.IllegalValueException;
import mcscheduler.logic.commands.AssignCommand;
import mcscheduler.logic.parser.exceptions.ParseException;
import mcscheduler.model.assignment.WorkerRolePair;
import mcscheduler.model.role.Leave;

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
                CliSyntax.PREFIX_SHIFT, CliSyntax.PREFIX_WORKER_ROLE);

        if (!arePrefixesPresent(argMultimap, CliSyntax.PREFIX_SHIFT, CliSyntax.PREFIX_WORKER_ROLE)) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, AssignCommand.MESSAGE_USAGE));
        }

        Index shiftIndex;
        Set<WorkerRolePair> workerRolePairs;
        try {
            shiftIndex = ParserUtil.parseIndex(argMultimap.getValue(CliSyntax.PREFIX_SHIFT).get());
            workerRolePairs = ParserUtil.parseWorkerRoles(argMultimap.getAllValues(CliSyntax.PREFIX_WORKER_ROLE));
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    ive.getMessage() + AssignCommand.MESSAGE_USAGE), ive);
        }
        if (workerRolePairs.stream().anyMatch(workerRolePair -> Leave.isLeave(workerRolePair.getRole()))) {
            throw new ParseException(MESSAGE_DO_NOT_PARSE_LEAVE_ASSIGN);
        }

        return new AssignCommand(shiftIndex, workerRolePairs);
    }
}
