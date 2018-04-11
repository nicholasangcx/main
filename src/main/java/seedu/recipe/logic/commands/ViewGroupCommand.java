//@@author hoangduong1607
package seedu.recipe.logic.commands;

import seedu.recipe.model.recipe.GroupName;
import seedu.recipe.model.recipe.GroupPredicate;

/**
 * Lists all recipes in a group to the user.
 */
public class ViewGroupCommand extends Command {

    public static final String COMMAND_WORD = "view_group";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Views recipes in a group.\n"
            + "Parameters: GROUP_NAME\n"
            + "Example: " + COMMAND_WORD + " Best";
    public static final String MESSAGE_GROUP_NOT_FOUND = "Recipe group not found!";
    public static final String MESSAGE_SUCCESS = "Listed all recipes in [%s]";

    private GroupPredicate groupPredicate;
    private GroupName groupName;

    public ViewGroupCommand(GroupPredicate groupPredicate, GroupName groupName) {
        this.groupPredicate = groupPredicate;
        this.groupName = groupName;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredRecipeList(groupPredicate);

        String commandResult;
        if (model.getFilteredRecipeList().size() > 0) {
            commandResult = String.format(MESSAGE_SUCCESS, groupName);
        } else {
            commandResult = MESSAGE_GROUP_NOT_FOUND;
        }

        return new CommandResult(commandResult);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ViewGroupCommand // instanceof handles nulls
                && this.groupName.equals(((ViewGroupCommand) other).groupName)); // state check
    }
}
