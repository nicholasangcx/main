# nicholasangcx
###### \java\seedu\recipe\commons\events\ui\UploadRecipesEvent.java
``` java
package seedu.recipe.commons.events.ui;

import seedu.recipe.commons.events.BaseEvent;

/**
 * Indicates a request to upload recipes to Dropbox
 */
public class UploadRecipesEvent extends BaseEvent {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
```
###### \java\seedu\recipe\logic\commands\AccessTokenCommand.java
``` java
package seedu.recipe.logic.commands;

import com.dropbox.core.DbxException;

import seedu.recipe.ui.util.CloudStorageUtil;

/**
 * Takes in the access token given by dropbox and saves it in the app
 * to allow user to upload files easily.
 */
public class AccessTokenCommand extends Command {

    public static final String COMMAND_WORD = "token";
    public static final String MESSAGE_SUCCESS = "Upload Success!";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Takes in the access token given by Dropbox "
            + "to allow user to upload files. *ONLY use this command directly after the upload command.*\n"
            + "Parameters: TOKEN\n"
            + "Example: " + COMMAND_WORD + "VALID_ACCESS_TOKEN";

    private final String accessCode;

    public AccessTokenCommand(String code) {
        this.accessCode = code;
    }

    @Override
    public CommandResult execute() throws DbxException {
        CloudStorageUtil.processAuthorizationCode(accessCode);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AccessTokenCommand // instanceof handles nulls
                && accessCode.equals(((AccessTokenCommand) other).accessCode));
    }
}
```
###### \java\seedu\recipe\logic\commands\Command.java
``` java
    /**
     * Constructs a feedback message to summarise an operation that displayed
     * a listing of recipes with the specified tags.
     *
     * @param displaySize indicates the number of recipe listed, used to generate summary
     * @param tagKeywords the tags searched for, used to generate summary
     * @return summary message for recipes displayed
     */
    public static String getMessageForTagListShownSummary(int displaySize, String tagKeywords) {
        return String.format(Messages.MESSAGE_RECIPES_WITH_TAGS_LISTED_OVERVIEW, displaySize, tagKeywords);
    }

    /**
     * Constructs a feedback message to summarise an operation that displayed
     * a listing of recipes with the specified ingredients.
     *
     * @param displaySize indicates the number of recipe listed, used to generate summary
     * @param ingredientKeywords the ingredients searched for, used to generate summary
     * @return summary message for recipes displayed
     */
    public static String getMessageForIngredientListShownSummary(int displaySize, String ingredientKeywords) {
        return String.format(Messages.MESSAGE_RECIPES_WITH_INGREDIENTS_LISTED_OVERVIEW,
                displaySize, ingredientKeywords);
    }
```
###### \java\seedu\recipe\logic\commands\IngredientCommand.java
``` java
package seedu.recipe.logic.commands;

import java.util.Arrays;

import seedu.recipe.model.recipe.IngredientContainsKeywordsPredicate;

/**
 * Finds and lists all recipes in recipe book whose ingredient contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class IngredientCommand extends Command {

    public static final String COMMAND_WORD = "ingredient";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all recipes whose ingredients contain ALL of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " chicken";

    private final IngredientContainsKeywordsPredicate predicate;
    private final String[] ingredientKeywords;

    public IngredientCommand(IngredientContainsKeywordsPredicate predicate, String[] ingredientKeywords) {
        this.predicate = predicate;
        this.ingredientKeywords = ingredientKeywords;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredRecipeList(predicate);
        return new CommandResult(getMessageForIngredientListShownSummary
                (model.getFilteredRecipeList().size(), Arrays.toString(ingredientKeywords)));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if they are the same object
                || (other instanceof IngredientCommand // instanceof handles nulls
                && this.predicate.equals(((IngredientCommand) other).predicate)); // state check
    }
}
```
###### \java\seedu\recipe\logic\commands\TagCommand.java
``` java
package seedu.recipe.logic.commands;

import java.util.Arrays;

import seedu.recipe.model.tag.TagContainsKeywordsPredicate;

/**
 * Finds and lists all recipes in recipe book whose tag contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class TagCommand extends Command {

    public static final String COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all recipes whose tags contain any of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " favourite";

    // Contains a list of strings of the keywords
    private final TagContainsKeywordsPredicate predicate;
    private final String[] tagKeywords;

    public TagCommand(TagContainsKeywordsPredicate predicate, String[] tagKeywords) {
        this.predicate = predicate;
        this.tagKeywords = tagKeywords;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredRecipeList(predicate);
        return new CommandResult(getMessageForTagListShownSummary
                                    (model.getFilteredRecipeList().size(), Arrays.toString(tagKeywords)));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TagCommand // instanceof handles nulls
                && this.predicate.equals(((TagCommand) other).predicate)); // state check
    }
}
```
###### \java\seedu\recipe\logic\commands\UploadCommand.java
``` java
package seedu.recipe.logic.commands;

import static seedu.recipe.model.file.Filename.MESSAGE_FILENAME_CONSTRAINTS;

import com.dropbox.core.DbxException;

import seedu.recipe.commons.core.EventsCenter;
import seedu.recipe.commons.events.ui.UploadRecipesEvent;
import seedu.recipe.ui.util.CloudStorageUtil;

/**
 * Uploads all recipes online, specifically to Dropbox.
 */
public class UploadCommand extends Command {

    public static final String COMMAND_WORD = "upload";
    public static final String MESSAGE_SUCCESS = "Upload Success!";
    public static final String MESSAGE_FAILURE = "Failed to upload!";
    public static final String MESSAGE_ACCESS_TOKEN = "Copy and paste the code given by dropbox.\n"
            + "Example: token VALID_ACCESS_TOKEN";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Uploads all recipes to your Dropbox with the "
            + "specified filename, with no spaces. It will only take in the first parameter. "
            + MESSAGE_FILENAME_CONSTRAINTS + "\n"
            + "Parameters: KEYWORD\n"
            + "Example: " + COMMAND_WORD + " RecipeBook";

    private final String xmlExtensionFilename;

    /**
     * Creates an UploadCommand to upload recipebook.xml to Dropbox with the
     * specified {@code String XmlExtensionFilename}
     */
    public UploadCommand(String xmlExtensionFilename) {
        this.xmlExtensionFilename = xmlExtensionFilename;
        CloudStorageUtil.setUploadFilename(xmlExtensionFilename);
    }

    @Override
    public CommandResult execute() throws DbxException {
        if (CloudStorageUtil.hasAccessToken()) {
            CloudStorageUtil.upload();
            return new CommandResult(MESSAGE_SUCCESS);
        }
        EventsCenter.getInstance().post(new UploadRecipesEvent());
        return new CommandResult(MESSAGE_ACCESS_TOKEN);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UploadCommand // instanceof handles nulls
                && xmlExtensionFilename.equals(((UploadCommand) other).xmlExtensionFilename));
    }
}
```
###### \java\seedu\recipe\logic\parser\IngredientCommandParser.java
``` java
package seedu.recipe.logic.parser;

import static seedu.recipe.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.recipe.logic.commands.IngredientCommand;
import seedu.recipe.logic.parser.exceptions.ParseException;
import seedu.recipe.model.recipe.IngredientContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new IngredientCommand object
 */
public class IngredientCommandParser implements Parser<IngredientCommand> {

    private static final String REGEX = "\\s+";

    /**
     * Parses the given {@code String} of arguments in the context of the IngredientCommand
     * and returns an IngredientCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public IngredientCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, IngredientCommand.MESSAGE_USAGE));
        }

        String[] ingredientKeywords = trimmedArgs.split(REGEX);

        return new IngredientCommand(new IngredientContainsKeywordsPredicate(Arrays.asList(ingredientKeywords)),
                    ingredientKeywords);
    }
}
```
###### \java\seedu\recipe\logic\parser\ParserUtil.java
``` java

    /**
     * Parses {@code String filename} into a {@code String XmlExtensionFilename}.
     * A .xml extension will be added to the original filename.
     *
     * @throws IllegalValueException if the give {@code filename} is invalid.
     */
    public static String parseFilename(String filename) throws IllegalValueException {
        requireNonNull(filename);
        if (!Filename.isValidFilename(filename)) {
            throw new IllegalValueException(Filename.MESSAGE_FILENAME_CONSTRAINTS);
        }
        return filename + ".xml";
    }
```
###### \java\seedu\recipe\logic\parser\TagCommandParser.java
``` java
package seedu.recipe.logic.parser;

import static seedu.recipe.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.recipe.logic.commands.TagCommand;
import seedu.recipe.logic.parser.exceptions.ParseException;
import seedu.recipe.model.tag.TagContainsKeywordsPredicate;


/**
 * Parses input arguments and creates a new TagCommand object
 */
public class TagCommandParser implements Parser<TagCommand> {

    private static final String REGEX = "\\s+";

    /**
     * Parses the given {@code String} of arguments in the context of the TagCommand
     * and returns an TagCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public TagCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
        }

        String[] tagKeywords = trimmedArgs.split(REGEX);

        return new TagCommand(new TagContainsKeywordsPredicate(Arrays.asList(tagKeywords)), tagKeywords);
    }
}
```
###### \java\seedu\recipe\logic\parser\UploadCommandParser.java
``` java
package seedu.recipe.logic.parser;

import static seedu.recipe.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.recipe.commons.exceptions.IllegalValueException;
import seedu.recipe.logic.commands.UploadCommand;
import seedu.recipe.logic.parser.exceptions.ParseException;
import seedu.recipe.model.file.Filename;

/**
 * Parses input arguments and creates a new UploadCommand object
 */
public class UploadCommandParser implements Parser<UploadCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the UploadCommand
     * and returns an UploadCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public UploadCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        String[] tagKeywords = trimmedArgs.split("\\s+");
        String filename = tagKeywords[0];
        if (filename.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, UploadCommand.MESSAGE_USAGE));
        }
        try {
            String xmlExtensionFilename = ParserUtil.parseFilename(filename);
            return new UploadCommand(xmlExtensionFilename);
        } catch (IllegalValueException ive) {
            throw new ParseException(Filename.MESSAGE_FILENAME_CONSTRAINTS);
        }
    }
}
//@author
```
###### \java\seedu\recipe\model\recipe\IngredientContainsKeywordsPredicate.java
``` java
package seedu.recipe.model.recipe;

import java.util.List;
import java.util.function.Predicate;

import seedu.recipe.commons.util.StringUtil;

/**
 * Tests that a {@code Recipe}'s {@code Ingredient} matches all of the keywords given.
 */
public class IngredientContainsKeywordsPredicate implements Predicate<Recipe> {
    private final List<String> keywords;

    public IngredientContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Recipe recipe) {
        int matches = 0;
        String ingredients = recipe.getIngredient().toString().replaceAll(",", " ");
        for (String keyword : keywords) {
            if (StringUtil.containsWordIgnoreCase(ingredients, keyword)) {
                matches++;
            }
        }
        return matches == keywords.size();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof IngredientContainsKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((IngredientContainsKeywordsPredicate) other).keywords)); // state check
    }
}
```
###### \java\seedu\recipe\model\tag\TagContainsKeywordsPredicate.java
``` java
package seedu.recipe.model.tag;

import java.util.List;
import java.util.function.Predicate;

import seedu.recipe.model.recipe.Recipe;

/**
 * Tests that a {@code Recipe}'s {@code Tags} matches any of the keywords given.
 */
public class TagContainsKeywordsPredicate implements Predicate<Recipe> {

    private final List<String> keywords;

    public TagContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Recipe recipe) {
        return keywords.stream()
                    .anyMatch(keyword -> recipe.getTags().stream()
                        .anyMatch(tag -> tag.tagName.equals(keyword)));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TagContainsKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((TagContainsKeywordsPredicate) other).keywords)); // state check
    }

}
```
###### \java\seedu\recipe\ui\BrowserPanel.java
``` java
    @Subscribe
    private void handleUploadRecipesEvent(UploadRecipesEvent event) {
        loadPageExternalBrowser(CloudStorageUtil.getAuthorizationUrl());
    }
```
###### \java\seedu\recipe\ui\util\CloudStorageUtil.java
``` java
package seedu.recipe.ui.util;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.v2.DbxClientV2;

import seedu.recipe.commons.util.FileUtil;
import seedu.recipe.logic.commands.UploadCommand;

/**
 * Contains data and methods needed for cloud storage
 * (uploading to dropbox) purposes.
 */
public class CloudStorageUtil {

    private static final String RECIPE_DATA_FOLDER = FileUtil.getPath("data/");
    private static final File RECIPE_BOOK_FILE = new File(RECIPE_DATA_FOLDER + "recipebook.xml");
    private static final String CLIENT_IDENTIFIER = "dropbox/recirecipe";

    private static final String APP_KEY = "0kj3cb9w27d66n8";
    private static final String APP_SECRET = "7stnncfsyvgim60";
    private static final String AUTHORIZATION_DOMAIN = "https://www.dropbox.com/1/oauth2/authorize?";
    private static final String AUTHORIZATION_URL = AUTHORIZATION_DOMAIN + "response_type=code&client_id="
                                                    + APP_KEY;

    private static final DbxRequestConfig config = DbxRequestConfig.newBuilder(CLIENT_IDENTIFIER).build();

    private static String accessToken = null;
    private static String uploadFilename = null;

    /**
     * Returns true if CloudStorageUtil already has an access token.
     */
    public static boolean hasAccessToken() {
        if (accessToken == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Creates a Dropbox client with the user's {@code getAccessToken()}
     * and uploads file specified by {@code RECIPE_BOOK_FILE} to their Dropbox account
     *
     * @throws DbxException
     */
    public static void upload() throws DbxException {
        // Ensures access token has been obtained
        requireNonNull(CloudStorageUtil.getAccessToken());

        // Create Dropbox client
        DbxClientV2 client = new DbxClientV2(config, CloudStorageUtil.getAccessToken());

        // Upload "recipebook.xml" to Dropbox
        try (InputStream in = new FileInputStream(RECIPE_BOOK_FILE)) {
            client.files().uploadBuilder("/" + uploadFilename)
                    .withAutorename(true)
                    .uploadAndFinish(in);
            System.out.println("File has been uploaded");
        } catch (IOException | DbxException e) {
            throw new DbxException(UploadCommand.MESSAGE_FAILURE + " Make sure you have an Internet connection.");
        }

    }

    /**
     * Takes in the authorization code
     * @param code given by Dropbox after user has allowed access
     * and converts it into the access token that can be used to upload files
     */
    public static void processAuthorizationCode(String code) throws DbxException {
        // Converts authorization code to access token
        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
        DbxWebAuth webAuth = new DbxWebAuth(config, appInfo);
        try {
            DbxAuthFinish authFinish = webAuth.finishFromCode(code);
            accessToken = authFinish.getAccessToken();
        } catch (DbxException e) {
            throw new DbxException(UploadCommand.MESSAGE_FAILURE + " Make sure you have an Internet connection"
                    + " and have entered a  valid authorization code");
        }
        upload();
    }

    public static void setAccessToken(String token) {
        accessToken = token;
    }

    private static String getAccessToken() {
        return accessToken;
    }

    public static void setUploadFilename(String uploadFilename) {
        CloudStorageUtil.uploadFilename = uploadFilename;
    }

    public static String getAuthorizationUrl() {
        return AUTHORIZATION_URL;
    }
}
```
