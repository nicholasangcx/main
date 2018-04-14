//@@author kokonguyen191
package seedu.recipe.commons.events.ui;

import seedu.recipe.commons.events.BaseEvent;
import seedu.recipe.logic.commands.util.WikiaQueryHandler;

/**
 * Indicates a request to search for recipe on the Internet
 */
public class InternetSearchRequestEvent extends BaseEvent {

    private final WikiaQueryHandler wikiaQueryHandler;

    public InternetSearchRequestEvent(WikiaQueryHandler wikiaQueryHandler) {
        this.wikiaQueryHandler = wikiaQueryHandler;
    }

    public int getQueryNumberOfResults() {
        return wikiaQueryHandler.getQueryNumberOfResults();
    }

    public String getRecipeQueryUrl() {
        return wikiaQueryHandler.getRecipeQueryUrl();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
