# RyanAngJY
###### \java\seedu\recipe\commons\util\FileUtilTest.java
``` java
    @Test
    public void isImageFile() {
        // file is not an image file
        File file = new File(MainApp.class.getResource("/view/DarkTheme.css").toExternalForm()
                .substring(5));
        assertFalse(FileUtil.isImageFile(file));

        // file is directory
        file = new File(MainApp.class.getResource("/view").toExternalForm().substring(5));
        assertFalse(FileUtil.isImageFile(file));

        // file is null pointer
        File nullFile = null;
        Assert.assertThrows(NullPointerException.class, () -> FileUtil.isImageFile(nullFile));

        // valid image file
        file = new File(Image.VALID_IMAGE_PATH);
        assertTrue(FileUtil.isImageFile(file));
    }
```
###### \java\seedu\recipe\logic\parser\AddCommandParserTest.java
``` java
        // multiple urls - last url accepted
        assertParseSuccess(parser, NAME_DESC_BOB + PREPARATION_TIME_DESC_BOB + INGREDIENT_DESC_BOB
                + COOKING_TIME_DESC_BOB + SERVINGS_DESC_BOB + CALORIES_DESC_BOB
                + INSTRUCTION_DESC_BOB + URL_DESC_AMY + URL_DESC_BOB + IMG_DESC_BOB
                + TAG_DESC_FRIEND, new AddCommand(expectedRecipe));

        // multiple images - last image accepted
        assertParseSuccess(parser, NAME_DESC_BOB + PREPARATION_TIME_DESC_BOB + INGREDIENT_DESC_BOB
                + COOKING_TIME_DESC_BOB + SERVINGS_DESC_BOB + CALORIES_DESC_BOB
                + INSTRUCTION_DESC_BOB + URL_DESC_BOB + IMG_DESC_AMY + IMG_DESC_BOB
                + TAG_DESC_FRIEND, new AddCommand(expectedRecipe));
```
###### \java\seedu\recipe\logic\parser\AddCommandParserTest.java
``` java
        // invalid url
        assertParseFailure(parser,
                NAME_DESC_BOB + PREPARATION_TIME_DESC_BOB + INGREDIENT_DESC_BOB + INSTRUCTION_DESC_BOB
                        + INVALID_URL_DESC + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                Url.MESSAGE_URL_CONSTRAINTS);
```
###### \java\seedu\recipe\logic\parser\RecipeBookParserTest.java
``` java
    @Test
    public void parseCommand_share() throws Exception {
        ShareCommand command = (ShareCommand) parser.parseCommand(
                ShareCommand.COMMAND_WORD + " " + INDEX_FIRST_RECIPE.getOneBased());
        assertEquals(new ShareCommand(INDEX_FIRST_RECIPE), command);
    }
```
###### \java\seedu\recipe\model\recipe\ImageTest.java
``` java
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
```
###### \java\seedu\recipe\model\recipe\UrlTest.java
``` java
package seedu.recipe.model.recipe;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.recipe.testutil.Assert;

public class UrlTest {
    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Url(null));
    }

    @Test
    public void constructor_invalidUrl_throwsIllegalArgumentException() {
        String invalidUrl = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Url(invalidUrl));
    }

    @Test
    public void isValidUrl() {
        // blank Url
        assertFalse(Url.isValidUrl("")); // empty string
        assertFalse(Url.isValidUrl(" ")); // spaces only

        // invalid Url
        assertFalse(Url.isValidUrl("www.google.com")); // "http://" of "https://" not at the beginning of Url

        // valid Url
        assertTrue(Url.isValidUrl("http://www.google.com")); // "http://" at the beginning of Url
        assertTrue(Url.isValidUrl("https://www.google.com")); // "https://" at the beginning of Url
        assertTrue(Url.isValidUrl(" http://www.google.com")); // leading space
        assertTrue(Url.isValidUrl("http://www.google.com ")); // trailing space
        assertTrue(Url.isValidUrl("https://www.google.com.sg/search"
                + "?ei=1oqfWryFJYvtvgS2kovIDw&q=long+url+trying+to+add+words&oq"
                + "=long+url+trying+to+add+words&gs_l=psy-ab.3...16827.19809.0.19937"
                + ".20.20.0.0.0.0.131.1429.15j3.18.0....0...1c.1.64.psy-ab..2.9.695...0j0"
                + "i20i263k1j0i22i30k1j33i160k1j33i21k1j33i22i29i30k1.0.ToeND2eqJXA")); // long url
    }
}
```
###### \java\seedu\recipe\storage\XmlAdaptedRecipeTest.java
``` java
    @Test
    public void toModelType_invalidUrl_throwsIllegalValueException() {
        XmlAdaptedRecipe recipe =
                new XmlAdaptedRecipe(VALID_NAME, VALID_INGREDIENT, VALID_INSTRUCTION, VALID_COOKING_TIME,
                        VALID_PREPARATION_TIME, VALID_CALORIES, VALID_SERVINGS, INVALID_URL,
                        VALID_IMAGE, VALID_TAGS);
        String expectedMessage = Url.MESSAGE_URL_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, recipe::toModelType);
    }

    @Test
    public void toModelType_nullUrl_throwsIllegalValueException() {
        XmlAdaptedRecipe recipe =
                new XmlAdaptedRecipe(VALID_NAME, VALID_INGREDIENT, VALID_INSTRUCTION, VALID_COOKING_TIME,
                        VALID_PREPARATION_TIME, VALID_CALORIES, VALID_SERVINGS, null,
                        VALID_IMAGE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Url.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, recipe::toModelType);
    }

    @Test
    public void toModelType_nullImage_throwsIllegalValueException() {
        XmlAdaptedRecipe recipe =
                new XmlAdaptedRecipe(VALID_NAME, VALID_INGREDIENT, VALID_INSTRUCTION, VALID_COOKING_TIME,
                        VALID_PREPARATION_TIME, VALID_CALORIES, VALID_SERVINGS, VALID_URL,
                        null, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Image.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, recipe::toModelType);
    }


    @Test
    public void toModelType_invalidImage_setsImageToNullReference() {
        Recipe recipe = null;
        try {
            recipe = new XmlAdaptedRecipe(VALID_NAME, VALID_INGREDIENT, VALID_INSTRUCTION, VALID_COOKING_TIME,
                    VALID_PREPARATION_TIME, VALID_CALORIES, VALID_SERVINGS, VALID_URL,
                    INVALID_IMAGE, VALID_TAGS).toModelType();
        } catch (Exception e) {
            System.out.println("Unable to create recipe");
        }
        assertTrue(recipe.getImage().toString().equals(Image.NULL_IMAGE_REFERENCE));
    }
```
###### \java\seedu\recipe\storage\XmlAdaptedRecipeTest.java
``` java


    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<XmlAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new XmlAdaptedTag(INVALID_TAG));
        XmlAdaptedRecipe recipe =
            new XmlAdaptedRecipe(VALID_NAME, VALID_INGREDIENT, VALID_INSTRUCTION, VALID_COOKING_TIME,
                    VALID_PREPARATION_TIME, VALID_CALORIES, VALID_SERVINGS, VALID_URL,
                    VALID_IMAGE, invalidTags);
        Assert.assertThrows(IllegalValueException.class, recipe::toModelType);
    }

}
```
###### \java\seedu\recipe\testutil\EditRecipeDescriptorBuilder.java
``` java
    /**
     * Sets the {@code Url} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditRecipeDescriptorBuilder withUrl(String url) {
        descriptor.setUrl(new Url(url));
        return this;
    }

    /**
     * Sets the {@code Url} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditRecipeDescriptorBuilder withImage(String image) {
        descriptor.setImage(new Image(image));
        return this;
    }
```
###### \java\seedu\recipe\testutil\RecipeBuilder.java
``` java
    /**
     * Sets the {@code Url} of the {@code Recipe} that we are building.
     */
    public RecipeBuilder withUrl(String url) {
        this.url = new Url(url);
        return this;
    }

    /**
     * Sets the {@code Url} of the {@code Recipe} that we are building.
     */
    public RecipeBuilder withImage(String image) {
        this.image = new Image(image);
        return this;
    }
```
###### \java\seedu\recipe\ui\testutil\FacebookHandlerTest.java
``` java
public class FacebookHandlerTest {
    public static final String ACCESS_TOKEN_STUB = "123";
    public static final String VALID_EMBEDDED_ACCESS_TOKEN = REDIRECT_DOMAIN
            + ACCESS_TOKEN_IDENTIFIER + ACCESS_TOKEN_STUB + "&";
    public static final String INVALID_EMBEDDED_ACCESS_TOKEN = REDIRECT_DOMAIN
            + ACCESS_TOKEN_STUB; // without token identifier
    @Test
    public void hasAccessToken() {
        FacebookHandler.setAccessToken(null);
        assertFalse(FacebookHandler.hasAccessToken());

        FacebookHandler.setAccessToken(ACCESS_TOKEN_STUB);
        assertTrue(FacebookHandler.hasAccessToken());
    }

    @Test
    public void extractAccessToken() {
        assertTrue(FacebookHandler.extractAccessToken(VALID_EMBEDDED_ACCESS_TOKEN)
                .equals(ACCESS_TOKEN_STUB));
    }

    @Test
    public void checkAndSetAccessToken() {
        assertTrue(FacebookHandler.checkAndSetAccessToken(VALID_EMBEDDED_ACCESS_TOKEN));
        assertFalse(FacebookHandler.checkAndSetAccessToken(INVALID_EMBEDDED_ACCESS_TOKEN));
    }

    @Test
    public void getPostDomain() {
        assertEquals(FacebookHandler.getPostDomain(), FacebookHandler.POST_DOMAIN);
    }

    @Test
    public void getRedirectEmbedded() {
        assertEquals(FacebookHandler.getRedirectEmbedded(), FacebookHandler.REDIRECT_EMBEDDED);
    }
}
```
###### \java\seedu\recipe\ui\UiUtilTest.java
``` java
package seedu.recipe.ui;

import static org.junit.Assert.assertTrue;
import static seedu.recipe.ui.util.UiUtil.HEX_COLOR_BLACK;
import static seedu.recipe.ui.util.UiUtil.HEX_COLOR_WHITE;

import java.util.regex.Pattern;

import org.junit.Test;

import seedu.recipe.ui.util.UiUtil;

public class UiUtilTest {

    private static final String LIGHT_COLOR_CODE = "#FFFFFF";
    private static final String DARK_COLOR_CODE = "#000000";
    private static final String VALID_STRING = "This is a valid string";

    @Test
    public void convertStringToValidColorCode() {

        // empty string
        assertTrue(Pattern.matches(UiUtil.HEX_COLOR_REGEX,
                UiUtil.convertStringToValidColorCode("")));

        // valid hexadecimal
        assertTrue(Pattern.matches(UiUtil.HEX_COLOR_REGEX,
                UiUtil.convertStringToValidColorCode("01fb45")));

        // valid hexadecimal with random whitespaces
        assertTrue(Pattern.matches(UiUtil.HEX_COLOR_REGEX,
                UiUtil.convertStringToValidColorCode("  cd  eff  f")));

        // valid hexadecimal with varying lengths
        assertTrue(Pattern.matches(UiUtil.HEX_COLOR_REGEX,
                UiUtil.convertStringToValidColorCode("1a2b")));
        assertTrue(Pattern.matches(UiUtil.HEX_COLOR_REGEX,
                UiUtil.convertStringToValidColorCode("1a2bfc124ab")));

        // valid hexadecimal with varying lengths and with random whitespaces
        assertTrue(Pattern.matches(UiUtil.HEX_COLOR_REGEX,
                UiUtil.convertStringToValidColorCode("  1  a  2b ")));
        assertTrue(Pattern.matches(UiUtil.HEX_COLOR_REGEX,
                UiUtil.convertStringToValidColorCode(" 1a2 bfc12  4ab ")));

        // invalid hexadecimal
        assertTrue(Pattern.matches(UiUtil.HEX_COLOR_REGEX,
                UiUtil.convertStringToValidColorCode("i#tov129nfoiZZ!!Za")));

        // invalid hexadecimal with random whitespaces
        assertTrue(Pattern.matches(UiUtil.HEX_COLOR_REGEX,
                UiUtil.convertStringToValidColorCode(" i t#ov   129 nfoi  ZZ !! Za ")));
    }

    @Test
    public void getMatchingColorFromGivenColor() {
        // invalid CSS color code
        assertTrue(HEX_COLOR_BLACK.equals(UiUtil.getMatchingColorFromGivenColor("asdio 42oi n")));

        // light CSS color code
        assertTrue(HEX_COLOR_BLACK.equals(UiUtil.getMatchingColorFromGivenColor(LIGHT_COLOR_CODE)));

        // dark CSS color code
        assertTrue(HEX_COLOR_WHITE.equals(UiUtil.getMatchingColorFromGivenColor(DARK_COLOR_CODE)));
    }
}
```
