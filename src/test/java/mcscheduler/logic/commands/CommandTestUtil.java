package mcscheduler.logic.commands;

import static mcscheduler.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_NAME;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_PAY;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_PHONE;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_ROLE;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_SHIFT;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_SHIFT_DAY;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_SHIFT_NEW;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_SHIFT_OLD;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_SHIFT_TIME;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_UNAVAILABILITY;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_WORKER;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_WORKER_NEW;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_WORKER_OLD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mcscheduler.commons.core.index.Index;
import mcscheduler.logic.commands.exceptions.CommandException;
import mcscheduler.model.McScheduler;
import mcscheduler.model.Model;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.shift.ShiftDayOrTimeContainsKeywordsPredicate;
import mcscheduler.model.worker.NameContainsKeywordsPredicate;
import mcscheduler.model.worker.Worker;
import mcscheduler.testutil.Assert;
import mcscheduler.testutil.EditShiftDescriptorBuilder;
import mcscheduler.testutil.EditWorkerDescriptorBuilder;
import mcscheduler.testutil.TestUtil;
import mcscheduler.testutil.TypicalIndexes;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_PHONE_AMY = "91111111";
    public static final String VALID_PHONE_BOB = "82222222";
    public static final String VALID_PAY_AMY = "13.50";
    public static final String VALID_PAY_BOB = "17";
    public static final String VALID_ADDRESS_AMY = "Block 312, Amy Street 1";
    public static final String VALID_ADDRESS_BOB = "Block 123, Bobby Street 3";
    public static final String VALID_ROLE_CASHIER = "Cashier";
    public static final String VALID_ROLE_CHEF = "Chef";
    public static final String VALID_ROLE_JANITOR = "Janitor";
    public static final String VALID_UNAVAILABILITY = "MON AM";

    public static final String INVALID_ROLE = "cashier*"; // '*' not allowed in roles

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PHONE_BOB;
    public static final String PAY_DESC_AMY = " " + PREFIX_PAY + VALID_PAY_AMY;
    public static final String PAY_DESC_BOB = " " + PREFIX_PAY + VALID_PAY_BOB;
    public static final String ADDRESS_DESC_AMY = " " + PREFIX_ADDRESS + VALID_ADDRESS_AMY;
    public static final String ADDRESS_DESC_BOB = " " + PREFIX_ADDRESS + VALID_ADDRESS_BOB;
    public static final String ROLE_DESC_CASHIER = " " + PREFIX_ROLE + VALID_ROLE_CASHIER;
    public static final String ROLE_DESC_CHEF = " " + PREFIX_ROLE + VALID_ROLE_CHEF;
    public static final String UNAVAILABILITY_DESC = " " + PREFIX_UNAVAILABILITY + VALID_UNAVAILABILITY;

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    public static final String INVALID_PAY_DESC = " " + PREFIX_PAY + "12.101"; // limit of 2 digits behind decimal
    public static final String INVALID_ADDRESS_DESC = " " + PREFIX_ADDRESS; // empty string not allowed for addresses
    public static final String INVALID_ROLE_DESC = " " + PREFIX_ROLE + "cashier*"; // '*' not allowed in roles
    public static final String INVALID_UNAVAILABILITY_DESC = " " + PREFIX_UNAVAILABILITY
            + "MON AM pm"; // only 2 keywords allowed
    public static final String NOT_FOUND_ROLE = "Random role"; // role not in model

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final WorkerEditCommand.EditWorkerDescriptor DESC_AMY;
    public static final WorkerEditCommand.EditWorkerDescriptor DESC_BOB;

    public static final ShiftEditCommand.EditShiftDescriptor DESC_FIRST_SHIFT;
    public static final ShiftEditCommand.EditShiftDescriptor DESC_SECOND_SHIFT;

    public static final String VALID_DAY_MON = "MON";
    public static final String VALID_DAY_TUE = "TUE";
    public static final String VALID_TIME_AM = "AM";
    public static final String VALID_TIME_PM = "PM";
    public static final String VALID_ROLE_REQUIREMENT_CASHIER = VALID_ROLE_CASHIER + " 1 1";
    public static final String VALID_ROLE_REQUIREMENT_CHEF = VALID_ROLE_CHEF + " 3 2";

    public static final String DAY_DESC_MON = " " + PREFIX_SHIFT_DAY + VALID_DAY_MON;
    public static final String DAY_DESC_TUE = " " + PREFIX_SHIFT_DAY + VALID_DAY_TUE;
    public static final String TIME_DESC_AM = " " + PREFIX_SHIFT_TIME + VALID_TIME_AM;
    public static final String TIME_DESC_PM = " " + PREFIX_SHIFT_TIME + VALID_TIME_PM;

    public static final String INVALID_DAY = " " + PREFIX_SHIFT_DAY + "Mmon";
    public static final String INVALID_TIME = " " + PREFIX_SHIFT_TIME + "aam";

    public static final String VALID_SHIFT_INDEX_1 =
        " " + PREFIX_SHIFT + TypicalIndexes.INDEX_FIRST_SHIFT.getOneBased();
    public static final String VALID_SHIFT_INDEX_2 =
        " " + PREFIX_SHIFT + TypicalIndexes.INDEX_SECOND_SHIFT.getOneBased();
    public static final String VALID_WORKER_INDEX_1 =
        " " + PREFIX_WORKER + TypicalIndexes.INDEX_FIRST_WORKER.getOneBased();
    public static final String VALID_WORKER_INDEX_2 =
        " " + PREFIX_WORKER + TypicalIndexes.INDEX_SECOND_WORKER.getOneBased();

    public static final String INVALID_NEW_SHIFT_INDEX = " " + PREFIX_SHIFT_NEW + "a";
    public static final String INVALID_NEW_WORKER_INDEX = " " + PREFIX_WORKER_NEW + "a";
    public static final String INVALID_OLD_SHIFT_INDEX = " " + PREFIX_SHIFT_OLD + "a";
    public static final String INVALID_OLD_WORKER_INDEX = " " + PREFIX_WORKER_OLD + "a";

    public static final String INVALID_SHIFT_INDEX = " " + PREFIX_SHIFT + "a";
    public static final String INVALID_WORKER_INDEX = " " + PREFIX_WORKER + "a";

    public static final String VALID_NEW_SHIFT_INDEX_1 =
        " " + PREFIX_SHIFT_NEW + TypicalIndexes.INDEX_FIRST_SHIFT.getOneBased();
    public static final String VALID_NEW_SHIFT_INDEX_2 =
        " " + PREFIX_SHIFT_NEW + TypicalIndexes.INDEX_SECOND_SHIFT.getOneBased();
    public static final String VALID_NEW_WORKER_INDEX_1 =
        " " + PREFIX_WORKER_NEW + TypicalIndexes.INDEX_FIRST_WORKER.getOneBased();
    public static final String VALID_NEW_WORKER_INDEX_2 =
        " " + PREFIX_WORKER_NEW + TypicalIndexes.INDEX_SECOND_WORKER.getOneBased();
    public static final String VALID_OLD_WORKER_INDEX_1 =
        " " + PREFIX_WORKER_OLD + TypicalIndexes.INDEX_FIRST_WORKER.getOneBased();
    public static final String VALID_OLD_SHIFT_INDEX_1 =
        " " + PREFIX_SHIFT_OLD + TypicalIndexes.INDEX_FIRST_SHIFT.getOneBased();


    static {
        DESC_AMY = new EditWorkerDescriptorBuilder().withName(VALID_NAME_AMY)
            .withPhone(VALID_PHONE_AMY).withPay(VALID_PAY_AMY).withAddress(VALID_ADDRESS_AMY)
            .withRoles(VALID_ROLE_CASHIER).build();
        DESC_BOB = new EditWorkerDescriptorBuilder().withName(VALID_NAME_BOB)
            .withPhone(VALID_PHONE_BOB).withPay(VALID_PAY_BOB).withAddress(VALID_ADDRESS_BOB)
            .withRoles(VALID_ROLE_CASHIER, VALID_ROLE_CHEF).build();

        DESC_FIRST_SHIFT = new EditShiftDescriptorBuilder().withShiftDay(VALID_DAY_MON).withShiftTime(VALID_TIME_AM)
            .withRoleRequirements(VALID_ROLE_REQUIREMENT_CHEF).build();
        DESC_SECOND_SHIFT = new EditShiftDescriptorBuilder().withShiftDay(VALID_DAY_TUE).withShiftTime(VALID_TIME_PM)
            .withRoleRequirements(VALID_ROLE_REQUIREMENT_CHEF, VALID_ROLE_REQUIREMENT_CASHIER).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
                                            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
                                            Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the McScheduler, filtered worker list and selected worker in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        McScheduler expectedMcScheduler = new McScheduler(actualModel.getMcScheduler());
        List<Worker> expectedFilteredList = new ArrayList<>(actualModel.getFilteredWorkerList());

        Assert.assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedMcScheduler, actualModel.getMcScheduler());
        assertEquals(expectedFilteredList, actualModel.getFilteredWorkerList());
    }

    /**
     * Updates {@code model}'s filtered list to show only the worker at the given {@code targetIndex} in the
     * {@code model}'s McScheduler.
     */
    public static void showWorkerAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredWorkerList().size());

        Worker worker = TestUtil.getWorker(model, targetIndex);
        final String[] splitName = worker.getName().fullName.split("\\s+");
        model.updateFilteredWorkerList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredWorkerList().size());
    }

    /**
     * Updates {@code model}'s filtered list to show only the shift at the given {@code targetIndex} in the
     * {@code model}'s McScheduler.
     */
    public static void showShiftAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredShiftList().size());

        Shift shift = TestUtil.getShift(model, targetIndex);
        final String[] shiftDayKeywords = {shift.getShiftDay().toString()};
        model.updateFilteredShiftList(new ShiftDayOrTimeContainsKeywordsPredicate(Arrays.asList(shiftDayKeywords)));
        assertEquals(1, model.getFilteredShiftList().size());
    }
}
