package mcscheduler.logic.parser;

import static mcscheduler.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static mcscheduler.logic.commands.CommandTestUtil.INVALID_SHIFT_INDEX;
import static mcscheduler.logic.commands.CommandTestUtil.INVALID_WORKER_INDEX;
import static mcscheduler.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_SHIFT_INDEX_1;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_WORKER_INDEX_1;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import mcscheduler.commons.core.Messages;
import mcscheduler.commons.core.index.Index;
import mcscheduler.logic.commands.TakeLeaveCommand;
import mcscheduler.testutil.TypicalIndexes;

//@@author WangZijun97
public class TakeLeaveCommandParserTest {
    private final TakeLeaveCommandParser parser = new TakeLeaveCommandParser();

    @Test
    public void parse_allFieldPresent_success() {
        Set<Index> validIndex = new HashSet<>();
        validIndex.add(TypicalIndexes.INDEX_FIRST_WORKER);
        // whitespace only preamble
        CommandParserTestUtil
            .assertParseSuccess(parser, PREAMBLE_WHITESPACE + VALID_WORKER_INDEX_1 + VALID_SHIFT_INDEX_1,
                new TakeLeaveCommand(TypicalIndexes.INDEX_FIRST_SHIFT, validIndex));

        // different order
        CommandParserTestUtil
            .assertParseSuccess(parser, PREAMBLE_WHITESPACE + VALID_SHIFT_INDEX_1 + VALID_WORKER_INDEX_1,
                new TakeLeaveCommand(TypicalIndexes.INDEX_FIRST_SHIFT, validIndex));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, TakeLeaveCommand.MESSAGE_USAGE);

        CommandParserTestUtil
            .assertParseFailure(parser, TypicalIndexes.INDEX_FIRST_SHIFT + VALID_WORKER_INDEX_1, expectedMessage);
        CommandParserTestUtil
            .assertParseFailure(parser, VALID_SHIFT_INDEX_1 + " " + TypicalIndexes.INDEX_FIRST_WORKER, expectedMessage);
        CommandParserTestUtil
            .assertParseFailure(parser, TypicalIndexes.INDEX_FIRST_SHIFT + " " + TypicalIndexes.INDEX_FIRST_WORKER,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, "%1$s" + TakeLeaveCommand.MESSAGE_USAGE);
        String invalidIndexExpectedMessage = String.format(expectedMessage,
                String.format(Messages.MESSAGE_INVALID_DISPLAYED_INDEX, "a"));

        CommandParserTestUtil.assertParseFailure(parser, INVALID_SHIFT_INDEX + VALID_WORKER_INDEX_1,
                invalidIndexExpectedMessage);
        CommandParserTestUtil.assertParseFailure(parser, VALID_SHIFT_INDEX_1 + INVALID_WORKER_INDEX,
                invalidIndexExpectedMessage);
        CommandParserTestUtil.assertParseFailure(parser, INVALID_SHIFT_INDEX + INVALID_WORKER_INDEX,
                invalidIndexExpectedMessage);
    }
}
