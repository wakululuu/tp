package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE_REQUIREMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SHIFT_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SHIFT_TIME;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.shift.Shift;

/**
 * Adds a shift to the McScheduler.
 */
public class ShiftAddCommand extends Command {
    public static final String COMMAND_WORD = "shift-add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a shift to the McScheduler. "
            + "Parameters: "
            + PREFIX_SHIFT_DAY + "DAY "
            + PREFIX_SHIFT_TIME + "TIME "
            + "[" + PREFIX_ROLE_REQUIREMENT + "ROLE <SPACE> NUMBER_NEEDED]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_SHIFT_DAY + "MON "
            + PREFIX_SHIFT_TIME + "AM "
            + PREFIX_ROLE_REQUIREMENT + "Cleaner 2 "
            + PREFIX_ROLE_REQUIREMENT + "Cashier 1";

    public static final String MESSAGE_SUCCESS = "New shift added: %1$s";
    public static final String MESSAGE_DUPLICATE_SHIFT = "This shift already exists in the McScheduler";

    private final Shift toAdd;

    /**
     * Creates a ShiftAddCommand to add the specified {@code Shift}
     */
    public ShiftAddCommand(Shift shift) {
        requireNonNull(shift);
        toAdd = shift;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (model.hasShift(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_SHIFT);
        }

        model.addShift(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ShiftAddCommand // instanceof handles nulls
                && toAdd.equals(((ShiftAddCommand) other).toAdd));
    }
}
