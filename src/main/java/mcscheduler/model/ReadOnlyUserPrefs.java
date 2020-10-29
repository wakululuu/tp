package mcscheduler.model;

import java.nio.file.Path;

import mcscheduler.commons.core.GuiSettings;

/**
 * Unmodifiable view of user prefs.
 */
public interface ReadOnlyUserPrefs {

    GuiSettings getGuiSettings();

    Path getMcSchedulerFilePath();

}
