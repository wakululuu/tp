package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ROLE;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_SHIFT_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_WORKER_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_CASHIER;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_CHEF;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SHIFT_INDEX_1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SHIFT_INDEX_2;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WORKER_INDEX_1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WORKER_INDEX_2;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SHIFT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_WORKER;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_WORKER;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AssignCommand;
import seedu.address.model.assignment.WorkerRolePair;
import seedu.address.model.tag.Role;


public class AssignCommandParserTest {
    private AssignCommandParser parser = new AssignCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Set<WorkerRolePair> validWorkerRole = new HashSet<>();
        validWorkerRole.add(new WorkerRolePair(INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CASHIER)));

        // whitespace only preamble
        assertParseSuccess(parser,
                PREAMBLE_WHITESPACE + VALID_SHIFT_INDEX_1 + VALID_WORKER_INDEX_1 + " " + VALID_ROLE_CASHIER,
                new AssignCommand(INDEX_FIRST_SHIFT, validWorkerRole));

        // different order
        assertParseSuccess(parser, VALID_WORKER_INDEX_1 + " " + VALID_ROLE_CASHIER + VALID_SHIFT_INDEX_1,
                new AssignCommand(INDEX_FIRST_SHIFT, validWorkerRole));

        // multiple shift indexes - last shift index accepted
        assertParseSuccess(parser,
                VALID_SHIFT_INDEX_2 + VALID_SHIFT_INDEX_1 + VALID_WORKER_INDEX_1 + " " + VALID_ROLE_CASHIER,
                new AssignCommand(INDEX_FIRST_SHIFT, validWorkerRole));

        // add pair for mass ops
        validWorkerRole.add(new WorkerRolePair(INDEX_SECOND_WORKER, Role.createRole(VALID_ROLE_CHEF)));

        // multiple worker-role indexes - mass ops
        assertParseSuccess(parser,
                VALID_SHIFT_INDEX_1 + VALID_WORKER_INDEX_1 + " " + VALID_ROLE_CASHIER
                        + VALID_WORKER_INDEX_2 + " " + VALID_ROLE_CHEF,
                new AssignCommand(INDEX_FIRST_SHIFT, validWorkerRole));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignCommand.MESSAGE_USAGE);

        // missing shift prefix
        assertParseFailure(parser,
                INDEX_FIRST_SHIFT + VALID_WORKER_INDEX_1 + VALID_ROLE_CASHIER, expectedMessage);

        // missing worker-role prefix
        assertParseFailure(parser,
                VALID_SHIFT_INDEX_1 + " " + INDEX_FIRST_WORKER + VALID_ROLE_CASHIER, expectedMessage);

        // both prefixes missing
        assertParseFailure(parser,
                INDEX_FIRST_SHIFT + " " + INDEX_FIRST_WORKER + VALID_ROLE_CASHIER, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignCommand.MESSAGE_USAGE);

        // invalid shift index
        assertParseFailure(parser, INVALID_SHIFT_INDEX + VALID_WORKER_INDEX_1 + " " + VALID_ROLE_CHEF,
                expectedMessage);

        // invalid worker index
        assertParseFailure(parser, VALID_SHIFT_INDEX_1 + INVALID_WORKER_INDEX + " " + VALID_ROLE_CHEF,
                expectedMessage);

        // multiple indexes in worker-role prefix
        assertParseFailure(parser, VALID_SHIFT_INDEX_1 + VALID_WORKER_INDEX_1 + " "
                + INDEX_SECOND_WORKER + VALID_ROLE_CASHIER, expectedMessage);

        // invalid worker-role regex
        assertParseFailure(parser, VALID_SHIFT_INDEX_1 + VALID_WORKER_INDEX_1 + " " + INVALID_ROLE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignCommand.MESSAGE_USAGE));

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_SHIFT_INDEX + VALID_WORKER_INDEX_1 + " " + INVALID_ROLE,
                expectedMessage);
    }
}
