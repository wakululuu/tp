package mcscheduler.logic.commands;

import static mcscheduler.commons.core.Messages.MESSAGE_WORKERS_LISTED_OVERVIEW;
import static mcscheduler.logic.commands.CommandTestUtil.assertCommandSuccess;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import mcscheduler.model.Model;
import mcscheduler.model.ModelManager;
import mcscheduler.model.UserPrefs;
import mcscheduler.model.worker.NameContainsKeywordsPredicate;
import mcscheduler.testutil.McSchedulerBuilder;
import mcscheduler.testutil.TypicalWorkers;

//@@author
/**
 * Contains integration tests (interaction with the Model) for {@code WorkerFindCommand}.
 */
public class WorkerFindCommandTest {
    private final Model model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());
    private final Model expectedModel = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());

    @Test
    public void equals() {
        NameContainsKeywordsPredicate firstPredicate = new NameContainsKeywordsPredicate(
                Collections.singletonList("first"));
        NameContainsKeywordsPredicate secondPredicate = new NameContainsKeywordsPredicate(
                Collections.singletonList("second"));

        WorkerFindCommand findFirstCommand = new WorkerFindCommand(firstPredicate);
        WorkerFindCommand findSecondCommand = new WorkerFindCommand(secondPredicate);

        // same object -> returns true
        assertEquals(findFirstCommand, findFirstCommand);

        // same values -> returns true
        WorkerFindCommand findFirstCommandCopy = new WorkerFindCommand(firstPredicate);
        assertEquals(findFirstCommandCopy, findFirstCommand);

        // different types -> returns false
        assertNotEquals(findFirstCommand, 1);

        // null -> returns false
        assertNotEquals(findFirstCommand, null);

        // different worker -> returns false
        assertNotEquals(findSecondCommand, findFirstCommand);
    }

    @Test
    public void execute_zeroKeywords_noWorkerFound() {
        String expectedMessage = String.format(MESSAGE_WORKERS_LISTED_OVERVIEW, 0);
        NameContainsKeywordsPredicate predicate = preparePredicate(" ");
        WorkerFindCommand command = new WorkerFindCommand(predicate);
        expectedModel.updateFilteredWorkerList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredWorkerList());
    }

    @Test
    public void execute_multipleKeywords_multipleWorkersFound() {
        String expectedMessage = String.format(MESSAGE_WORKERS_LISTED_OVERVIEW, 3);
        NameContainsKeywordsPredicate predicate = preparePredicate("Kurz Elle Kunz");
        WorkerFindCommand command = new WorkerFindCommand(predicate);
        expectedModel.updateFilteredWorkerList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(TypicalWorkers.CARL, TypicalWorkers.ELLE, TypicalWorkers.FIONA),
                model.getFilteredWorkerList());
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private NameContainsKeywordsPredicate preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
