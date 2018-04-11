//@@author hoangduong1607
package systemtests;

import static seedu.recipe.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import seedu.recipe.commons.core.index.Index;
import seedu.recipe.logic.commands.GroupCommand;
import seedu.recipe.logic.commands.ViewGroupCommand;
import seedu.recipe.logic.parser.CliSyntax;
import seedu.recipe.model.Model;
import seedu.recipe.model.recipe.Recipe;

public class ViewGroupCommandSystemTest extends RecipeBookSystemTest {

    private static final String GROUP_THAT_EXISTS = "My best";
    private static final String GROUP_THAT_DOES_NOT_EXIST = "Best";
    private static final String WHITESPACE = " ";
    private static final String FIRST_INDEX = "1";
    private static final String SECOND_INDEX = "2";
    private static final List<Recipe> EMPTY_LIST = new ArrayList<>();

    @Test
    public void group() throws Exception {
        Model expectedModel = getModel();

        String groupCommand = GroupCommand.COMMAND_WORD + WHITESPACE + CliSyntax.PREFIX_GROUP_NAME + GROUP_THAT_EXISTS
                + WHITESPACE + CliSyntax.PREFIX_INDEX + FIRST_INDEX + WHITESPACE + CliSyntax.PREFIX_INDEX
                + SECOND_INDEX;
        String expectedResultMessage = String.format(GroupCommand.MESSAGE_SUCCESS, GROUP_THAT_EXISTS);
        assertCommandSuccess(groupCommand, expectedResultMessage, expectedModel);

        /* Case: view a group that exists -> show recipe(s) in the group */
        String viewGroupCommand = ViewGroupCommand.COMMAND_WORD + WHITESPACE + GROUP_THAT_EXISTS;
        expectedResultMessage = String.format(ViewGroupCommand.MESSAGE_SUCCESS, GROUP_THAT_EXISTS);
        expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel,
                expectedModel.getFilteredRecipeList()
                        .get(Index.fromOneBased(Integer.valueOf(FIRST_INDEX)).getZeroBased()),
                expectedModel.getFilteredRecipeList()
                        .get(Index.fromOneBased(Integer.valueOf(SECOND_INDEX)).getZeroBased()));
        assertCommandSuccess(viewGroupCommand, expectedResultMessage, expectedModel);


        /*Case: view a group that does not exist -> show empty list and inform user that the group does not exist */
        viewGroupCommand = ViewGroupCommand.COMMAND_WORD + WHITESPACE + GROUP_THAT_DOES_NOT_EXIST;
        expectedResultMessage = ViewGroupCommand.MESSAGE_GROUP_NOT_FOUND;
        expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, EMPTY_LIST);
        assertCommandSuccess(viewGroupCommand, expectedResultMessage, expectedModel);

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("View_Group", MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String)} except that the result box displays
     * {@code expectedResultMessage} and the model related components equal to {@code expectedModel}.
     */
    private void assertCommandSuccess(String command, String expectedResultMessage, Model expectedModel) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
    }

    /**
     * Executes {@code command} and verifies that the command box displays {@code command}, the result display
     * box displays {@code expectedResultMessage} and the model related components equal to the current model.
     * These verifications are done by
     * {@code RecipeBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the browser url, selected card and status bar remain unchanged, and the command box has the
     * error style.
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
