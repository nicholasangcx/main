//@@author kokonguyen191
package seedu.recipe.storage;

import static java.util.Objects.requireNonNull;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.imageio.ImageIO;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import seedu.recipe.commons.core.LogsCenter;
import seedu.recipe.commons.util.FileUtil;
import seedu.recipe.model.recipe.Image;

/**
 * A class that downloads images and saves them to the images folder
 */
public class ImageDownloader {

    public static final String DOWNLOADED_IMAGE_FORMAT = "jpg";

    /**
     * Returns true if {@code testUrl} is valid and links to an image
     */
    public static boolean isValidImageUrl(String testUrl) {
        URL imageUrl;
        try {
            imageUrl = new URL(testUrl);
        } catch (MalformedURLException e) {
            return false;
        }

        BufferedImage image;
        try {
            image = ImageIO.read(imageUrl);
        } catch (IOException ioe) {
            LogsCenter.getLogger(Image.class).warning("Cannot get image from "
                    + testUrl + ". It is likely the app is not connected to the Internet.");
            return false;
        }

        if (image != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Downloads an iamge from {@code imageUrlString} to the images folder
     */
    public static String downloadImage(String imageUrlString) {
        assert isValidImageUrl(imageUrlString);

        try {
            byte[] imageData = getImageData(imageUrlString);
            String md5Checksum = calculateMd5Checksum(imageData);
            String filePath = getImageFilePathFromImageName(md5Checksum);
            File file = prepareImageFile(filePath);
            if (file != null) {
                writeDataToFile(imageData, file);
            }
            return filePath;
        } catch (IOException ioe) {
            throw new AssertionError(
                    "Something wrong happened when the app was trying to "
                            + "download image data from " + imageUrlString
                            + ". This should not happen.", ioe);
        } catch (NoSuchAlgorithmException nsaee) {
            throw new AssertionError(
                    "Something wrong happened when the app was trying to "
                            + "calculate the MD5 checksum for the iamge from " + imageUrlString
                            + ". This should not happen.", nsaee);
        }
    }

    /**
     * Gets a byte array from the {@code imageUrlSring}
     */
    private static byte[] getImageData(String imageUrlString) throws IOException {
        URL imageUrl = new URL(imageUrlString);
        BufferedImage image = ImageIO.read(imageUrl);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, DOWNLOADED_IMAGE_FORMAT, byteArrayOutputStream);
        byteArrayOutputStream.flush();
        byte[] data = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return data;
    }

    /**
     * Returns the MD5 checksum String value of given {@code data}
     */
    private static String calculateMd5Checksum(byte[] data) throws NoSuchAlgorithmException {
        requireNonNull(data);
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(data);
        // Adapted from https://stackoverflow.com/questions/5470219/get-md5-string-from-message-digest
        HexBinaryAdapter hexBinaryAdapter = new HexBinaryAdapter();
        return hexBinaryAdapter.marshal(md5.digest());
    }

    private static String getImageFilePathFromImageName(String imageName) {
        return Image.IMAGE_STORAGE_FOLDER + imageName + "." + DOWNLOADED_IMAGE_FORMAT;
    }

    /**
     * Checks if {@code filePath} exists or not. If not, create a file at {@code filePath} as well as any parent
     * directory if necessary, then returns the File object.
     */
    private static File prepareImageFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (FileUtil.createFile(file)) {
            return file;
        } else {
            return null;
        }
    }

    /**
     * Writes given {@code data} to {@code file}
     */
    private static void writeDataToFile(byte[] data, File file) throws IOException {
        requireNonNull(data);
        requireNonNull(file);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(data);
        fileOutputStream.flush();
        fileOutputStream.close();
    }
}
