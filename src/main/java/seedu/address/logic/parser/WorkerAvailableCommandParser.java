package seedu.address.logic.parser;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.WorkerAvailableCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Role;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;

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
        if (!argMultimap.getValue(PREFIX_ROLE).isPresent() || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, WorkerAvailableCommand.MESSAGE_USAGE));
        }
        try {
            Index index = ParserUtil.parseIndex(argMultimap.getPreamble());
            Role role = ParserUtil.parseRole(argMultimap.getValue(PREFIX_ROLE).get());
            return new WorkerAvailableCommand(index, role);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, WorkerAvailableCommand.MESSAGE_USAGE), pe);
        }
    }
}
