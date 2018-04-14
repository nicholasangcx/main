//@@author kokonguyen191
package seedu.recipe.logic.commands;

import seedu.recipe.commons.core.EventsCenter;
import seedu.recipe.commons.events.ui.WebParseRequestEvent;

/**
 * Parses the current page loaded in the BrowserPanel.
 */
public class ParseCommand extends Command {

    public static final String COMMAND_WORD = "parse";
    public static final String MESSAGE_SUCCESS = "ReciRecipe tried to parse the web page.";

    @Override
    public CommandResult execute() {
        EventsCenter.getInstance().post(new WebParseRequestEvent());
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ParseCommand); // instanceof handles nulls
    }
}
