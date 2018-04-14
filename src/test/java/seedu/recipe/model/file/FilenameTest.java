//@@author nicholasangcx
package seedu.recipe.model.file;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.recipe.testutil.Assert;

public class FilenameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Filename(null));
    }

    @Test
    public void constructor_invalidFilename_throwsIllegalArgumentException() {
        String invalidFilename = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Filename(invalidFilename));
    }

    @Test
    public void isValidFilename() {
        // null Filename
        Assert.assertThrows(NullPointerException.class, () -> Filename.isValidFilename(null));

        // blank Filename
        assertFalse(Filename.isValidFilename("")); // empty string
        assertFalse(Filename.isValidFilename(" ")); // spaces only

        // invalid Filename
        assertFalse(Filename.isValidFilename("test.Filename")); // invalid character .
        assertFalse(Filename.isValidFilename("test|test")); // invalid character |
        assertFalse(Filename.isValidFilename("test/filename")); // invalid character /

        // valid Filename
        assertTrue(Filename.isValidFilename("Recipe2Book")); // alphanumeric filename
        assertTrue(Filename.isValidFilename("RecipeBook(1)")); // valid characters ()
        assertTrue(Filename.isValidFilename("Recipe_Book")); // valid character _
    }
}
//@@author
