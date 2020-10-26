package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.tag.Role;

/**
 * Adds a role to the address book.
 */
public class RoleAddCommand extends Command {

    public static final String COMMAND_WORD = "role-add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a role to the address book. "
            + "Parameters: ROLE"
            + "Example: " + COMMAND_WORD + " cashier";

    public static final String MESSAGE_SUCCESS = "New role added: %1$s";
    public static final String MESSAGE_DUPLICATE_ROLE = "This role already exists in the address book";

    private final Role toAdd;

    /**
     * Creates a RoleAddCommand to add the specified {@code Role}
     */
    public RoleAddCommand(Role role) {
        requireNonNull(role);
        toAdd = role;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasRole(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_ROLE);
        }

        model.addRole(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof RoleAddCommand // instanceof handles nulls
                && toAdd.equals(((RoleAddCommand) other).toAdd));
    }
}
