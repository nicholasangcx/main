//@@author nicholasangcx
package seedu.recipe.ui.util;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.v2.DbxClientV2;

import seedu.recipe.commons.util.FileUtil;
import seedu.recipe.logic.commands.UploadCommand;

/**
 * Contains data and methods needed for cloud storage
 * (uploading to dropbox) purposes.
 */
public class CloudStorageUtil {

    private static final String RECIPE_DATA_FOLDER = FileUtil.getPath("data/");
    private static final File RECIPE_BOOK_FILE = new File(RECIPE_DATA_FOLDER + "recipebook.xml");
    private static final String CLIENT_IDENTIFIER = "dropbox/recirecipe";

    private static final String APP_KEY = "0kj3cb9w27d66n8";
    private static final String APP_SECRET = "7stnncfsyvgim60";
    private static final String AUTHORIZATION_DOMAIN = "https://www.dropbox.com/1/oauth2/authorize?";
    private static final String AUTHORIZATION_URL = AUTHORIZATION_DOMAIN + "response_type=code&client_id="
                                                    + APP_KEY;

    private static final DbxRequestConfig config = DbxRequestConfig.newBuilder(CLIENT_IDENTIFIER).build();

    private static String accessToken = null;
    private static String uploadFilename = null;

    /**
     * Returns true if CloudStorageUtil already has an access token.
     */
    public static boolean hasAccessToken() {
        if (accessToken == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Creates a Dropbox client with the user's {@code getAccessToken()}
     * and uploads file specified by {@code RECIPE_BOOK_FILE} to their Dropbox account
     *
     * @throws DbxException
     */
    public static void upload() throws DbxException {
        // Ensures access token has been obtained
        requireNonNull(CloudStorageUtil.getAccessToken());

        // Create Dropbox client
        DbxClientV2 client = new DbxClientV2(config, CloudStorageUtil.getAccessToken());

        // Upload "recipebook.xml" to Dropbox
        try (InputStream in = new FileInputStream(RECIPE_BOOK_FILE)) {
            client.files().uploadBuilder("/" + uploadFilename)
                    .withAutorename(true)
                    .uploadAndFinish(in);
            System.out.println("File has been uploaded");
        } catch (IOException | DbxException e) {
            throw new DbxException(UploadCommand.MESSAGE_FAILURE + " Make sure you have an Internet connection.");
        }

    }

    /**
     * Takes in the authorization code
     * @param code given by Dropbox after user has allowed access
     * and converts it into the access token that can be used to upload files
     */
    public static void processAuthorizationCode(String code) throws DbxException {
        // Converts authorization code to access token
        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
        DbxWebAuth webAuth = new DbxWebAuth(config, appInfo);
        try {
            DbxAuthFinish authFinish = webAuth.finishFromCode(code);
            accessToken = authFinish.getAccessToken();
        } catch (DbxException e) {
            throw new DbxException(UploadCommand.MESSAGE_FAILURE + " Make sure you have an Internet connection"
                    + " and have entered a  valid authorization code");
        }
        upload();
    }

    public static void setAccessToken(String token) {
        accessToken = token;
    }

    private static String getAccessToken() {
        return accessToken;
    }

    public static void setUploadFilename(String uploadFilename) {
        CloudStorageUtil.uploadFilename = uploadFilename;
    }

    public static String getAuthorizationUrl() {
        return AUTHORIZATION_URL;
    }
}
//@@author
