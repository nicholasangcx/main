package seedu.recipe.storage;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.recipe.commons.core.LogsCenter;
import seedu.recipe.commons.exceptions.DataConversionException;
import seedu.recipe.commons.exceptions.IllegalValueException;
import seedu.recipe.commons.util.FileUtil;
import seedu.recipe.model.ReadOnlyRecipeBook;

/**
 * A class to access RecipeBook data stored as an xml file on the hard disk.
 */
public class XmlRecipeBookStorage implements RecipeBookStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlRecipeBookStorage.class);


    private String filePath;

    public XmlRecipeBookStorage(String filePath) {
        this.filePath = filePath;
    }

    public String getRecipeBookFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyRecipeBook> readRecipeBook() throws DataConversionException, IOException {
        return readRecipeBook(filePath);
    }

    /**
     * Similar to {@link #readRecipeBook()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyRecipeBook> readRecipeBook(String filePath) throws DataConversionException,
                                                                                 FileNotFoundException {
        requireNonNull(filePath);

        File recipeBookFile = new File(filePath);

        if (!recipeBookFile.exists()) {
            logger.info("RecipeBook file "  + recipeBookFile + " not found");
            return Optional.empty();
        }

        XmlSerializableRecipeBook xmlRecipeBook = XmlFileStorage.loadDataFromSaveFile(new File(filePath));
        try {
            return Optional.of(xmlRecipeBook.toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + recipeBookFile + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveRecipeBook(ReadOnlyRecipeBook recipeBook) throws IOException {
        saveRecipeBook(recipeBook, filePath);
    }

    /**
     * Similar to {@link #saveRecipeBook(ReadOnlyRecipeBook)}
     * @param filePath location of the data. Cannot be null
     */
    public void saveRecipeBook(ReadOnlyRecipeBook recipeBook, String filePath) throws IOException {
        requireNonNull(recipeBook);
        requireNonNull(filePath);

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        ImageStorage.saveAllImageFiles(recipeBook, filePath);
        XmlFileStorage.saveDataToFile(file, new XmlSerializableRecipeBook(recipeBook));
    }
}
