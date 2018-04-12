# hoangduong1607
###### \java\seedu\recipe\logic\commands\GroupCommand.java
``` java
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
```
###### \java\seedu\recipe\logic\commands\ViewGroupCommand.java
``` java
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
```
###### \java\seedu\recipe\logic\parser\GroupCommandParser.java
``` java
package seedu.recipe.logic.parser;

import static seedu.recipe.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_GROUP_NAME;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_INDEX;

import java.util.Set;
import java.util.stream.Stream;

import seedu.recipe.commons.core.index.Index;
import seedu.recipe.commons.exceptions.IllegalValueException;
import seedu.recipe.logic.commands.GroupCommand;
import seedu.recipe.logic.parser.exceptions.ParseException;
import seedu.recipe.model.recipe.GroupName;

/**
 * Parses input arguments and creates a new GroupCommand object
 */
public class GroupCommandParser implements Parser<GroupCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the GroupCommand
     * and returns an GroupCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public GroupCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer
                .tokenize(args, PREFIX_GROUP_NAME, PREFIX_INDEX);

        if (!arePrefixesPresent(argMultimap, PREFIX_GROUP_NAME)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, GroupCommand.MESSAGE_USAGE));
        }

        try {
            GroupName groupName = ParserUtil.parseGroupName(argMultimap.getValue(PREFIX_GROUP_NAME)).get();
            Set<Index> indexSet = ParserUtil.parseIndices(argMultimap.getAllValues(PREFIX_INDEX));

            return new GroupCommand(groupName, indexSet);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
```
###### \java\seedu\recipe\logic\parser\ParserUtil.java
``` java
    /**
     * Parses {@code Collection<String> indices} into a {@code Set<Index>}.
     */
    public static Set<Index> parseIndices(Collection<String> indices) throws IllegalValueException {
        requireNonNull(indices);
        final Set<Index> indexSet = new HashSet<>();
        for (String index : indices) {
            indexSet.add(parseIndex(index));
        }
        return indexSet;
    }
```
###### \java\seedu\recipe\logic\parser\ParserUtil.java
``` java
    /**
     * Parses a {@code String groupName} into a {@code GroupName}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code groupName} is invalid.
     */
    public static GroupName parseGroupName(String groupName) throws IllegalValueException {
        requireNonNull(groupName);
        String trimmedName = groupName.trim();
        if (!GroupName.isValidName(trimmedName)) {
            throw new IllegalValueException(GroupName.MESSAGE_NAME_CONSTRAINTS);
        }
        return new GroupName(trimmedName);
    }

    /**
     * Parses a {@code Optional<String> groupName} into an {@code Optional<GroupName>} if {@code groupName} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<GroupName> parseGroupName(Optional<String> groupName) throws IllegalValueException {
        requireNonNull(groupName);
        return groupName.isPresent() ? Optional.of(parseGroupName(groupName.get())) : Optional.empty();
    }
```
###### \java\seedu\recipe\logic\parser\ViewGroupCommandParser.java
``` java
package seedu.recipe.logic.parser;

import static seedu.recipe.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.recipe.commons.exceptions.IllegalValueException;
import seedu.recipe.logic.commands.ViewGroupCommand;
import seedu.recipe.logic.parser.exceptions.ParseException;
import seedu.recipe.model.recipe.GroupName;
import seedu.recipe.model.recipe.GroupPredicate;

/**
 * Parses input arguments and creates a new ViewGroupCommand object
 */
public class ViewGroupCommandParser implements Parser<ViewGroupCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ViewGroupCommand
     * and returns an ViewGroupCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public ViewGroupCommand parse(String args) throws ParseException {
        try {
            GroupName groupName = ParserUtil.parseGroupName(args);
            return new ViewGroupCommand(new GroupPredicate(groupName), groupName);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewGroupCommand.MESSAGE_USAGE));
        }
    }
}
```
###### \java\seedu\recipe\model\recipe\GroupName.java
``` java
package seedu.recipe.model.recipe;

/**
 * Represents a Recipe's group name in the recipe book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class GroupName extends Name {
    public static final String MESSAGE_NAME_CONSTRAINTS =
            "Recipe group names should only contain alphanumeric characters and spaces, and it should not be blank";

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public GroupName(String name) {
        super(name);
    }
}
```
###### \java\seedu\recipe\model\recipe\GroupPredicate.java
``` java
package seedu.recipe.model.recipe;

import java.util.function.Predicate;

/**
 * Tests that a given {@code groupName} matches any of {@code Recipe}'s {@code groupNames}.
 */
public class GroupPredicate implements Predicate<Recipe> {
    private final GroupName groupName;

    public GroupPredicate(GroupName groupName) {
        this.groupName = groupName;
    }

    @Override
    public boolean test(Recipe recipe) {
        return recipe.getGroupNames().stream().anyMatch(groupName1 -> groupName1.equals(groupName));
    }
}
```
###### \java\seedu\recipe\ui\CommandBox.java
``` java

    /**
     * Moves caret to the next field in input text.
     * If no field is found after current position, continue from beginning of input text.
     */
    private void moveToNextField() {
        int currentCaretPosition = commandTextArea.getCaretPosition();
        int nextFieldPosition = autoCompletionUtil.getNextFieldPosition(commandTextArea.getText(),
                currentCaretPosition);
        commandTextArea.positionCaret(nextFieldPosition);
    }

    /**
     * Moves caret to the next field in input text.
     * If no field is found after current position, continue from beginning of input text.
     */
    private void moveToPrevField() {
        int currentCaretPosition = commandTextArea.getCaretPosition();
        int prevFieldPosition = autoCompletionUtil.getPrevFieldPosition(commandTextArea.getText(),
                currentCaretPosition);
        commandTextArea.positionCaret(prevFieldPosition);
    }

    /**
     * Automatically fills command box with text generated by auto-completion
     */
    protected void autoComplete(String text) {
        String prefix = commandTextArea.getText().substring(0, commandTextArea.getCaretPosition());
        String suffix = commandTextArea.getText().substring(commandTextArea.getCaretPosition());

        textInputProcessor.setContent(prefix);

        String autoCompletionText;
        int caretPosition;
        if (autoCompletionUtil.isCommandKeyWord(text)) {
            autoCompletionText = autoCompletionUtil.getAutoCompletionText(text);
            caretPosition = text.length() + 1;
        } else {
            autoCompletionText = textInputProcessor.replaceLastWord(text);
            caretPosition = autoCompletionText.length();
            autoCompletionText = autoCompletionText + suffix;
        }

        replaceText(autoCompletionText);
        commandTextArea.positionCaret(caretPosition);
    }

```
###### \java\seedu\recipe\ui\SuggestionsPopUp.java
``` java
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

        show(commandTextArea, Side.BOTTOM, anchorX, anchorY);
    }

    /**
     * Finds possible suggestions from {@code word} and
     * list of valid suggestions {@code textList}.
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
        return commandBox.getRoot().getLayoutX() + commandTextArea.getInsets().getLeft() + caretPositionX;
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
```
###### \java\seedu\recipe\ui\util\AutoCompletionUtil.java
``` java
package seedu.recipe.ui.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import seedu.recipe.logic.parser.CliSyntax;

/**
 * Contains constants and functions needed for auto-completion
 */
public class AutoCompletionUtil {
    public static final ArrayList<String> APPLICATION_COMMANDS = new ArrayList<>(Arrays.asList("add", "clear", "delete",
            "edit", "exit", "find", "group", "help", "history", "ingredient", "list", "parse", "redo", "search",
            "select", "share", "tag", "theme", "token", "undo", "upload", "view_group"));
    public static final ArrayList<String> APPLICATION_KEYWORDS = new ArrayList<>(Arrays.asList(
            CliSyntax.PREFIX_NAME.toString(), CliSyntax.PREFIX_INGREDIENT.toString(),
            CliSyntax.PREFIX_INSTRUCTION.toString(), CliSyntax.PREFIX_COOKING_TIME.toString(),
            CliSyntax.PREFIX_PREPARATION_TIME.toString(), CliSyntax.PREFIX_CALORIES.toString(),
            CliSyntax.PREFIX_SERVINGS.toString(), CliSyntax.PREFIX_TAG.toString(), CliSyntax.PREFIX_URL.toString(),
            CliSyntax.PREFIX_IMG.toString(), CliSyntax.PREFIX_GROUP_NAME.toString(),
            CliSyntax.PREFIX_INDEX.toString()));
    public static final int MAX_SUGGESTIONS = 4;
    public static final char LF = '\n';
    public static final char WHITESPACE = ' ';
    public static final char END_FIELD = '/';

    private static HashMap<String, ArrayList<String>> prefixesForCommand;

    public AutoCompletionUtil() {
        initializePrefixesForCommandsOffline();
    }

    /**
     * Creates a list of all prefixes associated with each command
     */
    private void initializePrefixesForCommandsOffline() {
        prefixesForCommand = new HashMap<>();

        ArrayList<String> addPrefixes = new ArrayList<>(Arrays.asList(CliSyntax.PREFIX_NAME.toString(),
                CliSyntax.PREFIX_INGREDIENT.toString(), CliSyntax.PREFIX_INSTRUCTION.toString(),
                CliSyntax.PREFIX_COOKING_TIME.toString(), CliSyntax.PREFIX_PREPARATION_TIME.toString(),
                CliSyntax.PREFIX_CALORIES.toString(), CliSyntax.PREFIX_SERVINGS.toString(),
                CliSyntax.PREFIX_URL.toString(), CliSyntax.PREFIX_IMG.toString(), CliSyntax.PREFIX_TAG.toString()));
        prefixesForCommand.put("add", addPrefixes);

        ArrayList<String> editPrefixes = new ArrayList<>(Arrays.asList(CliSyntax.PREFIX_NAME.toString(),
                CliSyntax.PREFIX_INGREDIENT.toString(), CliSyntax.PREFIX_INSTRUCTION.toString(),
                CliSyntax.PREFIX_TAG.toString(), CliSyntax.PREFIX_URL.toString()));
        prefixesForCommand.put("edit", editPrefixes);

        ArrayList<String> groupPrefixes = new ArrayList<>(Arrays.asList(CliSyntax.PREFIX_GROUP_NAME.toString(),
                CliSyntax.PREFIX_INDEX.toString()));
        prefixesForCommand.put("group", groupPrefixes);

        for (String command : APPLICATION_COMMANDS) {
            if (!prefixesForCommand.containsKey(command)) {
                prefixesForCommand.put(command, new ArrayList<>());
            }
        }
    }

    /**
     * Checks whether {@code text} is a command keyword
     */
    public boolean isCommandKeyWord(String text) {
        return prefixesForCommand.containsKey(text);
    }

    /**
     * Generates auto-completed command
     */
    public String getAutoCompletionText(String command) {
        String autoCompletionText = command;

        for (String prefix : prefixesForCommand.get(command)) {
            autoCompletionText = autoCompletionText + WHITESPACE + LF + prefix;
        }

        return autoCompletionText;
    }

    /**
     * Finds position of next field.
     * Returns current position of caret if no field is found
     */
    public int getNextFieldPosition(String inputText, int currentCaretPosition) {
        int nextFieldCaretPosition = currentCaretPosition;

        for (int i = 0; i < inputText.length(); i++) {
            int wrapAroundPosition = (i + currentCaretPosition) % inputText.length();

            if (inputText.charAt(wrapAroundPosition) == END_FIELD) {
                TextInputProcessorUtil textInputProcessor = new TextInputProcessorUtil();
                textInputProcessor.setContent(inputText.substring(0, wrapAroundPosition + 1));

                if (APPLICATION_KEYWORDS.contains(textInputProcessor.getLastWord())) {
                    nextFieldCaretPosition = wrapAroundPosition + 1;
                    break;
                }
            }
        }

        return nextFieldCaretPosition;
    }

    /**
     * Finds position of previous field.
     * Returns current position of caret if no field is found
     */
    public int getPrevFieldPosition(String inputText, int currentCaretPosition) {
        int prevFieldCaretPosition = currentCaretPosition;

        // skips current field (if any)
        for (int i = 2; i < inputText.length(); i++) {
            int wrapAroundPosition = currentCaretPosition - i;
            if (wrapAroundPosition < 0) {
                wrapAroundPosition += inputText.length();
            }
            wrapAroundPosition %= inputText.length();

            if (inputText.charAt(wrapAroundPosition) == END_FIELD) {
                TextInputProcessorUtil textInputProcessor = new TextInputProcessorUtil();
                textInputProcessor.setContent(inputText.substring(0, wrapAroundPosition + 1));

                if (APPLICATION_KEYWORDS.contains(textInputProcessor.getLastWord())) {
                    prevFieldCaretPosition = wrapAroundPosition + 1;
                    break;
                }
            }
        }

        return prevFieldCaretPosition;
    }

    public static HashMap<String, ArrayList<String>> getPrefixesForCommand() {
        return (HashMap<String, ArrayList<String>>) prefixesForCommand.clone();
    }
}
```
###### \java\seedu\recipe\ui\util\TextInputProcessorUtil.java
``` java
package seedu.recipe.ui.util;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Provides basic functions for processing text input
 */
public class TextInputProcessorUtil {

    private static final String EMPTY_STRING = "";
    private static final char LF = '\n';
    private static final char SPACE = ' ';
    private static final int LINE_HEIGHT = 26;

    private String content;
    private Font font;

    public TextInputProcessorUtil() {
        content = new String();
    }

    /**
     * Gets X-coordinate of caret
     */
    public double getCaretPositionX() {
        Text lastLine = new Text(getTextWithoutLastWord(getLastLine()));
        lastLine.setFont(font);
        return lastLine.prefWidth(-1);
    }

    /**
     * Gets Y-coordinate of caret
     */
    public double getCaretPositionY() {
        return getRow() * LINE_HEIGHT;
    }

    /**
     * Gets row index (1-indexed) of caret
     */
    public int getRow() {
        int row = 1;
        for (int i = 0; i < content.length(); i++) {
            if (content.charAt(i) == LF) {
                row++;
            }
        }
        return row;
    }

    /**
     * Gets last word (character(s) between the last whitespace and end of string) from {@code content}
     */
    public String getLastWord() {
        String lastWord = EMPTY_STRING;

        for (int i = content.length() - 1; i >= 0; i--) {
            if (isWordSeparator(content.charAt(i))) {
                break;
            }
            lastWord = content.charAt(i) + lastWord;
        }

        return lastWord;
    }

    /**
     * Gets first word from {@code content}
     */
    public String getFirstWord() {
        String firstWord = EMPTY_STRING;

        String trimmedContent = content.trim();
        for (int i = 0; i < trimmedContent.length(); i++) {
            if (isWordSeparator(trimmedContent.charAt(i))) {
                break;
            }
            firstWord = firstWord + trimmedContent.charAt(i);
        }

        return firstWord;
    }

    /**
     * Checks whether {@code inputChar} is a word separator
     */
    private boolean isWordSeparator(char inputChar) {
        return (inputChar == LF || inputChar == SPACE);
    }

    /**
     * Gets last line from {@code content}
     */
    public String getLastLine() {
        String lastLine = new String("");

        for (int i = content.length() - 1; i >= 0; i--) {
            if (isLineSeparator(content.charAt(i))) {
                break;
            }
            lastLine = content.charAt(i) + lastLine;
        }

        return lastLine;
    }

    /**
     * Checks whether {@code inputChar} is a line separator
     */
    private boolean isLineSeparator(char inputChar) {
        return (inputChar == LF);
    }

    /**
     * Gets the resulting text after replacing last word of {@code content} with {@code newLastWord}
     */
    public String replaceLastWord(String newLastWord) {
        return getTextWithoutLastWord() + newLastWord;
    }

    /**
     * Gets the resulting text after removing last word from {@code content}
     */
    public String getTextWithoutLastWord() {
        int newLength = content.length() - getLastWord().length();
        return content.substring(0, newLength);
    }

    /**
     * Gets the resulting text after removing last word from {@code inputText}
     */
    public String getTextWithoutLastWord(String inputText) {
        int newLength = inputText.length() - getLastWord().length();
        return inputText.substring(0, newLength);
    }

    /**
     * Sets content in TextInputProcessor to {@code inputText}
     */
    public void setContent(String inputText) {
        content = inputText;
    }

    /**
     * Sets font of content in TextInputProcessor to {@code font}
     */
    public void setFont(Font inputFont) {
        font = inputFont;
    }
}
```
