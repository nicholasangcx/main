package systemtests;

import static seedu.recipe.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.recipe.logic.commands.CommandTestUtil.CALORIES_DESC_AMY;
import static seedu.recipe.logic.commands.CommandTestUtil.CALORIES_DESC_BOB;
import static seedu.recipe.logic.commands.CommandTestUtil.COOKING_TIME_DESC_AMY;
import static seedu.recipe.logic.commands.CommandTestUtil.COOKING_TIME_DESC_BOB;
import static seedu.recipe.logic.commands.CommandTestUtil.IMG_DESC_AMY;
import static seedu.recipe.logic.commands.CommandTestUtil.IMG_DESC_BOB;
import static seedu.recipe.logic.commands.CommandTestUtil.INGREDIENT_DESC_AMY;
import static seedu.recipe.logic.commands.CommandTestUtil.INGREDIENT_DESC_BOB;
import static seedu.recipe.logic.commands.CommandTestUtil.INSTRUCTION_DESC_AMY;
import static seedu.recipe.logic.commands.CommandTestUtil.INSTRUCTION_DESC_BOB;
import static seedu.recipe.logic.commands.CommandTestUtil.INVALID_IMG_DESC;
import static seedu.recipe.logic.commands.CommandTestUtil.INVALID_INGREDIENT_DESC;
import static seedu.recipe.logic.commands.CommandTestUtil.INVALID_INSTRUCTION_DESC;
import static seedu.recipe.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.recipe.logic.commands.CommandTestUtil.INVALID_PREPARATION_TIME_DESC;
import static seedu.recipe.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.recipe.logic.commands.CommandTestUtil.INVALID_URL_DESC;
import static seedu.recipe.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.recipe.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.recipe.logic.commands.CommandTestUtil.PREPARATION_TIME_DESC_AMY;
import static seedu.recipe.logic.commands.CommandTestUtil.PREPARATION_TIME_DESC_BOB;
import static seedu.recipe.logic.commands.CommandTestUtil.SERVINGS_DESC_AMY;
import static seedu.recipe.logic.commands.CommandTestUtil.SERVINGS_DESC_BOB;
import static seedu.recipe.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.recipe.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.recipe.logic.commands.CommandTestUtil.URL_DESC_AMY;
import static seedu.recipe.logic.commands.CommandTestUtil.URL_DESC_BOB;
import static seedu.recipe.logic.commands.CommandTestUtil.VALID_CALORIES_AMY;
import static seedu.recipe.logic.commands.CommandTestUtil.VALID_COOKING_TIME_AMY;
import static seedu.recipe.logic.commands.CommandTestUtil.VALID_IMG_AMY;
import static seedu.recipe.logic.commands.CommandTestUtil.VALID_INGREDIENT_AMY;
import static seedu.recipe.logic.commands.CommandTestUtil.VALID_INGREDIENT_BOB;
import static seedu.recipe.logic.commands.CommandTestUtil.VALID_INSTRUCTION_AMY;
import static seedu.recipe.logic.commands.CommandTestUtil.VALID_INSTRUCTION_BOB;
import static seedu.recipe.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.recipe.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.recipe.logic.commands.CommandTestUtil.VALID_PREPARATION_TIME_AMY;
import static seedu.recipe.logic.commands.CommandTestUtil.VALID_SERVINGS_AMY;
import static seedu.recipe.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.recipe.logic.commands.CommandTestUtil.VALID_URL_AMY;
import static seedu.recipe.logic.commands.CommandTestUtil.VALID_URL_BOB;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.recipe.testutil.TypicalRecipes.ALICE;
import static seedu.recipe.testutil.TypicalRecipes.AMY;
import static seedu.recipe.testutil.TypicalRecipes.BOB;
import static seedu.recipe.testutil.TypicalRecipes.CARL;
import static seedu.recipe.testutil.TypicalRecipes.HOON;
import static seedu.recipe.testutil.TypicalRecipes.IDA;
import static seedu.recipe.testutil.TypicalRecipes.KEYWORD_MATCHING_MEIER;

import org.junit.Test;

import seedu.recipe.commons.core.Messages;
import seedu.recipe.commons.core.index.Index;
import seedu.recipe.logic.commands.AddCommand;
import seedu.recipe.logic.commands.RedoCommand;
import seedu.recipe.logic.commands.UndoCommand;
import seedu.recipe.model.Model;
import seedu.recipe.model.recipe.Image;
import seedu.recipe.model.recipe.Ingredient;
import seedu.recipe.model.recipe.Instruction;
import seedu.recipe.model.recipe.Name;
import seedu.recipe.model.recipe.PreparationTime;
import seedu.recipe.model.recipe.Recipe;
import seedu.recipe.model.recipe.Url;
import seedu.recipe.model.recipe.exceptions.DuplicateRecipeException;
import seedu.recipe.model.tag.Tag;
import seedu.recipe.testutil.RecipeBuilder;
import seedu.recipe.testutil.RecipeUtil;

public class AddCommandSystemTest extends RecipeBookSystemTest {

    @Test
    public void add() throws Exception {
        Model model = getModel();

        /* ------------------------ Perform add operations on the shown unfiltered list ----------------------------- */

        /* Case: add a recipe without tags to a non-empty recipe book, command with leading spaces and trailing spaces
         * -> added
         */
        Recipe toAdd = AMY;
        String command = "   " + AddCommand.COMMAND_WORD + "  " + NAME_DESC_AMY + "  " + INGREDIENT_DESC_AMY + " "
                + INSTRUCTION_DESC_AMY + "   " + PREPARATION_TIME_DESC_AMY + "   " + COOKING_TIME_DESC_AMY + "   "
                + CALORIES_DESC_AMY + "   " + SERVINGS_DESC_AMY + "   " + URL_DESC_AMY + "   "
                + IMG_DESC_AMY + "   " + TAG_DESC_FRIEND + " ";
        assertCommandSuccess(command, toAdd);

        /* Case: undo adding Amy to the list -> Amy deleted */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo adding Amy to the list -> Amy added again */
        command = RedoCommand.COMMAND_WORD;
        model.addRecipe(toAdd);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: add a recipe with all fields same as another recipe in the recipe book except name -> added */
        toAdd = new RecipeBuilder()
                .withName(VALID_NAME_BOB)
                .withIngredient(VALID_INGREDIENT_AMY)
                .withInstruction(VALID_INSTRUCTION_AMY)
                .withCookingTime(VALID_COOKING_TIME_AMY)
                .withPreparationTime(VALID_PREPARATION_TIME_AMY)
                .withCalories(VALID_CALORIES_AMY)
                .withServings(VALID_SERVINGS_AMY)
                .withUrl(VALID_URL_AMY)
                .withImage(VALID_IMG_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        command = AddCommand.COMMAND_WORD
                + NAME_DESC_BOB
                + INGREDIENT_DESC_AMY
                + INSTRUCTION_DESC_AMY
                + COOKING_TIME_DESC_AMY
                + PREPARATION_TIME_DESC_AMY
                + CALORIES_DESC_AMY
                + SERVINGS_DESC_AMY
                + URL_DESC_AMY
                + IMG_DESC_AMY
                + TAG_DESC_FRIEND;
        assertCommandSuccess(command, toAdd);

        /* Case: add a recipe with all fields same as another recipe in the recipe book except ingredient -> added */
        toAdd = new RecipeBuilder()
                .withName(VALID_NAME_AMY)
                .withIngredient(VALID_INGREDIENT_BOB)
                .withInstruction(VALID_INSTRUCTION_AMY)
                .withCookingTime(VALID_COOKING_TIME_AMY)
                .withPreparationTime(VALID_PREPARATION_TIME_AMY)
                .withCalories(VALID_CALORIES_AMY)
                .withServings(VALID_SERVINGS_AMY)
                .withUrl(VALID_URL_AMY)
                .withImage(VALID_IMG_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        command = AddCommand.COMMAND_WORD
                + NAME_DESC_AMY
                + INGREDIENT_DESC_BOB
                + INSTRUCTION_DESC_AMY
                + COOKING_TIME_DESC_AMY
                + PREPARATION_TIME_DESC_AMY
                + CALORIES_DESC_AMY
                + SERVINGS_DESC_AMY
                + URL_DESC_AMY
                + IMG_DESC_AMY
                + TAG_DESC_FRIEND;
        assertCommandSuccess(command, toAdd);

        /* Case: add a recipe with all fields same as another recipe in the recipe book except inst -> added */
        toAdd = new RecipeBuilder()
                .withName(VALID_NAME_AMY)
                .withIngredient(VALID_INGREDIENT_AMY)
                .withInstruction(VALID_INSTRUCTION_BOB)
                .withCookingTime(VALID_COOKING_TIME_AMY)
                .withPreparationTime(VALID_PREPARATION_TIME_AMY)
                .withCalories(VALID_CALORIES_AMY)
                .withServings(VALID_SERVINGS_AMY)
                .withUrl(VALID_URL_AMY)
                .withImage(VALID_IMG_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        command = AddCommand.COMMAND_WORD
                + NAME_DESC_AMY
                + INGREDIENT_DESC_AMY
                + INSTRUCTION_DESC_BOB
                + COOKING_TIME_DESC_AMY
                + PREPARATION_TIME_DESC_AMY
                + CALORIES_DESC_AMY
                + SERVINGS_DESC_AMY
                + URL_DESC_AMY
                + IMG_DESC_AMY
                + TAG_DESC_FRIEND;
        assertCommandSuccess(command, toAdd);


        /* Case: add a recipe with all fields same as another recipe in the recipe book except URL -> added */
        toAdd = new RecipeBuilder()
                .withName(VALID_NAME_AMY)
                .withIngredient(VALID_INGREDIENT_AMY)
                .withInstruction(VALID_INSTRUCTION_AMY)
                .withCookingTime(VALID_COOKING_TIME_AMY)
                .withPreparationTime(VALID_PREPARATION_TIME_AMY)
                .withCalories(VALID_CALORIES_AMY)
                .withServings(VALID_SERVINGS_AMY)
                .withUrl(VALID_URL_BOB)
                .withImage(VALID_IMG_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        command = AddCommand.COMMAND_WORD
                + NAME_DESC_AMY
                + INGREDIENT_DESC_AMY
                + INSTRUCTION_DESC_AMY
                + COOKING_TIME_DESC_AMY
                + PREPARATION_TIME_DESC_AMY
                + CALORIES_DESC_AMY
                + SERVINGS_DESC_AMY
                + URL_DESC_BOB
                + IMG_DESC_AMY
                + TAG_DESC_FRIEND;
        assertCommandSuccess(command, toAdd);

        /* Case: add to empty recipe book -> added */
        deleteAllRecipes();
        assertCommandSuccess(ALICE);

        /* Case: add a recipe with tags, command with parameters in random order -> added */
        toAdd = BOB;
        command = AddCommand.COMMAND_WORD + TAG_DESC_FRIEND + URL_DESC_BOB + PREPARATION_TIME_DESC_BOB
                + COOKING_TIME_DESC_BOB + CALORIES_DESC_BOB + SERVINGS_DESC_BOB + IMG_DESC_BOB
                + INSTRUCTION_DESC_BOB + NAME_DESC_BOB + TAG_DESC_HUSBAND + INGREDIENT_DESC_BOB;
        assertCommandSuccess(command, toAdd);

        /* Case: add a recipe, missing tags -> added */
        assertCommandSuccess(HOON);

        /* -------------------------- Perform add operation on the shown filtered list ------------------------------ */

        /* Case: filters the recipe list before adding -> added */
        showRecipesWithName(KEYWORD_MATCHING_MEIER);
        assertCommandSuccess(IDA);

        /* ------------------------ Perform add operation while a recipe card is selected --------------------------- */

        /* Case: selects first card in the recipe list, add a recipe -> added, card selection remains unchanged */
        selectRecipe(Index.fromOneBased(1));
        assertCommandSuccess(CARL);

        /* ----------------------------------- Perform invalid add operations --------------------------------------- */

        /* Case: add a duplicate recipe -> rejected */
        command = RecipeUtil.getAddCommand(HOON);
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_RECIPE);

        /* Case: add a duplicate recipe except with different tags -> rejected */
        // "friends" is an existing tag used in the default model, see TypicalRecipes#ALICE
        // This test will fail if a new tag that is not in the model is used, see the bug documented in
        // RecipeBook#addRecipe(Recipe)
        command = RecipeUtil.getAddCommand(HOON) + " " + PREFIX_TAG.getPrefix() + "friends";
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_RECIPE);

        /* Case: missing name -> rejected */
        command = AddCommand.COMMAND_WORD + PREPARATION_TIME_DESC_AMY + INGREDIENT_DESC_AMY + INSTRUCTION_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: invalid keyword -> rejected */
        command = "adds " + RecipeUtil.getRecipeDetails(toAdd);
        assertCommandFailure(command, Messages.MESSAGE_UNKNOWN_COMMAND);

        /* Case: invalid name -> rejected */
        command = AddCommand.COMMAND_WORD + INVALID_NAME_DESC + PREPARATION_TIME_DESC_AMY + INGREDIENT_DESC_AMY
                + INSTRUCTION_DESC_AMY;
        assertCommandFailure(command, Name.MESSAGE_NAME_CONSTRAINTS);

        /* Case: invalid preparation time -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + INVALID_PREPARATION_TIME_DESC + INGREDIENT_DESC_AMY
                + INSTRUCTION_DESC_AMY;
        assertCommandFailure(command, PreparationTime.MESSAGE_PREPARATION_TIME_CONSTRAINTS);

        /* Case: invalid ingredient -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PREPARATION_TIME_DESC_AMY + INVALID_INGREDIENT_DESC
                + INSTRUCTION_DESC_AMY;
        assertCommandFailure(command, Ingredient.MESSAGE_INGREDIENT_CONSTRAINTS);

        /* Case: invalid recipe -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PREPARATION_TIME_DESC_AMY + INGREDIENT_DESC_AMY
                + INVALID_INSTRUCTION_DESC;
        assertCommandFailure(command, Instruction.MESSAGE_INSTRUCTION_CONSTRAINTS);

        /* Case: invalid url -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PREPARATION_TIME_DESC_AMY + INGREDIENT_DESC_AMY
                + INSTRUCTION_DESC_AMY + INVALID_URL_DESC;
        assertCommandFailure(command, Url.MESSAGE_URL_CONSTRAINTS);

        /* Case: invalid image -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PREPARATION_TIME_DESC_AMY + INGREDIENT_DESC_AMY
                + INSTRUCTION_DESC_AMY + INVALID_IMG_DESC;
        assertCommandFailure(command, Image.MESSAGE_IMAGE_CONSTRAINTS);

        /* Case: invalid tag -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PREPARATION_TIME_DESC_AMY + INGREDIENT_DESC_AMY
                + INSTRUCTION_DESC_AMY + INVALID_TAG_DESC;
        assertCommandFailure(command, Tag.MESSAGE_TAG_CONSTRAINTS);
    }

    /**
     * Executes the {@code AddCommand} that adds {@code toAdd} to the model and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing {@code AddCommand} with the details of
     * {@code toAdd}.<br>
     * 4. {@code Model}, {@code Storage} and {@code RecipeListPanel} equal to the corresponding components in
     * the current model added with {@code toAdd}.<br>
     * 5. Browser url and selected card remain unchanged.<br>
     * 6. Status bar's sync status changes.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code RecipeBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     *
     * @see RecipeBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(Recipe toAdd) {
        assertCommandSuccess(RecipeUtil.getAddCommand(toAdd), toAdd);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(Recipe)}. Executes {@code command}
     * instead.
     *
     * @see AddCommandSystemTest#assertCommandSuccess(Recipe)
     */
    private void assertCommandSuccess(String command, Recipe toAdd) {
        Model expectedModel = getModel();
        try {
            expectedModel.addRecipe(toAdd);
        } catch (DuplicateRecipeException dpe) {
            throw new IllegalArgumentException("toAdd already exists in the model.");
        }
        String expectedResultMessage = String.format(AddCommand.MESSAGE_SUCCESS, toAdd);

        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Recipe)} except asserts that
     * the,<br>
     * 1. Result display box displays {@code expectedResultMessage}.<br>
     * 2. {@code Model}, {@code Storage} and {@code RecipeListPanel} equal to the corresponding components in
     * {@code expectedModel}.<br>
     *
     * @see AddCommandSystemTest#assertCommandSuccess(String, Recipe)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays {@code command}.<br>
     * 2. Command box has the error style class.<br>
     * 3. Result display box displays {@code expectedResultMessage}.<br>
     * 4. {@code Model}, {@code Storage} and {@code RecipeListPanel} remain unchanged.<br>
     * 5. Browser url, selected card and status bar remain unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code RecipeBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     *
     * @see RecipeBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
