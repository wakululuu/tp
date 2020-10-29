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

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.TakeLeaveCommand;

public class TakeLeaveCommandParserTest {
    private TakeLeaveCommandParser parser = new TakeLeaveCommandParser();

    @Test
    public void parse_allFieldPresent_success() {
        Set<Index> validIndex = new HashSet<>();
        validIndex.add(INDEX_FIRST_WORKER);
        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + VALID_WORKER_INDEX_1 + VALID_SHIFT_INDEX_1,
                new TakeLeaveCommand(INDEX_FIRST_SHIFT, validIndex));

        // different order
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + VALID_SHIFT_INDEX_1 + VALID_WORKER_INDEX_1,
                new TakeLeaveCommand(INDEX_FIRST_SHIFT, validIndex));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, TakeLeaveCommand.MESSAGE_USAGE);

        assertParseFailure(parser, INDEX_FIRST_SHIFT + VALID_WORKER_INDEX_1, expectedMessage);
        assertParseFailure(parser, VALID_SHIFT_INDEX_1 + " " + INDEX_FIRST_WORKER, expectedMessage);
        assertParseFailure(parser, INDEX_FIRST_SHIFT + " " + INDEX_FIRST_WORKER, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, TakeLeaveCommand.MESSAGE_USAGE);

        assertParseFailure(parser, INVALID_SHIFT_INDEX + VALID_WORKER_INDEX_1, expectedMessage);
        assertParseFailure(parser, VALID_SHIFT_INDEX_1 + INVALID_WORKER_INDEX, expectedMessage);
        assertParseFailure(parser, INVALID_SHIFT_INDEX + INVALID_WORKER_INDEX, expectedMessage);
    }
}
