package mcscheduler.model;

import static mcscheduler.model.Model.PREDICATE_SHOW_ALL_SHIFTS;
import static mcscheduler.model.Model.PREDICATE_SHOW_ALL_WORKERS;
import static mcscheduler.testutil.Assert.assertThrows;
import static mcscheduler.testutil.TypicalShifts.SHIFT_A;
import static mcscheduler.testutil.TypicalShifts.SHIFT_B;
import static mcscheduler.testutil.TypicalWorkers.ALICE;
import static mcscheduler.testutil.TypicalWorkers.BENSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import mcscheduler.commons.core.GuiSettings;
import mcscheduler.model.shift.ShiftDayOrTimeContainsKeywordsPredicate;
import mcscheduler.model.worker.NameContainsKeywordsPredicate;
import mcscheduler.testutil.McSchedulerBuilder;

//@@author
public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new McScheduler(), new McScheduler(modelManager.getMcScheduler()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setMcSchedulerFilePath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setMcSchedulerFilePath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setMcSchedulerFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setMcSchedulerFilePath(null));
    }

    @Test
    public void setMcSchedulerFilePath_validPath_setsMcSchedulerFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setMcSchedulerFilePath(path);
        assertEquals(path, modelManager.getMcSchedulerFilePath());
    }

    @Test
    public void hasWorker_nullWorker_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasWorker(null));
    }

    @Test
    public void hasWorker_workerNotInMcScheduler_returnsFalse() {
        assertFalse(modelManager.hasWorker(ALICE));
    }

    @Test
    public void hasWorker_workerInMcScheduler_returnsTrue() {
        modelManager.addWorker(ALICE);
        assertTrue(modelManager.hasWorker(ALICE));
    }

    @Test
    public void getFilteredWorkerList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredWorkerList().remove(0));
    }

    @Test
    public void hasShift_nullShift_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasShift(null));
    }

    @Test
    public void hasShift_shiftNotInMcScheduler_returnsFalse() {
        assertFalse(modelManager.hasShift(SHIFT_A));
    }

    @Test
    public void hasShift_shiftInMcScheduler_returnsTrue() {
        modelManager.addShift(SHIFT_A);
        assertTrue(modelManager.hasShift(SHIFT_A));
    }

    @Test
    public void getFilteredShiftList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredShiftList().remove(0));
    }

    @Test
    public void equals() {
        McScheduler mcScheduler = new McSchedulerBuilder()
                .withWorker(ALICE).withWorker(BENSON)
                .withShift(SHIFT_A).withShift(SHIFT_B)
                .build();
        McScheduler differentMcScheduler = new McScheduler();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(mcScheduler, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(mcScheduler, userPrefs);
        assertEquals(modelManagerCopy, modelManager);

        // same object -> returns true
        assertEquals(modelManager, modelManager);

        // null -> returns false
        assertNotEquals(modelManager, null);

        // different types -> returns false
        assertNotEquals(modelManager, 5);

        // different mcScheduler -> returns false
        assertNotEquals(new ModelManager(differentMcScheduler, userPrefs), modelManager);

        // different filteredWorkerList -> returns false
        String[] workerKeywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredWorkerList(new NameContainsKeywordsPredicate(Arrays.asList(workerKeywords)));
        assertNotEquals(new ModelManager(mcScheduler, userPrefs), modelManager);

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredWorkerList(PREDICATE_SHOW_ALL_WORKERS);

        //different filteredShiftList -> returns false
        List<String> shiftKeywords = Arrays.asList(SHIFT_A.getShiftTime().toString());
        modelManager.updateFilteredShiftList(new ShiftDayOrTimeContainsKeywordsPredicate(shiftKeywords));
        assertNotEquals(new ModelManager(mcScheduler, userPrefs), modelManager);

        //resets modelManager to initial state
        modelManager.updateFilteredShiftList(PREDICATE_SHOW_ALL_SHIFTS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setMcSchedulerFilePath(Paths.get("differentFilePath"));
        assertNotEquals(new ModelManager(mcScheduler, differentUserPrefs), modelManager);
    }
}
