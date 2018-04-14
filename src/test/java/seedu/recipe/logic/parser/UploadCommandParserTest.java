//@@author nicholasangcx
package seedu.recipe.logic.parser;

import static seedu.recipe.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.recipe.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.recipe.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.recipe.logic.commands.UploadCommand;
import seedu.recipe.model.file.Filename;

public class UploadCommandParserTest {

    private UploadCommandParser parser = new UploadCommandParser();

    @Test
    public void parseEmptyArgThrowsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, UploadCommand.MESSAGE_USAGE));
    }

    @Test
    public void parseValidArgsReturnsUploadCommand() {
        // no leading and trailing whitespaces
        UploadCommand expectedUploadCommand =
                new UploadCommand("RecipeBook.xml");
        assertParseSuccess(parser, "RecipeBook", expectedUploadCommand);

        // ignores subsequent keywords after the first
        assertParseSuccess(parser, " \n RecipeBook \n \t otherBook \t", expectedUploadCommand);
    }

    @Test
    public void parseInvalidArgsThrowsParseException() {
        assertParseFailure(parser, "recipe/book", Filename.MESSAGE_FILENAME_CONSTRAINTS);
    }
}
//@@author
