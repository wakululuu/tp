package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.DAY_DESC_MON;
import static seedu.address.logic.commands.CommandTestUtil.DAY_DESC_TUE;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DAY;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TIME;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_WORKER_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.TIME_DESC_AM;
import static seedu.address.logic.commands.CommandTestUtil.TIME_DESC_PM;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DAY_MON;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DAY_TUE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SHIFT_INDEX_1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TIME_AM;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TIME_PM;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WORKER_INDEX_1;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_WORKER;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.MassTakeLeaveCommand;
import seedu.address.model.shift.ShiftDay;
import seedu.address.model.shift.ShiftTime;

public class MassTakeLeaveCommandParserTest {
    private MassTakeLeaveCommandParser parser = new MassTakeLeaveCommandParser();
    private ShiftDay mon = new ShiftDay(VALID_DAY_MON);
    private ShiftTime am = new ShiftTime(VALID_TIME_AM);
    private ShiftDay tue = new ShiftDay(VALID_DAY_TUE);
    private ShiftTime pm = new ShiftTime(VALID_TIME_PM);

    @Test
    public void parser_allFieldsPresent_success() {

        assertParseSuccess(parser, PREAMBLE_WHITESPACE + VALID_WORKER_INDEX_1 + DAY_DESC_MON +
                TIME_DESC_AM + DAY_DESC_TUE + TIME_DESC_PM,
                new MassTakeLeaveCommand(INDEX_FIRST_WORKER, mon, am, tue, pm));


        // change order should result in a different MassTakeLeaveCommand
        assertParseSuccess(parser, TIME_DESC_PM + DAY_DESC_MON + DAY_DESC_TUE + VALID_WORKER_INDEX_1 +
                        TIME_DESC_AM, new MassTakeLeaveCommand(INDEX_FIRST_WORKER, mon, pm, tue, am));
    }

    @Test
    public void parser_missingCompulsoryFields_fail() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, MassTakeLeaveCommand.MESSAGE_USAGE);

        assertParseFailure(parser, DAY_DESC_MON + TIME_DESC_AM + DAY_DESC_TUE + TIME_DESC_PM,
                expectedMessage); // missing index
        assertParseFailure(parser, VALID_WORKER_INDEX_1 + TIME_DESC_AM + DAY_DESC_TUE + TIME_DESC_PM,
                expectedMessage); // missing day
        assertParseFailure(parser, VALID_WORKER_INDEX_1 + DAY_DESC_MON + TIME_DESC_AM + DAY_DESC_TUE,
                expectedMessage); // missing time
    }

    @Test
    public void parser_extraCompulsoryFields_fail() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, MassTakeLeaveCommand.MESSAGE_USAGE);

        assertParseFailure(parser, VALID_WORKER_INDEX_1 + DAY_DESC_MON + TIME_DESC_AM + DAY_DESC_TUE +
                DAY_DESC_MON + TIME_DESC_PM, expectedMessage); // extra day
        assertParseFailure(parser, VALID_WORKER_INDEX_1 + DAY_DESC_MON + TIME_DESC_AM + DAY_DESC_TUE +
                TIME_DESC_PM + TIME_DESC_AM, expectedMessage); // extra time
        assertParseFailure(parser, VALID_WORKER_INDEX_1 + DAY_DESC_MON + TIME_DESC_AM + DAY_DESC_MON +
                TIME_DESC_AM + DAY_DESC_MON + TIME_DESC_AM, expectedMessage); // extra day/time
    }

    @Test
    public void parser_invalidValue_fail() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, MassTakeLeaveCommand.MESSAGE_USAGE);

        assertParseFailure(parser, INVALID_WORKER_INDEX + DAY_DESC_MON + TIME_DESC_AM + DAY_DESC_TUE +
                TIME_DESC_PM, expectedMessage); // invalid worker
        assertParseFailure(parser, VALID_WORKER_INDEX_1 + INVALID_DAY + TIME_DESC_AM + DAY_DESC_TUE +
                TIME_DESC_AM, expectedMessage); // invalid day
        assertParseFailure(parser, VALID_WORKER_INDEX_1 + DAY_DESC_MON + INVALID_TIME + DAY_DESC_TUE +
                TIME_DESC_PM, expectedMessage); //invalid time
    }
}
