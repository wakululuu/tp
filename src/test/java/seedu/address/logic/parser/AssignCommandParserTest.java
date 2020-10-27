package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ROLE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_SHIFT_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_WORKER_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.ROLE_DESC_CASHIER;
import static seedu.address.logic.commands.CommandTestUtil.ROLE_DESC_CHEF;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_CASHIER;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SHIFT_INDEX_1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SHIFT_INDEX_2;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WORKER_INDEX_1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WORKER_INDEX_2;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SHIFT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_WORKER;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AssignCommand;
import seedu.address.model.tag.Role;

public class AssignCommandParserTest {
    private AssignCommandParser parser = new AssignCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + VALID_SHIFT_INDEX_1 + VALID_WORKER_INDEX_1 + ROLE_DESC_CASHIER,
                new AssignCommand(INDEX_FIRST_SHIFT, INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CASHIER)));

        // different order
        assertParseSuccess(parser, VALID_WORKER_INDEX_1 + VALID_SHIFT_INDEX_1 + ROLE_DESC_CASHIER,
                new AssignCommand(INDEX_FIRST_SHIFT, INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CASHIER)));

        // multiple shift indexes - last shift index accepted
        assertParseSuccess(parser, VALID_SHIFT_INDEX_2 + VALID_SHIFT_INDEX_1 + VALID_WORKER_INDEX_1 + ROLE_DESC_CASHIER,
                new AssignCommand(INDEX_FIRST_SHIFT, INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CASHIER)));

        // multiple worker indexes - last worker index accepted
        assertParseSuccess(parser, VALID_SHIFT_INDEX_1 + VALID_WORKER_INDEX_2 + VALID_WORKER_INDEX_1
                + ROLE_DESC_CASHIER, new AssignCommand(INDEX_FIRST_SHIFT, INDEX_FIRST_WORKER,
                Role.createRole(VALID_ROLE_CASHIER)));

        // multiple roles - last role accepted
        assertParseSuccess(parser, VALID_SHIFT_INDEX_1 + VALID_WORKER_INDEX_1 + ROLE_DESC_CHEF + ROLE_DESC_CASHIER,
                new AssignCommand(INDEX_FIRST_SHIFT, INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CASHIER)));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignCommand.MESSAGE_USAGE);

        // missing shift prefix
        assertParseFailure(parser, INDEX_FIRST_SHIFT + VALID_WORKER_INDEX_1 + ROLE_DESC_CASHIER, expectedMessage);

        // missing worker prefix
        assertParseFailure(parser, VALID_SHIFT_INDEX_1 + " " + INDEX_FIRST_WORKER + ROLE_DESC_CASHIER,
                expectedMessage);

        // missing role prefix
        assertParseFailure(parser, VALID_SHIFT_INDEX_1 + VALID_WORKER_INDEX_1 + " " + VALID_ROLE_CASHIER,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, INDEX_FIRST_SHIFT + " " + INDEX_FIRST_WORKER + VALID_ROLE_CASHIER, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignCommand.MESSAGE_USAGE);

        // invalid shift index
        assertParseFailure(parser, INVALID_SHIFT_INDEX + VALID_WORKER_INDEX_1 + ROLE_DESC_CHEF,
                expectedMessage);

        // invalid worker index
        assertParseFailure(parser, VALID_SHIFT_INDEX_1 + INVALID_WORKER_INDEX + ROLE_DESC_CHEF,
                expectedMessage);

        // invalid role
        assertParseFailure(parser, VALID_SHIFT_INDEX_1 + VALID_WORKER_INDEX_1 + INVALID_ROLE_DESC,
                Role.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_SHIFT_INDEX + VALID_WORKER_INDEX_1 + INVALID_ROLE_DESC,
                expectedMessage);
    }
}
