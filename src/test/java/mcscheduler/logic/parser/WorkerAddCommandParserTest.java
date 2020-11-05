package mcscheduler.logic.parser;

import static mcscheduler.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static mcscheduler.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static mcscheduler.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static mcscheduler.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static mcscheduler.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static mcscheduler.logic.commands.CommandTestUtil.INVALID_PAY_DESC;
import static mcscheduler.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static mcscheduler.logic.commands.CommandTestUtil.INVALID_ROLE_DESC;
import static mcscheduler.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static mcscheduler.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static mcscheduler.logic.commands.CommandTestUtil.PAY_DESC_AMY;
import static mcscheduler.logic.commands.CommandTestUtil.PAY_DESC_BOB;
import static mcscheduler.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static mcscheduler.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static mcscheduler.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static mcscheduler.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static mcscheduler.logic.commands.CommandTestUtil.ROLE_DESC_CASHIER;
import static mcscheduler.logic.commands.CommandTestUtil.ROLE_DESC_CHEF;
import static mcscheduler.logic.commands.CommandTestUtil.UNAVAILABILITY_DESC;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_PAY_BOB;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_ROLE_CASHIER;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_ROLE_CHEF;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_UNAVAILABILITY;
import static mcscheduler.logic.parser.CommandParserTestUtil.assertParseFailure;
import static mcscheduler.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static mcscheduler.testutil.TypicalWorkers.AMY;
import static mcscheduler.testutil.TypicalWorkers.BOB;

import org.junit.jupiter.api.Test;

import mcscheduler.commons.core.Messages;
import mcscheduler.logic.commands.WorkerAddCommand;
import mcscheduler.model.role.Role;
import mcscheduler.model.worker.Address;
import mcscheduler.model.worker.Name;
import mcscheduler.model.worker.Pay;
import mcscheduler.model.worker.Phone;
import mcscheduler.model.worker.Worker;
import mcscheduler.testutil.WorkerBuilder;

//@@author
public class WorkerAddCommandParserTest {
    private final WorkerAddCommandParser parser = new WorkerAddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Worker expectedWorker = new WorkerBuilder(BOB).withRoles(VALID_ROLE_CASHIER).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB + PAY_DESC_BOB
                + ADDRESS_DESC_BOB + ROLE_DESC_CASHIER, new WorkerAddCommand(expectedWorker));

        // multiple names - last name accepted
        assertParseSuccess(parser, NAME_DESC_AMY + NAME_DESC_BOB + PHONE_DESC_BOB + PAY_DESC_BOB
                + ADDRESS_DESC_BOB + ROLE_DESC_CASHIER, new WorkerAddCommand(expectedWorker));

        // multiple phones - last phone accepted
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_AMY + PHONE_DESC_BOB + PAY_DESC_BOB
                + ADDRESS_DESC_BOB + ROLE_DESC_CASHIER, new WorkerAddCommand(expectedWorker));

        // multiple addresses - last address accepted
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + PAY_DESC_BOB + ADDRESS_DESC_AMY
                + ADDRESS_DESC_BOB + ROLE_DESC_CASHIER, new WorkerAddCommand(expectedWorker));

        // multiple roles - all accepted
        Worker expectedWorkerMultipleTags = new WorkerBuilder(BOB).withRoles(VALID_ROLE_CASHIER, VALID_ROLE_CHEF)
                .build();
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + PAY_DESC_BOB + ADDRESS_DESC_BOB
                + ROLE_DESC_CASHIER + ROLE_DESC_CHEF, new WorkerAddCommand(expectedWorkerMultipleTags));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Worker expectedWorker = new WorkerBuilder(AMY).build();
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY + PAY_DESC_AMY + ADDRESS_DESC_AMY + ROLE_DESC_CASHIER,
                new WorkerAddCommand(expectedWorker));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, WorkerAddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + PHONE_DESC_BOB + PAY_DESC_BOB + ADDRESS_DESC_BOB
                + ROLE_DESC_CASHIER, expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + PAY_DESC_BOB + ADDRESS_DESC_BOB
                + ROLE_DESC_CASHIER, expectedMessage);

        // missing pay prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + VALID_PAY_BOB + ADDRESS_DESC_BOB
                + ROLE_DESC_CASHIER, expectedMessage);

        // missing address prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + PAY_DESC_BOB + VALID_ADDRESS_BOB
                + ROLE_DESC_CASHIER, expectedMessage);


        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PHONE_BOB + VALID_PAY_BOB + VALID_ADDRESS_BOB
                + VALID_ROLE_CASHIER + VALID_UNAVAILABILITY, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + PAY_DESC_BOB + ADDRESS_DESC_BOB
                + ROLE_DESC_CASHIER + ROLE_DESC_CHEF,
                String.format(Messages.MESSAGE_INVALID_PARSE_VALUE,
                        "Name", INVALID_NAME_DESC.substring(3), Name.MESSAGE_CONSTRAINTS));

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_PHONE_DESC + PAY_DESC_BOB + ADDRESS_DESC_BOB
                + ROLE_DESC_CASHIER + ROLE_DESC_CHEF,
                String.format(Messages.MESSAGE_INVALID_PARSE_VALUE,
                        "Phone number", INVALID_PHONE_DESC.substring(4), Phone.MESSAGE_CONSTRAINTS));
        //phone desc has 2 length prefix, so substring 4 for its value

        // invalid pay
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_PAY_DESC + ADDRESS_DESC_BOB
                + ROLE_DESC_CASHIER + ROLE_DESC_CHEF,
                String.format(Messages.MESSAGE_INVALID_PARSE_VALUE,
                        "Pay", INVALID_PAY_DESC.substring(3), Pay.MESSAGE_CONSTRAINTS));

        // invalid address
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + PAY_DESC_BOB + INVALID_ADDRESS_DESC
                + ROLE_DESC_CASHIER + ROLE_DESC_CHEF,
                String.format(Messages.MESSAGE_INVALID_PARSE_VALUE,
                        "Address", INVALID_ADDRESS_DESC.substring(3), Address.MESSAGE_CONSTRAINTS));

        // invalid role
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + PAY_DESC_BOB + ADDRESS_DESC_BOB
                + INVALID_ROLE_DESC,
                String.format(Messages.MESSAGE_INVALID_PARSE_VALUE,
                        "Role", INVALID_ROLE_DESC.substring(3), Role.MESSAGE_CONSTRAINTS));

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + PAY_DESC_BOB + INVALID_ADDRESS_DESC
                + ROLE_DESC_CASHIER,
                String.format(Messages.MESSAGE_INVALID_PARSE_VALUE,
                    "Name", INVALID_NAME_DESC.substring(3), Name.MESSAGE_CONSTRAINTS));

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PHONE_DESC_BOB + PAY_DESC_BOB + ADDRESS_DESC_BOB
                + ROLE_DESC_CASHIER + ROLE_DESC_CHEF + UNAVAILABILITY_DESC,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, WorkerAddCommand.MESSAGE_USAGE));
    }
}
