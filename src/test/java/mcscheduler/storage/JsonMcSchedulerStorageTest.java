package mcscheduler.storage;

import static mcscheduler.testutil.Assert.assertThrows;
import static mcscheduler.testutil.McSchedulerBuilder.getTypicalMcScheduler;
import static mcscheduler.testutil.TypicalWorkers.ALICE;
import static mcscheduler.testutil.TypicalWorkers.HOON;
import static mcscheduler.testutil.TypicalWorkers.IDA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import mcscheduler.commons.exceptions.DataConversionException;
import mcscheduler.model.McScheduler;
import mcscheduler.model.ReadOnlyMcScheduler;

public class JsonMcSchedulerStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonMcSchedulerStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readMcScheduler_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readMcScheduler(null));
    }

    private java.util.Optional<ReadOnlyMcScheduler> readMcScheduler(String filePath) throws Exception {
        return new JsonMcSchedulerStorage(Paths.get(filePath)).readMcScheduler(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
            ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
            : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readMcScheduler("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataConversionException.class, () -> readMcScheduler("notJsonFormatMcScheduler.json"));
    }

    @Test
    public void readMcScheduler_invalidWorkerMcScheduler_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readMcScheduler("invalidWorkerMcScheduler.json"));
    }

    @Test
    public void readMcScheduler_invalidAndValidWorkerMcScheduler_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readMcScheduler("invalidAndValidWorkerMcScheduler.json"));
    }

    @Test
    public void readAndSaveMcScheduler_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempMcScheduler.json");
        McScheduler original = getTypicalMcScheduler();
        JsonMcSchedulerStorage jsonMcSchedulerStorage = new JsonMcSchedulerStorage(filePath);

        // Save in new file and read back
        jsonMcSchedulerStorage.saveMcScheduler(original, filePath);
        ReadOnlyMcScheduler readBack = jsonMcSchedulerStorage.readMcScheduler(filePath).get();
        assertEquals(original, new McScheduler(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addWorker(HOON);
        original.removeWorker(ALICE);
        jsonMcSchedulerStorage.saveMcScheduler(original, filePath);
        readBack = jsonMcSchedulerStorage.readMcScheduler(filePath).get();
        assertEquals(original, new McScheduler(readBack));

        // Save and read without specifying file path
        original.addWorker(IDA);
        jsonMcSchedulerStorage.saveMcScheduler(original); // file path not specified
        readBack = jsonMcSchedulerStorage.readMcScheduler().get(); // file path not specified
        assertEquals(original, new McScheduler(readBack));

    }

    @Test
    public void saveMcScheduler_nullMcScheduler_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveMcScheduler(null, "SomeFile.json"));
    }

    /**
     * Saves {@code mcScheduler} at the specified {@code filePath}.
     */
    private void saveMcScheduler(ReadOnlyMcScheduler mcScheduler, String filePath) {
        try {
            new JsonMcSchedulerStorage(Paths.get(filePath))
                .saveMcScheduler(mcScheduler, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveMcScheduler_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveMcScheduler(new McScheduler(), null));
    }
}
