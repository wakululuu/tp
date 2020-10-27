package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_SHIFT_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_WORKER_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SHIFT_INDEX_1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WORKER_INDEX_1;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SHIFT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_WORKER;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CancelLeaveCommand;
import seedu.address.logic.commands.TakeLeaveCommand;

public class CancelLeaveCommandParserTest {
    private CancelLeaveCommandParser parser = new CancelLeaveCommandParser();

    @Test
    public void parse_allFieldPresent_success() {
        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + VALID_WORKER_INDEX_1 + VALID_SHIFT_INDEX_1,
                new CancelLeaveCommand(INDEX_FIRST_SHIFT, INDEX_FIRST_WORKER));

        // different order
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + VALID_SHIFT_INDEX_1 + VALID_WORKER_INDEX_1,
                new CancelLeaveCommand(INDEX_FIRST_SHIFT, INDEX_FIRST_WORKER));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, CancelLeaveCommand.MESSAGE_USAGE);

        assertParseFailure(parser, INDEX_FIRST_SHIFT + VALID_WORKER_INDEX_1, expectedMessage);
        assertParseFailure(parser, VALID_SHIFT_INDEX_1 + " " + INDEX_FIRST_WORKER, expectedMessage);
        assertParseFailure(parser, INDEX_FIRST_SHIFT + " " + INDEX_FIRST_WORKER, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, CancelLeaveCommand.MESSAGE_USAGE);

        assertParseFailure(parser, INVALID_SHIFT_INDEX + VALID_WORKER_INDEX_1, expectedMessage);
        assertParseFailure(parser, VALID_SHIFT_INDEX_1 + INVALID_WORKER_INDEX, expectedMessage);
        assertParseFailure(parser, INVALID_SHIFT_INDEX + INVALID_WORKER_INDEX, expectedMessage);
    }
}
