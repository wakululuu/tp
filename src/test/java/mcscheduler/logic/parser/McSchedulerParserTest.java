package mcscheduler.logic.parser;

import static mcscheduler.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static mcscheduler.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import mcscheduler.logic.commands.ClearCommand;
import mcscheduler.logic.commands.ExitCommand;
import mcscheduler.logic.commands.FindCommand;
import mcscheduler.logic.commands.HelpCommand;
import mcscheduler.logic.commands.WorkerAddCommand;
import mcscheduler.logic.commands.WorkerDeleteCommand;
import mcscheduler.logic.commands.WorkerEditCommand;
import mcscheduler.logic.commands.WorkerEditCommand.EditWorkerDescriptor;
import mcscheduler.logic.commands.WorkerListCommand;
import mcscheduler.logic.parser.exceptions.ParseException;
import mcscheduler.model.worker.NameContainsKeywordsPredicate;
import mcscheduler.model.worker.Worker;
import mcscheduler.testutil.Assert;
import mcscheduler.testutil.EditWorkerDescriptorBuilder;
import mcscheduler.testutil.TypicalIndexes;
import mcscheduler.testutil.WorkerBuilder;
import mcscheduler.testutil.WorkerUtil;

public class McSchedulerParserTest {

    private final McSchedulerParser parser = new McSchedulerParser();

    @Test
    public void parseCommand_add() throws Exception {
        Worker worker = new WorkerBuilder().build();
        WorkerAddCommand command = (WorkerAddCommand) parser.parseCommand(WorkerUtil.getAddCommand(worker));
        assertEquals(new WorkerAddCommand(worker), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        WorkerDeleteCommand command = (WorkerDeleteCommand) parser.parseCommand(
            WorkerDeleteCommand.COMMAND_WORD + " " + TypicalIndexes.INDEX_FIRST_WORKER.getOneBased());
        assertEquals(new WorkerDeleteCommand(TypicalIndexes.INDEX_FIRST_WORKER), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Worker worker = new WorkerBuilder().build();
        EditWorkerDescriptor descriptor = new EditWorkerDescriptorBuilder(worker).build();
        WorkerEditCommand command = (WorkerEditCommand) parser.parseCommand(WorkerEditCommand.COMMAND_WORD + " "
            + TypicalIndexes.INDEX_FIRST_WORKER.getOneBased() + " "
            + WorkerUtil.getEditWorkerDescriptorDetails(descriptor));
        assertEquals(new WorkerEditCommand(TypicalIndexes.INDEX_FIRST_WORKER, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
            FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(WorkerListCommand.COMMAND_WORD) instanceof WorkerListCommand);
        assertTrue(parser.parseCommand(WorkerListCommand.COMMAND_WORD + " 3") instanceof WorkerListCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        Assert.assertThrows(ParseException.class,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
                -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        Assert.assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
