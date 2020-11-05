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
import static mcscheduler.logic.commands.CommandTestUtil.VALID_SHIFT_INDEX_1;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_WORKER_INDEX_1;
import static mcscheduler.logic.parser.CommandParserTestUtil.assertParseFailure;
import static mcscheduler.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static mcscheduler.testutil.TypicalIndexes.INDEX_FIRST_SHIFT;
import static mcscheduler.testutil.TypicalIndexes.INDEX_FIRST_WORKER;
import static mcscheduler.testutil.TypicalIndexes.INDEX_SECOND_SHIFT;
import static mcscheduler.testutil.TypicalIndexes.INDEX_SECOND_WORKER;

import org.junit.jupiter.api.Test;

import mcscheduler.commons.core.Messages;
import mcscheduler.logic.commands.ReassignCommand;
import mcscheduler.model.role.Role;

//@@author tnsyn
public class ReassignCommandParserTest {
    private final ReassignCommandParser parser = new ReassignCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + VALID_OLD_WORKER_INDEX_1 + VALID_NEW_WORKER_INDEX_2
                + VALID_OLD_SHIFT_INDEX_1 + VALID_NEW_SHIFT_INDEX_2 + ROLE_DESC_CASHIER,
                new ReassignCommand(INDEX_FIRST_WORKER, INDEX_SECOND_WORKER, INDEX_FIRST_SHIFT, INDEX_SECOND_SHIFT,
                        Role.createRole(VALID_ROLE_CASHIER)));

        // different order
        assertParseSuccess(parser, VALID_NEW_WORKER_INDEX_2 + VALID_OLD_WORKER_INDEX_1 + VALID_OLD_SHIFT_INDEX_1
                + ROLE_DESC_CASHIER + VALID_NEW_SHIFT_INDEX_2,
                new ReassignCommand(INDEX_FIRST_WORKER, INDEX_SECOND_WORKER, INDEX_FIRST_SHIFT, INDEX_SECOND_SHIFT,
                        Role.createRole(VALID_ROLE_CASHIER)));

        // multiple new shift indexes - last shift index accepted
        assertParseSuccess(parser, VALID_OLD_WORKER_INDEX_1 + VALID_NEW_WORKER_INDEX_2 + VALID_OLD_SHIFT_INDEX_1
                + VALID_NEW_SHIFT_INDEX_2 + VALID_NEW_SHIFT_INDEX_1 + ROLE_DESC_CASHIER,
                new ReassignCommand(INDEX_FIRST_WORKER, INDEX_SECOND_WORKER, INDEX_FIRST_SHIFT, INDEX_FIRST_SHIFT,
                        Role.createRole(VALID_ROLE_CASHIER)));

        // multiple new worker indexes - last worker index accepted
        assertParseSuccess(parser, VALID_OLD_WORKER_INDEX_1 + VALID_NEW_WORKER_INDEX_2 + VALID_NEW_WORKER_INDEX_1
                + VALID_OLD_SHIFT_INDEX_1 + VALID_NEW_SHIFT_INDEX_2 + ROLE_DESC_CASHIER,
                new ReassignCommand(INDEX_FIRST_WORKER, INDEX_FIRST_WORKER, INDEX_FIRST_SHIFT, INDEX_SECOND_SHIFT,
                        Role.createRole(VALID_ROLE_CASHIER)));

        // multiple roles - last role accepted
        assertParseSuccess(parser, VALID_OLD_WORKER_INDEX_1 + VALID_NEW_WORKER_INDEX_2 + VALID_OLD_SHIFT_INDEX_1
                + VALID_NEW_SHIFT_INDEX_2 + ROLE_DESC_CASHIER + ROLE_DESC_CHEF,
                new ReassignCommand(INDEX_FIRST_WORKER, INDEX_SECOND_WORKER, INDEX_FIRST_SHIFT, INDEX_SECOND_SHIFT,
                        Role.createRole(VALID_ROLE_CHEF)));

        // worker index and shift index provided for reassign within 1 shift
        assertParseSuccess(parser, VALID_OLD_WORKER_INDEX_1 + VALID_NEW_WORKER_INDEX_2 + VALID_OLD_SHIFT_INDEX_1
                        + VALID_NEW_SHIFT_INDEX_2 + ROLE_DESC_CASHIER + ROLE_DESC_CHEF,
                new ReassignCommand(INDEX_FIRST_WORKER, INDEX_SECOND_WORKER, INDEX_FIRST_SHIFT, INDEX_SECOND_SHIFT,
                        Role.createRole(VALID_ROLE_CHEF)));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ReassignCommand.MESSAGE_USAGE);

        // missing old worker prefix
        assertParseFailure(parser, VALID_NEW_WORKER_INDEX_2 + VALID_OLD_SHIFT_INDEX_1 + VALID_NEW_SHIFT_INDEX_2
                + ROLE_DESC_CASHIER, expectedMessage);

        // missing new worker prefix
        assertParseFailure(parser, VALID_OLD_WORKER_INDEX_1 + VALID_OLD_SHIFT_INDEX_1 + VALID_NEW_SHIFT_INDEX_2
                + ROLE_DESC_CASHIER, expectedMessage);

        //missing old shift prefix
        assertParseFailure(parser, VALID_OLD_WORKER_INDEX_1 + VALID_NEW_WORKER_INDEX_2 + VALID_NEW_SHIFT_INDEX_2
                + ROLE_DESC_CASHIER, expectedMessage);

        //missing new shift prefix
        assertParseFailure(parser, VALID_OLD_WORKER_INDEX_1 + VALID_NEW_WORKER_INDEX_2 + VALID_OLD_SHIFT_INDEX_1
                + ROLE_DESC_CASHIER, expectedMessage);

        // missing role prefix
        assertParseFailure(parser, VALID_OLD_WORKER_INDEX_1 + VALID_NEW_WORKER_INDEX_2 + VALID_OLD_SHIFT_INDEX_1
                + VALID_NEW_SHIFT_INDEX_2, expectedMessage);

        // missing shift index for 3-parameter format
        assertParseFailure(parser, VALID_WORKER_INDEX_1 + ROLE_DESC_CASHIER, expectedMessage);

        // missing shift index for 3-parameter format
        assertParseFailure(parser, VALID_SHIFT_INDEX_1 + ROLE_DESC_CASHIER, expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, INDEX_FIRST_SHIFT + " " + INDEX_FIRST_WORKER + VALID_ROLE_CASHIER, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, "%1$s" + ReassignCommand.MESSAGE_USAGE);
        String invalidIndexExpectedMessage = String.format(expectedMessage,
                String.format(Messages.MESSAGE_INVALID_DISPLAYED_INDEX, "a"));

        // invalid new shift index
        assertParseFailure(parser, VALID_OLD_WORKER_INDEX_1 + VALID_NEW_WORKER_INDEX_2 + VALID_OLD_SHIFT_INDEX_1
                + INVALID_NEW_SHIFT_INDEX + ROLE_DESC_CASHIER, invalidIndexExpectedMessage);

        // invalid old shift index
        assertParseFailure(parser, VALID_OLD_WORKER_INDEX_1 + VALID_NEW_WORKER_INDEX_2 + INVALID_OLD_SHIFT_INDEX
                + VALID_NEW_SHIFT_INDEX_1 + ROLE_DESC_CASHIER, invalidIndexExpectedMessage);

        // invalid new worker index
        assertParseFailure(parser, VALID_OLD_WORKER_INDEX_1 + INVALID_NEW_WORKER_INDEX + VALID_OLD_SHIFT_INDEX_1
                + VALID_NEW_SHIFT_INDEX_1 + ROLE_DESC_CASHIER, invalidIndexExpectedMessage);

        // invalid old worker index
        assertParseFailure(parser, INVALID_OLD_WORKER_INDEX + VALID_NEW_WORKER_INDEX_1 + VALID_OLD_SHIFT_INDEX_1
                + VALID_NEW_SHIFT_INDEX_1 + ROLE_DESC_CASHIER, invalidIndexExpectedMessage);

        // invalid role
        assertParseFailure(parser, VALID_OLD_WORKER_INDEX_1 + VALID_NEW_WORKER_INDEX_1 + VALID_OLD_SHIFT_INDEX_1
                + VALID_NEW_SHIFT_INDEX_1 + INVALID_ROLE_DESC,
            String.format(Messages.MESSAGE_INVALID_PARSE_VALUE,
                "Role", INVALID_ROLE_DESC.substring(3), Role.MESSAGE_CONSTRAINTS));

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_OLD_WORKER_INDEX + VALID_NEW_WORKER_INDEX_1 + VALID_OLD_SHIFT_INDEX_1
                + VALID_NEW_SHIFT_INDEX_1 + INVALID_ROLE_DESC, invalidIndexExpectedMessage);
    }
}
