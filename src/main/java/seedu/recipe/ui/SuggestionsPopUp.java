//@@author hoangduong1607
package seedu.recipe.ui;

import static seedu.recipe.ui.util.AutoCompletionUtil.APPLICATION_COMMANDS;
import static seedu.recipe.ui.util.AutoCompletionUtil.MAX_SUGGESTIONS;
import static seedu.recipe.ui.util.AutoCompletionUtil.getPrefixesForCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import seedu.recipe.ui.util.TextInputProcessorUtil;

/**
 * The component that is responsible for showing a suggestion list for auto-completion
 */
public class SuggestionsPopUp extends ContextMenu {

    private static final int MARGIN = 5;

    private CommandBox commandBox;
    private TextArea commandTextArea;
    private TextInputProcessorUtil textInputProcessor;

    protected SuggestionsPopUp(CommandBox commandBox) {
        super();
        this.commandBox = commandBox;
        commandTextArea = commandBox.getCommandTextArea();
        textInputProcessor = new TextInputProcessorUtil();
    }

    /**
     * Hides suggestions
     */
    protected void hideSuggestions() {
        if (isShowing()) {
            hide();
        }
    }

    /**
     * Shows suggestions for commands when users type in Command Box
     */
    protected void showSuggestions() {
        String prefix = commandTextArea.getText().substring(0, commandTextArea.getCaretPosition());
        textInputProcessor.setContent(prefix);
        textInputProcessor.setFont(commandTextArea.getFont());
        String lastWord = textInputProcessor.getLastWord();

        // finds suggestions and displays
        ArrayList<String> suggestionList = new ArrayList<>();

        // decides to show commands or field prefixes as suggestions
        String firstWord = textInputProcessor.getFirstWord();
        if (APPLICATION_COMMANDS.contains(firstWord)) {
            if (firstWord.equals(lastWord)) {
                suggestionList.add(firstWord);
            } else {
                suggestionList.addAll(getPrefixesForCommand().get(firstWord));
            }
        } else {
            suggestionList.addAll(APPLICATION_COMMANDS);
        }

        findSuggestions(lastWord, suggestionList);

        // gets caret position based on input text and font
        double anchorX = findDisplayPositionX(textInputProcessor.getCaretPositionX());
        double anchorY = findDisplayPositionY(textInputProcessor.getCaretPositionY());

        // shows at bottom of input area if text is too long
        textInputProcessor.setContent(commandTextArea.getText());
        if (textInputProcessor.isTextTooLong()) {
            anchorX = anchorY = MARGIN;
        }

        show(commandTextArea, Side.BOTTOM, anchorX, anchorY);
    }

    /**
     * Finds possible suggestions from {@code prefix} and
     * list of valid suggestions {@code dictionary}.
     */
    private void findSuggestions(String prefix, List<String> dictionary) {
        getItems().clear();
        Collections.sort(dictionary);

        for (String suggestion : dictionary) {
            if (suggestion.startsWith(prefix)) {
                addSuggestion(suggestion);
            }

            if (getItems().size() == MAX_SUGGESTIONS) {
                break;
            }
        }
    }

    /**
     * Finds X-coordinate to display SuggestionsPopUp in CommandBox
     */
    double findDisplayPositionX(double caretPositionX) {
        return commandBox.getRoot().getLayoutX() + commandTextArea.getInsets().getLeft() + caretPositionX + MARGIN;
    }

    /**
     * Finds Y-coordinate to display SuggestionsPopUp in CommandBox
     */
    double findDisplayPositionY(double caretPositionY) {
        return Math.min(-commandTextArea.getHeight() + commandTextArea.getInsets().getTop()
                + commandTextArea.getInsets().getBottom() + caretPositionY, -commandTextArea.getInsets().getBottom())
                + MARGIN;
    }

    /**
     * Adds a suggestion to suggestion list
     */
    private void addSuggestion(String suggestion) {
        MenuItem item = new MenuItem(suggestion);
        item.setOnAction(event -> commandBox.autoComplete(item.getText()));
        getItems().add(item);
    }
}
