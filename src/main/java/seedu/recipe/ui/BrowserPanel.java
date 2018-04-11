package seedu.recipe.ui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;
import seedu.recipe.MainApp;
import seedu.recipe.commons.core.LogsCenter;
import seedu.recipe.commons.core.index.Index;
import seedu.recipe.commons.events.ui.InternetSearchRequestEvent;
import seedu.recipe.commons.events.ui.RecipePanelSelectionChangedEvent;
import seedu.recipe.commons.events.ui.ShareRecipeEvent;
import seedu.recipe.commons.events.ui.UploadRecipesEvent;
import seedu.recipe.model.recipe.Recipe;
import seedu.recipe.model.recipe.Url;
import seedu.recipe.model.util.HtmlFormatter;
import seedu.recipe.ui.parser.WebParser;
import seedu.recipe.ui.parser.WebParserHandler;
import seedu.recipe.ui.util.CloudStorageUtil;
import seedu.recipe.ui.util.FacebookHandler;
import seedu.recipe.ui.util.UiUtil;

/**
 * The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {

    public static final String DEFAULT_PAGE_DARK = "defaultdark.html";
    public static final String DEFAULT_PAGE_LIGHT = "defaultlight.html";
    public static final String SEARCH_PAGE_URL =
            "https://se-edu.github.io/addressbook-level4/DummySearchPage.html?name=";
    private static final String FXML = "BrowserPanel.fxml";
    private static final Index FIRST_INDEX = Index.fromOneBased(1);

    private Recipe recipeToShare;
    private WebParserHandler webParserHandler;

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    @FXML
    private WebView browser;

    public BrowserPanel(boolean isDarkTheme) {
        super(FXML);

        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);

        initializeWebParserHandler();
        loadDefaultPage(isDarkTheme);
        registerAsAnEventHandler(this);

    }

    public WebView getBrowser() {
        return browser;
    }

    public void loadPage(String url) {
        Platform.runLater(() -> browser.getEngine().load(url));
    }

    /**
     * Loads a website on the user's external default browser based on the
     *
     * @param url provided, if it is valid.
     */
    public void loadPageExternalBrowser(String url) {
        Desktop d = Desktop.getDesktop();
        try {
            d.browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            throw new AssertionError("URL wrong format exception " + e.getMessage());
        }
    }

    private void loadRecipePage(Recipe recipe) {
        loadPage(recipe.getUrl().toString());
    }

    //@@author RyanAngJY

    /**
     * Loads the text recipe onto the browser
     */
    private void loadLocalRecipe(Recipe recipe) {
        browser.getEngine().loadContent(HtmlFormatter.getHtmlFormat(recipe));
    }

    //@@author kokonguyen191

    /**
     * Loads a default HTML file with a background that matches the general theme.
     *
     * @param isDarkTheme true if the app is using dark theme
     */
    public void loadDefaultPage(boolean isDarkTheme) {
        if (!isLoaded()) {
            URL defaultPage;
            if (isDarkTheme) {
                defaultPage = MainApp.class.getResource(FXML_FILE_FOLDER + DEFAULT_PAGE_DARK);
            } else {
                defaultPage = MainApp.class.getResource(FXML_FILE_FOLDER + DEFAULT_PAGE_LIGHT);
            }
            loadPage(defaultPage.toExternalForm());
            logger.info("BrowserPanel is empty, changed theme and reloaded BrowserPanel.");
        } else {
            logger.info("BrowserPanel is not empty, changed theme without reloading BrowserPanel.");
        }
    }

    /**
     * Returns true if BrowserPanel is loaded with a page that is neither null nor default
     */
    private boolean isLoaded() {
        URL lightTheme = MainApp.class.getResource(FXML_FILE_FOLDER + DEFAULT_PAGE_LIGHT);
        URL darkTheme = MainApp.class.getResource(FXML_FILE_FOLDER + DEFAULT_PAGE_DARK);

        String loadedUrlString = browser.getEngine().getLocation();
        if (loadedUrlString == null) {
            return false;
        } else {
            URL loadedUrl = null;
            try {
                loadedUrl = new URL(loadedUrlString);
            } catch (MalformedURLException murle) {
                throw new AssertionError("Something wrong happened when the app is trying to read the "
                        + "url loaded inside BrowserPanel. This should not happen.", murle);
            }
            boolean isLightThemeLoaded = loadedUrl.equals(lightTheme);
            boolean isDarkThemeLoaded = loadedUrl.equals(darkTheme);

            boolean isBlankPageLoaded = isLightThemeLoaded || isDarkThemeLoaded;

            return !isBlankPageLoaded;
        }
    }
    //@@author

    /**
     * Frees resources allocated to the browser.
     */
    public void freeResources() {
        browser = null;
    }

    @Subscribe
    private void handleRecipePanelSelectionChangedEvent(RecipePanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        Recipe recipe = event.getNewSelection().recipe;
        if (recipe.getUrl().toString().equals(Url.NULL_URL_REFERENCE)) {
            loadLocalRecipe(recipe);
        } else {
            loadRecipePage(recipe);
        }
    }

    //@@author kokonguyen191
    @Subscribe
    private void handleInternetSearchRequestEvent(InternetSearchRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        if (event.wikiaQueryHandler.getQueryNumberOfResults() != 0) {
            loadPage(event.wikiaQueryHandler.getRecipeQueryUrl());
        }
    }

    private void initializeWebParserHandler() {
        webParserHandler = new WebParserHandler(browser);
    }

    /**
     * Parses the BrowserPanel, gets an AddCommand string.
     */
    public String parseRecipe() {
        WebParser webParser = webParserHandler.getWebParser();
        if (webParser != null) {
            return webParser.parseRecipe();
        } else {
            return null;
        }
    }

    //@@author RyanAngJY
    @Subscribe
    private void handleShareRecipeEvent(ShareRecipeEvent event) {
        recipeToShare = event.getTargetRecipe();
        String urlToShare = recipeToShare.getUrl().toString();
        UiUtil.copyToClipboard(recipeToShare.getTextFormattedRecipe());

        if (!urlToShare.equals(Url.NULL_URL_REFERENCE)) {
            loadPage(FacebookHandler.getPostDomain() + recipeToShare.getUrl().toString()
                    + FacebookHandler.getRedirectEmbedded());
        } else {
            loadPage(FacebookHandler.REDIRECT_DOMAIN);
        }
    }

    //@@author nicholasangcx
    @Subscribe
    private void handleUploadRecipesEvent(UploadRecipesEvent event) {
        loadPage(CloudStorageUtil.getAuthorizationUrl());
    }
    //@@author
}
