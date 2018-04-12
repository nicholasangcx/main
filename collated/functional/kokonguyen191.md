# kokonguyen191
###### \java\seedu\recipe\commons\events\ui\ChangeThemeRequestEvent.java
``` java
package seedu.recipe.commons.events.ui;

import seedu.recipe.commons.events.BaseEvent;

/**
 * An event requesting to change the theme.
 */
public class ChangeThemeRequestEvent extends BaseEvent {
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
```
###### \java\seedu\recipe\commons\events\ui\InternetSearchRequestEvent.java
``` java
package seedu.recipe.commons.events.ui;

import seedu.recipe.commons.events.BaseEvent;
import seedu.recipe.logic.commands.util.WikiaQueryHandler;

/**
 * Indicates a request to search for recipe on the internet
 */
public class InternetSearchRequestEvent extends BaseEvent {

    public final WikiaQueryHandler wikiaQueryHandler;

    public InternetSearchRequestEvent(WikiaQueryHandler wikiaQueryHandler) {
        this.wikiaQueryHandler = wikiaQueryHandler;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
```
###### \java\seedu\recipe\commons\events\ui\WebParseRequestEvent.java
``` java
package seedu.recipe.commons.events.ui;

import seedu.recipe.commons.events.BaseEvent;

/**
 * Indicates a request to parse the page loaded in BrowserPanel.
 */
public class WebParseRequestEvent extends BaseEvent {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
```
###### \java\seedu\recipe\logic\commands\ChangeThemeCommand.java
``` java
package seedu.recipe.logic.commands;

import seedu.recipe.commons.core.EventsCenter;
import seedu.recipe.commons.events.ui.ChangeThemeRequestEvent;

/**
 * Toggle between dark/light theme.
 */
public class ChangeThemeCommand extends Command {

    public static final String COMMAND_WORD = "theme";

    public static final String SHOWING_CHANGE_THEME_MESSAGE = "Theme changed.";

    @Override
    public CommandResult execute() {
        EventsCenter.getInstance().post(new ChangeThemeRequestEvent());
        return new CommandResult(SHOWING_CHANGE_THEME_MESSAGE);
    }
}
```
###### \java\seedu\recipe\logic\commands\ParseCommand.java
``` java
package seedu.recipe.logic.commands;

import seedu.recipe.commons.core.EventsCenter;
import seedu.recipe.commons.events.ui.WebParseRequestEvent;

/**
 * Parse the current page loaded in the BrowserPanel.
 */
public class ParseCommand extends Command {

    public static final String COMMAND_WORD = "parse";
    public static final String MESSAGE_SUCCESS = "ReciRecipe tried to parse the web page.";

    @Override
    public CommandResult execute() {
        EventsCenter.getInstance().post(new WebParseRequestEvent());
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ParseCommand); // instanceof handles nulls
    }
}
```
###### \java\seedu\recipe\logic\commands\SearchCommand.java
``` java
package seedu.recipe.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.recipe.commons.core.EventsCenter;
import seedu.recipe.commons.events.ui.InternetSearchRequestEvent;
import seedu.recipe.logic.commands.exceptions.CommandException;
import seedu.recipe.logic.commands.util.WikiaQueryHandler;

/**
 * Selects a recipe identified using it's last displayed index from the recipe book.
 */
public class SearchCommand extends Command {

    public static final String COMMAND_WORD = "search";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Search the recipe on recipes.wikia.com.\n"
            + "Parameters: NAME\n"
            + "Example: " + COMMAND_WORD + " chicken rice";
    public static final String MESSAGE_NO_RESULT = "No recipes found. Please try another query.";
    public static final String MESSAGE_FAILURE = "ReciRecipe couldn't search. Are you connected to the Internet?";
    public static final String MESSAGE_SUCCESS = "Found %1$s recipe(s). Please wait while the page is loading...";

    private final String recipeToSearch;
    private WikiaQueryHandler wikiaQueryHandler;

    public SearchCommand(String recipeToSearch) {
        requireNonNull(recipeToSearch);
        this.recipeToSearch = recipeToSearch;
    }

    @Override
    public CommandResult execute() throws CommandException {
        try {
            this.wikiaQueryHandler = new WikiaQueryHandler(recipeToSearch);
        } catch (AssertionError ae) {
            return new CommandResult(MESSAGE_FAILURE);
        }
        int noOfResult = wikiaQueryHandler.getQueryNumberOfResults();

        EventsCenter.getInstance().post(new InternetSearchRequestEvent(wikiaQueryHandler));

        if (noOfResult == 0) {
            return new CommandResult(MESSAGE_NO_RESULT);
        } else {
            return new CommandResult(String.format(MESSAGE_SUCCESS, noOfResult));
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SearchCommand // instanceof handles nulls
                && this.recipeToSearch.equals(((SearchCommand) other).recipeToSearch)); // state check
    }
}

```
###### \java\seedu\recipe\logic\commands\util\WikiaQuery.java
``` java
package seedu.recipe.logic.commands.util;

/**
 *  The API set of WikiaQueryHandler
 */
public interface WikiaQuery {

    /**
     * Returns the string value of the URL of the query.
     * This string can be used to get a displayable page for the {@code BrowserPanel}
     */
    String getRecipeQueryUrl();

    /**
     * Returns the number of results from the query.
     */
    int getQueryNumberOfResults();
}
```
###### \java\seedu\recipe\logic\commands\util\WikiaQueryHandler.java
``` java
package seedu.recipe.logic.commands.util;

import static java.util.Objects.requireNonNull;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.restfb.json.Json;
import com.restfb.json.JsonObject;

/**
 * Handle a query to recipes.wikia.com
 */
public class WikiaQueryHandler implements WikiaQuery {

    public static final String QUERY_URL = "http://recipes.wikia.com/search?query=";
    private static final String API_QUERY_URL = "http://recipes.wikia.com/api/v1/Search/List?query=";

    private String recipeToSearch;
    private URL queryUrl;
    private HttpURLConnection httpUrlConnection;
    private String rawDataString;
    private JsonObject rawDataJson;

    public WikiaQueryHandler(String recipeToSearch) throws AssertionError {
        requireNonNull(recipeToSearch);
        this.recipeToSearch = recipeToSearch;
        loadUrl();
        startHttpConnection();
        getRawData();
        parseData();
    }

    @Override
    public String getRecipeQueryUrl() {
        return QUERY_URL + this.recipeToSearch;
    }

    @Override
    public int getQueryNumberOfResults() {
        return rawDataString == null ? 0 : rawDataJson.get("total").asInt();
    }

    /**
     * Creates a {@code URL} object from given {@code recipeToSearch} string.
     */
    private void loadUrl() {
        requireNonNull(recipeToSearch);
        try {
            queryUrl = new URL(API_QUERY_URL + recipeToSearch);
        } catch (MalformedURLException mue) {
            throw new AssertionError("Invalid query URL. This should not happen.", mue);
        }
    }

    /**
     * Creates a HTTP connection from the {@code URL} object.
     */
    private void startHttpConnection() {
        requireNonNull(queryUrl);
        try {
            httpUrlConnection = (HttpURLConnection) queryUrl.openConnection();
            httpUrlConnection.setRequestMethod("GET");
        } catch (IOException ioe) {
            throw new AssertionError("Something went wrong while the app was "
                    + "trying to create a connection to " + queryUrl.toExternalForm(), ioe);
        }
    }

    /**
     * Reads the HTTP connection and print data to {@code rawDataString}.
     * Adapted from https://stackoverflow.com/questions/1485708/how-do-i-do-a-http-get-in-java
     */
    private void getRawData() throws AssertionError {
        requireNonNull(queryUrl);
        requireNonNull(httpUrlConnection);

        StringBuilder result = new StringBuilder();
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            br.close();
            rawDataString = result.toString();
        } catch (FileNotFoundException fnfe) {
            rawDataString = null;
        } catch (IOException ie) {
            throw new AssertionError("Something wrong happened while the app "
                    + "was trying to read data from the url " + queryUrl.toExternalForm(), ie);
        }
    }

    /**
     * Gets a {@code JSONObject} from {@code rawDataString}
     */
    private void parseData() {
        if (rawDataString != null) {
            rawDataJson = (JsonObject) Json.parse(rawDataString);
        }
    }
}
```
###### \java\seedu\recipe\logic\parser\ArgumentTokenizer.java
``` java
    private static int findPrefixPosition(String argsString, String prefix, int fromIndex) {
        Pattern pattern = Pattern.compile("[ \n]" + "\\Q" + prefix + "\\E");
        Matcher matcher = pattern.matcher(argsString);
        if (matcher.find(fromIndex)) {
            return matcher.start() + 1;
        } else {
            return -1;
        }
    }
```
###### \java\seedu\recipe\logic\parser\ParserUtil.java
``` java

    /**
     * Returns a null {@code Ingredient} object to use as the default value if no value is given.
     */
    public static Ingredient getNullReferenceIngredient() throws IllegalValueException {
        return new Ingredient(Ingredient.NULL_INGREDIENT_REFERENCE);
    }

    /**
     * Parses a {@code String ingredient} into an {@code Ingredient}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code ingredient} is invalid.
     */
    public static Ingredient parseIngredient(String ingredient) throws IllegalValueException {
        requireNonNull(ingredient);
        String trimmedIngredient = ingredient.trim();
        if (!Ingredient.isValidIngredient(trimmedIngredient)) {
            throw new IllegalValueException(Ingredient.MESSAGE_INGREDIENT_CONSTRAINTS);
        }
        return new Ingredient(trimmedIngredient);
    }

    /**
     * Parses a {@code Optional<String> ingredient} into an {@code Optional<Ingredient>}
     * if {@code ingredient} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Ingredient> parseIngredient(Optional<String> ingredient) throws IllegalValueException {
        requireNonNull(ingredient);
        return ingredient.isPresent() ? Optional.of(parseIngredient(ingredient.get())) : Optional.empty();
    }

    /**
     * Parses a {@code Optional<String> ingredient} into an {@code Optional<Ingredient>}
     * if {@code ingredient} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Ingredient> parseIngredientOnInitialAdd(Optional<String> ingredient)
            throws IllegalValueException {
        requireNonNull(ingredient);
        return ingredient.isPresent()
                ? Optional.of(parseIngredient(ingredient.get())) : Optional.of(getNullReferenceIngredient());
    }

    /**
     * Returns a null {@code Instruction} object to use as the default value if no value is given.
     */
    public static Instruction getNullReferenceInstruction() throws IllegalValueException {
        return new Instruction(Instruction.NULL_INSTRUCTION_REFERENCE);
    }

    /**
     * Parses a {@code String recipe} into an {@code Instruction}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code recipe} is invalid.
     */
    public static Instruction parseInstruction(String instruction) throws IllegalValueException {
        requireNonNull(instruction);
        String trimmedInstruction = instruction.trim();
        if (!Instruction.isValidInstuction(trimmedInstruction)) {
            throw new IllegalValueException(Instruction.MESSAGE_INSTRUCTION_CONSTRAINTS);
        }
        return new Instruction(trimmedInstruction);
    }

    /**
     * Parses a {@code Optional<String> recipe} into an {@code Optional<Instruction>} if {@code recipe} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Instruction> parseInstruction(Optional<String> instruction) throws IllegalValueException {
        requireNonNull(instruction);
        return instruction.isPresent() ? Optional.of(parseInstruction(instruction.get())) : Optional.empty();
    }

    /**
     * Parses a {@code Optional<String> instruction} into an {@code Optional<Instruction>}
     * if {@code instruction} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Instruction> parseInstructionOnInitialAdd(Optional<String> instruction)
            throws IllegalValueException {
        requireNonNull(instruction);
        return instruction.isPresent()
                ? Optional.of(parseInstruction(instruction.get())) : Optional.of(getNullReferenceInstruction());
    }

    /**
     * Returns a null {@code CookingTime} object to use as the default value if no value is given.
     */
    public static CookingTime getNullReferenceCookingTime() throws IllegalValueException {
        return new CookingTime(CookingTime.NULL_COOKING_TIME_REFERENCE);
    }

    /**
     * Parses a {@code String cookingTime} into a {@code CookingTime}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code cookingTime} is invalid.
     */
    public static CookingTime parseCookingTime(String cookingTime) throws IllegalValueException {
        requireNonNull(cookingTime);
        String trimmedCookingTime = cookingTime.trim();
        if (!CookingTime.isValidCookingTime(trimmedCookingTime)) {
            throw new IllegalValueException(CookingTime.MESSAGE_COOKING_TIME_CONSTRAINTS);
        }
        return new CookingTime(trimmedCookingTime);
    }

    /**
     * Parses a {@code Optional<String> cookingTime} into an {@code Optional<CookingTime>}
     * if {@code cookingTime} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<CookingTime> parseCookingTime(Optional<String> cookingTime) throws IllegalValueException {
        requireNonNull(cookingTime);
        return cookingTime.isPresent()
                ? Optional.of(parseCookingTime(cookingTime.get())) : Optional.empty();
    }

    /**
     * Parses a {@code Optional<String> cookingTime} into an {@code Optional<CookingTime>}
     * if {@code cookingTime} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<CookingTime> parseCookingTimeOnInitialAdd(Optional<String> cookingTime)
            throws IllegalValueException {
        requireNonNull(cookingTime);
        return cookingTime.isPresent()
                ? Optional.of(parseCookingTime(cookingTime.get())) : Optional.of(getNullReferenceCookingTime());
    }

    /**
     * Returns a null {@code PreparationTime} object to use as the default value if no value is given.
     */
    public static PreparationTime getNullReferencePreparationTime() throws IllegalValueException {
        return new PreparationTime(PreparationTime.NULL_PREPARATION_TIME_REFERENCE);
    }

    /**
     * Parses a {@code String preparationTime} into a {@code PreparationTime}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code preparationTime} is invalid.
     */
    public static PreparationTime parsePreparationTime(String preparationTime) throws IllegalValueException {
        requireNonNull(preparationTime);
        String trimmedPreparationTime = preparationTime.trim();
        if (!PreparationTime.isValidPreparationTime(trimmedPreparationTime)) {
            throw new IllegalValueException(PreparationTime.MESSAGE_PREPARATION_TIME_CONSTRAINTS);
        }
        return new PreparationTime(trimmedPreparationTime);
    }

    /**
     * Parses a {@code Optional<String> preparationTime} into an {@code Optional<PreparationTime>}
     * if {@code preparationTime} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<PreparationTime> parsePreparationTime(Optional<String> preparationTime)
            throws IllegalValueException {
        requireNonNull(preparationTime);
        return preparationTime.isPresent()
                ? Optional.of(parsePreparationTime(preparationTime.get())) : Optional.empty();
    }

    /**
     * Parses a {@code Optional<String> preparationTime} into an {@code Optional<PreparationTime>}
     * if {@code preparationTime} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<PreparationTime> parsePreparationTimeOnInitialAdd(Optional<String> preparationTime)
            throws IllegalValueException {
        requireNonNull(preparationTime);
        return preparationTime.isPresent() ? Optional.of(parsePreparationTime(preparationTime.get()))
                : Optional.of(getNullReferencePreparationTime());
    }

    /**
     * Returns a null {@code Calories} object to use as the default value if no value is given.
     */
    public static Calories getNullReferenceCalories() throws IllegalValueException {
        return new Calories(Calories.NULL_CALORIES_REFERENCE);
    }

    /**
     * Parses a {@code String calories} into a {@code Calories}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code calories} is invalid.
     */
    public static Calories parseCalories(String calories) throws IllegalValueException {
        requireNonNull(calories);
        String trimmedCalories = calories.trim();
        if (!Calories.isValidCalories(trimmedCalories)) {
            throw new IllegalValueException(Calories.MESSAGE_CALORIES_CONSTRAINTS);
        }
        return new Calories(trimmedCalories);
    }

    /**
     * Parses a {@code Optional<String> calories} into an {@code Optional<Calories>}
     * if {@code calories} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Calories> parseCalories(Optional<String> calories) throws IllegalValueException {
        requireNonNull(calories);
        return calories.isPresent()
                ? Optional.of(parseCalories(calories.get())) : Optional.empty();
    }

    /**
     * Parses a {@code Optional<String> calories} into an {@code Optional<Calories>}
     * if {@code calories} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Calories> parseCaloriesOnInitialAdd(Optional<String> calories) throws IllegalValueException {
        requireNonNull(calories);
        return calories.isPresent()
                ? Optional.of(parseCalories(calories.get())) : Optional.of(getNullReferenceCalories());
    }

    /**
     * Returns a null {@code Servings} object to use as the default value if no value is given.
     */
    public static Servings getNullReferenceServings() throws IllegalValueException {
        return new Servings(Servings.NULL_SERVINGS_REFERENCE);
    }

    /**
     * Parses a {@code String servings} into a {@code Servings}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code servings} is invalid.
     */
    public static Servings parseServings(String servings) throws IllegalValueException {
        requireNonNull(servings);
        String trimmedServings = servings.trim();
        if (!Servings.isValidServings(trimmedServings)) {
            throw new IllegalValueException(Servings.MESSAGE_SERVINGS_CONSTRAINTS);
        }
        return new Servings(trimmedServings);
    }

    /**
     * Parses a {@code Optional<String> servings} into an {@code Optional<Servings>}
     * if {@code servings} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Servings> parseServings(Optional<String> servings) throws IllegalValueException {
        requireNonNull(servings);
        return servings.isPresent()
                ? Optional.of(parseServings(servings.get())) : Optional.empty();
    }

    /**
     * Parses a {@code Optional<String> servings} into an {@code Optional<Servings>}
     * if {@code servings} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Servings> parseServingsOnInitialAdd(Optional<String> servings) throws IllegalValueException {
        requireNonNull(servings);
        return servings.isPresent()
                ? Optional.of(parseServings(servings.get())) : Optional.of(getNullReferenceServings());
    }

```
###### \java\seedu\recipe\logic\parser\SearchCommandParser.java
``` java
package seedu.recipe.logic.parser;

import static seedu.recipe.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.recipe.logic.commands.SearchCommand;
import seedu.recipe.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code SearchCommand} object
 */
public class SearchCommandParser implements Parser<SearchCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SearchCOmmand
     * and returns an {@code SearchCommand} object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public SearchCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchCommand.MESSAGE_USAGE));
        }

        String[] keywords = trimmedArgs.split("\\s+");

        return new SearchCommand(String.join("+", keywords));
    }

}
```
###### \java\seedu\recipe\model\recipe\Calories.java
``` java
package seedu.recipe.model.recipe;

import static java.util.Objects.requireNonNull;
import static seedu.recipe.commons.util.AppUtil.checkArgument;

/**
 * Represents a Recipe's number of calories in the Recipe Book.
 * Guarantees: immutable; is valid as declared in {@link #isValidCalories(String)}
 */
public class Calories {

    public static final String NULL_CALORIES_REFERENCE = "-";
    public static final String MESSAGE_CALORIES_CONSTRAINTS = "The number of calories must a positive integer.";
    public static final String CALORIES_VALIDATION_REGEX = "[\\d]+";
    public final String value;

    /**
     * Constructs a {@code Calories} object.
     *
     * @param calories A valid number of calories.
     */
    public Calories(String calories) {
        requireNonNull(calories);
        checkArgument(isValidCalories(calories), MESSAGE_CALORIES_CONSTRAINTS);
        this.value = calories;
    }

    /**
     * Returns true if a given string is a valid number of calories.
     */
    public static boolean isValidCalories(String test) {
        return test.equals(NULL_CALORIES_REFERENCE) || test.matches(CALORIES_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Calories // instanceof handles nulls
                && this.value.equals(((Calories) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
```
###### \java\seedu\recipe\model\recipe\CookingTime.java
``` java
package seedu.recipe.model.recipe;

import static java.util.Objects.requireNonNull;
import static seedu.recipe.commons.util.AppUtil.checkArgument;

/**
 * Represents a Recipe's cooking time in the Recipe Book.
 * Guarantees: immutable; is valid as declared in {@link #isValidCookingTime(String)}
 */
public class CookingTime {

    public static final String NULL_COOKING_TIME_REFERENCE = "-";
    public static final String MESSAGE_COOKING_TIME_CONSTRAINTS =
            "Cooking time can be in any of these format: 1h10m, 70m, 1 hour 10 minutes, 1 h 10 min, 70.";
    public static final String COOKING_TIME_VALIDATION_REGEX =
            "([\\d]+[ ]{0,1}(minute[s]{0,1}|min[s]{0,1}|m|hour[s]{0,1}|h){0,1}[ ]{0,1}){1,2}";
    public final String value;

    /**
     * Constructs a {@code CookingTime}.
     *
     * @param cookingTime A valid cooking time.
     */
    public CookingTime(String cookingTime) {
        requireNonNull(cookingTime);
        checkArgument(isValidCookingTime(cookingTime), MESSAGE_COOKING_TIME_CONSTRAINTS);
        this.value = cookingTime;
    }

    /**
     * Returns true if a given string is a valid recipe cooking time.
     */
    public static boolean isValidCookingTime(String test) {
        return test.equals(NULL_COOKING_TIME_REFERENCE) || test.matches(COOKING_TIME_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof CookingTime // instanceof handles nulls
                && this.value.equals(((CookingTime) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
```
###### \java\seedu\recipe\model\recipe\PreparationTime.java
``` java
package seedu.recipe.model.recipe;

import static java.util.Objects.requireNonNull;
import static seedu.recipe.commons.util.AppUtil.checkArgument;

/**
 * Represents a Recipe's preparation time in the Recipe Book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPreparationTime(String)}
 */
public class PreparationTime {

    public static final String NULL_PREPARATION_TIME_REFERENCE = "-";
    public static final String MESSAGE_PREPARATION_TIME_CONSTRAINTS =
            "Preparation time can be in any of these format: 1h20m, 80m, 1 hour 20 minutes, 1 h 20 min, 80.";
    public static final String PREPARATION_TIME_VALIDATION_REGEX =
            "([\\d]+[ ]{0,1}(minute[s]{0,1}|min[s]{0,1}|m|hour[s]{0,1}|h){0,1}[ ]{0,1}){1,2}";
    public final String value;

    /**
     * Constructs a {@code PreparationTime}.
     *
     * @param preparationTime A valid preparation time.
     */
    public PreparationTime(String preparationTime) {
        requireNonNull(preparationTime);
        checkArgument(isValidPreparationTime(preparationTime), MESSAGE_PREPARATION_TIME_CONSTRAINTS);
        this.value = preparationTime;
    }

    /**
     * Returns true if a given string is a valid recipe preparation time.
     */
    public static boolean isValidPreparationTime(String test) {
        return test.equals(NULL_PREPARATION_TIME_REFERENCE) || test.matches(PREPARATION_TIME_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PreparationTime // instanceof handles nulls
                && this.value.equals(((PreparationTime) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
```
###### \java\seedu\recipe\model\recipe\Servings.java
``` java
package seedu.recipe.model.recipe;

import static java.util.Objects.requireNonNull;
import static seedu.recipe.commons.util.AppUtil.checkArgument;

/**
 * Represents a Recipe's number of servings in the Recipe Book.
 * Guarantees: immutable; is valid as declared in {@link #isValidServings(String)}
 */
public class Servings {

    public static final String NULL_SERVINGS_REFERENCE = "-";
    public static final String MESSAGE_SERVINGS_CONSTRAINTS = "The number of servings must a positive integer.";
    public static final String SERVINGS_VALIDATION_REGEX = "[\\d]+";
    public final String value;

    /**
     * Constructs a {@code Servings} object.
     *
     * @param servings A valid number of servings.
     */
    public Servings(String servings) {
        requireNonNull(servings);
        checkArgument(isValidServings(servings), MESSAGE_SERVINGS_CONSTRAINTS);
        this.value = servings;
    }

    /**
     * Returns true if a given string is a valid number of servings.
     */
    public static boolean isValidServings(String test) {
        return test.equals(NULL_SERVINGS_REFERENCE) || test.matches(SERVINGS_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Servings // instanceof handles nulls
                && this.value.equals(((Servings) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
```
###### \java\seedu\recipe\storage\XmlAdaptedRecipe.java
``` java
        if (this.ingredient == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Ingredient.class.getSimpleName()));
        }
        if (!Ingredient.isValidIngredient(this.ingredient)) {
            throw new IllegalValueException(Ingredient.MESSAGE_INGREDIENT_CONSTRAINTS);
        }
        final Ingredient ingredient = new Ingredient(this.ingredient);

        if (this.instruction == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Instruction.class.getSimpleName()));
        }
        if (!Instruction.isValidInstuction(this.instruction)) {
            throw new IllegalValueException(Instruction.MESSAGE_INSTRUCTION_CONSTRAINTS);
        }
        final Instruction instruction = new Instruction(this.instruction);

        if (this.preparationTime == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    PreparationTime.class.getSimpleName()));
        }
        if (!PreparationTime.isValidPreparationTime(this.preparationTime)) {
            throw new IllegalValueException(PreparationTime.MESSAGE_PREPARATION_TIME_CONSTRAINTS);
        }
        final PreparationTime preparationTime = new PreparationTime(this.preparationTime);

        if (this.cookingTime == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    CookingTime.class.getSimpleName()));
        }
        if (!CookingTime.isValidCookingTime(this.cookingTime)) {
            throw new IllegalValueException(CookingTime.MESSAGE_COOKING_TIME_CONSTRAINTS);
        }
        final CookingTime cookingTime = new CookingTime(this.cookingTime);

        if (this.calories == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Calories.class.getSimpleName()));
        }
        if (!Calories.isValidCalories(this.calories)) {
            throw new IllegalValueException(Calories.MESSAGE_CALORIES_CONSTRAINTS);
        }
        final Calories calories = new Calories(this.calories);

        if (this.servings == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Servings.class.getSimpleName()));
        }
        if (!Servings.isValidServings(this.servings)) {
            throw new IllegalValueException(Servings.MESSAGE_SERVINGS_CONSTRAINTS);
        }
        final Servings servings = new Servings(this.servings);

```
###### \java\seedu\recipe\ui\BrowserPanel.java
``` java
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

```
###### \java\seedu\recipe\ui\MainWindow.java
``` java
    @Subscribe
    private void handleChangeThemeRequestEvent(ChangeThemeRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        handleChangeTheme();
    }

    /**
     * Toggle between dark and light theme
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

```
###### \java\seedu\recipe\ui\parser\MobileWikiaParser.java
``` java
package seedu.recipe.ui.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * A WebParser for recipes.wikia.com mobile version
 */
public class MobileWikiaParser extends WikiaParser {

    public static final String DOMAIN = "recipes.wikia.com";

    /**
     * Constructs from a Jsoup Document.
     */
    public MobileWikiaParser(Document document) {
        super(document);
    }

    /**
     * Constructs from a HTML string and a URL.
     */
    public MobileWikiaParser(String html, String url) {
        super(html, url);
    }

    @Override
    protected void getMainBody() {
        contentText = this.document.selectFirst(".article-content.mw-content");
    }

    @Override
    public String getName() {
        return document.selectFirst(".wiki-page-header__title").text();
    }

    @Override
    public String getImageUrl() {
        Element image = contentText.selectFirst(".article-media-placeholder");
        if (image == null) {
            return "";
        } else {
            return image.attr("data-src");
        }
    }

    @Override
    public String getUrl() {
        return document.selectFirst("[rel=\"canonical\"]").attr("href") + "?useskin=wikiamobile";
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof MobileWikiaParser // instanceof handles nulls
                && document.html().equals(((MobileWikiaParser) other).document.html()));
    }
}
```
###### \java\seedu\recipe\ui\parser\WebParser.java
``` java
package seedu.recipe.ui.parser;

import static seedu.recipe.logic.commands.AddCommand.COMMAND_WORD;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_IMG;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_INGREDIENT;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_INSTRUCTION;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_URL;

import org.jsoup.nodes.Document;

/**
 * The API set of a web parser
 */
public abstract class WebParser {

    private static final String LF = "\n";

    protected Document document;

    /**
     * Returns the parsed Recipe object from the page.
     */
    public String parseRecipe() {
        String name = getName();
        String ingredient = getIngredient();
        String instruction = getInstruction();
        String imageUrl = getImageUrl();
        String url = getUrl();

        if (!name.equals("")) {
            StringBuilder commandBuilder = new StringBuilder();
            commandBuilder.append(COMMAND_WORD);
            commandBuilder.append(LF);
            commandBuilder.append(PREFIX_NAME);
            commandBuilder.append(name);
            if (!ingredient.equals("")) {
                commandBuilder.append(LF);
                commandBuilder.append(PREFIX_INGREDIENT);
                commandBuilder.append(ingredient);
            }
            if (!instruction.equals("")) {
                commandBuilder.append(LF);
                commandBuilder.append(PREFIX_INSTRUCTION);
                commandBuilder.append(instruction);
            }
            if (!imageUrl.equals("")) {
                commandBuilder.append(LF);
                commandBuilder.append(PREFIX_IMG);
                commandBuilder.append(imageUrl);
            }
            commandBuilder.append(LF);
            commandBuilder.append(PREFIX_URL);
            commandBuilder.append(url);
            return commandBuilder.toString();
        }
        return null;
    }

    public abstract String getName();

    public abstract String getIngredient();

    public abstract String getInstruction();

    public abstract String getImageUrl();

    public abstract String getUrl();
}
```
###### \java\seedu\recipe\ui\parser\WebParserHandler.java
``` java
package seedu.recipe.ui.parser;

import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.net.URISyntaxException;

import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.w3c.dom.Document;

import javafx.scene.web.WebView;

/**
 * Handles a WebParseRequestEvent
 */
public class WebParserHandler {

    private WebView browser;

    public WebParserHandler(WebView browser) {
        requireNonNull(browser);
        this.browser = browser;
    }

    /**
     * Returns the according WebParser to the currently loaded page in the BrowserPanel
     */
    public WebParser getWebParser() {
        String url = browser.getEngine().getLocation();
        Document document = browser.getEngine().getDocument();
        W3CDom w3CDom = new W3CDom();
        String documentString = w3CDom.asString(document);
        if (browser.getEngine().getTitle() != null) {
            return WebParserHandler.getWebParser(url, documentString);
        } else {
            return null;
        }
    }

    /**
     * Reads the {@code url}, returns the according WebParser loaded with {@code documentString}
     */
    public static WebParser getWebParser(String url, String documentString) {
        try {
            URI uri = new URI(url);
            String domain = uri.getHost();
            switch (domain) {
            case WikiaParser.DOMAIN:
                // Try to pre-parse first
                org.jsoup.nodes.Document jsoupDocument = Jsoup.parse(documentString);
                if (jsoupDocument.getElementById("mw-content-text") == null) {
                    return new MobileWikiaParser(jsoupDocument);
                } else {
                    return new WikiaParser(jsoupDocument);
                }

            default:
                return null;

            }
        } catch (URISyntaxException e) {
            return null;
        }
    }
}
```
###### \java\seedu\recipe\ui\parser\WikiaParser.java
``` java
package seedu.recipe.ui.parser;

import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * A WebParser for recipes.wikia.com
 */
public class WikiaParser extends WebParser {

    public static final String DOMAIN = "recipes.wikia.com";
    private static final String LF = "\n";

    protected Document document;
    protected Element contentText;

    /**
     * Constructs from a Jsoup Document.
     */
    public WikiaParser(Document document) {
        requireNonNull(document);
        this.document = document;
        getMainBody();
    }

    /**
     * Constructs from a HTML string and a URL.
     */
    public WikiaParser(String html, String url) {
        requireNonNull(html);
        this.document = Jsoup.parse(html, url);
        getMainBody();
    }

    /**
     * Assigns {@code contentText} to the Element that contains the article body.
     */
    protected void getMainBody() {
        contentText = this.document.getElementById("mw-content-text");
    }

    @Override
    public String getName() {
        return document.selectFirst(".page-header__title").text();
    }

    @Override
    public String getIngredient() {
        Elements elements = contentText.select("h2,ul");
        Iterator<Element> eleIte = elements.iterator();
        while (eleIte.hasNext() && !eleIte.next().text().startsWith("Ingredient")) {
            // Do nothing and just go to the line that starts with "Ingredient"
        }

        Elements elementsWithIngredientWithLink = new Elements();
        Elements elementsWithIngredient = new Elements();
        while (eleIte.hasNext()) {
            Element nextElement = eleIte.next();
            if (nextElement.tagName().contains("h2")) {
                break;
            }
            elementsWithIngredient.addAll(nextElement.select("li"));
            elementsWithIngredientWithLink.addAll(nextElement.select("a"));
        }

        List<String> ingredientList;
        if (elementsWithIngredientWithLink.isEmpty()) {
            ingredientList = elementsWithIngredient.eachText();
        } else {
            ingredientList = elementsWithIngredientWithLink.eachText();
        }
        return String.join(", ", ingredientList);
    }

    @Override
    public String getInstruction() {
        Elements elementsWithInstruction = contentText.select("ol li");
        List<String> instructionList = elementsWithInstruction.eachText();
        return String.join("\n", instructionList);
    }

    @Override
    public String getImageUrl() {
        Element image = contentText.selectFirst(".image img");
        if (image == null) {
            return "";
        } else {
            return image.attr("src");
        }
    }

    @Override
    public String getUrl() {
        return document.selectFirst("[rel=\"canonical\"]").attr("href");
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof WikiaParser // instanceof handles nulls
                && document.html().equals(((WikiaParser) other).document.html()));
    }
}
```
