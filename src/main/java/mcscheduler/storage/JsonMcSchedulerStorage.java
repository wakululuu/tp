package mcscheduler.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import mcscheduler.commons.core.LogsCenter;
import mcscheduler.commons.exceptions.DataConversionException;
import mcscheduler.commons.exceptions.IllegalValueException;
import mcscheduler.commons.util.FileUtil;
import mcscheduler.commons.util.JsonUtil;
import mcscheduler.model.ReadOnlyMcScheduler;

/**
 * A class to access McScheduler data stored as a json file on the hard disk.
 */
public class JsonMcSchedulerStorage implements McSchedulerStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonMcSchedulerStorage.class);

    private final Path filePath;

    public JsonMcSchedulerStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getMcSchedulerFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyMcScheduler> readMcScheduler() throws DataConversionException {
        return readMcScheduler(filePath);
    }

    /**
     * Similar to {@link #readMcScheduler()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyMcScheduler> readMcScheduler(Path filePath) throws DataConversionException {
        requireNonNull(filePath);

        Optional<JsonSerializableMcScheduler> jsonMcScheduler = JsonUtil.readJsonFile(
                filePath, JsonSerializableMcScheduler.class);
        if (!jsonMcScheduler.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonMcScheduler.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveMcScheduler(ReadOnlyMcScheduler mcScheduler) throws IOException {
        saveMcScheduler(mcScheduler, filePath);
    }

    /**
     * Similar to {@link #saveMcScheduler(ReadOnlyMcScheduler)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveMcScheduler(ReadOnlyMcScheduler mcScheduler, Path filePath) throws IOException {
        requireNonNull(mcScheduler);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableMcScheduler(mcScheduler), filePath);
    }

}
