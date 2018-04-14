//@@author hoangduong1607
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
                CliSyntax.PREFIX_COOKING_TIME.toString(), CliSyntax.PREFIX_PREPARATION_TIME.toString(),
                CliSyntax.PREFIX_CALORIES.toString(), CliSyntax.PREFIX_SERVINGS.toString(),
                CliSyntax.PREFIX_URL.toString(), CliSyntax.PREFIX_IMG.toString(), CliSyntax.PREFIX_TAG.toString()));
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
     * Checks whether {@code text} is a field prefix
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
     * Finds position of next field prefix.
     * Returns current position of caret if no field prefix is found
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
     * Finds position of previous field prefix.
     * Returns current position of caret if no field prefix is found
     */
    public int getPrevFieldPosition(String inputText, int currentCaretPosition) {
        int prevFieldCaretPosition = currentCaretPosition;

        // skips current field prefix (if any)
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
