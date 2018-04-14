//@@author hoangduong1607
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
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewGroupCommand.MESSAGE_USAGE));
        }
    }
}
