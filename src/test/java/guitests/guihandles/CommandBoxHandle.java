package guitests.guihandles;

import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import seedu.recipe.ui.CommandBox;

/**
 * A handle to the {@code CommandBox} in the GUI.
 */
public class CommandBoxHandle extends NodeHandle<TextArea> {

    public static final String COMMAND_INPUT_FIELD_ID = "#commandTextArea";

    public CommandBoxHandle(TextArea commandBoxNode) {
        super(commandBoxNode);
    }

    /**
     * Returns the text in the command box.
     */
    public String getInput() {
        return getRootNode().getText();
    }

    /**
     * Enters the given command in the Command Box and presses enter.
     *
     * @return true if the command succeeded, false otherwise.
     */
    public boolean run(String command) {
        click();
        guiRobot.interact(() -> getRootNode().setText(command));
        guiRobot.pauseForHuman();

        guiRobot.type(KeyCode.ENTER);

        return !getStyleClass().contains(CommandBox.ERROR_STYLE_CLASS);
    }

    //@@author kokonguyen191

    /**
     * Appends the given string to text already existing in the Command box
     */
    public void appendText(String text) {
        guiRobot.interact(() -> getRootNode().appendText(text));
        guiRobot.pauseForHuman();
    }

    /**
     * Submits whatever is in the CommandBox
     */
    public void submitCommand() {
        click();
        guiRobot.type(KeyCode.ENTER);
    }

    //@@author hoangduong1607

    /**
     * Inserts the given string to text at current caret position
     */
    public void insertText(String text) {
        int caretPosition = getRootNode().getCaretPosition();
        guiRobot.interact(() -> getRootNode().insertText(caretPosition, text));
        guiRobot.pauseForHuman();
    }

    //@@author

    /**
     * Returns the list of style classes present in the command box.
     */
    public ObservableList<String> getStyleClass() {
        return getRootNode().getStyleClass();
    }
}
