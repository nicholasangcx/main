package seedu.recipe.ui;

import static org.junit.Assert.assertEquals;
import static seedu.recipe.ui.util.KeyboardShortcutsMapping.LAST_COMMAND;
import static seedu.recipe.ui.util.KeyboardShortcutsMapping.NEXT_COMMAND;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.CommandBoxHandle;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import seedu.recipe.logic.Logic;
import seedu.recipe.logic.LogicManager;
import seedu.recipe.logic.commands.ListCommand;
import seedu.recipe.model.Model;
import seedu.recipe.model.ModelManager;
import seedu.recipe.ui.util.KeyboardShortcutsMapping;

public class CommandBoxTest extends GuiUnitTest {

    private static final String LF = "\n";
    private static final String WHITESPACE = " ";
    private static final String COMMAND_THAT_SUCCEEDS = ListCommand.COMMAND_WORD;
    private static final String COMMAND_THAT_FAILS = "invalid command";
    private static final String FIRST_LINE_OF_COMMAND_THAT_HAS_MULTIPLE_LINES = "add";
    private static final String SECOND_LINE_OF_COMMAND_THAT_HAS_MULTIPLE_LINES = "should not work for now";
    private static final String COMMAND_THAT_HAS_MULTIPLE_LINES = FIRST_LINE_OF_COMMAND_THAT_HAS_MULTIPLE_LINES + LF
            + SECOND_LINE_OF_COMMAND_THAT_HAS_MULTIPLE_LINES;
    private static final String ADD_COMMAND = "add";
    private static final String SECOND_FIELD_OF_ADD_COMMAND = "cooking_time/";
    private static final String PREFIX_CALORIES = "calories/";
    private static final String PREFIX_COOKING_TIME = "cooking_time/";
    private static final String PREFIX_IMG = "img/";
    private static final String PREFIX_INGREDIENT = "ingredient/";
    private static final String PREFIX_INSTRUCTION = "instruction/";
    private static final String PREFIX_NAME = "name/";
    private static final String PREFIX_PREPARATION_TIME = "preparation_time/";
    private static final String PREFIX_SERVINGS = "servings/";
    private static final String PREFIX_TAG = "tag/";
    private static final String PREFIX_URL = "url/";
    private static final String RECIPE_NAME = "Chicken rice";
    private static final String TAG = "Best";
    private static final String ADD_COMMAND_WITH_PREFIX_NAME = ADD_COMMAND + WHITESPACE + PREFIX_NAME;
    private static final String AUTO_COMPLETION_FOR_ADD_COMMAND = ADD_COMMAND + WHITESPACE + LF + PREFIX_NAME
            + WHITESPACE + LF + PREFIX_INGREDIENT + WHITESPACE + LF + PREFIX_INSTRUCTION + WHITESPACE + LF
            + PREFIX_COOKING_TIME + WHITESPACE + LF + PREFIX_PREPARATION_TIME + WHITESPACE + LF + PREFIX_CALORIES
            + WHITESPACE + LF + PREFIX_SERVINGS + WHITESPACE + LF + PREFIX_URL + WHITESPACE + LF + PREFIX_IMG
            + WHITESPACE + LF + PREFIX_TAG;
    private static final String AUTO_COMPLETION_FOR_ADD_COMMAND_WITH_RECIPE_NAME = ADD_COMMAND + WHITESPACE + LF
            + PREFIX_NAME + RECIPE_NAME + WHITESPACE + LF + PREFIX_INGREDIENT + WHITESPACE + LF + PREFIX_INSTRUCTION
            + WHITESPACE + LF + PREFIX_COOKING_TIME + WHITESPACE + LF + PREFIX_PREPARATION_TIME + WHITESPACE + LF
            + PREFIX_CALORIES + WHITESPACE + LF + PREFIX_SERVINGS + WHITESPACE + LF + PREFIX_URL + WHITESPACE + LF
            + PREFIX_IMG + WHITESPACE + LF + PREFIX_TAG;
    private static final String AUTO_COMPLETION_FOR_ADD_COMMAND_WITH_RECIPE_NAME_AND_TAG = ADD_COMMAND + WHITESPACE + LF
            + PREFIX_NAME + RECIPE_NAME + WHITESPACE + LF + PREFIX_INGREDIENT + WHITESPACE + LF + PREFIX_INSTRUCTION
            + WHITESPACE + LF + PREFIX_COOKING_TIME + WHITESPACE + LF + PREFIX_PREPARATION_TIME + WHITESPACE + LF
            + PREFIX_CALORIES + WHITESPACE + LF + PREFIX_SERVINGS + WHITESPACE + LF + PREFIX_URL + WHITESPACE + LF
            + PREFIX_IMG + WHITESPACE + LF + PREFIX_TAG + TAG;

    private ArrayList<String> defaultStyleOfCommandBox;
    private ArrayList<String> errorStyleOfCommandBox;

    private CommandBoxHandle commandBoxHandle;

    @Before
    public void setUp() {
        Model model = new ModelManager();
        Logic logic = new LogicManager(model);

        CommandBox commandBox = new CommandBox(logic);
        commandBoxHandle = new CommandBoxHandle(getChildNode(commandBox.getRoot(),
                CommandBoxHandle.COMMAND_INPUT_FIELD_ID));
        uiPartRule.setUiPart(commandBox);

        defaultStyleOfCommandBox = new ArrayList<>(commandBoxHandle.getStyleClass());

        errorStyleOfCommandBox = new ArrayList<>(defaultStyleOfCommandBox);
        errorStyleOfCommandBox.add(CommandBox.ERROR_STYLE_CLASS);
    }

    @Test
    public void commandBox_startingWithSuccessfulCommand() {
        assertBehaviorForSuccessfulCommand();
        assertBehaviorForFailedCommand();
    }

    @Test
    public void commandBox_startingWithFailedCommand() {
        assertBehaviorForFailedCommand();
        assertBehaviorForSuccessfulCommand();

        // verify that style is changed correctly even after multiple consecutive failed commands
        assertBehaviorForSuccessfulCommand();
        assertBehaviorForFailedCommand();
        assertBehaviorForFailedCommand();
    }

    @Test
    public void commandBox_handleKeyPress() {
        commandBoxHandle.run(COMMAND_THAT_FAILS);
        assertEquals(errorStyleOfCommandBox, commandBoxHandle.getStyleClass());
        guiRobot.push(KeyCode.ESCAPE);
        assertEquals(errorStyleOfCommandBox, commandBoxHandle.getStyleClass());

        guiRobot.push(KeyCode.A);
        assertEquals(defaultStyleOfCommandBox, commandBoxHandle.getStyleClass());
    }

    //@@author kokonguyen191
    @Test
    public void commandBox_handleMultipleLinesCommand() {
        commandBoxHandle.appendText(FIRST_LINE_OF_COMMAND_THAT_HAS_MULTIPLE_LINES);
        guiRobot.push(KeyboardShortcutsMapping.NEW_LINE_IN_COMMAND);
        commandBoxHandle.appendText(SECOND_LINE_OF_COMMAND_THAT_HAS_MULTIPLE_LINES);
        assertInput(COMMAND_THAT_HAS_MULTIPLE_LINES);
    }
    //@@author

    //@@author hoangduong1607
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

    //@@author
    @Test
    public void handleKeyPress_startingWithUp() {
        // empty history
        assertInputHistory(LAST_COMMAND, "");
        assertInputHistory(NEXT_COMMAND, "");

        // one command
        commandBoxHandle.run(COMMAND_THAT_SUCCEEDS);
        assertInputHistory(LAST_COMMAND, COMMAND_THAT_SUCCEEDS);
        assertInputHistory(NEXT_COMMAND, "");

        // two commands (latest command is failure)
        commandBoxHandle.run(COMMAND_THAT_FAILS);
        assertInputHistory(LAST_COMMAND, COMMAND_THAT_SUCCEEDS);
        assertInputHistory(LAST_COMMAND, COMMAND_THAT_SUCCEEDS);
        assertInputHistory(NEXT_COMMAND, COMMAND_THAT_FAILS);
        assertInputHistory(NEXT_COMMAND, "");
        assertInputHistory(NEXT_COMMAND, "");
        assertInputHistory(LAST_COMMAND, COMMAND_THAT_FAILS);

        // insert command in the middle of retrieving previous commands
        guiRobot.push(LAST_COMMAND);
        String thirdCommand = "list";
        commandBoxHandle.run(thirdCommand);
        assertInputHistory(LAST_COMMAND, thirdCommand);
        assertInputHistory(LAST_COMMAND, COMMAND_THAT_FAILS);
        assertInputHistory(LAST_COMMAND, COMMAND_THAT_SUCCEEDS);
        assertInputHistory(NEXT_COMMAND, COMMAND_THAT_FAILS);
        assertInputHistory(NEXT_COMMAND, thirdCommand);
        assertInputHistory(NEXT_COMMAND, "");
    }

    @Test
    public void handleKeyPress_startingWithDown() {
        // empty history
        assertInputHistory(NEXT_COMMAND, "");
        assertInputHistory(LAST_COMMAND, "");

        // one command
        commandBoxHandle.run(COMMAND_THAT_SUCCEEDS);
        assertInputHistory(NEXT_COMMAND, "");
        assertInputHistory(LAST_COMMAND, COMMAND_THAT_SUCCEEDS);

        // two commands
        commandBoxHandle.run(COMMAND_THAT_FAILS);
        assertInputHistory(NEXT_COMMAND, "");
        assertInputHistory(LAST_COMMAND, COMMAND_THAT_FAILS);

        // insert command in the middle of retrieving previous commands
        guiRobot.push(LAST_COMMAND);
        String thirdCommand = "list";
        commandBoxHandle.run(thirdCommand);
        assertInputHistory(NEXT_COMMAND, "");
        assertInputHistory(LAST_COMMAND, thirdCommand);
    }

    /**
     * Runs a command that fails, then verifies that <br>
     * - the text remains <br>
     * - the command box's style is the same as {@code errorStyleOfCommandBox}.
     */
    private void assertBehaviorForFailedCommand() {
        commandBoxHandle.run(COMMAND_THAT_FAILS);
        assertEquals(COMMAND_THAT_FAILS, commandBoxHandle.getInput());
        assertEquals(errorStyleOfCommandBox, commandBoxHandle.getStyleClass());
    }

    /**
     * Runs a command that succeeds, then verifies that <br>
     * - the text is cleared <br>
     * - the command box's style is the same as {@code defaultStyleOfCommandBox}.
     */
    private void assertBehaviorForSuccessfulCommand() {
        commandBoxHandle.run(COMMAND_THAT_SUCCEEDS);
        assertEquals("", commandBoxHandle.getInput());
        assertEquals(defaultStyleOfCommandBox, commandBoxHandle.getStyleClass());
    }

    /**
     * Pushes {@code keycode} and checks that the input in the {@code commandBox} equals to {@code expectedCommand}.
     */
    private void assertInputHistory(KeyCodeCombination keyCodeCombination, String expectedCommand) {
        guiRobot.push(keyCodeCombination);
        assertEquals(expectedCommand, commandBoxHandle.getInput());
    }

    /**
     * Pushes {@code keycode} and checks that the input in the {@code commandBox} equals to {@code expectedCommand}.
     */
    private void assertInputHistory(KeyCode keycode, String expectedCommand) {
        guiRobot.push(keycode);
        assertEquals(expectedCommand, commandBoxHandle.getInput());
    }

    //@@author kokonguyen191

    /**
     * Checks that the input in the {@code commandBox} equals to {@code expectedCommand}.
     */
    private void assertInput(String expectedCommand) {
        assertEquals(expectedCommand, commandBoxHandle.getInput());
    }
}
