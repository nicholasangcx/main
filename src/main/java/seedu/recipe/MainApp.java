package seedu.recipe;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import seedu.recipe.commons.core.Config;
import seedu.recipe.commons.core.EventsCenter;
import seedu.recipe.commons.core.LogsCenter;
import seedu.recipe.commons.core.Version;
import seedu.recipe.commons.events.ui.ExitAppRequestEvent;
import seedu.recipe.commons.exceptions.DataConversionException;
import seedu.recipe.commons.util.ConfigUtil;
import seedu.recipe.commons.util.StringUtil;
import seedu.recipe.logic.Logic;
import seedu.recipe.logic.LogicManager;
import seedu.recipe.model.Model;
import seedu.recipe.model.ModelManager;
import seedu.recipe.model.ReadOnlyRecipeBook;
import seedu.recipe.model.RecipeBook;
import seedu.recipe.model.UserPrefs;
import seedu.recipe.model.util.SampleDataUtil;
import seedu.recipe.storage.RecipeBookStorage;
import seedu.recipe.storage.JsonUserPrefsStorage;
import seedu.recipe.storage.Storage;
import seedu.recipe.storage.StorageManager;
import seedu.recipe.storage.UserPrefsStorage;
import seedu.recipe.storage.XmlRecipeBookStorage;
import seedu.recipe.ui.Ui;
import seedu.recipe.ui.UiManager;

/**
 * The main entry point to the application.
 */
public class MainApp extends Application {

    public static final Version VERSION = new Version(0, 6, 0, true);

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;
    protected UserPrefs userPrefs;


    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing RecipeBook ]===========================");
        super.init();

        config = initConfig(getApplicationParameter("config"));

        UserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(config.getUserPrefsFilePath());
        userPrefs = initPrefs(userPrefsStorage);
        RecipeBookStorage recipeBookStorage = new XmlRecipeBookStorage(userPrefs.getRecipeBookFilePath());
        storage = new StorageManager(recipeBookStorage, userPrefsStorage);

        initLogging(config);

        model = initModelManager(storage, userPrefs);

        logic = new LogicManager(model);

        ui = new UiManager(logic, config, userPrefs);

        initEventsCenter();
    }

    private String getApplicationParameter(String parameterName) {
        Map<String, String> applicationParameters = getParameters().getNamed();
        return applicationParameters.get(parameterName);
    }

    /**
     * Returns a {@code ModelManager} with the data from {@code storage}'s recipe book and {@code userPrefs}. <br>
     * The data from the sample recipe book will be used instead if {@code storage}'s recipe book is not found,
     * or an empty recipe book will be used instead if errors occur when reading {@code storage}'s recipe book.
     */
    private Model initModelManager(Storage storage, UserPrefs userPrefs) {
        Optional<ReadOnlyRecipeBook> recipeBookOptional;
        ReadOnlyRecipeBook initialData;
        try {
            recipeBookOptional = storage.readRecipeBook();
            if (!recipeBookOptional.isPresent()) {
                logger.info("Data file not found. Will be starting with a sample RecipeBook");
            }
            initialData = recipeBookOptional.orElseGet(SampleDataUtil::getSampleRecipeBook);
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Will be starting with an empty RecipeBook");
            initialData = new RecipeBook();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty RecipeBook");
            initialData = new RecipeBook();
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
    protected Config initConfig(String configFilePath) {
        Config initializedConfig;
        String configFilePathUsed;

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
        String prefsFilePath = storage.getUserPrefsFilePath();
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
            logger.warning("Problem while reading from the file. Will be starting with an empty RecipeBook");
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

    private void initEventsCenter() {
        EventsCenter.getInstance().registerHandler(this);
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting RecipeBook " + MainApp.VERSION);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping Instruction Book ] =============================");
        ui.stop();
        try {
            storage.saveUserPrefs(userPrefs);
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
        Platform.exit();
        System.exit(0);
    }

    @Subscribe
    public void handleExitAppRequestEvent(ExitAppRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        this.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
