package mcscheduler.logic.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mcscheduler.commons.core.Messages;
import mcscheduler.logic.commands.AssignCommand;
import mcscheduler.logic.commands.CancelLeaveCommand;
import mcscheduler.logic.commands.ClearCommand;
import mcscheduler.logic.commands.Command;
import mcscheduler.logic.commands.ExitCommand;
import mcscheduler.logic.commands.HelpCommand;
import mcscheduler.logic.commands.MassCancelLeaveCommand;
import mcscheduler.logic.commands.MassTakeLeaveCommand;
import mcscheduler.logic.commands.ReassignCommand;
import mcscheduler.logic.commands.RoleAddCommand;
import mcscheduler.logic.commands.RoleDeleteCommand;
import mcscheduler.logic.commands.RoleEditCommand;
import mcscheduler.logic.commands.ShiftAddCommand;
import mcscheduler.logic.commands.ShiftDeleteCommand;
import mcscheduler.logic.commands.ShiftEditCommand;
import mcscheduler.logic.commands.ShiftFindCommand;
import mcscheduler.logic.commands.ShiftListCommand;
import mcscheduler.logic.commands.TakeLeaveCommand;
import mcscheduler.logic.commands.UnassignCommand;
import mcscheduler.logic.commands.WorkerAddCommand;
import mcscheduler.logic.commands.WorkerAvailableCommand;
import mcscheduler.logic.commands.WorkerDeleteCommand;
import mcscheduler.logic.commands.WorkerEditCommand;
import mcscheduler.logic.commands.WorkerFindCommand;
import mcscheduler.logic.commands.WorkerListCommand;
import mcscheduler.logic.commands.WorkerPayCommand;
import mcscheduler.logic.parser.exceptions.ParseException;
import mcscheduler.model.shift.ShiftFindCommandParser;

/**
 * Parses user input.
 */
public class McSchedulerParser {

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
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord.toLowerCase()) {

        case WorkerAddCommand.COMMAND_WORD:
            return new WorkerAddCommandParser().parse(arguments);

        case AssignCommand.COMMAND_WORD:
            return new AssignCommandParser().parse(arguments);

        case WorkerEditCommand.COMMAND_WORD:
            return new WorkerEditCommandParser().parse(arguments);

        case WorkerDeleteCommand.COMMAND_WORD:
            return new WorkerDeleteCommandParser().parse(arguments);

        case WorkerAvailableCommand.COMMAND_WORD:
            return new WorkerAvailableCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            if (!arguments.trim().equals("")) {
                throw new ParseException(String.format(Messages.MESSAGE_UNEXPECTED_ARGUMENT,
                        ClearCommand.COMMAND_WORD, arguments.trim()));
            }
            return new ClearCommand();

        case WorkerFindCommand.COMMAND_WORD:
            return new WorkerFindCommandParser().parse(arguments);

        case WorkerListCommand.COMMAND_WORD:
            if (!arguments.trim().equals("")) {
                throw new ParseException(String.format(Messages.MESSAGE_UNEXPECTED_ARGUMENT,
                        WorkerListCommand.COMMAND_WORD, arguments.trim()));
            }
            return new WorkerListCommand();

        case ExitCommand.COMMAND_WORD:
            if (!arguments.trim().equals("")) {
                throw new ParseException(String.format(Messages.MESSAGE_UNEXPECTED_ARGUMENT,
                        ExitCommand.COMMAND_WORD, arguments.trim()));
            }
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            if (!arguments.trim().equals("")) {
                throw new ParseException(String.format(Messages.MESSAGE_UNEXPECTED_ARGUMENT,
                        HelpCommand.COMMAND_WORD, arguments.trim()));
            }
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
            if (!arguments.trim().equals("")) {
                throw new ParseException(String.format(Messages.MESSAGE_UNEXPECTED_ARGUMENT,
                        ShiftListCommand.COMMAND_WORD, arguments.trim()));
            }
            return new ShiftListCommand();

        case ShiftFindCommand.COMMAND_WORD:
            return new ShiftFindCommandParser().parse(arguments);

        case RoleAddCommand.COMMAND_WORD:
            return new RoleAddCommandParser().parse(arguments);

        case RoleDeleteCommand.COMMAND_WORD:
            return new RoleDeleteCommandParser().parse(arguments);

        case RoleEditCommand.COMMAND_WORD:
            return new RoleEditCommandParser().parse(arguments);

        case WorkerPayCommand.COMMAND_WORD:
            return new WorkerPayCommandParser().parse(arguments);

        default:
            throw new ParseException(Messages.MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
