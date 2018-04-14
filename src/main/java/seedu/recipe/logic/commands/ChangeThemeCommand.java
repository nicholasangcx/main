//@@author kokonguyen191
package seedu.recipe.logic.commands;

import seedu.recipe.commons.core.EventsCenter;
import seedu.recipe.commons.events.ui.ChangeThemeRequestEvent;

/**
 * Toggles between dark/light theme.
 */
public class ChangeThemeCommand extends Command {

    public static final String COMMAND_WORD = "theme";

    public static final String SHOWING_CHANGE_THEME_MESSAGE = "Theme changed.";

    @Override
    public CommandResult execute() {
        EventsCenter.getInstance().post(new ChangeThemeRequestEvent());
        return new CommandResult(SHOWING_CHANGE_THEME_MESSAGE);
    }
}
