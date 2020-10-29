package mcscheduler.logic.parser;

import static mcscheduler.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static mcscheduler.logic.commands.CommandTestUtil.INVALID_SHIFT_INDEX;
import static mcscheduler.logic.commands.CommandTestUtil.INVALID_WORKER_INDEX;
import static mcscheduler.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_SHIFT_INDEX_1;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_SHIFT_INDEX_2;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_WORKER_INDEX_1;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_WORKER_INDEX_2;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import mcscheduler.testutil.*;
import mcscheduler.commons.core.index.Index;
import mcscheduler.logic.commands.UnassignCommand;

public class UnassignCommandParserTest {
    private UnassignCommandParser parser = new UnassignCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Set<Index> workerIndex = new HashSet<>();
        workerIndex.add(TypicalIndexes.INDEX_FIRST_WORKER);

        // whitespace only preamble
        CommandParserTestUtil
            .assertParseSuccess(parser, PREAMBLE_WHITESPACE + VALID_SHIFT_INDEX_1 + VALID_WORKER_INDEX_1,
                new UnassignCommand(TypicalIndexes.INDEX_FIRST_SHIFT, workerIndex));

        // different order
        CommandParserTestUtil.assertParseSuccess(parser, VALID_WORKER_INDEX_1 + VALID_SHIFT_INDEX_1,
                new UnassignCommand(TypicalIndexes.INDEX_FIRST_SHIFT, workerIndex));

        // multiple shift indexes - last shift index accepted
        CommandParserTestUtil
            .assertParseSuccess(parser, VALID_SHIFT_INDEX_2 + VALID_SHIFT_INDEX_1 + VALID_WORKER_INDEX_1,
                new UnassignCommand(TypicalIndexes.INDEX_FIRST_SHIFT, workerIndex));

        // add index for mass ops
        workerIndex.add(TypicalIndexes.INDEX_SECOND_WORKER);

        // multiple worker indexes - mass ops
        CommandParserTestUtil
            .assertParseSuccess(parser, VALID_SHIFT_INDEX_1 + VALID_WORKER_INDEX_2 + VALID_WORKER_INDEX_1,
                new UnassignCommand(TypicalIndexes.INDEX_FIRST_SHIFT, workerIndex));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnassignCommand.MESSAGE_USAGE);

        // missing shift prefix
        CommandParserTestUtil
            .assertParseFailure(parser, TypicalIndexes.INDEX_FIRST_SHIFT + VALID_WORKER_INDEX_1, expectedMessage);

        // missing worker prefix
        CommandParserTestUtil
            .assertParseFailure(parser, VALID_SHIFT_INDEX_1 + " " + TypicalIndexes.INDEX_FIRST_WORKER, expectedMessage);

        // all prefixes missing
        CommandParserTestUtil
            .assertParseFailure(parser, TypicalIndexes.INDEX_FIRST_SHIFT + " " + TypicalIndexes.INDEX_FIRST_WORKER, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnassignCommand.MESSAGE_USAGE);

        // invalid shift index
        CommandParserTestUtil.assertParseFailure(parser, INVALID_SHIFT_INDEX + VALID_WORKER_INDEX_1, expectedMessage);

        // invalid worker index
        CommandParserTestUtil.assertParseFailure(parser, VALID_SHIFT_INDEX_1 + INVALID_WORKER_INDEX, expectedMessage);

        // two invalid values, only first invalid value reported
        CommandParserTestUtil.assertParseFailure(parser, INVALID_SHIFT_INDEX + VALID_WORKER_INDEX_1, expectedMessage);
    }

}
