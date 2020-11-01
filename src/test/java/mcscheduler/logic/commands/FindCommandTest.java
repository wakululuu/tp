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

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private final Model model = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());
    private final Model expectedModel = new ModelManager(McSchedulerBuilder.getTypicalMcScheduler(), new UserPrefs());

    @Test
    public void equals() {
        NameContainsKeywordsPredicate firstPredicate =
            new NameContainsKeywordsPredicate(Collections.singletonList("first"));
        NameContainsKeywordsPredicate secondPredicate =
            new NameContainsKeywordsPredicate(Collections.singletonList("second"));

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        // same object -> returns true
        assertEquals(findFirstCommand, findFirstCommand);

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
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
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredWorkerList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredWorkerList());
    }

    @Test
    public void execute_multipleKeywords_multipleWorkersFound() {
        String expectedMessage = String.format(MESSAGE_WORKERS_LISTED_OVERVIEW, 3);
        NameContainsKeywordsPredicate predicate = preparePredicate("Kurz Elle Kunz");
        FindCommand command = new FindCommand(predicate);
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
