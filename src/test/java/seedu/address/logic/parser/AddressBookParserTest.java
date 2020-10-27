package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_CASHIER;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_CHEF;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_WORKER;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.WorkerAddCommand;
import seedu.address.logic.commands.WorkerDeleteCommand;
import seedu.address.logic.commands.WorkerEditCommand;
import seedu.address.logic.commands.WorkerEditCommand.EditWorkerDescriptor;
import seedu.address.logic.commands.WorkerListCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.assignment.exceptions.AssignmentNotFoundException;
import seedu.address.model.shift.Shift;
import seedu.address.model.tag.Role;
import seedu.address.model.worker.NameContainsKeywordsPredicate;
import seedu.address.model.worker.Worker;
import seedu.address.testutil.EditWorkerDescriptorBuilder;
import seedu.address.testutil.WorkerBuilder;
import seedu.address.testutil.WorkerUtil;

public class AddressBookParserTest {

    private final AddressBookParser parserWithModelStub = new AddressBookParser(new ModelStub());
    private final AddressBookParser parserWithModelStubWithRoles = new AddressBookParser(new ModelStubWithRoles(
            Role.createRole(VALID_ROLE_CASHIER), Role.createRole(VALID_ROLE_CHEF)));

    @Test
    public void parseCommand_add() throws Exception {
        Worker worker = new WorkerBuilder().build();
        WorkerAddCommand command = (WorkerAddCommand) parserWithModelStubWithRoles.parseCommand(
                WorkerUtil.getAddCommand(worker));
        assertEquals(new WorkerAddCommand(worker), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parserWithModelStub.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parserWithModelStub.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        WorkerDeleteCommand command = (WorkerDeleteCommand) parserWithModelStub.parseCommand(
                WorkerDeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_WORKER.getOneBased());
        assertEquals(new WorkerDeleteCommand(INDEX_FIRST_WORKER), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Worker worker = new WorkerBuilder().build();
        EditWorkerDescriptor descriptor = new EditWorkerDescriptorBuilder(worker).build();
        WorkerEditCommand command = (WorkerEditCommand) parserWithModelStubWithRoles.parseCommand(
                WorkerEditCommand.COMMAND_WORD + " " + INDEX_FIRST_WORKER.getOneBased() + " "
                        + WorkerUtil.getEditWorkerDescriptorDetails(descriptor));
        assertEquals(new WorkerEditCommand(INDEX_FIRST_WORKER, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parserWithModelStub.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parserWithModelStub.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parserWithModelStub.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parserWithModelStub.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parserWithModelStub.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parserWithModelStub.parseCommand(WorkerListCommand.COMMAND_WORD) instanceof WorkerListCommand);
        assertTrue(parserWithModelStub.parseCommand(WorkerListCommand.COMMAND_WORD + " 3")
                instanceof WorkerListCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parserWithModelStub.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () ->
                parserWithModelStub.parseCommand("unknownCommand"));
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
        public void deleteAssignment(Assignment target) throws AssignmentNotFoundException {
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
