package mcscheduler.logic.parser;

import static java.util.Objects.requireNonNull;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_ROLE;

import mcscheduler.commons.core.Messages;
import mcscheduler.commons.core.index.Index;
import mcscheduler.logic.commands.WorkerAvailableCommand;
import mcscheduler.logic.parser.exceptions.ParseException;
import mcscheduler.model.role.Role;

/**
 * Parses input arguments and creates a new AvailableWorkersCommand object
 */
public class WorkerAvailableCommandParser implements Parser<WorkerAvailableCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AvailableWorkersCommand
     * and returns a AvailableWorkersCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public WorkerAvailableCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ROLE);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    pe.getMessage() + WorkerAvailableCommand.MESSAGE_USAGE), pe);
        }

        if (!argMultimap.getValue(PREFIX_ROLE).isPresent() || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, WorkerAvailableCommand.MESSAGE_USAGE));
        }
        Role role = ParserUtil.parseRole(argMultimap.getValue(PREFIX_ROLE).get());
        return new WorkerAvailableCommand(index, role);

    }
}
