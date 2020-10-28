package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.AssignCommand;
import seedu.address.logic.commands.CancelLeaveCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.MassCancelLeaveCommand;
import seedu.address.logic.commands.MassTakeLeaveCommand;
import seedu.address.logic.commands.ReassignCommand;
import seedu.address.logic.commands.RoleAddCommand;
import seedu.address.logic.commands.RoleDeleteCommand;
import seedu.address.logic.commands.RoleListCommand;
import seedu.address.logic.commands.ShiftAddCommand;
import seedu.address.logic.commands.ShiftDeleteCommand;
import seedu.address.logic.commands.ShiftEditCommand;
import seedu.address.logic.commands.ShiftListCommand;
import seedu.address.logic.commands.TakeLeaveCommand;
import seedu.address.logic.commands.UnassignCommand;
import seedu.address.logic.commands.WorkerAddCommand;
import seedu.address.logic.commands.WorkerDeleteCommand;
import seedu.address.logic.commands.WorkerEditCommand;
import seedu.address.logic.commands.WorkerListCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class AddressBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord.toLowerCase()) {

        case WorkerAddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments);

        case AssignCommand.COMMAND_WORD:
            return new AssignCommandParser().parse(arguments);

        case WorkerEditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case WorkerDeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case WorkerListCommand.COMMAND_WORD:
            return new WorkerListCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case UnassignCommand.COMMAND_WORD:
            return new UnassignCommandParser().parse(arguments);

        case ReassignCommand.COMMAND_WORD:
            return new ReassignCommandParser().parse(arguments);

        case TakeLeaveCommand.COMMAND_WORD:
            return new TakeLeaveCommandParser().parse(arguments);

        case MassTakeLeaveCommand.COMMAND_WORD:
            return new MassTakeLeaveCommandParser().parse(arguments);

        case CancelLeaveCommand.COMMAND_WORD:
            return new CancelLeaveCommandParser().parse(arguments);

        case MassCancelLeaveCommand.COMMAND_WORD:
            return new MassCancelLeaveCommandParser().parse(arguments);

        case ShiftAddCommand.COMMAND_WORD:
            return new ShiftAddCommandParser().parse(arguments);

        case ShiftEditCommand.COMMAND_WORD:
            return new ShiftEditCommandParser().parse(arguments);

        case ShiftDeleteCommand.COMMAND_WORD:
            return new ShiftDeleteCommandParser().parse(arguments);

        case ShiftListCommand.COMMAND_WORD:
            return new ShiftListCommand();

        case RoleAddCommand.COMMAND_WORD:
            return new RoleAddCommandParser().parse(arguments);

        case RoleDeleteCommand.COMMAND_WORD:
            return new RoleDeleteCommandParser().parse(arguments);

        case RoleListCommand.COMMAND_WORD:
            return new RoleListCommand();

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
