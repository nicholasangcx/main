package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_PERSONS_WITH_TAGS_LISTED_OVERVIEW;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.TagContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code TagCommand}.
 */

public class TagCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        TagContainsKeywordsPredicate firstPredicate =
                new TagContainsKeywordsPredicate(Collections.singletonList("first"));
        String[] firstArray = {"first"};
        TagContainsKeywordsPredicate secondPredicate =
                new TagContainsKeywordsPredicate(Collections.singletonList("second"));
        String[] secondArray = {"second"};

        TagCommand tagFirstCommand = new TagCommand(firstPredicate, firstArray);
        TagCommand tagSecondCommand = new TagCommand(secondPredicate, secondArray);

        // same object -> returns true
        assertTrue(tagFirstCommand.equals(tagFirstCommand));

        // same values -> returns true
        TagCommand tagFirstCommandCopy = new TagCommand(firstPredicate, firstArray);
        assertTrue(tagFirstCommand.equals(tagFirstCommandCopy));

        // different types -> returns false
        assertFalse(tagFirstCommand.equals(1));

        // null -> returns false
        assertFalse(tagFirstCommand == null);

        // different person -> returns false
        assertFalse(tagFirstCommand.equals(tagSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String userInput = " ";
        String[] keywords = userInput.split("\\s+");
        String expectedMessage = String.format(MESSAGE_PERSONS_WITH_TAGS_LISTED_OVERVIEW, 0,
                                                    Arrays.toString(keywords));
        TagCommand command = prepareCommand(" ");
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {
        String userInput = "family owesMoney";
        String[] keywords = userInput.split("\\s+");
        String expectedMessage = String.format(MESSAGE_PERSONS_WITH_TAGS_LISTED_OVERVIEW, 3,
                                                    Arrays.toString(keywords));
        TagCommand command = prepareCommand(userInput);
        assertCommandSuccess(command, expectedMessage, Arrays.asList(ALICE, BENSON, CARL));
    }

    /**
     * Parses {@code userInput} into a {@code TagCommand}.
     */
    private TagCommand prepareCommand(String userInput) {

        TagCommand command =
                new TagCommand(new TagContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+"))),
                                        userInput.split("\\s+"));
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     *     - the command feedback is equal to {@code expectedMessage}<br>
     *     - the {@code FilteredList<Person>} is equal to {@code expectedList}<br>
     *     - the {@code AddressBook} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(TagCommand command, String expectedMessage, List<Person> expectedList) {
        AddressBook expectedAddressBook = new AddressBook(model.getAddressBook());
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredPersonList());
        assertEquals(expectedAddressBook, model.getAddressBook());
    }
}
