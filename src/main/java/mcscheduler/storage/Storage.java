package mcscheduler.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import mcscheduler.commons.exceptions.DataConversionException;
import mcscheduler.model.ReadOnlyMcScheduler;
import mcscheduler.model.ReadOnlyUserPrefs;
import mcscheduler.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends McSchedulerStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    @Override
    Path getMcSchedulerFilePath();

    @Override
    Optional<ReadOnlyMcScheduler> readMcScheduler() throws DataConversionException, IOException;

    @Override
    void saveMcScheduler(ReadOnlyMcScheduler mcScheduler) throws IOException;

}
