package mcscheduler.logic.parser;

import static java.util.Objects.requireNonNull;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_ROLE_REQUIREMENT;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_SHIFT_DAY;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_SHIFT_TIME;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import mcscheduler.commons.core.Messages;
import mcscheduler.commons.core.index.Index;
import mcscheduler.logic.commands.ShiftEditCommand;
import mcscheduler.logic.parser.exceptions.ParseException;
import mcscheduler.model.shift.RoleRequirement;

/**
 * Parses input arguments and creates a new ShiftEditCommand object.
 */
public class ShiftEditCommandParser implements Parser<ShiftEditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ShiftEditCommand.
     *
     * @return ShiftEditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public ShiftEditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_SHIFT_DAY, PREFIX_SHIFT_TIME, PREFIX_ROLE_REQUIREMENT);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    pe.getMessage() + ShiftEditCommand.MESSAGE_USAGE), pe);
        }

        ShiftEditCommand.EditShiftDescriptor editShiftDescriptor = new ShiftEditCommand.EditShiftDescriptor();
        if (argMultimap.getValue(PREFIX_SHIFT_DAY).isPresent()) {
            editShiftDescriptor.setShiftDay(ParserUtil.parseShiftDay(argMultimap.getValue(PREFIX_SHIFT_DAY).get()));
        }
        if (argMultimap.getValue(PREFIX_SHIFT_TIME).isPresent()) {
            editShiftDescriptor.setShiftTime(ParserUtil.parseShiftTime(argMultimap.getValue(PREFIX_SHIFT_TIME).get()));
        }
        parseRoleRequirementsForEdit(argMultimap.getAllValues(PREFIX_ROLE_REQUIREMENT))
                .ifPresent(editShiftDescriptor::setRoleRequirements);

        if (!editShiftDescriptor.isAnyFieldEdited()) {
            throw new ParseException(ShiftEditCommand.MESSAGE_NOT_EDITED);
        }

        return new ShiftEditCommand(index, editShiftDescriptor);
    }

    /**
     * Parses non-empty {@code Collection<String> roleRequirements} into a {@code Set<RoleRequirement>}
     * If {@code roleRequirements} only contains an empty string element,
     * it will be parsed into an empty {@code Set<RoleRequirement>}.
     */
    private Optional<Set<RoleRequirement>> parseRoleRequirementsForEdit(
            Collection<String> roleRequirements) throws ParseException {
        assert roleRequirements != null;

        if (roleRequirements.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> roleRequirementsSet = roleRequirements.size() == 1 && roleRequirements.contains("")
                ? Collections.emptySet()
                : roleRequirements;
        return Optional.of(ParserUtil.parseRoleRequirements(roleRequirementsSet));
    }

}
