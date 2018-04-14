//@@author nicholasangcx
package seedu.recipe.model.file;

import static java.util.Objects.requireNonNull;
import static seedu.recipe.commons.util.AppUtil.checkArgument;

/**
 * Represents a Filename used to upload to Dropbox
 * Guarantees: immutable; filename is valid as declared in {@link #isValidFilename(String)}
 */
public class Filename {

    public static final String MESSAGE_FILENAME_CONSTRAINTS = "Filenames should not contain any"
            + " incompatible characters";
    private static final String FILENAME_VALIDATION_REGEX = "[^\\Q<>:/|.?\"\\*\\E\\s]+";

    public final String filename;

    /**
     * Constructs a {@code Filename}
     *
     * @param filename a valid filename
     */
    public Filename(String filename) {
        requireNonNull(filename);
        checkArgument(isValidFilename(filename), MESSAGE_FILENAME_CONSTRAINTS);
        this.filename = filename;
    }

    /**
     * Returns true if a given string is a valid filename.
     */
    public static boolean isValidFilename(String test) {
        return test.matches(FILENAME_VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Filename // instanceof handles nulls
                && this.filename.equals(((Filename) other).filename)); // state check
    }

    @Override
    public int hashCode() {
        return filename.hashCode();
    }
}
//@@author
