# hoangduong1607
###### \java\guitests\guihandles\CommandBoxHandle.java
``` java

    /**
     * Inserts the given string to text at current caret position
     */
    public void insertText(String text) {
        int caretPosition = getRootNode().getCaretPosition();
        guiRobot.interact(() -> getRootNode().insertText(caretPosition, text));
        guiRobot.pauseForHuman();
    }

```
###### \java\seedu\recipe\logic\parser\ViewGroupCommandParserTest.java
``` java
package seedu.recipe.logic.parser;

import static seedu.recipe.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.recipe.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.recipe.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.recipe.logic.commands.ViewGroupCommand;
import seedu.recipe.model.recipe.GroupName;
import seedu.recipe.model.recipe.GroupPredicate;

public class ViewGroupCommandParserTest {

    private ViewGroupCommandParser parser = new ViewGroupCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ViewGroupCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nonEmptyArgs_success() {
        String groupNameString = "My best";
        GroupName groupName = new GroupName(groupNameString);
        ViewGroupCommand expectedViewGroupCommand = new ViewGroupCommand(new GroupPredicate(groupName), groupName);
        assertParseSuccess(parser, groupNameString, expectedViewGroupCommand);
    }

}
```
###### \java\seedu\recipe\ui\CommandBoxTest.java
``` java
    @Test
    public void commandBox_handleShowingSuggestions() {
        commandBoxHandle.insertText(ADD_COMMAND);
        commandBoxHandle.insertText(WHITESPACE);
        commandBoxHandle.insertText(String.valueOf(PREFIX_NAME.charAt(0)));
        guiRobot.push(KeyboardShortcutsMapping.SHOW_SUGGESTIONS_COMMAND);
        guiRobot.push(KeyCode.DOWN);
        guiRobot.push(KeyCode.ENTER);
        assertInput(ADD_COMMAND_WITH_PREFIX_NAME);

        commandBoxHandle.insertText(WHITESPACE);
        guiRobot.push(KeyboardShortcutsMapping.SHOW_SUGGESTIONS_COMMAND);
        guiRobot.push(KeyCode.DOWN);
        guiRobot.push(KeyCode.DOWN);
        guiRobot.push(KeyCode.ENTER);
        assertInput(ADD_COMMAND_WITH_PREFIX_NAME + WHITESPACE + SECOND_FIELD_OF_ADD_COMMAND);
    }

    @Test
    public void commandBox_handleAutoCompletion() {
        guiRobot.push(KeyboardShortcutsMapping.SHOW_SUGGESTIONS_COMMAND);
        guiRobot.push(KeyCode.DOWN);
        guiRobot.push(KeyCode.ENTER);
        assertInput(AUTO_COMPLETION_FOR_ADD_COMMAND);

        guiRobot.push(KeyboardShortcutsMapping.NEXT_FIELD);
        commandBoxHandle.insertText(RECIPE_NAME);
        assertInput(AUTO_COMPLETION_FOR_ADD_COMMAND_WITH_RECIPE_NAME);

        guiRobot.push(KeyboardShortcutsMapping.PREV_FIELD);
        guiRobot.push(KeyboardShortcutsMapping.PREV_FIELD);
        commandBoxHandle.insertText(TAG);
        assertInput(AUTO_COMPLETION_FOR_ADD_COMMAND_WITH_RECIPE_NAME_AND_TAG);
    }

```
###### \java\seedu\recipe\ui\testutil\TextInputProcessorUtilTest.java
``` java
package seedu.recipe.ui.testutil;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import seedu.recipe.ui.util.TextInputProcessorUtil;

public class TextInputProcessorUtilTest {

    private static final char WHITESPACE = ' ';
    private static final char LF = '\n';
    private static final String EMPTY_STRING = "";

    private static final String FIRST_WORD = "HELLO";
    private static final String SECOND_WORD = "WORLD";
    private static final String THIRD_WORD = "MY";
    private static final String FOURTH_WORD = "NAME";
    private static final String FIFTH_WORD = "IS";
    private static final String FIRST_SINGLE_LINE_SENTENCE = FIRST_WORD + WHITESPACE + SECOND_WORD;
    private static final String SECOND_SINGLE_LINE_SENTENCE = WHITESPACE + THIRD_WORD + WHITESPACE + FOURTH_WORD
            + WHITESPACE + FIFTH_WORD + WHITESPACE;
    private static final String MULTIPLE_LINES_SENTENCE = FIRST_SINGLE_LINE_SENTENCE + LF + SECOND_SINGLE_LINE_SENTENCE;

    private static TextInputProcessorUtil textInputProcessor = new TextInputProcessorUtil();

    @Test
    public void getLastWord_success() {
        textInputProcessor.setContent(FIRST_SINGLE_LINE_SENTENCE);
        assertEquals(SECOND_WORD, textInputProcessor.getLastWord());

        textInputProcessor.setContent(MULTIPLE_LINES_SENTENCE);
        assertEquals(EMPTY_STRING, textInputProcessor.getLastWord());
    }

    @Test
    public void getFirstWord_success() {
        textInputProcessor.setContent(FIRST_SINGLE_LINE_SENTENCE);
        assertEquals(FIRST_WORD, textInputProcessor.getFirstWord());

        textInputProcessor.setContent(MULTIPLE_LINES_SENTENCE);
        assertEquals(FIRST_WORD, textInputProcessor.getFirstWord());
    }

    @Test
    public void getLastLine_success() {
        textInputProcessor.setContent(MULTIPLE_LINES_SENTENCE);
        assertEquals(SECOND_SINGLE_LINE_SENTENCE, textInputProcessor.getLastLine());
    }
}
```
###### \java\systemtests\GroupCommandSystemTest.java
``` java
package systemtests;

import static seedu.recipe.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import org.junit.Test;

import seedu.recipe.commons.core.Messages;
import seedu.recipe.logic.commands.GroupCommand;
import seedu.recipe.logic.parser.CliSyntax;
import seedu.recipe.model.Model;

public class GroupCommandSystemTest extends RecipeBookSystemTest {

    private static final String GROUP_NAME = "My best";

    private static final String WHITESPACE = " ";
    private static final String FIRST_INDEX = "1";
    private static final String SECOND_INDEX = "2";

    @Test
    public void group() throws Exception {
        final Model defaultModel = getModel();

        /* Case: group with valid name and indices -> successful */
        String command = GroupCommand.COMMAND_WORD + WHITESPACE + CliSyntax.PREFIX_GROUP_NAME + GROUP_NAME + WHITESPACE
                + CliSyntax.PREFIX_INDEX + FIRST_INDEX + WHITESPACE + CliSyntax.PREFIX_INDEX + SECOND_INDEX;
        String expectedResultMessage = String.format(GroupCommand.MESSAGE_SUCCESS, GROUP_NAME);
        assertCommandSuccess(command, expectedResultMessage, defaultModel);

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("gRoUp", MESSAGE_UNKNOWN_COMMAND);

        /*Case: invalid index -> rejected */
        String invalidIndex = String.valueOf(defaultModel.getRecipeBook().getRecipeList().size() + 1);
        command = GroupCommand.COMMAND_WORD + WHITESPACE + CliSyntax.PREFIX_GROUP_NAME + GROUP_NAME + WHITESPACE
                + CliSyntax.PREFIX_INDEX + invalidIndex;
        expectedResultMessage = Messages.MESSAGE_INVALID_RECIPE_DISPLAYED_INDEX;
        assertCommandFailure(command, expectedResultMessage);
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
```
###### \java\systemtests\ViewGroupCommandSystemTest.java
``` java
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
```
