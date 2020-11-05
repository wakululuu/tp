package mcscheduler.logic.parser;

import static mcscheduler.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static mcscheduler.logic.commands.CommandTestUtil.INVALID_ROLE_DESC;
import static mcscheduler.logic.commands.CommandTestUtil.ROLE_DESC_CASHIER;
import static mcscheduler.logic.commands.CommandTestUtil.ROLE_DESC_CHEF;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_ROLE_CHEF;
import static mcscheduler.logic.parser.CommandParserTestUtil.assertParseFailure;
import static mcscheduler.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import mcscheduler.commons.core.Messages;
import mcscheduler.commons.core.index.Index;
import mcscheduler.logic.commands.WorkerAvailableCommand;
import mcscheduler.model.role.Role;
import mcscheduler.testutil.TypicalIndexes;

//@@author plosslaw
public class WorkerAvailableCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
        String.format(MESSAGE_INVALID_COMMAND_FORMAT, WorkerAvailableCommand.MESSAGE_USAGE);

    private final WorkerAvailableCommandParser parser = new WorkerAvailableCommandParser();

    @Test
    public void parse_missingParts_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                "%1$s" + WorkerAvailableCommand.MESSAGE_USAGE);
        String invalidIndexErrorMessage = String.format(expectedMessage, Messages.MESSAGE_INVALID_DISPLAYED_INDEX);

        // no index specified
        assertParseFailure(parser, ROLE_DESC_CASHIER, String.format(invalidIndexErrorMessage, ""));

        // no field specified
        assertParseFailure(parser, "1", MESSAGE_INVALID_FORMAT);

        // no index and no field specified
        assertParseFailure(parser, "", String.format(invalidIndexErrorMessage, ""));
    }

    @Test
    public void parse_invalidPreamble_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                "%1$s" + WorkerAvailableCommand.MESSAGE_USAGE);
        String invalidIndexErrorMessage = String.format(expectedMessage, Messages.MESSAGE_INVALID_DISPLAYED_INDEX);

        // negative index
        assertParseFailure(parser, "-5" + ROLE_DESC_CASHIER, String.format(invalidIndexErrorMessage, "-5"));

        // zero index
        assertParseFailure(parser, "0" + ROLE_DESC_CASHIER, String.format(invalidIndexErrorMessage, "0"));

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 random string", String.format(invalidIndexErrorMessage, "1 random string"));

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", String.format(invalidIndexErrorMessage, "1 i/ string"));
    }

    @Test
    public void parse_invalidRole_failure() {
        assertParseFailure(parser, "1" + INVALID_ROLE_DESC,
            String.format(Messages.MESSAGE_INVALID_PARSE_VALUE,
                "Role", INVALID_ROLE_DESC.substring(3), Role.MESSAGE_CONSTRAINTS)); // invalid role
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = TypicalIndexes.INDEX_SECOND_WORKER;
        String userInput = targetIndex.getOneBased() + ROLE_DESC_CHEF;
        Role role = Role.createRole(VALID_ROLE_CHEF);
        WorkerAvailableCommand expectedCommand = new WorkerAvailableCommand(targetIndex, role);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

}
