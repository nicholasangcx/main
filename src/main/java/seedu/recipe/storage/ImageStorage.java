//@@author RyanAngJY
package seedu.recipe.storage;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import seedu.recipe.commons.core.LogsCenter;
import seedu.recipe.commons.util.FileUtil;
import seedu.recipe.model.ReadOnlyRecipeBook;
import seedu.recipe.model.recipe.Image;

/**
 * A class to save RecipeBook image data stored on the hard disk.
 */
public class ImageStorage {
    public static final String IMAGE_FOLDER = "images/";
    private static final String RECIPE_BOOK_FILENAME = "recipebook.xml";
    private static final String WARNING_UNABLE_TO_SAVE_IMAGE = "Image cannot be saved.";

    /**
     * Saves all image files into the images folder of the application
     *
     * @param filePath location of the image. Cannot be null
     */
    public static void saveAllImageFiles(ReadOnlyRecipeBook recipeBook, String filePath) throws IOException {
        String imageFolderPath = filePath.replaceAll(RECIPE_BOOK_FILENAME, IMAGE_FOLDER);
        File imageFolder = new File(imageFolderPath);
        FileUtil.createDirs(imageFolder);

        for (int i = 0; i < recipeBook.getRecipeList().size(); i++) {
            Image recipeImage = recipeBook.getRecipeList().get(i).getImage();
            saveImageFile(recipeImage.toString(), imageFolderPath);
            recipeImage.setImageToInternalReference();
        }
    }

    /**
     * Saves an image file into the images folder of the application
     *
     * @param imagePath       location of the image. Cannot be null
     * @param imageFolderPath location of the image. Cannot be null
     */
    public static void saveImageFile(String imagePath, String imageFolderPath) {
        try {
            File imageToSave = new File(imagePath);
            File pathToNewImage = new File(imageFolderPath + imageToSave.getName());
            Files.copy(imageToSave.toPath(), pathToNewImage.toPath(), REPLACE_EXISTING);
        } catch (IOException e) {
            LogsCenter.getLogger(ImageStorage.class).warning(WARNING_UNABLE_TO_SAVE_IMAGE);
        }
    }
}
//@@author
