package mcscheduler.logic.parser;

import static mcscheduler.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static mcscheduler.logic.commands.CommandTestUtil.INVALID_ROLE_DESC;
import static mcscheduler.logic.commands.CommandTestUtil.ROLE_DESC_CASHIER;
import static mcscheduler.logic.commands.CommandTestUtil.ROLE_DESC_CHEF;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_ROLE_CHEF;

import org.junit.jupiter.api.Test;

import mcscheduler.commons.core.index.Index;
import mcscheduler.logic.commands.WorkerAvailableCommand;
import mcscheduler.model.role.Role;
import mcscheduler.testutil.TypicalIndexes;

public class WorkerAvailableCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
        String.format(MESSAGE_INVALID_COMMAND_FORMAT, WorkerAvailableCommand.MESSAGE_USAGE);

    private WorkerAvailableCommandParser parser = new WorkerAvailableCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        CommandParserTestUtil.assertParseFailure(parser, ROLE_DESC_CASHIER, MESSAGE_INVALID_FORMAT);

        // no field specified
        CommandParserTestUtil.assertParseFailure(parser, "1", MESSAGE_INVALID_FORMAT);

        // no index and no field specified
        CommandParserTestUtil.assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        CommandParserTestUtil.assertParseFailure(parser, "-5" + ROLE_DESC_CASHIER, MESSAGE_INVALID_FORMAT);

        // zero index
        CommandParserTestUtil.assertParseFailure(parser, "0" + ROLE_DESC_CASHIER, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        CommandParserTestUtil.assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        CommandParserTestUtil.assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidRole_failure() {
        CommandParserTestUtil
            .assertParseFailure(parser, "1" + INVALID_ROLE_DESC, Role.MESSAGE_CONSTRAINTS); // invalid tag
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = TypicalIndexes.INDEX_SECOND_WORKER;
        String userInput = targetIndex.getOneBased() + ROLE_DESC_CHEF;
        Role role = Role.createRole(VALID_ROLE_CHEF);
        WorkerAvailableCommand expectedCommand = new WorkerAvailableCommand(targetIndex, role);

        CommandParserTestUtil.assertParseSuccess(parser, userInput, expectedCommand);
    }

}
