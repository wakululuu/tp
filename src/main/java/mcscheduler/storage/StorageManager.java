package mcscheduler.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import mcscheduler.commons.core.LogsCenter;
import mcscheduler.commons.exceptions.DataConversionException;
import mcscheduler.model.ReadOnlyMcScheduler;
import mcscheduler.model.ReadOnlyUserPrefs;
import mcscheduler.model.UserPrefs;

/**
 * Manages storage of McScheduler data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private final McSchedulerStorage mcSchedulerStorage;
    private final UserPrefsStorage userPrefsStorage;

    /**
     * Creates a {@code StorageManager} with the given {@code McSchedulerStorage} and {@code UserPrefStorage}.
     */
    public StorageManager(McSchedulerStorage mcSchedulerStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.mcSchedulerStorage = mcSchedulerStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ McScheduler methods ==============================

    @Override
    public Path getMcSchedulerFilePath() {
        return mcSchedulerStorage.getMcSchedulerFilePath();
    }

    @Override
    public Optional<ReadOnlyMcScheduler> readMcScheduler() throws DataConversionException, IOException {
        return readMcScheduler(mcSchedulerStorage.getMcSchedulerFilePath());
    }

    @Override
    public Optional<ReadOnlyMcScheduler> readMcScheduler(Path filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return mcSchedulerStorage.readMcScheduler(filePath);
    }

    @Override
    public void saveMcScheduler(ReadOnlyMcScheduler mcScheduler) throws IOException {
        saveMcScheduler(mcScheduler, mcSchedulerStorage.getMcSchedulerFilePath());
    }

    @Override
    public void saveMcScheduler(ReadOnlyMcScheduler mcScheduler, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        mcSchedulerStorage.saveMcScheduler(mcScheduler, filePath);
    }

}
