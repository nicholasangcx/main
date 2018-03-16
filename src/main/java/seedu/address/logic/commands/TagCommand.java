package seedu.address.logic.commands;

import java.util.Arrays;

import seedu.address.model.tag.TagContainsKeywordsPredicate;

/**
 * Finds and lists all persons in address book whose tag contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class TagCommand extends Command {

    public static final String COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose tags contain any of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " favourite";

    // Contains a list of strings of the keywords
    private final TagContainsKeywordsPredicate predicate;
    private final String[] tagKeywords;

    public TagCommand(TagContainsKeywordsPredicate predicate, String[] tagKeywords) {
        this.predicate = predicate;
        this.tagKeywords = tagKeywords;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredPersonList(predicate);
        return new CommandResult(getMessageForTagListShownSummary
                                    (model.getFilteredPersonList().size(), Arrays.toString(tagKeywords)));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TagCommand // instanceof handles nulls
                && this.predicate.equals(((TagCommand) other).predicate)); // state check
    }
}
