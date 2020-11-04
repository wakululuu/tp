package mcscheduler.logic.parser;

import static mcscheduler.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static mcscheduler.logic.commands.CommandTestUtil.INVALID_SHIFT_INDEX;
import static mcscheduler.logic.commands.CommandTestUtil.INVALID_WORKER_INDEX;
import static mcscheduler.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_SHIFT_INDEX_1;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_SHIFT_INDEX_2;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_WORKER_INDEX_1;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_WORKER_INDEX_2;
import static mcscheduler.logic.parser.CommandParserTestUtil.assertParseFailure;
import static mcscheduler.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static mcscheduler.testutil.TypicalIndexes.INDEX_FIRST_SHIFT;
import static mcscheduler.testutil.TypicalIndexes.INDEX_FIRST_WORKER;
import static mcscheduler.testutil.TypicalIndexes.INDEX_SECOND_WORKER;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import mcscheduler.commons.core.Messages;
import mcscheduler.commons.core.index.Index;
import mcscheduler.logic.commands.UnassignCommand;

public class UnassignCommandParserTest {
    private final UnassignCommandParser parser = new UnassignCommandParser();

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

        // add index for mass ops
        workerIndex.add(INDEX_SECOND_WORKER);

        // multiple worker indexes - mass ops
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
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, "%1$s" + UnassignCommand.MESSAGE_USAGE);
        String invalidIndexErrorMessage = String.format(expectedMessage,
                String.format(Messages.MESSAGE_INVALID_DISPLAYED_INDEX, "a"));

        // invalid shift index
        assertParseFailure(parser, INVALID_SHIFT_INDEX + VALID_WORKER_INDEX_1, invalidIndexErrorMessage);

        // invalid worker index
        assertParseFailure(parser, VALID_SHIFT_INDEX_1 + INVALID_WORKER_INDEX, invalidIndexErrorMessage);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_SHIFT_INDEX + VALID_WORKER_INDEX_1, invalidIndexErrorMessage);
    }

}
