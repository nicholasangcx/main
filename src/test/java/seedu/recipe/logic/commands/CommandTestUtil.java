package seedu.recipe.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_CALORIES;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_COOKING_TIME;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_IMG;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_INGREDIENT;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_INSTRUCTION;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_PREPARATION_TIME;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_SERVINGS;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dropbox.core.DbxException;

import seedu.recipe.commons.core.index.Index;
import seedu.recipe.logic.CommandHistory;
import seedu.recipe.logic.UndoRedoStack;
import seedu.recipe.logic.commands.exceptions.CommandException;
import seedu.recipe.model.Model;
import seedu.recipe.model.RecipeBook;
import seedu.recipe.model.recipe.NameContainsKeywordsPredicate;
import seedu.recipe.model.recipe.Recipe;
import seedu.recipe.model.recipe.exceptions.RecipeNotFoundException;
import seedu.recipe.testutil.EditRecipeDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_INGREDIENT_AMY = "this,is,a,test";
    public static final String VALID_INGREDIENT_BOB = "some, ingredients, here";
    public static final String VALID_INSTRUCTION_AMY = "Block 312, \nAmy Street 1";
    public static final String VALID_INSTRUCTION_BOB = "Block 123, \nBobby Street 3";
    public static final String VALID_COOKING_TIME_AMY = "1111";
    public static final String VALID_COOKING_TIME_BOB = "2222";
    public static final String VALID_PREPARATION_TIME_AMY = "150h";
    public static final String VALID_PREPARATION_TIME_BOB = "200m";
    public static final String VALID_CALORIES_AMY = "11122";
    public static final String VALID_CALORIES_BOB = "5000";
    public static final String VALID_SERVINGS_AMY = "3";
    public static final String VALID_SERVINGS_BOB = "4";
    public static final String VALID_IMG_AMY = "-";
    public static final String VALID_IMG_BOB = "-";
    public static final String VALID_URL_AMY = "https://www.bbcgoodfood.com/recipes/volcano-cake";
    public static final String VALID_URL_BOB = "https://www.bbcgoodfood.com/recipes/collection/chicken-salad";
    public static final String VALID_TAG_HUSBAND = "husband";
    public static final String VALID_TAG_FRIEND = "friend";

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String INGREDIENT_DESC_AMY = " " + PREFIX_INGREDIENT + VALID_INGREDIENT_AMY;
    public static final String INGREDIENT_DESC_BOB = " " + PREFIX_INGREDIENT + VALID_INGREDIENT_BOB;
    public static final String INSTRUCTION_DESC_AMY = " " + PREFIX_INSTRUCTION + VALID_INSTRUCTION_AMY;
    public static final String INSTRUCTION_DESC_BOB = " " + PREFIX_INSTRUCTION + VALID_INSTRUCTION_BOB;
    public static final String COOKING_TIME_DESC_AMY = " " + PREFIX_COOKING_TIME + VALID_COOKING_TIME_AMY;
    public static final String COOKING_TIME_DESC_BOB = " " + PREFIX_COOKING_TIME + VALID_COOKING_TIME_BOB;
    public static final String PREPARATION_TIME_DESC_AMY = " " + PREFIX_PREPARATION_TIME + VALID_PREPARATION_TIME_AMY;
    public static final String PREPARATION_TIME_DESC_BOB = " " + PREFIX_PREPARATION_TIME + VALID_PREPARATION_TIME_BOB;
    public static final String CALORIES_DESC_AMY = " " + PREFIX_CALORIES + VALID_CALORIES_AMY;
    public static final String CALORIES_DESC_BOB = " " + PREFIX_CALORIES + VALID_CALORIES_BOB;
    public static final String SERVINGS_DESC_AMY = " " + PREFIX_SERVINGS + VALID_SERVINGS_AMY;
    public static final String SERVINGS_DESC_BOB = " " + PREFIX_SERVINGS + VALID_SERVINGS_BOB;
    public static final String URL_DESC_AMY = " " + PREFIX_URL + VALID_URL_AMY;
    public static final String URL_DESC_BOB = " " + PREFIX_URL + VALID_URL_BOB;
    public static final String IMG_DESC_AMY = " " + PREFIX_IMG + VALID_IMG_AMY;
    public static final String IMG_DESC_BOB = " " + PREFIX_IMG + VALID_IMG_BOB;
    public static final String TAG_DESC_FRIEND = " " + PREFIX_TAG + VALID_TAG_FRIEND;
    public static final String TAG_DESC_HUSBAND = " " + PREFIX_TAG + VALID_TAG_HUSBAND;

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    // 'a' not allowed in preparationTimes
    public static final String INVALID_INGREDIENT_DESC = " " + PREFIX_INGREDIENT + "bob!yahoo"; // missing '@' symbol
    // empty string not allowed for instructions
    public static final String INVALID_INSTRUCTION_DESC = " " + PREFIX_INSTRUCTION;
    public static final String INVALID_COOKING_TIME_DESC = " " + PREFIX_COOKING_TIME + "911a";
    public static final String INVALID_PREPARATION_TIME_DESC = " " + PREFIX_PREPARATION_TIME + "911a";
    public static final String INVALID_CALORIES_DESC = " " + PREFIX_CALORIES + "no lah";
    public static final String INVALID_SERVINGS_DESC = " " + PREFIX_SERVINGS + "can't do";
    // missing "https://" or "http://"
    public static final String INVALID_URL_DESC = " " + PREFIX_URL + "www.google.com";
    public static final String INVALID_IMG_DESC = " " + PREFIX_IMG + "NOT@VALID";
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "hubby*"; // '*' not allowed in tags

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";
    public static final String LF = "\n";

    public static final EditCommand.EditRecipeDescriptor DESC_AMY;
    public static final EditCommand.EditRecipeDescriptor DESC_BOB;

    static {
        DESC_AMY = new EditRecipeDescriptorBuilder()
                .withName(VALID_NAME_AMY)
                .withIngredient(VALID_INGREDIENT_AMY)
                .withInstruction(VALID_INSTRUCTION_AMY)
                .withCookingTime(VALID_COOKING_TIME_AMY)
                .withPreparationTime(VALID_PREPARATION_TIME_AMY)
                .withCalories(VALID_CALORIES_AMY)
                .withServings(VALID_SERVINGS_AMY)
                .withUrl(VALID_URL_AMY)
                .withImage(VALID_IMG_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        DESC_BOB = new EditRecipeDescriptorBuilder()
                .withName(VALID_NAME_BOB)
                .withIngredient(VALID_INGREDIENT_BOB)
                .withInstruction(VALID_INSTRUCTION_BOB)
                .withCookingTime(VALID_COOKING_TIME_BOB)
                .withPreparationTime(VALID_PREPARATION_TIME_BOB)
                .withCalories(VALID_CALORIES_BOB)
                .withServings(VALID_SERVINGS_BOB)
                .withUrl(VALID_URL_BOB)
                .withImage(VALID_IMG_BOB)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the result message matches {@code expectedMessage} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
                                            Model expectedModel) {
        try {
            CommandResult result = command.execute();
            assertEquals(expectedMessage, result.feedbackToUser);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException | DbxException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - or <br>
     * - a {@code UploadCommandException} is thrown <br>
     * - the UploadCommandException message matches {@code expectedMessage} <br>
     * - the recipe book and the filtered recipe list in the {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        RecipeBook expectedRecipeBook = new RecipeBook(actualModel.getRecipeBook());
        List<Recipe> expectedFilteredList = new ArrayList<>(actualModel.getFilteredRecipeList());

        try {
            command.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException | DbxException e) {
            assertEquals(expectedMessage, e.getMessage());
            assertEquals(expectedRecipeBook, actualModel.getRecipeBook());
            assertEquals(expectedFilteredList, actualModel.getFilteredRecipeList());
        }
    }

    /**
     * Updates {@code model}'s filtered list to show only the recipe at the given {@code targetIndex} in the
     * {@code model}'s recipe book.
     */
    public static void showRecipeAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredRecipeList().size());

        Recipe recipe = model.getFilteredRecipeList().get(targetIndex.getZeroBased());
        final String[] splitName = recipe.getName().fullName.split("\\s+");
        model.updateFilteredRecipeList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredRecipeList().size());
    }

    /**
     * Deletes the first recipe in {@code model}'s filtered list from {@code model}'s recipe book.
     */
    public static void deleteFirstRecipe(Model model) {
        Recipe firstRecipe = model.getFilteredRecipeList().get(0);
        try {
            model.deleteRecipe(firstRecipe);
        } catch (RecipeNotFoundException pnfe) {
            throw new AssertionError("Recipe in filtered list must exist in model.", pnfe);
        }
    }

    /**
     * Returns an {@code UndoCommand} with the given {@code model} and {@code undoRedoStack} set.
     */
    public static UndoCommand prepareUndoCommand(Model model, UndoRedoStack undoRedoStack) {
        UndoCommand undoCommand = new UndoCommand();
        undoCommand.setData(model, new CommandHistory(), undoRedoStack);
        return undoCommand;
    }

    /**
     * Returns a {@code RedoCommand} with the given {@code model} and {@code undoRedoStack} set.
     */
    public static RedoCommand prepareRedoCommand(Model model, UndoRedoStack undoRedoStack) {
        RedoCommand redoCommand = new RedoCommand();
        redoCommand.setData(model, new CommandHistory(), undoRedoStack);
        return redoCommand;
    }
}
