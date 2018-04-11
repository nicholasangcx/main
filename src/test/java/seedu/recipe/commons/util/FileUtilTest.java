package seedu.recipe.commons.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.recipe.MainApp;
import seedu.recipe.model.recipe.Image;
import seedu.recipe.testutil.Assert;

public class FileUtilTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getPath() {

        // valid case
        assertEquals("folder" + File.separator + "sub-folder", FileUtil.getPath("folder/sub-folder"));

        // null parameter -> throws NullPointerException
        thrown.expect(NullPointerException.class);
        FileUtil.getPath(null);

        // no forwards slash -> assertion failure
        thrown.expect(AssertionError.class);
        FileUtil.getPath("folder");
    }

    //@@author RyanAngJY
    @Test
    public void isImageFile() {
        // file is not an image file
        File file = new File(MainApp.class.getResource("/view/DarkTheme.css").toExternalForm()
                .substring(5));
        assertFalse(FileUtil.isImageFile(file));

        // file is directory
        file = new File(MainApp.class.getResource("/view").toExternalForm().substring(5));
        assertFalse(FileUtil.isImageFile(file));

        // file is null pointer
        File nullFile = null;
        Assert.assertThrows(NullPointerException.class, () -> FileUtil.isImageFile(nullFile));

        // valid image file
        file = new File(Image.VALID_IMAGE_PATH);
        assertTrue(FileUtil.isImageFile(file));
    }
    //@@author
}
