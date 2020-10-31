package mcscheduler.logic.parser;

import static mcscheduler.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static mcscheduler.logic.commands.CommandTestUtil.INVALID_ROLE;
import static mcscheduler.logic.commands.CommandTestUtil.INVALID_SHIFT_INDEX;
import static mcscheduler.logic.commands.CommandTestUtil.INVALID_WORKER_INDEX;
import static mcscheduler.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_ROLE_CASHIER;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_ROLE_CHEF;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_SHIFT_INDEX_1;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_SHIFT_INDEX_2;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_WORKER_INDEX_1;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_WORKER_INDEX_2;
import static mcscheduler.logic.parser.CommandParserTestUtil.assertParseFailure;
import static mcscheduler.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import mcscheduler.commons.core.Messages;
import mcscheduler.logic.commands.AssignCommand;
import mcscheduler.model.assignment.WorkerRolePair;
import mcscheduler.model.role.Role;
import mcscheduler.testutil.TypicalIndexes;


public class AssignCommandParserTest {
    private final AssignCommandParser parser = new AssignCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Set<WorkerRolePair> validWorkerRole = new HashSet<>();
        validWorkerRole.add(new WorkerRolePair(TypicalIndexes.INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CASHIER)));

        // whitespace only preamble
        assertParseSuccess(parser,
            PREAMBLE_WHITESPACE + VALID_SHIFT_INDEX_1 + VALID_WORKER_INDEX_1 + " " + VALID_ROLE_CASHIER,
            new AssignCommand(TypicalIndexes.INDEX_FIRST_SHIFT, validWorkerRole));

        // different order
        assertParseSuccess(parser, VALID_WORKER_INDEX_1 + " " + VALID_ROLE_CASHIER + VALID_SHIFT_INDEX_1,
            new AssignCommand(TypicalIndexes.INDEX_FIRST_SHIFT, validWorkerRole));

        // multiple shift indexes - last shift index accepted
        assertParseSuccess(parser,
            VALID_SHIFT_INDEX_2 + VALID_SHIFT_INDEX_1 + VALID_WORKER_INDEX_1 + " " + VALID_ROLE_CASHIER,
            new AssignCommand(TypicalIndexes.INDEX_FIRST_SHIFT, validWorkerRole));

        // add pair for mass ops
        validWorkerRole.add(new WorkerRolePair(TypicalIndexes.INDEX_SECOND_WORKER, Role.createRole(VALID_ROLE_CHEF)));

        // multiple worker-role indexes - mass ops
        assertParseSuccess(parser,
            VALID_SHIFT_INDEX_1 + VALID_WORKER_INDEX_1 + " " + VALID_ROLE_CASHIER
                + VALID_WORKER_INDEX_2 + " " + VALID_ROLE_CHEF,
            new AssignCommand(TypicalIndexes.INDEX_FIRST_SHIFT, validWorkerRole));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignCommand.MESSAGE_USAGE);

        // missing shift prefix
        assertParseFailure(parser,
            TypicalIndexes.INDEX_FIRST_SHIFT + VALID_WORKER_INDEX_1 + VALID_ROLE_CASHIER, expectedMessage);

        // missing worker-role prefix
        assertParseFailure(parser,
            VALID_SHIFT_INDEX_1 + " " + TypicalIndexes.INDEX_FIRST_WORKER + VALID_ROLE_CASHIER, expectedMessage);

        // both prefixes missing
        assertParseFailure(parser,
            TypicalIndexes.INDEX_FIRST_SHIFT + " " + TypicalIndexes.INDEX_FIRST_WORKER + VALID_ROLE_CASHIER,
            expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, "%1$s" + AssignCommand.MESSAGE_USAGE);
        String invalidIndexExpectedMessage = String.format(expectedMessage,
                String.format(Messages.MESSAGE_INVALID_DISPLAYED_INDEX, "a"));

        // invalid shift index
        assertParseFailure(parser, INVALID_SHIFT_INDEX + VALID_WORKER_INDEX_1 + " " + VALID_ROLE_CHEF,
                invalidIndexExpectedMessage);

        // invalid worker index
        assertParseFailure(parser, VALID_SHIFT_INDEX_1 + INVALID_WORKER_INDEX + " " + VALID_ROLE_CHEF,
            String.format(expectedMessage, WorkerRolePair.MESSAGE_CONSTRAINTS));

        // multiple indexes in worker-role prefix
        assertParseFailure(parser, VALID_SHIFT_INDEX_1 + VALID_WORKER_INDEX_1 + " "
            + TypicalIndexes.INDEX_SECOND_WORKER + VALID_ROLE_CASHIER,
                String.format(expectedMessage, WorkerRolePair.MESSAGE_CONSTRAINTS));

        // invalid worker-role regex
        assertParseFailure(parser, VALID_SHIFT_INDEX_1 + VALID_WORKER_INDEX_1 + " " + INVALID_ROLE,
            String.format(expectedMessage, WorkerRolePair.MESSAGE_CONSTRAINTS));

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_SHIFT_INDEX + VALID_WORKER_INDEX_1 + " " + INVALID_ROLE,
                invalidIndexExpectedMessage);
    }
}
