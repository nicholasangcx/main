//@@author kokonguyen191
package seedu.recipe.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

import org.junit.After;
import org.junit.Test;

import seedu.recipe.model.recipe.Image;
import seedu.recipe.testutil.Assert;

public class ImageDownloaderTest {

    private static final String INVALID_IMAGE_URL = "http://google.com";
    private static final String VALID_IMAGE_URL = "https://i.imgur.com/FhRsgCK.jpg";
    private static final String VALID_IMAGE_MD5 = "2A78C63135CCB8BCECEF189FE0CD834C";
    private static final String VALID_IMAGE_PATH =
            Image.IMAGE_STORAGE_FOLDER + VALID_IMAGE_MD5 + "." + ImageDownloader.DOWNLOADED_IMAGE_FORMAT;
    private static final long VALID_IMAGE_CRC = 2184062566L;

    @Test
    public void isValidImageUrl() throws Exception {
        // not an image url
        assertFalse(ImageDownloader.isValidImageUrl(null));
        assertFalse(ImageDownloader.isValidImageUrl("\t\n\t\r\n"));
        assertFalse(ImageDownloader.isValidImageUrl("ZZZ://ZZZ!!@@#"));
        assertFalse(ImageDownloader.isValidImageUrl(Image.VALID_IMAGE_PATH));
        assertFalse(ImageDownloader.isValidImageUrl(Image.NULL_IMAGE_REFERENCE));

        // invalid image url
        assertFalse(ImageDownloader.isValidImageUrl(INVALID_IMAGE_URL));

        // valid image url
        assertTrue(ImageDownloader.isValidImageUrl(VALID_IMAGE_URL));
    }

    @Test
    public void downloadImage_invalidUrl_throwsAssertionError() {
        Assert.assertThrows(AssertionError.class, () -> ImageDownloader.downloadImage(INVALID_IMAGE_URL));
    }

    @Test
    public void downloadImage_validUrl_returnsImageName() throws Exception {
        // First download
        String fileName = ImageDownloader.downloadImage(VALID_IMAGE_URL);
        File file = new File(fileName);
        assertTrue(file.exists());
        assertImageCrc(file, VALID_IMAGE_CRC);
        assertEquals(VALID_IMAGE_PATH, fileName);

        // Re-download will still return file name
        assertEquals(VALID_IMAGE_PATH, ImageDownloader.downloadImage(VALID_IMAGE_URL));
    }

    @After
    public void cleanUp() {
        File file = new File(VALID_IMAGE_PATH);
        file.delete();
    }

    /**
     * Asserts that {@code image} has CRC {@code crcValue}
     */
    private void assertImageCrc(File image, long crcValue) throws IOException {
        InputStream in = new FileInputStream(image);
        CRC32 crc32 = new CRC32();
        byte[] buffer = new byte[1000];
        int bytes;
        while ((bytes = in.read(buffer)) != -1) {
            crc32.update(buffer, 0, bytes);
        }
        long crc = crc32.getValue();
        in.close();

        assertEquals(VALID_IMAGE_CRC, crc);
    }
}
