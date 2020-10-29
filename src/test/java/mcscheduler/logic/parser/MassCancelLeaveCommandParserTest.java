package mcscheduler.logic.parser;

import static mcscheduler.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static mcscheduler.logic.commands.CommandTestUtil.DAY_DESC_MON;
import static mcscheduler.logic.commands.CommandTestUtil.DAY_DESC_TUE;
import static mcscheduler.logic.commands.CommandTestUtil.INVALID_DAY;
import static mcscheduler.logic.commands.CommandTestUtil.INVALID_TIME;
import static mcscheduler.logic.commands.CommandTestUtil.INVALID_WORKER_INDEX;
import static mcscheduler.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static mcscheduler.logic.commands.CommandTestUtil.TIME_DESC_AM;
import static mcscheduler.logic.commands.CommandTestUtil.TIME_DESC_PM;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_DAY_MON;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_DAY_TUE;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_TIME_AM;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_TIME_PM;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_WORKER_INDEX_1;

import org.junit.jupiter.api.Test;

import mcscheduler.testutil.*;
import mcscheduler.logic.commands.MassCancelLeaveCommand;
import mcscheduler.model.shift.ShiftDay;
import mcscheduler.model.shift.ShiftTime;

public class MassCancelLeaveCommandParserTest {
    private MassCancelLeaveCommandParser parser = new MassCancelLeaveCommandParser();
    private ShiftDay mon = new ShiftDay(VALID_DAY_MON);
    private ShiftTime am = new ShiftTime(VALID_TIME_AM);
    private ShiftDay tue = new ShiftDay(VALID_DAY_TUE);
    private ShiftTime pm = new ShiftTime(VALID_TIME_PM);

    @Test
    public void parser_allFieldsPresent_success() {

        CommandParserTestUtil.assertParseSuccess(parser, PREAMBLE_WHITESPACE + VALID_WORKER_INDEX_1 + DAY_DESC_MON
                        + TIME_DESC_AM + DAY_DESC_TUE + TIME_DESC_PM,
                new MassCancelLeaveCommand(TypicalIndexes.INDEX_FIRST_WORKER, mon, am, tue, pm));


        // change order should result in a different MassTakeLeaveCommand
        CommandParserTestUtil
            .assertParseSuccess(parser, TIME_DESC_PM + DAY_DESC_MON + DAY_DESC_TUE + VALID_WORKER_INDEX_1
                + TIME_DESC_AM, new MassCancelLeaveCommand(TypicalIndexes.INDEX_FIRST_WORKER, mon, pm, tue, am));
    }

    @Test
    public void parser_missingCompulsoryFields_fail() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, MassCancelLeaveCommand.MESSAGE_USAGE);

        CommandParserTestUtil.assertParseFailure(parser, DAY_DESC_MON + TIME_DESC_AM + DAY_DESC_TUE + TIME_DESC_PM,
                expectedMessage); // missing index
        CommandParserTestUtil
            .assertParseFailure(parser, VALID_WORKER_INDEX_1 + TIME_DESC_AM + DAY_DESC_TUE + TIME_DESC_PM,
                expectedMessage); // missing day
        CommandParserTestUtil
            .assertParseFailure(parser, VALID_WORKER_INDEX_1 + DAY_DESC_MON + TIME_DESC_AM + DAY_DESC_TUE,
                expectedMessage); // missing time
    }

    @Test
    public void parser_extraCompulsoryFields_fail() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, MassCancelLeaveCommand.MESSAGE_USAGE);

        CommandParserTestUtil
            .assertParseFailure(parser, VALID_WORKER_INDEX_1 + DAY_DESC_MON + TIME_DESC_AM + DAY_DESC_TUE
                + DAY_DESC_MON + TIME_DESC_PM, expectedMessage); // extra day
        CommandParserTestUtil
            .assertParseFailure(parser, VALID_WORKER_INDEX_1 + DAY_DESC_MON + TIME_DESC_AM + DAY_DESC_TUE
                + TIME_DESC_PM + TIME_DESC_AM, expectedMessage); // extra time
        CommandParserTestUtil
            .assertParseFailure(parser, VALID_WORKER_INDEX_1 + DAY_DESC_MON + TIME_DESC_AM + DAY_DESC_MON
                + TIME_DESC_AM + DAY_DESC_MON + TIME_DESC_AM, expectedMessage); // extra day/time
    }

    @Test
    public void parser_invalidValue_fail() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, MassCancelLeaveCommand.MESSAGE_USAGE);

        CommandParserTestUtil
            .assertParseFailure(parser, INVALID_WORKER_INDEX + DAY_DESC_MON + TIME_DESC_AM + DAY_DESC_TUE
                + TIME_DESC_PM, expectedMessage); // invalid worker
        CommandParserTestUtil
            .assertParseFailure(parser, VALID_WORKER_INDEX_1 + INVALID_DAY + TIME_DESC_AM + DAY_DESC_TUE
                + TIME_DESC_AM, expectedMessage); // invalid day
        CommandParserTestUtil
            .assertParseFailure(parser, VALID_WORKER_INDEX_1 + DAY_DESC_MON + INVALID_TIME + DAY_DESC_TUE
                + TIME_DESC_PM, expectedMessage); //invalid time
    }
}
