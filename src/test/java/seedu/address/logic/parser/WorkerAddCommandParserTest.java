package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_ROLE_NOT_FOUND;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PAY_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ROLE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.NOT_FOUND_ROLE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.PAY_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PAY_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.ROLE_DESC_CASHIER;
import static seedu.address.logic.commands.CommandTestUtil.ROLE_DESC_CHEF;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PAY_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_CASHIER;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_CHEF;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalWorkers.AMY;
import static seedu.address.testutil.TypicalWorkers.BOB;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.WorkerAddCommand;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.shift.Shift;
import seedu.address.model.tag.Role;
//import seedu.address.model.tag.Tag;
import seedu.address.model.worker.Address;
import seedu.address.model.worker.Name;
import seedu.address.model.worker.Pay;
import seedu.address.model.worker.Phone;
import seedu.address.model.worker.Worker;
import seedu.address.testutil.WorkerBuilder;

public class WorkerAddCommandParserTest {
    private AddCommandParser parserWithModelStub = new AddCommandParser(new ModelStub());
    private AddCommandParser parserWithModelStubWithRoles = new AddCommandParser(
            new ModelStubWithRoles(Role.createRole(VALID_ROLE_CASHIER), Role.createRole(VALID_ROLE_CHEF)));

    @Test
    public void parse_allFieldsPresent_success() {
        Worker expectedWorker = new WorkerBuilder(BOB).withRoles(VALID_ROLE_CASHIER).build();

        // whitespace only preamble
        assertParseSuccess(parserWithModelStubWithRoles, PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB
                + PAY_DESC_BOB + ADDRESS_DESC_BOB + ROLE_DESC_CASHIER, new WorkerAddCommand(expectedWorker));

        // multiple names - last name accepted
        assertParseSuccess(parserWithModelStubWithRoles, NAME_DESC_AMY + NAME_DESC_BOB + PHONE_DESC_BOB + PAY_DESC_BOB
                + ADDRESS_DESC_BOB + ROLE_DESC_CASHIER, new WorkerAddCommand(expectedWorker));

        // multiple phones - last phone accepted
        assertParseSuccess(parserWithModelStubWithRoles, NAME_DESC_BOB + PHONE_DESC_AMY + PHONE_DESC_BOB + PAY_DESC_BOB
                + ADDRESS_DESC_BOB + ROLE_DESC_CASHIER, new WorkerAddCommand(expectedWorker));

        // multiple addresses - last address accepted
        assertParseSuccess(parserWithModelStubWithRoles, NAME_DESC_BOB + PHONE_DESC_BOB + PAY_DESC_BOB
                + ADDRESS_DESC_AMY + ADDRESS_DESC_BOB + ROLE_DESC_CASHIER, new WorkerAddCommand(expectedWorker));

        // multiple roles - all accepted
        Worker expectedWorkerMultipleTags = new WorkerBuilder(BOB).withRoles(VALID_ROLE_CASHIER, VALID_ROLE_CHEF)
                .build();
        assertParseSuccess(parserWithModelStubWithRoles, NAME_DESC_BOB + PHONE_DESC_BOB + PAY_DESC_BOB
                        + ADDRESS_DESC_BOB + ROLE_DESC_CASHIER + ROLE_DESC_CHEF,
                new WorkerAddCommand(expectedWorkerMultipleTags));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Worker expectedWorker = new WorkerBuilder(AMY).build();
        assertParseSuccess(parserWithModelStubWithRoles, NAME_DESC_AMY + PHONE_DESC_AMY + PAY_DESC_AMY
                        + ADDRESS_DESC_AMY + ROLE_DESC_CASHIER, new WorkerAddCommand(expectedWorker));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, WorkerAddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parserWithModelStub, VALID_NAME_BOB + PHONE_DESC_BOB + PAY_DESC_BOB + ADDRESS_DESC_BOB
                + ROLE_DESC_CASHIER, expectedMessage);

        // missing phone prefix
        assertParseFailure(parserWithModelStub, NAME_DESC_BOB + VALID_PHONE_BOB + PAY_DESC_BOB + ADDRESS_DESC_BOB
                + ROLE_DESC_CASHIER, expectedMessage);

        // missing pay prefix
        assertParseFailure(parserWithModelStub, NAME_DESC_BOB + PHONE_DESC_BOB + VALID_PAY_BOB + ADDRESS_DESC_BOB
                + ROLE_DESC_CASHIER, expectedMessage);

        // missing address prefix
        assertParseFailure(parserWithModelStub, NAME_DESC_BOB + PHONE_DESC_BOB + PAY_DESC_BOB + VALID_ADDRESS_BOB
                + ROLE_DESC_CASHIER, expectedMessage);


        // all prefixes missing
        assertParseFailure(parserWithModelStub, VALID_NAME_BOB + VALID_PHONE_BOB + VALID_PAY_BOB + VALID_ADDRESS_BOB
                + VALID_ROLE_CASHIER, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parserWithModelStub, INVALID_NAME_DESC + PHONE_DESC_BOB + PAY_DESC_BOB + ADDRESS_DESC_BOB
                + ROLE_DESC_CASHIER + ROLE_DESC_CHEF, Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parserWithModelStub, NAME_DESC_BOB + INVALID_PHONE_DESC + PAY_DESC_BOB + ADDRESS_DESC_BOB
                + ROLE_DESC_CASHIER + ROLE_DESC_CHEF, Phone.MESSAGE_CONSTRAINTS);

        // invalid pay
        assertParseFailure(parserWithModelStub, NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_PAY_DESC + ADDRESS_DESC_BOB
                + ROLE_DESC_CASHIER + ROLE_DESC_CHEF, Pay.MESSAGE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parserWithModelStub, NAME_DESC_BOB + PHONE_DESC_BOB + PAY_DESC_BOB + INVALID_ADDRESS_DESC
                + ROLE_DESC_CASHIER + ROLE_DESC_CHEF, Address.MESSAGE_CONSTRAINTS);

        // invalid role
        assertParseFailure(parserWithModelStub, NAME_DESC_BOB + PHONE_DESC_BOB + PAY_DESC_BOB + ADDRESS_DESC_BOB
                + INVALID_ROLE_DESC, Role.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parserWithModelStub, INVALID_NAME_DESC + PHONE_DESC_BOB + PAY_DESC_BOB + INVALID_ADDRESS_DESC
                + ROLE_DESC_CASHIER, Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parserWithModelStub, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PHONE_DESC_BOB + PAY_DESC_BOB
                        + ADDRESS_DESC_BOB + ROLE_DESC_CASHIER + ROLE_DESC_CHEF,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, WorkerAddCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_roleNotFound_failure() {
        assertParseFailure(parserWithModelStubWithRoles, NAME_DESC_AMY + PHONE_DESC_AMY + PAY_DESC_AMY
                + ADDRESS_DESC_AMY + NOT_FOUND_ROLE_DESC, MESSAGE_ROLE_NOT_FOUND);
    }


    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addWorker(Worker worker) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasWorker(Worker worker) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteWorker(Worker target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setWorker(Worker target, Worker editedWorker) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Worker> getFullWorkerList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Worker> getFilteredWorkerList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredWorkerList(Predicate<Worker> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasShift(Shift shift) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteShift(Shift target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addShift(Shift shift) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setShift(Shift target, Shift editedShift) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredShiftList(Predicate<Shift> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Shift> getFullShiftList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Shift> getFilteredShiftList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasAssignment(Assignment assignment) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteAssignment(Assignment target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addAssignment(Assignment assignment) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAssignment(Assignment target, Assignment editedAssignment) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Assignment> getFullAssignmentList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasRole(Role role) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteRole(Role target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addRole(Role role) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setRole(Role target, Role editedRole) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Role> getFilteredRoleList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredRoleList(Predicate<Role> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains two sample roles.
     */
    private class ModelStubWithRoles extends ModelStub {
        private final List<Role> roles;

        ModelStubWithRoles(Role... roles) {
            requireNonNull(roles);
            this.roles = Arrays.asList(roles);
        }

        @Override
        public boolean hasRole(Role role) {
            requireNonNull(role);
            return this.roles.contains(role);
        }

        @Override
        public ObservableList<Role> getFilteredRoleList() {
            return FXCollections.observableList(roles);
        }

        @Override
        public void updateFilteredRoleList(Predicate<Role> predicate) { }
    }
}
