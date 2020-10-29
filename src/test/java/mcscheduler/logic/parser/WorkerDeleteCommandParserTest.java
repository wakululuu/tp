package mcscheduler.logic.parser;

import static mcscheduler.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import org.junit.jupiter.api.Test;

import mcscheduler.testutil.*;
import mcscheduler.logic.commands.WorkerDeleteCommand;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the WorkerDeleteCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the WorkerDeleteCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class WorkerDeleteCommandParserTest {

    private DeleteCommandParser parser = new DeleteCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteCommand() {
        CommandParserTestUtil.assertParseSuccess(parser, "1", new WorkerDeleteCommand(TypicalIndexes.INDEX_FIRST_WORKER));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        CommandParserTestUtil.assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                WorkerDeleteCommand.MESSAGE_USAGE));
    }
}
