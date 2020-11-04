package mcscheduler.logic.parser;

import static mcscheduler.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_NAME;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_PAY;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_PHONE;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_ROLE;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_UNAVAILABILITY;
import static mcscheduler.logic.parser.ParserUtil.arePrefixesPresent;

import java.util.Set;

import mcscheduler.logic.commands.WorkerAddCommand;
import mcscheduler.logic.parser.exceptions.ParseException;
import mcscheduler.model.role.Role;
import mcscheduler.model.worker.Address;
import mcscheduler.model.worker.Name;
import mcscheduler.model.worker.Pay;
import mcscheduler.model.worker.Phone;
import mcscheduler.model.worker.Unavailability;
import mcscheduler.model.worker.Worker;

/**
 * Parses input arguments and creates a new WorkerAddCommand object
 */
public class WorkerAddCommandParser implements Parser<WorkerAddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the WorkerAddCommand
     * and returns an WorkerAddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public WorkerAddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_PAY, PREFIX_ADDRESS, PREFIX_ROLE,
                        PREFIX_UNAVAILABILITY);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE, PREFIX_PAY)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, WorkerAddCommand.MESSAGE_USAGE));
        }

        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Pay pay = ParserUtil.parsePay(argMultimap.getValue(PREFIX_PAY).get());
        Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get());
        Set<Role> roleList = ParserUtil.parseRoles(argMultimap.getAllValues(PREFIX_ROLE));
        Set<Unavailability> unavailableTimings = ParserUtil.parseUnavailabilities(argMultimap
                .getAllValues(PREFIX_UNAVAILABILITY));

        Worker worker = new Worker(name, phone, pay, address, roleList, unavailableTimings);

        return new WorkerAddCommand(worker);
    }

}
