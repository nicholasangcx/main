package seedu.recipe.storage;

import java.io.IOException;
import java.util.Optional;

import seedu.recipe.commons.exceptions.DataConversionException;
import seedu.recipe.model.ReadOnlyRecipeBook;
import seedu.recipe.model.RecipeBook;

/**
 * Represents a storage for {@link RecipeBook}.
 */
public interface RecipeBookStorage {

    /**
     * Returns the file path of the data file.
     */
    String getRecipeBookFilePath();

    /**
     * Returns RecipeBook data as a {@link ReadOnlyRecipeBook}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyRecipeBook> readRecipeBook() throws DataConversionException, IOException;

    /**
     * @see #getRecipeBookFilePath()
     */
    Optional<ReadOnlyRecipeBook> readRecipeBook(String filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyRecipeBook} to the storage.
     * @param recipeBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveRecipeBook(ReadOnlyRecipeBook recipeBook) throws IOException;

    /**
     * @see #saveRecipeBook(ReadOnlyRecipeBook)
     */
    void saveRecipeBook(ReadOnlyRecipeBook recipeBook, String filePath) throws IOException;
}
