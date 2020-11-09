package mcscheduler.storage;

import static mcscheduler.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import mcscheduler.commons.exceptions.IllegalValueException;
import mcscheduler.commons.util.JsonUtil;
import mcscheduler.model.McScheduler;
import mcscheduler.testutil.McSchedulerBuilder;

public class JsonSerializableMcSchedulerTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableMcSchedulerTest");
    private static final Path TYPICAL_WORKERS_FILE = TEST_DATA_FOLDER.resolve("typicalWorkersMcScheduler.json");
    private static final Path INVALID_WORKER_FILE = TEST_DATA_FOLDER.resolve("invalidWorkerMcScheduler.json");
    private static final Path DUPLICATE_WORKER_FILE = TEST_DATA_FOLDER.resolve("duplicateWorkerMcScheduler.json");

    @Test
    public void toModelType_typicalWorkersFile_success() throws Exception {
        JsonSerializableMcScheduler dataFromFile = JsonUtil.readJsonFile(TYPICAL_WORKERS_FILE,
                JsonSerializableMcScheduler.class).get();
        McScheduler mcSchedulerFromFile = dataFromFile.toModelType();
        McScheduler typicalWorkersMcScheduler = McSchedulerBuilder.getTypicalMcScheduler();
        assertEquals(mcSchedulerFromFile, typicalWorkersMcScheduler);
    }

    @Test
    public void toModelType_invalidWorkerFile_throwsIllegalValueException() throws Exception {
        JsonSerializableMcScheduler dataFromFile = JsonUtil.readJsonFile(INVALID_WORKER_FILE,
                JsonSerializableMcScheduler.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateWorkers_throwsIllegalValueException() throws Exception {
        JsonSerializableMcScheduler dataFromFile = JsonUtil.readJsonFile(DUPLICATE_WORKER_FILE,
                JsonSerializableMcScheduler.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableMcScheduler.MESSAGE_DUPLICATE_WORKER,
                dataFromFile::toModelType);
    }

}
