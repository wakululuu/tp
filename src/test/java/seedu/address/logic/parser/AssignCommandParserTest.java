package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ROLE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_SHIFT_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_WORKER_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.ROLE_DESC_CASHIER;
import static seedu.address.logic.commands.CommandTestUtil.ROLE_DESC_CHEF;
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

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.AssignCommand;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.shift.Shift;
import seedu.address.model.tag.Role;
import seedu.address.model.worker.Worker;

public class AssignCommandParserTest {
    private AssignCommandParser parserWithModelStub = new AssignCommandParser(new ModelStub());
    private AssignCommandParser parserWithModelStubWithRoles = new AssignCommandParser(
            new ModelStubWithRoles(Role.createRole(VALID_ROLE_CASHIER), Role.createRole(VALID_ROLE_CHEF)));

    @Test
    public void parse_allFieldsPresent_success() {
        // whitespace only preamble
        assertParseSuccess(parserWithModelStubWithRoles,
                PREAMBLE_WHITESPACE + VALID_SHIFT_INDEX_1 + VALID_WORKER_INDEX_1 + ROLE_DESC_CASHIER,
                new AssignCommand(INDEX_FIRST_SHIFT, INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CASHIER)));

        // different order
        assertParseSuccess(parserWithModelStubWithRoles,
                VALID_WORKER_INDEX_1 + VALID_SHIFT_INDEX_1 + ROLE_DESC_CASHIER,
                new AssignCommand(INDEX_FIRST_SHIFT, INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CASHIER)));

        // multiple shift indexes - last shift index accepted
        assertParseSuccess(parserWithModelStubWithRoles,
                VALID_SHIFT_INDEX_2 + VALID_SHIFT_INDEX_1 + VALID_WORKER_INDEX_1 + ROLE_DESC_CASHIER,
                new AssignCommand(INDEX_FIRST_SHIFT, INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CASHIER)));

        // multiple worker indexes - last worker index accepted
        assertParseSuccess(parserWithModelStubWithRoles,
                VALID_SHIFT_INDEX_1 + VALID_WORKER_INDEX_2 + VALID_WORKER_INDEX_1 + ROLE_DESC_CASHIER,
                new AssignCommand(INDEX_FIRST_SHIFT, INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CASHIER)));

        // multiple roles - last role accepted
        assertParseSuccess(parserWithModelStubWithRoles,
                VALID_SHIFT_INDEX_1 + VALID_WORKER_INDEX_1 + ROLE_DESC_CHEF + ROLE_DESC_CASHIER,
                new AssignCommand(INDEX_FIRST_SHIFT, INDEX_FIRST_WORKER, Role.createRole(VALID_ROLE_CASHIER)));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignCommand.MESSAGE_USAGE);

        // missing shift prefix
        assertParseFailure(parserWithModelStub, INDEX_FIRST_SHIFT + VALID_WORKER_INDEX_1 + ROLE_DESC_CASHIER,
                expectedMessage);

        // missing worker prefix
        assertParseFailure(parserWithModelStub, VALID_SHIFT_INDEX_1 + " " + INDEX_FIRST_WORKER + ROLE_DESC_CASHIER,
                expectedMessage);

        // missing role prefix
        assertParseFailure(parserWithModelStub, VALID_SHIFT_INDEX_1 + VALID_WORKER_INDEX_1 + " " + VALID_ROLE_CASHIER,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parserWithModelStub, INDEX_FIRST_SHIFT + " " + INDEX_FIRST_WORKER + VALID_ROLE_CASHIER,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignCommand.MESSAGE_USAGE);

        // invalid shift index
        assertParseFailure(parserWithModelStub, INVALID_SHIFT_INDEX + VALID_WORKER_INDEX_1 + ROLE_DESC_CHEF,
                expectedMessage);

        // invalid worker index
        assertParseFailure(parserWithModelStub, VALID_SHIFT_INDEX_1 + INVALID_WORKER_INDEX + ROLE_DESC_CHEF,
                expectedMessage);

        // invalid role
        assertParseFailure(parserWithModelStub, VALID_SHIFT_INDEX_1 + VALID_WORKER_INDEX_1 + INVALID_ROLE_DESC,
                Role.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parserWithModelStub, INVALID_SHIFT_INDEX + VALID_WORKER_INDEX_1 + INVALID_ROLE_DESC,
                expectedMessage);
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
