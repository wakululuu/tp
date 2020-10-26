package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_UNAVAILABILITY;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.WorkerEditCommand;
import seedu.address.logic.commands.WorkerEditCommand.EditWorkerDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.tag.Role;
import seedu.address.model.worker.Unavailability;

/**
 * Parses input arguments and creates a new WorkerEditCommand object
 */
public class EditCommandParser implements Parser<WorkerEditCommand> {

    private final Model model;

    /** Standard constructor. */
    public EditCommandParser(Model model) {
        super();
        this.model = model;
    }

    /**
     * Parses the given {@code String} of arguments in the context of the WorkerEditCommand
     * and returns an WorkerEditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public WorkerEditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_PAY, PREFIX_ADDRESS, PREFIX_ROLE,
                        PREFIX_UNAVAILABILITY);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    WorkerEditCommand.MESSAGE_USAGE), pe);
        }

        EditWorkerDescriptor editWorkerDescriptor = new EditWorkerDescriptor();
        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editWorkerDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editWorkerDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_PAY).isPresent()) {
            editWorkerDescriptor.setPay(ParserUtil.parsePay(argMultimap.getValue(PREFIX_PAY).get()));
        }

        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            editWorkerDescriptor.setAddress(ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get()));
        }

        parseRolesForEdit(argMultimap.getAllValues(PREFIX_ROLE)).ifPresent(editWorkerDescriptor::setRoles);
        parseUnavailabilitiesForEdit(argMultimap.getAllValues(PREFIX_UNAVAILABILITY))
                .ifPresent(editWorkerDescriptor::setUnavailableTimings);

        if (!editWorkerDescriptor.isAnyFieldEdited()) {
            throw new ParseException(WorkerEditCommand.MESSAGE_NOT_EDITED);
        }

        return new WorkerEditCommand(index, editWorkerDescriptor);
    }

    /**
     * Parses {@code Collection<String> roles} into a {@code Set<Role>} if {@code roles} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Role>} containing zero roles.
     */
    private Optional<Set<Role>> parseRolesForEdit(Collection<String> roles) throws ParseException {
        assert roles != null;

        if (roles.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> roleSet = roles.size() == 1 && roles.contains("") ? Collections.emptySet() : roles;
        return Optional.of(ParserUtil.parseRoles(roleSet, model));
    }

    private Optional<Set<Unavailability>> parseUnavailabilitiesForEdit(Collection<String> unavailabilities)
            throws ParseException {
        assert unavailabilities != null;

        if (unavailabilities.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> unavailabilitySet = unavailabilities.size() == 1 && unavailabilities.contains("")
                ? Collections.emptySet() : unavailabilities;
        return Optional.of(ParserUtil.parseUnavailabilities(unavailabilitySet));
    }

}
