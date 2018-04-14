# nicholasangcx
###### \java\seedu\recipe\logic\commands\AccessTokenCommandTest.java
``` java
package seedu.recipe.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.dropbox.core.DbxException;

public class AccessTokenCommandTest {

    private static final String INVALID_AUTHORIZATION_CODE = "wrong_format";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void equals() {
        String firstTest = "firstCode";
        String secondTest = "secondCode";

        AccessTokenCommand accessTokenFirstCommand = new AccessTokenCommand(firstTest);
        AccessTokenCommand accessTokenSecondCommand = new AccessTokenCommand(secondTest);

        // same object -> returns true
        assertTrue(accessTokenFirstCommand.equals(accessTokenFirstCommand));

        // same values -> returns true
        AccessTokenCommand accessTokenFirstCommandCopy = new AccessTokenCommand(firstTest);
        assertTrue(accessTokenFirstCommandCopy.equals(accessTokenFirstCommand));

        // different types -> returns false
        assertFalse(accessTokenFirstCommand.equals(1));
        assertFalse(accessTokenFirstCommand.equals(new HelpCommand()));
        assertFalse(accessTokenFirstCommand.equals("anything"));

        // null -> returns false
        assertFalse(accessTokenFirstCommand.equals(null));

        // different recipe -> returns false
        assertFalse(accessTokenFirstCommand.equals(accessTokenSecondCommand));
    }

    @Test
    public void execute_invalidAuthorizationCode() throws DbxException {
        thrown.expect(DbxException.class);
        AccessTokenCommand command = new AccessTokenCommand(INVALID_AUTHORIZATION_CODE);
        command.execute();
    }
}
```
###### \java\seedu\recipe\logic\commands\IngredientCommandTest.java
``` java
package seedu.recipe.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.recipe.commons.core.Messages.MESSAGE_RECIPES_WITH_INGREDIENTS_LISTED_OVERVIEW;
import static seedu.recipe.testutil.TypicalRecipes.BENSON;
import static seedu.recipe.testutil.TypicalRecipes.getTypicalRecipeBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.recipe.logic.CommandHistory;
import seedu.recipe.logic.UndoRedoStack;
import seedu.recipe.logic.parser.IngredientCommandParser;
import seedu.recipe.logic.parser.exceptions.ParseException;
import seedu.recipe.model.Model;
import seedu.recipe.model.ModelManager;
import seedu.recipe.model.RecipeBook;
import seedu.recipe.model.UserPrefs;
import seedu.recipe.model.recipe.IngredientContainsKeywordsPredicate;
import seedu.recipe.model.recipe.Recipe;

/**
 * Contains integration tests (interaction with the Model) for {@code IngredientCommand}.
 */
public class IngredientCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(getTypicalRecipeBook(), new UserPrefs());

    @Test
    public void equals() {
        IngredientContainsKeywordsPredicate firstPredicate =
                new IngredientContainsKeywordsPredicate(Collections.singletonList("first"));
        String[] firstArray = {"first"};
        IngredientContainsKeywordsPredicate secondPredicate =
                new IngredientContainsKeywordsPredicate(Collections.singletonList("second"));
        String[] secondArray = {"second"};

        IngredientCommand ingredientFirstCommand = new IngredientCommand(firstPredicate, firstArray);
        IngredientCommand ingredientSecondCommand = new IngredientCommand(secondPredicate, secondArray);

        // same object -> returns true
        assertTrue(ingredientFirstCommand.equals(ingredientFirstCommand));

        // same values -> returns true
        IngredientCommand ingredientFirstCommandCopy = new IngredientCommand(firstPredicate, firstArray);
        assertTrue(ingredientFirstCommand.equals(ingredientFirstCommandCopy));

        // different types -> returns false
        assertFalse(ingredientFirstCommand.equals(1));

        // null -> returns false
        assertFalse(ingredientFirstCommand == null);

        // different recipe -> returns false
        assertFalse(ingredientFirstCommand.equals(ingredientSecondCommand));
    }

    @Test
    public void executeZeroKeywordsNoRecipeFound() throws ParseException {
        thrown.expect(ParseException.class);
        String userInput = " ";
        IngredientCommand command = prepareCommand(userInput);
    }

    @Test
    public void executeMultipleKeywordsRecipesFound() throws ParseException {
        String userInput = "genuine salt";
        String[] keywords = userInput.split("\\s+");
        String expectedMessage = String.format(MESSAGE_RECIPES_WITH_INGREDIENTS_LISTED_OVERVIEW, 1,
                Arrays.toString(keywords));
        IngredientCommand command = prepareCommand(userInput);
        assertCommandSuccess(command, expectedMessage, Arrays.asList(BENSON));
    }

    /**
     * Parses {@code userInput} into a {@code IngredientCommand}.
     */
    private IngredientCommand prepareCommand(String userInput) throws ParseException {

        IngredientCommand command = new IngredientCommandParser().parse(userInput);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     *     - the command feedback is equal to {@code expectedMessage}<br>
     *     - the {@code FilteredList<Recipe>} is equal to {@code expectedList}<br>
     *     - the {@code RecipeBook} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(IngredientCommand command, String expectedMessage, List<Recipe> expectedList) {
        RecipeBook expectedRecipeBook = new RecipeBook(model.getRecipeBook());
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredRecipeList());
        assertEquals(expectedRecipeBook, model.getRecipeBook());
    }
}
```
###### \java\seedu\recipe\logic\commands\TagCommandTest.java
``` java
package seedu.recipe.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.recipe.commons.core.Messages.MESSAGE_RECIPES_WITH_TAGS_LISTED_OVERVIEW;
import static seedu.recipe.testutil.TypicalRecipes.ALICE;
import static seedu.recipe.testutil.TypicalRecipes.BENSON;
import static seedu.recipe.testutil.TypicalRecipes.CARL;
import static seedu.recipe.testutil.TypicalRecipes.getTypicalRecipeBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.recipe.logic.CommandHistory;
import seedu.recipe.logic.UndoRedoStack;
import seedu.recipe.model.Model;
import seedu.recipe.model.ModelManager;
import seedu.recipe.model.RecipeBook;
import seedu.recipe.model.UserPrefs;
import seedu.recipe.model.recipe.Recipe;
import seedu.recipe.model.tag.TagContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code TagCommand}.
 */

public class TagCommandTest {
    private Model model = new ModelManager(getTypicalRecipeBook(), new UserPrefs());

    @Test
    public void equals() {
        TagContainsKeywordsPredicate firstPredicate =
                new TagContainsKeywordsPredicate(Collections.singletonList("first"));
        String[] firstArray = {"first"};
        TagContainsKeywordsPredicate secondPredicate =
                new TagContainsKeywordsPredicate(Collections.singletonList("second"));
        String[] secondArray = {"second"};

        TagCommand tagFirstCommand = new TagCommand(firstPredicate, firstArray);
        TagCommand tagSecondCommand = new TagCommand(secondPredicate, secondArray);

        // same object -> returns true
        assertTrue(tagFirstCommand.equals(tagFirstCommand));

        // same values -> returns true
        TagCommand tagFirstCommandCopy = new TagCommand(firstPredicate, firstArray);
        assertTrue(tagFirstCommand.equals(tagFirstCommandCopy));

        // different types -> returns false
        assertFalse(tagFirstCommand.equals(1));

        // null -> returns false
        assertFalse(tagFirstCommand == null);

        // different recipe -> returns false
        assertFalse(tagFirstCommand.equals(tagSecondCommand));
    }

    @Test
    public void executeZeroKeywordsNoRecipeFound() {
        String userInput = " ";
        String[] keywords = userInput.split("\\s+");
        String expectedMessage = String.format(MESSAGE_RECIPES_WITH_TAGS_LISTED_OVERVIEW, 0,
                                                    Arrays.toString(keywords));
        TagCommand command = prepareCommand(" ");
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void executeMultipleKeywordsMultipleRecipesFound() {
        String userInput = "family owesMoney";
        String[] keywords = userInput.split("\\s+");
        String expectedMessage = String.format(MESSAGE_RECIPES_WITH_TAGS_LISTED_OVERVIEW, 3,
                                                    Arrays.toString(keywords));
        TagCommand command = prepareCommand(userInput);
        assertCommandSuccess(command, expectedMessage, Arrays.asList(ALICE, BENSON, CARL));
    }

    /**
     * Parses {@code userInput} into a {@code TagCommand}.
     */
    private TagCommand prepareCommand(String userInput) {

        TagCommand command =
                new TagCommand(new TagContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+"))),
                                        userInput.split("\\s+"));
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     *     - the command feedback is equal to {@code expectedMessage}<br>
     *     - the {@code FilteredList<Recipe>} is equal to {@code expectedList}<br>
     *     - the {@code RecipeBook} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(TagCommand command, String expectedMessage, List<Recipe> expectedList) {
        RecipeBook expectedRecipeBook = new RecipeBook(model.getRecipeBook());
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredRecipeList());
        assertEquals(expectedRecipeBook, model.getRecipeBook());
    }
}
```
###### \java\seedu\recipe\logic\commands\UploadCommandTest.java
``` java
package seedu.recipe.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.recipe.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.recipe.logic.commands.UploadCommand.MESSAGE_ACCESS_TOKEN;
import static seedu.recipe.testutil.TypicalRecipes.getTypicalRecipeBook;

import org.junit.Test;

import seedu.recipe.model.Model;
import seedu.recipe.model.ModelManager;
import seedu.recipe.model.UserPrefs;
import seedu.recipe.ui.util.CloudStorageUtil;

public class UploadCommandTest {

    private Model model = new ModelManager(getTypicalRecipeBook(), new UserPrefs());

    @Test
    public void equals() {
        String firstTest = "RecipeBook1";
        String secondTest = "Recipe_Book_1";

        UploadCommand uploadFirstCommand = new UploadCommand(firstTest);
        UploadCommand uploadSecondCommand = new UploadCommand(secondTest);

        // same object -> returns true
        assertTrue(uploadFirstCommand.equals(uploadFirstCommand));

        // same values -> returns true
        UploadCommand uploadFirstCommandCopy = new UploadCommand(firstTest);
        assertTrue(uploadFirstCommandCopy.equals(uploadFirstCommand));

        // different types -> returns false
        assertFalse(uploadFirstCommand.equals(1));
        assertFalse(uploadFirstCommand.equals(new HelpCommand()));
        assertFalse(uploadFirstCommand.equals("anything"));

        // null -> returns false
        assertFalse(uploadFirstCommand.equals(null));

        // different recipe -> returns false
        assertFalse(uploadFirstCommand.equals(uploadSecondCommand));
    }

    @Test
    public void execute_inputWithValidArgs_noAccessToken() {
        UploadCommand uploadCommand = new UploadCommand("recipebook.xml");
        CloudStorageUtil.setAccessToken(null);
        assertCommandSuccess(uploadCommand, model, MESSAGE_ACCESS_TOKEN, model);
    }
}
```
###### \java\seedu\recipe\logic\parser\AccessTokenCommandParserTest.java
``` java
package seedu.recipe.logic.parser;

import static seedu.recipe.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.recipe.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.recipe.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.recipe.logic.commands.AccessTokenCommand;

public class AccessTokenCommandParserTest {

    private static final String DUMMY_ACCESS_CODE = "valid_access_code";
    private AccessTokenCommandParser parser = new AccessTokenCommandParser();

    @Test
    public void parseEmptyArgThrowsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AccessTokenCommand.MESSAGE_USAGE));
    }

    @Test
    public void parseValidArgsReturnsUploadCommand() {
        // code taken from an actual dropbox authorization process
        AccessTokenCommand expectedAccessTokenCommand =
                new AccessTokenCommand(DUMMY_ACCESS_CODE);
        assertParseSuccess(parser, "valid_access_code", expectedAccessTokenCommand);

        // trim leading and trailing whitespaces
        assertParseSuccess(parser, "  valid_access_code  ", expectedAccessTokenCommand);
    }
}
```
###### \java\seedu\recipe\logic\parser\IngredientCommandParserTest.java
``` java
package seedu.recipe.logic.parser;

import static seedu.recipe.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.recipe.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.recipe.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.Test;

import seedu.recipe.logic.commands.IngredientCommand;
import seedu.recipe.model.recipe.IngredientContainsKeywordsPredicate;

public class IngredientCommandParserTest {

    private IngredientCommandParser parser = new IngredientCommandParser();

    @Test
    public void parseEmptyArgThrowsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                IngredientCommand.MESSAGE_USAGE));
    }

    @Test
    public void parseValidArgsReturnsIngredientCommand() {
        // no leading and trailing whitespaces
        IngredientCommand expectedIngredientCommand =
                new IngredientCommand(new IngredientContainsKeywordsPredicate(Arrays.asList("chicken", "rice")),
                        new String[] {"chicken", "rice"});
        assertParseSuccess(parser, "chicken rice", expectedIngredientCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n chicken \n \t rice  \t", expectedIngredientCommand);
    }
}
```
###### \java\seedu\recipe\logic\parser\ParserUtilTest.java
``` java
    @Test
    public void parseFilename_null_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        ParserUtil.parseFilename(null);
    }

    @Test
    public void parseFilename_invalidValue_throwsIllegalValueException() throws Exception {
        thrown.expect(IllegalValueException.class);
        ParserUtil.parseFilename(INVALID_FILENAME);
    }

    @Test
    public void parseFilename_validValueWithoutWhitespace_returnsString() throws Exception {
        String expectedFilename = VALID_FILENAME_1 + ".xml";
        assertEquals(expectedFilename, ParserUtil.parseFilename(VALID_FILENAME_1));
    }
```
###### \java\seedu\recipe\logic\parser\RecipeBookParserTest.java
``` java
    @Test
    public void parseCommand_tag() throws Exception {
        List<String> keywords = Arrays.asList("friends", "family", "owesMoney");
        TagCommand command = (TagCommand) parser.parseCommand(
                TagCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new TagCommand(new TagContainsKeywordsPredicate(keywords),
                keywords.toArray(new String[0])), command);
    }

    @Test
    public void parseCommand_upload() throws Exception {
        String filename = "RecipeBook";
        UploadCommand command = (UploadCommand) parser.parseCommand(
                UploadCommand.COMMAND_WORD + " " + filename);
        assertEquals(new UploadCommand(filename + ".xml"), command);
    }
```
###### \java\seedu\recipe\logic\parser\TagCommandParserTest.java
``` java
package seedu.recipe.logic.parser;

import static seedu.recipe.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.recipe.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.recipe.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.Test;

import seedu.recipe.logic.commands.TagCommand;
import seedu.recipe.model.tag.TagContainsKeywordsPredicate;

public class TagCommandParserTest {

    private TagCommandParser parser = new TagCommandParser();

    @Test
    public void parseEmptyArgThrowsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parseValidArgsReturnsTagCommand() {
        // no leading and trailing whitespaces
        TagCommand expectedTagCommand =
                new TagCommand(new TagContainsKeywordsPredicate(Arrays.asList("friends", "owesMoney")),
                                    new String[] {"friends", "owesMoney"});
        assertParseSuccess(parser, "friends owesMoney", expectedTagCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n friends \n \t owesMoney  \t", expectedTagCommand);
    }
}
```
###### \java\seedu\recipe\logic\parser\UploadCommandParserTest.java
``` java
package seedu.recipe.logic.parser;

import static seedu.recipe.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.recipe.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.recipe.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.recipe.logic.commands.UploadCommand;
import seedu.recipe.model.file.Filename;

public class UploadCommandParserTest {

    private UploadCommandParser parser = new UploadCommandParser();

    @Test
    public void parseEmptyArgThrowsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, UploadCommand.MESSAGE_USAGE));
    }

    @Test
    public void parseValidArgsReturnsUploadCommand() {
        // no leading and trailing whitespaces
        UploadCommand expectedUploadCommand =
                new UploadCommand("RecipeBook.xml");
        assertParseSuccess(parser, "RecipeBook", expectedUploadCommand);

        // ignores subsequent keywords after the first
        assertParseSuccess(parser, " \n RecipeBook \n \t otherBook \t", expectedUploadCommand);
    }

    @Test
    public void parseInvalidArgsThrowsParseException() {
        assertParseFailure(parser, "recipe/book", Filename.MESSAGE_FILENAME_CONSTRAINTS);
    }
}
```
###### \java\seedu\recipe\model\file\FilenameTest.java
``` java
package seedu.recipe.model.file;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.recipe.testutil.Assert;

public class FilenameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Filename(null));
    }

    @Test
    public void constructor_invalidFilename_throwsIllegalArgumentException() {
        String invalidFilename = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Filename(invalidFilename));
    }

    @Test
    public void isValidFilename() {
        // null Filename
        Assert.assertThrows(NullPointerException.class, () -> Filename.isValidFilename(null));

        // blank Filename
        assertFalse(Filename.isValidFilename("")); // empty string
        assertFalse(Filename.isValidFilename(" ")); // spaces only

        // invalid Filename
        assertFalse(Filename.isValidFilename("test.Filename")); // invalid character .
        assertFalse(Filename.isValidFilename("test|test")); // invalid character |
        assertFalse(Filename.isValidFilename("test/filename")); // invalid character /

        // valid Filename
        assertTrue(Filename.isValidFilename("Recipe2Book")); // alphanumeric filename
        assertTrue(Filename.isValidFilename("RecipeBook(1)")); // valid characters ()
        assertTrue(Filename.isValidFilename("Recipe_Book")); // valid character _
    }
}
```
###### \java\seedu\recipe\model\recipe\IngredientContainsKeywordsPredicateTest.java
``` java
package seedu.recipe.model.recipe;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.recipe.testutil.RecipeBuilder;

public class IngredientContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        IngredientContainsKeywordsPredicate firstPredicate =
                new IngredientContainsKeywordsPredicate(firstPredicateKeywordList);
        IngredientContainsKeywordsPredicate secondPredicate =
                new IngredientContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        IngredientContainsKeywordsPredicate firstPredicateCopy =
                new IngredientContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate == null);

        // different recipe -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void testIngredientContainsKeywordsReturnsTrue() {
        // One keyword
        IngredientContainsKeywordsPredicate predicate =
                new IngredientContainsKeywordsPredicate(Collections.singletonList("chicken"));
        assertTrue(predicate.test(new RecipeBuilder().withIngredient("chicken, rice").build()));

        // Multiple keywords
        predicate = new IngredientContainsKeywordsPredicate(Arrays.asList("chicken", "rice"));
        assertTrue(predicate.test(new RecipeBuilder().withIngredient("chicken, rice").build()));

    }

    @Test
    public void testIngredientDoesNotContainKeywordsReturnsFalse() {
        // Non-matching keyword
        IngredientContainsKeywordsPredicate predicate = new IngredientContainsKeywordsPredicate(Arrays.asList("food"));
        assertFalse(predicate.test(new RecipeBuilder().build()));

        // Only matches one keyword
        predicate = new IngredientContainsKeywordsPredicate(Arrays.asList("chicken", "rice"));
        assertFalse(predicate.test(new RecipeBuilder().withIngredient("chicken").build()));

        // Keywords match phone, email, name and address, but does not match Ingredient
        predicate = new IngredientContainsKeywordsPredicate(Arrays.asList
                ("Food", "12345", "fish", "egg", "Main", "Street"));
        assertFalse(predicate.test(new RecipeBuilder().withName("Food").withPreparationTime("12345")
                .withIngredient("chicken, rice").withInstruction("Main Street").build()));
    }
}
```
###### \java\seedu\recipe\model\tag\TagContainsKeywordsPredicateTest.java
``` java
package seedu.recipe.model.tag;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.recipe.testutil.RecipeBuilder;

public class TagContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        TagContainsKeywordsPredicate firstPredicate = new TagContainsKeywordsPredicate(firstPredicateKeywordList);
        TagContainsKeywordsPredicate secondPredicate = new TagContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        TagContainsKeywordsPredicate firstPredicateCopy = new TagContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate == null);

        // different recipe -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void testTagContainsKeywordsReturnsTrue() {
        // One keyword
        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate(Collections.singletonList("friends"));
        assertTrue(predicate.test(new RecipeBuilder().build()));

        // Multiple keywords
        predicate = new TagContainsKeywordsPredicate(Arrays.asList("friends", "food"));
        assertTrue(predicate.test(new RecipeBuilder().withTags("friends", "food").build()));

        // Only one matching keyword
        predicate = new TagContainsKeywordsPredicate(Arrays.asList("friends", "food"));
        assertTrue(predicate.test(new RecipeBuilder().build()));
    }

    @Test
    public void testTagDoesNotContainKeywordsReturnsFalse() {
        // Zero keywords
        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new RecipeBuilder().build()));

        // Non-matching keyword
        predicate = new TagContainsKeywordsPredicate(Arrays.asList("food"));
        assertFalse(predicate.test(new RecipeBuilder().build()));

        // Keywords match phone, email, name and address, but does not match tag
        predicate = new TagContainsKeywordsPredicate(Arrays.asList
                            ("Food", "12345", "fish", "egg", "Main", "Street"));
        assertFalse(predicate.test(new RecipeBuilder().withName("Food").withPreparationTime("12345")
                .withIngredient("fish, egg").withInstruction("Main Street").build()));
    }
}
```
###### \java\seedu\recipe\ui\testutil\CloudStorageUtilTest.java
``` java
package seedu.recipe.ui.testutil;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.dropbox.core.DbxException;

import seedu.recipe.ui.util.CloudStorageUtil;

public class CloudStorageUtilTest {

    private static final String WRONG_AUTHORIZATION_CODE = "abcdefg";
    private static final String ACCESS_TOKEN_STUB = "adjhsj";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void hasAccessToken() {
        CloudStorageUtil.setAccessToken(null);
        assertFalse(CloudStorageUtil.hasAccessToken());

        CloudStorageUtil.setAccessToken(ACCESS_TOKEN_STUB);
        assertTrue(CloudStorageUtil.hasAccessToken());
    }

    @Test
    public void processInvalidAuthorizationCode() throws DbxException {
        thrown.expect(DbxException.class);
        CloudStorageUtil.processAuthorizationCode(WRONG_AUTHORIZATION_CODE);
        assertFalse(CloudStorageUtil.hasAccessToken());
    }
}
```
