# kokonguyen191
###### \java\guitests\guihandles\CommandBoxHandle.java
``` java
    /**
     * Appends the given string to text already existing in the Command box
     */
    public void appendText(String text) {
        guiRobot.interact(() -> getRootNode().appendText(text));
        guiRobot.pauseForHuman();
    }

```
###### \java\guitests\guihandles\MainMenuHandle.java
``` java
    /**
     * Change theme using the menu bar in {@code MainWindow}.
     */
    public void changeThemeUsingMenu() {
        clickOnMenuItemsSequentially("Change Theme", "F2");
    }

    /**
     * Change theme by pressing the shortcut key associated with the menu bar in {@code MainWindow}.
     */
    public void changeThemeUsingAccelerator() {
        guiRobot.push(KeyCode.F2);
    }

```
###### \java\seedu\recipe\logic\commands\ChangeThemeCommandTest.java
``` java
package seedu.recipe.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.recipe.logic.commands.ChangeThemeCommand.SHOWING_CHANGE_THEME_MESSAGE;

import org.junit.Rule;
import org.junit.Test;

import seedu.recipe.commons.events.ui.ChangeThemeRequestEvent;
import seedu.recipe.ui.testutil.EventsCollectorRule;

public class ChangeThemeCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    @Test
    public void execute_changeTheme_success() {
        CommandResult result = new ChangeThemeCommand().execute();
        assertEquals(SHOWING_CHANGE_THEME_MESSAGE, result.feedbackToUser);
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof ChangeThemeRequestEvent);
        assertTrue(eventsCollectorRule.eventsCollector.getSize() == 1);
    }
}
```
###### \java\seedu\recipe\logic\commands\ParseCommandTest.java
``` java
package seedu.recipe.logic.commands;

import static org.junit.Assert.assertEquals;
import static seedu.recipe.logic.commands.ParseCommand.MESSAGE_SUCCESS;

import org.junit.Test;

public class ParseCommandTest {
    @Test
    public void execute_nothing_throwsAssertionError() {
        CommandResult result = new ParseCommand().execute();
        assertEquals(MESSAGE_SUCCESS, result.feedbackToUser);
    }
}
```
###### \java\seedu\recipe\logic\commands\SearchCommandTest.java
``` java
package seedu.recipe.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.recipe.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.recipe.logic.commands.SearchCommand.MESSAGE_NO_RESULT;
import static seedu.recipe.logic.commands.SearchCommand.MESSAGE_SUCCESS;
import static seedu.recipe.testutil.TypicalRecipes.getTypicalRecipeBook;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.recipe.model.Model;
import seedu.recipe.model.ModelManager;
import seedu.recipe.model.UserPrefs;

public class SearchCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(getTypicalRecipeBook(), new UserPrefs());

    @Test
    public void equals() {
        String firstTest = "chicken rice";
        String secondTest = "boneless pizza";

        SearchCommand searchFirstCommand = new SearchCommand(firstTest);
        SearchCommand searchSecondCommand = new SearchCommand(secondTest);

        // same object -> returns true
        assertTrue(searchFirstCommand.equals(searchFirstCommand));

        // same values -> returns true
        SearchCommand searchFirstCommandCopy = new SearchCommand(firstTest);
        assertTrue(searchFirstCommand.equals(searchFirstCommandCopy));

        // different types -> returns false
        assertFalse(searchFirstCommand.equals(1));
        assertFalse(searchFirstCommand.equals(new HelpCommand()));
        assertFalse(searchFirstCommand.equals("DOGGO"));

        // null -> returns false
        assertFalse(searchFirstCommand.equals(null));

        // different recipe -> returns false
        assertFalse(searchFirstCommand.equals(searchSecondCommand));
    }

    @Test
    public void constructor_nullInput_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        SearchCommand searchCommand = new SearchCommand(null);
    }

    @Test
    public void execute_inputWithNoResults_noRecipesFound() {
        SearchCommand searchCommandWithNoResult = new SearchCommand("blah");
        assertCommandSuccess(searchCommandWithNoResult, model, MESSAGE_NO_RESULT, model);
    }

    // THIS TEST MIGHT FAIL IN THE FUTURE! PLEASE UPDATE IF IT FAILS!
    @Test
    public void execute_inputWithFourResults_fourRecipesFound() {
        SearchCommand searchCommandWithFourResult = new SearchCommand("bot");
        assertCommandSuccess(searchCommandWithFourResult, model, String.format(MESSAGE_SUCCESS, 4), model);
    }
}
```
###### \java\seedu\recipe\logic\commands\util\WikiaQueryHandlerTest.java
``` java
package seedu.recipe.logic.commands.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.recipe.logic.commands.util.WikiaQueryHandler.QUERY_URL;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class WikiaQueryHandlerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullQuery_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new WikiaQueryHandler(null);
    }

    @Test
    public void constructor_invalidQuery_throwsAssertionError() {
        thrown.expect(AssertionError.class);
        new WikiaQueryHandler("");
    }

    @Test
    public void getRecipeQueryUrl_normalString_success() throws Exception {
        WikiaQueryHandler testWikiaQueryHandler = new WikiaQueryHandler("chickens soup");
        assertEquals(testWikiaQueryHandler.getRecipeQueryUrl(), QUERY_URL + "chickens soup");
    }

    @Test
    public void getQueryNumberOfResults_zeroResults_success() throws Exception {
        WikiaQueryHandler wikiaQueryHandlerWithZeroResults = new WikiaQueryHandler("blah");
        assertEquals(wikiaQueryHandlerWithZeroResults.getQueryNumberOfResults(), 0);
    }

    // It is very hard to give a concrete number for this test as recipes are added everyday
    @Test
    public void getQueryNumberOfResults_bigResults_success() throws Exception {
        WikiaQueryHandler wikiaQueryHandlerWithZeroResults = new WikiaQueryHandler("chicken");
        assertTrue(wikiaQueryHandlerWithZeroResults.getQueryNumberOfResults() > 5000);
    }

    // THIS TEST MIGHT FAIL IN THE FUTURE! PLEASE UPDATE IF IT FAILS
    @Test
    public void getQueryNumberOfResults_fourResults_success() throws Exception {
        WikiaQueryHandler wikiaQueryHandlerWithFourResults = new WikiaQueryHandler("bot");
        assertEquals(wikiaQueryHandlerWithFourResults.getQueryNumberOfResults(), 4);
    }
}
```
###### \java\seedu\recipe\logic\parser\AddCommandParserTest.java
``` java
    @Test
    public void parse_allFieldsPresentWithNewLineDelimiter_success() {
        Recipe expectedRecipe = new RecipeBuilder().withName(VALID_NAME_AMY).withServings(VALID_SERVINGS_AMY)
                .withPreparationTime(VALID_PREPARATION_TIME_AMY).withIngredient(VALID_INGREDIENT_AMY)
                .withCookingTime(VALID_COOKING_TIME_AMY).withCalories(VALID_CALORIES_AMY)
                .withInstruction(VALID_INSTRUCTION_AMY).withUrl(VALID_URL_AMY)
                .withImage(VALID_IMG_AMY).withTags(VALID_TAG_FRIEND).build();

        // Multiple names - last name accepted
        assertParseSuccess(parser, NAME_DESC_BOB + LF
                        + NAME_DESC_AMY + LF + PREPARATION_TIME_DESC_AMY
                        + LF + INGREDIENT_DESC_AMY + LF + INSTRUCTION_DESC_AMY
                        + LF + URL_DESC_AMY + LF + IMG_DESC_AMY + LF + COOKING_TIME_DESC_AMY
                        + LF + CALORIES_DESC_AMY + LF + SERVINGS_DESC_AMY,
                new AddCommand(expectedRecipe));


        // multiple ingredients - last ingredient accepted
        assertParseSuccess(parser, INGREDIENT_DESC_BOB + LF
                        + NAME_DESC_AMY + LF + PREPARATION_TIME_DESC_AMY
                        + LF + INGREDIENT_DESC_AMY + LF + INSTRUCTION_DESC_AMY
                        + LF + URL_DESC_AMY + LF + IMG_DESC_AMY + LF + COOKING_TIME_DESC_AMY
                        + LF + CALORIES_DESC_AMY + LF + SERVINGS_DESC_AMY,
                new AddCommand(expectedRecipe));

        // multiple preparationTimes - last instruction accepted
        assertParseSuccess(parser, INSTRUCTION_DESC_BOB + LF
                        + NAME_DESC_AMY + LF + PREPARATION_TIME_DESC_AMY
                        + LF + INGREDIENT_DESC_AMY + LF + INSTRUCTION_DESC_AMY
                        + LF + URL_DESC_AMY + LF + IMG_DESC_AMY + LF + COOKING_TIME_DESC_AMY
                        + LF + CALORIES_DESC_AMY + LF + SERVINGS_DESC_AMY,
                new AddCommand(expectedRecipe));

        // multiple instructions - last cooking time
        assertParseSuccess(parser, COOKING_TIME_DESC_BOB + LF
                        + NAME_DESC_AMY + LF + PREPARATION_TIME_DESC_AMY
                        + LF + INGREDIENT_DESC_AMY + LF + INSTRUCTION_DESC_AMY
                        + LF + URL_DESC_AMY + LF + IMG_DESC_AMY + LF + COOKING_TIME_DESC_AMY
                        + LF + CALORIES_DESC_AMY + LF + SERVINGS_DESC_AMY,
                new AddCommand(expectedRecipe));

        // multiple instructions - last preparation time
        assertParseSuccess(parser, PREPARATION_TIME_DESC_BOB + LF
                        + NAME_DESC_AMY + LF + PREPARATION_TIME_DESC_AMY
                        + LF + INGREDIENT_DESC_AMY + LF + INSTRUCTION_DESC_AMY
                        + LF + URL_DESC_AMY + LF + IMG_DESC_AMY + LF + COOKING_TIME_DESC_AMY
                        + LF + CALORIES_DESC_AMY + LF + SERVINGS_DESC_AMY,
                new AddCommand(expectedRecipe));

        // multiple instructions - last calories
        assertParseSuccess(parser, CALORIES_DESC_BOB + LF
                        + NAME_DESC_AMY + LF + PREPARATION_TIME_DESC_AMY
                        + LF + INGREDIENT_DESC_AMY + LF + INSTRUCTION_DESC_AMY
                        + LF + URL_DESC_AMY + LF + IMG_DESC_AMY + LF + COOKING_TIME_DESC_AMY
                        + LF + CALORIES_DESC_AMY + LF + SERVINGS_DESC_AMY,
                new AddCommand(expectedRecipe));

        // multiple instructions - last servings
        assertParseSuccess(parser, SERVINGS_DESC_BOB + LF
                        + NAME_DESC_AMY + LF + PREPARATION_TIME_DESC_AMY
                        + LF + INGREDIENT_DESC_AMY + LF + INSTRUCTION_DESC_AMY
                        + LF + URL_DESC_AMY + LF + IMG_DESC_AMY + LF + COOKING_TIME_DESC_AMY
                        + LF + CALORIES_DESC_AMY + LF + SERVINGS_DESC_AMY,
                new AddCommand(expectedRecipe));

        // multiple url - last url accepted
        assertParseSuccess(parser, URL_DESC_BOB + LF
                        + NAME_DESC_AMY + LF + PREPARATION_TIME_DESC_AMY
                        + LF + INGREDIENT_DESC_AMY + LF + INSTRUCTION_DESC_AMY
                        + LF + URL_DESC_AMY + LF + URL_DESC_AMY + LF + IMG_DESC_AMY
                        + LF + COOKING_TIME_DESC_AMY + LF + CALORIES_DESC_AMY + LF + SERVINGS_DESC_AMY,
                new AddCommand(expectedRecipe));

        // multiple images - last image accepted
        assertParseSuccess(parser, URL_DESC_BOB + LF
                        + NAME_DESC_AMY + LF + PREPARATION_TIME_DESC_AMY
                        + LF + INGREDIENT_DESC_AMY + LF + INSTRUCTION_DESC_AMY
                        + LF + URL_DESC_AMY + LF + IMG_DESC_AMY + LF + IMG_DESC_AMY
                        + LF + COOKING_TIME_DESC_AMY + LF + CALORIES_DESC_AMY + LF + SERVINGS_DESC_AMY,
                new AddCommand(expectedRecipe));

        // multiple tags - all accepted
        Recipe expectedRecipeMultipleTags = new RecipeBuilder().withName(VALID_NAME_AMY).withServings(
                VALID_SERVINGS_AMY).withPreparationTime(VALID_PREPARATION_TIME_AMY).withIngredient(VALID_INGREDIENT_AMY)
                .withCookingTime(VALID_COOKING_TIME_AMY).withCalories(VALID_CALORIES_AMY)
                .withInstruction(VALID_INSTRUCTION_AMY).withUrl(VALID_URL_AMY).withImage(VALID_IMG_AMY)
                .withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND).build();
        assertParseSuccess(parser, NAME_DESC_AMY + LF + PREPARATION_TIME_DESC_AMY + LF + INGREDIENT_DESC_AMY
                        + LF + INSTRUCTION_DESC_AMY + LF + URL_DESC_AMY + LF + IMG_DESC_AMY
                        + LF + COOKING_TIME_DESC_AMY + LF + CALORIES_DESC_AMY + LF + SERVINGS_DESC_AMY,
                new AddCommand(expectedRecipeMultipleTags));
    }
```
###### \java\seedu\recipe\logic\parser\AddCommandParserTest.java
``` java
        // invalid ingredient
        assertParseFailure(parser,
                NAME_DESC_BOB + PREPARATION_TIME_DESC_BOB + INVALID_INGREDIENT_DESC + INSTRUCTION_DESC_BOB
                        + URL_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                Ingredient.MESSAGE_INGREDIENT_CONSTRAINTS);

        // invalid instruction
        assertParseFailure(parser,
                NAME_DESC_BOB + PREPARATION_TIME_DESC_BOB + INGREDIENT_DESC_BOB + INVALID_INSTRUCTION_DESC
                        + URL_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                Instruction.MESSAGE_INSTRUCTION_CONSTRAINTS);

        // invalid preparation time
        assertParseFailure(parser,
                NAME_DESC_BOB + INVALID_PREPARATION_TIME_DESC + INGREDIENT_DESC_BOB + INSTRUCTION_DESC_BOB
                        + URL_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                PreparationTime.MESSAGE_PREPARATION_TIME_CONSTRAINTS);

        // invalid cooking time
        assertParseFailure(parser,
                NAME_DESC_BOB + INVALID_COOKING_TIME_DESC + INGREDIENT_DESC_BOB + INSTRUCTION_DESC_BOB
                        + URL_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                CookingTime.MESSAGE_COOKING_TIME_CONSTRAINTS);

        // invalid calories
        assertParseFailure(parser,
                NAME_DESC_BOB + INVALID_CALORIES_DESC + INGREDIENT_DESC_BOB + INSTRUCTION_DESC_BOB
                        + URL_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                Calories.MESSAGE_CALORIES_CONSTRAINTS);

        // invalid servings
        assertParseFailure(parser,
                NAME_DESC_BOB + INVALID_SERVINGS_DESC + INGREDIENT_DESC_BOB + INSTRUCTION_DESC_BOB
                        + URL_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                Servings.MESSAGE_SERVINGS_CONSTRAINTS);

```
###### \java\seedu\recipe\logic\parser\RecipeBookParserTest.java
``` java
    @Test
    public void parseCommand_changeTheme() throws Exception {
        assertTrue(parser.parseCommand(ChangeThemeCommand.COMMAND_WORD) instanceof ChangeThemeCommand);
        assertTrue(parser.parseCommand(ChangeThemeCommand.COMMAND_WORD + " 3") instanceof ChangeThemeCommand);
    }

    @Test
    public void parseCommand_search() throws Exception {
        assertTrue(parser.parseCommand(SearchCommand.COMMAND_WORD + " chicken rice") instanceof SearchCommand);

        String keywords = "chicken rice";
        SearchCommand command = (SearchCommand) parser.parseCommand(SearchCommand.COMMAND_WORD + " " + keywords);
        assertEquals(new SearchCommand(keywords.replaceAll("\\s+", "+")), command);

        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchCommand.MESSAGE_USAGE));
        parser.parseCommand(SearchCommand.COMMAND_WORD);
    }

    @Test
    public void parseCommand_parse() throws Exception {
        assertTrue(parser.parseCommand(ParseCommand.COMMAND_WORD) instanceof ParseCommand);
        assertTrue(parser.parseCommand(ParseCommand.COMMAND_WORD + " 3") instanceof ParseCommand);
    }
```
###### \java\seedu\recipe\logic\parser\SearchCommandParserTest.java
``` java
package seedu.recipe.logic.parser;

import static seedu.recipe.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.recipe.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.recipe.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.recipe.logic.commands.SearchCommand;

public class SearchCommandParserTest {

    private SearchCommandParser parser = new SearchCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        SearchCommand expectedSearchCommand = new SearchCommand("chicken+rice");
        assertParseSuccess(parser, "chicken rice", expectedSearchCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n chicken \n \t rice  \t", expectedSearchCommand);
        assertParseSuccess(parser, "        chicken             rice            ", expectedSearchCommand);
    }

}
```
###### \java\seedu\recipe\model\recipe\CaloriesTest.java
``` java
package seedu.recipe.model.recipe;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.recipe.testutil.Assert;

public class CaloriesTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Calories(null));
    }

    @Test
    public void constructor_invalidCalories_throwsIllegalArgumentException() {
        String invalidCalories = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Calories(invalidCalories));
    }

    @Test
    public void isValidCalories() {
        // null calories number
        Assert.assertThrows(NullPointerException.class, () -> Calories.isValidCalories(null));

        // invalid calories number
        assertFalse(Calories.isValidCalories("")); // empty string
        assertFalse(Calories.isValidCalories(" ")); // spaces only
        assertFalse(Calories.isValidCalories("NaN")); // not a number
        assertFalse(Calories.isValidCalories("BLAHBLAHBLAH")); // non-numeric
        assertFalse(Calories.isValidCalories(".1111..")); // non-numeric

        // valid calories number
        assertTrue(Calories.isValidCalories("1000"));
        assertTrue(Calories.isValidCalories("1111"));
    }
}
```
###### \java\seedu\recipe\model\recipe\CookingTimeTest.java
``` java
package seedu.recipe.model.recipe;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.recipe.testutil.Assert;

public class CookingTimeTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new CookingTime(null));
    }

    @Test
    public void constructor_invalidCookingTime_throwsIllegalArgumentException() {
        String invalidCookingTime = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new CookingTime(invalidCookingTime));
    }

    @Test
    public void isValidCookingTime() {
        // null CookingTime
        Assert.assertThrows(NullPointerException.class, () -> CookingTime.isValidCookingTime(null));

        // invalid CookingTime
        assertFalse(CookingTime.isValidCookingTime("")); // empty string
        assertFalse(CookingTime.isValidCookingTime(" ")); // spaces only
        assertFalse(CookingTime.isValidCookingTime("NaN")); // not a number
        assertFalse(CookingTime.isValidCookingTime("BLAHBLAHBLAH")); // non-numeric
        assertFalse(CookingTime.isValidCookingTime("123aaaa55555")); // unknown character p
        assertFalse(CookingTime.isValidCookingTime("1h  6666m")); // more spaces than needed

        // valid CookingTime
        assertTrue(CookingTime.isValidCookingTime("1h10m"));
        assertTrue(CookingTime.isValidCookingTime("1 hour 10 min"));
        assertTrue(CookingTime.isValidCookingTime("1 hour 10 mins"));
        assertTrue(CookingTime.isValidCookingTime("1 hour 10 minutes"));
        assertTrue(CookingTime.isValidCookingTime("70"));
        assertTrue(CookingTime.isValidCookingTime("70m"));
        assertTrue(CookingTime.isValidCookingTime("70min"));
        assertTrue(CookingTime.isValidCookingTime("70 mins"));
        assertTrue(CookingTime.isValidCookingTime("5h20m"));
        assertTrue(CookingTime.isValidCookingTime("5 hours 20 mins"));
        assertTrue(CookingTime.isValidCookingTime("5 hours 20 minutes"));
    }
}
```
###### \java\seedu\recipe\model\recipe\PreparationTimeTest.java
``` java
package seedu.recipe.model.recipe;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.recipe.testutil.Assert;

public class PreparationTimeTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new PreparationTime(null));
    }

    @Test
    public void constructor_invalidPreparationTime_throwsIllegalArgumentException() {
        String invalidPreparationTime = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new PreparationTime(invalidPreparationTime));
    }

    @Test
    public void isValidPreparationTime() {
        // null PreparationTime
        Assert.assertThrows(NullPointerException.class, () -> PreparationTime.isValidPreparationTime(null));

        // invalid PreparationTime
        assertFalse(PreparationTime.isValidPreparationTime("")); // empty string
        assertFalse(PreparationTime.isValidPreparationTime(" ")); // spaces only
        assertFalse(PreparationTime.isValidPreparationTime("NaN")); // not a number
        assertFalse(PreparationTime.isValidPreparationTime("preparationTime")); // non-numeric
        assertFalse(PreparationTime.isValidPreparationTime("9011p041")); // unknown character p
        assertFalse(PreparationTime.isValidPreparationTime("1h  1534m")); // more spaces than needed

        // valid PreparationTime
        assertTrue(PreparationTime.isValidPreparationTime("1h20m"));
        assertTrue(PreparationTime.isValidPreparationTime("1 hour 20 min"));
        assertTrue(PreparationTime.isValidPreparationTime("1 hour 20 mins"));
        assertTrue(PreparationTime.isValidPreparationTime("1 hour 20 minutes"));
        assertTrue(PreparationTime.isValidPreparationTime("80"));
        assertTrue(PreparationTime.isValidPreparationTime("80m"));
        assertTrue(PreparationTime.isValidPreparationTime("80min"));
        assertTrue(PreparationTime.isValidPreparationTime("80 mins"));
        assertTrue(PreparationTime.isValidPreparationTime("2h20m"));
        assertTrue(PreparationTime.isValidPreparationTime("2 hours 20 mins"));
        assertTrue(PreparationTime.isValidPreparationTime("2 hours 20 minutes"));
    }
}
```
###### \java\seedu\recipe\model\recipe\ServingsTest.java
``` java
package seedu.recipe.model.recipe;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.recipe.testutil.Assert;

public class ServingsTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Servings(null));
    }

    @Test
    public void constructor_invalidServings_throwsIllegalArgumentException() {
        String invalidServings = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Servings(invalidServings));
    }

    @Test
    public void isValidServings() {
        // null servings number
        Assert.assertThrows(NullPointerException.class, () -> Servings.isValidServings(null));

        // invalid servings number
        assertFalse(Servings.isValidServings("")); // empty string
        assertFalse(Servings.isValidServings(" ")); // spaces only
        assertFalse(Servings.isValidServings("NaN")); // not a number
        assertFalse(Servings.isValidServings("BLAHBLAHBLAH")); // non-numeric
        assertFalse(Servings.isValidServings(".1111..")); // non-numeric

        // valid servings number
        assertTrue(Servings.isValidServings("2"));
        assertTrue(Servings.isValidServings("10"));
    }
}
```
###### \java\seedu\recipe\storage\XmlAdaptedRecipeTest.java
``` java
    @Test
    public void toModelType_invalidIngredient_throwsIllegalValueException() {
        XmlAdaptedRecipe recipe =
            new XmlAdaptedRecipe(VALID_NAME, INVALID_INGREDIENT, VALID_INSTRUCTION, VALID_COOKING_TIME,
                    VALID_PREPARATION_TIME, VALID_CALORIES, VALID_SERVINGS, VALID_URL, VALID_IMAGE, VALID_TAGS);
        String expectedMessage = Ingredient.MESSAGE_INGREDIENT_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, recipe::toModelType);
    }

    @Test
    public void toModelType_nullIngredient_throwsIllegalValueException() {
        XmlAdaptedRecipe recipe = new XmlAdaptedRecipe(VALID_NAME, null, VALID_INSTRUCTION, VALID_COOKING_TIME,
                VALID_PREPARATION_TIME, VALID_CALORIES, VALID_SERVINGS, VALID_URL, VALID_IMAGE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Ingredient.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, recipe::toModelType);
    }

    @Test
    public void toModelType_invalidInstruction_throwsIllegalValueException() {
        XmlAdaptedRecipe recipe =
            new XmlAdaptedRecipe(VALID_NAME, VALID_INGREDIENT, INVALID_INSTRUCTION, VALID_COOKING_TIME,
                    VALID_PREPARATION_TIME, VALID_CALORIES, VALID_SERVINGS, VALID_URL, VALID_IMAGE, VALID_TAGS);
        String expectedMessage = Instruction.MESSAGE_INSTRUCTION_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, recipe::toModelType);
    }

    @Test
    public void toModelType_nullInstruction_throwsIllegalValueException() {
        XmlAdaptedRecipe recipe = new XmlAdaptedRecipe(VALID_NAME, VALID_INGREDIENT, null,
                VALID_COOKING_TIME, VALID_PREPARATION_TIME, VALID_CALORIES, VALID_SERVINGS, VALID_URL,
                VALID_IMAGE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Instruction.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, recipe::toModelType);
    }

    @Test
    public void toModelType_invalidPreparationTime_throwsIllegalValueException() {
        XmlAdaptedRecipe recipe =
                new XmlAdaptedRecipe(VALID_NAME, VALID_INGREDIENT, VALID_INSTRUCTION, VALID_COOKING_TIME,
                        INVALID_PREPARATION_TIME, VALID_CALORIES, VALID_SERVINGS, VALID_URL,
                        VALID_IMAGE, VALID_TAGS);
        String expectedMessage = PreparationTime.MESSAGE_PREPARATION_TIME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, recipe::toModelType);
    }

    @Test
    public void toModelType_nullPreparationTime_throwsIllegalValueException() {
        XmlAdaptedRecipe recipe = new XmlAdaptedRecipe(VALID_NAME, VALID_INGREDIENT, VALID_INSTRUCTION,
                VALID_COOKING_TIME, null, VALID_CALORIES, VALID_SERVINGS, VALID_URL,
                VALID_IMAGE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, PreparationTime.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, recipe::toModelType);
    }

    @Test
    public void toModelType_invalidCookingTime_throwsIllegalValueException() {
        XmlAdaptedRecipe recipe =
                new XmlAdaptedRecipe(VALID_NAME, VALID_INGREDIENT, VALID_INSTRUCTION, INVALID_COOKING_TIME,
                        VALID_PREPARATION_TIME, VALID_CALORIES, VALID_SERVINGS, VALID_URL,
                        VALID_IMAGE, VALID_TAGS);
        String expectedMessage = CookingTime.MESSAGE_COOKING_TIME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, recipe::toModelType);
    }

    @Test
    public void toModelType_nullCookingTime_throwsIllegalValueException() {
        XmlAdaptedRecipe recipe = new XmlAdaptedRecipe(VALID_NAME, VALID_INGREDIENT, VALID_INSTRUCTION,
                null, VALID_PREPARATION_TIME, VALID_CALORIES, VALID_SERVINGS, VALID_URL,
                VALID_IMAGE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, CookingTime.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, recipe::toModelType);
    }

    @Test
    public void toModelType_invalidCalories_throwsIllegalValueException() {
        XmlAdaptedRecipe recipe =
                new XmlAdaptedRecipe(VALID_NAME, VALID_INGREDIENT, VALID_INSTRUCTION, VALID_PREPARATION_TIME,
                        VALID_COOKING_TIME, INVALID_CALORIES, VALID_SERVINGS, VALID_URL,
                        VALID_IMAGE, VALID_TAGS);
        String expectedMessage = Calories.MESSAGE_CALORIES_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, recipe::toModelType);
    }

    @Test
    public void toModelType_nullCalories_throwsIllegalValueException() {
        XmlAdaptedRecipe recipe = new XmlAdaptedRecipe(VALID_NAME, VALID_INGREDIENT, VALID_INSTRUCTION,
                VALID_PREPARATION_TIME, VALID_COOKING_TIME, null, VALID_SERVINGS, VALID_URL,
                VALID_IMAGE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Calories.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, recipe::toModelType);
    }

    @Test
    public void toModelType_invalidServings_throwsIllegalValueException() {
        XmlAdaptedRecipe recipe =
                new XmlAdaptedRecipe(VALID_NAME, VALID_INGREDIENT, VALID_INSTRUCTION, VALID_PREPARATION_TIME,
                        VALID_COOKING_TIME, VALID_CALORIES, INVALID_SERVINGS, VALID_URL,
                        VALID_IMAGE, VALID_TAGS);
        String expectedMessage = Servings.MESSAGE_SERVINGS_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, recipe::toModelType);
    }

    @Test
    public void toModelType_nullServings_throwsIllegalValueException() {
        XmlAdaptedRecipe recipe = new XmlAdaptedRecipe(VALID_NAME, VALID_INGREDIENT, VALID_INSTRUCTION,
                VALID_PREPARATION_TIME, VALID_COOKING_TIME, VALID_CALORIES, null, VALID_URL,
                VALID_IMAGE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Servings.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, recipe::toModelType);
    }

```
###### \java\seedu\recipe\testutil\EditRecipeDescriptorBuilder.java
``` java
    /**
     * Sets the {@code Ingredient} of the {@code EditRecipeDescriptor} that we are building.
     */
    public EditRecipeDescriptorBuilder withIngredient(String ingredient) {
        descriptor.setIngredient(new Ingredient(ingredient));
        return this;
    }

    /**
     * Sets the {@code Instruction} of the {@code EditRecipeDescriptor} that we are building.
     */
    public EditRecipeDescriptorBuilder withInstruction(String instruction) {
        descriptor.setInstruction(new Instruction(instruction));
        return this;
    }

    /**
     * Sets the {@code CookingTime} of the {@code EditRecipeDescriptor} that we are building.
     */
    public EditRecipeDescriptorBuilder withCookingTime(String cookingTime) {
        descriptor.setCookingTime(new CookingTime(cookingTime));
        return this;
    }

    /**
     * Sets the {@code PreparationTime} of the {@code EditRecipeDescriptor} that we are building.
     */
    public EditRecipeDescriptorBuilder withPreparationTime(String preparationTime) {
        descriptor.setPreparationTime(new PreparationTime(preparationTime));
        return this;
    }

    /**
     * Sets the {@code Calories} of the {@code EditRecipeDescriptor} that we are building.
     */
    public EditRecipeDescriptorBuilder withCalories(String calories) {
        descriptor.setCalories(new Calories(calories));
        return this;
    }

    /**
     * Sets the {@code Servings} of the {@code EditRecipeDescriptor} that we are building.
     */
    public EditRecipeDescriptorBuilder withServings(String servings) {
        descriptor.setServings(new Servings(servings));
        return this;
    }
```
###### \java\seedu\recipe\testutil\RecipeBuilder.java
``` java
    /**
     * Sets the {@code Instruction} of the {@code Recipe} that we are building.
     */
    public RecipeBuilder withInstruction(String instruction) {
        this.instruction = new Instruction(instruction);
        return this;
    }

    /**
     * Sets the {@code Ingredient} of the {@code Recipe} that we are building.
     */
    public RecipeBuilder withIngredient(String ingredient) {
        this.ingredient = new Ingredient(ingredient);
        return this;
    }

    /**
     * Sets the {@code CookingTime} of the {@code Recipe} that we are building.
     */
    public RecipeBuilder withCookingTime(String cookingTime) {
        this.cookingTime = new CookingTime(cookingTime);
        return this;
    }

    /**
     * Sets the {@code PreparationTime} of the {@code Recipe} that we are building.
     */
    public RecipeBuilder withPreparationTime(String preparationTime) {
        this.preparationTime = new PreparationTime(preparationTime);
        return this;
    }

    /**
     * Sets the {@code Calories} of the {@code Recipe} that we are building.
     */
    public RecipeBuilder withCalories(String calories) {
        this.calories = new Calories(calories);
        return this;
    }

    /**
     * Sets the {@code Servings} of the {@code Recipe} that we are building.
     */
    public RecipeBuilder withServings(String servings) {
        this.servings = new Servings(servings);
        return this;
    }

```
###### \java\seedu\recipe\testutil\WikiaRecipes.java
``` java
package seedu.recipe.testutil;

import static seedu.recipe.logic.commands.AddCommand.COMMAND_WORD;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_IMG;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_INGREDIENT;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_INSTRUCTION;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_URL;

/**
 * A utility class containing a list of {@code Recipe} objects parsed from Wikia to be used in tests.
 */
public class WikiaRecipes {
    public static final String LF = "\n";

    public static final String WIKIA_NOT_RECIPE = "http://recipes.wikia.com/d/f?sort=latest";

    public static final String CHICKEN_NAME = "Hainanese Chicken Rice";
    public static final String CHICKEN_INGREDIENT = "chicken, salt, spring onion, pandan leaves, "
            + "ginger, ginger, garlic, cinnamon, cloves, star anise, chicken broth, pandan leaves, salt,"
            + " light soy sauce, sesame oil, cucumber, tomatoes, coriander, lettuce, pineapple, fresh "
            + "chillies, ginger, garlic, vinegar, fish sauce, sugar, sweet soy sauce";
    public static final String CHICKEN_INSTRUCTION = "Boil water with spring Onion, ginger and pandan leaves, "
            + "put in Chicken and cook till done,"
            + " do not over cook. briefly dip in cold water and set aside to cool. Keep broth heated.\n"
            + "Wash rice and drain. Finely shred ginger and garlic, fry in oil with cloves, cinammon and "
            + "star anise till fragrant, add in rice and fry for several minutes. Transfer into rice "
            + "cooker, add chicken broth, pinch of salt, pandan leaves and start cooking.\n"
            + "Put all chili sauce ingredient in a mixer and grind till fine.\n"
            + "Slice and arrange tomatoes and cucumbers on a big plate, cut Chicken into small pieces and p"
            + "ut on top. Splash some light soy sauce and sesame oil over, throw a bunch of coriander "
            + "on top.\nNext, Put broth in a bowl with lettuce, get ready chili sauce and sweet "
            + "soy sauce. #Serve rice on a plate with spoon and folk.";
    public static final String WIKIA_RECIPE_URL_CHICKEN = "http://recipes.wikia.com/wiki/Hainanese_Chicken_Rice";
    public static final String CHICKEN_RICE_IMAGE_URL = "https://vignette.wikia.nocookie.net/recipes/images/d/d3"
            + "/Chickenrice2.jpg/revision/latest/scale-to-width-down/180?cb=20080516004325";
    public static final String MOBILE_WIKIA_RECIPE_URL_CHICKEN =
            "http://recipes.wikia.com/wiki/Hainanese_Chicken_Rice?useskin=wikiamobile";
    public static final String MOBILE_CHICKEN_RICE_IMAGE_URL = "https://vignette.wikia.nocookie.net/recipes/images/d/d3"
            + "/Chickenrice2.jpg/revision/latest/scale-to-width-down/340?cb=20080516004325";

    public static final String UGANDAN_NAME = "Ugandan Chicken Stew";
    public static final String UGANDAN_INGREDIENT = "chicken, oil, onion, tomatoes, potatoes, salt, pepper";
    public static final String UGANDAN_INSTRUCTION = "In a heavy stewing pan, sauté chicken pieces "
            + "in hot oil until nicely browned.\n"
            + "Add onion, tomatoes, potatoes, salt, pepper, and enough water to just cover.\n"
            + "Cover pan and simmer until chicken is cooked, 45 minutes to 1 hour.";
    public static final String WIKIA_RECIPE_URL_UGANDAN = "http://recipes.wikia.com/wiki/Ugandan_Chicken_Stew";
    public static final String MOBILE_WIKIA_RECIPE_URL_UGANDAN =
            "http://recipes.wikia.com/wiki/Ugandan_Chicken_Stew?useskin=wikiamobile";

    public static final String WIKIA_RECIPE_URL_BEEF =
            "http://recipes.wikia.com/wiki/Beef_Tenderloin_with_Madeira_Sauce";
    public static final String BEEF_INGREDIENT = "1 cup of garlic, 2 cups of mustard, 3 tbs chopped rosemary, 1 cup "
            + "chopped thyme, 2 tsp garlic, 2 tsp vegtebale oil, 4 tsp salt, 1 tsp pepper, 3 cups of water, 4 "
            + "tbs butter, 2 cups of red wine, 1 cup of garlic, 2 1/2 cups of corn, 4 cup of water, 2 tomatoes,"
            + " 1 tsp chopped thyme, 1/2 tsp each sea salt and pepper";

    public static final String WIKIA_CHICKEN_ADD_COMMAND =
            COMMAND_WORD + LF + PREFIX_NAME + CHICKEN_NAME + LF + PREFIX_INGREDIENT + CHICKEN_INGREDIENT + LF
                    + PREFIX_INSTRUCTION + CHICKEN_INSTRUCTION + LF + PREFIX_IMG + CHICKEN_RICE_IMAGE_URL + LF
                    + PREFIX_URL + WIKIA_RECIPE_URL_CHICKEN;
    public static final String WIKIA_UGANDAN_ADD_COMMAND =
            COMMAND_WORD + LF + PREFIX_NAME + UGANDAN_NAME + LF + PREFIX_INGREDIENT + UGANDAN_INGREDIENT + LF
                    + PREFIX_INSTRUCTION + UGANDAN_INSTRUCTION + LF + PREFIX_URL + WIKIA_RECIPE_URL_UGANDAN;
    public static final String MOBILE_WIKIA_CHICKEN_ADD_COMMAND =
            COMMAND_WORD + LF + PREFIX_NAME + CHICKEN_NAME + LF + PREFIX_INGREDIENT + CHICKEN_INGREDIENT + LF
                    + PREFIX_INSTRUCTION + CHICKEN_INSTRUCTION + LF + PREFIX_IMG + MOBILE_CHICKEN_RICE_IMAGE_URL + LF
                    + PREFIX_URL + MOBILE_WIKIA_RECIPE_URL_CHICKEN;
    public static final String MOBILE_WIKIA_UGANDAN_ADD_COMMAND =
            COMMAND_WORD + LF + PREFIX_NAME + UGANDAN_NAME + LF + PREFIX_INGREDIENT + UGANDAN_INGREDIENT + LF
                    + PREFIX_INSTRUCTION + UGANDAN_INSTRUCTION + LF + PREFIX_URL + MOBILE_WIKIA_RECIPE_URL_UGANDAN;

    private WikiaRecipes() {
    } // prevents instantiation
}
```
###### \java\seedu\recipe\ui\CommandBoxTest.java
``` java
    @Test
    public void commandBox_handleMultipleLinesCommand() {
        commandBoxHandle.appendText(FIRST_LINE_OF_COMMAND_THAT_HAS_MULTIPLE_LINES);
        guiRobot.push(KeyboardShortcutsMapping.NEW_LINE_IN_COMMAND);
        commandBoxHandle.appendText(SECOND_LINE_OF_COMMAND_THAT_HAS_MULTIPLE_LINES);
        assertInput(COMMAND_THAT_HAS_MULTIPLE_LINES);
    }
```
###### \java\seedu\recipe\ui\CommandBoxTest.java
``` java
    /**
     * Checks that the input in the {@code commandBox} equals to {@code expectedCommand}.
     */
    private void assertInput(String expectedCommand) {
        assertEquals(expectedCommand, commandBoxHandle.getInput());
    }
}
```
###### \java\seedu\recipe\ui\parser\MobileWikiaParserTest.java
``` java
package seedu.recipe.ui.parser;

import static org.junit.Assert.assertEquals;
import static seedu.recipe.testutil.WikiaRecipes.CHICKEN_INGREDIENT;
import static seedu.recipe.testutil.WikiaRecipes.CHICKEN_INSTRUCTION;
import static seedu.recipe.testutil.WikiaRecipes.CHICKEN_NAME;
import static seedu.recipe.testutil.WikiaRecipes.MOBILE_CHICKEN_RICE_IMAGE_URL;
import static seedu.recipe.testutil.WikiaRecipes.MOBILE_WIKIA_CHICKEN_ADD_COMMAND;
import static seedu.recipe.testutil.WikiaRecipes.MOBILE_WIKIA_RECIPE_URL_CHICKEN;
import static seedu.recipe.testutil.WikiaRecipes.MOBILE_WIKIA_RECIPE_URL_UGANDAN;
import static seedu.recipe.testutil.WikiaRecipes.MOBILE_WIKIA_UGANDAN_ADD_COMMAND;
import static seedu.recipe.testutil.WikiaRecipes.UGANDAN_INGREDIENT;
import static seedu.recipe.testutil.WikiaRecipes.UGANDAN_INSTRUCTION;
import static seedu.recipe.testutil.WikiaRecipes.UGANDAN_NAME;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.recipe.ui.GuiUnitTest;

public class MobileWikiaParserTest extends GuiUnitTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private MobileWikiaParser wikiaParserChicken;
    private MobileWikiaParser wikiaParserUgandan;

    @Before
    public void setUp() throws IOException {
        wikiaParserChicken = new MobileWikiaParser(Jsoup.connect(MOBILE_WIKIA_RECIPE_URL_CHICKEN).get());
        wikiaParserUgandan = new MobileWikiaParser(Jsoup.connect(MOBILE_WIKIA_RECIPE_URL_UGANDAN).get());
    }

    @Test
    public void constructor_nullArgument_throwsException() {
        thrown.expect(NullPointerException.class);
        new MobileWikiaParser(null);
        new MobileWikiaParser(null, "");
    }

    @Test
    public void equals() {
        String testDocumentString = "<html>Test</html>";
        String testUrl = "127.0.0.1";
        MobileWikiaParser wikiaParserA = new MobileWikiaParser(testDocumentString, testUrl);
        MobileWikiaParser wikiaParserB = new MobileWikiaParser(Jsoup.parse(testDocumentString, testUrl));
        assertEquals(wikiaParserA, wikiaParserB);
    }

    @Test
    public void getName_validRecipes_returnsResult() throws Exception {
        assertEquals(wikiaParserChicken.getName(), CHICKEN_NAME);
        assertEquals(wikiaParserUgandan.getName(), UGANDAN_NAME);
    }

    @Test
    public void getIngredient_validRecipes_returnsResult() throws Exception {
        assertEquals(wikiaParserChicken.getIngredient(), CHICKEN_INGREDIENT);
        assertEquals(wikiaParserUgandan.getIngredient(), UGANDAN_INGREDIENT);
    }

    @Test
    public void getInstruction_validRecipes_returnsResult() throws Exception {
        assertEquals(wikiaParserChicken.getInstruction(), CHICKEN_INSTRUCTION);
        assertEquals(wikiaParserUgandan.getInstruction(), UGANDAN_INSTRUCTION);
    }

    @Test
    public void getImageUrl_validRecipes_returnsResult() throws Exception {
        assertEquals(wikiaParserChicken.getImageUrl(), MOBILE_CHICKEN_RICE_IMAGE_URL);
    }

    @Test
    public void getUrl_validRecipes_returnsResult() throws Exception {
        assertEquals(wikiaParserChicken.getUrl(), MOBILE_WIKIA_RECIPE_URL_CHICKEN);
        assertEquals(wikiaParserUgandan.getUrl(), MOBILE_WIKIA_RECIPE_URL_UGANDAN);
    }

    @Test
    public void parseRecipe_validRecipe_returnsValidCommand() throws Exception {
        assertEquals(wikiaParserChicken.parseRecipe(), MOBILE_WIKIA_CHICKEN_ADD_COMMAND);
        assertEquals(wikiaParserUgandan.parseRecipe(), MOBILE_WIKIA_UGANDAN_ADD_COMMAND);
    }
}
```
###### \java\seedu\recipe\ui\parser\WebParserHandlerTest.java
``` java
package seedu.recipe.ui.parser;

import static org.junit.Assert.assertEquals;

import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.nodes.Document;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import guitests.guihandles.BrowserPanelHandle;
import seedu.recipe.ui.BrowserPanel;
import seedu.recipe.ui.GuiUnitTest;

public class WebParserHandlerTest extends GuiUnitTest {

    private static final String WIKIA_RECIPE_URL = "http://recipes.wikia.com/wiki/Hainanese_Chicken_Rice";
    private static final String DUMMY_URL = "https://google.com/";
    private static final String DUMMY_DOCUMENT_WIKIA = "<html><div id=\"mw-content-text\">something</div></html>";
    private static final String DUMMY_DOCUMENT_WIKIA_MOBILE = "<html></html>";
    private static final String DUMMY_DOCUMENT_EMPTY = "<html></html>";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private WebParserHandler webParserHandler;
    private BrowserPanel browserPanel;
    private BrowserPanelHandle browserPanelHandle;

    @Test
    public void constructor_nullBrowser_throwsException() throws Exception {
        thrown.expect(NullPointerException.class);
        webParserHandler = new WebParserHandler(null);
    }

    @Test
    public void getWebParser_nothingLoaded_returnNull() throws Exception {
        guiRobot.interact(() -> browserPanel = new BrowserPanel(true));
        uiPartRule.setUiPart(browserPanel);
        browserPanelHandle = new BrowserPanelHandle(browserPanel.getRoot());
        webParserHandler = new WebParserHandler(browserPanel.getBrowser());
        WebParser actualWebParser = webParserHandler.getWebParser();
        assertEquals(null, actualWebParser);
    }

    @Test
    public void getWebParser_unparsableWebsite_returnNull() throws Exception {
        assertNullWebParser(DUMMY_URL, "");
    }

    @Test
    public void getWebParser_wikiaLoaded_returnWikiaParser() throws Exception {
        assertWebParser(WIKIA_RECIPE_URL, DUMMY_DOCUMENT_WIKIA,
                new WikiaParser(new Document("")));
    }

    @Test
    public void getWebParser_mobileWikiaLoaded_returnMobileWikiaParser() throws Exception {
        assertWebParser(WIKIA_RECIPE_URL, DUMMY_DOCUMENT_WIKIA_MOBILE,
                new MobileWikiaParser(new Document("")));
    }

    /**
     * Asserts that the created WebParser from the {@code url} matches the {@code expectedWebParser}
     */
    private void assertWebParser(String url, String documentString, WebParser expectedWebParser)
            throws ParserConfigurationException {
        WebParser actualWebParser = WebParserHandler.getWebParser(url, documentString);
        assertEquals(expectedWebParser.getClass(), actualWebParser.getClass());
    }

    /**
     * Asserts that the created WebParser from the {@code url} doesn't exist
     */
    private void assertNullWebParser(String url, String documentString) throws ParserConfigurationException {
        WebParser actualWebParser = WebParserHandler.getWebParser(url, documentString);
        assertEquals(null, actualWebParser);
    }
}
```
###### \java\seedu\recipe\ui\parser\WikiaParserTest.java
``` java
package seedu.recipe.ui.parser;

import static org.junit.Assert.assertEquals;
import static seedu.recipe.testutil.WikiaRecipes.BEEF_INGREDIENT;
import static seedu.recipe.testutil.WikiaRecipes.CHICKEN_INGREDIENT;
import static seedu.recipe.testutil.WikiaRecipes.CHICKEN_INSTRUCTION;
import static seedu.recipe.testutil.WikiaRecipes.CHICKEN_NAME;
import static seedu.recipe.testutil.WikiaRecipes.CHICKEN_RICE_IMAGE_URL;
import static seedu.recipe.testutil.WikiaRecipes.UGANDAN_INGREDIENT;
import static seedu.recipe.testutil.WikiaRecipes.UGANDAN_INSTRUCTION;
import static seedu.recipe.testutil.WikiaRecipes.UGANDAN_NAME;
import static seedu.recipe.testutil.WikiaRecipes.WIKIA_CHICKEN_ADD_COMMAND;
import static seedu.recipe.testutil.WikiaRecipes.WIKIA_NOT_RECIPE;
import static seedu.recipe.testutil.WikiaRecipes.WIKIA_RECIPE_URL_BEEF;
import static seedu.recipe.testutil.WikiaRecipes.WIKIA_RECIPE_URL_CHICKEN;
import static seedu.recipe.testutil.WikiaRecipes.WIKIA_RECIPE_URL_UGANDAN;
import static seedu.recipe.testutil.WikiaRecipes.WIKIA_UGANDAN_ADD_COMMAND;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.recipe.ui.GuiUnitTest;

public class WikiaParserTest extends GuiUnitTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private WikiaParser wikiaParserChicken;
    private WikiaParser wikiaParserBeef;
    private WikiaParser wikiaParserUgandan;
    private WikiaParser wikiaParserNotRecipe;

    @Before
    public void setUp() throws IOException {
        wikiaParserChicken = new WikiaParser(Jsoup.connect(WIKIA_RECIPE_URL_CHICKEN).get());
        wikiaParserUgandan = new WikiaParser(Jsoup.connect(WIKIA_RECIPE_URL_UGANDAN).get());
        wikiaParserBeef = new WikiaParser(Jsoup.connect(WIKIA_RECIPE_URL_BEEF).get());
        wikiaParserNotRecipe = new WikiaParser(Jsoup.connect(WIKIA_NOT_RECIPE).get());
    }

    @Test
    public void constructor_nullArgument_throwsException() {
        thrown.expect(NullPointerException.class);
        new WikiaParser(null);
        new WikiaParser(null, "");
    }

    @Test
    public void equals() {
        String testDocumentString = "<html>Test</html>";
        String testUrl = "127.0.0.1";
        WikiaParser wikiaParserA = new WikiaParser(testDocumentString, testUrl);
        WikiaParser wikiaParserB = new WikiaParser(Jsoup.parse(testDocumentString, testUrl));
        assertEquals(wikiaParserA, wikiaParserB);
    }

    @Test
    public void getName_validRecipes_returnsResult() throws Exception {
        assertEquals(wikiaParserChicken.getName(), CHICKEN_NAME);
        assertEquals(wikiaParserUgandan.getName(), UGANDAN_NAME);
    }

    @Test
    public void getIngredient_validRecipes_returnsResult() throws Exception {
        assertEquals(wikiaParserBeef.getIngredient(), BEEF_INGREDIENT);
        assertEquals(wikiaParserChicken.getIngredient(), CHICKEN_INGREDIENT);
        assertEquals(wikiaParserUgandan.getIngredient(), UGANDAN_INGREDIENT);
    }

    @Test
    public void getInstruction_validRecipes_returnsResult() throws Exception {
        assertEquals(wikiaParserChicken.getInstruction(), CHICKEN_INSTRUCTION);
        assertEquals(wikiaParserUgandan.getInstruction(), UGANDAN_INSTRUCTION);
    }

    @Test
    public void getImageUrl_validRecipes_returnsResult() throws Exception {
        assertEquals(wikiaParserChicken.getImageUrl(), CHICKEN_RICE_IMAGE_URL);
    }

    @Test
    public void getUrl_validRecipes_returnsResult() throws Exception {
        assertEquals(wikiaParserChicken.getUrl(), WIKIA_RECIPE_URL_CHICKEN);
        assertEquals(wikiaParserUgandan.getUrl(), WIKIA_RECIPE_URL_UGANDAN);
    }

    @Test
    public void parseRecipe_validRecipe_returnsValidCommand() throws Exception {
        String a = WIKIA_CHICKEN_ADD_COMMAND;
        assertEquals(wikiaParserChicken.parseRecipe(), WIKIA_CHICKEN_ADD_COMMAND);
        assertEquals(wikiaParserUgandan.parseRecipe(), WIKIA_UGANDAN_ADD_COMMAND);
    }

    @Test
    public void getName_notRecipe_throwsNullPointer() throws Exception {
        thrown.expect(NullPointerException.class);
        wikiaParserNotRecipe.getName();
    }

    @Test
    public void getIngredient_notRecipe_throwsNullPointer() throws Exception {
        thrown.expect(NullPointerException.class);
        wikiaParserNotRecipe.getIngredient();
    }

    @Test
    public void getInstruction_notRecipe_throwsNullPointer() throws Exception {
        thrown.expect(NullPointerException.class);
        wikiaParserNotRecipe.getInstruction();
    }

    @Test
    public void getImageUrl_notRecipe_throwsNullPointer() throws Exception {
        thrown.expect(NullPointerException.class);
        wikiaParserNotRecipe.getImageUrl();
    }

    @Test
    public void parseRecipe_notRecipe_throwsNullPointer() throws Exception {
        thrown.expect(NullPointerException.class);
        wikiaParserNotRecipe.parseRecipe();
    }
}
```
###### \java\systemtests\ChangeThemeSystemTest.java
``` java
package systemtests;

import static org.junit.Assert.assertTrue;
import static seedu.recipe.TestApp.APP_TITLE;
import static GIRL_THEME_CSS;
import static seedu.recipe.ui.MainWindow.LIGHT_THEME_CSS;
import static seedu.recipe.ui.UiPart.FXML_FILE_FOLDER;

import org.junit.Test;

import guitests.GuiRobot;
import seedu.recipe.MainApp;
import seedu.recipe.logic.commands.ChangeThemeCommand;

public class ChangeThemeSystemTest extends RecipeBookSystemTest {
    private static final String ERROR_MESSAGE = "ATTENTION!!!! : On some computers, this test may fail when run on "
            + "non-headless mode as FxRobot#clickOn(Node, MouseButton...) clicks on the wrong location. We suspect "
            + "that this is a bug with TestFX library that we are using. If this test fails, you have to run your "
            + "tests on headless mode. See UsingGradle.adoc on how to do so.";

    private final GuiRobot guiRobot = new GuiRobot();

    @Test
    public void changeTheme() {

        // Use accelerator
        getCommandBox().click();
        getMainMenu().changeThemeUsingAccelerator();
        assertDarkTheme();

        getResultDisplay().click();
        getMainMenu().changeThemeUsingAccelerator();
        assertLightTheme();

        getResultDisplay().click();
        getMainMenu().changeThemeUsingAccelerator();
        assertDarkTheme();

        getCommandBox().click();
        getMainMenu().changeThemeUsingAccelerator();
        assertLightTheme();

        // Use menu button
        getMainMenu().changeThemeUsingMenu();
        assertDarkTheme();

        getMainMenu().changeThemeUsingMenu();
        assertLightTheme();

        // Change theme with command
        executeCommand(ChangeThemeCommand.COMMAND_WORD);
        assertDarkTheme();

        executeCommand(ChangeThemeCommand.COMMAND_WORD);
        assertLightTheme();
    }

    /**
     * Asserts that the current theme color is dark
     */
    private void assertDarkTheme() {
        assertTrue(ERROR_MESSAGE, guiRobot.getStage(APP_TITLE).getScene().getStylesheets().get(0)
                .equals(MainApp.class.getResource(FXML_FILE_FOLDER + DARK_THEME_CSS).toExternalForm()));
        guiRobot.pauseForHuman();
    }

    /**
     * Asserts that the current theme color is light
     */
    private void assertLightTheme() {
        assertTrue(ERROR_MESSAGE, guiRobot.getStage(APP_TITLE).getScene().getStylesheets().get(0)
                .equals(MainApp.class.getResource(FXML_FILE_FOLDER + LIGHT_THEME_CSS).toExternalForm()));
        guiRobot.pauseForHuman();
    }
}
```
###### \java\systemtests\ParseCommandSystemTest.java
``` java
package systemtests;

import static seedu.recipe.testutil.TypicalIndexes.INDEX_FIRST_RECIPE;
import static seedu.recipe.testutil.TypicalIndexes.INDEX_SECOND_RECIPE;
import static seedu.recipe.testutil.TypicalRecipes.getTypicalRecipeBook;
import static seedu.recipe.testutil.WikiaRecipes.MOBILE_WIKIA_UGANDAN_ADD_COMMAND;
import static seedu.recipe.testutil.WikiaRecipes.WIKIA_CHICKEN_ADD_COMMAND;

import org.junit.Test;

import seedu.recipe.logic.commands.ParseCommand;
import seedu.recipe.logic.commands.SelectCommand;
import seedu.recipe.model.Model;
import seedu.recipe.model.ModelManager;
import seedu.recipe.model.UserPrefs;

/**
 * A system test class for the search command, which contains interaction with other UI components.
 */
public class ParseCommandSystemTest extends RecipeBookSystemTest {

    private Model model = new ModelManager(getTypicalRecipeBook(), new UserPrefs());

    @Test
    public void parse() {
        String command = SelectCommand.COMMAND_WORD + " " + INDEX_FIRST_RECIPE.getOneBased();
        executeCommand(command);

        assertCommandSuccess(WIKIA_CHICKEN_ADD_COMMAND);

        command = SelectCommand.COMMAND_WORD + " " + INDEX_SECOND_RECIPE.getOneBased();
        executeCommand(command);

        assertCommandSuccess(MOBILE_WIKIA_UGANDAN_ADD_COMMAND);
    }

    /**
     * Assert that the {@code SearchCommand} was executed correctly
     * and the current content of the CommandBox is {@code content}
     */
    private void assertCommandSuccess(String content) {

        executeCommand(ParseCommand.COMMAND_WORD);
        assertStatusBarUnchanged();
        assertCommandBoxContent(content);
    }
}
```
###### \java\systemtests\RecipeBookSystemTest.java
``` java
    /**
     * Asserts that the command box is showing the {@code content}
     */
    protected void assertCommandBoxContent(String content) {
        assertEquals(content, getCommandBox().getInput());
    }
```
###### \java\systemtests\SearchCommandSystemTest.java
``` java
package systemtests;

import static org.junit.Assert.assertEquals;
import static seedu.recipe.testutil.TypicalRecipes.getTypicalRecipeBook;

import org.junit.Test;

import seedu.recipe.logic.commands.SearchCommand;
import seedu.recipe.logic.commands.util.WikiaQueryHandler;
import seedu.recipe.model.Model;
import seedu.recipe.model.ModelManager;
import seedu.recipe.model.UserPrefs;

/**
 * A system test class for the search command, which contains interaction with other UI components.
 */
public class SearchCommandSystemTest extends RecipeBookSystemTest {

    private Model model = new ModelManager(getTypicalRecipeBook(), new UserPrefs());

    @Test
    public void search() {
        String query = "bot";
        assertCommandSuccess(query);

        query = "chicken rice";
        assertCommandSuccess(query);

        query = "blah";
        assertCommandSuccess(query);
    }

    /**
     * Assert that the {@code BrowserPanel} is currently loaded with given {@code url}
     */
    private void assertBrowserPanel(String url) {
        assertEquals(getBrowserPanel().getLoadedUrl().toExternalForm(), url);
    }

    /**
     * Assert that the {@code SearchCommand} was executed correctly with the given {@code query}.
     */
    private void assertCommandSuccess(String query) {
        executeCommand(SearchCommand.COMMAND_WORD + " " + query);

        WikiaQueryHandler wikiaQueryHandler = new WikiaQueryHandler(query.replaceAll("\\s+", "+"));
        int noOfResults = wikiaQueryHandler.getQueryNumberOfResults();

        if (noOfResults == 0) {
            assertApplicationDisplaysExpected("", SearchCommand.MESSAGE_NO_RESULT, model);
        } else {
            assertApplicationDisplaysExpected("", String.format(SearchCommand.MESSAGE_SUCCESS, noOfResults), model);
            assertBrowserPanel(wikiaQueryHandler.getRecipeQueryUrl());
        }
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchanged();
    }
}
```
