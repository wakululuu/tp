package mcscheduler.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import mcscheduler.commons.exceptions.DataConversionException;
import mcscheduler.model.McScheduler;
import mcscheduler.model.ReadOnlyMcScheduler;

/**
 * Represents a storage for {@link McScheduler}.
 */
public interface McSchedulerStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getMcSchedulerFilePath();

    /**
     * Returns McScheduler data as a {@link ReadOnlyMcScheduler}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyMcScheduler> readMcScheduler() throws DataConversionException, IOException;

    /**
     * @see #getMcSchedulerFilePath()
     */
    Optional<ReadOnlyMcScheduler> readMcScheduler(Path filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyMcScheduler} to the storage.
     * @param mcScheduler cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveMcScheduler(ReadOnlyMcScheduler mcScheduler) throws IOException;

    /**
     * @see #saveMcScheduler(ReadOnlyMcScheduler)
     */
    void saveMcScheduler(ReadOnlyMcScheduler mcScheduler, Path filePath) throws IOException;

}
