//@@author kokonguyen191
package seedu.recipe.commons.exceptions;

/**
 * Signals that there is no Internet connection for an action that requires one
 */
public class NoInternetConnectionException extends Exception {
    public NoInternetConnectionException(String message) {
        super(message);
    }

}
