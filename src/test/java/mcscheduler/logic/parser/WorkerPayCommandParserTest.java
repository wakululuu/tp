package mcscheduler.logic.parser;

import static mcscheduler.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import org.junit.jupiter.api.Test;

import mcscheduler.commons.core.Messages;
import mcscheduler.logic.commands.WorkerPayCommand;
import mcscheduler.testutil.TypicalIndexes;

//@@author plosslaw
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
