package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_SHIFT_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_WORKER_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SHIFT_INDEX_1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SHIFT_INDEX_2;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WORKER_INDEX_1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WORKER_INDEX_2;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SHIFT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_WORKER;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UnassignCommand;

import java.util.HashSet;
import java.util.Set;

public class UnassignCommandParserTest {
    private UnassignCommandParser parser = new UnassignCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Set<Index> workerIndex = new HashSet<>();
        workerIndex.add(INDEX_FIRST_WORKER);

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + VALID_SHIFT_INDEX_1 + VALID_WORKER_INDEX_1,
                new UnassignCommand(INDEX_FIRST_SHIFT, workerIndex));

        // different order
        assertParseSuccess(parser, VALID_WORKER_INDEX_1 + VALID_SHIFT_INDEX_1,
                new UnassignCommand(INDEX_FIRST_SHIFT, workerIndex));

        // multiple shift indexes - last shift index accepted
        assertParseSuccess(parser, VALID_SHIFT_INDEX_2 + VALID_SHIFT_INDEX_1 + VALID_WORKER_INDEX_1,
                new UnassignCommand(INDEX_FIRST_SHIFT, workerIndex));

        // multiple worker indexes - last worker index accepted
        assertParseSuccess(parser, VALID_SHIFT_INDEX_1 + VALID_WORKER_INDEX_2 + VALID_WORKER_INDEX_1,
                new UnassignCommand(INDEX_FIRST_SHIFT, workerIndex));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnassignCommand.MESSAGE_USAGE);

        // missing shift prefix
        assertParseFailure(parser, INDEX_FIRST_SHIFT + VALID_WORKER_INDEX_1, expectedMessage);

        // missing worker prefix
        assertParseFailure(parser, VALID_SHIFT_INDEX_1 + " " + INDEX_FIRST_WORKER, expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, INDEX_FIRST_SHIFT + " " + INDEX_FIRST_WORKER, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnassignCommand.MESSAGE_USAGE);

        // invalid shift index
        assertParseFailure(parser, INVALID_SHIFT_INDEX + VALID_WORKER_INDEX_1, expectedMessage);

        // invalid worker index
        assertParseFailure(parser, VALID_SHIFT_INDEX_1 + INVALID_WORKER_INDEX, expectedMessage);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_SHIFT_INDEX + VALID_WORKER_INDEX_1, expectedMessage);
    }

}
