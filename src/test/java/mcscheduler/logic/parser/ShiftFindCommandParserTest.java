package mcscheduler.logic.parser;

import static mcscheduler.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static mcscheduler.logic.parser.CommandParserTestUtil.assertParseFailure;
import static mcscheduler.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import mcscheduler.logic.commands.ShiftFindCommand;
import mcscheduler.model.shift.ShiftDayOrTimeContainsKeywordsPredicate;
import mcscheduler.model.shift.ShiftFindCommandParser;

public class ShiftFindCommandParserTest {

    private final ShiftFindCommandParser parser = new ShiftFindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "           ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ShiftFindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        ShiftFindCommand expectedCommand =
                new ShiftFindCommand(new ShiftDayOrTimeContainsKeywordsPredicate(Arrays.asList("help", "mon", "am")));
        assertParseSuccess(parser, "help mon am", expectedCommand);
        assertParseSuccess(parser, " \n\n help \t\n\t mon  am", expectedCommand);
    }
}
