//@@author nicholasangcx
package seedu.recipe.logic.commands;

import static seedu.recipe.model.file.Filename.MESSAGE_FILENAME_CONSTRAINTS;

import com.dropbox.core.DbxException;

import seedu.recipe.commons.core.EventsCenter;
import seedu.recipe.commons.events.ui.UploadRecipesEvent;
import seedu.recipe.ui.util.CloudStorageUtil;

/**
 * Uploads all recipes online, specifically to Dropbox.
 */
public class UploadCommand extends Command {

    public static final String COMMAND_WORD = "upload";
    public static final String MESSAGE_SUCCESS = "Upload Success!";
    public static final String MESSAGE_FAILURE = "Failed to upload!";
    public static final String MESSAGE_ACCESS_TOKEN = "Copy and paste the code given by dropbox.\n"
            + "Example: token VALID_ACCESS_TOKEN";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Uploads all recipes to your Dropbox with the "
            + "specified filename, with no spaces. It will only take in the first parameter. "
            + MESSAGE_FILENAME_CONSTRAINTS + "\n"
            + "Parameters: KEYWORD\n"
            + "Example: " + COMMAND_WORD + " RecipeBook";

    private final String xmlExtensionFilename;

    /**
     * Creates an UploadCommand to upload recipebook.xml to Dropbox with the
     * specified {@code String XmlExtensionFilename}
     */
    public UploadCommand(String xmlExtensionFilename) {
        this.xmlExtensionFilename = xmlExtensionFilename;
        CloudStorageUtil.setUploadFilename(xmlExtensionFilename);
    }

    @Override
    public CommandResult execute() throws DbxException {
        if (CloudStorageUtil.hasAccessToken()) {
            CloudStorageUtil.upload();
            return new CommandResult(MESSAGE_SUCCESS);
        }
        EventsCenter.getInstance().post(new UploadRecipesEvent());
        return new CommandResult(MESSAGE_ACCESS_TOKEN);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UploadCommand // instanceof handles nulls
                && xmlExtensionFilename.equals(((UploadCommand) other).xmlExtensionFilename));
    }
}
//@@author
