package mcscheduler.logic.parser;

import static java.util.Objects.requireNonNull;
import static mcscheduler.commons.core.Messages.MESSAGE_DO_NOT_PARSE_LEAVE_ASSIGN;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_ROLE;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_SHIFT;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_SHIFT_NEW;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_SHIFT_OLD;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_WORKER;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_WORKER_NEW;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_WORKER_OLD;
import static mcscheduler.logic.parser.ParserUtil.allPrefixesAbsent;
import static mcscheduler.logic.parser.ParserUtil.arePrefixesPresent;
import static mcscheduler.logic.parser.ParserUtil.notAllPrefixesAbsent;

import mcscheduler.commons.core.Messages;
import mcscheduler.commons.core.index.Index;
import mcscheduler.commons.exceptions.IllegalValueException;
import mcscheduler.logic.commands.ReassignCommand;
import mcscheduler.logic.parser.exceptions.ParseException;
import mcscheduler.model.role.Leave;
import mcscheduler.model.role.Role;

public class ReassignCommandParser implements Parser<ReassignCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the ReassignCommand
     * and returns an ReassignCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format.
     */
    public ReassignCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
                PREFIX_WORKER_OLD, PREFIX_WORKER_NEW, PREFIX_SHIFT_OLD, PREFIX_SHIFT_NEW,
                PREFIX_ROLE, PREFIX_WORKER, PREFIX_SHIFT);

        if (arePrefixesPresent(argMultimap, PREFIX_WORKER_OLD, PREFIX_WORKER_NEW, PREFIX_SHIFT_OLD,
                PREFIX_SHIFT_NEW)) {
            if (notAllPrefixesAbsent(argMultimap, PREFIX_SHIFT, PREFIX_WORKER)) {
                throw new ParseException(
                        String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ReassignCommand.MESSAGE_USAGE));
            }
        }
        if (arePrefixesPresent(argMultimap, PREFIX_SHIFT, PREFIX_WORKER)) {
            if (notAllPrefixesAbsent(argMultimap, PREFIX_WORKER_OLD, PREFIX_WORKER_NEW, PREFIX_SHIFT_OLD,
                    PREFIX_SHIFT_NEW)) {
                throw new ParseException(
                        String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ReassignCommand.MESSAGE_USAGE));
            }
        }
        if (allPrefixesAbsent(argMultimap, PREFIX_SHIFT, PREFIX_WORKER)) {
            if (!arePrefixesPresent(argMultimap, PREFIX_WORKER_OLD, PREFIX_WORKER_NEW, PREFIX_SHIFT_OLD,
                    PREFIX_SHIFT_NEW)) {
                throw new ParseException(
                        String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ReassignCommand.MESSAGE_USAGE));
            }
        }
        if (allPrefixesAbsent(argMultimap, PREFIX_WORKER_OLD, PREFIX_WORKER_NEW, PREFIX_SHIFT_OLD,
                PREFIX_SHIFT_NEW)) {
            if (!arePrefixesPresent(argMultimap, PREFIX_SHIFT, PREFIX_WORKER)) {
                throw new ParseException(
                        String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ReassignCommand.MESSAGE_USAGE));
            }
        }
        if (!arePrefixesPresent(argMultimap, PREFIX_ROLE)) {
            throw new ParseException(
                        String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ReassignCommand.MESSAGE_USAGE));
        }

        Index oldShiftIndex;
        Index newShiftIndex;
        Index oldWorkerIndex;
        Index newWorkerIndex;
        try {
            oldShiftIndex = arePrefixesPresent(argMultimap, PREFIX_SHIFT_OLD)
                    ? ParserUtil.parseIndex(argMultimap.getValue(PREFIX_SHIFT_OLD).get())
                    : ParserUtil.parseIndex(argMultimap.getValue(PREFIX_SHIFT).get());
            newShiftIndex = arePrefixesPresent(argMultimap, PREFIX_SHIFT_NEW)
                    ? ParserUtil.parseIndex(argMultimap.getValue(PREFIX_SHIFT_NEW).get())
                    : oldShiftIndex;
            oldWorkerIndex = arePrefixesPresent(argMultimap, PREFIX_WORKER_OLD)
                    ? ParserUtil.parseIndex(argMultimap.getValue(PREFIX_WORKER_OLD).get())
                    : ParserUtil.parseIndex(argMultimap.getValue(PREFIX_WORKER).get());
            newWorkerIndex = arePrefixesPresent(argMultimap, PREFIX_WORKER_NEW)
                    ? ParserUtil.parseIndex(argMultimap.getValue(PREFIX_WORKER_NEW).get())
                    : oldWorkerIndex;
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    ive.getMessage() + ReassignCommand.MESSAGE_USAGE), ive);
        }

        Role newRole = ParserUtil.parseRole(argMultimap.getValue(PREFIX_ROLE).get());
        if (Leave.isLeave(newRole)) {
            throw new ParseException(MESSAGE_DO_NOT_PARSE_LEAVE_ASSIGN);
        }

        return new ReassignCommand(oldWorkerIndex, newWorkerIndex, oldShiftIndex, newShiftIndex, newRole);
    }
}
