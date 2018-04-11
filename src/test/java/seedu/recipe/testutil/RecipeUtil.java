package seedu.recipe.testutil;

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

import seedu.recipe.logic.commands.AddCommand;
import seedu.recipe.model.recipe.Recipe;

/**
 * A utility class for Recipe.
 */
public class RecipeUtil {

    /**
     * Returns an add command string for adding the {@code recipe}.
     */
    public static String getAddCommand(Recipe recipe) {
        return AddCommand.COMMAND_WORD + " " + getRecipeDetails(recipe);
    }

    /**
     * Returns the part of command string for the given {@code recipe}'s details.
     */
    public static String getRecipeDetails(Recipe recipe) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + recipe.getName().fullName + " ");
        sb.append(PREFIX_INGREDIENT + recipe.getIngredient().value + " ");
        sb.append(PREFIX_INSTRUCTION + recipe.getInstruction().value + " ");
        sb.append(PREFIX_COOKING_TIME + recipe.getCookingTime().value + " ");
        sb.append(PREFIX_PREPARATION_TIME + recipe.getPreparationTime().value + " ");
        sb.append(PREFIX_CALORIES + recipe.getCalories().value + " ");
        sb.append(PREFIX_SERVINGS + recipe.getServings().value + " ");
        sb.append(PREFIX_URL + recipe.getUrl().value + " ");
        sb.append(PREFIX_IMG + recipe.getImage().toString() + " ");
        recipe.getTags().stream().forEach(
            s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        return sb.toString();
    }
}
