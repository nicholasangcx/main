//@@author nicholasangcx
package seedu.recipe.ui.testutil;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.dropbox.core.DbxException;

import seedu.recipe.ui.util.CloudStorageUtil;

public class CloudStorageUtilTest {

    private static final String WRONG_AUTHORIZATION_CODE = "abcdefg";
    private static final String ACCESS_TOKEN_STUB = "adjhsj";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void hasAccessToken() {
        CloudStorageUtil.setAccessToken(null);
        assertFalse(CloudStorageUtil.hasAccessToken());

        CloudStorageUtil.setAccessToken(ACCESS_TOKEN_STUB);
        assertTrue(CloudStorageUtil.hasAccessToken());
    }

    @Test
    public void processInvalidAuthorizationCode() throws DbxException {
        thrown.expect(DbxException.class);
        CloudStorageUtil.processAuthorizationCode(WRONG_AUTHORIZATION_CODE);
        assertFalse(CloudStorageUtil.hasAccessToken());
    }
}
//@@author
