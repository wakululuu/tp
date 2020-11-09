package mcscheduler.logic.commands;

import static java.util.Objects.requireNonNull;
import static mcscheduler.commons.core.Messages.MESSAGE_DO_NOT_MODIFY_LEAVE;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_ROLE_REQUIREMENT;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_SHIFT_DAY;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_SHIFT_TIME;

import java.util.Set;

import mcscheduler.commons.core.Messages;
import mcscheduler.logic.commands.exceptions.CommandException;
import mcscheduler.model.Model;
import mcscheduler.model.role.Leave;
import mcscheduler.model.shift.RoleRequirement;
import mcscheduler.model.shift.Shift;

/**
 * Adds a shift to the McScheduler.
 */
public class ShiftAddCommand extends Command {
    public static final String COMMAND_WORD = "shift-add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a shift to the McScheduler. "
            + "Parameters: "
            + PREFIX_SHIFT_DAY + "DAY "
            + PREFIX_SHIFT_TIME + "TIME "
            + "[" + PREFIX_ROLE_REQUIREMENT + "ROLE NUMBER_NEEDED]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_SHIFT_DAY + "MON "
            + PREFIX_SHIFT_TIME + "AM "
            + PREFIX_ROLE_REQUIREMENT + "Cleaner 2 "
            + PREFIX_ROLE_REQUIREMENT + "Cashier 1";

    public static final String MESSAGE_SUCCESS = "New shift added: %1$s";
    public static final String MESSAGE_DUPLICATE_SHIFT = "This shift already exists in the McScheduler";

    private final Shift shiftToAdd;

    /**
     * Creates a ShiftAddCommand to add the specified {@code Shift}
     */
    public ShiftAddCommand(Shift shift) {
        requireNonNull(shift);
        shiftToAdd = shift;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (model.hasShift(shiftToAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_SHIFT);
        }

        Set<RoleRequirement> roleRequirementSet = shiftToAdd.getRoleRequirements();
        for (RoleRequirement requirement : roleRequirementSet) {
            if (!model.hasRole(requirement.getRole())) {
                throw new CommandException(String.format(Messages.MESSAGE_ROLE_NOT_FOUND, requirement.getRole()));
            }
            if (Leave.isLeave(requirement.getRole())) {
                throw new CommandException(MESSAGE_DO_NOT_MODIFY_LEAVE);
            }
        }

        model.addShift(shiftToAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, shiftToAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ShiftAddCommand // instanceof handles nulls
                && shiftToAdd.equals(((ShiftAddCommand) other).shiftToAdd));
    }
}
