//@@author RyanAngJY
package seedu.recipe.model.recipe;

import static java.util.Objects.requireNonNull;
import static seedu.recipe.commons.util.AppUtil.checkArgument;

import java.net.URL;

/**
 * Represents a Recipe's image in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidImage(String)}
 */
public class Image {

    public static final String NULL_IMAGE_REFERENCE = "-";
    public static final String MESSAGE_IMAGE_CONSTRAINTS = "Image path should be valid";
    public final String value;

    /**
     * Constructs a {@code Image}.
     *
     * @param imagePath A valid file path.
     */
    public Image(String imagePath) {
        requireNonNull(imagePath);
        checkArgument(isValidImage(imagePath), MESSAGE_IMAGE_CONSTRAINTS);
        this.value = imagePath;
    }

    /**
     *  Returns true if a given string is a valid file path, or no file path has been assigned
     */
    public static boolean isValidImage(String testImagePath) {
        if (testImagePath.equals(NULL_IMAGE_REFERENCE)) {
            return true;
        }
        try {
            URL url = new URL(testImagePath);
            url.toURI();
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Url // instanceof handles nulls
                && this.value.equals(((Url) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
//@@author
