package mcscheduler.testutil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import mcscheduler.commons.core.index.Index;
import mcscheduler.model.Model;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.worker.Worker;

/**
 * A utility class for test cases.
 */
public class TestUtil {

    /**
     * Folder used for temp files created during testing. Ignored by Git.
     */
    private static final Path SANDBOX_FOLDER = Paths.get("src", "test", "data", "sandbox");

    /**
     * Appends {@code fileName} to the sandbox folder path and returns the resulting path.
     * Creates the sandbox folder if it doesn't exist.
     */
    public static Path getFilePathInSandboxFolder(String fileName) {
        try {
            Files.createDirectories(SANDBOX_FOLDER);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return SANDBOX_FOLDER.resolve(fileName);
    }

    /**
     * Returns the last index of the worker in the {@code model}'s worker list.
     */
    public static Index getLastWorkerIndex(Model model) {
        return Index.fromOneBased(model.getFilteredWorkerList().size());
    }

    /**
     * Returns the last index of the shift in the {@code model}'s shift list.
     */
    public static Index getLastShiftIndex(Model model) {
        return Index.fromOneBased(model.getFilteredShiftList().size());
    }

    /**
     * Returns an out of bound index of the worker in the {@code model}'s worker list.
     */
    public static Index getOutOfBoundWorkerIndex(Model model) {
        return Index.fromOneBased(model.getFilteredWorkerList().size() + 1);
    }

    /**
     * Returns an out of bound index of the shift in the {@code model}'s shift list.
     */
    public static Index getOutOfBoundShiftIndex(Model model) {
        return Index.fromOneBased(model.getFilteredShiftList().size() + 1);
    }

    /**
     * Returns the worker in the {@code model}'s worker list at {@code index}.
     */
    public static Worker getWorker(Model model, Index index) {
        return model.getFilteredWorkerList().get(index.getZeroBased());
    }

    /**
     * Returns the shift in the {@code model}'s shift list at {@code index}.
     */
    public static Shift getShift(Model model, Index index) {
        return model.getFilteredShiftList().get(index.getZeroBased());
    }
}
