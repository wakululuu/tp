package mcscheduler.logic.parser;

import static mcscheduler.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import org.junit.jupiter.api.Test;

import mcscheduler.commons.core.Messages;
import mcscheduler.logic.commands.WorkerPayCommand;
import mcscheduler.testutil.TypicalIndexes;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the WorkerDeleteCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the WorkerDeleteCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class WorkerPayCommandParserTest {

    private final WorkerPayCommandParser parser = new WorkerPayCommandParser();

    @Test
    public void parse_validArgs_returnsWorkerPayCommand() {
        CommandParserTestUtil.assertParseSuccess(parser, "1", new WorkerPayCommand(TypicalIndexes.INDEX_FIRST_WORKER));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, "%1$s" + WorkerPayCommand.MESSAGE_USAGE);
        String invalidIndexExpectedMessage = String.format(expectedMessage,
                String.format(Messages.MESSAGE_INVALID_DISPLAYED_INDEX, "a"));
        CommandParserTestUtil.assertParseFailure(parser, "a", invalidIndexExpectedMessage);
    }
}
