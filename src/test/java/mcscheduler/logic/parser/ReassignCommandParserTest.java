package mcscheduler.logic.parser;

import static mcscheduler.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static mcscheduler.logic.commands.CommandTestUtil.INVALID_NEW_SHIFT_INDEX;
import static mcscheduler.logic.commands.CommandTestUtil.INVALID_NEW_WORKER_INDEX;
import static mcscheduler.logic.commands.CommandTestUtil.INVALID_OLD_SHIFT_INDEX;
import static mcscheduler.logic.commands.CommandTestUtil.INVALID_OLD_WORKER_INDEX;
import static mcscheduler.logic.commands.CommandTestUtil.INVALID_ROLE_DESC;
import static mcscheduler.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static mcscheduler.logic.commands.CommandTestUtil.ROLE_DESC_CASHIER;
import static mcscheduler.logic.commands.CommandTestUtil.ROLE_DESC_CHEF;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_NEW_SHIFT_INDEX_1;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_NEW_SHIFT_INDEX_2;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_NEW_WORKER_INDEX_1;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_NEW_WORKER_INDEX_2;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_OLD_SHIFT_INDEX_1;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_OLD_WORKER_INDEX_1;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_ROLE_CASHIER;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_ROLE_CHEF;

import org.junit.jupiter.api.Test;

import mcscheduler.testutil.*;
import mcscheduler.logic.commands.ReassignCommand;
import mcscheduler.model.tag.Role;

public class ReassignCommandParserTest {
    private ReassignCommandParser parser = new ReassignCommandParser();
    @Test
    public void parse_allFieldsPresent_success() {
        // whitespace only preamble
        CommandParserTestUtil
            .assertParseSuccess(parser, PREAMBLE_WHITESPACE + VALID_OLD_WORKER_INDEX_1 + VALID_NEW_WORKER_INDEX_2
                        + VALID_OLD_SHIFT_INDEX_1 + VALID_NEW_SHIFT_INDEX_2 + ROLE_DESC_CASHIER,
                new ReassignCommand(TypicalIndexes.INDEX_FIRST_WORKER, TypicalIndexes.INDEX_SECOND_WORKER, TypicalIndexes.INDEX_FIRST_SHIFT,
                        TypicalIndexes.INDEX_SECOND_SHIFT, Role.createRole(VALID_ROLE_CASHIER)));

        // different order
        CommandParserTestUtil.assertParseSuccess(parser, VALID_NEW_WORKER_INDEX_2 + VALID_OLD_WORKER_INDEX_1
                        + VALID_OLD_SHIFT_INDEX_1 + ROLE_DESC_CASHIER + VALID_NEW_SHIFT_INDEX_2,
                new ReassignCommand(TypicalIndexes.INDEX_FIRST_WORKER, TypicalIndexes.INDEX_SECOND_WORKER, TypicalIndexes.INDEX_FIRST_SHIFT,
                        TypicalIndexes.INDEX_SECOND_SHIFT, Role.createRole(VALID_ROLE_CASHIER)));

        // multiple new shift indexes - last shift index accepted
        CommandParserTestUtil.assertParseSuccess(parser, VALID_OLD_WORKER_INDEX_1 + VALID_NEW_WORKER_INDEX_2
                        + VALID_OLD_SHIFT_INDEX_1 + VALID_NEW_SHIFT_INDEX_2 + VALID_NEW_SHIFT_INDEX_1
                        + ROLE_DESC_CASHIER,
                new ReassignCommand(TypicalIndexes.INDEX_FIRST_WORKER, TypicalIndexes.INDEX_SECOND_WORKER, TypicalIndexes.INDEX_FIRST_SHIFT,
                        TypicalIndexes.INDEX_FIRST_SHIFT, Role.createRole(VALID_ROLE_CASHIER)));

        // multiple new worker indexes - last worker index accepted
        CommandParserTestUtil.assertParseSuccess(parser,
                VALID_OLD_WORKER_INDEX_1 + VALID_NEW_WORKER_INDEX_2 + VALID_NEW_WORKER_INDEX_1
                        + VALID_OLD_SHIFT_INDEX_1 + VALID_NEW_SHIFT_INDEX_2 + ROLE_DESC_CASHIER,
                new ReassignCommand(
                    TypicalIndexes.INDEX_FIRST_WORKER, TypicalIndexes.INDEX_FIRST_WORKER, TypicalIndexes.INDEX_FIRST_SHIFT,
                        TypicalIndexes.INDEX_SECOND_SHIFT, Role.createRole(VALID_ROLE_CASHIER)));

        // multiple roles - last role accepted
        CommandParserTestUtil.assertParseSuccess(parser, VALID_OLD_WORKER_INDEX_1 + VALID_NEW_WORKER_INDEX_2
                        + VALID_OLD_SHIFT_INDEX_1 + VALID_NEW_SHIFT_INDEX_2 + ROLE_DESC_CASHIER + ROLE_DESC_CHEF,
                new ReassignCommand(TypicalIndexes.INDEX_FIRST_WORKER, TypicalIndexes.INDEX_SECOND_WORKER, TypicalIndexes.INDEX_FIRST_SHIFT,
                        TypicalIndexes.INDEX_SECOND_SHIFT, Role.createRole(VALID_ROLE_CHEF)));
    }
    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ReassignCommand.MESSAGE_USAGE);

        // missing old worker prefix
        CommandParserTestUtil.assertParseFailure(parser, VALID_NEW_WORKER_INDEX_2
                + VALID_OLD_SHIFT_INDEX_1 + VALID_NEW_SHIFT_INDEX_2 + ROLE_DESC_CASHIER, expectedMessage);

        // missing new worker prefix
        CommandParserTestUtil.assertParseFailure(parser, VALID_OLD_WORKER_INDEX_1
                + VALID_OLD_SHIFT_INDEX_1 + VALID_NEW_SHIFT_INDEX_2 + ROLE_DESC_CASHIER, expectedMessage);

        //missing old shift prefix
        CommandParserTestUtil.assertParseFailure(parser, VALID_OLD_WORKER_INDEX_1 + VALID_NEW_WORKER_INDEX_2
                + VALID_NEW_SHIFT_INDEX_2 + ROLE_DESC_CASHIER, expectedMessage);

        //missing new shift prefix
        CommandParserTestUtil.assertParseFailure(parser, VALID_OLD_WORKER_INDEX_1 + VALID_NEW_WORKER_INDEX_2
                + VALID_OLD_SHIFT_INDEX_1 + ROLE_DESC_CASHIER, expectedMessage);

        // missing role prefix
        CommandParserTestUtil.assertParseFailure(parser, VALID_OLD_WORKER_INDEX_1 + VALID_NEW_WORKER_INDEX_2
                        + VALID_OLD_SHIFT_INDEX_1 + VALID_NEW_SHIFT_INDEX_2,
                expectedMessage);

        // all prefixes missing
        CommandParserTestUtil
            .assertParseFailure(parser, TypicalIndexes.INDEX_FIRST_SHIFT + " " + TypicalIndexes.INDEX_FIRST_WORKER + VALID_ROLE_CASHIER, expectedMessage);
    }
    @Test
    public void parse_invalidValue_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ReassignCommand.MESSAGE_USAGE);

        // invalid new shift index
        CommandParserTestUtil.assertParseFailure(parser, VALID_OLD_WORKER_INDEX_1 + VALID_NEW_WORKER_INDEX_2
                        + VALID_OLD_SHIFT_INDEX_1 + INVALID_NEW_SHIFT_INDEX + ROLE_DESC_CASHIER,
                expectedMessage);

        // invalid old shift index
        CommandParserTestUtil.assertParseFailure(parser, VALID_OLD_WORKER_INDEX_1 + VALID_NEW_WORKER_INDEX_2
                        + INVALID_OLD_SHIFT_INDEX + VALID_NEW_SHIFT_INDEX_1 + ROLE_DESC_CASHIER,
                expectedMessage);

        // invalid new worker index
        CommandParserTestUtil.assertParseFailure(parser, VALID_OLD_WORKER_INDEX_1 + INVALID_NEW_WORKER_INDEX
                        + VALID_OLD_SHIFT_INDEX_1 + VALID_NEW_SHIFT_INDEX_1 + ROLE_DESC_CASHIER,
                expectedMessage);

        // invalid old worker index
        CommandParserTestUtil.assertParseFailure(parser, INVALID_OLD_WORKER_INDEX + VALID_NEW_WORKER_INDEX_1
                        + VALID_OLD_SHIFT_INDEX_1 + VALID_NEW_SHIFT_INDEX_1 + ROLE_DESC_CASHIER,
                expectedMessage);

        // invalid role
        CommandParserTestUtil.assertParseFailure(parser, VALID_OLD_WORKER_INDEX_1 + VALID_NEW_WORKER_INDEX_1
                        + VALID_OLD_SHIFT_INDEX_1 + VALID_NEW_SHIFT_INDEX_1 + INVALID_ROLE_DESC,
                Role.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        CommandParserTestUtil.assertParseFailure(parser, INVALID_OLD_WORKER_INDEX + VALID_NEW_WORKER_INDEX_1
                        + VALID_OLD_SHIFT_INDEX_1 + VALID_NEW_SHIFT_INDEX_1 + INVALID_ROLE_DESC,
                expectedMessage);
    }
}
