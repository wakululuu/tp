package mcscheduler.logic.parser;

import static mcscheduler.logic.parser.ParserUtil.arePrefixesPresent;

import java.util.Set;

import mcscheduler.commons.core.Messages;
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
public class AddCommandParser implements Parser<WorkerAddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the WorkerAddCommand
     * and returns an WorkerAddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public WorkerAddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, CliSyntax.PREFIX_NAME, CliSyntax.PREFIX_PHONE, CliSyntax.PREFIX_PAY,
                    CliSyntax.PREFIX_ADDRESS, CliSyntax.PREFIX_ROLE, CliSyntax.PREFIX_UNAVAILABILITY);

        if (!arePrefixesPresent(argMultimap, CliSyntax.PREFIX_NAME, CliSyntax.PREFIX_ADDRESS,
            CliSyntax.PREFIX_PHONE, CliSyntax.PREFIX_PAY) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    WorkerAddCommand.MESSAGE_USAGE));
        }

        Name name = ParserUtil.parseName(argMultimap.getValue(CliSyntax.PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(CliSyntax.PREFIX_PHONE).get());
        Pay pay = ParserUtil.parsePay(argMultimap.getValue(CliSyntax.PREFIX_PAY).get());
        Address address = ParserUtil.parseAddress(argMultimap.getValue(CliSyntax.PREFIX_ADDRESS).get());
        Set<Role> roleList = ParserUtil.parseRoles(argMultimap.getAllValues(CliSyntax.PREFIX_ROLE));
        Set<Unavailability> unavailableTimings = ParserUtil.parseUnavailabilities(argMultimap
                .getAllValues(CliSyntax.PREFIX_UNAVAILABILITY));

        Worker worker = new Worker(name, phone, pay, address, roleList, unavailableTimings);

        return new WorkerAddCommand(worker);
    }

}
