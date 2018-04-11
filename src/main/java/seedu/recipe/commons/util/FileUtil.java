package seedu.recipe.commons.util;

import static seedu.recipe.commons.util.AppUtil.checkArgument;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.imageio.ImageIO;

/**
 * Writes and reads files
 */
public class FileUtil {

    private static final String CHARSET = "UTF-8";
    private static final String TYPE_IMAGE = "image";

    public static boolean isFileExists(File file) {
        return file.exists() && file.isFile();
    }

    //@@author RyanAngJY
    /**
     * Checks if a given file is an image file.
     *
     * @return true if a given file is a valid image file.
     */
    public static boolean isImageFile(File file) {
        if (!isFileExists(file) || file.isDirectory()) {
            return false;
        } else {
            try {
                if (ImageIO.read(file) == null) {
                    return false;
                }
            } catch (IOException exception) {
                System.out.println("Error reading file");
            }
            return true;
        }
    }
    //@@author kokonguyen191
    /**
     * Returns true if {@code testPath} is a valid file path and points to an image.
     */
    public static boolean isImageFile(String testPath) {
        File file = new File(testPath);
        return isImageFile(file);
    }
    //@@author

    /**
     * Creates a file if it does not exist along with its missing parent directories.
     * @throws IOException if the file or directory cannot be created.
     */
    public static void createIfMissing(File file) throws IOException {
        if (!isFileExists(file)) {
            createFile(file);
        }
    }

    /**
     * Creates a file if it does not exist along with its missing parent directories
     *
     * @return true if file is created, false if file already exists
     */
    public static boolean createFile(File file) throws IOException {
        if (file.exists()) {
            return false;
        }

        createParentDirsOfFile(file);

        return file.createNewFile();
    }

    /**
     * Creates the given directory along with its parent directories
     *
     * @param dir the directory to be created; assumed not null
     * @throws IOException if the directory or a parent directory cannot be created
     */
    public static void createDirs(File dir) throws IOException {
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Failed to make directories of " + dir.getName());
        }
    }

    /**
     * Creates parent directories of file if it has a parent directory
     */
    public static void createParentDirsOfFile(File file) throws IOException {
        File parentDir = file.getParentFile();

        if (parentDir != null) {
            createDirs(parentDir);
        }
    }

    /**
     * Assumes file exists
     */
    public static String readFromFile(File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath()), CHARSET);
    }

    /**
     * Writes given string to a file.
     * Will create the file if it does not exist yet.
     */
    public static void writeToFile(File file, String content) throws IOException {
        Files.write(file.toPath(), content.getBytes(CHARSET));
    }

    /**
     * Converts a string to a platform-specific file path
     * @param pathWithForwardSlash A String representing a file path but using '/' as the separator
     * @return {@code pathWithForwardSlash} but '/' replaced with {@code File.separator}
     */
    public static String getPath(String pathWithForwardSlash) {
        checkArgument(pathWithForwardSlash.contains("/"));
        return pathWithForwardSlash.replace("/", File.separator);
    }

}
