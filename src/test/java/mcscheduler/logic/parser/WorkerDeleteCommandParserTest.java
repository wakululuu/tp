package mcscheduler.logic.parser;

import static mcscheduler.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static mcscheduler.logic.parser.CommandParserTestUtil.assertParseFailure;
import static mcscheduler.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import mcscheduler.commons.core.Messages;
import mcscheduler.logic.commands.WorkerDeleteCommand;
import mcscheduler.testutil.TypicalIndexes;

//@@author
/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the WorkerDeleteCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the WorkerDeleteCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class WorkerDeleteCommandParserTest {

    private final WorkerDeleteCommandParser parser = new WorkerDeleteCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteCommand() {
        assertParseSuccess(parser, "1", new WorkerDeleteCommand(TypicalIndexes.INDEX_FIRST_WORKER));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                "%1$s" + WorkerDeleteCommand.MESSAGE_USAGE);
        String invalidIndexExpectedMessage = String.format(expectedMessage,
                String.format(Messages.MESSAGE_INVALID_DISPLAYED_INDEX, "a"));
        assertParseFailure(parser, "a", invalidIndexExpectedMessage);
    }
}
