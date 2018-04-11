//@@author RyanAngJY
package seedu.recipe.model.recipe;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.recipe.testutil.Assert;

public class ImageTest {
    private static final String INVALID_IMAGE_URL = "http://google.com";
    private static final String NOT_AN_IMAGE_PATH = "build.gradle";
    private static final String VALID_IMAGE_URL = "https://i.imgur.com/FhRsgCK.jpg";

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Image(null));
    }

    @Test
    public void constructor_invalidImage_throwsIllegalArgumentException() {
        String invalidImage = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Image(invalidImage));
    }

    @Test
    public void isValidImage() {
        // blank image
        assertFalse(Image.isValidImage("")); // empty string
        assertFalse(Image.isValidImage("   ")); // spaces only
        assertFalse(Image.isValidImage("\t\n\t\r\n"));

        // invalid image
        assertFalse(Image.isValidImage("estsed")); //random string

        // valid image path
        assertTrue(Image.isValidImage(Image.NULL_IMAGE_REFERENCE));
        assertTrue(Image.isValidImage(Image.VALID_IMAGE_PATH));
        // invalid image path
        assertFalse(Image.isValidImage("ZZZ://ZZZ!!@@#"));
        // not an image
        assertFalse(Image.isValidImage(NOT_AN_IMAGE_PATH));

        // valid image url
        assertTrue(Image.isValidImage(VALID_IMAGE_URL));
        // invalid image url
        assertFalse(Image.isValidImage(INVALID_IMAGE_URL));
    }

    @Test
    public void setImageToInternalReference() {
        Image imageStub = new Image(Image.VALID_IMAGE_PATH);
        imageStub.setImageToInternalReference();
        assertTrue(imageStub.toString().equals(Image.IMAGE_STORAGE_FOLDER + imageStub.getImageName()));
    }
}
//@@author
