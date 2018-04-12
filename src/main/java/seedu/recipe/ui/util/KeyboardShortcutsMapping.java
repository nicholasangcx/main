package seedu.recipe.ui.util;

import javafx.scene.input.KeyCodeCombination;

/**
 * Contains all mappings for keyboard shortcuts
 */
public class KeyboardShortcutsMapping {
    public static final KeyCodeCombination COMMAND_SUBMISSION =
            (KeyCodeCombination) KeyCodeCombination.valueOf("Enter");
    public static final KeyCodeCombination LAST_COMMAND =
            (KeyCodeCombination) KeyCodeCombination.valueOf("Ctrl+Up");
    public static final KeyCodeCombination NEXT_COMMAND =
            (KeyCodeCombination) KeyCodeCombination.valueOf("Ctrl+Down");
    public static final KeyCodeCombination NEW_LINE_IN_COMMAND =
            (KeyCodeCombination) KeyCodeCombination.valueOf("Shift+Enter");
    public static final KeyCodeCombination SHOW_SUGGESTIONS_COMMAND =
            (KeyCodeCombination) KeyCodeCombination.valueOf("Shift+Tab");
    public static final KeyCodeCombination NEXT_FIELD =
            (KeyCodeCombination) KeyCodeCombination.valueOf("Tab");
    public static final KeyCodeCombination PREV_FIELD =
            (KeyCodeCombination) KeyCodeCombination.valueOf("Ctrl+Tab");
}
