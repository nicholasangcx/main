package seedu.recipe.logic.commands;

import com.dropbox.core.DbxException;

import seedu.recipe.commons.core.Messages;
import seedu.recipe.logic.CommandHistory;
import seedu.recipe.logic.UndoRedoStack;
import seedu.recipe.logic.commands.exceptions.CommandException;
import seedu.recipe.model.Model;

/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {
    protected Model model;
    protected CommandHistory history;
    protected UndoRedoStack undoRedoStack;

    /**
     * Constructs a feedback message to summarise an operation that displayed a listing of recipes.
     *
     * @param displaySize used to generate summary
     * @return summary message for recipes displayed
     */
    public static String getMessageForRecipeListShownSummary(int displaySize) {
        return String.format(Messages.MESSAGE_RECIPES_LISTED_OVERVIEW, displaySize);
    }

    //@@author nicholasangcx
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
    //@@author

    /**
     * Executes the command and returns the result message.
     *
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     */
    public abstract CommandResult execute() throws CommandException, DbxException;

    /**
     * Provides any needed dependencies to the command.
     * Commands making use of any of these should override this method to gain
     * access to the dependencies.
     */
    public void setData(Model model, CommandHistory history, UndoRedoStack undoRedoStack) {
        this.model = model;
    }
}
