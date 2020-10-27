package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_UNAVAILABILITY;
//import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
//import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.tag.Role;
import seedu.address.model.worker.Worker;

/**
 * Adds a worker to the address book.
 */
public class WorkerAddCommand extends Command {

    public static final String COMMAND_WORD = "worker-add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a worker to the address book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_PAY + "HOURLY_PAY "
            //+ PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + "[" + PREFIX_ROLE + "ROLE]...\n"
            + "[" + PREFIX_UNAVAILABILITY + "UNAVAILABLE TIMINGS]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_PAY + "10.20 "
            //+ PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_ROLE + "chef "
            + PREFIX_ROLE + "cashier "
            + PREFIX_UNAVAILABILITY + "MON PM";;

    public static final String MESSAGE_SUCCESS = "New worker added: %1$s";
    public static final String MESSAGE_DUPLICATE_WORKER = "This worker already exists in the address book";

    private final Worker toAdd;

    /**
     * Creates an WorkerAddCommand to add the specified {@code Worker}
     */
    public WorkerAddCommand(Worker worker) {
        requireNonNull(worker);
        toAdd = worker;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasWorker(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_WORKER);
        }

        Set<Role> roleSet = toAdd.getRoles();
        for (Role role : roleSet) {
            if (!model.hasRole(role)) {
                throw new CommandException(String.format(Messages.MESSAGE_ROLE_NOT_FOUND, role));
            }
        }

        model.addWorker(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof WorkerAddCommand // instanceof handles nulls
                && toAdd.equals(((WorkerAddCommand) other).toAdd));
    }
}
