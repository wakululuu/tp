package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
//import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
//import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.worker.NameContainsKeywordsPredicate;
import seedu.address.model.worker.Worker;
import seedu.address.model.shift.Shift;
import seedu.address.model.shift.ShiftDayOrTimeContainsKeywordsPredicate;
import seedu.address.testutil.EditWorkerDescriptorBuilder;
import seedu.address.testutil.EditShiftDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_PHONE_AMY = "11111111";
    public static final String VALID_PHONE_BOB = "22222222";
    public static final String VALID_PAY_AMY = "13.50";
    public static final String VALID_PAY_BOB = "17";
    //public static final String VALID_EMAIL_AMY = "amy@example.com";
    //public static final String VALID_EMAIL_BOB = "bob@example.com";
    public static final String VALID_ADDRESS_AMY = "Block 312, Amy Street 1";
    public static final String VALID_ADDRESS_BOB = "Block 123, Bobby Street 3";
    public static final String VALID_ROLE_CASHIER = "cashier";
    public static final String VALID_ROLE_CHEF = "chef";

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PHONE_BOB;
    public static final String PAY_DESC_AMY = " " + PREFIX_PAY + VALID_PAY_AMY;
    public static final String PAY_DESC_BOB = " " + PREFIX_PAY + VALID_PAY_BOB;
    //public static final String EMAIL_DESC_AMY = " " + PREFIX_EMAIL + VALID_EMAIL_AMY;
    //public static final String EMAIL_DESC_BOB = " " + PREFIX_EMAIL + VALID_EMAIL_BOB;
    public static final String ADDRESS_DESC_AMY = " " + PREFIX_ADDRESS + VALID_ADDRESS_AMY;
    public static final String ADDRESS_DESC_BOB = " " + PREFIX_ADDRESS + VALID_ADDRESS_BOB;
    public static final String ROLE_DESC_CASHIER = " " + PREFIX_ROLE + VALID_ROLE_CASHIER;
    public static final String ROLE_DESC_CHEF = " " + PREFIX_ROLE + VALID_ROLE_CHEF;

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    public static final String INVALID_PAY_DESC = " " + PREFIX_PAY + "12.101"; // limit of 2 digits behind decimal
    //public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_ADDRESS_DESC = " " + PREFIX_ADDRESS; // empty string not allowed for addresses
    public static final String INVALID_ROLE_DESC = " " + PREFIX_ROLE + "cashier*"; // '*' not allowed in roles

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
    public static final String VALID_ROLE_REQUIREMENT_CASHIER = VALID_ROLE_CASHIER + " 1";
    public static final String VALID_ROLE_REQUIREMENT_CHEF = VALID_ROLE_CHEF + " 3";

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
     * - the address book, filtered worker list and selected worker in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        AddressBook expectedAddressBook = new AddressBook(actualModel.getAddressBook());
        List<Worker> expectedFilteredList = new ArrayList<>(actualModel.getFilteredWorkerList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedAddressBook, actualModel.getAddressBook());
        assertEquals(expectedFilteredList, actualModel.getFilteredWorkerList());
    }
    /**
     * Updates {@code model}'s filtered list to show only the worker at the given {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showWorkerAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredWorkerList().size());

        Worker worker = model.getFilteredWorkerList().get(targetIndex.getZeroBased());
        final String[] splitName = worker.getName().fullName.split("\\s+");
        model.updateFilteredWorkerList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredWorkerList().size());
    }

    /**
     * Updates {@code model}'s filtered list to show only the shift at the given {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showShiftAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredShiftList().size());

        Shift shift = model.getFilteredShiftList().get(targetIndex.getZeroBased());
        final String[] shiftDayKeywords = { shift.getShiftDay().toString() };
        model.updateFilteredShiftList(new ShiftDayOrTimeContainsKeywordsPredicate(Arrays.asList(shiftDayKeywords)));
        assertEquals(1, model.getFilteredShiftList().size());
    }

}
