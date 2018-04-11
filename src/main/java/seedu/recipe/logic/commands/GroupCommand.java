//@@author hoangduong1607
package seedu.recipe.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_GROUP_NAME;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_INDEX;

import java.util.List;
import java.util.Set;

import seedu.recipe.commons.core.Messages;
import seedu.recipe.commons.core.index.Index;
import seedu.recipe.logic.commands.exceptions.CommandException;
import seedu.recipe.model.recipe.GroupName;
import seedu.recipe.model.recipe.Recipe;

/**
 * Groups selected recipes.
 */
public class GroupCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "group";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Groups the recipes identified by the indices numbers used in the last recipe listing.\n"
            + "Parameters: "
            + PREFIX_GROUP_NAME + "GROUP_NAME "
            + PREFIX_INDEX + "INDEX "
            + "[" + PREFIX_INDEX + "INDEX] (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_GROUP_NAME + "Best "
            + PREFIX_INDEX + "1 "
            + PREFIX_INDEX + "3 ";
    public static final String MESSAGE_SUCCESS = "Created New Recipe Group: %s";

    private GroupName groupName;
    private Set<Index> targetIndices;

    public GroupCommand(GroupName groupName, Set<Index> targetIndices) {
        requireNonNull(groupName);
        requireNonNull(targetIndices);
        this.targetIndices = targetIndices;
        this.groupName = groupName;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        List<Recipe> lastShownList = model.getFilteredRecipeList();

        for (Index index : targetIndices) {
            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_RECIPE_DISPLAYED_INDEX);
            }
            Recipe recipe = model.getFilteredRecipeList().get(index.getZeroBased());
            recipe.addNewGroup(groupName);
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, groupName));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof GroupCommand // instanceof handles nulls
                && this.targetIndices.equals(((GroupCommand) other).targetIndices)
                && this.groupName.equals(((GroupCommand) other).groupName)); // state check
    }
}
