package seedu.recipe.storage;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import seedu.recipe.MainApp;
import seedu.recipe.commons.util.FileUtil;
import seedu.recipe.model.recipe.Image;

public class ImageStorageTest {
    private static final String IMAGE_NAME = "clock.png";
    private static final String VIEW_FOLDER = MainApp.class.getResource("/view/")
            .toExternalForm().substring(5);
    @Test
    public void saveImageFile() {
        ImageStorage.saveImageFile(Image.VALID_IMAGE_PATH, VIEW_FOLDER);
        assertTrue(FileUtil.isImageFile(new File(VIEW_FOLDER + IMAGE_NAME)));
    }
}
