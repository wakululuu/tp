package mcscheduler;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.stage.Stage;
import mcscheduler.commons.core.Config;
import mcscheduler.commons.core.LogsCenter;
import mcscheduler.commons.core.Version;
import mcscheduler.commons.exceptions.DataConversionException;
import mcscheduler.commons.util.ConfigUtil;
import mcscheduler.commons.util.StringUtil;
import mcscheduler.logic.Logic;
import mcscheduler.logic.LogicManager;
import mcscheduler.model.McScheduler;
import mcscheduler.model.Model;
import mcscheduler.model.ModelManager;
import mcscheduler.model.ReadOnlyMcScheduler;
import mcscheduler.model.ReadOnlyUserPrefs;
import mcscheduler.model.UserPrefs;
import mcscheduler.model.util.SampleDataUtil;
import mcscheduler.storage.JsonMcSchedulerStorage;
import mcscheduler.storage.JsonUserPrefsStorage;
import mcscheduler.storage.McSchedulerStorage;
import mcscheduler.storage.Storage;
import mcscheduler.storage.StorageManager;
import mcscheduler.storage.UserPrefsStorage;
import mcscheduler.ui.Ui;
import mcscheduler.ui.UiManager;

/**
 * Runs the application.
 */
public class MainApp extends Application {

    public static final Version VERSION = new Version(1, 4, 0, true);

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;

    private boolean couldLoad;

    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing McScheduler ]===========================");
        super.init();

        AppParameters appParameters = AppParameters.parse(getParameters());
        config = initConfig(appParameters.getConfigPath());

        UserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(config.getUserPrefsFilePath());
        UserPrefs userPrefs = initPrefs(userPrefsStorage);
        McSchedulerStorage mcSchedulerStorage = new JsonMcSchedulerStorage(userPrefs.getMcSchedulerFilePath());
        storage = new StorageManager(mcSchedulerStorage, userPrefsStorage);

        initLogging(config);

        model = initModelManager(storage, userPrefs);

        logic = new LogicManager(model, storage);

        ui = new UiManager(logic);
    }

    /**
     * Returns a {@code ModelManager} with the data from {@code storage}'s McScheduler and {@code userPrefs}. <br>
     * The data from the sample McScheduler will be used instead if {@code storage}'s McScheduler is not found,
     * or an empty McScheduler will be used instead if errors occur when reading {@code storage}'s McScheduler.
     */
    private Model initModelManager(Storage storage, ReadOnlyUserPrefs userPrefs) {
        Optional<ReadOnlyMcScheduler> mcSchedulerOptional;
        ReadOnlyMcScheduler initialData;
        try {
            mcSchedulerOptional = storage.readMcScheduler();
            if (!mcSchedulerOptional.isPresent()) {
                logger.info("Data file not found. Will be starting with a sample McScheduler");
            }
            initialData = mcSchedulerOptional.orElseGet(SampleDataUtil::getSampleMcScheduler);
            couldLoad = true;
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Will be starting with an empty McScheduler");
            couldLoad = false;
            initialData = new McScheduler();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty McScheduler");
            couldLoad = false;
            initialData = new McScheduler();
        }

        return new ModelManager(initialData, userPrefs);
    }

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

    /**
     * Returns a {@code Config} using the file at {@code configFilePath}. <br>
     * The default file path {@code Config#DEFAULT_CONFIG_FILE} will be used instead
     * if {@code configFilePath} is null.
     */
    protected Config initConfig(Path configFilePath) {
        Config initializedConfig;
        Path configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if (configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            logger.warning("Config file at " + configFilePathUsed + " is not in the correct format. "
                    + "Using default config properties");
            initializedConfig = new Config();
        }

        //Update config file in case it was missing to begin with or there are new/unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    /**
     * Returns a {@code UserPrefs} using the file at {@code storage}'s user prefs file path,
     * or a new {@code UserPrefs} with default configuration if errors occur when
     * reading from the file.
     */
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        Path prefsFilePath = storage.getUserPrefsFilePath();
        logger.info("Using prefs file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataConversionException e) {
            logger.warning("UserPrefs file at " + prefsFilePath + " is not in the correct format. "
                    + "Using default user prefs");
            initializedPrefs = new UserPrefs();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty McScheduler");
            initializedPrefs = new UserPrefs();
        }

        //Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting McScheduler " + MainApp.VERSION);
        ui.start(primaryStage, couldLoad);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping McScheduler ] =============================");
        try {
            storage.saveUserPrefs(model.getUserPrefs());
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
    }
}
