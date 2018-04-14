//@@author kokonguyen191
package systemtests;

import static org.junit.Assert.assertEquals;
import static seedu.recipe.testutil.TypicalIndexes.INDEX_FIRST_RECIPE;
import static seedu.recipe.testutil.TypicalIndexes.INDEX_SECOND_RECIPE;
import static seedu.recipe.testutil.TypicalIndexes.INDEX_THIRD_RECIPE;
import static seedu.recipe.testutil.TypicalRecipes.getTypicalRecipeBook;
import static seedu.recipe.testutil.TypicalRecipes.getTypicalRecipes;
import static seedu.recipe.testutil.WikiaRecipes.HAINANESE_CHICKEN_RICE;
import static seedu.recipe.testutil.WikiaRecipes.MOBILE_WIKIA_UGANDAN_ADD_COMMAND;
import static seedu.recipe.testutil.WikiaRecipes.UGANDAN_CHICKEN_STEW;
import static seedu.recipe.testutil.WikiaRecipes.WIKIA_CHICKEN_ADD_COMMAND;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import seedu.recipe.logic.commands.ParseCommand;
import seedu.recipe.logic.commands.SelectCommand;
import seedu.recipe.logic.commands.UndoCommand;
import seedu.recipe.model.Model;
import seedu.recipe.model.ModelManager;
import seedu.recipe.model.UserPrefs;
import seedu.recipe.model.recipe.Image;
import seedu.recipe.model.recipe.Recipe;
import seedu.recipe.storage.ImageDownloader;
import seedu.recipe.testutil.RecipeBuilder;

/**
 * A system test class for the parse command, which contains interaction with other UI components.
 */
public class ParseCommandSystemTest extends RecipeBookSystemTest {

    private static final String HAINANESE_CHICKEN_RICE_IMAGE_PATH =
            Image.IMAGE_STORAGE_FOLDER + "7F474E50D9E9F21A980A30B4D54308AD."
                    + ImageDownloader.DOWNLOADED_IMAGE_FORMAT;

    private Model model = new ModelManager(getTypicalRecipeBook(), new UserPrefs());
    private List<Recipe> recipes = getTypicalRecipes();

    @Test
    public void parse() {
        // First add
        String command = SelectCommand.COMMAND_WORD + " " + INDEX_FIRST_RECIPE.getOneBased();
        executeCommand(command);
        assertCommandSuccess(WIKIA_CHICKEN_ADD_COMMAND);
        getCommandBox().submitCommand();

        Recipe testRecipe = new RecipeBuilder(HAINANESE_CHICKEN_RICE).withImage(
                HAINANESE_CHICKEN_RICE_IMAGE_PATH).build();

        recipes.add(testRecipe);
        assertEquals(recipes, getModel().getFilteredRecipeList());

        // Remove recipe
        command = UndoCommand.COMMAND_WORD;
        executeCommand(command);
        recipes.remove(testRecipe);
        assertEquals(recipes, getModel().getFilteredRecipeList());

        // Then add again
        command = SelectCommand.COMMAND_WORD + " " + INDEX_FIRST_RECIPE.getOneBased();
        executeCommand(command);
        assertCommandSuccess(WIKIA_CHICKEN_ADD_COMMAND);
        getCommandBox().submitCommand();

        recipes.add(testRecipe);
        assertEquals(recipes, getModel().getFilteredRecipeList());

        // Try to parse and add another recipe
        command = SelectCommand.COMMAND_WORD + " " + INDEX_SECOND_RECIPE.getOneBased();
        executeCommand(command);
        assertCommandSuccess(MOBILE_WIKIA_UGANDAN_ADD_COMMAND);
        getCommandBox().submitCommand();
        recipes.add(UGANDAN_CHICKEN_STEW);
        assertEquals(recipes, getModel().getFilteredRecipeList());


        // Not supported site
        command = SelectCommand.COMMAND_WORD + " " + INDEX_THIRD_RECIPE.getOneBased();
        executeCommand(command);
        assertCommandSuccess("");
    }

    @After
    public void cleanUp() {
        File file = new File(HAINANESE_CHICKEN_RICE_IMAGE_PATH);
        file.delete();
    }

    /**
     * Asserts that the {@code SearchCommand} was executed correctly
     * and the current content of the CommandBox is {@code content}
     */
    private void assertCommandSuccess(String content) {
        executeCommand(ParseCommand.COMMAND_WORD);
        assertStatusBarUnchanged();
        assertCommandBoxContent(content);
    }
}
