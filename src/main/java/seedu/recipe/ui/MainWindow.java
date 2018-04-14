package seedu.recipe.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.recipe.MainApp;
import seedu.recipe.commons.core.Config;
import seedu.recipe.commons.core.GuiSettings;
import seedu.recipe.commons.core.LogsCenter;
import seedu.recipe.commons.events.ui.ChangeThemeRequestEvent;
import seedu.recipe.commons.events.ui.ExitAppRequestEvent;
import seedu.recipe.commons.events.ui.ShowHelpRequestEvent;
import seedu.recipe.commons.events.ui.WebParseRequestEvent;
import seedu.recipe.logic.Logic;
import seedu.recipe.model.UserPrefs;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    public static final String GIRL_THEME_CSS = "GirlTheme.css";
    public static final String LIGHT_THEME_CSS = "LightTheme.css";
    public static final String EXTENSIONS_CSS = "Extensions.css";

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private BrowserPanel browserPanel;
    private CommandBox commandBox;
    private RecipeListPanel recipeListPanel;
    private Config config;
    private UserPrefs prefs;

    @FXML
    private StackPane browserPlaceholder;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem changeThemeItem;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane recipeListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    public MainWindow(Stage primaryStage, Config config, UserPrefs prefs, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;
        this.config = config;
        this.prefs = prefs;

        // Configure the UI
        setTitle(config.getAppTitle());
        setWindowDefaultSize(prefs);

        setAccelerators();
        registerAsAnEventHandler(this);

        loadStyle(prefs.getIsUsingGirlTheme());
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
        setAccelerator(changeThemeItem, KeyCombination.valueOf("F2"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     *
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if ((event.getTarget() instanceof TextInputControl || event.getTarget() instanceof ListView)
                    && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        browserPanel = new BrowserPanel(prefs.getIsUsingGirlTheme());
        browserPlaceholder.getChildren().add(browserPanel.getRoot());

        recipeListPanel = new RecipeListPanel(logic.getFilteredRecipeList());
        recipeListPanelPlaceholder.getChildren().add(recipeListPanel.getRoot());

        ResultDisplay resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(prefs.getRecipeBookFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        commandBox = new CommandBox(logic);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    void hide() {
        primaryStage.hide();
    }

    private void setTitle(String appTitle) {
        primaryStage.setTitle(appTitle);
    }

    /**
     * Sets the default size based on user preferences.
     */
    private void setWindowDefaultSize(UserPrefs prefs) {
        primaryStage.setHeight(prefs.getGuiSettings().getWindowHeight());
        primaryStage.setWidth(prefs.getGuiSettings().getWindowWidth());
        if (prefs.getGuiSettings().getWindowCoordinates() != null) {
            primaryStage.setX(prefs.getGuiSettings().getWindowCoordinates().getX());
            primaryStage.setY(prefs.getGuiSettings().getWindowCoordinates().getY());
        }
    }

    /**
     * Returns the current size and the position of the main Window.
     */
    GuiSettings getCurrentGuiSetting() {
        return new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
    }

    //@@author kokonguyen191
    @Subscribe
    private void handleChangeThemeRequestEvent(ChangeThemeRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        handleChangeTheme();
    }

    /**
     * Toggles between dark and light theme
     */
    @FXML
    public void handleChangeTheme() {
        boolean isUsingGirlTheme = prefs.getIsUsingGirlTheme();
        browserPanel.loadDefaultPage(!isUsingGirlTheme);
        loadStyle(!isUsingGirlTheme);
        prefs.setIsUsingGirlTheme(!isUsingGirlTheme);
    }

    @Subscribe
    private void handleWebParseRequestEvent(WebParseRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        String parsedRecipe = browserPanel.parseRecipe();
        if (parsedRecipe != null) {
            commandBox.replaceText(parsedRecipe);
        } else {
            commandBox.replaceText("");
        }
    }

    //@@author

    /**
     * Opens the help window.
     */
    @FXML
    public void handleHelp() {
        HelpWindow helpWindow = new HelpWindow();
        helpWindow.show();
    }

    void show() {
        primaryStage.show();
    }

    //@@author kokonguyen191
    /**
     * Toggles the main window theme
     */
    private void loadStyle(boolean darkTheme) {
        Scene scene = primaryStage.getScene();
        scene.getStylesheets().clear();
        if (darkTheme) {
            scene.getStylesheets().add(MainApp.class.getResource(FXML_FILE_FOLDER + GIRL_THEME_CSS).toExternalForm());
        } else {
            scene.getStylesheets().add(MainApp.class.getResource(FXML_FILE_FOLDER + LIGHT_THEME_CSS).toExternalForm());
        }
        scene.getStylesheets().add(MainApp.class.getResource(FXML_FILE_FOLDER + EXTENSIONS_CSS).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    //@@author

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        raise(new ExitAppRequestEvent());
    }

    public RecipeListPanel getRecipeListPanel() {
        return this.recipeListPanel;
    }

    void releaseResources() {
        browserPanel.freeResources();
    }

    @Subscribe
    private void handleShowHelpEvent(ShowHelpRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        handleHelp();
    }
}
