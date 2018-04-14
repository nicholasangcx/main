//@@author kokonguyen191
package seedu.recipe.logic.commands.util;

/**
 *  The API set of WikiaQueryHandler
 */
public interface WikiaQuery {

    /**
     * Returns the string value of the URL of the query.
     * This string can be used to get a displayable page for the {@code BrowserPanel}.
     */
    String getRecipeQueryUrl();

    /**
     * Returns the number of results from the query.
     */
    int getQueryNumberOfResults();
}
